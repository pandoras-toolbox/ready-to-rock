package rtr;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
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
import static rtr.AllureUtil.takeScreenshot;
import static rtr.TestGroup.GUI_TESTS;

@Epic("GUI Test Examples")
@Tag(GUI_TESTS)
@ExtendWith(JUnitCallback.class)
final class GuiTests {

    @Feature("Google")
    @Story("Search")
    @Test
    void searchAndExpectResults() {
        WebDriver driver = WebDriverProvider.INSTANCE.getWebDriver();
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
