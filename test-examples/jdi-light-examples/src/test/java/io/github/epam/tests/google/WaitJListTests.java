package io.github.epam.tests.google;

import io.github.epam.StaticTestsInit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.github.com.StaticSite.homePage;
import static io.github.com.pages.Header.search;
import static io.github.com.pages.SearchPage.jsearchTitle;
import static io.github.epam.tests.recommended.steps.Preconditions.shouldBeLoggedIn;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

/**
 * Created by Roman_Iovlev on 3/2/2018.
 */
public class WaitJListTests extends StaticTestsInit {

    @BeforeMethod
    public void before() {
        homePage.shouldBeOpened();
        shouldBeLoggedIn();
        search("jdi");
    }

    @Test
    public void notEmptyTest() {
        jsearchTitle.is().notEmpty();
    }

    @Test
    public void notEmpty2Test() {
        jsearchTitle.assertThat(not(empty()));
    }

    @Test
    public void emptyTest() {
        jsearchTitle.waitFor(2).notEmpty();
    }

    @Test
    public void sizeTest() {
        assertEquals(jsearchTitle.size(), 6);
        jsearchTitle.waitFor(10).size(equalTo(8));
    }

    @Test
    public void sizeNotEmptyTest() {
        jsearchTitle.waitFor(10).size(greaterThan(7));
    }

}
