package gui.tests.MAT;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.Nodes;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

public class RemoteDeployToLin extends TestSuiteBase{
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_RemoteDeployToLin() {
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
			
			//
			// Setup:
			//
			String remoteHostIp_l1 			= CONFIG.getProperty("remoteHostIp_l1");
			String remoteHostUserName1_l1 	= CONFIG.getProperty("remoteHostUserName1_l1");
			String remoteHostPassword1_l1 	= CONFIG.getProperty("remoteHostPassword1_l1");
			String remoteHostDesc_l1 		= "Linux";
			String remoteHostType_l1		= "NodeType_linux";
			String accessLevel 				= "none";
			String remoteHostSuperUserName	= null;
			String remoteHostSuperPasswaord	= null;
			
			// Find out if it is Win-Lin or Lin-Lin deployment.
			if (currentOsName.contains("windows")) {
				printLogs("Trying Win-Lin remote deployment.");
			}
			else {
				printLogs("Trying Lin-Lin remote deployment.");
			}
			
			printLogs("Remote host IP: " + remoteHostIp_l1);
			printLogs("Remote host Type: " + remoteHostType_l1);
			printLogs("UserName: " + remoteHostUserName1_l1 + " Password: " + remoteHostPassword1_l1);
			
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Do Baseline inventory
			if(!BaselineLibrary.performBaselineInventory()) {
				throw new TestException("Test Failed");
			}
			
			// 3. Add Linux Node.
			if(!Nodes.addNodeLinux(remoteHostIp_l1, remoteHostDesc_l1, true, remoteHostUserName1_l1, remoteHostPassword1_l1 ,
							  accessLevel, remoteHostSuperUserName, remoteHostSuperPasswaord)) {
				throw new TestException("Test Failed");
			}
			
			// 4. Do Node inventory
			if(!killRemoteHpsum(remoteHostDesc_l1, remoteHostIp_l1, remoteHostUserName1_l1, remoteHostPassword1_l1)) {
				printError("Failed to kill hpsum service in RemoteDeployToLin");
			}
			if(!Nodes.performNodeInventory()) {
				throw new TestException("Test Failed");
			}
			
			// 5. Deploy Node.
			//if(!Nodes.deployNode(remoteHostIp_l1, remoteHostType_l1)) {
			//	throw new TestException("Test Failed");
			//}
			
			// Deploy Node in easy mode
			//if(!Nodes.deployNodeEasyMode(remoteHostIp_l1, remoteHostType_l1)) {
			//	throw new TestException("Test Failed");
			//	}
			// Deploy Node in Advanced mode
			if(!Nodes.deployNodeAdvMode(remoteHostIp_l1, remoteHostType_l1)) {
				throw new TestException("Test Failed");
			}
			// 6. View Logs
			if(!Nodes.viewLogsAfterDeploy(remoteHostIp_l1,remoteHostType_l1)) {
				throw new TestException("Test Failed");
			}
			captureScreenShot();
			
			printLogs("DONE.");
		}
		catch (Throwable t) {
			if (skip) {
				test_result = "SKIP";
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
			printLogs("DONE");
		}
	}
}
