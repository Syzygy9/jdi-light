package io.github.epam.bootstrap.tests.common;

import static com.epam.jdi.light.common.Exceptions.safeException;
import static com.epam.jdi.light.elements.common.Alerts.validateAlert;
import static io.github.com.StaticSite.bsPage;
import static io.github.com.pages.BootstrapPage.disabledButton;
import static io.github.com.pages.BootstrapPage.doubleButton;
import static io.github.com.pages.BootstrapPage.redButton;
import static io.github.epam.bootstrap.tests.BaseValidationsUtils.baseValidation;
import static io.github.epam.states.States.shouldBeLoggedIn;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import io.github.epam.TestsInit;
import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by Roman Iovlev on 12.09.2019
 * Email: roman.iovlev.jdi@gmail.com; Skype: roman.iovlev
 */

public class ButtonTests implements TestsInit {
    final String text = "Red button";

    @BeforeMethod
    public void before() {
        shouldBeLoggedIn();
        bsPage.shouldBeOpened();
        redButton.show();
    }

    @Test
    public void getTextTest() {
        assertEquals(redButton.getText(), text);
    }

    @Test
    public void getValueTest() {
        assertEquals(redButton.getValue(), text);
    }

    @Test
    public void clickTest() {
        redButton.click();
        validateAlert(is("Red button"));
    }

    @Test
    public void disableButtonTest() {
        try {
            disabledButton.click();
            fail("Disabled button should not work, but work");
        } catch (Exception ex) {
            assertThat(safeException(ex),
                containsString("Can't perform click. Element is disabled"));
        }
    }

    @Test
    public void doubleClickTest() {
        doubleButton.doubleClick();
        validateAlert(is("Double Click"));
    }

    @Test
    public void rightClickTest() {
        redButton.rightClick();
        validateAlert("Right Click");
        redButton.core().sendKeys(Keys.ESCAPE);
    }

    @Test
    public void isValidationTest() {
        redButton.is().displayed();
        redButton.is().enabled();
        redButton.is().text(is(text));
        redButton.is().text(containsString("Red"));
        assertThat(redButton.core().css("font-size"), is("16px"));
        redButton.assertThat().displayed()
            .and().text(is(text))
            .core()
            .css("font-size", is("16px"))
            .cssClass("btn btn-danger")
            .attr("type", "button")
            .tag(is("button"));
        disabledButton.is().text(containsString("Disabled button"));
        // disabledButtonInput.is().text(containsString("Disabled Button"));
        disabledButton.is().disabled();
    }

    @Test
    public void assertValidationTest() {
        redButton.assertThat().text(is(text));
    }

    @Test
    public void baseValidationTest() {
        baseValidation(redButton);
    }
}
