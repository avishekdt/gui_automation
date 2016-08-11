package gui.tests.NodesDeploy;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.Nodes;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

//
//Assertion: 
//<Write the purpose of the test case>
//

public class dn_installation_option_downgrade_rewrite_software extends TestSuiteBase {	
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_dn_installation_option_downgrade_rewrite_software() {
		screenshot_name 	= test_name;
		screenshot_counter 	= 0;
		
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
			
			// Setup:
			/*Before running this test case, do the deployment using gen9snap3 SPP so that we can select 
			  downgrade software option and check the components weather selected correctly or not.
			  sppLocation_toSelect -  provide the spp path in the config.properties file and
			  give the value for this variable.
				*/
			
			String remoteHostIp_w1 			= CONFIG.getProperty("remoteHostIp_w1");
			String remoteHostUserName1_w1 	= CONFIG.getProperty("remoteHostUserName1_w1");
			String remoteHostPassword1_w1 	= CONFIG.getProperty("remoteHostPassword1_w1");
			String remoteHostDesc_w1 		= "Windows";
			String remoteHostType_w1 		= "NodeType_windows";
			String sppLocation_toSelect = CONFIG.getProperty("additional_Comp_Location_w");
			
			// Find out if it is Win-Lin or Lin-Lin deployment.
			if (currentOsName.contains("windows")) {
				printLogs("Trying Win-Win remote deployment.");
			}
			else {
				printLogs("WARNING:: Skipping the test as host OS is " + currentOsName + " and trying to deploy on Windows");
				skip = true;
				test_result = "SKIP";
				throw new Exception();				
			}
			
			printLogs("Remote host host IP: " + remoteHostIp_w1);
			printLogs("Remote host Type: " + remoteHostType_w1);
			printLogs("UserName: " + remoteHostUserName1_w1 + " Password: " + remoteHostPassword1_w1);
			printLogs("Adding additional SPP "+sppLocation_toSelect);
			
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Do Baseline inventory
			/*Steps for baseline inventory
			 * 1. Add additional baseline which contains the lower version of component compare to the spp.(Using Gen9snap3 SPP).
		    */
			if(!BaselineLibrary.performBaselineInventory()) {
				throw new TestException("Test Failed");
			}
			//Adding additional package for downloading the component.
		    if(!BaselineLibrary.addAdditionalBaseline(sppLocation_toSelect)) {
				throw new TestException("Test Failed");
			}
			//3. Adding a node without baseline.			
			Nodes.without_baseline = true;
			if(!Nodes.addNodeWindows(remoteHostIp_w1, remoteHostDesc_w1, true, 
						remoteHostUserName1_w1, remoteHostPassword1_w1)) {
				throw new TestException("Test Failed");
			}
			Nodes.without_baseline = false;
			
			// 4. Do Node inventory
			if(!killRemoteHpsum(remoteHostDesc_w1, remoteHostIp_w1, remoteHostUserName1_w1, remoteHostPassword1_w1)) {
				printError("Failed to kill hpsum service in dn_installation_option_downgrade_rewrite_software");
			}
			//Selecting the additional baseline package for performing the download.
			if(!Nodes.specifyBlAndPerformNodeInventory(sppLocation_toSelect)){
				throw new TestException("Test Failed");
			}
			
			// Deploy Node in Advanced mode
			if(!Nodes.deployNodeAdvMode_installation_Options(remoteHostIp_w1, remoteHostType_w1,"Downgrade & Rewrite Software")) {
				throw new TestException("Test Failed");
			}
			
			// 6. View Logs
			if(!Nodes.viewLogsAfterDeploy(remoteHostIp_w1, remoteHostType_w1)) {
				throw new TestException("Test Failed");
			}
						
			//
			// Verification: Need to add the verification point where it has selected the correct components.
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