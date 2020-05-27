package com.epam.jdi.light.settings;

import com.epam.jdi.light.common.ElementArea;
import com.epam.jdi.light.common.SetTextTypes;
import com.epam.jdi.light.common.TextTypes;
import com.epam.jdi.light.common.UseSmartSearch;
import com.epam.jdi.light.common.VisualCheckAction;
import com.epam.jdi.light.common.VisualCheckPage;
import com.epam.jdi.light.driver.WebDriverFactory;
import com.epam.jdi.light.driver.get.DriverTypes;
import com.epam.jdi.light.elements.common.UIElement;
import com.epam.jdi.light.elements.interfaces.base.IBaseElement;
import com.epam.jdi.light.elements.interfaces.composite.PageObject;
import com.epam.jdi.light.logger.ILogger;
import com.epam.jdi.tools.PropReader;
import com.epam.jdi.tools.PropertyReader;
import com.epam.jdi.tools.Safe;
import com.epam.jdi.tools.func.JAction;
import com.epam.jdi.tools.func.JAction1;
import com.epam.jdi.tools.func.JFunc;
import com.epam.jdi.tools.func.JFunc1;
import com.epam.jdi.tools.pairs.Pair;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.epam.jdi.light.common.ElementArea.CENTER;
import static com.epam.jdi.light.common.Exceptions.exception;
import static com.epam.jdi.light.common.NameToLocator.SMART_MAP_NAME_TO_LOCATOR;
import static com.epam.jdi.light.common.SearchStrategies.inView;
import static com.epam.jdi.light.common.SearchStrategies.noValidation;
import static com.epam.jdi.light.common.SearchStrategies.onlyVisible;
import static com.epam.jdi.light.common.SearchStrategies.visibleEnabled;
import static com.epam.jdi.light.common.SetTextTypes.SET_TEXT;
import static com.epam.jdi.light.common.TextTypes.SMART_TEXT;
import static com.epam.jdi.light.common.UseSmartSearch.ALWAYS;
import static com.epam.jdi.light.common.UseSmartSearch.FALSE;
import static com.epam.jdi.light.common.UseSmartSearch.ONLY_UI;
import static com.epam.jdi.light.common.UseSmartSearch.UI_AND_ELEMENTS;
import static com.epam.jdi.light.driver.WebDriverFactory.getWebDriverFactory;
import static com.epam.jdi.light.driver.get.RemoteDriver.browserstack;
import static com.epam.jdi.light.driver.get.RemoteDriver.sauceLabs;
import static com.epam.jdi.light.driver.get.RemoteDriver.seleniumLocalhost;
import static com.epam.jdi.light.driver.sauce.SauceSettings.sauceCapabilities;
import static com.epam.jdi.light.elements.init.UIFactory.$;
import static com.epam.jdi.light.logger.JDILogger.instance;
import static com.epam.jdi.light.logger.Strategy.parseStrategy;
import static com.epam.jdi.light.settings.CommonSettings.getCommonSettings;
import static com.epam.jdi.light.settings.JDISettings.getJDISettings;
import static com.epam.jdi.light.settings.Strategies.JDI;
import static com.epam.jdi.light.settings.Strategies.JDI_STABLE;
import static com.epam.jdi.light.settings.Strategies.SELENIUM;
import static com.epam.jdi.tools.EnumUtils.getAllEnumValues;
import static com.epam.jdi.tools.LinqUtils.first;
import static com.epam.jdi.tools.LinqUtils.map;
import static com.epam.jdi.tools.PathUtils.mergePath;
import static com.epam.jdi.tools.PrintUtils.print;
import static com.epam.jdi.tools.PropertyReader.getProperty;
import static com.epam.jdi.tools.ReflectionUtils.isInterface;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.openqa.selenium.PageLoadStrategy.EAGER;
import static org.openqa.selenium.PageLoadStrategy.NORMAL;

/**
 * Created by Roman Iovlev on 14.02.2018
 * Email: roman.iovlev.jdi@gmail.com; Skype: roman.iovlev
 */
public class WebSettings {
    private static WebSettings webSettings;
    private static final JDISettings jdiSettings = getJDISettings();

    public ILogger logger;
    public VisualCheckAction VISUAL_ACTION_STRATEGY;
    public VisualCheckPage VISUAL_PAGE_STRATEGY;
    public boolean STRICT_SEARCH;

    public String TEST_GROUP = "";
    // TODO multi properties example
    public Safe<String> TEST_NAME;

    public boolean initialized;

    public static JFunc1<IBaseElement, WebElement> SMART_SEARCH = el -> {
        switch (jdiSettings.ELEMENT.useSmartSearch) {
            case FALSE:
                return null;
            case ONLY_UI:
                if (el.base().locator.isNull()) {
                    return null;
                }
                break;
            case UI_AND_ELEMENTS:
                if (el.base().locator.isNull() && isInterface(el.getClass(), PageObject.class)) {
                    return null;
                }
                break;
        }
        String locatorName = jdiSettings.ELEMENT.smartName.value.execute(el.getName());
        return el.base().timer().getResult(() -> {
            String locator = format(jdiSettings.ELEMENT.smartTemplate, locatorName);
            UIElement ui = (("#%s").equals(jdiSettings.ELEMENT.smartTemplate)
                    ? $(locator)
                    : $(locator, el.base().parent))
                    .setup(e -> e.setName(el.getName()).noWait());
            try {
                return ui.getWebElement();
            } catch (Exception ignore) {
                throw exception("Element '%s' has no locator and Smart Search failed (%s). " +
                                "Please add locator to the element or make sure that the element can be found using Smart Search",
                        el.getName(),
                        getWebSettings().printSmartLocators(el));
            }
        });
    };

    private void fillAction(JAction1<String> action, String name) {
        String prop = null;
        try {
            prop = getProperty(name);
        } catch (Exception ignore) {}
        if (prop == null) {
            prop = "null";
        }
        logger.debug("fillAction(%s=%s)", name, prop);
        PropertyReader.fillAction(action, name);
    }

    private WebSettings() {
        logger = instance("JDI");
        VISUAL_ACTION_STRATEGY = VisualCheckAction.NONE;
        VISUAL_PAGE_STRATEGY = VisualCheckPage.NONE;
        STRICT_SEARCH = true;
        TEST_NAME = new Safe<>((String) null);
        initialized = false;
    }

    public static WebSettings getWebSettings() {
        if (webSettings == null) {
            webSettings = new WebSettings();
        }
        return webSettings;
    }

    public String getDomain() {
        if (getJDISettings().DRIVER.domain != null)
            return getJDISettings().DRIVER.domain;
        init();
        return "No Domain Found. Use test.properties or JDISettings.DRIVER.domain";
    }

    public void setDomain(String domain) {
        getJDISettings().DRIVER.domain = domain;
    }

    public boolean hasDomain() {
        init();
        return jdiSettings.DRIVER.domain != null && jdiSettings.DRIVER.domain.contains("://");
    }

    public String useDriver(JFunc<WebDriver> driver) {
        return getWebDriverFactory().useDriver(driver);
    }

    public String useDriver(String driverName) {
        return getWebDriverFactory().useDriver(driverName);
    }

    public String useDriver(DriverTypes driverType) {
        return getWebDriverFactory().useDriver(driverType);
    }

    public String printSmartLocators(IBaseElement el) {
        try {
            return "smart: " + format(getJDISettings().ELEMENT.smartTemplate, getJDISettings().ELEMENT.smartName.value.execute(el.getName()));
        } catch (Exception ex) {
            return format("Can't define smart locator(%s, %s)", getJDISettings().ELEMENT.smartTemplate, getJDISettings().ELEMENT.smartName.key);
        }
    }


    public synchronized void init() {
        if (initialized) return;
        logger.debug("init()");
        try {
            getProperties(getCommonSettings().testPropertiesPath);
            TestProperties.getTestsProperties().forEach((k, v) ->
            {
                String propertyName = k;
                JAction1<String> setPropertyAction = v.key;
                JAction actionAfterPropertyLoading = v.value;
                fillAction(setPropertyAction, propertyName);
                actionAfterPropertyLoading.execute();
			});

            loadCapabilities("chrome.capabilities.path", "chrome.properties",
                    p -> p.forEach((key, value) -> getJDISettings().DRIVER.capabilities.chrome.put(key.toString(), value.toString())));
            loadCapabilities("ff.capabilities.path", "ff.properties",
                    p -> p.forEach((key, value) -> getJDISettings().DRIVER.capabilities.firefox.put(key.toString(), value.toString())));
            loadCapabilities("ie.capabilities.path", "ie.properties",
                    p -> p.forEach((key, value) -> getJDISettings().DRIVER.capabilities.ie.put(key.toString(), value.toString())));
            loadCapabilities("edge.capabilities.path", "edge.properties",
                    p -> p.forEach((key, value) -> getJDISettings().DRIVER.capabilities.ieEdge.put(key.toString(), value.toString())));
            loadCapabilities("opera.capabilities.path", "opera.properties",
                    p -> p.forEach((key, value) -> getJDISettings().DRIVER.capabilities.opera.put(key.toString(), value.toString())));
            loadCapabilities("safari.capabilities.path", "safari.properties",
                    p -> p.forEach((key, value) -> getJDISettings().DRIVER.capabilities.safari.put(key.toString(), value.toString())));
            loadCapabilities("common.capabilities.path", "common.properties",
                    p -> p.forEach((key, value) -> getJDISettings().DRIVER.capabilities.common.put(key.toString(), value.toString())));

            getWebDriverFactory().setInitThreadId(Thread.currentThread().getId());
            initialized = true;
        } catch (Throwable ex) {
            throw exception(ex, "Failed to init test.properties");
        }
    }

    private ElementArea getClickType(String type) {
        ElementArea clickType = first(getAllEnumValues(ElementArea.class),
                t -> t.toString().equals(type));
        return clickType != null
                ? clickType : CENTER;
    }

    private TextTypes getTextType(String type) {
        TextTypes textType = first(getAllEnumValues(TextTypes.class),
                t -> t.toString().equals(type));
        return textType != null
                ? textType : SMART_TEXT;
    }

    private SetTextTypes getSetTextType(String type) {
        SetTextTypes textType = first(getAllEnumValues(SetTextTypes.class),
                t -> t.toString().equals(type));
        return textType != null
                ? textType : SET_TEXT;
    }

    private String getRemoteUrl(String prop) {
        switch (prop.toLowerCase().replaceAll(" ", "")) {
            case "sauce":
            case "saucelabs":
                getJDISettings().DRIVER.capabilities.common = sauceCapabilities();
                return sauceLabs();
            case "browserstack":
                return browserstack();
            default:
                return seleniumLocalhost();
        }
    }

    private Pair<String, JFunc1<String, String>> getSmartSearchFunc(String name) {
        if (!SMART_MAP_NAME_TO_LOCATOR.keys().contains(name)) {
            throw exception("Unknown JDISettings.ELEMENT.smartName: '%s'. Please correct value 'smart.locators.toName' in test.properties." +
                    "Available names: [%s]", name, print(SMART_MAP_NAME_TO_LOCATOR.keys()));
        }
        return Pair.$(name, SMART_MAP_NAME_TO_LOCATOR.get(name));
    }

    private UseSmartSearch getSmartSearchUse(String prop) {
        String propLower = prop.toLowerCase().trim().replaceAll(" ", "");
        switch (propLower) {
            case "false":
                return FALSE;
            case "onlyui":
                return ONLY_UI;
            case "uiandelements":
                return UI_AND_ELEMENTS;
            case "always":
                return ALWAYS;
            default:
                return UI_AND_ELEMENTS;
        }
    }

    private boolean getBoolean(String param) {
        String lowerParams = param.toLowerCase();
        return !lowerParams.equals("off") && !lowerParams.equals("false");
    }

    public void loadCapabilities(String property, String defaultPath, JAction1<Properties> setCapabilities) {
        String path = "";
        try {
            path = System.getProperty(property, getProperty(property));
        } catch (Exception ignore) {
        }
        if (isEmpty(path))
            path = defaultPath;
        Properties properties = new PropReader(path).getProperties();
        if (properties.isEmpty())
            return;
        try {
            setCapabilities.execute(properties);
        } catch (Exception ignore) {
        }
    }

    private void setSearchStrategy(String param) {
        String p = param.toLowerCase();
        if ("soft".equals(p)) {
            p = "any, multiple";
        }
        if ("strict".equals(p)) {
            p = "visible, single";
        }
        if (p.split(",").length == 2) {
            List<String> params = asList(p.split(","));
            if (params.contains("visible") || params.contains("displayed")) {
                onlyVisible();
            }
            if (params.contains("any") || params.contains("all")) {
                noValidation();
            }
            if (params.contains("enabled")) {
                visibleEnabled();
            }
            if (params.contains("inview")) {
                inView();
            }
            if (params.contains("single")) {
                STRICT_SEARCH = true;
            }
            if (params.contains("multiple")) {
                STRICT_SEARCH = false;
            }
        }
    }

    private PageLoadStrategy getPageLoadStrategy(String strategy) {
        switch (strategy.toLowerCase()) {
            case "normal":
                return NORMAL;
            case "none":
                return PageLoadStrategy.NONE;
            case "eager":
                return EAGER;
        }
        return NORMAL;
    }

    private Properties getProperties(String path) {
        File propertyFile = new File(path);
        Properties properties;
        if (propertyFile.exists()) {
            properties = getCiProperties(path, propertyFile);
        } else {
            Properties pTest = PropertyReader.getProperties(mergePath(path));
            Properties pTarget = PropertyReader.getProperties(mergePath("/../../target/classes/" + path));
            if (pTarget.size() > 0)
                return pTarget;
            String propertiesPath = pTest.size() > 0
                    ? path
                    : mergePath("/../../target/classes/" + path);
            properties = PropertyReader.getProperties(propertiesPath);
        }
        return properties;
    }

    private Properties getCiProperties(String path, File propertyFile) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propertyFile));
            System.out.println("Property file found: " + propertyFile.getAbsolutePath());
        } catch (IOException ex) {
            throw exception("Couldn't load properties for CI Server" + path);
        }
        return properties;
    }

    private static LogInfoDetails getInfoDetailsLevel(String option) {
        switch (option.toLowerCase()) {
            case "none":
                return LogInfoDetails.NONE;
            case "name":
                return LogInfoDetails.NAME;
            case "locator":
                return LogInfoDetails.LOCATOR;
            case "context":
                return LogInfoDetails.CONTEXT;
            case "element":
                return LogInfoDetails.ELEMENT;
            default:
                return LogInfoDetails.ELEMENT;
        }
    }

    private static List<com.epam.jdi.light.logger.Strategy> getLoggerStrategy(String strategy) {
        if (isBlank(strategy)) {
            return new ArrayList<>();
        }
        List<com.epam.jdi.light.logger.Strategy> strategies = new ArrayList<>();
        try {
            String[] split = strategy.split(";");
            strategies = map(split, s -> parseStrategy(s.trim()));
        } catch (Exception ignore) {
        }
        return strategies;
    }

    private Strategies getStrategy(String prop) {
        String strategy = prop.trim().toLowerCase();
        switch (strategy) {
            case "jdi":
                return JDI;
            case "jdi-stable":
                return JDI_STABLE;
            case "selenium":
                return SELENIUM;
            default:
                return JDI;
        }
    }
}
