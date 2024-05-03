package config;

import com.relevantcodes.extentreports.LogStatus;
import config.extent_report.TestListener;
import config.mail_config.ReportConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import static config.extent_report.ExtentTestManager.*;
import static config.extent_report.ExtentTestManager.getReporter;
import static java.lang.System.out;
import static org.openqa.selenium.remote.BrowserType.*;

@Listeners({TestListener.class})
public class BaseTest{

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
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            if(headless){
                options.addPreference("--headless", true);
            }
            driver = new FirefoxDriver(options);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(20L, TimeUnit.SECONDS);
        } else if (browserName.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"/driver/chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            if(headless){
                options.addArguments("--headless");
            }
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(20L, TimeUnit.SECONDS);
        } else if (browserName.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            if(headless){
              //options.addArguments("--headless");
            }
            driver = new EdgeDriver(options);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(20L, TimeUnit.SECONDS);
        } else {
            out.println("Please provide valid browser name");
        }
        return driver;
    }

    public synchronized WebDriver getRemoteDriver(@Optional("firefox") @NotNull String browserName, String hubURL) throws MalformedURLException {
        WebDriver driver = null;
        DesiredCapabilities cap = new DesiredCapabilities();
        if (browserName.equalsIgnoreCase("firefox")){
            cap.setBrowserName(FIREFOX);
            cap.setPlatform(Platform.WINDOWS);
            FirefoxOptions options = new FirefoxOptions();
            options.setAcceptInsecureCerts(true);
            options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
            options.merge(cap);
            driver = new RemoteWebDriver(new URL(hubURL), options);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(20L, TimeUnit.SECONDS);
        }else if(browserName.equalsIgnoreCase("chrome")) {
            cap.setBrowserName(CHROME);
            cap.setPlatform(Platform.WINDOWS);
            driver = new RemoteWebDriver(new URL(hubURL), cap);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(20L, TimeUnit.SECONDS);
        }else if(browserName.equalsIgnoreCase("edge")){
            cap.setBrowserName(EDGE);
            cap.setPlatform(Platform.WINDOWS);
            EdgeOptions options = new EdgeOptions();
            options.merge(cap);
            driver = new RemoteWebDriver(new URL(hubURL), options);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(20L, TimeUnit.SECONDS);
        }else {
            out.println("Please provide valid browser name");
        }
        return driver;
    }

    @Parameters({"record","selGrid", "hubURL","browserName", "headless", "baseURL"})
    @BeforeMethod(alwaysRun = true)
    public void launchBrowser(Method method, boolean record,@Optional("false")boolean selGrid, String hubURL,@Optional("firefox")String browserName, @Optional("false")boolean headless, @Optional("https://www.irafinancialtrust.com/") String baseURL) throws Exception {
        if (record)
            ScreenRecorderUtil.startRecord(""+method.getName());
        WebDriver driver;
        if (selGrid){
            driver = this.getRemoteDriver(browserName,hubURL);
        }else {
            driver = this.getDriver(browserName,headless);
        }
        threadLocalDriver.set(driver);
        Capabilities caps = ((RemoteWebDriver) getDriver()).getCapabilities();
        getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        getDriver().get(baseURL);
        startTest(method.getName());
        getTest().log(LogStatus.INFO, "Browser Name", caps.getBrowserName());
        getTest().log(LogStatus.INFO, caps.getBrowserName() + ": version", caps.getVersion());
        getTest().log(LogStatus.INFO, "Platform Name", caps.getPlatform().toString());
        getTest().log(LogStatus.INFO, "Open Browser and navigate to", "<a href=" + driver.getCurrentUrl() +  "target=_blank>" + driver.getCurrentUrl() + "</a>");
    }

    @Parameters({"record"})
    @AfterMethod(alwaysRun = true)
    public void afterMethod(boolean record) throws Exception {
        if (record) {
            ScreenRecorderUtil.stopRecord();
        }
        getReporter().endTest(getTest());
        getReporter().flush();
        getDriver().quit();
        threadLocalDriver.remove();
    }

    @Parameters({"mailConfig"})
    @AfterSuite(alwaysRun = true)
    public void sendMail(boolean mailConfig) {
        if (mailConfig) {
            new ReportConfig().sendEmail();
        }else {
            out.println("Email not sent");
        }

    }

    public void testLog(String stepName, String details) {
        getTest().log(LogStatus.PASS, stepName, details);
    }
}