package gui.tests.Baseline;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.SelUtil;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;
import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

// Assertion: 
// Verify all the elements on the Add Baseline screen and check the
// functionality of Close button.
// 

public class bl_add_bl_page_verify extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_bl_add_bl_page_verify() {
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
			
			// 2. Go to Add BL Page
			if(!guiSetPage("AddBaseline")) {
				throw new TestException("Test Failed");
			}
			
			// 
			// Execution::
			// Select the various(4) drop down options (3 in Linux) from 'Select the location type' drop-down 
			// and verify the screen elements.
			// 
			
			// Verify buttons and buttons-Status
			// 		Add - Disabled
			//		StartOver - Enabled
			//		Close - Enabled
			//		Help
			//		Click 'Help' button and verify it opens the Help window. 
			if (!SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineAddButton")) {
				printError("Failed to verify BaselineLibrary_AddBaselineAddButton");
			}
			if (!SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineStartOverButton")) {
				printError("Failed to verify BaselineLibrary_AddBaselineStartOverButton");
			}
			if (!SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineCloseButton")) {
				printError("Failed to verify BaselineLibrary_AddBaselineCloseButton");
			}
			if (!SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHelp")) {
				printError("Failed to verify BaselineLibrary_AddBaselineHelp");
			}
			
			//   Click on the Help (?) button.
			if(!SelUtil.clickByXpath("Common_NewWindowHelp")) {
				printError("Failed to click on Help button.");
			}
			
			// Verify that the heading on the help page matches the expected text.
			if (!helpWindowHeadingCompare("Adding a baseline")) {
				printError("Add BL help page heading not as expected.");
			}
			
			int optionToSelect = 1;
			
			for (int i = 1 ; i <= 4 ; i++) {
				switch (i) {
					case 1:
						//
						// *********** Option - 1 ********************************************************
						//
						optionToSelect = 1;
						
						// Select the 'Browse HP SUM server path' (Option-1) from the Location Type drop-down
						BaselineLibrary.addBlSelectLocTypeDdOption(optionToSelect);
						
						// Verify the screen elements
						// Heading	: Location type
						// Label	: Select the location type
						// Heading	: Location Details
						// Text		: Browse or manually enter a directory path where the components for the baseline are located.
						// Label	: Enter directory path
						// Field	: Text-box: Enter dir path
						// Field	: Button: Browse

						SelUtil.verifyText("BaselineLibrary_AddBaselineLocationTypeLabel", "Location Type");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineSelectTheLocationTypeText", "Select the location type");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineLocationDetailsLabel", "Location Details");
						SelUtil.verifyText("BaselineLibrary_AddBaselineLocationDetailsText", "Browse or manually enter a directory path where the components for the baseline are located.");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineEnterDirectoryPathText", "Enter directory path");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineEnterDirectoryPathInput");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineEnterDirectoryPathBrowseButton");
						
						printLogs("Successfully verified Browse HP SUM server path");
						break;
						
					case 2:
						//
						// *********** Option - 2 ********************************************************
						//
						// UNC path option is NOT present in Linux, hence skip it.
						if (!currentOsName.contains("windows")) {
							break;
						}
						optionToSelect = 2;
						
						// Select the 'UNC path (for example \\host\dir)' (Option-2) from the Location Type drop-down
						BaselineLibrary.addBlSelectLocTypeDdOption(optionToSelect);
						
						// Verify the screen elements
						// Heading	: Location type				--> same, IGNORE
						// Label	: Select the location type	--> same, IGNORE
						// Heading	: Location Details			--> same, IGNORE
						// Text		: Enter an URI where the components for the baseline are located.
						// Label	: Enter URI for the baseline
						// Field	: Text-box: Enter URI
						// Heading	: Credentials
						// Label	: Use current credentials (requires existing trust relationship with the node).
						// Label	: Enter administrator credentials
						// Field	: RadioButton1
						// Field	: RadioButton2
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineUriLocationDetailsText", "Enter a URI where the components for the baseline are located.");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineUriEnterUriText", "Enter URI for the baseline");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineUriEnterUriInput");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineUriCredLabel", "Credentials");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineUriCredRadio1Label", "Use current credentials (requires existing trust relationship with the node).");
						SelUtil.verifyText("BaselineLibrary_AddBaselineUriCredRadio2Label", "Enter administrator credentials");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineUriCredRadio1");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineUriCredRadio2");
						
						// Select the 'Enter administrator credentials' Radio button and verify the text and fields
						// Texts & Fields:
						//		Username & corresponding field
						//		Password & corresponding field
						
						SelUtil.clickByXpath("BaselineLibrary_AddBaselineUriCredRadio2");
						sleep(1);
						captureScreenShot();
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineUriAdminUserLabel", "Username");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineUriAdminUserInput");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineUriAdminPwdLabel", "Password");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineUriAdminPwdInput");
						
						printLogs("Successfully verified UNC path");
						break;
						
					case 3:
						//
						// *********** Option - 3 ********************************************************
						//
						if (!currentOsName.contains("windows")) {
							optionToSelect = 2;
						}
						else {
							optionToSelect = 3;
						}
						
						// Select the 'Download from hp.com' (Option-3) (Option-2 in Linux) from the Location Type drop-down
						BaselineLibrary.addBlSelectLocTypeDdOption(optionToSelect);
						
						// Verify the screen elements
						// Heading	: Location type				--> same, IGNORE
						// Label	: Select the location type	--> same, IGNORE
						// Heading	: Location Details			--> same, IGNORE
						// Text		: Browse or manually enter a directory path where the Available Web Bundles list and components being downloaded from hp.com can be stored. Please select an empty folder.
						// Label	: Enter directory path
						// Field	: Text-box: Enter directory path
						// Field	: Button: Browse
						// Heading	: Proxy Settings
						// Label	: Proxy Options
						// Field	: Drop-down: Proxy options
						// Field	: Button: Retrieve - Disabled

						SelUtil.verifyText("BaselineLibrary_AddBaselineHpLocationDetailsText", "Browse or manually enter a directory path where the Available Web Bundles list and components being downloaded from hp.com can be stored. Please select an empty folder.");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHpEnterWdirLabel", "Enter directory path");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHpEnterWdirInput");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHpEnterWdirBrowse");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHpProxyLabel", "Proxy Settings");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHpProxyDropDownLabel", "Proxy Options");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHpProxyDropDown");
						
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHpRetrieveListButton");
						
						// Select Proxy server (Option-2) from the Proxy Settings drop-down
						// and verify the elements which get displayed after the selection.
						BaselineLibrary.addBlPfwSelectProxyDdOption(2);
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHpProxyServerAddrLabel", "Address");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHpProxyServerAddr");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHpProxyServerPortLabel", "Port");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHpProxyServerPort");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHpProxyServerUserLabel", "Username");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHpProxyServerUser");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHpProxyServerPwdLabel", "Password");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHpProxyServerPwd");
						
						// Select Proxy Script (Option-3) from the Proxy Settings drop-down
						// and verify the elements which get displayed after the selection.
						BaselineLibrary.addBlPfwSelectProxyDdOption(3);
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHpProxyScriptPathLabel", "Automatic Configuration Script");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHpProxyScriptPath");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHpProxyScriptUserLabel", "Username");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHpProxyScriptUser");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHpProxyScriptPwdLabel", "Password");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHpProxyScriptPwd");
						
						printLogs("Successfully verified Download from hp.com");
						break;
						
					case 4:
						//
						// *********** Option - 4 ********************************************************
						//
						if (!currentOsName.contains("windows")) {
							optionToSelect = 3;
						}
						else {
							optionToSelect = 4;
						}
						
						// Select the 'Download from http share' (Option-4) (Option-3 in Linux) from the Location Type drop-down
						BaselineLibrary.addBlSelectLocTypeDdOption(optionToSelect);
						
						// Verify the screen elements
						// Heading	: Location type				--> same, IGNORE
						// Label	: Select the location type	--> same, IGNORE
						// Heading	: Location Details			--> same, IGNORE
						// Text		: Enter a directory path where the components being downloaded can be stored. Please select an empty folder.
						// Label	: Enter directory path
						// Field	: Text-box: Enter directory path
						// Field	: Button: Browse
						// Heading	: HTTP Share URL
						// Text		: Provide an HTTP URL to validate a bundle file (bpxxxxxx.xml). All components must be present in the same directory on the HTTP server.
						// Label	: Enter HTTP URL
						// Field	: Text-box: Enter HTTP url
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHttpLocationDetailsText", "Enter a directory path where the components being downloaded can be stored. Please select an empty folder.");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHttpEnterWdirLabel", "Enter directory path");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHttpEnterWdirInput");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHttpEnterWdirBrowse");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHttpShareSubheadingLabel", "HTTP Share URL");
						SelUtil.verifyText("BaselineLibrary_AddBaselineHttpSubheadingText", "Provide an HTTP URL to validate a bundle file (bpxxxxxx.xml). All components must be present in the same directory on the HTTP server.");
						
						SelUtil.verifyText("BaselineLibrary_AddBaselineHttpUrlLabel", "Enter HTTP URL");
						SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHttpUrl");
						
						printLogs("Successfully verified Download from http share");
						break;
						
					default:
						printError("WARNING:: Incorrect drop-down option specified.");
						break;
				}	
			}
			
			// Press close button			
			if(!SelUtil.clickByXpath("BaselineLibrary_AddBaselineCloseButton")){
				printError("Failed to click on close button.");
			}
			captureScreenShot();
			
			//
			// Verification:
			//
			// The Add Baseline window closes
			if(!SelUtil.waitForNoElementByXpath("BaselineLibrary_AddBaselineHeading")){
				printError("Baseline Add window did NOT close after pressing the close button. Should have been.");
				
				// Close the window using the close button.
				printLogs("Retrying closing the window using the Close button.");
				if(!SelUtil.clickByXpath("BaselineLibrary_AddBaselineCloseButton")){
					throw new TestException("Test failed");
				}
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
			printLogs("DONE");
		}
	}
}
