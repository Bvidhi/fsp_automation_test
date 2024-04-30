package login;

import com.github.javafaker.Faker;
import config.BaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;

public class LoginPO extends BaseTest {

    public LoginPO(){
        super(getDriver());
    }

    public LoginPO(WebDriver driver) {
        super(driver);
        PageFactory.initElements(getDriver(), this);
    }

    @FindBy(
            how = How.NAME,
            using = "emailMobile"
    )
    WebElement emailMobile;

    @FindBy(
            how = How.XPATH,
            using = "//button[text()='CONTINUE TO LOGIN']"
    )
    WebElement continueLoginButton;

    @FindBy(
            how = How.CSS,
            using = ".toaster-message"
    )
    WebElement toasterMessage;

    @FindBy(
            how = How.XPATH,
            using = "//h2[@content='Signup with CaratLane']"
    )
    WebElement headerText;

    @FindBy(
            how = How.XPATH,
            using = "//h2[text()='Enter Password to Login']"
    )
    WebElement passwordPageHeader;

    @FindBy(
            how = How.NAME,
            using = "password"
    )
    WebElement passwordTextbox;

    @FindBy(
            how = How.CSS,
            using = "p.error"
    )
    WebElement errorText;

    @FindBy(
            how = How.XPATH,
            using = "//button[text()='LOGIN']"
    )
    WebElement loginButton;

    public WebElement getEmailMobile() {
        return emailMobile;
    }

    public WebElement getContinueLoginButton() {
        return continueLoginButton;
    }

    public WebElement getToasterMessage() {
        return toasterMessage;
    }

    public WebElement getHeaderText() {
        return headerText;
    }

    public WebElement getPasswordPageHeader() {
        return passwordPageHeader;
    }

    public WebElement getPasswordTextbox() {
        return passwordTextbox;
    }

    public WebElement getErrorText() {
        return errorText;
    }

    public  WebElement getLoginButton() {
        return loginButton;
    }

    public boolean verifyErrorForEmptyFields(){
        if (getEmailMobile().getAttribute("class").contains("error"))
            return true;
        return false;
    }

    public String getEmail(){
        Faker faker = new Faker();
        String email;
        email = faker.name().firstName() + "@yopmail.com";
        return  email;
    }

    @DataProvider(name = "user")
    public Object[][] dpMethod(){
        Faker faker = new Faker();
        return new Object[][] {{faker.name().firstName() + "@yopmail.com"},
                {faker.name().lastName() + "@yopmail.com"},
                {faker.name().username() + "@yopmail.com"}
        };
    }

}
