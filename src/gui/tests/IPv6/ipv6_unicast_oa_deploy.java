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
 * Date: 05/16/2016
 * Test case Description: Verify whether HP SUM deploys on OA node which has unicast IPV6 addtress configured.
 * Prerequistes: Configure localhost and OA node with unicast IPV6 IP address. (Optional: Disable IPv4 address on localhost.)
 * Steps:
 * 1. Launch HPSUM.
 * 2. Do Baseline Inventory.
 * 3. Add node with IPv6 Address.
 * 4. Perform Inventory.
 * 5. Do the deployment by selecting all the components.
 * Variables to update: remoteHostIpv6u_oa1, remoteHostUserNameIPv6u1_oa1, remoteHostPasswordUPv6u1_oa1
 */



public class ipv6_unicast_oa_deploy extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_ipv6_unicast_oa_deploy() {
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
			String remoteHostIp_oa1 		= CONFIG.getProperty("remoteHostIpv6u_oa1");
			String remoteHostUserName1_oa1 	= CONFIG.getProperty("remoteHostUserName1_oa1");
			String remoteHostPassword1_oa1 	= CONFIG.getProperty("remoteHostPassword1_oa1");
			String remoteHostDesc_oa1 		= "oa";
			String remoteHostType_oa1 		= "NodeType_enclosure";
					
			printLogs("Remote host host IP: " + remoteHostIp_oa1);
			printLogs("Remote host Type: " + remoteHostDesc_oa1);
			printLogs("UserName: " + remoteHostUserName1_oa1 + " Password: " + remoteHostPassword1_oa1);
			
			
			// Execution:
			
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Wait for all the Baseline inventory to complete.
			if(!BaselineLibrary.performBaselineInventory()){
				throw new TestException("Test failed");
			}
			
			//3. Adding a node.
			if(!Nodes.addNodeOa(remoteHostIp_oa1, remoteHostDesc_oa1, false, true,
					remoteHostUserName1_oa1, remoteHostPassword1_oa1)) {
			throw new TestException("Test Failed");
			}
			
			// 4. Do Node inventory
			//Performing node inventory
			if(!Nodes.performNodeInventory()){
				throw new TestException("Test Failed");
			}
			
			//5. Deploy Node in Advanced mode
			if(!Nodes.deployNodeAdvMode(remoteHostIp_oa1, remoteHostType_oa1)) {
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
