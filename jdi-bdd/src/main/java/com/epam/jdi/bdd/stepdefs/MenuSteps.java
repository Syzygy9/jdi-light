package com.epam.jdi.bdd.stepdefs;

import static com.epam.jdi.light.elements.init.entities.collection.EntitiesCollection.getUI;

import com.epam.jdi.bdd.elements.CustomMenu;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.List;

/**
 * Created by Roman Iovlev on 26.09.2019 Email: roman.iovlev.jdi@gmail.com; Skype: roman.iovlev
 */
public class MenuSteps {

    public static CustomMenu menu(String name) {
        return getUI(name, CustomMenu.class);
    }

    @When("^(?:I |)select \"([^\"]*)\" in \"([^\"]*)\" menu$")
    public void select(String value, String name) {
        menu(name).select(value);
    }

    @When("^(?:I |)select items in \"([^\"]*)\" menu:$")
    public void selectItems(String name, List<String> values) {
        for (String item : values)
            menu(name).select(item);
    }

    @Then("^the \"([^\"]*)\" in \"([^\"]*)\" menu is selected$")
    public void isSelected(String value, String name) {
        menu(name).is().selected(value);
    }
}
