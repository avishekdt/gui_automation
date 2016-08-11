package gui.tests.Baseline;

import java.util.ArrayList;

import gui.common.base.Activity;
import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.SelUtil;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

//
//Assertion: 
// Verify the new activities on the Activity screen after adding and deleting new BLs.
//

public class bl_add_delete_activity extends TestSuiteBase {	
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;			// Make this false when test is automated.
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime 	= "";
	
	@Test
	public void test_bl_add_delete_activity() {
		screenshot_name 	= test_name;
		screenshot_counter 	= 0;
		String ablPath_1 = "";
		String ablPath_2 = "";
		String ablPath_3 = "";
		activityScreenRows_counter = 0;
		
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
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Wait for all the Baseline inventory to complete.
			if(!BaselineLibrary.performBaselineInventory()){
				throw new TestException("Test failed");
			}
			
			// 3. Create three locations to be used for additional baselines locations
			ablPath_1 = createAblFolder(5, "exe");
			ablPath_2 = createAblFolder(5, "rpm");
			ablPath_3 = createAblFolder(5, "zip");
			
			// 3. Get the latest Activity screen rows. This will be used as base
			// the new rows will be identified based on this base activities.
			if(!Activity.printNewlyAddedActivityRows()) {
			    printError("Failed to print the base activity rows.");
			}
			
			String abl_name = "";
			ArrayList<String> latestActivityRows = new ArrayList<String>();
			
			// Add ABLs one by one and verify the activities
			for (int i = 1; i <= 3 ; i++) {
				latestActivityRows = null;
				
				// Additional baseline to use
				if (i == 1) {
					abl_name = ablPath_1;
				}
				if (i == 2) {
					abl_name = ablPath_2;
				}
				if (i == 3) {
					abl_name = ablPath_3;
				}
				
				// 
				//String ablFileName = getFileNameFromPath(abl_name);
				//String ablFileName = new File(abl_name).getName();
				
				printLogs("Verifying add data for ABL: " + abl_name);
				
				// Add the ABL
				if(!BaselineLibrary.addAdditionalBaseline(abl_name)) {
					throw new TestException("Test failed");
				}
				
				// Wait for Baseline Inventory to complete
				if(!BaselineLibrary.performBaselineInventory()) {
					throw new TestException("Test failed to performBaselineInventory");
				}
				
				// Get new activity rows
				latestActivityRows = Activity.getNewlyAddedActivityRows();
				
				if (latestActivityRows == null) {
					printError("Failed to get new activities after ABL addition.");
				}
				
				// Initialize values
				int newActivitiesCount = latestActivityRows.size();
				
				String fileName = getFileNameFromPath(abl_name);
				int expectedActivitiesCount = 2;
				int actualActivitiesCount = 0;
				
				if (newActivitiesCount == 0) {
					printLogs("Did not find the activities populated. Pressing the refresh button.");
					sleep(120);
					SelUtil.clickByXpath("ActivityPage_Refresh");
					sleep(10);
					captureScreenShot();
					// Get new activity rows
					latestActivityRows = Activity.getNewlyAddedActivityRows();
					newActivitiesCount = latestActivityRows.size();
				}
				
				// Two activities are expected after a new ABL is added.
				if (newActivitiesCount < expectedActivitiesCount) {
					printError(expectedActivitiesCount + ": activites were expected after ABL addition. But found : " + newActivitiesCount);
				}
				else {
					printLogs("Found " + newActivitiesCount + " new activities after adding ABL " + abl_name);
				}
				
				// Verify that the activity rows are as expected
				for (int j = 0 ; j < newActivitiesCount ; j++) {
					if (latestActivityRows.get(j).contains(fileName)) {
						actualActivitiesCount++;
						//printLogs("ABL name " + fileName + " not found in newly added activity: " + latestActivityRows.get(j));
					}
				}
				if (newActivitiesCount > 0) {
					if (actualActivitiesCount == 0) {
						printError(fileName + ": Not found in any of the " + newActivitiesCount + " new activities");
					}
					else if (actualActivitiesCount < expectedActivitiesCount) {
						printError(actualActivitiesCount + " :activities found for: " + fileName + ". Expected were: " + expectedActivitiesCount);
					}
					else {
						printLogs("Out of " + newActivitiesCount + " new activities. " + actualActivitiesCount + " were as expected.");
					}
				}
			}
			
			// 
			// Execution::
			//
			
			// Delete the three recently added ABLs and verify the Activities
			for (int i = 1; i <= 3 ; i++) {
				latestActivityRows = null;
				
				// Additional baseline to use
				if (i == 1) {
					abl_name = ablPath_1;
				}
				if (i == 2) {
					abl_name = ablPath_2;
				}
				if (i == 3) {
					abl_name = ablPath_3;
				}
				
				printLogs("Verifying delete data for ABL: " + abl_name);
				
				// Delete the ABL
				if(!BaselineLibrary.deleteAbl(abl_name)) {
					printError("Failed to delete the ABL: " + abl_name);;
				}
				
				// Wait for page to load
				sleep (5);
				
				// Get new activity rows
				latestActivityRows = Activity.getNewlyAddedActivityRows();
				
				if (latestActivityRows == null) {
					printError("Failed to get new activities after ABL deletion.");
				}
				
				// Initialize values
				int newActivitiesCount = latestActivityRows.size();
				String fileName = getFileNameFromPath(abl_name);
				int expectedActivitiesCount = 1;
				int actualActivitiesCount = 0;
				
				// One activity is expected after a new ABL is added.
				if (newActivitiesCount != expectedActivitiesCount) {
					printError(expectedActivitiesCount + " activity was expected after ABL deletion. But found : " + newActivitiesCount);
				}
				
				// Verify that the activity rows are as expected
				for (int j = 0 ; j < newActivitiesCount ; j++) {
					if (latestActivityRows.get(j).contains(fileName)) {
						if (!latestActivityRows.get(j).contains("Deleted")) {
							actualActivitiesCount++;
						}
					}
				}
				if (newActivitiesCount > 0) {
					if (actualActivitiesCount == 0) {
						printError(fileName + ": Not found in any of the " + newActivitiesCount + " new activities");
					}
					else if (actualActivitiesCount != expectedActivitiesCount) {
						printError(actualActivitiesCount + " :activities found for: " + fileName + ". Expected were: " + expectedActivitiesCount);
					}
					else {
						printError("Out of " + newActivitiesCount + " new activities. " + actualActivitiesCount + " was as expected.");
					}
				}
			}
			
			//
			// Verification:
			//
			printLogs("All the activities verified for Add and Delete of BLs");
			
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