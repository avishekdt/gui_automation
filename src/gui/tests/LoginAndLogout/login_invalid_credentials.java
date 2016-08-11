package gui.tests.LoginAndLogout;

import gui.common.base.CommonHpsum;
import gui.common.base.SelUtil;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

// Assertion: 
// Login with invalid credentials.
//

public class login_invalid_credentials extends TestSuiteBase {
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
	String invalidCresdentialErrorMsg_expected = "Incorrect username/password or user does not have admin rights.";
	String currentFocusName_pwd = "password";
	String invalidCresdentialErrorMsg_actual = "";
	String password_value = "";
	
	HashMap<String,String> hash = new HashMap<String,String>();
	
	// Start the test.	
	@Test
	public void test_login_invalid_credentials() {
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
			
			hash.put("Admin","123");
			hash.put("aa", "*&&");
			hash.put("!!123","abcd");
			hash.put("Administrator","Abcdefghijklmnopqrstabcdefghijklmnopqrstabcdefghijklmnopqrstabcdefghijklmnopqrstabcdefghijklmnopqrstabcdefghijklmnopqrstabcdef!1Abcdefghijklmnopqrstabcdefghijklmnopqrstabcdefghijklmnopqrstabcdefghijklmnopqrstabcdefghijklmnopqrstabcdefghijklmnopqrstabcdef!11");
			
			Set<Entry<String, String>> set = hash.entrySet();
			Iterator<Entry<String, String>> i = set.iterator();
			
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
			
			while(i.hasNext()){
				Map.Entry me = (Map.Entry)i.next();
				loginUserName = (String) me.getKey();
			    loginPassword = (String) me.getValue();
			    
			    // Enter the Username
			    if(!SelUtil.sendKeysByXpath("LoginPage_Username", loginUserName)) {
			    	throw new TestException("Test failed");
			    }
			    captureScreenShot();
			
			    // Enter a letter in the password  
			    if(!SelUtil.sendKeysByXpath("LoginPage_Password", loginPassword)) {
			    	throw new TestException("Test failed");
			    }
			    captureScreenShot();
			
			    // Click on Login button
			    if(!SelUtil.clickByXpath("LoginPage_LoginButton")) {
			    	throw new TestException("Test failed");
			    }
			    captureScreenShot();
			    
		    //
			// Verification:
			//
			    
			    // Check the presence of error message
			    if(!SelUtil.checkElementPresenceByXpath("LoginPage_InvalidCresdentialErrorMsg")){
			    	throw new TestException("Test failed");
			    }
			
			    // Verify the error message
			    invalidCresdentialErrorMsg_actual = SelUtil.getTextByXpath("LoginPage_InvalidCresdentialErrorMsg");
			
			    if(!compareTexts(invalidCresdentialErrorMsg_expected, invalidCresdentialErrorMsg_actual)){
			    	throw new TestException("Test failed");
			    }
			
			    sleep(5);
			
			    // Verify whether password field is empty
			    password_value = SelUtil.getAttributeByXpath("LoginPage_Password","value");
			
			    if(!password_value.equals("")){
			    	printError("Password field is NOT empty.");
			    	throw new TestException("Test failed");
			    }
			
			    //check whether current focus is on password
			    if(!SelUtil.checkAttributeValueOfCurrentFocus("name","password")){
			    	printError("Focus is not on password");
			    	throw new TestException("Test failed");
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
