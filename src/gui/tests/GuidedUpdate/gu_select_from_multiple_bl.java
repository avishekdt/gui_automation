package gui.tests.GuidedUpdate;

import java.util.Collection;
import java.util.HashMap;















import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.GuidedUpdate;
import gui.common.pages.GuidedUpdatePage;
import gui.common.pages.PageCommon;
import gui.common.pages.PageCommon.Pages;
import gui.common.pages.elements.Table;
import gui.common.record.Record;

import org.testng.TestException;
import org.testng.annotations.Test;

//
//Assertion: 
//<Write the purpose of the test case>
//

public class gu_select_from_multiple_bl extends TestSuiteBase {	
	// Common Test Variables.
	int iTimeout = 240;	
	int iIndex = 0;
	
	boolean bFound = false;
	
	String test_name = this.getClass().getSimpleName();
	String test_result = "PASS";
	String sValue = "";
	
	Collection<String> cValues;
	
	Record R = new Record();
	
	HashMap<String, Object> H = new HashMap<String, Object>();
	
	GuidedUpdatePage GuidedUpdatePg = null;	
	
	@Test
	public void test_gu_select_from_multiple_bl() {				
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
			
			// 2. Add 2 baseline.
			if(!BaselineLibrary.cblApplyFilteriLO()){
				throw new TestException("Test setup Failed");
			}
			
			// 3. Go to Guided update dialog
			if(!CommonHpsum.clickLinkOnMainMenu("MainMenu_GuidedUpdate")) {
				throw new TestException("Test setup Failed");
					
			}
					
			// wait for Localhost Guided Update popup to appear
			//waitForDialogBox(GuidedUpdate_PopupDialog);
			if(!PageCommon.WaitForPage(Pages.LocalhostGuidedUpdateDialog, iTimeout)) {
				throw new TestException("Test setup Failed");
				
			}
			
			//Select interactive mode.
			GuidedUpdatePg = GuidedUpdatePage.getInstance();
			if(!GuidedUpdatePg.GUInteractive.Select()) {
				throw new TestException("Test setup Failed");					
			}
			
			//Select Assign baseline.
			if (!GuidedUpdatePg.AssignBaseline.Select()){
				throw new TestException("Test setup Failed");						
			}
			
			// 
			// Execution::
			//			
			// Select the Additional baseline.
			if(currentOsName.contains("windows")) {
				GuidedUpdatePg.CurrentAddBaseline.Select("C:/SPP/custom_exe");
			}
			else {
				GuidedUpdatePg.CurrentAddBaseline.Select("/root/Desktop/SPP/custom_rpm");
			}
			
			if(!GuidedUpdatePg.GUOK.Click()) {
				throw new TestException("Test Execution Failed");				
			}
			//
			// Verification:
			// 1. Wait for inventory to complete.
			if(!GuidedUpdate.guidedUpdateWaitForInventory(iTimeout)){
				throw new TestException("Test verification Failed");	
			}
			
			// 2. Click on Next.
			if(!GuidedUpdatePg.GUStep1Next.Click()){
				throw new Exception("Test verification Failed");
			}			
			captureScreenShot();
						
			// 3. On Review screen
			if(!PageCommon.WaitForPage(Pages.GuidedUpdateStep2, iTimeout)){
				throw new Exception("Test verification Failed");
			}
			captureScreenShot();
			
			// 4. Verify the table.
			cValues = GuidedUpdatePg.ReviewDeployTable.
											GetRowData("HPE Integrated Lights-Out 4");
			
			
			for(String sValue:cValues){
				if(sValue.contains("SelectedSelect")){
					printLogs("The component is selected.");					
					bFound = true;
					break;
				}
			}
			
			if(!bFound){
				iIndex = Table.iValueIndex;
				sValue = GuidedUpdatePg.ReviewDeployTable.GetXpathAtIndex(iIndex);
				if(!GuidedUpdatePage.ClickOnTableContent(sValue)){
					throw new TestException("Test Verification Failed");
				}
			}
			captureScreenShot();
			if(!GuidedUpdatePg.GUDeploy.Click()){
				throw new TestException("Test Verification Failed");
			}
			
			if(!GuidedUpdate.guidedUpdateWaitForDeploymentToComplete()){
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