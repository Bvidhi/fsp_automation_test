package signup;

import com.github.javafaker.Faker;
import com.google.common.base.CharMatcher;
import config.BaseTest;
import config.CentralizePO;
import login.LoginPO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.mail.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.TimeZone;

public class SignupPO extends BaseTest {

    public SignupPO(WebDriver driver) {
        super(driver);
        PageFactory.initElements(getDriver(), this);
    }

    public CentralizePO centralize(){
        return new CentralizePO(getDriver());
    }

    @FindBy(
            how = How.XPATH,
            using = "//a[text()='Create an Account']"
    )
    WebElement createAccountButton;

    @FindBy(
            how = How.XPATH,
            using = "//button[text()='SIGN ME UP']"
    )
    WebElement signUpButton;

    @FindBy(
            how = How.XPATH,
            using = "//label[text()='Enter Email']//following-sibling::input"
    )
    WebElement enterEmail;

    @FindBy(
            how = How.CSS,
            using = "span.register"
    )
    WebElement otpPopup;

    @FindBy(
            how = How.XPATH,
            using = "//p[text()='VERIFY & LOGIN']"
    )
    WebElement verifyLoginButton;

    @FindBy(
            how = How.ID,
            using = "auth-mobile-verify-otp-sign-modal"
    )
    WebElement otpTextBox;

    public WebElement getCreateAccountButton() {
        return createAccountButton;
    }

    public WebElement getSignUpButton() {
        return signUpButton;
    }

    public  WebElement getEnterEmail() {
        return enterEmail;
    }

    public WebElement getOtpPopup() {
        return otpPopup;
    }

    public WebElement getVerifyLoginButton() {
        return verifyLoginButton;
    }

    public WebElement getOtpTextBox() {
        return otpTextBox;
    }

    public boolean verifyErrorForEmptyFields(){
        String[] fieldsName = new String[]{"Mobile Number","Enter Email", "First Name", "Last Name","Password","Confirm Password"};
        for (String s : fieldsName){
            WebElement element = new WebDriverWait(getDriver(), 20).
                    until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[text()='"+s+"']//following-sibling::input")));
            if (element.getAttribute("class").contains("error"))
                return true;
            return false;
        }
        return true;
    }

    public String getEmail(){
        Faker faker = new Faker();
        String email;
        email = faker.name().firstName() + "@yopmail.com";
        return  email;
    }

    public String getOTP() throws IOException, InterruptedException {
        String otp = "";
        Thread.sleep(3000);
        Properties props2 = System.getProperties();
        System.getProperties().setProperty("mail.store.protocol", "imaps");
        try {
            Store store = Session.getDefaultInstance(props2, null).getStore("imaps");
            store.connect(
                    "imap.gmail.com",
                    String.valueOf(centralize().getValueFromFile("email")),
                    String.valueOf(centralize().getValueFromFile("app_password")));
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            if (messages[messages.length - 1].isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) messages[messages.length - 1].getContent();
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (bodyPart.isMimeType("text/html")) {
                        Document document = Jsoup.parse((String) bodyPart.getContent());
                        String code = Jsoup.parse(document.body().text()).text();
                        otp = CharMatcher.digit().retainFrom(code.split("Use the OTP below to access your CaratLane account!")[1]);
                        System.out.println("OTP:   " + otp);
                    }
                }
            }
            folder.close(false);
            store.close();
            return otp.substring(0,otp.length()-2);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

            // Display the default time zone
            System.out.println("Default Time Zone: " + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));

    }

}
