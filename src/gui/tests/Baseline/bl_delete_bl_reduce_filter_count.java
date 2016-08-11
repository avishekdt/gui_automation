package gui.tests.Baseline;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

//
//Assertion: 
//Add/delete multiple BLs and see the count at the top heading matches. 
//

public class bl_delete_bl_reduce_filter_count extends TestSuiteBase {	
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_bl_delete_bl_reduce_filter_count() {
		screenshot_name 	= test_name;
		screenshot_counter 	= 0;
		String ablPath_1 = "";
		String ablPath_2 = "";
		String ablPath_3 = "";
		String abl_name = "";
		int initialBlCount = 0;
		
		activityScreenRows_counter = 0;
		
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
			
			//
			// Setup:
			//
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			// Wait for Baseline Inventory to complete
						if(!BaselineLibrary.performBaselineInventory()) {
							throw new TestException("Test failed to performBaselineInventory");
						}
			
			
			// 2. Create three locations to be used for additional baselines locations
			ablPath_1 = createAblFolder(5, "exe");
			ablPath_2 = createAblFolder(5, "rpm");
			ablPath_3 = createAblFolder(5, "zip");
			
			// 3. Go to Add BL Page
			if(!guiSetPage("BaselineLibrary")) {
				throw new TestException("Test Failed");
			}
			
			initialBlCount 	 = BaselineLibrary.getBlCount();
			int availableBls = 0;
			int expectedBls  = 0;
			int blCountInHeading = 0;
			String countText = "";
			
			// 4. Add ABLs one by one and verify the count
			for (int i = 1; i <= 3 ; i++) {
				//latestActivityRows = null;
				
				// Additional baseline to use
				if (i == 1) {
					abl_name = ablPath_1;
				}
				if (i == 2) {
					abl_name = ablPath_2;
				}
				if (i == 3) {
					abl_name = ablPath_3;
				}
				
				printLogs("Verifying add data for ABL: " + abl_name);
				
				// 
				// Execution::
				//
				// Add the ABL
				if(!BaselineLibrary.addAdditionalBaseline(abl_name)) {
					throw new TestException("Test failed");
				}
				sleep(10); 
				
				// Wait for Baseline Inventory to complete
				//if(!BaselineLibrary.performBaselineInventory()) {
					//throw new TestException("Test failed to performBaselineInventory");
				//}
				
				// Count the existing BLs
				availableBls = BaselineLibrary.getBlCount();
				expectedBls = initialBlCount + i;
				
				if (availableBls != expectedBls) {
					printError("Available BL count is not expected. Expected: "+ expectedBls + " Found: " + availableBls);
				}
				else {
					printLogs("As expected BL count after BL Add is : " + availableBls);
				}
				
				//
				// Verification:
				//
				// Get the text from top-panel heading showing the BL count
				countText = BaselineLibrary.getBlCountTextFromBlPageTopHeading();
				blCountInHeading = Integer.parseInt(countText);
				
				if (blCountInHeading != availableBls) {
					printError("BL heading count: " + blCountInHeading + " did not match actual count: " + availableBls);
				}
				else {
					printLogs("BL heading count text matches total BLs count: " + blCountInHeading);
				}
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