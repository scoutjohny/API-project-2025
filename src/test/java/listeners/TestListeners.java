package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListeners implements ITestListener {

    public static final Logger logger = LogManager.getLogger(TestListeners.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("STARTING TEST METHOD: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("TEST METHOD: " + result.getMethod().getMethodName() + " SUCCESSFUL");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.info("TEST METHOD: " + result.getMethod().getMethodName() + " FAILED!");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.info("TEST METHOD: " + result.getMethod().getMethodName() + " SKIPPED!");
    }

}
