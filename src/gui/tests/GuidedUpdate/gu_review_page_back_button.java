package gui.tests.GuidedUpdate;

import gui.common.base.CommonHpsum;
import gui.common.base.GuidedUpdate;
import gui.common.base.GuidedUpdate.GU_Mode;
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

public class gu_review_page_back_button extends TestSuiteBase {	
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	int iTimeout = 240;
	
	GuidedUpdatePage GuidedUpdatePg = null;
	
	@Test
	public void test_gu_review_page_back_button() {
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
			//1. Open HPSUM.
			if (!CommonHpsum.performTestSetup()) {
				throw new TestException("Test setup Failed");
			}
			
			//2. Perform Inventory Update
			// Handle the Automatic or interactive popup.
			if(!GuidedUpdate.handleGUModeAndBaseline(GU_Mode.GU_INTERACTIVE, null, null)){
				throw new TestException("Test Setup Failed");
			}

			// 3. Wait for inventory to happen.
			if(!GuidedUpdate.guidedUpdateWaitForInventory(iTimeout)){
				throw new TestException("Test Setup Failed");
			}
			
			// 4. Click on Next.
			GuidedUpdatePg = GuidedUpdatePage.getInstance();
			if(!GuidedUpdatePg.GUStep1Next.Click()){
				throw new TestException("Test Failed");
			}			

			// 5. On Review screen
			if(!PageCommon.WaitForPage(Pages.GuidedUpdateStep2, iTimeout)){
				throw new TestException("Test Failed");
			}
			
			// 
			// Execution::
			// Click on Back button
			if(!GuidedUpdatePg.ReviewPreviousStep.Click()){
				throw new TestException("Test Failed");
			}	
			
			//
			// Verification:
			// Verify that Step 1 screen is displayed
			if(!PageCommon.WaitForPage(Pages.GuidedUpdateStep1, iTimeout)){
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