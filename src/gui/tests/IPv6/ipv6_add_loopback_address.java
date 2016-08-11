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
 * Date: 05/17/2016
 * Test case Description: Check that the loopback address in IPV6 shouldnt be added when localhost is already present in the list.
 * Prerequistes: localhost is configured with ipv6 address.
 * Steps:
 * 1. Launch HPSUM.
 * 2. Do Baseline Inventory.
 * 3. Add node with loopback IPv6 Address and check the error message.
 * Variables to update: remoteHostIpv6gDNS_w1, remoteHostIpv6gDNS_l1
 */

public class ipv6_add_loopback_address extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_ipv6_add_loopback_address() {
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
			String remoteHostIp = CONFIG.getProperty("localHostIpv6_loopback");
			String remoteHostDesc       = "loopback";			
			String remoteHostUserName = "";
			String remoteHostPassword = "";					
			String accessLevel 				= "none";
			String remoteHostSuperUserName	= null;
			String remoteHostSuperPasswaord	= null;		
			
			if(currentOsName.contains("windows")) {	
				verText = "windows";				
				remoteHostUserName = CONFIG.getProperty("loginUserName_w");
				remoteHostPassword = CONFIG.getProperty("loginPassword_w");	
			}
			else {
				verText = "linux";				
				remoteHostUserName = CONFIG.getProperty("loginUserName_l");
				remoteHostPassword = CONFIG.getProperty("loginPassword_l");
			}
			
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Wait for all the Baseline inventory to complete.
			if(!BaselineLibrary.performBaselineInventory()){
				throw new TestException("Test failed");
			}	
			
			//3. Adding a node without baseline.			
			Nodes.node_loopback = true; Nodes.without_baseline = true;
			if (verText == "windows"){
				if(!Nodes.addNodeWindows(remoteHostIp, remoteHostDesc, true, 
						remoteHostUserName, remoteHostPassword)) {
				throw new TestException("Test Failed");
			}
			}else{
				if(!Nodes.addNodeLinux(remoteHostIp, remoteHostDesc, true, remoteHostUserName, remoteHostPassword ,
						  accessLevel, remoteHostSuperUserName, remoteHostSuperPasswaord)) {
					throw new TestException("Test Failed");
				}				
			}
			Nodes.node_loopback = false; Nodes.without_baseline = false;			
			captureScreenShot();
			printLogs("DONE.");
		}
		catch (Throwable t) {
			if(Nodes.node_loopback || Nodes.without_baseline){
				Nodes.node_loopback = false; Nodes.without_baseline = false;
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
