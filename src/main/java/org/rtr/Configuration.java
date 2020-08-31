package org.rtr;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.firefox.FirefoxBinary;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

// Needs to be thread-safe because of parallel test execution.
public enum Configuration {

    INSTANCE;

    private final PropertiesConfiguration config;

    Configuration() {
        Configurations configs = new Configurations();
        try {
            config = configs.properties(new File("default.properties"));
        } catch (ConfigurationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Browser getBrowser() {
        String browser = config.getString("selenium.browser");
        return Browser.valueOf(browser);
    }

    public Optional<File> getChromeBinary() {
        Optional<File> result;
        String path = config.getString("selenium.chrome.binary");
        if (StringUtils.isEmpty(path)) {
            result = Optional.empty();
        } else {
            result = Optional.of(new File(path));
        }
        return result;
    }

    public Optional<FirefoxBinary> getFirefoxBinary() {
        Optional<FirefoxBinary> result;
        String path = config.getString("selenium.firefox.binary");
        if (StringUtils.isEmpty(path)) {
            result = Optional.empty();
        } else {
            result = Optional.of(new FirefoxBinary(new File(path)));
        }
        return result;
    }

    public URL getSeleniumUrl() {
        try {
            return new URL(config.getString("selenium.url"));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Duration getSeleniumTimeout() {
        long amount = config.getLong("selenium.timeout.amount");
        String unit = config.getString("selenium.timeout.unit");
        return Duration.of(amount, ChronoUnit.valueOf(unit));
    }

}
