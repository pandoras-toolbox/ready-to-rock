package org.rtr;

import io.qameta.allure.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.net.UrlChecker;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.Keys.ENTER;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.rtr.AllureUtil.takeScreenshot;
import static org.rtr.TestGroup.GUI_TESTS;

@Epic("GUI Test Examples")
@Tag(GUI_TESTS)
@ExtendWith(JUnitCallback.class)
final class GuiTests {

    @Feature("Google")
    @Story("Search")
    @Link(name = "websearch/answer/2466433", type = "google-support")
    @TmsLink("Test-Google-Search-Engine")
    @Issue("1")
    @Description("This is a little smoke test for the search engine.")
    @Test
    void searchAndExpectResults() {
        WebDriver driver = DriverProvider.INSTANCE.getWebDriver();
        Configuration config = Configuration.INSTANCE;
        Duration seleniumTimeout = config.getSeleniumTimeout();
        WebDriverWait wait = new WebDriverWait(driver, seleniumTimeout);
        URL url = config.getSeleniumUrl();
        driver.get(url.toExternalForm());
        waitUntilAvailable(seleniumTimeout.getSeconds(), SECONDS, url);
        takeScreenshot("Search Screen");
        driver.findElement(By.name("q")).sendKeys("cheese" + ENTER);
        List<WebElement> results = wait.until(presenceOfAllElementsLocatedBy(By.cssSelector("#rso > .g")));
        assertThat(results).as("number of search results").hasSizeGreaterThan(0);
        assertThat(results.get(0).getAttribute("textContent")).as("textContent").isNotEmpty();
    }

    @Step
    private static void waitUntilAvailable(long timeout, TimeUnit unit, URL url) {
        try {
            new UrlChecker().waitUntilAvailable(timeout, unit, url);
        } catch (UrlChecker.TimeoutException e) {
            throw new TimeoutException(e);
        }
    }

}
