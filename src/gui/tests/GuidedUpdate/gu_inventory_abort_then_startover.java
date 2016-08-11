package gui.tests.GuidedUpdate;

import java.util.HashMap;

import gui.common.base.GuidedUpdate;
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

public class gu_inventory_abort_then_startover extends TestSuiteBase {	
	// Common Test Variables.
		int iTimeout = 240;	
		
		String test_name = this.getClass().getSimpleName();
		String test_result = "PASS";
		
		Record R = new Record();
		HashMap<String, Object> H = new HashMap<String, Object>();
		GuidedUpdatePage GuidedUpdatePg = null;	
	@Test
	public void test_gu_inventory_abort_then_startover() {
		screenshot_name 	= test_name;
		screenshot_counter 	= 0;
		
		try {
			//
			// Environment:
			//
			printLogs("Running test with arguments:" + H);
			H.put("test_name", test_name);
			H.put("test_result", "PASS");			
			H.put("manual", false);		// Make this false when test is automated
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
			// 1. Perform Guided Update Inventory Abort
			if(!GuidedUpdate.performInventoryAbort()){
				throw new TestException("Setup Failed.");
			}
			
			//
			// Execution
			// Click on StartOver.
			if(!GuidedUpdatePg.GUStartOver.Click()){
				throw new TestException("Execution Failed.");
			}
			
			
			//
			// Verification		
			// Wait for the Localhost Guided Update dialog to come up.
			if(!PageCommon.WaitForPage(Pages.LocalhostGuidedUpdateDialog, iTimeout)){
				throw new TestException("Verification Failed");
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