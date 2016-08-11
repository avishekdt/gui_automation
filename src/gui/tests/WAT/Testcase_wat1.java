package gui.tests.WAT;

import gui.common.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Testcase_wat1 extends TestSuiteBase{
	String test_name = this.getClass().getSimpleName();
	
	// Check if the test case has to be skipped.
	// BeforeTest will not be repeated for data Parameterization. 
	@BeforeTest
	public void checkTestSkip() {
		printTestStartLine(test_name);
		
		printLogs("Checking Runmode of the test.");
		if(!TestUtil.isTestCaseRunnable(suite_xl, test_name)) {
			printLogs("WARNING:: Runmode of the test '" + test_name + "' set to 'N'. So Skipping the test.");
			throw new SkipException("WARNING:: Runmode of the test '" + test_name + "' set to 'N'. So Skipping the test.");
		}
	}
	
	@Test(dataProvider="getTestData")
	public void test_Testcase_wat1(String col1) {
		testParamStart();
		printLogs("Starting " + test_name + " with values : " + col1);
	}
	
	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(suite_xl, test_name);
	}
	
}
