package io.github.epam.html.tests.elements.complex.list;

import io.github.epam.TestsInit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.github.com.StaticSite.homePage;
import static io.github.com.pages.Header.search;
import static io.github.com.pages.SearchPage.searchS;
import static io.github.epam.html.tests.site.steps.States.shouldBeLoggedIn;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Created by Roman_Iovlev on 3/2/2018.
 */
public class WaitDataListTests implements TestsInit {
    @BeforeMethod
    public void before() {
        homePage.shouldBeOpened();
        shouldBeLoggedIn();
        search("jdi");
    }

    @Test
    public void notEmptyTest() {
        searchS.is().notEmpty();
    }
    @Test
    public void notEmpty2Test() {
        searchS.assertThat(not(empty()));
    }
    @Test
    public void emptyTest() {

        try {
            searchS.is().empty();
            fail("List should not be empty");
        } catch (Throwable ignored) { }
        searchS.is().notEmpty();
    }
    @Test
    public void sizeTest() {
        assertEquals(searchS.size(), 6);
        searchS.is().size(equalTo(8));
    }
    @Test
    public void sizeGreaterTest() {
        searchS.is().size(greaterThan(7));
    }

}
