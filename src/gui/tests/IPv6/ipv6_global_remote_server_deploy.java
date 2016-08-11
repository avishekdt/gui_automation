package gui.tests.IPv6;
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
 * Date: 05/12/2016
 * Test case Description: Verify that HP SUM can add remote server node with global IPV6 address and deploy on the same.
 * Prerequistes: Configure localhost and remote server node with global IPV6 IP address. (Optional: Disable IPv4 address)
 * Steps:
 * 1. Launch HPSUM.
 * 2. Do Baseline Inventory.
 * 3. Add node with IPv6 Address.
 * 4. Perform Inventory.
 * 5. Do the deployment by selecting all the components.
 * Variables to update: remoteHostIpv6g_w1, remoteHostIpv6g_l1
 */



public class ipv6_global_remote_server_deploy extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_ipv6_global_remote_server_deploy() {
		screenshot_name 	= test_name;
		screenshot_counter 	= 0;
		String verText= "";
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
			String remoteHostIp_w1 			= CONFIG.getProperty("remoteHostIpv6g_w1");
			String remoteHostUserName1_w1 	= CONFIG.getProperty("remoteHostUserName1_w1");
			String remoteHostPassword1_w1 	= CONFIG.getProperty("remoteHostPassword1_w1");
			String remoteHostDesc_w1 		= "Windows";
			String remoteHostType_w1 		= "NodeType_windows";
			String remoteHostIp_l1 			= CONFIG.getProperty("remoteHostIpv6g_l1");
			String remoteHostUserName1_l1 	= CONFIG.getProperty("remoteHostUserName1_l1");
			String remoteHostPassword1_l1 	= CONFIG.getProperty("remoteHostPassword1_l1");
			String remoteHostDesc_l1 		= "Linux";
			String remoteHostType_l1		= "NodeType_linux";
			String accessLevel 				= "none";
			String remoteHostSuperUserName	= null;
			String remoteHostSuperPasswaord	= null;
			String remoteHostIp = "";
			String remoteHostType = "";
			String remoteHostUserName = "";
			String remoteHostPassword = "";
			
				
			// Set output location and Version name
			if(currentOsName.contains("windows")) {
				printLogs("Trying Win-Win remote deployment.");
				verText = "windows";
				remoteHostIp = remoteHostIp_w1;
				remoteHostType = remoteHostType_w1;
				remoteHostUserName = remoteHostUserName1_w1;
				remoteHostPassword = remoteHostPassword1_w1;				
			}
			else {
				printLogs("Trying Lin-Lin remote deployment.");
				verText = "linux";
				remoteHostIp = remoteHostIp_l1;
				remoteHostType = remoteHostType_l1;
				remoteHostUserName = remoteHostUserName1_l1;
				remoteHostPassword = remoteHostPassword1_l1;				
			}			
			printLogs("Remote host host IP: " + remoteHostIp);
			printLogs("Remote host Type: " + remoteHostType);
			printLogs("UserName: " + remoteHostUserName + " Password: " + remoteHostPassword);
			
			
			// Execution:
			
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Wait for all the Baseline inventory to complete.
			if(!BaselineLibrary.performBaselineInventory()){
				throw new TestException("Test failed");
			}
			
			//3. Adding a node without baseline.				
			if (verText == "windows"){
				if(!Nodes.addNodeWindows(remoteHostIp_w1, remoteHostDesc_w1, true, 
						remoteHostUserName1_w1, remoteHostPassword1_w1)) {
				throw new TestException("Test Failed");
			}
			}else{
				if(!Nodes.addNodeLinux(remoteHostIp_l1, remoteHostDesc_l1, true, remoteHostUserName1_l1, remoteHostPassword1_l1 ,
						  accessLevel, remoteHostSuperUserName, remoteHostSuperPasswaord)) {
					throw new TestException("Test Failed");
				}				
			}
			
			// 4. Do Node inventory
			if(!killRemoteHpsum(remoteHostDesc_w1, remoteHostIp_w1, remoteHostUserName1_w1, remoteHostPassword1_w1)) {
				printError("Failed to kill hpsum service in dn_installation_option_downgrade_rewrite_software");
			}
			//Performing node inventory
			if(!Nodes.performNodeInventory()){
				throw new TestException("Test Failed");
			}
			
			//5. Deploy Node in Advanced mode
			if(!Nodes.deployNodeAdvMode(remoteHostIp, remoteHostType)) {
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
