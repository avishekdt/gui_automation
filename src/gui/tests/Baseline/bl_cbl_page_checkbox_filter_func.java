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
//<Write the purpose of the test case>
//

public class bl_cbl_page_checkbox_filter_func extends TestSuiteBase {	
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_bl_cbl_page_checkbox_filter_func() {
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
			
			/*
			   1.In the Filters section, 
      		a] check match cloudsystem matrix version.
      		b] Select the component type in component type drop down list.
      		c]Check "Critical updates'
       		d]check "Recommended updates
       		e] check "option updates'
       
       EXPECTED:::::::
       		a]selected this option, it listed 'matrix defintion file XML" and select matrix xml. and it should hide component type , critical , optional and recommended updates check box. and bottom settings button should not list.
 			b]if "CSM" not checked, able to see other options. selected component type show in the drop down list.
			c]select "critical updates' option. in bottom settings will give the count number.
			d]select "recommended updates' option. in bottom settings will give the count number.
			e]select "optional updates' option. in bottom settings will give the count number.
			 
			 */
			
			
			
			// 
			// Execution::
			//

			
			//
			// Verification:
			//
			
			
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