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
 * Date: 05/25/2016
 * edited date: 05/27/016(By Praveen: modified the oa credentials variable).
 * Test case Description: Check for:
                Add a SAS switch without adding its associated nodes
                Do inventory
                Deployment
 * Prerequisites: NA.
 * Steps:
 * 1. Launch HPSUM.
 * 2. Do Baseline Inventory.
 * 3. Navigate to Node screen and add SAS switch.
 * 4. Perform Inventory.
 * 5. Do the deployment by selecting all the components.
 * Variables to update: remoteHostIp_sas1, remoteHostIp_sas_oa1(use the OA which is associated with SAS).
 */


public class enc_sas_deploy_without_asso_nodes extends TestSuiteBase{
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
public void test_ipv6_global_vc_deploy() {		
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
			String remoteHostIp_sas 		= CONFIG.getProperty("remoteHostIp_sas1");
			String remoteHostUserName1_sas 	= CONFIG.getProperty("remoteHostUserName1_sas1");
			String remoteHostPassword1_sas 	= CONFIG.getProperty("remoteHostPassword1_sas1");
			String remoteHostDesc_sas 		= "sas";
			String remoteHostType_sas 		= "NodeType_sas_switch";
			String remoteHostIp_oa 			= CONFIG.getProperty("remoteHostIp_sas_oa1");
			String remoteHostUserName1_oa 	= CONFIG.getProperty("remoteHostUserName1_sas_oa1");
			String remoteHostPassword1_oa 	= CONFIG.getProperty("remoteHostPassword1_sas_oa1");
					
			printLogs("Remote host host IP: " + remoteHostIp_sas);
			printLogs("Remote host Type: " + remoteHostDesc_sas);
			printLogs("UserName: " + remoteHostUserName1_sas + " Password: " + remoteHostPassword1_sas);
			
			
			// Execution:
			
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Wait for all the Baseline inventory to complete.
			if(!BaselineLibrary.performBaselineInventory()){
				throw new TestException("Test failed");
			}
			
			//3. Adding a node.
			if(!Nodes.addNodeSas(remoteHostIp_sas, remoteHostDesc_sas, false, false,remoteHostUserName1_sas, 
					remoteHostPassword1_sas,remoteHostIp_oa,remoteHostUserName1_oa,remoteHostPassword1_oa)) {
			throw new TestException("Test Failed");
			}
			
			// 4. Do Node inventory
			//Performing node inventory
			if(!Nodes.performNodeInventory()){
				throw new TestException("Test Failed");
			}
			
			//5. Deploy Node in Advanced mode
			if(!Nodes.deployNodeAdvMode(remoteHostIp_sas, remoteHostType_sas)) {
				throw new TestException("Test Failed");
			}
			
			//6.Verifying the logs.
			if(!Nodes.viewLogsAfterDeploy(remoteHostIp_sas, remoteHostType_sas)){
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
