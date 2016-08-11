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
 * Test case Description:  
Check for the OA node addition without associated nodes, Inventory, Inventory Report, Deploy,Deploy abort 
		option, Log analysis, Deploy Report generation and Delete operation
 * Prerequistes: OA should be at higher version.
 * Steps:
 * 1. Launch HPSUM.
 * 2. Do Baseline Inventory.
 * 3. Add node with IPv6 Address.
 * 4. Perform Inventory and generate the report.
 * 5. Do the deployment by selecting all the components and generate the report.
 * 6. Delete the OA node.
 * Variables to update: remoteHostIp_oa2,additional_Comp_Location_w, additional_Comp_Location_l
 */



public class enc_oa_downgrade_report_without_asso_nodes_nodes_page extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_enc_oa_rewrite_without_asso_nodes_enclosures_page() {
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
			String remoteHostIp_oa 		= CONFIG.getProperty("remoteHostIp_oa2");
			String remoteHostUserName1_oa 	= CONFIG.getProperty("remoteHostUserName1_oa2");
			String remoteHostPassword1_oa 	= CONFIG.getProperty("remoteHostPassword1_oa2");
			String remoteHostDesc_oa 		= "oa";
			String remoteHostType_oa 		= "NodeType_enclosure";
			String sppLocation_toSelect = "";
			String sppLocation_toSelect_w = CONFIG.getProperty("additionalPkg_Comp_Downgrad_w");
			String sppLocation_toSelect_l = CONFIG.getProperty("additionalPkg_Comp_Downgrad_l");
					
			printLogs("Remote host host IP: " + remoteHostIp_oa);
			printLogs("Remote host Type: " + remoteHostDesc_oa);
			printLogs("UserName: " + remoteHostUserName1_oa + " Password: " + remoteHostPassword1_oa);
			
			
			// Execution:
			// Find out if it is Win-iLO or Lin-iLO deployment.
			if (currentOsName.contains("windows")) {
				printLogs("Trying Win-OA remote deployment.");
				sppLocation_toSelect = sppLocation_toSelect_w;
			}
			else {
				printLogs("Trying Lin-OA remote deployment.");
				sppLocation_toSelect = sppLocation_toSelect_l;
			}
			
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
			if(!Nodes.addNodeOa(remoteHostIp_oa, remoteHostDesc_oa, false, true,
					remoteHostUserName1_oa, remoteHostPassword1_oa)) {
			throw new TestException("Test Failed");
			}
			
			// 4. Do Node inventory
			//Performing node inventory				
			if(!Nodes.specifyBlAndPerformNodeInventory(sppLocation_toSelect)){
				throw new TestException("Test Failed");
			}
			
			//5. Generate report for inventory phase
			if(!Reports.reportGeneration("CssNodesActions","NodeInventoryReport")){
				throw new TestException("Test Failed");
			}
			
			//6. Deploy Node in Advanced mode
			if(!Nodes.deployNodeAdvMode(remoteHostIp_oa, remoteHostType_oa)) {
				throw new TestException("Test Failed");
			}
			
			//7. Generate the report after deployment completes.
			if(!Reports.reportGeneration("CssNodesActions","NodeDeployReport")){
				throw new TestException("Test Failed");
			}
			
			// 8. View Logs
			if(!Nodes.viewLogsAfterDeploy(remoteHostIp_oa,remoteHostType_oa)) {
				throw new TestException("Test Failed");
			}
			
			//9. Delete OA node.
			if(Nodes.deleteNode(remoteHostIp_oa)){
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
