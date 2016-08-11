package gui.tests.MAT;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.Nodes;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

public class RemoteDeployToVm extends TestSuiteBase{
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_RemoteDeployToVm() {
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
			String remoteHostIp_vm1 		= CONFIG.getProperty("remoteHostIp_vm1");
			String remoteHostUserName1_vm1 	= CONFIG.getProperty("remoteHostUserName1_vm1");
			String remoteHostPassword1_vm1 	= CONFIG.getProperty("remoteHostPassword1_vm1");
			String remoteHostDesc_vm1 		= "VMware";
			String remoteHostType_vm1 		= "NodeType_vmware";			
			String vcenter_vm1 		= null;
			String vcentertUserName1_vm1 	= null;
			String vcenterPassword1_vm1 	= null;
			
			// Find out if it is Win-VM or Lin-VM deployment.
			if (currentOsName.contains("windows")) {
				printLogs("Trying Win-VM remote deployment.");
			}
			else {
				printLogs("Trying Lin-VM remote deployment.");
			}
			
			printLogs("Remote host IP: " + remoteHostIp_vm1);
			printLogs("Remote host Type: " + remoteHostType_vm1);
			printLogs("UserName: " + remoteHostUserName1_vm1 + " Password: " + remoteHostPassword1_vm1);
			
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Do Baseline inventory
			if(!BaselineLibrary.performBaselineInventory()) {
				throw new TestException("Test Failed");
			}
			
			// 3. Add Node
			if(!Nodes.addNodeVm(remoteHostIp_vm1, remoteHostDesc_vm1, remoteHostUserName1_vm1, remoteHostPassword1_vm1, 
					false,vcenter_vm1, vcentertUserName1_vm1, vcenterPassword1_vm1)) {
				throw new TestException("Test Failed");
			}
			
			// 4. Do Node inventory
			if(!Nodes.performNodeInventory()) {
				throw new TestException("Test Failed");
			}
			
			// 5. Deploy Node.
			//if(!Nodes.deployNode(remoteHostIp_vm1, remoteHostType_vm1)) {
			//	throw new TestException("Test Failed");
			//}
			// Deploy Node in easy mode
				if(!Nodes.deployNodeEasyMode(remoteHostIp_vm1, remoteHostType_vm1)) {
					throw new TestException("Test Failed");
					}
			// Deploy Node in Advanced mode
			//	if(!Nodes.deployNodeAdvMode(remoteHostIp_vm1, remoteHostType_vm1)) {
			//		throw new TestException("Test Failed");
			//	}
				
			// 6. View Logs
			if(!Nodes.viewLogsAfterDeploy(remoteHostIp_vm1, remoteHostType_vm1)) {
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
