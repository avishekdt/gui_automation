package gui.tests.Enclosure_VC;
import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.Nodes;
import gui.common.base.Reports;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

/*
 * Author: Praveen AC
 * Date: 06/02/2016
 * Test case Description:Check for the OA node addition without associated nodes, Inventory, Inventory Report, Deploy,Deploy abort 
		option, Log analysis, Deploy Report generation and Delete operation
 * Prerequistes: vc should be at lower version
 * Steps:
 * 1. Launch HPSUM.
 * 2. Do Baseline Inventory.
 * 3. Add the VC node.
 * 4. Perform Inventory and generate the report.
 * 5. Do the deployment by selecting all the components and generate the report.
 * 6. Delete the VC node.
 * Variables to update: remoteHostIp_vc2, remoteHostIp_oa2(use the OA which is associated with VC). remoteHostVCEncKey(If needed).
 */

public class enc_vc_upgrade_report_without_asso_nodes_nodes_page extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_enc_vc_upgrade_report_without_asso_nodes_nodes_page() {
		screenshot_name 	= test_name;
		reportFolder = test_name;
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
			String remoteHostIp_vc 			= CONFIG.getProperty("remoteHostIp_vc2");
			String remoteHostUserName1_vc 	= CONFIG.getProperty("remoteHostUserName1_vc2");
			String remoteHostPassword1_vc 	= CONFIG.getProperty("remoteHostPassword1_vc2");
			String remoteHostDesc_vc 		= "vc";
			String remoteHostType_vc 		= "NodeType_vc";
			String remoteHostIp_oa 			= CONFIG.getProperty("remoteHostIp_oa2");
			String remoteHostUserName1_oa 	= CONFIG.getProperty("remoteHostUserName1_oa2");
			String remoteHostPassword1_oa 	= CONFIG.getProperty("remoteHostPassword1_oa2");
					
			printLogs("Remote host host IP: " + remoteHostIp_oa);
			printLogs("Remote host Type: " + remoteHostIp_vc);
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
			
			//3. Adding a node.
			if(!Nodes.addNodeVc(remoteHostIp_vc, remoteHostDesc_vc, false, false,remoteHostUserName1_vc, 
					remoteHostPassword1_vc,remoteHostIp_oa,remoteHostUserName1_oa,remoteHostPassword1_oa)) {
			throw new TestException("Test Failed");
			}
			
			// 4. Do Node inventory
			//Performing node inventory				
			if(!Nodes.performNodeInventory()){
				throw new TestException("Test Failed");
			}
			
			//5. Generate report for inventory phase
			if(!Reports.reportGeneration("CssNodesActions","NodeInventoryReport")){
				throw new TestException("Test Failed");
			}
			
			//6. Deploy Node in Advanced mode
			if(!Nodes.deployNodeAdvMode(remoteHostIp_vc, remoteHostType_vc)) {
				throw new TestException("Test Failed");
			}
			
			//7. Generate the report after deployment completes.
			if(!Reports.reportGeneration("CssNodesActions","NodeDeployReport")){
				throw new TestException("Test Failed");
			}
			
			// 8. View Logs
			if(!Nodes.viewLogsAfterDeploy(remoteHostIp_vc,remoteHostType_vc)) {
				throw new TestException("Test Failed");
			}
			
			//9. Delete OA node.
			if(Nodes.deleteNode(remoteHostIp_vc)){
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
