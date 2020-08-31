package org.rtr;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;

// Needs to be thread-safe because of parallel test execution.
public enum DriverProvider {

    INSTANCE;

    private WebDriver webDriver;

    public WebDriver getWebDriver() {
        if (webDriver == null) {
            Configuration config = Configuration.INSTANCE;
            Browser browser = config.getBrowser();
            switch (browser) {
                case CHROME -> {
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = createChromeOptions(config.getChromeBinary().orElse(null));
                    webDriver = new ChromeDriver(options);
                }
                case CHROME_HEADLESS -> {
                    WebDriverManager.chromedriver().setup();
                    // See: https://peter.sh/experiments/chromium-command-line-switches/
                    ChromeOptions options = createChromeOptions(config.getChromeBinary().orElse(null));
                    options.addArguments("--headless");
                    webDriver = new ChromeDriver(options);
                }
                case FIREFOX -> {
                    WebDriverManager.firefoxdriver().setup();
                    // It might not work if the binary location is not set:
                    FirefoxOptions options = new FirefoxOptions();
                    config.getFirefoxBinary().ifPresent(options::setBinary);
                    webDriver = new FirefoxDriver(options);
                }
                default -> throw new IllegalArgumentException(String.format("Browser '%s' is not supported", browser));
            }
        }
        return webDriver;
    }

    private ChromeOptions createChromeOptions(File chromeBinary) {
        ChromeOptions options = new ChromeOptions();
        if (chromeBinary != null) {
            options.setBinary(chromeBinary);
        }
        return options;
    }

}
