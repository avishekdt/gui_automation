package gui.tests.Switch;

import gui.common.base.CommonHpsum;
import gui.common.base.BaselineLibrary;
import gui.common.base.Activity;
import gui.common.base.Reports;
import gui.common.base.SelUtil;
import gui.common.base.Nodes;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

// Assertion: 
// Add and deploy a switch.
// 

public class switch_fc_normal_validation extends TestSuiteBase {
	String test_name = this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime = "";
	int ActivityRowCount=0;
	int NodesPaneRowCount=0;
	int nodesLeftPaneTableRowCount = 0;
	int ListCount=0;
	
	String deployLinkClassAttr = "";
	String rebootLinkClassAttr = "";
	String xPathLeftPaneNode = "";
	String nodeNameLeftPane = "";
	
	String remoteHostUserName = "";
	String remoteHostPassword = "";
	String remoteHostIp = "";
	String remoteHostType = "";
	String remoteHostDesc = "";
	String additionalPkgLoacationKey = "";
	String deployLinkTextOnMsgBar = "";
	
	// Start the test.
	@Test
	public void test_switch_fc_normal_validation() {
		screenshot_name = test_name;
		reportFolder = test_name;
		screenshot_counter = 0;
		activityScreenRows_counter = 0;
		
		try {
			//
			// Environment:
			//
			printTestStartLine(test_name);
			
			// Get the current time in ms.
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
			
			remoteHostIp 		= CONFIG.getProperty("remoteHostIp_sanSwitch1");
			remoteHostUserName 	= CONFIG.getProperty("remoteHostUserName1_sanSwitch1");
			remoteHostPassword 	= CONFIG.getProperty("remoteHostPassword1_sanSwitch1");
			remoteHostType 		= "NodeType_fc_switch";
			remoteHostDesc 		= "Switch";
			
			if (currentOsName.contains("windows")) {
				additionalPkgLoacationKey = "additionalPkg_sanSwitch1_Location_w";
			}
			else {
				additionalPkgLoacationKey = "additionalPkg_sanSwitch1_Location_l";
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
			if(!BaselineLibrary.addAdditionalBaseline(additionalPkgLoacationKey)) {
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
            
            // Add node : Fibre Channel Switch
            if(!Nodes.addNodeSwitch(remoteHostIp, remoteHostDesc, false, remoteHostUserName, remoteHostPassword, additionalPkgLoacationKey)) {
            	throw new TestException("Test failed");
            }
         	
			// Print new activity rows
			if(!Activity.printNewlyAddedActivityRows()) {
			    printError("Failed to print latest activity rows.");
			}
         	
            // Go to Nodes page.
         	if(!guiSetPage("Nodes")) {
         		throw new TestException("Test failed");
         	}
         	
         	// Check whether deploy option is disabled in action drop down
         	if(checkActionLinkEnabled("Review/ Deploy")){
				printError("Deploy option is enabled in action drop down");
			}
         			
         	// Node inventory 
         	if(!Nodes.performNodeInventory()) {
				throw new TestException("Test Failed");
			}
         	
            // Check whether deploy option is enabled in action drop down
         	if(!checkActionLinkEnabled("Review/ Deploy")){
				printError("Deploy option is not enabled in action drop down");
			}
         	            
         	captureScreenShot();
         	
            // Click on the message bar 
            if(!SelUtil.clickByXpath("Common_PageMsgBar")) {
         		throw new TestException("Test failed");
         	}
         	printLogs("Expanded the Message Bar.");
         	
         	// Check deploy link in the message bar
         	if(!SelUtil.checkElementPresenceByXpath("Common_PageMsgBarLink")) {
         		throw new TestException("Test failed");
         	}

         	// Check link has Review and deploy updates text on the message bar
         	deployLinkTextOnMsgBar = SelUtil.getTextByXpath("Common_PageMsgBarLink");
         	
         	if(!compareTexts("Review and deploy updates" , deployLinkTextOnMsgBar)){
         		printError("Review and deploy updates link is not present on the message bar.");
         	}
         	
			// Print new activity rows
			if(!Activity.printNewlyAddedActivityRows()) {
			    printError("Failed to print latest activity rows.");
			}
         	
         	// Select the Nodes from the Main Menu.
         	if(!guiSetPage("Nodes")) {
         		throw new TestException("Test failed");
         	}
         	
         	// Report Generation
         	if(!Reports.reportGeneration("CssNodesActions","NodeInventoryReport")) {
         		printError("Failed to generate report.");
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
			}
			
			// Check whether reboot option is disabled in action drop down
			if(checkActionLinkEnabled("Reboot")) {
				printError("Reboot option is enabled in action drop down");
			}
         	
        	// Report Generation
         	if(!Reports.reportGeneration("CssNodesActions","NodeDeployReport")) {
         		printError("Failed to generate report.");
         	}
         	
         	// Select Delete from Action Drop Down
         	if(!selectActionDropDownOption("CssNodesActions","Delete")) {
         		throw new TestException("Test failed");
         	}
         	
         	// Check the presence of Delete window
         	if(!SelUtil.checkElementPresenceByXpath("Nodes_ActionDeleteTitle")) {
         		throw new TestException("Test failed");
         	}
         	
         	captureScreenShot();
         	
         	// Click on Cancel
         	if(!SelUtil.clickByXpath("Nodes_ActionDeleteCancelButton")) {
         		throw new TestException("Test failed");
         	}
         	
         	// Check for the presence of Nodes Page
         	if(!SelUtil.waitForPage("Nodes_Heading")) {
         		throw new TestException("Test failed");
         	}
         	
         	captureScreenShot();
         	
         	// Select Delete from Action Drop Down
            if(!selectActionDropDownOption("CssNodesActions","Delete")) {
            	throw new TestException("Test failed");
         	}
            
            // Check the presence of Delete window
         	if(!SelUtil.checkElementPresenceByXpath("Nodes_ActionDeleteTitle")) {
         		throw new TestException("Test failed");
         	}
            
            captureScreenShot();
            
            // Click on Yes,Delete 
            if(!SelUtil.clickByXpath("Nodes_ActionDeleteYesDeleteButton")) {
         		throw new TestException("Test failed");
         	}
            
            // Check whether the selected switch is deleted 
            if(Nodes.findNodeOnLeftPane(remoteHostIp)) {
            	printError("Failed to delete the node: " + remoteHostIp);
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
