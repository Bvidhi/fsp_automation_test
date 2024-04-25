package login;

import config.BaseTest;
import org.testng.annotations.Test;
import java.io.IOException;

public class LoginTC extends BaseTest {

        public LoginTC() {
            super(getDriver());
        }

        public LoginPO loginPO(){
            return new LoginPO(getDriver());
        }

        @Test
        public void loginWithBankFields(){
            loginPO().clickOnWebElement(loginPO().getLoginButton());
            loginPO().verifyAlertMessageForEmptyFields();
        }

    @Test
    public void loginWithInvalidUsernameAndPassword() throws IOException {
        loginPO().enterUsernameAndPassword(loginPO().getValueFromFile("username").substring(0,3), loginPO().getValueFromFile("password").substring(0,3));
        loginPO().clickOnWebElement(loginPO().getLoginButton());
        loginPO().verifyAlertMessageForInvalidData();
    }

    @Test
    public void loginWithValidUsernameAndPassword() throws IOException {
        loginPO().enterUsernameAndPassword(loginPO().getValueFromFile("username"), loginPO().getValueFromFile("password"));
        loginPO().clickOnWebElement(loginPO().getLoginButton());
        loginPO().verifyDashboardPage();
    }
}
