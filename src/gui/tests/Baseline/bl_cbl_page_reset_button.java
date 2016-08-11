package gui.tests.Baseline;

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
// Verify the functionality of RESET button on the CBL page.
//

public class bl_cbl_page_reset_button extends TestSuiteBase {	
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_bl_cbl_page_reset_button() {
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
			
			//
			// Setup:
			//
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
			
			// 4. Change some values in the fields:
			// Put text in textbox- Description
			// Put text in textbox- Version
			// Put text in textbox- Output Location
			// Check the checkbox - Make Bootable ISO file
			// Drop-Down - Select HP SUM Source Location - value changed to 'Extracted source ISO directory'
			// Check the checkbox - Run in background
			// Check the checkbox - Critical Updates
			// Check the checkbox - Recommended Updates
			// Check the checkbox - Optional Updates
			// Check the checkbox - Advanced Filters->Architecture->x86 and x64
			// Press button - Apply Filters : Verify the component table appears
			// 
			String descText 	= "Dummy Description";
			String verText  	= "xx";
			String outLocText	= "Dummy Location";
			
			SelUtil.sendKeysByXpath("BL_CBL_TextBox_Description", descText);
			SelUtil.sendKeysByXpath("BL_CBL_TextBox_Version", verText);
			SelUtil.sendKeysByXpath("BL_CBL_TextBox_OutLoc", outLocText);
			SelUtil.clickByXpath("BL_CBL_CheckBox_MakeBootableIso");
			SelUtil.clickByXpath("BL_CBL_CheckBox_RunBackInBackground");
			captureScreenShot();
			
			SelUtil.clickByXpath("BL_CBL_CheckBox_Critical");
			SelUtil.clickByXpath("BL_CBL_CheckBox_Recom");
			SelUtil.clickByXpath("BL_CBL_CheckBox_Optional");
			captureScreenShot();
			
			// 
			// Execution::
			//
			// Click on the Reset button.
			SelUtil.clickByXpath("BL_CBL_Button_Reset");
			printLogs("Successfully pressed the Reset button.");
			sleep(2);
			
			//
			// Verification:
			//
			// Verify - after Reset
			// Blank text in textbox- Description
			// Blank text in textbox- Version
			// Blank text in textbox- Output Location
			// Un-checked checkbox - Make Bootable ISO file
			// Drop-Down - Select HP SUM Source Location - value reset to 'Current working directory'
			// Un-checked checkbox - Run in background
			// Un-checked checkbox - Critical Updates
			// Un-checked checkbox - Recommended Updates
			// Un-checked checkbox - Optional Updates
			// Un-checked checkbox - Advanced Filters->Architecture->x86 and x64
			// The component table disappears
			String deskText_reset 	= SelUtil.getTextByXpath("BL_CBL_TextBox_Description");
			String verText_reset  	= SelUtil.getTextByXpath("BL_CBL_TextBox_Version");
			String outLocText_reset = SelUtil.getTextByXpath("BL_CBL_TextBox_OutLoc");
			
			SelUtil.scrollPageUp();
			captureScreenShot();
			
			if (deskText_reset.contentEquals(descText)) {
				printError("Description Text did not reset.Expected: Blank Actual: " + deskText_reset);
			}
			if (verText_reset.contentEquals(verText)) {
				printError("Version Text did not reset.Expected: Blank Actual: " + verText_reset);
			}
			if (outLocText_reset.contentEquals(outLocText)) {
				printError("Output Location Text did not reset.Expected: Blank Actual: " + outLocText_reset);
			}
			
			SelUtil.verifyCheckboxStatus("BL_CBL_CheckBox_MakeBootableIso", "disabled");
			SelUtil.verifyCheckboxStatus("BL_CBL_CheckBox_RunBackInBackground", "disabled");
			
			SelUtil.scrollPageDown();
			
			SelUtil.verifyCheckboxStatus("BL_CBL_CheckBox_Critical", "disabled");
			SelUtil.verifyCheckboxStatus("BL_CBL_CheckBox_Recom", "disabled");
			SelUtil.verifyCheckboxStatus("BL_CBL_CheckBox_Optional", "disabled");
			
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