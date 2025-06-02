package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.Constants;

import java.io.File;

public class TestListeners implements ITestListener {

    public static final Logger logger = LogManager.getLogger(TestListeners.class);
    ExtentReports extentReports = new ExtentReports();
    File file = new File(Constants.PROJECT_ROOT + "/src/results/reports/eReport.html");
    ExtentSparkReporter sparkReporter = new ExtentSparkReporter(file);
    ExtentTest extentTest;

    private void createReport(ITestResult result){
        sparkReporter.config(ExtentSparkReporterConfig.builder()
                .theme(Theme.DARK)
                .documentTitle("Rest Assured Report")
                .build());
        extentReports.attachReporter(sparkReporter);
        extentTest = extentReports.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("STARTING TEST METHOD: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        createReport(result);
        logger.info("TEST METHOD: " + result.getMethod().getMethodName() + " SUCCESSFUL");
        extentTest.log(Status.PASS, "Test: "+result.getMethod().getMethodName() + " PASSED");
        extentReports.flush();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        createReport(result);
        logger.info("TEST METHOD: " + result.getMethod().getMethodName() + " FAILED!");
        extentTest.log(Status.FAIL, "Test: "+result.getMethod().getMethodName() + " FAILED");
        extentReports.flush();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.info("TEST METHOD: " + result.getMethod().getMethodName() + " SKIPPED!");
    }

}
