package config.extent_report;

import com.relevantcodes.extentreports.LogStatus;
import config.BaseTest;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.net.URISyntaxException;

import static config.BaseTest.getDriver;
import static config.extent_report.ExtentTestManager.*;

/**
 * <b>Author: Vidhi Bakaraniya<b/>
 * @Date:  <tt>{@code 21-11-2023}</tt>
 */
public class TestListener implements ITestListener, ISuiteListener {

        public static int ALL_PASS =0;
        public static int ALL_FAIL=0;

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         */
        private static String getTestMethodName(@NotNull ITestResult iTestResult) {
            return iTestResult.getMethod().getConstructorOrMethod().getName();
        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         * @param iTestContext
         */
        @Override
        public void onStart(@NotNull ITestContext iTestContext) {
            iTestContext.setAttribute("WebDriver", getDriver());
        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         * @param iTestContext
         */
        @Override
        public void onFinish(ITestContext iTestContext) {
            endTest();
            try {
                getReporter().flush();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         * @param iTestResult
         */
        @Override
        public void onTestStart(ITestResult iTestResult) {
        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         * @param iTestResult
         */
        @Override
        public void onTestSuccess(ITestResult iTestResult) {
            getTest().log(LogStatus.PASS, "Test passed");
            ALL_PASS ++;

        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         * @param iTestResult
         */
        @Override
        public void onTestFailure(@NotNull ITestResult iTestResult) {
            Object testClass = iTestResult.getInstance();
            WebDriver webDriver = ((BaseTest) testClass).getDriver();
            String base64Screenshot = "data:image/png;base64,"+((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BASE64);
            getTest().log(LogStatus.FAIL, getTest().addBase64ScreenShot(base64Screenshot),iTestResult.getThrowable().getMessage());
            getTest().log(LogStatus.FAIL, "Test Failed");
            ALL_FAIL ++;
        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         * @param iTestResult
         */
        @Override
        public void onTestSkipped(ITestResult iTestResult) {
            getTest().log(LogStatus.SKIP, "Test Skipped");
        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         * @param iTestResult
         */
        @Override
        public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
            System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         * @param iSuite
         */
        @Override
        public void onStart(@NotNull ISuite iSuite) {
            System.out.println("Suite executed onStart"  + iSuite.getName());
        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         * @param iSuite
         */
        @Override
        public void onFinish(@NotNull ISuite iSuite) {
            System.out.println("Suite executed onStart"  + iSuite.getName());
        }

}
