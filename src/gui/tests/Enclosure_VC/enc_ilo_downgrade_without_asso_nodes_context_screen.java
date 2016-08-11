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
 * Date: 05/23/2016
 * Update Date: 05/24/2016
 * Test case Description: check for: Add iLO without assocaited nodes from iLO page
                Do inventory and Downgrade
 * Prerequisites: NA.
 * Steps:
 * 1. Launch HPSUM.
 * 2. Do Baseline Inventory.
 * 3. Add iLO in the context screen.
 * 4. Perform Inventory.
 * 5. Do the deployment by selecting all the components.
 * Variables to update: remoteHostIp_ilo1, additional_Comp_Location_w, additional_Comp_Location_l
 */

public class enc_ilo_downgrade_without_asso_nodes_context_screen extends TestSuiteBase{
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
		
	@Test
	public void test_enc_ilo_downgrade_without_asso_nodes_context_screen() {
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
		
			String remoteHostIp_ilo 		= CONFIG.getProperty("remoteHostIp_ilo1");
			String remoteHostUserName1_ilo 	= CONFIG.getProperty("remoteHostUserName1_ilo1");
			String remoteHostPassword1_ilo 	= CONFIG.getProperty("remoteHostPassword1_ilo1");
			String remoteHostDesc_ilo 		= "iLO";
			String remoteHostType_ilo 		= "NodeType_ilo";
			String sppLocation_toSelect = "";
			String sppLocation_toSelect_w = CONFIG.getProperty("additional_Comp_Location_w");
			String sppLocation_toSelect_l = CONFIG.getProperty("additional_Comp_Location_l");
			
			// Setup::
			// Find out if it is Win-iLO or Lin-iLO deployment.
			if (currentOsName.contains("windows")) {
				printLogs("Trying Win-iLO remote deployment.");
				sppLocation_toSelect = sppLocation_toSelect_w;
			}
			else {
				printLogs("Trying Lin-iLO remote deployment.");
				sppLocation_toSelect = sppLocation_toSelect_l;
			}
						
			printLogs("Remote host IP: " + remoteHostIp_ilo);
			printLogs("Remote host Type: " + remoteHostType_ilo);
			printLogs("UserName: " + remoteHostUserName1_ilo + " Password: " + remoteHostPassword1_ilo);
			
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
			
			// 3. Add Node
		    if(!ContextScreen.addNodeIlo(remoteHostIp_ilo, remoteHostDesc_ilo, false, true,
						remoteHostUserName1_ilo, remoteHostPassword1_ilo)) {
				throw new TestException("Test Failed");
			}
			
			// 4. Do Node inventory
			//performing the inventory by selecting the additional baseline package. 
			if(!Nodes.specifyBlAndPerformNodeInventory(sppLocation_toSelect)){
				throw new TestException("Test Failed");
			}
			
			// 5. Deploy Node.
			// Deploy Node in Advanced mode
			if(!Nodes.deployNodeAdvMode(remoteHostIp_ilo, remoteHostType_ilo)) {
				throw new TestException("Test Failed");
			}
			
			// 6. View Logs
			if(!Nodes.viewLogsAfterDeploy(remoteHostIp_ilo,remoteHostType_ilo)) {
				throw new TestException("Test Failed");
			}
			// Final ScreenShot
			captureScreenShot();
			printLogs("DONE.");
		}
		catch (Throwable t) {
			if ( Nodes.without_baseline){
				 Nodes.without_baseline = false;
			}
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
