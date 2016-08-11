package gui.tests.LoginAndLogout;

import gui.common.base.CommonHpsum;
import gui.common.base.SelUtil;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

// Assertion: 
// Check seesion information and log off and shutdown option.
//

public class logout_person_icon extends TestSuiteBase {
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
	String sessionUser = "";
	String logoffOption_actual = "";
	String logOffOption_expected = "Log Off - Current user.";
	String shutdownOption_actual = "";
	String shutdownOption_expected = "Shutdown - This option will shutdown HP SUM engine.";
	String loggedIn_actual = "";
	String loggedIn_expected = "Logged in";
	String shutdownMsg1_actual = "";
	String shutdownMsg2_actual = "";
	String shutdownMsg1_expected = "HP Smart Update Manager has been successfully shutdown";
	String shutdownMsg2_expected = "Thank you for using HP Smart Update Manager";
	
	
	// Start the test.
	@Test
	public void test_logout_person_icon() {
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
			
			// Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 
			// Execution::
			//
		 	
		    // Click on session 
		   	if(!SelUtil.clickByXpath("HomePage_SessionIcon")){
		   		throw new TestException("Test failed");
		   	}
		   	captureScreenShot();
		   	
		   	sessionUser = SelUtil.getTextByXpath("HomePage_SessionUserName");
		   	
		   	// Verify the username
		   	if(!compareTexts(loginUserName,sessionUser)){
		   		throw new TestException("Test failed");
		   	}
				   	
		   	// Verify the time
		   	if(!SelUtil.checkElementPresenceByXpath("HomePage_SessionTime")){
		   		throw new TestException("Test failed");
		   	}
		   	
		    // Check whether "logged in" is present 
		   	loggedIn_actual = SelUtil.getTextByXpath("HomePage_SessionLoggedInLabel");
		   	
		   	if(!compareTexts(loggedIn_expected, loggedIn_actual)){
		   		printError("Logged In is not present");
		   	}
		   	
		   	// Verify the presence of logout link
		   	if(!SelUtil.checkElementPresenceByXpath("HomePage_SessionLogoutLink")){
		   		throw new TestException("Test failed");
		   	}
		   	
		   	// Click on logout button
		   	if(!SelUtil.clickByXpath("HomePage_SessionLogoutLink")){
		   		throw new TestException("Test failed");
		   	}
		   	
		    // Check for shutdown window
		   	if(!SelUtil.checkElementPresenceByXpath("HomePage_SessionLogoutHeading")){
		   		throw new TestException("Test Failed");
		   	}
		   	captureScreenShot();
		   	
		   	// Check the presence of log off option
		   	logoffOption_actual = SelUtil.getTextByXpath("HomePage_SessionLogoutLogOffLabel"); 
		   	
		   	if(!compareTexts(logOffOption_expected, logoffOption_actual)){
		   		throw new TestException("Test failed");
		   	}
		   	
		   	// Check the presence of shutdown option
	        shutdownOption_actual = SelUtil.getTextByXpath("HomePage_SessionLogoutShutdownLabel"); 
		   	
		   	if(!compareTexts(shutdownOption_expected, shutdownOption_actual)){
		   		throw new TestException("Test failed");
		   	}
		   	
		   	// Click on log off
		   	if(!SelUtil.clickByXpath("HomePage_SessionLogoutLogOffRadioButton")){
		   		throw new TestException("Test failed");
		   	}
		   	captureScreenShot();
		   	
		    // Click ok button 
		   	if(!SelUtil.clickByXpath("HomePage_SessionLogoutOkButton")){
		   		throw new TestException("Test failed");
		   	}
		   	
		   	sleep(10);
		   	
		   	// Check whether login page has appeared
		   	if(!SelUtil.checkElementPresenceByXpath("LoginPage_Heading")){
		   		throw new TestException("Test failed");
		   	}
		   	captureScreenShot();
		   	
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
		   	
		    // Click the login button
		    if(!SelUtil.clickByXpath("LoginPage_LoginButton")) {
		    	throw new TestException("Test failed");		
		    }
		    
		    // Wait for user to login:  HomePage_Heading
		 	if(!SelUtil.checkElementPresenceByXpath("HomePage_Heading")) {
		 		throw new TestException("Test failed");	
		 	}
		 	captureScreenShot();
		 	
		    // Click on session 
		   	if(!SelUtil.clickByXpath("HomePage_SessionIcon")){
		   		throw new TestException("Test failed");
		   	}
		   	captureScreenShot();
		   	
			// Click on logout link
		   	if(!SelUtil.clickByXpath("HomePage_SessionLogoutLink")){
		   		throw new TestException("Test failed");
		   	}
		   	
		    // Check for shutdown window
		   	if(!SelUtil.checkElementPresenceByXpath("HomePage_SessionLogoutHeading")){
		   		throw new TestException("Test Failed");
		   	}
		   	captureScreenShot();
		   	
		   	// Click on shutdown option
		   	if(!SelUtil.clickByXpath("HomePage_SessionLogoutShutdownRadioButton")){
		   		throw new TestException("Test failed");
		   	}
		   	captureScreenShot();
		   	
		    // Click ok button 
		   	if(!SelUtil.clickByXpath("HomePage_SessionLogoutOkButton")){
		   		throw new TestException("Test failed");
		   	}
		   	
		   	//
			// Verification:
			//
		   	
		    // Check for shutdown message
		   	if(!SelUtil.checkElementPresenceByXpath("Shutdown_ShutdownMessage")){
		   		throw new TestException("Test failed");
		   	}
		   	captureScreenShot();
		   	
		   	shutdownMsg1_actual = SelUtil.getTextByXpath("Shutdown_ShutdownMessage1");
		   	
		    if(!compareTexts(shutdownMsg1_expected, shutdownMsg1_actual)){
		    	printError("Shutdown message1 did not match");
		    }
		    
            shutdownMsg2_actual = SelUtil.getTextByXpath("Shutdown_ShutdownMessage2");
		   	
		    if(!compareTexts(shutdownMsg2_expected, shutdownMsg2_actual)){
		    	printError("Shutdown message2 did not match");
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
