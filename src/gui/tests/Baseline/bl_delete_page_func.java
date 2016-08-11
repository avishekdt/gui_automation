package gui.tests.Baseline;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.pages.BaselinePage;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;



;//Assertion: 
// Delete window , it should have "Yes delete" button and 'Cancel" button
//
public class bl_delete_page_func extends TestSuiteBase{
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	BaselinePage Baseline = BaselinePage.getInstance();	
	@Test
	public void test_bl_delete_page_func() {
		screenshot_name = test_name;
		screenshot_counter = 0;		
		
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
			
			// Create additional baseline
			String ablPath = createAblFolder(5, "exe");
			
			// 2. Wait for all the Baseline inventory to complete.
			if(!BaselineLibrary.performBaselineInventory()){
				throw new TestException("Test failed");
			}
			
			//3. Add an additional Baseline
			if(!BaselineLibrary.addAdditionalBaseline(ablPath)){
				throw new TestException("Test failed");
			}
			
			//4. Wait for baseline Inventory
			if(!BaselineLibrary.performBaselineInventory()){
				throw new TestException("Test failed");
			}
			
			//5. Select the Actions->Delete
			selectActionDropDownOption("CssBaselineActions", "Delete");
			
			// Wait for the Confirm delete popup page
			BaselineLibrary.WaitForPage("BaselineLibrary_DeleteConfirmPopupHeading");
			
			//Verify
			if(!Baseline.DeleteConfirmYes.isEnabled()) {
				printLogs("The Confirm Delete buttom is disabled" +
							"Expected: Enabled" +
							"Actual: Disabled");
				throw new TestException("Test Failed");
			}
			if(!Baseline.DeleteConfirmCancel.isEnabled()) {
				printLogs("The Cancel buttom is disabled" +
						"Expected: Enabled" +
						"Actual: Disabled");
				throw new TestException("Test Failed");
			}
			
			//
			// Execution:
			//			
			// Click on cancel button.
			Baseline.DeleteConfirmCancel.Click();
			
			// Wait for the Baseline Page
			BaselineLibrary.WaitForPage("BaselineLibrary_Heading");
			
			// Click on delete from actions menu in nodes page.
			sleep(5);
			if(!selectActionDropDownOption("CssBaselineActions", "Delete")) {
				
			}
			
			// Wait for the Delete baseline popup
			BaselineLibrary.WaitForPage("BaselineLibrary_DeleteConfirmPopupHeading");
			
			// Click on delete button.
			Baseline.DeleteConfirmYes.Click();
			
			//
			// Verification:
			if(BaselineLibrary.ifBlExists(ablPath))
			{
				captureScreenShot();
				printLogs(ablPath+" is present in Baseline list in Baseline page, deleted successfully.");
				throw new TestException("Test Failed");
			} 	
			
			captureScreenShot();
			printLogs("DONE.");
		}
		catch (Throwable t) {
			if (skip) {
				test_result = "SKIP";
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
