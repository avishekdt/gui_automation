package gui.tests.Enclosure_VC;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.Nodes;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

/*
 * Author: Praveen AC
 * Date: 05/27/2016
 * Test case Testcase to check if VC can be downgraded to its previous versions in GUI
 * Prerequisites: Check the vc which is at higher version.
 * Steps:
 * 1. Launch HPSUM.
 * 2. Do Baseline Inventory.
 * 3. Navigate to context screen and add VC.
 * 4. Perform Inventory.
 * 5. Do the deployment by selecting all the components.
 * Variables to update: remoteHostIp_vc1, remoteHostIp_oa1(use the OA which is associated with VC). remoteHostVCEncKey(If needed).
 */


public class enc_vc_downgrade_nodes_page extends TestSuiteBase{
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
public void test_enc_vc_downgrade_nodes_page() {		
		screenshot_name 	= test_name;
		screenshot_counter 	= 0;
		//String verText= "";
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
			String remoteHostIp_vc1 			= CONFIG.getProperty("remoteHostIp_vc1");
			String remoteHostUserName1_vc1 	= CONFIG.getProperty("remoteHostUserName1_vc1");
			String remoteHostPassword1_vc1 	= CONFIG.getProperty("remoteHostPassword1_vc1");
			String remoteHostDesc_vc1 		= "vc";
			String remoteHostType_vc1 		= "NodeType_vc";
			String remoteHostIp_oa1 		= CONFIG.getProperty("remoteHostIp_oa1");
			String remoteHostUserName1_oa1 	= CONFIG.getProperty("remoteHostUserName1_oa1");
			String remoteHostPassword1_oa1 	= CONFIG.getProperty("remoteHostPassword1_oa1");
			String sppLocation_toSelect = "";
			String sppLocation_toSelect_w = CONFIG.getProperty("additionalPkg_Comp_Downgrad_w");
			String sppLocation_toSelect_l = CONFIG.getProperty("additionalPkg_Comp_Downgrad_l");
					
			printLogs("Remote host host IP: " + remoteHostIp_vc1);
			printLogs("Remote host Type: " + remoteHostDesc_vc1);
			printLogs("UserName: " + remoteHostUserName1_vc1 + " Password: " + remoteHostPassword1_vc1);
			
			// Find out if it is Win-vc or Lin-vc deployment.
			if (currentOsName.contains("windows")) {
				printLogs("Trying Win-vc remote deployment.");
				sppLocation_toSelect = sppLocation_toSelect_w;
			}
			else {
				printLogs("Trying Lin-vc remote deployment.");
				sppLocation_toSelect = sppLocation_toSelect_l;
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
			
			//Adding additional package for downloading the component.
		    if(!BaselineLibrary.addAdditionalBaseline(sppLocation_toSelect)) {
				throw new TestException("Test Failed");
			}	
			
			//3. Adding a node.
			if(!Nodes.addNodeVc(remoteHostIp_vc1, remoteHostDesc_vc1, false, true,remoteHostUserName1_vc1, 
					remoteHostPassword1_vc1,remoteHostIp_oa1,remoteHostUserName1_oa1,remoteHostPassword1_oa1)) {
			throw new TestException("Test Failed");
			}
			
			// 4. Do Node inventory
			//Performing node inventory			
			if(!Nodes.specifyBlAndPerformNodeInventory(sppLocation_toSelect)){
				throw new TestException("Test Failed");
			}
			
			//5. Deploy Node in Advanced mode
			if(!Nodes.deployNodeAdvMode(remoteHostIp_vc1, remoteHostType_vc1)) {
				throw new TestException("Test Failed");
			}
			
			//6. Verifying the deploy logs.
			if(!Nodes.viewLogsAfterDeploy(remoteHostIp_vc1, remoteHostType_vc1)){
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
