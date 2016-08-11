package gui.tests.GuidedUpdate;

import java.util.HashMap;

import gui.common.base.CommonHpsum;
import gui.common.base.GuidedUpdate;
import gui.common.base.GuidedUpdate.GU_Mode;
import gui.common.pages.GuidedUpdatePage;
import gui.common.pages.PageCommon;
import gui.common.pages.PageCommon.Pages;
import gui.common.record.Record;

import org.testng.TestException;
import org.testng.annotations.Test;

//
//Assertion: 
//<Write the purpose of the test case>
//

public class gu_inventory_startover extends TestSuiteBase {	
	// Common Test Variables.
	int iTimeout = 240;	
	
	String test_name = this.getClass().getSimpleName();
	String test_result = "PASS";
	
	Record R = new Record();
	HashMap<String, Object> H = new HashMap<String, Object>();
	GuidedUpdatePage GuidedUpdatePg = null;	
	
	@Test
	public void test_gu_inventory_startover() {				
		screenshot_name 	= test_name;
		screenshot_counter 	= 0;
		
		try {
			//
			// Environment:
			//	
			printLogs("Running test with arguments:" + H);
			H.put("test_name", test_name);
			H.put("test_result", "PASS");			
			H.put("manual", false);		// False when test is automated
			H.put("skip", false);
			H.put("fail", false);			
			H.put("executionTime","");
			H.put("suite_xl", suite_xl);
			printLogs("Successfully set arguments:" + H);
			
			R.setRecord(H, "Test");			
			if(!performTestEnv()){
				throw new TestException("Test setup Failed");
			}
			
			//
			// Setup:
			//
			//1. Open HPSUM.
			if (!CommonHpsum.performTestSetup()) {
				throw new TestException("Test setup Failed");
			}
			
			//2. Perform Inventory Update			
			
			// Handle the Automatic or interactive popup.
			if(!GuidedUpdate.handleGUModeAndBaseline(GU_Mode.GU_INTERACTIVE, 
													 null, 
													 null)){
				
				
				throw new TestException("Test Setup Failed");
			}

			// 3. Wait for inventory to happen.
			if(!GuidedUpdate.guidedUpdateWaitForInventory(iTimeout)){
				throw new TestException("Test Setup Failed");
			}
			
			// 
			// Execution::
			//			
			// Click on Start Over. 
			GuidedUpdatePg = GuidedUpdatePage.getInstance();
			if(!GuidedUpdatePg.ActionDropDown.WaitForElement(iTimeout)){
				throw new TestException("Action drop down did not appear.");
			}
			
			if(!GuidedUpdatePg.GUInventoryStartOver.Click()){
				throw new TestException("Test Execution Failed");
			}
			
			//
			// Verification:
			//
			
			// Wait for the Localhost Guided Update dialog to come up.
			if(!PageCommon.WaitForPage(Pages.LocalhostGuidedUpdateDialog, 
									   iTimeout)){
				throw new TestException("Test Verification Failed");
			}
			
			captureScreenShot();
			printLogs("DONE.");
		}
		catch (Throwable t) {
			gatherTestResult();
		}
		finally {
			try {
				//
				// Cleanup:
				//
				performCleanup();
			}
			catch (Throwable t) {
				printError("Exception occurred in finally.");
			}
		}	
	}
}