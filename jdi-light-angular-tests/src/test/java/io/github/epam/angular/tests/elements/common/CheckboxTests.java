package io.github.epam.angular.tests.elements.common;

import io.github.epam.TestsInit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.github.com.StaticSite.angularPage;
import static io.github.com.pages.sections.CheckboxSection.*;
import static io.github.epam.site.steps.States.shouldBeLoggedIn;

public class CheckboxTests extends TestsInit {

    @BeforeMethod
    public void before() {
        shouldBeLoggedIn();
        angularPage.shouldBeOpened();
    }

    @Test
    public void basicCheckboxTest() {
        basicCheckbox.isDisplayed();
        basicCheckbox.check();
        basicCheckbox.isSelected();
        basicCheckbox.uncheck();
        basicCheckbox.is().deselected();
    }

    @Test
    public void configurableCheckboxTest() {
        configurableCheckedCheckbox.show();
        configurableCheckedCheckbox.check();
        configurableResultCheckbox.isSelected();
        configurableCheckedCheckbox.uncheck();
        configurableResultCheckbox.is().deselected();

        configurableIndeterminateCheckbox.check();
        configurableResultCheckbox.has().cssClass("mat-checkbox-indeterminate");

        configurableCheckboxAlignBeforeRadioButton.click();
        configurableResultCheckbox.has().cssClass("mat-checkbox-label-before");

        configurableCheckboxAlignAfterRadioButton.click();
        // TODO FIX TEST
        //configurableResultCheckbox.has().cssClass("mat-checkbox-label-after");

        configurableDisabledCheckbox.click();
        configurableResultCheckbox.is().disabled();
    }
}
