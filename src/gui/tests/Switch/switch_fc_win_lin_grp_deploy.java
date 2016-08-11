package gui.tests.Switch;

import gui.common.base.Activity;
import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.Nodes;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

// Assertion: 
// Switch, window, linux node Group deployment. 
// 

public class switch_fc_win_lin_grp_deploy extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= true;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime = "";
	
	String remoteHostUserName_switch = "";
	String remoteHostPassword_switch = "";
	String remoteHostIp_switch = "";
	String remoteHostType_switch = "";
	String remoteHostDesc_switch = "";
	String additionalPkgLoacationKey_switch = "";
	
	String remoteHostIp_windows = "";
	String remoteHostUserName_windows = "";
	String remoteHostPassword_windows = "";
	String remoteHostType_windows = "";
	String remoteHostDesc_windows = "";
	
	String remoteHostIp_linux = "";
	String remoteHostUserName_linux = "";
	String remoteHostPassword_linux = "";
	String remoteHostDesc_linux = "";
	String remoteHostType_linux = "";
	String accessLevel = "none";
	String remoteHostSuperUserName = null;
	String remoteHostSuperPasswaord = null;
	
	// Start the test.
	@Test
	public void test_switch_fc_win_lin_grp_deploy() {
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
				printLogs("Bug in the product while group inventory. Cannot be automated.");
				throw new Exception("INFO:: Manual Test");
			}
			
			//
			// Setup:
			//
			
		/*	remoteHostIp_switch 		= CONFIG.getProperty("remoteHostIp_sanSwitch1");
			remoteHostUserName_switch 	= CONFIG.getProperty("remoteHostUserName1_sanSwitch1");
			remoteHostPassword_switch 	= CONFIG.getProperty("remoteHostPassword1_sanSwitch1");
			remoteHostType_switch 		= "NodeType_fc_switch";
			remoteHostDesc_switch 		= "Switch";
			
			remoteHostIp_windows = CONFIG.getProperty("remoteHostIp_w1");
			remoteHostUserName_windows = CONFIG.getProperty("remoteHostUserName1_w1");
			remoteHostPassword_windows = CONFIG.getProperty("remoteHostPassword1_w1");
			remoteHostType_windows = "NodeType_windows";
			remoteHostDesc_windows = "windows";
			
			remoteHostIp_linux = CONFIG.getProperty("remoteHostIp_l1");
			remoteHostUserName_linux = CONFIG.getProperty("remoteHostUserName1_l1");
			remoteHostPassword_linux = CONFIG.getProperty("remoteHostPassword1_l1");
			remoteHostDesc_linux = "Linux";
			remoteHostType_linux = "NodeType_linux";
			accessLevel = "none";
			remoteHostSuperUserName	= null;
			remoteHostSuperPasswaord = null;
			
			if (currentOsName.contains("windows")) {
				additionalPkgLoacationKey_switch = "additionalPkg_sanSwitch1_Location_w";
			}
			else {
				additionalPkgLoacationKey_switch = "additionalPkg_sanSwitch1_Location_l";
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
			if(!BaselineLibrary.addAdditionalBaseline(additionalPkgLoacationKey_switch)) {
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
            if(!Nodes.addNodeSwitch(remoteHostIp_switch, remoteHostDesc_switch, false, remoteHostUserName_switch,
            					remoteHostPassword_switch, additionalPkgLoacationKey_switch)) {
            	throw new TestException("Test failed");
            }
         	
			// Print new activity rows
			if(!Activity.printNewlyAddedActivityRows()) {
			    printError("Failed to print latest activity rows.");
			}
			
			// Add Windows Node 
			if(!Nodes.addNodeWindows(remoteHostIp_windows, remoteHostDesc_windows, true, 
								remoteHostUserName_windows, remoteHostPassword_windows)) {
				throw new TestException("Test Failed");
			}
			
			// Print new activity rows
			if(!Activity.printNewlyAddedActivityRows()) {
			    printError("Failed to print latest activity rows.");
			}
         	
			// Add Linux Node
			if(!Nodes.addNodeLinux(remoteHostIp_linux, remoteHostDesc_linux, true, remoteHostUserName_linux, remoteHostPassword_linux,
							  accessLevel, remoteHostSuperUserName, remoteHostSuperPasswaord)) {
				throw new TestException("Test Failed");
			}
         	
			// Print new activity rows
			if(!Activity.printNewlyAddedActivityRows()) {
			    printError("Failed to print latest activity rows.");
			}
         	
			// Go to the Activity Page
			if(!guiSetPage("NodeGroups")) {
				throw new TestException("Failed to load Activity Page.");
			}
			
			//
			// Verification:
			//
		*/
			
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
