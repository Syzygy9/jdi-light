package com.epam.jdi.light.ui.html.elements.complex;

import com.epam.jdi.light.common.JDIAction;
import com.epam.jdi.light.common.ListElementNameTypes;
import com.epam.jdi.light.elements.common.UIElement;
import com.epam.jdi.light.elements.complex.WebList;

import java.util.List;

import static com.epam.jdi.light.common.ListElementNameTypes.*;
import static com.epam.jdi.light.elements.init.UIFactory.$$;
import static com.epam.jdi.tools.EnumUtils.getEnumValue;
import static com.epam.jdi.tools.LinqUtils.ifSelect;

// Implements TextField + Droplist
// https://www.w3schools.com/tags/tryit.asp?filename=tryhtml5_datalist
public class DataListOptions extends Combobox {

    @Override
    public WebList list() {
        return  $$("#"+ uiElement.attr("list")+" option")
                .setup(e->e.noValidation().setName(getName() + "list"))
                .setUIElementName(VALUE);
    }
    /*List<String> list() {
        return dataList().values();
    }*/

    /**
     * Selects the value
     * @param value String to select
     */
    @JDIAction("Select '{0}' for '{name}''") @Override
    public void select(String value) {
        super.setText(value);
    }

    /**
     * Selects the value based on its index
     * @param index int to search
     */
    @JDIAction("Select '{0}' for '{name}'") @Override
    public void select(int index) {
        setText(values().get(index-1));
    }

    /**
     * Gets selected option
     * @return String
     */
    @JDIAction("Get selected in '{name}' option") @Override
    public String selected() {
        return uiElement.attr("value");
    }

    /**
     * Gets all options
     * @return List<String>
     */
    @JDIAction("Get all '{name}' options") @Override
    public List<String> values() {
        return list().values();
    }

    /**
     * Gets all enabled options
     * @return List<String>
     */
    @JDIAction("Get all '{name}' enabled options") @Override
    public List<String> listEnabled() {
        return ifSelect(list(), UIElement::isEnabled, UIElement::getText);
    }


    /**
     * Gets all disabled options
     * @return List<String>
     */
    @JDIAction("Get all '{name}' disabled options") @Override
    public List<String> listDisabled() {
        return ifSelect(list(), UIElement::isDisabled, UIElement::getText);
    }

    @JDIAction("Check that '{name}' is enabled") @Override
    public boolean isEnabled() {
        return !core().hasAttribute("disabled");
    }
}
