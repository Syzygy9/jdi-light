package io.github.epam.html.tests.elements.complex;

import io.github.epam.TestsInit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.github.com.StaticSite.metalAndColorsPage;
import static io.github.com.pages.MetalAndColorsPage.odds;
import static io.github.epam.html.tests.site.steps.States.shouldBeLoggedIn;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.testng.Assert.assertEquals;

public class RadioLabelTests implements TestsInit {

    String text = "5";

    @BeforeMethod
    public void before() {
        shouldBeLoggedIn();
        metalAndColorsPage.shouldBeOpened();
        odds.select(text);
    }

    @Test
    public void getValueTest() {
        assertEquals(odds.getValue(), text);
    }

    @Test
    public void selectTest() {
        odds.select("3");
        assertEquals(odds.getValue(), "3");
    }

    @Test
    public void selectNumTest() {
        odds.select(1);
        assertEquals(odds.getValue(), "1");
    }

    @Test
    public void selectedTest() {
        assertEquals(odds.selected(), text);
    }

    @Test
    public void valuesTest() {
        assertEquals(odds.values(), asList("1", "3", "5", "7"));
    }

    @Test
    public void isValidationTest() {
        odds.is().selected("5");
        odds.is().values(hasItem("7"));
        odds.is().enabled(hasItems("3", "5"));
    }

    @Test
    public void assertValidationTest() {
        odds.assertThat().values(contains("1", "3", "5", "7"));
    }

}
