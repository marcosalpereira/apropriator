package br.com.marcosoft.apropriator.selenium;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;

import br.com.marcosoft.apropriator.model.ApropriationFile.Config;

/**
 * Selenium Support.
 */
public class SeleniumSupport {
    private static WebDriver webDriver;

    private SeleniumSupport() {
    }

    public static WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * Inicializa o Selenium.
     */
    public static void initSelenium(Config config) {
        webDriver = getDriver(config);
        webDriver.get(config.getUrlApropriacao());
    }

    private static WebDriver getDriver(Config config) {
        final String browser = config.getBrowserType();
        if ("chrome".equals(browser)) {
            return getChromeDriver(config);
        }
        return getFirefoxDriver(config);
    }

    private static WebDriver getFirefoxDriver(Config config) {
        setDriverLocation(config, "gecko");
        final ProfilesIni allProfiles = new ProfilesIni();
        final String profile = config.getFirefoxProfile();
        final FirefoxProfile firefoxProfile = allProfiles.getProfile(profile);
        final FirefoxOptions options = new FirefoxOptions();
        options.setProfile(firefoxProfile);
        final String browserLocation = getBrowserLocation();
        if (browserLocation != null) {
            options.setBinary(browserLocation);
        }
        options.setCapability(org.openqa.selenium.firefox.FirefoxDriver.MARIONETTE, true);
        
        final WebDriver driver = new FirefoxDriver(options);
        return driver;
    }

    private static WebDriver getChromeDriver(Config config) {
        setDriverLocation(config, "chrome");
        final ChromeOptions options = new ChromeOptions();
        final String browserLocation = getBrowserLocation();
        if (browserLocation != null) {
            options.setBinary(browserLocation);
        }
        final WebDriver driver = new ChromeDriver(options);
        return driver;
    }

    private static String getBrowserLocation() {
        return System.getProperty("browser.location");
    }

    private static void setDriverLocation(Config config, String driverName) {
        final String propertyKey = String.format("webdriver.%s.driver", driverName);

        if (System.getProperty(propertyKey) == null) {
            final String planilhaDir = config.getPlanilhaDir();
            final String driverPath = planilhaDir + File.separator + driverName + "driver";
            if (new File(driverPath).canExecute()) {
                System.setProperty(propertyKey, driverPath);
            } else {
                final String driverPathDefault = String.format("/usr/bin/%sdriver", driverName);
                System.setProperty(propertyKey, driverPathDefault);
            }
        }
    }

    public static  void stopSelenium() {
        webDriver.quit();
    }

}

