package gui.tests.WAT;

import gui.common.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Testcase_wat2 extends TestSuiteBase{
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
	
	// This ideally does NOT need a dataProvider funtion as 
	// there is not data for this test in the xlsx file.
	@Test(dataProvider="getTestData")
	public void test_Testcase_wat2() {
		testParamStart();
		printLogs("Starting " + test_name + " with no values");
	}
	
	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(suite_xl, test_name);
	}
}
