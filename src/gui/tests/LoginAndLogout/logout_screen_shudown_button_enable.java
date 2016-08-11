package gui.tests.LoginAndLogout;

import gui.common.base.CommonHpsum;
import gui.common.base.SelUtil;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

// Assertion: 
// Enable Shutdown button on the login page.
//

public class logout_screen_shudown_button_enable extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime = "";

	String loginUserName = "";
	String loginPassword = "";
	String loginShutdownClassAttr = "";
	String loginUsernameFieldRequiredErrorMsg="";
	String loginPasswordFieldRequiredErrorMsg = "";
	String FieldRequired = "This field is required.";
			
	// Start the test.	
	@Test
	public void test_logout_screen_shudown_button_enable() {
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
			
			// Check whether shutdown button is disabled
			loginShutdownClassAttr = SelUtil.getAttributeByXpath("LoginPage_ShutdownButton","class");
			
			if(!compareTexts("hp-disabled", loginShutdownClassAttr)){
				printError("Shutdown button is not disabled");
				throw new TestException("Test failed");
			}
			
			// Click on shutdown button
			if(!SelUtil.clickButtonByXpath("LoginPage_ShutdownButton")){
				throw new TestException("Test failed");
			}
			captureScreenShot();
			
			// Check the username field required error message
			if(!SelUtil.checkElementPresenceByXpath("LoginPage_UsernameFieldRequiredErrorMsg")){
				throw new TestException("Test failed");
			}
			
			// Check for the error message
			loginUsernameFieldRequiredErrorMsg = SelUtil.getTextByXpath("LoginPage_UsernameFieldRequiredErrorMsg");
			
			if(!compareTexts(FieldRequired, loginUsernameFieldRequiredErrorMsg)){
				throw new TestException("Test failed");
			}
			
			// Check the password field required error message 
			if(!SelUtil.checkElementPresenceByXpath("LoginPage_PasswordFieldRequiredErrorMsg")){
				throw new TestException("Test failed");
			}
			
			// Check for the error message
			loginPasswordFieldRequiredErrorMsg = SelUtil.getTextByXpath("LoginPage_PasswordFieldRequiredErrorMsg");
			
			if(!compareTexts(FieldRequired, loginPasswordFieldRequiredErrorMsg)){
				throw new TestException("Test failed");
			}
						
			// Get username and password					
			if(currentOsName.equalsIgnoreCase("Windows")) {
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
			
			// Enter the password  
			if(!SelUtil.sendKeysByXpath("LoginPage_Password", loginPassword)) {
				throw new TestException("Test failed");
			}
			captureScreenShot();
			
			//
			// Verification:
			//
			
			// Check whether the shutdown button is enabled 
			loginShutdownClassAttr = SelUtil.getAttributeByXpath("LoginPage_ShutdownButton","class");
			
			if(loginShutdownClassAttr.contains("hp-disabled")){
				printError("Shutdown button is not enabled");
				throw new TestException("Test failed");
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
