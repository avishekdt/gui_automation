package gui.tests.Baseline;
//import gui.common.base.Activity;
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
 * Date: 04/29/2016
 * Need to have Additional path before running this test case.
*/
public class bl_local_cbl_processing_hpsum_standalone extends TestSuiteBase {	
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_bl_local_cbl_processing_hpsum_standalone() {
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
			
			// The keys to use. Values will be got inside method from CONFIG file.
			
			String remoteHostIp_w1 			= CONFIG.getProperty("remoteHostIp_w1");
			String remoteHostUserName1_w1 	= CONFIG.getProperty("remoteHostUserName1_w1");
			String remoteHostPassword1_w1 	= CONFIG.getProperty("remoteHostPassword1_w1");
			String remoteHostDesc_w1 		= "Windows";
			String remoteHostType_w1 		= "NodeType_windows";
			String remoteHostIp_l1 			= CONFIG.getProperty("remoteHostIp_l1");
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
			String sppLocation_toSelect = "";
			String sppLocation_toSelect_w = CONFIG.getProperty("cust_baseline_Location_w");
			String sppLocation_toSelect_l = CONFIG.getProperty("cust_baseline_Location_l");
				
			// Set output location and Version name
			if(currentOsName.contains("windows")) {
				printLogs("Trying Win-Win remote deployment.");
				verText = "windows";
				remoteHostIp = remoteHostIp_w1;
				remoteHostType = remoteHostType_w1;
				remoteHostUserName = remoteHostUserName1_w1;
				remoteHostPassword = remoteHostPassword1_w1;
				sppLocation_toSelect = sppLocation_toSelect_w;
			}
			else {
				printLogs("Trying Lin-Lin remote deployment.");
				verText = "linux";
				remoteHostIp = remoteHostIp_l1;
				remoteHostType = remoteHostType_l1;
				remoteHostUserName = remoteHostUserName1_l1;
				remoteHostPassword = remoteHostPassword1_l1;
				sppLocation_toSelect = sppLocation_toSelect_l;
			}
			
			printLogs("Remote host host IP: " + remoteHostIp);
			printLogs("Remote host Type: " + remoteHostType);
			printLogs("UserName: " + remoteHostUserName + " Password: " + remoteHostPassword);
			printLogs("Adding additional SPP "+sppLocation_toSelect);
			
			// Execution:
			
			// 1. Do test setup
			standaloneHpsum = true;
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
						
			// 2. Go to BL page.
			if(!guiSetPage("BaselineLibrary")) {
				printError("Failed to reach Baseline Library page.");
				printFunctionReturn(fn_fail);
				throw new TestException("Test failed");
			}
							
			//Adding additional package for downloading the component.
		    if(!BaselineLibrary.addAdditionalBaseline(sppLocation_toSelect)) {
				throw new TestException("Test Failed");
			}
		    
		    //Verifying the baseline exists or not
		    if(BaselineLibrary.ifBlExists(sppLocation_toSelect))
			{
				captureScreenShot();
				printLogs(sppLocation_toSelect +" is present in Baseline list in Baseline page.");
				//throw new TestException("Test Failed");
			}
			
		    //3. Adding a node without baseline.			
			Nodes.without_baseline = true;
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
			if(!Nodes.deployNodeAdvMode(remoteHostIp, remoteHostType)) {
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