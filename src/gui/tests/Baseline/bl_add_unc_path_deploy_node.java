package gui.tests.Baseline;

import gui.common.base.Activity;
import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.Nodes;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

//
//Assertion: 
//Add a SPP from UNC path and verify it gets added correctly.
//

public class bl_add_unc_path_deploy_node extends TestSuiteBase {	
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_bl_add_unc_path_deploy_node() {
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
			
			// This test is not valid for Linux scenario.
			if (!currentOsName.contains("windows")) {
				printLogs("WARNING:: This test is supported only on Windows. Current host OS is " + currentOsName);
				skip = true;
				test_result = "SKIP";
				throw new Exception();
			}
			
			// The keys to use. Values will be got inside method from CONFIG file.
			String ablUncPathKey 		= "additionalPkg_UNC_Location1";
			String ablUncUserNameKey 	= "additionalPkg_UNC_UserName1"; 
			String ablUncPasswordKey 	= "additionalPkg_UNC_Password1";
			
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// Print initial content of the activity page
			if(!Activity.printNewlyAddedActivityRows()) {
			    printError("Failed to print latest activity rows.");
			}
			
			// Do initial Baseline Inventory
			if(!BaselineLibrary.performBaselineInventory()) {
				throw new TestException("Test failed");
			}
			
			// Get initial BLs count.
			int blCountBeforeAdd = BaselineLibrary.getBlCount();
			
			// 2. Add UNC baseline
			if(!BaselineLibrary.addAblWithUncPath(ablUncPathKey, ablUncUserNameKey, ablUncPasswordKey)) {
				throw new TestException("Test failed");
			}
			
			captureScreenShot();
			
			// Wait for Baseline Inventory to complete
			if(!BaselineLibrary.performBaselineInventory()) {
				throw new TestException("Test failed");
			}
			
			int blCountAfterAdd = BaselineLibrary.getBlCount();
			
			if (blCountAfterAdd <= blCountBeforeAdd) {
				printError("BL count did not increase even after adding a new UNC path BL.");
				
				throw new TestException("Test failed");
			}
			
			captureScreenShot();
			
			// Print new activity rows
			if(!Activity.printNewlyAddedActivityRows()) {
			    printError("Failed to print latest activity rows.");
			}
			
			// 3. Go to Nodes Page
			if(!guiSetPage("Nodes")) {
				throw new TestException("Test Failed");
			}
			
			// 4. Perform Node inventory with UNC path BL
			if(!Nodes.specifyBlAndPerformNodeInventory(CONFIG.getProperty(ablUncPathKey))) {
				throw new TestException("Test failed");
			}
			
			// Print new activity rows
			if(!Activity.printNewlyAddedActivityRows()) {
			    printError("Failed to print latest activity rows.");
			}
			
			// Set page to Nodes.
			if(!guiSetPage("Nodes")) {
				throw new TestException("Test Failed");
			}
			
			// 
			// Execution::
			//
			// Deploy the node with the UNC path BL
			if(!Nodes.deployNode("localhost", "local")) {
				throw new TestException("Test Failed");
			}
			
			//
			// Verification:
			//
			// Verify that the deploy complete successfully.
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