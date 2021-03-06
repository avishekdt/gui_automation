package gui.tests.Baseline;
import gui.common.base.Activity;
import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.Nodes;
//import gui.common.base.TestBase;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;
import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

//
//Assertion: 
//<Write the purpose of the test case>
//
/*Author: Praveen AC
 * Date: 04/21/2016
 * Need to have unc path beforing runnning this test case.
*/
public class bl_unc_processing_abl_hpsum_standalone extends TestSuiteBase {	
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	@Test
	public void test_bl_unc_processing_abl_hpsum_standalone() {
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
			
			// The keys to use. Values will be got inside method from CONFIG file.
			String ablUncPathKey 		= "additionalPkg_UNC_Location1";
			String ablUncUserNameKey 	= "additionalPkg_UNC_UserName1"; 
			String ablUncPasswordKey 	= "additionalPkg_UNC_Password1";
			String sppLocation_toSelect = CONFIG.getProperty("additionalPkg_UNC_Location1");
			String remoteHostIp_w1 			= CONFIG.getProperty("remoteHostIp_w1");
			String remoteHostUserName1_w1 	= CONFIG.getProperty("remoteHostUserName1_w1");
			String remoteHostPassword1_w1 	= CONFIG.getProperty("remoteHostPassword1_w1");
			String remoteHostDesc_w1 		= "Windows";
			String remoteHostType_w1 		= "NodeType_windows";
									
			printLogs("Remote host host IP: " + remoteHostIp_w1);
			printLogs("Remote host Type: " + remoteHostType_w1);
			printLogs("UserName: " + remoteHostUserName1_w1 + " Password: " + remoteHostPassword1_w1);
			printLogs("Adding additional SPP "+sppLocation_toSelect);
			
			
			// Execution:
			//Deleting the stored network share credentials.			
			executeCommand("net use * /delete /yes");
			standaloneHpsum = true;
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 1. Go to BL page.
			if(!guiSetPage("BaselineLibrary")) {
				printError("Failed to reach Baseline Library page.");
				printFunctionReturn(fn_fail);
				throw new TestException("Test failed");
			}
								    
		    // 2. Add UNC baseline
 			if(!BaselineLibrary.addAblWithUncPath(ablUncPathKey, ablUncUserNameKey, ablUncPasswordKey)) {
 				throw new TestException("Test failed");
 			}
 			
			
			// Verification:			
 			// Checking the baseline is added or not
 			if(!BaselineLibrary.ifBlExists(sppLocation_toSelect))
			{
				captureScreenShot();
				printLogs(sppLocation_toSelect +" is not present in Baseline list in Baseline page.");
				throw new TestException("Test Failed");
			}
 			
 			// Print new activity rows
			if(!Activity.printNewlyAddedActivityRows()) {
			    printError("Failed to print latest activity rows.");
			}
			
			// 3. Adding windows node
			Nodes.without_baseline = true;
			if(!Nodes.addNodeWindows(remoteHostIp_w1, remoteHostDesc_w1, true, 
					remoteHostUserName1_w1, remoteHostPassword1_w1)) {
			throw new TestException("Test Failed");
			}
			Nodes.without_baseline = false;
			// 4. Perform Node inventory with UNC path BL
			if(!killRemoteHpsum(remoteHostDesc_w1, remoteHostIp_w1, remoteHostUserName1_w1, remoteHostPassword1_w1)) {
				printError("Failed to kill hpsum service in dn_installation_option_downgrade_rewrite_software");
			}
			
			if(!Nodes.specifyBlAndPerformNodeInventory(sppLocation_toSelect)){
				throw new TestException("Test Failed");
			}
			
			// Deploy the node with the UNC path BL
			if(!Nodes.deployNodeEasyMode(remoteHostIp_w1, remoteHostType_w1)) {
				throw new TestException("Test Failed");
			}
			
			captureScreenShot();
			printLogs("DONE.");
		}
		catch (Throwable t) {
			if(Nodes.without_baseline){
				Nodes.without_baseline = false;
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
				standaloneHpsum = false;
			}
			catch (Throwable t) {
				if(standaloneHpsum){
					standaloneHpsum = false;
				}
				printError("Exception occurred in finally.");
			}
		}	
	}
}