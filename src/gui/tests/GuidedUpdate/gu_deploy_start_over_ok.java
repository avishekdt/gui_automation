package gui.tests.GuidedUpdate;

import gui.common.base.CommonHpsum;
import gui.common.base.GuidedUpdate;
import gui.common.pages.GuidedUpdatePage;
import gui.common.pages.PageCommon;
import gui.common.pages.PageCommon.Pages;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

//
//Assertion: 
//<Write the purpose of the test case>
//

public class gu_deploy_start_over_ok extends TestSuiteBase {		
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	int iTimeout =240;
	
	GuidedUpdatePage GuidedUpdatePg = null;
	
	@Test
	public void test_gu_deploy_start_over_ok() {
		screenshot_name 	= test_name;
		screenshot_counter 	= 0;
		
		try {
			//
			// Environment:
			//
			printTestStartLine(test_name);
			
			// Get the test start time.
			initialTime = getTimeInMsec();

			// Creating email template for the test.
			if(!testInitialize(test_name)) {
				printError("Failed to perform test initialize.");
			}
			
			printLogs("Checking Runmode of the test.");
			if(!TestUtil.isTestCaseRunnable(suite_xl, test_name)) {
				printLogs("WARNING:: Runmode of the test '" + test_name + "' set to 'N'. So Skipping the test.");
				skip = true;
				throw new SkipException("WARNING:: Runmode of the test '" + test_name + "' set to 'N'. So Skipping the test.");
			}
			printLogs("RunMode found as Y. Starting the test.");
			
			// THIS TEST IS MANUAL RIGHT NOW. RETURN MANUAL NOW.
			if(manual) {
				throw new Exception("INFO:: Manual Test");
			}
			
			//
			// Setup:
			//
			//1. Open HPSUM.
			if (!CommonHpsum.performTestSetup()) {
				throw new TestException("Test setup Failed");
			}
			
			//2. Perform Guided Update
			GuidedUpdate.compNumber = 2;
			if(!GuidedUpdate.performGuidedUpdate()){
				throw new TestException("Test setup Failed");
			}
			// 
			// Execution:			
			// Click on Start Over. 
			GuidedUpdatePg = GuidedUpdatePage.getInstance();
			if(!GuidedUpdatePg.GUStartOver.Click()){
				throw new TestException("Test Execution Failed");
			}
			
			//
			// Verification:
			//
			
			
			// Wait for the Localhost Guided Update dialog to come up.
			if(!PageCommon.WaitForPage(Pages.LocalhostGuidedUpdateDialog, iTimeout)){
				throw new TestException("Test Verification Failed");
			}
			
			captureScreenShot();
			printLogs("DONE.");
		}
		catch (Throwable t) {
			if (skip) {
				test_result = "SKIP";
			}
			else if (manual) {
				test_result = "MANUAL";
			}
			else {
				test_result = "FAIL";
				ErrorUtil.addVerificationFailure(t);
			}
		}
		finally {
			try {
				//
				// Cleanup:
				//
				printLogs("Test Cleanup:");
				
				// Get current time in ms.
				finalTime = getTimeInMsec();
				
				// Calculate test time interval.
				executionTime = calculateTimeInterval(initialTime, finalTime);
				
				// Write the final test result in html file.
				CommonHpsum.testCleanupAndReporting(test_name, test_result, executionTime);
				TestUtil.reportDataSetResult(suite_xl, "TestCases", TestUtil.getRowNum(suite_xl, test_name), test_result);
				printTestEndLine(test_name);
			}
			catch (Throwable t) {
				printError("Exception occurred in finally.");
			}
		}	
	}
}