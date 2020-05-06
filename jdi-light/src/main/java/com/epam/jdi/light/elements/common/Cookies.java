package com.epam.jdi.light.elements.common;

import com.epam.jdi.light.common.JDIAction;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

import static com.epam.jdi.light.driver.WebDriverFactory.getWebDriverFactory;

/**
 * Created by Roman Iovlev on 26.09.2019
 * Email: roman.iovlev.jdi@gmail.com; Skype: roman.iovlev
 */
public class Cookies {

    private static WebDriver.Options manage() {
        return getWebDriverFactory().getDriver().manage();
    }

    /**
     * Get all browser cookies
     *
     * @return Set
     */
    @JDIAction("Get site cookies")
    public static Set<Cookie> getCookies() {
        return manage().getCookies();
    }

    /**
     * Get cookie by name
     * @param value Get cookie by name
     */
    @JDIAction("Get cookie '{0}'")
    public static Cookie getCookie(String value) {
        return manage().getCookieNamed(value);
    }
    /**
     * @param cookie Specify cookie
     *               Add cookie in browser
     */
    @JDIAction("Add cookie '{0}'")
    public static void addCookie(Cookie cookie) {
        manage().addCookie(cookie);
    }

    /**
     * Clear browsers cache
     */
    @JDIAction("Delete all cookies")
    public static void clearAllCookies() {
        manage().deleteAllCookies();
    }
}
