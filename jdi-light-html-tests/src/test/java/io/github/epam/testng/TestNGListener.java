package io.github.epam.testng;

/**
 * Created by Roman Iovlev on 14.02.2018
 * Email: roman.iovlev.jdi@gmail.com; Skype: roman.iovlev
 */

import com.epam.jdi.light.settings.WebSettings;
import com.epam.jdi.tools.Safe;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.epam.jdi.light.driver.ScreenshotMaker.takeScreen;
import static com.epam.jdi.light.settings.WebSettings.getWebSettings;
import static java.lang.System.currentTimeMillis;

public class TestNGListener implements IInvokedMethodListener {
    private final Safe<Long> start = new Safe<>(0L);
    private final WebSettings webSettings = getWebSettings();

    @Override
    public void beforeInvocation(IInvokedMethod m, ITestResult tr) {
        if (m.isTestMethod()) {
            Method testMethod = m.getTestMethod().getConstructorOrMethod().getMethod();
            if (testMethod.isAnnotationPresent(Test.class)) {
                webSettings.TEST_NAME.set(tr.getTestClass().getRealClass().getSimpleName() + "." + testMethod.getName());
                start.set(currentTimeMillis());
                webSettings.logger.step("== Test '%s' START ==", webSettings.TEST_NAME.get());
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult r) {
        if (method.isTestMethod()) {
            String result = getTestResult(r);
            webSettings.logger.step("=== Test '%s' %s [%s] ===", webSettings.TEST_NAME.get(), result,
                    new SimpleDateFormat("mm:ss.SS").format(new Date(currentTimeMillis() - start.get())));
            if (result.equals("FAILED")) {
                takeScreen();
                webSettings.logger.step("ERROR: " + r.getThrowable().getMessage());
            }
            webSettings.logger.step("");
        }
    }

    private String getTestResult(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                return "PASSED";
            case ITestResult.SKIP:
                return "SKIPPED";
            default:
                return "FAILED";
        }
    }

}