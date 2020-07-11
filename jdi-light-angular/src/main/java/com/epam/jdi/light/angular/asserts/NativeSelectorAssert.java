package com.epam.jdi.light.angular.asserts;

import com.epam.jdi.light.angular.elements.complex.NativeSelector;
import com.epam.jdi.light.asserts.generic.UIAssert;
import com.epam.jdi.light.common.JDIAction;
import com.epam.jdi.light.common.TextTypes;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.epam.jdi.light.asserts.core.SoftAssert.jdiAssert;
import static com.epam.jdi.tools.EnumUtils.getEnumValue;
import static com.epam.jdi.tools.LinqUtils.toStringArray;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

public class NativeSelectorAssert extends UIAssert<NativeSelectorAssert, NativeSelector> {
    @JDIAction("Assert that '{0}' option selected for '{name}'")
    public NativeSelectorAssert selected(final Matcher<String> condition) {
        jdiAssert(element().selected(), condition);
        return this;
    }

    @JDIAction("Assert that '{0}' option selected for '{name}'")
    public NativeSelectorAssert selected(final String option) {
        return selected(Matchers.is(option));
    }

    public <TEnum extends Enum<?>> NativeSelectorAssert selected(final TEnum option) {
        return selected(getEnumValue(option));
    }

    public <TEnum extends Enum<?>> NativeSelectorAssert value(final TEnum option) {
        jdiAssert(element().values(), hasItem(getEnumValue(option)));
        return this;
    }

    @JDIAction("Assert that '{name}' values {0}")
    public NativeSelectorAssert value(final Matcher<String> condition) {
        return values(hasItem(condition));
    }

    @JDIAction("Assert that '{name}' has value {0}")
    public NativeSelectorAssert value(final String value) {
        return values(hasItem(value));
    }

    @JDIAction("Assert that '{name}' values {0}")
    public NativeSelectorAssert values(final Matcher<? super List<String>> condition) {
        jdiAssert(element().values(), condition);
        return this;
    }

    public NativeSelectorAssert values(final String... values) {
        return values(hasItems(values));
    }

    public NativeSelectorAssert values(final List<String> values) {
        return values(toStringArray(values));
    }

    @JDIAction("Assert that '{name}' values {0}")
    public NativeSelectorAssert values(final TextTypes type, final Matcher<? super List<String>> condition) {
        jdiAssert(element().values(type), condition);
        return this;
    }

    public NativeSelectorAssert values(final TextTypes type, final String... values) {
        return values(type, hasItems(values));
    }

    @JDIAction("Assert that '{name}' has groups {0}")
    public NativeSelectorAssert groups(final List<String> groups) {
        jdiAssert(element.groups(), Matchers.is(groups));
        return this;
    }

    @JDIAction("Assert that '{name}' has groups and options {0}")
    public NativeSelectorAssert groupsAndOptions(final Map<String, List<String>> expectedGroupsAndOptions) {
        jdiAssert(element.groupsAndOptions(), Matchers.is(expectedGroupsAndOptions));
        return this;
    }

    @JDIAction("Assert that '{name}' has enabled values {0}")
    public NativeSelectorAssert listEnabled(final List<String> listEnabled) {
        jdiAssert(element.listEnabled(), Matchers.is(listEnabled));
        return this;
    }

    @JDIAction("Assert that '{name}' has disabled values {0}")
    public NativeSelectorAssert listDisabled(final List<String> listDisabled) {
        jdiAssert(element.listDisabled(), Matchers.is(listDisabled));
        return this;
    }

    @JDIAction("Assert that '{name}' has no disabled values")
    public NativeSelectorAssert emptyDisabled() {
        jdiAssert(element.listDisabled(), Matchers.is(Collections.EMPTY_LIST));
        return this;
    }
}
