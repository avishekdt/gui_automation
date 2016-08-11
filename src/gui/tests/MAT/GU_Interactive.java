package gui.tests.MAT;

import gui.common.base.CommonHpsum;
import gui.common.base.GuidedUpdate;
import gui.common.base.SelUtil;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

public class GU_Interactive extends TestSuiteBase {
	String test_name = this.getClass().getSimpleName();
	static boolean skip = false;
	static boolean fail = false;
	String test_result = "PASS";
	
	long initialTime;
	long finalTime;
	String executionTime = "";
	
	@Test
	public void test_GU_Interactive() {
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
			
			/* 
			 * Execution::
			 * 1. Select Localhost Guided Update from the Main Menu.
			 * 2. Select Interactive and Click OK
			 * 3. Wait for inventory to complete and also verify the buttons status
			 * 4. Click the Next button.
			 * 5. Get all the table contents and then find out which component has failed dependencies, and then de-select those.
			 * 6. Click the Analyze and then Deploy button
			 * 7. Verify deployment complete and Return the table contents.
			 */
			
			// 1. Select Localhost Guided Update from the Main Menu.
			if(!CommonHpsum.clickLinkOnMainMenu("MainMenu_GuidedUpdate")) {
				throw new TestException("Test Failed");
			}
			
			// wait for Localhost Guided Update popup to appear
			//waitForDialogBox(GuidedUpdate_PopupDialog);
			if(!SelUtil.waitForPage("GuidedUpdate_PopupDialog")) {
				throw new TestException("Test Failed");
			}
			
			// 2. Select Interactive and Click OK
			// Select Interactive
			if(!SelUtil.clickByXpath("GuidedUpdate_InteractiveRadioButton")) {
				throw new TestException("Test Failed");
			}
			
			captureScreenShot();
			
			// Click OK Button
			if(!SelUtil.clickByXpath("GuidedUpdate_OkButton")) {
				throw new TestException("Test Failed");
			}
			
			// Wait for Localhost Guided Update popup to disappear
			if(!SelUtil.waitForNoElementByXpath("GuidedUpdate_PopupDialog")) {
				throw new TestException("Test Failed");
			}
			
			captureScreenShot();
			
			// 3. Verify page, Wait for inventory to complete and also verify the buttons status
			// Verify Page: Inventory of baseline and node
			if(!SelUtil.verifyPage("GuidedUpdate_Step1")) {
				throw new TestException("Test Failed");
			}
			
			// Verify the buttons status before the Inventory completes
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step1NextButton", "disabled", true)) {
				fail = true;
			}
			
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step1AbortButton", "disabled")) {
				fail = true;
			}
			
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step1ResetButton", "disabled")) {
				fail = true;
			}
			
			// Wait for Inventory complete
			sleep(2);	// waiting for tables to get populated.
			
			if(!GuidedUpdate.guidedUpdateWaitForInventoryToComplete("GuidedUpdate_BlTable")) {
				throw new TestException("Test Failed");
			}
			
			//if(!verifyButtonStatus("GuidedUpdate_Step1AbortButton", "enabled")) {
				//fail = true;
			//}
			
			if(!GuidedUpdate.guidedUpdateWaitForInventoryToComplete("GuidedUpdate_NodeTable")) {
				throw new TestException("Test Failed");
			}
			
			// After the Inventory completes::			
			// Verify inventory complete icons
			sleep(5);
			
			captureScreenShot();
			
			// Verify the buttons status
			// Next  - Enabled and Primary
			// Abort - Disabled
			// Reset - Enabled
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step1NextButton", "enabled", true)) {
				fail = true;
			}
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step1AbortButton", "disabled")) {
				fail = true;
			}
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step1ResetButton", "enabled")) {
				fail = true;
			}
			
			// Verify the presence of Actions drop-down
			// Get elements of the drop down
			
			
			// 4. Click the Next button.
			if(!SelUtil.clickByXpath("GuidedUpdate_Step1NextButton")) {
				throw new TestException("Test Failed");
			}
			
			captureScreenShot();
			
			// Wait for step 2 page
			if(!SelUtil.checkElementPresenceByXpath("GuidedUpdate_Step2Heading")) {
				throw new TestException("Test Failed");
			}
			
			if(!SelUtil.verifyPage("GuidedUpdate_Step2")) {
				fail = true;
				//throw new TestException("Test Failed");
			}
			
			
			// Workaround to bring the control to second frame.
			if(!SelUtil.clickByXpath("GuidedUpdate_SubHeadingNode1InstallSet")) {
					throw new TestException("Test Failed");
			}
			
			captureScreenShot();
			
			if(!SelUtil.clickByXpath("GuidedUpdate_SubHeadingNode1InstallSet")) {
				throw new TestException("Test Failed");
			}
			
			// Verify the buttons status
			// Back  - Enabled
			// Analyze - Disabled
			// Deploy - Disabled
			// Reset - Enabled
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step2BackButton", "enabled")) {
				fail = true;
			}
			
			//if(!verifyButtonStatus("GuidedUpdate_Step2DeployButton", "disabled")) {
				//fail = true;
			//}
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step2ResetButton", "enabled")) {
				fail = true;
			}
			
			//captureScreenShot(name_screenshot + count_screenshot++);
			
			// 5. Get all the table contents and then find out which component has failed dependencies, and then Unselect those.
			
			// Print the table contents
			if(!GuidedUpdate.analyzeGu2TableContents()) {
				//throw new TestException("Test Failed");
				fail = true;
			}
			
			// Verify the buttons status
			// Back  - Enabled
			// Analyze - Enable
			// Deploy - Enabled, Primary
			// Reset - Enabled
			/*if(!verifyButtonStatus("GuidedUpdate_Step2BackButton", "enabled")) {
				fail = true;
			}
			
			
			if(!verifyButtonStatus("GuidedUpdate_Step2DeployButton", "enabled", true)) {
				//fail = true;
				printError("Deploy button NOT enabled. Should have been.");
				if(!clickByXpath("GuidedUpdate_SubHeadingNode1InstallSet")) {
					throw new TestException("Test Failed");
				}
				captureScreenShot();
				throw new TestException("Test Failed");
			}
			if(!verifyButtonStatus("GuidedUpdate_Step2ResetButton", "enabled")) {
				fail = true;
			}*/
			
			/*// Click the deploy button
			if(!clickByXpath("GuidedUpdate_Step2DeployButton")) {
				// Put workaround till 6.2 problem is NOT solved.
				if(!clickByXpath("GuidedUpdate_SubHeadingNode1InstallSet")) {
					throw new TestException("Test Failed");
				}
				sleep(2);
				if(!clickByXpath("GuidedUpdate_Step2DeployButton")) {
					printLogs("Failed with workaround also.");
					throw new TestException("Test Failed");
				}
			}*/
			
			// Run the AutoIT script to close the popups which come for unauthorized components.
			if (currentOsName.contains("windows")) {
				if(!executeScript("autoItDeployPopUpScript", false)) {
					fail = true;
				}
			}
			
			// Adding this sleep until we add code for clicking analyze button and waiting for Analysis to complete.
			sleep(10);
			
			captureScreenShot();
			
			// wait for step3 page
			if(!SelUtil.checkElementPresenceByXpath("GuidedUpdate_Step3Heading")) {
				throw new TestException("Test Failed");
			}
			
			// 7. Verify deployment complete and Return the table contents.
			if(!SelUtil.verifyPage("GuidedUpdate_Step3")) {
				fail = true;
				//throw new TestException("Test Failed");
			}
			
			// Verify the buttons status
			// Reset  - Enabled
			// Abort - Enable
			// Reboot Now - Disabled
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step3ResetButton", "enabled")) {
				fail = true;
			}
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step3AbortButton", "enabled")) {
				fail = true;
			}
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step3RebootNowButton", "disabled")) {
				fail = true;
			}

			// wait for deployment to complete
			if(!GuidedUpdate.guidedUpdateWaitForDeploymentToComplete("GuidedUpdate_Step3DeploymentTable")) {
				throw new TestException("Test Failed");
			}
			
			// wait till table gets populated.
			sleep(5);
			
			
			// Print the final table contents
			if(!GuidedUpdate.analyzeViewGu3Table()) {
				fail = true;
			}
			
			captureScreenShot();
			
			// Verify the buttons status
			// Reset  - Enabled
			// Abort - Disabled
			// Reboot Now - Enabled
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step3ResetButton", "enabled")) {
				fail = true;
			}
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step3AbortButton", "disabled")) {
				fail = true;
			}
			if(!SelUtil.verifyButtonStatus("GuidedUpdate_Step3RebootNowButton", "enabled")) {
				fail = true;
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
				
				// Kill the AutoIT script and close the browser
				if (currentOsName.contains("windows")) {
					if(!executeScript("killAutoItDeployPopUpScript", false)) {
						printLogs("WARNING:: Failed to killAutoItDeployPopUpScript");
					}
				}
				
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
