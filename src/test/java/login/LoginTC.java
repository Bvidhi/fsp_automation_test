package login;

import com.relevantcodes.extentreports.LogStatus;
import config.BaseTest;
import config.CentralizePO;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import static config.extent_report.ExtentTestManager.getTest;

public class LoginTC extends BaseTest {

        public LoginTC() {
            super(getDriver());
        }

        public LoginPO login(){
            return new LoginPO(getDriver());
        }

        public CentralizePO centralize(){
            return new CentralizePO(getDriver());
        }

        @Test(groups={"login","000","Regression"})
        public void login_01_LoginWithBlankFields() throws InterruptedException {
            //Step 1 : Click on the "CONTINUE TO LOG IN" button without entering any information into the "Enter Mobile Number or Email" fields.
            Assert.assertTrue(centralize().clickOnWebElement(login().getContinueLoginButton()));
            testLog("Click on the \"CONTINUE TO LOG IN\" button without entering any information into the \"Enter Mobile Number or Email\" fields.", " The alert warning message should be appear under the textboxes.");

            // Step 2 : Check if an alert warning message appears to confirm that the login attempt failed due to empty fields.
            //Assert.assertTrue(login().verifyErrorForEmptyFields());
            testLog("Check if an alert warning message appears to confirm that the login attempt failed due to empty fields.", "The alert warning message should be appear on the screen.");
        }

    //@Test(dataProvider = "user", dataProviderClass = LoginPO.class )
    //public void login_02_LoginWithNonRegisteredUserEmail(String user){

    @Test(groups={"login","111"})
    public void login_02_LoginWithNonRegisteredUserEmail(){

        //Step 1 :  Enter a non-registered email "Enter Mobile Number or Email" into the "Enter Mobile Number or Email" field.
        //centralize().enterValue(login().getEmailMobile(), user);
        Assert.assertTrue(centralize().enterValue(login().getEmailMobile(), login().getEmail()));
        testLog("Enter a non-registered email \"Enter Mobile Number or Email\" into the \"Enter Mobile Number or Email\" field.", "The email should be entered into the email field.");

        //Step 2 : Click on the "CONTINUE TO LOG IN" button.
        Assert.assertTrue(centralize().clickOnWebElement(login().getContinueLoginButton()));
        testLog("Click on the \"CONTINUE TO LOG IN\" button.", "The user should be redirected to the signup page.");

        //S tep 3 :  Verify that the user is redirected to the signup page by entering  invalid "Enter Mobile Number or Email"
        Assert.assertEquals(login().getToasterMessage().getText(), "User does not exist");
        Assert.assertTrue(centralize().elementIsDisplayed(login().getHeaderText()));
        testLog("Verify that the user is redirected to the signup page by entering  invalid \"Enter Mobile Number or Email\"", "The user should be redirected to the signup page.");
    }

    @Test
    public void login_03_LoginWithValidEmailAndInvalidPassword() throws IOException {
        //Step 1 : Enter a valid "Enter Mobile Number or Email" into the "Enter Mobile Number or Email" field
        Assert.assertTrue(centralize().enterValue(login().getEmailMobile(), centralize().getValueFromFile("email")));
        testLog("Enter a valid \"Enter Mobile Number or Email\" into the \"Enter Mobile Number or Email\" field", "The email should be entered into the email field.");

        //Step 2 : Click on the "CONTINUE TO LOG IN" button.
        Assert.assertTrue(centralize().clickOnWebElement(login().getContinueLoginButton()));
        testLog("Click on the \"CONTINUE TO LOG IN\" button.", "The user should be redirected to the \"Enter Password page\" page.");

        //S tep 3 : Verify that the user is redirected to the "Enter Password page" by entering valid "Enter Mobile Number or Email"
        Assert.assertTrue(centralize().elementIsDisplayed(login().getPasswordPageHeader()));
        testLog("Verify that the user is redirected to the \"Enter Password page\" by entering valid \"Enter Mobile Number or Email\"", "The user should be able to enter the password.");

        //Step 4 : Enter an invalid "password" into the password field.
        Assert.assertTrue(centralize().enterValue(login().getPasswordTextbox(), centralize().getValueFromFile("password").substring(0,3)));
        testLog("Enter an invalid \"password\" into the password field.", "The user is able to click on the LOGIN button");

        //Step 5 : Click on the "LOGIN" button
        Assert.assertTrue(centralize().clickOnWebElement(login().getLoginButton()));
        testLog("Click on the \"LOGIN\" button",  "The user should not be logged into the website.");

        //Step 6 : Verify that the alert message is displayed under the password textbox
        Assert.assertEquals(login().getErrorText().getText(), "Password is required");
        testLog("Verify that the alert message is displayed under the password textbox", "The alert message should be displayed under the password textbox.");
    }

    @Test
    public void login_04_LoginWithValidEmailAndPassword() throws IOException {

        //Pre-condition : LOGIN_3 - Verify the login functionality by entering a valid "Enter Mobile Number or Email" along with a Invalid password.
        login_03_LoginWithValidEmailAndInvalidPassword();

        getTest().log(LogStatus.INFO, "Execution starts from LOGIN_04");

        //Step 1 :  Enter a valid "password" into the password field.
        Assert.assertTrue(centralize().enterValue(login().getPasswordTextbox(), centralize().getValueFromFile("password")));
        testLog("Enter a valid \"password\" into the password field.", "The user is able to click on the LOGIN button");

        //Step 2 : Click on the "LOGIN" button
        Assert.assertTrue(centralize().clickOnWebElement(login().getLoginButton()));
        testLog("Click on the \"LOGIN\" button", "The user should be logged into the website.");

        //S tep 3 : Verify that the user is logged into the website
        Assert.assertEquals(login().getToasterMessage().getText(), "Logged in successfully");
        testLog("Verify that the user is logged into the website", "Verify that the user is logged into the website");
    }

}
