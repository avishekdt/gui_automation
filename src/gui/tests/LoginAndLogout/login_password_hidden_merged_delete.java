package gui.tests.LoginAndLogout;

import gui.common.base.CommonHpsum;
import gui.common.base.SelUtil;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

// Assertion: 
// Check whether the password is hidden
//

public class login_password_hidden_merged_delete extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime = "";
	
	String osName="";
	String loginUserName = "";
	String loginPassword = "";
	String login_pwdType;
	String username="";
	String password="";
	
	// Start the test.	
	@Test
	public void test_login_password_hidden_merged_delete() {
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
				throw new Exception("INFO:: Manual Test");
			}
			
			//
			// Setup:
			//
			
			// Kill the HPSUM Service, if already running, and clean the HPSUM logs and start the service.
			if(!hpsumKillCleanStart()) {
				printError("hpsumKillCleanStart failed.");
			}
						
			// Open the Browser and Set Timeouts
			if(!CommonHpsum.openBrowserAndSetTimeouts()) {
				throw new TestException("Test failed");
			}
						
			// Go to the Product URL
			if(!CommonHpsum.openUrl()) {
				printError("URL cannot be opened");
				throw new TestException("Test failed");
			}			
			captureScreenShot();
			
			// 
			// Execution::
			//
			
			// Get username and password
			osName = TestUtil.getOsName();
						
			if(osName.equalsIgnoreCase("Windows")) {
				loginUserName = CONFIG.getProperty("loginUserName_w");
				loginPassword = CONFIG.getProperty("loginPassword_w");
			}
			else {
				loginUserName = CONFIG.getProperty("loginUserName_l");
				loginPassword = CONFIG.getProperty("loginPassword_l");
			}
						
			// Enter the Username
			if(!SelUtil.sendKeysByXpath("LoginPage_Username", loginUserName)) {
				throw new TestException("Test failed");
			}
			captureScreenShot();
			
			// Check whether username is displayed in the user field
			username = SelUtil.getAttributeByXpath("LoginPage_Username","value");
			
			if(!compareTexts(username,loginUserName)){
				printError("Username is not displayed in the user field");
			}		
					
			// Enter the password  
			if(!SelUtil.sendKeysByXpath("LoginPage_Password", loginPassword)) {
				throw new TestException("Test failed");
			}
			captureScreenShot();
			
			//
			// Verification:
			//
			
			// Check whether password is hidden
		   	login_pwdType = SelUtil.getAttributeByXpath("LoginPage_Password","type");
		   	 
		   	if(!(login_pwdType.contains("password"))){
		   		printError("Password is not hidden");
		   	}
		   	
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
