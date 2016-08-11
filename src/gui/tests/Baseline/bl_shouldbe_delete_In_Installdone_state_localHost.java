package gui.tests.Baseline;
import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.Nodes;
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

public class bl_shouldbe_delete_In_Installdone_state_localHost extends TestSuiteBase {	
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_bl_shouldbe_delete_In_Installdone_state_localHost() {
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
			String adlSPPPath = "" ;
			String sppLocation_toSelect;
			String sppLocation_toSelect_w = CONFIG.getProperty("sppLocation_w");
			String sppLocation_toSelect_l = CONFIG.getProperty("sppLocation_l");
			adlSPPPath = "/hp/swpackages";
			
			if(currentOsName.contains("windows")) {
				printLogs("Trying Win-Win remote deployment.");				
				sppLocation_toSelect = sppLocation_toSelect_w + adlSPPPath;
			}
			else {
				printLogs("Trying Lin-Lin remote deployment.");	
				sppLocation_toSelect = sppLocation_toSelect_l + adlSPPPath;
			}
			
			// Execution:
			
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Wait for all the Baseline inventory to complete.
			if(!BaselineLibrary.performBaselineInventory()){
				throw new TestException("Test failed");
			}
			// 3. Select the Nodes from the Main Menu.
			if(!CommonHpsum.clickLinkOnMainMenu("MainMenu_Nodes")) {
				throw new TestException("Test Failed");
			}
			
			captureScreenShot();
			
			// Verify Page: Nodes
			if(!SelUtil.verifyPage("Nodes")) {
				throw new TestException("Test Failed");
			}	
			
			sleep(5);
			
			// 4. Do Node inventory
			if(!Nodes.performNodeInventory()) {
				throw new TestException("Test Failed");
			}
			//5. Deploy Node in easy mode
			if(!Nodes.deployNodeEasyMode("localhost", "local")) {
				throw new TestException("Test Failed");
			}
			
			// Delete baseline after the installation is done.
			if(!BaselineLibrary.deleteAbl(sppLocation_toSelect)){
				throw new TestException("Test Failed");
			}
			
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