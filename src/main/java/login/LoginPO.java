package login;

import config.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class LoginPO extends BaseTest {

    public LoginPO(WebDriver driver) {
        super(driver);
        PageFactory.initElements(getDriver(), this);
    }

    @FindBy(
            how = How.NAME,
            using = "username"
    )
    WebElement username;

    @FindBy(
            how = How.NAME,
            using = "password"
    )
    WebElement password;

    @FindBy(
            how = How.CLASS_NAME,
            using = "orangehrm-login-button"
    )
    WebElement loginButton;

    @FindBy(
            how = How.XPATH,
            using = "//p[text()='Invalid credentials']"
    )
    WebElement alertMessage;

    public WebElement getLoginButton() {
        return loginButton;
    }

    public WebElement getPassword() {
        return password;
    }

    public WebElement getUsername() {
        return username;
    }

    public WebElement getAlertMessage() {
        return alertMessage;
    }

    public String getValueFromFile(String value) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(System.getProperty("user.dir") + "/src/main/resources/loginDetails.properties"));
        return properties.getProperty(value);
    }

    public void enterUsernameAndPassword(String username, String password) {
        getUsername().sendKeys(username);
        getPassword().sendKeys(password);
    }

    public void clickOnWebElement(WebElement element){
        element.click();
    }

    public void verifyAlertMessageForEmptyFields(){
        String[] strings = new String[]{"username", "password"};
        for (String string : strings) {
            WebElement alert = new WebDriverWait(getDriver(), Duration.ofSeconds(20L)).
                    until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='"+string+"']//parent::div//following-sibling::span")));
            alert.isDisplayed();
        }
    }

    public void verifyAlertMessageForInvalidData(){
        getAlertMessage().isDisplayed();
    }

    public void verifyDashboardPage(){
        getDriver().getCurrentUrl().contains("dashboard");
    }
}
