package gui.tests.MAT;

import java.io.IOException;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;
import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.Nodes;
import gui.common.base.SelUtil;

public class LocalDeploy extends TestSuiteBase{
	String test_name = this.getClass().getSimpleName();
	String runmodes[] = null;
	static int count = -1;
	static boolean skip = false;
	static boolean fail = false;
	static boolean isTestPass = true;
	String test_result = "PASS";
	long initialTime;
	long finalTime;
	String executionTime = "";
	
	// Check if the test case has to be skipped.
	// BeforeTest will not be repeated for data Parameterization. 
	@BeforeTest
	public void checkTestSkip() {
		printTestStartLine(test_name);
		
		printLogs("Checking Runmode of the test.");
		if(!TestUtil.isTestCaseRunnable(suite_xl, test_name)) {
			printLogs("WARNING:: Runmode of the test '" + test_name + "' set to 'N'. So Skipping the test.");
			throw new SkipException("WARNING:: Runmode of the test '" + test_name + "' set to 'N'. So Skipping the test.");
		}
		
		// Load the runmodes for the data of the test.
		runmodes = getDataSetRunmodes(suite_xl, test_name);
	}
	
	@BeforeMethod
	public void testParamSetup() {
		testParamStart();
		printLogs("Test Param Setup:");
	}
	
	@Test(dataProvider="getTestData")
	public void test_LocalDeploy(String col1) {
		screenshot_name = test_name;
		screenshot_counter = 0;
		
		printLogs("Starting " + test_name + " with values : " + col1);
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			printLogs("WARNING:: Skipping the data set as runmode set to 'N' : " + col1);
			skip = true;
			throw new SkipException("WARNING:: Skipping the data set as runmode set to 'N' : " + col1);
		}
		else {
			printLogs("Starting the test.");
		}
		
		// Update global and test lists.
		if(!testInitialize(test_name)) {
			printError("Failed to perform test initialize.");
		}
		
		// Get the current time in ms.
		initialTime = getTimeInMsec();
		
		try {
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			/* 
			 * Execution::
			 * 1. Select Baseline Library from the Main Menu.
			 * 2. Click on All Link and wait for inventory to complete
			 * 		- Verify:
			 * 		- Add Baselines Button
			 * 		- Actions drop-down and options present in it
			 * 3. Select the Nodes from the Main Menu.
			 * 4. Verify
			 * 		- Add Nodes Button 
			 * 		- Message: localhost has an Ready to start inventory
			 * 		- Inventory Link Presence
			 * 		- Actions drop-down and options present in it
			 * 5. Click on Inventory Link from message.(or Actions->Message)
			 * 6. On Inventory window, verify:
			 * 		- Heading, Buttons
			 * 		- Select Baseline
			 * 7. Click Inventory Button
			 * 		- Make sure Inventory window closes.
			 * 8. Wait till Nodes Inventory completes 
			 * 		- Message: Ready for Deployment
			 * 		- Message: localhost has an User Action Need
			 * 		- Link: Deploy
			 * 9. Click the Deploy link. (or Actions->Deploy)
			 * 10. On Deploy Screen, verify
			 * 		- Name
			 * 		- Buttons present
			 * 		- 
			 * 11. Click Analysis button.
			 * 		- Verify Analysis complete
			 * 12. Click Deploy button
			 * 		- Verify Deploy window closes
			 * 13. Wait for Deployment to complete
			 * 		- Message: Install Done
			 * 14. Verify deployment complete and Return the table contents.
			 */
			
			// 2. Do Baseline inventory
			if(!BaselineLibrary.performBaselineInventory()) {
				throw new TestException("Test Failed");
			}
			
			// 3. Select the Nodes from the Main Menu.
			if(!CommonHpsum.clickLinkOnMainMenu("MainMenu_Nodes")) {
				throw new TestException("Test Failed");
			}
			
			captureScreenShot();
			
			// Verify Page: Nodes
			if(!SelUtil.verifyPage("Nodes")) {
				throw new TestException("Test Failed");
			}
			
			/* 4. Verify
			 * 		- Add Nodes Button 
			 * 		- Message: localhost has an Ready to start inventory
			 * 		- Inventory Link Presence
			 * 		- Actions drop-down and options present in it
			 */
			
			// Verify button exists: Add Baselines
			if(!SelUtil.verifyButtonStatus("Nodes_AddNodeButton", "enabled")) {
				printError("verifyButtonStatus Nodes_AddNodeButton failed.");
				fail = true;
			}
			
			sleep(5);
			
			// 4. Do Node inventory
			if(!Nodes.performNodeInventory()) {
				throw new TestException("Test Failed");
			}
			
			// 5. Deploy Node.
			//if(!Nodes.deployNode("localhost", "local")) {
			//	throw new TestException("Test Failed");
			//}
			// Deploy Node in easy mode
			//if(!Nodes.deployNodeEasyMode("localhost", "local")) {
			//	throw new TestException("Test Failed");
			//	}
			// Deploy Node in Advanced mode
			if(!Nodes.deployNodeAdvMode("localhost", "local")) {
				throw new TestException("Test Failed");
				}
			
			
			// 6. View Logs
			if(!Nodes.viewLogsAfterDeploy("localhost", "localhost")) {
				throw new TestException("Test Failed");
			}
		}
		catch (Throwable t) {
			fail = true;
			test_result = "FAIL";
			ErrorUtil.addVerificationFailure(t);
		}
		finally {
			try {
				captureScreenShot();
				
				// Get current time in ms.
				finalTime = getTimeInMsec();
				
				// Calculate test time interval.
				executionTime = calculateTimeInterval(initialTime, finalTime);
				
				// Write the final test result in html file.
				CommonHpsum.testCleanupAndReporting(test_name, test_result, executionTime);
			}
			catch (IOException e) {
				printError("Exception occurred while taking screen-shot.");
				e.printStackTrace();
			}
			printLogs("DONE.");
		}
	}
	
	@AfterMethod
	public void testParamCleanup() {
		printLogs("Test Param Cleanup:");
		
		// Print the global list.
		for(int i = 0; i < testList.size(); i++) {  
			printLogs(testList.get(i));
		}
		

		if(skip) {
			TestUtil.reportDataSetResult(suite_xl, test_name, count+2, "SKIP");
		}
		else if(fail) {
			isTestPass = false;
			TestUtil.reportDataSetResult(suite_xl, test_name, count+2, "FAIL");
		}
		else {
			TestUtil.reportDataSetResult(suite_xl, test_name, count+2, "PASS");
		}
		skip = false;
		fail = false;
		testParamEnd();
	}
	
	@AfterTest
	public void testCleanup() {
		printLogs("Test Cleanup:");
		if(isTestPass) {
			TestUtil.reportDataSetResult(suite_xl, "TestCases", TestUtil.getRowNum(suite_xl, test_name), "PASS");
		}
		else {
			TestUtil.reportDataSetResult(suite_xl, "TestCases", TestUtil.getRowNum(suite_xl, test_name), "FAIL");
		}
		
		printTestEndLine(test_name);
	}
	
	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(suite_xl, test_name);
	}
}
