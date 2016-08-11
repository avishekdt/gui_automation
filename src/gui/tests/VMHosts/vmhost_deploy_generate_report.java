package gui.tests.VMHosts;

import java.util.ArrayList;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.NodeGroups;
import gui.common.base.Nodes;
import gui.common.base.Reports;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

// Assertion: 
// VM deploy and generate report
// 

public class vmhost_deploy_generate_report extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime = "";
	
	String remoteHostIp_vm = "";
	String remoteHostUserName_vm = "";
	String remoteHostPassword_vm = "";
	String remoteHostDesc_vm = "";
	String remoteHostType_vm = "";
	String nodeGroupName = "";
	String nodeGroupDesc = "";
	ArrayList<String> nodesInGroup = new ArrayList<String>();
	
	// Start the test.
	@Test
	public void test_vmhost_deploy_generate_report() {
		screenshot_name = test_name;
		reportFolder = test_name;
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
			
			// THIS TEST IS MANUAL RIGHT NOW. RETURN MANUAL NOW.
			if(manual) {
				printLogs("Set up is not available. Let the test be manual.");
				throw new Exception("INFO:: Manual Test");
			}
			
			//
			// Setup:
			//
			remoteHostIp_vm = CONFIG.getProperty("remoteHostIp_vm1");
			remoteHostUserName_vm = CONFIG.getProperty("remoteHostUserName1_vm1");
			remoteHostPassword_vm = CONFIG.getProperty("remoteHostPassword1_vm1");
			remoteHostDesc_vm = "VMware";
			remoteHostType_vm = "NodeType_vmware";
			nodeGroupName = "Group-A";
			nodeGroupDesc = "VM Host";
			nodesInGroup.add(remoteHostIp_vm);
			String vcenterIP_vm1 		= null;
			String vcentertUserName1_vm1 	= null;
			String vcenterPassword1_vm1 	= null;
			
			// Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Do Baseline inventory
			if(!BaselineLibrary.performBaselineInventory()) {
				throw new TestException("Test Failed");
			}
							
			// 
			// Execution::
			//
			
			// Add Node
			if(!Nodes.addNodeVm(remoteHostIp_vm, remoteHostDesc_vm,	remoteHostUserName_vm, 
					remoteHostPassword_vm,false, vcenterIP_vm1, vcentertUserName1_vm1, vcenterPassword1_vm1)) {
				throw new TestException("Test Failed");
			} 
			
			// Create Node group.
			if(!NodeGroups.createNodeGroups(nodeGroupName, nodeGroupDesc, nodesInGroup)){
				printError("Failed to create node group.");
			}
				
			// Go to Nodes page
			if(!guiSetPage("Nodes")){
				printError("Failed to load Node Groups page.");
			}
			
		 	// Do Node inventory
			if(!Nodes.performNodeInventory()) {
				throw new TestException("Test Failed");
			}
			
			// 5. Deploy Node.
			if(!Nodes.deployNode(remoteHostIp_vm, remoteHostType_vm)) {
				throw new TestException("Test Failed");
			}
			
			//
			// Verification:
			//
			
			if(!Reports.reportGeneration("CssNodesActions","NodeDeployReport")){
				printError("Report has not been generated.");
			}
			
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
				// Cleanup:
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
			printLogs("DONE");
		}	
	}
}
