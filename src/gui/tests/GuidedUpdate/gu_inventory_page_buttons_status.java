package gui.tests.GuidedUpdate;

import java.util.HashMap;

import gui.common.base.CommonHpsum;
import gui.common.base.GuidedUpdate;
import gui.common.base.GuidedUpdate.GU_Mode;
import gui.common.pages.GuidedUpdatePage;
import gui.common.record.Record;

import org.testng.TestException;
import org.testng.annotations.Test;

//
//Assertion: 
//<Write the purpose of the test case>
//

public class gu_inventory_page_buttons_status extends TestSuiteBase {	
	// Common Test Variables.
	int iTimeout = 240;	
		
	String test_name = this.getClass().getSimpleName();
	String test_result = "PASS";
	String sInventoryStatus = "";
		
	Record R = new Record();
	HashMap<String, Object> H = new HashMap<String, Object>();
	GuidedUpdatePage GuidedUpdatePg = null;			
	
	@Test
	public void test_gu_inventory_page_buttons_status() {
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
			//
			//1. Open HPSUM.
			if (!CommonHpsum.performTestSetup()) {
				throw new TestException("Failed to perform setup");
			}
			
			//2. Perform Inventory Update
			// Handle the Automatic or interactive popup.
			if(!GuidedUpdate.handleGUModeAndBaseline(GU_Mode.GU_INTERACTIVE, null, null)){
				throw new TestException("Test Setup Failed");
			}
			
			// Verify button status when inventory in progress, for baseline.
			GuidedUpdatePg = GuidedUpdatePage.getInstance();
			sInventoryStatus = GuidedUpdate.getInventoryStatus(1);
			
			// If inventory is in progress then non of the button should be enabled.
			if(sInventoryStatus.contains("progress") || sInventoryStatus.contains("added")){
				sleep(5);				
				if(!GuidedUpdatePg.GUStep1Next.isDisabled()){
					throw new TestException("The Next button is not disabled.");
				}
				if(!GuidedUpdatePg.GUInventoryReboot.isDisabled()){
					throw new TestException("The Reboot button is not disabled.");
				}
				if(!GuidedUpdatePg.GUInventoryAbort.isDisabled()){
					throw new TestException("The Abort button is not disabled.");
				}
				if(!GuidedUpdatePg.GUInventoryStartOver.isDisabled()){
					throw new TestException("The StartOVer button is not disabled.");
				}
			}
				
			// If inventory for baseline is complete, then Abort button must be enabled.
			sleep(10);
			sInventoryStatus = GuidedUpdate.getInventoryStatus(1);
			if(sInventoryStatus.contains("completed")){		
				
				if(!GuidedUpdatePg.GUInventoryAbort.isEnabled()){
					throw new TestException("The Abort button is not enabled.");
				}
				if(!GuidedUpdatePg.GUInventoryStartOver.isDisabled()){
					throw new TestException("The StartOVer button is not disabled.");
				}
				if(!GuidedUpdatePg.GUStep1Next.isDisabled()){
					throw new TestException("The Next button is not disabled.");
				}
				if(!GuidedUpdatePg.GUInventoryReboot.isDisabled()){
					throw new TestException("The Reboot button is not disabled.");
				}
			} 
			
			// Verify button status when inventory in progress, for localhost.
			sInventoryStatus = GuidedUpdate.getInventoryStatus(2);
			if(sInventoryStatus.contains("progress")){
				if(!GuidedUpdatePg.GUInventoryAbort.isEnabled()){
					throw new TestException("The Abort button is not enabled.");
				}
				if(!GuidedUpdatePg.GUInventoryStartOver.isDisabled()){
					throw new TestException("The StartOVer button is not disabled.");
				}
				if(!GuidedUpdatePg.GUStep1Next.isDisabled()){
					throw new TestException("The Next button is not disabled.");
				}
				if(!GuidedUpdatePg.GUInventoryReboot.isDisabled()){
					throw new TestException("The Reboot button is not disabled.");
				}
			}
					
			// Wait for inventory to happen.
			if(!GuidedUpdate.guidedUpdateWaitForInventory(iTimeout)){
					throw new TestException("Test Setup Failed");
			}
			
			// 
			// Execution::
			//
			// None.
			
			//
			// Verification:
			//
			//1. Verify StartOver is enabled.
			if(!GuidedUpdatePg.GUInventoryStartOver.isEnabled()){
				throw new TestException("The Startover button is not enabled.");
			}
			
			//2. Verify Abort button is disabled.
			if(!GuidedUpdatePg.GUInventoryAbort.isDisabled()){
				throw new TestException("The Abort button is not disabled.");
			}
			
			//3. Verify Next button is enabled.
			if(!GuidedUpdatePg.GUStep1Next.isEnabled()){
				throw new TestException("The Next button is not enabled.");
			}
			
			//4. Verify Reboot button is disabled.
			if(!GuidedUpdatePg.GUInventoryReboot.isDisabled()){
				throw new TestException("The Next button is not enabled.");
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