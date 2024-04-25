package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import java.time.Duration;

import static java.lang.System.out;

public class BaseTest {

    public static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<WebDriver>();

    public BaseTest(WebDriver driver) {
        setDriver(driver);
    }

    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    public void setDriver(WebDriver driver) {
        threadLocalDriver.set(driver);
    }

    public synchronized WebDriver getDriver(String browserName, boolean headless){
        WebDriver driver = null;
        if (browserName.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            if(headless){
                options.addPreference("--headless", true);
            }
            driver = new FirefoxDriver(options);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20L));
        } else if (browserName.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if(headless){
                options.addArguments("--headless");
            }
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20L));
        } else if (browserName.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            if(headless){
                options.addArguments("--headless");
            }
            driver = new EdgeDriver(options);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20L));
        } else {
            out.println("Please provide valid browser name");
        }
        return driver;
    }

    @Parameters({"browserName", "headless", "baseURL"})
    @BeforeMethod(alwaysRun = true)
    public void launchBrowser(@Optional("firefox")String browserName, @Optional("false")boolean headless, @Optional("https://www.irafinancialtrust.com/") String baseURL)   {
        WebDriver driver;
        driver = this.getDriver(browserName,headless);
        threadLocalDriver.set(driver);
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20L));
        getDriver().get(baseURL);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(){
        getDriver().quit();
        threadLocalDriver.remove();
    }
}