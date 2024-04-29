package config.extent_report;


import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <b>Author: Vidhi Bakaraniya<b/>
 * @Date:  <tt>{@code 21-11-2023}</tt>
 */
public class ExtentTestManager{

        private static ExtentReports extent;
        private static final Map extentTestMap = new HashMap();
        private static final ExtentReports report;

        static {
            try {
                report = getReporter();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

         /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         */
         public static synchronized ExtentTest getTest() {
            return (ExtentTest)extentTestMap.get((int) (long) (Thread.currentThread().getId()));
        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         */
        public static synchronized void endTest() {
            report.endTest((ExtentTest)extentTestMap.get((int) (long) (Thread.currentThread().getId())));
        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         * @param testName
         */
        public static synchronized ExtentTest startTest(String testName) {
            return startTest(testName, "");
        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         * @param testName
         * @param desc
         */
        public static synchronized ExtentTest startTest(String testName, String desc) {
            ExtentTest test = report.startTest(testName, desc);
            extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
            return test;
        }

        /**
         * @Date:  <tt>{@code 21-11-2023}</tt>
         * @throws URISyntaxException
         */
        public synchronized static ExtentReports getReporter() throws URISyntaxException {
            if(extent == null) {
                File f = new File(System.getProperty("user.dir") + "\\report\\");
                if (Objects.requireNonNull(f.listFiles()).length > 30) {
                    File[] files = f.listFiles();
                    assert files != null;
                    int file = files.length;
                    for(int i = 0; i < file; ++i) {
                        File file1 = files[i];
                        file1.delete();
                    }
                }
                extent = new ExtentReports(System.getProperty("user.dir")+"\\report\\Report_" + new SimpleDateFormat("dd_MMMM_yyyy_hh_mm_ss_a").format(new Date()) + ".html", false);
                extent.loadConfig(new File(System.getProperty("user.dir")+"\\src\\main\\resources\\Config\\report-config.xml"));
            }
            return extent;
        }
}
