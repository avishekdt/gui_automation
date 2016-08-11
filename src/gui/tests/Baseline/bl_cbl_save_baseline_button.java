package gui.tests.Baseline;

import java.io.File;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.SelUtil;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

//
//Assertion: 
// Verifies that Save BL button saves the BL and adds it to the BL page.
//

public class bl_cbl_save_baseline_button extends TestSuiteBase {	
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_bl_cbl_save_baseline_button() {
		screenshot_name 	= test_name;
		screenshot_counter 	= 0;
		String taskCompleteMsg = "The custom baseline has been saved and added to the list of baselines successfully"; // OLD message "Baseline has been saved successfully";
		int count = 0, maxTime = 270;
		boolean failureFlag = false;
		
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
			String outputLocation;
			String verText;
			
			// Set output location and Version name
			if(currentOsName.contains("windows")) {
				outputLocation = "C:/SPP/Custom-Baseline";
				verText = "W";
			}
			else {
				outputLocation = "/root/Desktop/SPP/Custom-Baseline";
				verText = "L";
			}
			String descText = test_name + "_" + verText;
			outputLocation = outputLocation + "_" + getTimeStamp();
			
			
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Wait for all the Baseline inventory to complete.
			if(!BaselineLibrary.performBaselineInventory()){
				throw new TestException("Test failed");
			}
			
			// 3. Go to Create Custom Baseline Page
			if(!guiSetPage("CreateCustomBaseline")) {
				throw new TestException("Test Failed");
			}
			
			// a. Specify description
			if (!SelUtil.sendKeysByXpath("BL_CBL_TextBox_Description", descText)) {
				printError("sendKeysByXpath BL_CBL_TextBox_Description failed.");
			}
			captureScreenShot();
			
			// b. Specify version text
			if(!SelUtil.clickByXpath("BL_CBL_Calendar_Version")) {
				printError("Failed to click on BL_CBL_Calendar_Version");
			}
			if(!SelUtil.clickByXpath("BL_CBL_TextBox_Version")) {
				printError("Failed to click on BL_CBL_TextBox_Version");
			}
			
			if (!SelUtil.sendKeysByXpath("BL_CBL_TextBox_Version", verText)) {
				printError("sendKeysByXpath BL_CBL_TextBox_Version failed.");
			}
			captureScreenShot();

			// 4. Create a new output location.
			if (!createFolder(outputLocation)) {
				printError("Failed to create folder: " + outputLocation);
				throw new TestException("Test Failed");
			}
			
			// c. Specify the Output location
			if(!SelUtil.sendKeysByXpath("BL_CBL_TextBox_OutLoc", outputLocation)) {
				printError("sendKeysByXpath BL_CBL_TextBox_OutLoc failed.");
				throw new TestException("Test Failed");
			}
			captureScreenShot();
			
			// d. Click on Apply Filters button.
			if(!SelUtil.clickByXpath("BL_CBL_Button_ApplyFilters")) {
				printError("Failed to click on BL_CBL_Button_ApplyFilters");
				throw new TestException("Test Failed");
			}
			captureScreenShot();
			
			// e. Wait for pop-up to close
			if(!SelUtil.waitForNoElementByXpath("BL_CBL_PopUp_FilterComps", 300)) {
				printError("Failed to wait for closing the popup window after clicking Apply Filters button.");
				throw new TestException("Test Failed");
			}
			captureScreenShot();
			
			// Verify the buttons state
			// Create ISO and save Baseline --> Enabled
			// Save Baseline --> Enabled
			if(!SelUtil.verifyButtonStatus("BL_CBL_Button_CreateIso", "enabled")) {
				printError("BL_CBL_Button_CreateIso not enabled as expected.");
			}
			
			if(!SelUtil.verifyButtonStatus("BL_CBL_Button_SaveBaseline", "enabled")) {
				printError("BL_CBL_Button_SaveBaseline not enabled as expected.");
			}
			
			if(!SelUtil.verifyButtonStatus("BL_CBL_Button_Reset", "enabled")) {
				printError("BL_CBL_Button_Reset not enabled as expected.");
			}
			
			if(!SelUtil.verifyButtonStatus("BL_CBL_Button_Close", "enabled")) {
				printError("BL_CBL_Button_Close not enabled as expected.");
			}
			
			// 
			// Execution::
			//
			// Click on the SaveBaseline button
			if(!SelUtil.clickByXpath("BL_CBL_Button_SaveBaseline")) {
				printError("Failed to click on BL_CBL_Button_SaveBaseline.");
			}
			captureScreenShot();
			sleep(30);
			
			//
			// Verification:
			//
			// Click on the bottom message.
			if (!SelUtil.clickByXpath("BL_CBL_Text_BottomMsg")) {
				printError("Failed to click on BL_CBL_Text_BottomMsg");
			}
			captureScreenShot();
			
			// Wait for completion message to appear
			
			while (!SelUtil.getTextByXpath("BL_CBL_Text_BottomMsg").contains(taskCompleteMsg)) {
				sleep(10);
				printLogs(SelUtil.getTextByXpath("BL_CBL_Text_BottomMsg"));
				count ++;
				if(count > maxTime) {
					captureScreenShot();
					String errorMsg = "Save Baseline took longer than " + ((maxTime * 10)/60) + "mins to finish";
					printError(errorMsg);
					throw new TestException("Test Failed");
				}
			}
			captureScreenShot();
			
			// Close the CBL page.
			if (!BaselineLibrary.closeCblPageUsingCloseButton()) {
				printError("Failed to close the CBL page.");
				failureFlag = true;
			}
			
			// Verify if the BL components got saved in the assigned folder: outputLocation
			int fileCount = new File(outputLocation).listFiles().length;
			if (fileCount < 5) {
				printError("Files not found in save baseline location. Number of files in the location: " + outputLocation + " = " + fileCount);
				failureFlag = true;
			}
			else {
				printLogs("Saved baseline in the folder:" + outputLocation + " Components found=" + fileCount);
			}
			
			// Verify if BL got added on the BL page.
			if (!BaselineLibrary.ifBlExists(descText)){
				printError("Saved BL not found in the added list of the BLs on the BL page.");
				failureFlag = true;
			}
			
			if (failureFlag) {
				throw new TestException("Test Failed");
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