package gui.tests.Enclosure_VC;
import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.ContextScreen;
import gui.common.base.Nodes;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

/*
 * Author: Praveen AC
 * Date: 05/25/2016
 * Test case Description: Check if OA can be added form the Enclosure page and after the deployment, OA should
 * 						upgrade.
 * Prerequistes: OA should be at lower version
 * Steps:
 * 1. Launch HPSUM.
 * 2. Do Baseline Inventory.
 * 3. Add OA in enclosure page.
 * 4. Perform Inventory.
 * 5. Do the deployment by selecting all the components.
 * Variables to update: remoteHostIp_oa2.
 */

public class enc_oa_upgrade_without_asso_nodes_enclosures_page extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_enc_oa_upgrade_without_asso_nodes_enclosures_page() {
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
			String remoteHostIp_oa 		= CONFIG.getProperty("remoteHostIp_oa2");
			String remoteHostUserName1_oa 	= CONFIG.getProperty("remoteHostUserName1_oa2");
			String remoteHostPassword1_oa 	= CONFIG.getProperty("remoteHostPassword1_oa2");
			String remoteHostDesc_oa 		= "oa";
			String remoteHostType_oa 		= "NodeType_enclosure";
								
			printLogs("Remote host host IP: " + remoteHostIp_oa);
			printLogs("Remote host Type: " + remoteHostDesc_oa);
			printLogs("UserName: " + remoteHostUserName1_oa + " Password: " + remoteHostPassword1_oa);
			
			
			// Execution:
			// Find out if it is Win-iLO or Lin-iLO deployment.
			if (currentOsName.contains("windows")) {
				printLogs("Trying Win-OA remote deployment.");				
			}
			else {
				printLogs("Trying Lin-OA remote deployment.");				
			}
			
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Wait for all the Baseline inventory to complete.
			if(!BaselineLibrary.performBaselineInventory()){
				throw new TestException("Test failed");
			}
			/*//Adding additional package for downloading the component.
		    if(!BaselineLibrary.addAdditionalBaseline(sppLocation_toSelect)) {
				throw new TestException("Test Failed");
			}*/	
			
			//3. Adding a node.
			if(!ContextScreen.addNodeOa(remoteHostIp_oa, remoteHostDesc_oa, false, false,
					remoteHostUserName1_oa, remoteHostPassword1_oa)) {
			throw new TestException("Test Failed");
			}
			
			// 4. Do Node inventory
			//Performing node inventory	
			
			if(!Nodes.performNodeInventory()){
				throw new TestException("Test Failed");
			}
			
			//5. Deploy Node in Advanced mode
			if(!Nodes.deployNodeAdvMode(remoteHostIp_oa, remoteHostType_oa)) {
				throw new TestException("Test Failed");
			}
			
			// 6. View Logs
			if(!Nodes.viewLogsAfterDeploy(remoteHostIp_oa,remoteHostType_oa)) {
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
