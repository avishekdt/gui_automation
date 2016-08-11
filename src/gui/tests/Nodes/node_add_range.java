package gui.tests.Nodes;

import java.util.Arrays;
import java.util.List;

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

public class node_add_range extends TestSuiteBase {	
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_template_test_1() {
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
			
			//
			// Setup:
			//
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			String remoteHostIpList_w1 		= CONFIG.getProperty("remoteHostIp_w1_range");
			String remoteHostUserName1_w1 	= CONFIG.getProperty("remoteHostUserName1_w1");
			String remoteHostPassword1_w1 	= CONFIG.getProperty("remoteHostPassword1_w1");
			String remoteHostDesc_w1 		= "Windows";
			
			// 
			// Execution::
			//
			
			//add list of windows nodes
			if(!Nodes.addNodeWindows(remoteHostIpList_w1, remoteHostDesc_w1, true, 
					remoteHostUserName1_w1, remoteHostPassword1_w1)) {
			throw new TestException("Test Failed");
			}
			
			//
			// Verification:
			//
			List<String> WinNodeList = Arrays.asList(remoteHostIpList_w1.split("-"));
			for(int i = 0; i < WinNodeList.size(); i++) {
				
	                if(!Nodes.findNodeOnLeftPane(WinNodeList.get(i))){
					throw new TestException("Test Failed");
				}
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