package gui.tests.Baseline;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.SelUtil;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;
import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

// Assertion: 
// If a new baseline location is provided on the Add BL page and add button is pressed
// the the new Baseline should get added.
// 

public class bl_add_from_action_menu extends TestSuiteBase {
	String test_name = this.getClass().getSimpleName();
	
	static boolean skip = false;
	static boolean fail = false;
	String test_result = "PASS";
	
	long initialTime;
	long finalTime;
	String executionTime = "";
	
	// Put here if anything needed before starting test.
	@BeforeTest
	public void checkTestSkip() {
		printTestStartLine(test_name);
	}
	
	// Start the test.
	@Test
	public void test_bl_add_from_action_menu() {
		screenshot_name = test_name;
		screenshot_counter = 0;
		String ablPath = "";
		
		try {
			//
			// Environment:
			//
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
			//
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// Create locations to be used for additional baselines locations
			ablPath = createAblFolder(5, "exe");
			
			// 2. Go to Add BL Page using the 'Add' button
			if(!guiSetPage("AddBaseline")) {
				throw new TestException("Test Failed");
			}
			
			// Close the Add-BL screen
			if(!SelUtil.clickByXpath("BaselineLibrary_AddBaselineCloseButton")){
				throw new TestException("Test failed");
			}
			
			// Wait for Add window to close
			if(!SelUtil.waitForNoElementByXpath("BaselineLibrary_AddBaselineHeading")){
				printError("Baseline Add window did NOT close after pressing the Add button. Should have been.");
				throw new TestException("Test failed");
			}
			
			printLogs("Successfully verified Add button of the Baseline Page and closed the Add Baseline page.");
			captureScreenShot();
			printLogs("Opening the Add Baseline Page using the Actions-Add drop-down.");
			
			// Go to Add Baseline Page using the Actions->Add drop-down
			if(!selectActionDropDownOption("CssBaselineActions", "Add")) {
				printError("Failed to select Add from the Actions drop-down on the BL page.");
				throw new TestException("Test failed");
			}
			
			if (!SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHeading")) {
				printError("Failed to reach Add BL page.");
				throw new TestException("Test failed");
			}
			
			captureScreenShot();
			
			// 3. Click on select location type drop down
			if(!SelUtil.clickByXpath("BaselineLibrary_AddBaselineLocationTypeDropDown")){
				throw new TestException("Test failed");
			}
			
			captureScreenShot();
			
			// 4. Click on Location Type: Browse HP SUM server path
			if(!SelUtil.clickByXpath("BaselineLibrary_AddBaselineLocationTypeBrowseHpsumPath")){
				printError("unable to click on Browse HP SUM server path ");
			}
			captureScreenShot();
			
			// 5. Enter the ABL directory path
			if(!SelUtil.sendKeysByXpath("BaselineLibrary_AddBaselineEnterDirectoryPathInput", ablPath)) {
				throw new TestException("Test failed");	
			}
			
			// 6. Click on browse button
			if(!SelUtil.clickByXpath("BaselineLibrary_AddBaselineEnterDirectoryPathBrowseButton")){
				throw new TestException("Test failed");
			}
			
			// Wait for browse window
			if(!SelUtil.waitForPage("BaselineLibrary_AddBaselineEnterDirectoryPathBrowseHeading")){
				throw new TestException("Test failed");
			}
			
			captureScreenShot();
								
			// 7. Select the path in browse window
			if(!SelUtil.clickByXpath("BaselineLibrary_AddBaselineBrowsepath")){
				printError("Unable to select path");
				throw new TestException("Test Failed");
			}
			
			captureScreenShot();
			
			// 8. Click on ok button
			if(!SelUtil.clickByXpath("BaselineLibrary_AddBaselineEnterDirectoryPathBrowseOkButton")){
				throw new TestException("Test failed");
			}
			
			// Wait for Browse window to close
			if(!SelUtil.waitForNoElementByXpath("BaselineLibrary_AddBaselineEnterDirectoryPathBrowseHeading")){
				throw new TestException("Test failed");
			}
			
			captureScreenShot();
			
			// 
			// Execution:
			//
			//   Click on the Add button.
			//
			if(!SelUtil.clickByXpath("BaselineLibrary_AddBaselineAddButton")) {
				throw new TestException("Test Failed");
			}
			
			// Wait for Add window to close
			if(!SelUtil.waitForNoElementByXpath("BaselineLibrary_AddBaselineHeading")){
				printError("Baseline Add window did NOT close after pressing the Add button. Should have been.");
				
				// Close the window using the close button.
				printLogs("Closing the window using the Close button.");
				if(!SelUtil.clickByXpath("BaselineLibrary_AddBaselineCloseButton")){
					throw new TestException("Test failed");
				}
			}
			
			captureScreenShot();
			
			//
			// Verification:
			//
			// 1. Verify that the baseline inventory completes.
			//
			if(!BaselineLibrary.WaitForBaselineInventoryToComplete()){
				throw new TestException("Test failed");
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
				printError("Exception occurred while taking screen-shot.");
				t.printStackTrace();
			}
		}
	}
}
