package gui.tests.GuidedUpdate;

import gui.common.base.CommonHpsum;
import gui.common.base.GuidedUpdate.GU_Mode;
import gui.common.pages.GuidedUpdatePage;
import gui.common.pages.PageCommon;
import gui.common.pages.PageCommon.Pages;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

import gui.common.base.GuidedUpdate;

//
//Assertion: 
//	while processing Deployment ,abort the session.
//

public class gu_deploy_abort extends TestSuiteBase {
	String test_name = this.getClass().getSimpleName();
	String test_result = "PASS";
	static boolean manual = false; // Make this false when test is automated.
	static boolean skip = false;
	static boolean fail = false;

	long initialTime;
	long finalTime;
	String executionTime = "";
	String sProgressText = "";

	int iTimeout = 240;
	int iIndex = 0;

	boolean bFound = false;
	boolean bFail = false;

	@Test
	public void test_gu_deploy_abort() {
		screenshot_name = test_name;
		screenshot_counter = 0;
		
		GuidedUpdatePage GUPage = GuidedUpdatePage.getInstance();
		
		try {
			//
			// Environment:
			//
			printTestStartLine(test_name);

			// Get the test start time.
			initialTime = getTimeInMsec();

			// Creating email template for the test.
			if (!testInitialize(test_name)) {
				printError("Failed to perform test initialize.");
			}

			printLogs("Checking Runmode of the test.");
			if (!TestUtil.isTestCaseRunnable(suite_xl, test_name)) {
				printLogs("WARNING:: Runmode of the test '" + test_name
						+ "' set to 'N'. So Skipping the test.");
				skip = true;
				throw new SkipException("WARNING:: Runmode of the test '"
						+ test_name + "' set to 'N'. So Skipping the test.");
			}
			printLogs("RunMode found as Y. Starting the test.");

			// THIS TEST IS MANUAL RIGHT NOW. RETURN MANUAL NOW.
			if (manual) {
				throw new Exception("INFO:: Manual Test");
			}

			//
			// Setup:
			// 1. Set page to Guided Update.
			if (!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}

			// 2. Handle the Automatic or interactive popup.
			if(!GuidedUpdate.handleGUModeAndBaseline(GU_Mode.GU_INTERACTIVE, null, null)){
				throw new TestException("Test Failed");
			}

			// 3. Wait for inventory to happen.
			if(!GuidedUpdate.guidedUpdateWaitForInventory(iTimeout)){
				throw new TestException("Test Failed");
			}
	
			// 4. Click on Next.
			if(!GUPage.GUStep1Next.Click()){
				throw new TestException("Test Failed");
			}			

			// 5. On Review screen
			if(!PageCommon.WaitForPage(Pages.GuidedUpdateStep2, iTimeout)){
				throw new TestException("Test Failed");
			}

			// 6. Analyze the table content and Click on Deploy.
			if(!GuidedUpdate.analyzeGu2TableContents()){
				throw new TestException("Test Failed");
			}
						
			if(!GuidedUpdate.guidedUpdateWaitForDeploymentToStart(iTimeout)){
				throw new TestException("Test Failed");
			}
			
			//
			// Execution::
			//
			// 1. Click on Abort.
			if(!GUPage.GUDeployAbort.Click()){
				throw new TestException("Test Failed");
			}
						
			//
			// Verification:
			//
			// 1. Verify that the deployment was aborted.			
			while(true) {
				// 2. Wait for Abort to start				
				sProgressText = GuidedUpdate.guidedUpdateGetInstallProgressText();
				if (sProgressText.matches("Node deploy abort in progress.*")) {
					printLogs("Node Deploy abort started successfully");
				}	
				
				if (sProgressText.matches("Node deploy aborted.*")) {
					printLogs("Node Deploy aborted successfully");
					bFound = true;
					break;
				}
				iIndex++;
				if(iIndex > 240){
					printError("Timeout occured and the expected status did not reach.");
					break;
				}
			}		

			if (!bFound) {
				printError("Expected Text: Node deploy aborted \n"
						+ "\tActual: " + sProgressText);
				bFail = true;
				throw new TestException("Test Failed");
			}

			// 2. Verify that "Aborted by user" is displayed.
			if (!GUPage.GUDeployAbortText.Exists()) {
				printError("Deploy Abort text is not displayed");
				bFail = true;

			} else {
				sProgressText = GUPage.GUDeployAbortText.GetText();
				if (!sProgressText.matches("Aborted by user.*")) {
					printError("Expected: 'Abort by user' Text to be displayed\n"
						+ "Found: none.");
					bFail = true;
					
				} else {
					printLogs("'Aborted by user' Text was displayed as expected.");
				}
			}
			
			if (bFail) {
				throw new TestException("Test Failed");
			}

			captureScreenShot();
			printLogs("DONE.");
		} catch (Throwable t) {
			if (skip) {
				test_result = "SKIP";
			} else if (manual) {
				test_result = "MANUAL";
			} else {
				test_result = "FAIL";
				ErrorUtil.addVerificationFailure(t);
			}
		} finally {
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
				CommonHpsum.testCleanupAndReporting(test_name, test_result,
						executionTime);
				TestUtil.reportDataSetResult(suite_xl, "TestCases",
						TestUtil.getRowNum(suite_xl, test_name), test_result);
				printTestEndLine(test_name);
			} catch (Throwable t) {
				printError("Exception occurred in finally.");
			}
		}
	}
}