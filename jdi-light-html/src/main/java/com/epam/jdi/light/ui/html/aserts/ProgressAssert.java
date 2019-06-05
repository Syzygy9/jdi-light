package com.epam.jdi.light.ui.html.aserts;

import com.epam.jdi.light.asserts.CommonAssert;
import com.epam.jdi.light.asserts.UIAssert;
import com.epam.jdi.light.common.JDIAction;
import com.epam.jdi.light.ui.html.asserts.HtmlAssertion;
import com.epam.jdi.light.ui.html.common.ProgressBar;
import com.epam.jdi.light.ui.html.common.Range;
import org.hamcrest.Matcher;

import static com.epam.jdi.light.asserts.SoftAssert.jdiAssert;
import static com.epam.jdi.light.ui.html.utils.HtmlUtils.getInt;
import static org.hamcrest.Matchers.is;

/**
 * Created by Roman Iovlev on 14.02.2018
 * Email: roman.iovlev.jdi@gmail.com; Skype: roman.iovlev
 */

public class ProgressAssert extends UIAssert<ProgressAssert, ProgressBar> {
    @JDIAction("Assert that '{name}' max volume {0}")
    public ProgressAssert maxVolume(Matcher<Integer> max) {
        jdiAssert(getInt("max", uiElement.element), max);
        return this;
    }
    public ProgressAssert maxVolume(int maxVolume) { return maxVolume(is(maxVolume)); }

    @JDIAction("Assert that '{name}' volume {0}")
    public ProgressAssert volume(Matcher<Integer> volume) {
        jdiAssert(getInt("value", uiElement.element), volume);
        return this;
    }
    public ProgressAssert volume(int volume) { return volume(is(volume)); }
}