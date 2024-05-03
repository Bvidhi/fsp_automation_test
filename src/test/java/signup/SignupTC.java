package signup;

import com.relevantcodes.extentreports.LogStatus;
import config.BaseTest;
import config.CentralizePO;
import login.LoginPO;
import login.LoginTC;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;

import static config.extent_report.ExtentTestManager.getTest;

public class SignupTC extends BaseTest {

        public SignupTC() {
            super(getDriver());
        }

        public SignupPO signup(){
            return new SignupPO(getDriver());
        }

        public LoginPO login(){
            return new LoginPO(getDriver());
        }

        public CentralizePO centralize(){
                return new CentralizePO(getDriver());
            }

        @Test
        public void signup_01_SignupWithBlankFields() throws InterruptedException {
            //Pre-condition : LOGIN_1 - Verify the login functionality by attempting to log in with the blank Enter Mobile Number or Email.
            new LoginTC().login_01_LoginWithBlankFields();

            getTest().log(LogStatus.INFO, "Execution starts from SIGNUP_01");

            //Step 1 : Click on "Create an Account" button
            Assert.assertTrue(centralize().clickOnWebElement(signup().getCreateAccountButton()));
            testLog("Clicked on Create Account button", "The Signup page is opened");

            // Step 2 : Click on the "SIGN ME UP" button without entering any information into any required fields.
            Assert.assertTrue(centralize().clickOnWebElement(signup().getSignUpButton()));
            testLog("Click on the \"SIGN ME UP\" button without entering any information into any required fields.", "The alert warning message should be appear under the textboxes.");

            // Step 3 : Check if an alert warning message appears to confirm that the SIGN ME UP attempt failed due to empty fields.
            Assert.assertTrue(signup().verifyErrorForEmptyFields());
            testLog("Check if an alert warning message appears to confirm that the SIGN ME UP attempt failed due to empty fields.", "The alert warning message should be appear on the screen.");

        }

    @Test
    public void signup_02_SignupWithEnteringExistingUserEmail() throws IOException, InterruptedException {
        //Pre-condition : SIGNUP_1 - Verify the signup functionality by attempting to signup without filling in any required fields.
        signup_01_SignupWithBlankFields();

        getTest().log(LogStatus.INFO, "Execution starts from SIGNUP_02");

        //Step 1 : Enter an existing user email into the email field
        Assert.assertTrue(centralize().enterValue(signup().getEnterEmail(), centralize().getValueFromFile("email")));
        testLog("Enter an existing user email into the email field", "OTP popup window should be displayed");

        // Step 2 : Verify enter OTP popup window is displayed
        Assert.assertTrue(centralize().elementIsDisplayed(signup().getOtpPopup()));
        testLog("Verify enter OTP popup window is displayed", "The user should be able to enter OTP");

        // Step 3 : Enter OTP
        Assert.assertTrue(centralize().enterValue(signup().getOtpTextBox(),signup().getOTP()));
        testLog("Enter OTP", "The user should be able to click on the verify button.");

        // Step 4 : Click on Verify & Login button
        Assert.assertTrue(centralize().clickOnWebElement(signup().getVerifyLoginButton()));
        testLog("Click on Verify & Login button","The user should be logged into the website.");

        // Step 5 : Verify that the user is logged into the website
        Assert.assertEquals(login().getToasterMessage().getText(), "Logged in successfully");
        testLog("Verify that the user is logged into the website","The user should be logged into the website.");
    }
}
