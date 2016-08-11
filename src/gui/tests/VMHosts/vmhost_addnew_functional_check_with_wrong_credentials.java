package gui.tests.VMHosts;

import java.util.ArrayList;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.NodeGroups;
import gui.common.base.Nodes;
import gui.common.base.SelUtil;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

// Assertion: 
// Add node with wrong credential
// 

public class vmhost_addnew_functional_check_with_wrong_credentials extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime = "";
	
	String addNodeStatusIconClassAttr = "";
	String addNodeMsgInMessageBar = "";
	String addNodeErrorMsg = "Unable to login or identify node as a supported device.";

	// Start the test.
	@Test
	public void test_vmhost_addnew_functional_check_with_wrong_credentials() {
		screenshot_name = test_name;
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
			String remoteHostIp_vm 		= CONFIG.getProperty("remoteHostIp_vm1");
			String remoteHostUserName_vm 	= "admin";
			String remoteHostPassword_vm 	= "123";
			String remoteHostDesc_vm 		= "VMware";
			String remoteHostType_vm 		= "NodeType_vmware";
			String nodeGroupName = "Group-A";
			String nodeGroupDesc = "VM Host";
			ArrayList<String> nodesInGroup = new ArrayList<String>();
			
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
			
			// Create Node group.
			if(!NodeGroups.createNodeGroups(nodeGroupName, nodeGroupDesc, nodesInGroup)){
				printError("Failed to create node group.");
			}
			
		/*	// Add Node
			if(!Nodes.addNodeVm(remoteHostIp_vm1, remoteHostDesc_vm3, true, 
					remoteHostUserName1_vm1, remoteHostPassword1_vm1)) {
				throw new TestException("Test Failed");
			} */
			
			// Calling addNodeBase to perform basic add node functions
			if(!Nodes.addNodeBase(remoteHostIp_vm, remoteHostDesc_vm, remoteHostType_vm)) {
				printError("Failed to enter the IP, Description and Node type");
				throw new TestException("Test failed");
			}
			
			// Select BL: NodesAddSelectBaseline
			if(!Nodes.NodesAddSelectBaseline()) {
				printError("NodesAddSelectBaseline failed.");
			}
			
			if(!Nodes.selectFormAssignToGroupDropDown(nodeGroupName)){
				printError("Failed to select group.");
			}
			
			// Check if admin credentials are to be used
			if((remoteHostUserName_vm != null)&& (remoteHostPassword_vm != null)) {
				
				if(!SelUtil.sendKeysByXpath("Nodes_AddNodeCredentialsUsernameInput", remoteHostUserName_vm)) {
					printError("sendKeysByXpath Nodes_AddNodeCredentialsUsernameInput failed.");
				}
				
				if(!SelUtil.sendKeysByXpath("Nodes_AddNodeCredentialsPasswordInput", remoteHostPassword_vm)) {
					printError("sendKeysByXpath Nodes_AddNodeCredentialsPasswordInput failed.");
				}
			}
			else{
				printLogs("Selecting the option : Use current credentials.");
				if(!SelUtil.clickByXpath("Nodes_AddNodeCredentialsCurrentRadio")) {
					printError("clickByXpath Nodes_AddNodeCredentialsCurrentRadio failed.");
				}
			}
			
			captureScreenShot();
			
			// Click on Add button
			if(!SelUtil.clickByXpath("Nodes_AddNodeAddButton")) {
				printError("clickByXpath Nodes_AddNodeAddButton failed.");
			}
			
			sleep(10);
			
			captureScreenShot();
			
			// Verify the Add Node screen is closed and control is on Nodes screen
			if(!SelUtil.waitForNoElementByXpath("Common_NewWindow")) {
				printError("Known Issue: Add New popup screen not closing after pressing Add button.");
				captureScreenShot();
				printLogs("Closing the Add New popup screen using the Cancel button.");
				if(!SelUtil.clickByXpath("Nodes_AddNodeCloseButton")) {
					printError("Failed to close the add Node window");
				}
			}
			
			// Wait till the node gets added.
			if(!SelUtil.waitForNoElementByXpath("Nodes_LoadingElement")){
				printError("Failed to wait till node gets added.");
			}
			
			// Check if Add nodes page vanished after clicking on Add button
			if(!SelUtil.verifyText("Nodes_Title", remoteHostIp_vm)) {
				printError("The control did not reach the added node page by default.");
				printError("Setting the control to new added node page.");
				driver.findElement(By.linkText(remoteHostIp_vm)).click();
				printLogs("Clicked the remote host IP: " +  remoteHostIp_vm);
			}
			
			//
			// Verification:
			//
			
			// Get class attribute of node status icon
			addNodeStatusIconClassAttr = SelUtil.getAttributeByXpath("Nodes_AddNodeStatusIcon","class");
			
			// Check whether node status icon indicates error
			if(!compareTexts("hp-status-error", addNodeStatusIconClassAttr)){
				printError("Error is not indicated in the node status icon.");
			}
			
			// Get text from add node msg bar
			addNodeMsgInMessageBar = SelUtil.getTextByXpath("Nodes_AddNodeMsg");
			
			if(!compareTexts(addNodeErrorMsg, addNodeMsgInMessageBar)){
				printError("Error message is not displayed.");
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
