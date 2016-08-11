package gui.tests.QualityQuotient;

import java.io.IOException;
import java.util.ArrayList;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.Nodes;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;
import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RemoteDeployToVc extends TestSuiteBase{
	String test_name = this.getClass().getSimpleName();
	String runmodes[] = null;
	static int count = -1;
	static boolean skip = false;
	static boolean fail = false;
	static boolean isTestPass = true;
	
	long initialTime;
	long finalTime;
	String executionTime = "-";
	
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
		
		// Load the runmodes for the data of the test.
		runmodes = getDataSetRunmodes(suite_xl, test_name);
	}
	
	@BeforeMethod
	public void testParamSetup() {
		testParamStart();
		printLogs("Test Param Setup:");
	}
	
	@Test(dataProvider="getTestData")
	public void test_RemoteDeployToVc(String col1) {
		screenshot_name = test_name;
		screenshot_counter = 0;
		
		boolean cleanupDone = false;
		boolean testFailureStatus = false;
		
		printLogs("Starting " + test_name + " with values : " + col1);
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			printLogs("WARNING:: Skipping the data set as runmode set to 'N' : " + col1);
			skip = true;
			throw new SkipException("WARNING:: Skipping the data set as runmode set to 'N' : " + col1);
		}
		else {
			printLogs("Starting the test.");
		}
		
		// Update global and test lists.
		if(!testInitialize(test_name)) {
			printError("Failed to perform test initialize.");
		}
				
		// Get the current time in ms.
		initialTime = getTimeInMsec();
		
		try {
			String remoteHostIp_vc 			= CONFIG.getProperty("remoteHostIp_vc1");
			String remoteHostUserName1_vc 	= CONFIG.getProperty("remoteHostUserName1_vc1");
			String remoteHostPassword1_vc 	= CONFIG.getProperty("remoteHostPassword1_vc1");
			String remoteHostDesc_vc 		= "VC";
			String remoteHostType_vc 		= "NodeType_vc";
			String remoteHostIp_oa 			= CONFIG.getProperty("remoteHostIp_oa1");
			String remoteHostUserName1_oa 	= CONFIG.getProperty("remoteHostUserName1_oa1");
			String remoteHostPassword1_oa 	= CONFIG.getProperty("remoteHostPassword1_oa1");
			
			// Setup::
			// Find out if it is Win-VC or Lin-VC deployment.
			if (currentOsName.contains("windows")) {
				printLogs("Trying Win-VC remote deployment.");
			}
			else {
				printLogs("Trying Lin-VC remote deployment.");
			}
			
			printLogs("Remote host IP: " + remoteHostIp_vc);
			printLogs("Remote host Type: " + remoteHostType_vc);
			printLogs("UserName: " + remoteHostUserName1_vc + " Password: " + remoteHostPassword1_vc);
			
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Do Baseline inventory
			if(!BaselineLibrary.performBaselineInventory()) {
				throw new TestException("Test Failed");
			}
			
			// 3. Add Node
			if(!Nodes.addNodeVc(remoteHostIp_vc, remoteHostDesc_vc, false, false, 
						remoteHostUserName1_vc, remoteHostPassword1_vc, remoteHostIp_oa, 
						remoteHostUserName1_oa, remoteHostPassword1_oa)){
				throw new TestException("Test Failed");
			}
			
			// 4. Do Node inventory
			if(!Nodes.performNodeInventory()) {
				throw new TestException("Test Failed");
			}
			
			// 5. Deploy Node.
			if(!Nodes.deployNode(remoteHostIp_vc, remoteHostType_vc)) {
				throw new TestException("Test Failed");
			}
			
			// 6. View Logs
			if(!Nodes.viewLogsAfterDeploy(remoteHostIp_vc,remoteHostType_vc)) {
				throw new TestException("Test Failed");
			}
			
			// Final ScreenShot
			captureScreenShot();
			
			// Cleanup::
			// Call general cleanup function - performTestCleanup
			if(!CommonHpsum.performTestCleanup()) {
				printError("Failed to perform cleanup.");
				testFailureStatus = true;
			}
			
			// If testFailureStatus is true - Mark test as failed.
			if(testFailureStatus) {
				printError("Found test failure flag set.");
				cleanupDone = true;
				throw new TestException("FAIL");
			}
			// Get current time in ms.
			finalTime = getTimeInMsec();
			
			// Calculate test time interval.
			executionTime = calculateTimeInterval(initialTime, finalTime);
			
			// Write the final test result in html file.
			testFinalize(test_name, "PASS", executionTime);
			
			printLogs("DONE.");
		}
		catch (Throwable t) {
			// Get current time in ms.
			finalTime = getTimeInMsec();
			
			// Calculate test time interval.
			executionTime = calculateTimeInterval(initialTime, finalTime);
			
			// Write the final test result in html file.
			testFinalize(test_name, "FAIL", executionTime);
			
			fail = true;			
			try {
				captureScreenShot();
			} catch (IOException e) {
				printError("Exception occurred while taking screen-shot.");
				e.printStackTrace();
			}
			
			//printError("TEST RESULT : FAIL");
			ErrorUtil.addVerificationFailure(t);
			
			if(!cleanupDone) {
				if(!CommonHpsum.performTestCleanup()) {
					printError("Failed to perform cleanup.");
				}
			}
			return;
		}
	}
	
	@AfterMethod
	public void testParamCleanup() {
		printLogs("Test Param Cleanup:");
		
		// Print the global list.
		for(int i = 0; i < testList.size(); i++) {  
			printLogs(testList.get(i));
		}
		

		if(skip) {
			TestUtil.reportDataSetResult(suite_xl, test_name, count+2, "SKIP");
		}
		else if(fail) {
			isTestPass = false;
			TestUtil.reportDataSetResult(suite_xl, test_name, count+2, "FAIL");
		}
		else {
			TestUtil.reportDataSetResult(suite_xl, test_name, count+2, "PASS");
		}
		skip = false;
		fail = false;
		testParamEnd();
	}
	
	@AfterTest
	public void testCleanup() {
		printLogs("Test Cleanup:");
		if(isTestPass) {
			TestUtil.reportDataSetResult(suite_xl, "TestCases", TestUtil.getRowNum(suite_xl, test_name), "PASS");
		}
		else {
			TestUtil.reportDataSetResult(suite_xl, "TestCases", TestUtil.getRowNum(suite_xl, test_name), "FAIL");
		}
		
		printTestEndLine(test_name);
	}
	
	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(suite_xl, test_name);
	}
}
