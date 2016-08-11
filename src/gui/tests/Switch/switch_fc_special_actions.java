package gui.tests.Switch;

import gui.common.base.CommonHpsum;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;
import gui.common.base.Activity;
import gui.common.base.BaselineLibrary;
import gui.common.base.Nodes;
import gui.common.base.SelUtil;


import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

// Assertion: 
// Deploy switch - add node with wrong credential, edit node, add baseline only at inventory page, abort inventory. 
// 

public class switch_fc_special_actions extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= true;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime = "";
	
	String remoteHostUserName = "";
	String remoteHostPassword = "";
	String remoteHostIp = "";
	String remoteHostType = "";
	String remoteHostDesc = "";
	String additionalPkgLoacationKey = "";
	String additionalPkgLoacation = "";
	String nodesInventoryMsg = "";
	String nodesInventoryAbortMsg = "";
	String addNodeEditLink = "";
	String nodeTitle = "";
	String nodeIncorrectUserNamePwdMsg = "";
	
	// Start the test.
	@Test
	public void test_switch_fc_special_actions() {
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
				printLogs("Node inventory abort has to be manual since switch has a small component and the test can't be automated.");
				throw new Exception("INFO:: Manual Test");
			}
			
			//
			// Setup:
			//
		/*	remoteHostIp 		= CONFIG.getProperty("remoteHostIp_sanSwitch1");
			remoteHostUserName 	= "Administrator";
			remoteHostPassword 	= "Admin";
			remoteHostType 		= "NodeType_fc_switch";
			remoteHostDesc 		= "Switch";
			
			if (currentOsName.contains("windows")) {
				additionalPkgLoacationKey = "additionalPkg_switch_Location_w";
			}
			else {
				additionalPkgLoacationKey = "additionalPkg_switch_Location_l";
			}
			
			// Do test setup
			if(!CommonHpsum.performTestSetup("ftpPortNumber")) {
				throw new TestException("Test Failed");
			}
			
			// Print initial content of the activity page
			if(!Activity.printNewlyAddedActivityRows()) {
			    printError("Failed to print latest activity rows.");
			}
			
			// Add baseline
			if(!BaselineLibrary.addAdditionalBaseline(additionalPkgLoacationKey)){
				throw new TestException("Test failed");
			}
			
			// Wait for Baseline Inventory to complete
			if(!BaselineLibrary.performBaselineInventory()) {
				throw new TestException("Test failed");
			}
			
			// Print new activity rows
			if(!Activity.printNewlyAddedActivityRows()) {
			    printError("Failed to print latest activity rows.");
			}	
			
			// 
			// Execution::
			//
			
			// Calling addNodeBase to perform basic add node functions
         	if(!Nodes.addNodeBase(remoteHostIp, remoteHostDesc, remoteHostType)) {
				throw new TestException("Test failed");
			}
         	
         	// Uncheck Auto Associate Node
         	if(!Nodes.uncheckAutoAddAssoNode()) {
				printError("Failed to uncheck the Auto Add Associated Node checkbox");
			}
         	
         	// Entering wrong username and password
         	if(!SelUtil.sendKeysByXpath("Nodes_AddNodeCredentialsUsernameInput", remoteHostUserName)) {
				printError("sendKeysByXpath Nodes_AddNodeCredentialsUsernameInput failed.");
			}
				
			if(!SelUtil.sendKeysByXpath("Nodes_AddNodeCredentialsPasswordInput", remoteHostPassword)) {
				printError("sendKeysByXpath Nodes_AddNodeCredentialsPasswordInput failed.");
			}
			
			captureScreenShot();
			
			// Click on Add button
			if(!SelUtil.clickByXpath("Nodes_AddNodeAddButton")) {
				throw new TestException("Test failed");
			}
			
			// Wait for Add Node window to disappear
			if(!SelUtil.waitForNoElementByXpath("Common_NewWindow")) {
				throw new TestException("Test failed");
			}
			
			// Get the status of the node 
			nodeTitle = SelUtil.getTextByXpath("Nodes_Title");
			
			// Check whether the status of the node is UNKNOWN
			if(!compareTexts("UNKNOWN: "+remoteHostIp , nodeTitle)){
				printError("The status of the node is not UNKNOWN");
			}
			
			// Get the status of the node 
			nodeIncorrectUserNamePwdMsg = SelUtil.getTextByXpath("NodesInventory_Msg");
			
			// Check for Incorrect username/password message
			if(!compareTexts("Incorrect username/password." , nodeIncorrectUserNamePwdMsg)){
				printError("Incorrect username/password message is not present");
			}
			
			// Print new activity rows
			if(!Activity.printNewlyAddedActivityRows()) {
				printError("Failed to print latest activity rows.");
			}
			
			// Go to Nodes page.
         	if(!guiSetPage("Nodes")) {
         		throw new TestException("Test failed");
         	}
			
			remoteHostUserName 	= CONFIG.getProperty("remoteHostUserName1_sanSwitch1");
			remoteHostPassword 	= CONFIG.getProperty("remoteHostPassword1_sanSwitch1");
			additionalPkgLoacation = CONFIG.getProperty(additionalPkgLoacationKey);
			
			addNodeEditLink = SelUtil.getTextByXpath("Common_PageMsgBarLink");
			
			// Check Edit link in message bar
			if(!compareTexts("Edit" , addNodeEditLink)) {
				printError("Edit link is not found in the message bar link");
			}
			
			// Click on Edit Link from Actions drop-down.
			if(!selectActionDropDownOption("CssNodesActions", "Edit")) {
				throw new TestException("Test failed");
			}
			
			// wait for edit page
			if(!SelUtil.checkElementPresenceByXpath("Common_NewWindowHeading")) {
				throw new TestException("Test failed");
			}
			 
			// Entering valid username and password
         	if(!SelUtil.sendKeysByXpath("Nodes_AddNodeCredentialsUsernameInput", remoteHostUserName)) {
				printError("sendKeysByXpath Nodes_AddNodeCredentialsUsernameInput failed.");
			}
				
			if(!SelUtil.sendKeysByXpath("Nodes_AddNodeCredentialsPasswordInput", remoteHostPassword)) {
				printError("sendKeysByXpath Nodes_AddNodeCredentialsPasswordInput failed.");
			}
			
			captureScreenShot();
			
			// Click on Ok button
			if(!SelUtil.clickByXpath("Nodes_EditOkButton")) {
				throw new TestException("Test failed");
			}
			
			// Wait for Edit window to disappear
			if(!SelUtil.waitForNoElementByXpath("Common_NewWindow")) {
				throw new TestException("Test failed");
			}
			
			captureScreenShot();
			
			if(!SelUtil.waitForNoElementByXpath("Nodes_LoadingElement")){
				throw new TestException("Test failed");
			}
			
			captureScreenShot();
		
			// Click on Inventory Link from Actions drop-down.
			if(!selectActionDropDownOption("CssNodesActions", "Inventory")) {
				throw new TestException("Test failed");
			}
			
			// wait for Nodes inventory window
			if(!SelUtil.checkElementPresenceByXpath("NodesInventory_Heading")) {
				throw new TestException("Test failed");
			}
			
			sleep(5);
			
			// Click on additional package search button 
			if(!Nodes.selectAblOnAddNodePage(additionalPkgLoacation)) {
				throw new TestException("Test failed");
			}
			
			// Click on inventory button
			if(!SelUtil.clickByXpath("NodesInventory_InventoryButton")) {
				throw new TestException("Test failed");
			}
		
			// Wait for Inventory window to disappear
			if(!SelUtil.waitForNoElementByXpath("Common_NewWindow")) {
				throw new TestException("Test failed");
			}
			
			captureScreenShot();
			
			// Click on Abort Link from Actions drop-down.
			if(!selectActionDropDownOption("CssNodesActions", "Abort")) {
				throw new TestException("Test failed");
			}
			
			// Wait for Abort window
			if(!SelUtil.checkElementPresenceByXpath("Nodes_ActionAbortTitle")) {
				printError("Abort window is not found.");
			}
			
			captureScreenShot();
			
			// cLick on cancel button
			if(!SelUtil.clickByXpath("Nodes_ActionAbortCancelButton")) {
				printError("Failed to click on cancel button");
			}
			
			nodesInventoryMsg = SelUtil.getTextByXpath("NodesInventory_Msg");
			
			// Check Inventory progress message on message bar
			if((!compareTexts("Inventory started." , nodesInventoryMsg)) || (!compareTexts("Inventory in progress." , nodesInventoryMsg))) {
				printError("Inventory progress message is not found on the message bar.");
			}
			else{
				printLogs("Inventory still in progress after cancelling abort.");
			}
			
			captureScreenShot();
			
			// Click on Abort Link from Actions drop-down.
			if(!selectActionDropDownOption("CssNodesActions", "Abort")) {
				throw new TestException("Test failed");
			}
						
			// Wait for Abort window
			if(!SelUtil.checkElementPresenceByXpath("Nodes_ActionAbortTitle")) {
				printError("Abort window is not found.");
			}
			
			captureScreenShot();
						
			// cLick on Yes,Abort button
			if(!SelUtil.clickByXpath("Nodes_ActionAbortYesAbortButton")) {
				printError("Failed to click on Yes,Abort button");
			}
			
			sleep(10);
			
			nodesInventoryAbortMsg = SelUtil.getTextByXpath("NodesInventory_Msg");
			
			// Check Abort message on message bar
			if(!compareTexts(remoteHostIp+": Inventory aborted.." , nodesInventoryMsg)) {
				printError("Inventory abort message is found on the message bar.");
			}
			else{
				printLogs("Inventory Aborted.");
			}
			
			captureScreenShot();
			
			// Print new activity rows
			if(!Activity.printNewlyAddedActivityRows()) {
				printError("Failed to print latest activity rows.");
			}
			
			// Go to Nodes page.
         	if(!guiSetPage("Nodes")) {
         		throw new TestException("Test failed");
         	}
			
			// Click on Inventory Link from Actions drop-down.
			if(!selectActionDropDownOption("CssNodesActions", "Inventory")) {
				throw new TestException("Test failed");
			} 
			
			// Wait for node inventory to complete
			if(!Nodes.WaitForNodesInventoryToComplete()) {
				throw new TestException("Test failed");
			}
			
			// Deploy Node.
			if(!Nodes.deployNode(remoteHostIp, remoteHostType)) {
				throw new TestException("Test Failed");
			}
								
			//
			// Verification:
			//
			
			// Print new activity rows
			if(!Activity.printNewlyAddedActivityRows()) {
				printError("Failed to print latest activity rows.");
			}
			            
			// Select the Nodes from the Main Menu.
			if(!guiSetPage("Nodes")) {
				throw new TestException("Test failed");
			}
						
			// View Logs
			if(!Nodes.viewLogsAfterDeploy(remoteHostIp, remoteHostType)) {
				throw new TestException("Test Failed");
			} */
			
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
