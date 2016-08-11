package gui.common.base;

import java.util.ArrayList;
import java.util.HashMap;

import gui.common.util.ErrorUtil;

import org.openqa.selenium.By;

import gui.common.pages.*;
import gui.common.pages.PageCommon.Pages;
import gui.common.record.*;


public class GuidedUpdate extends SelUtil {
	
	/*
	 * METHODS
	 * handleGUModeAndBaseline
	 * guidedUpdateWaitForInventory
	 * getInventoryStatus
	 * performInventoryAbort
	 * guidedUpdateWaitForInventoryToComplete
	 * getComponentXPath
	 * checkDependency
	 * analyzeGu2TableContents
	 * analyzeViewGu3Table
	 * getGu2FailedDependenciesStatus
	 * guidedUpdateWaitForDeploymentToComplete
	 * guidedUpdateGetInstallProgressText
	 * guidedUpdateWaitForDeploymentToStart
	 * verifyAdvOptionsPage
	 * GuStep3ViewLogs
	 * resolveGu2FailedDependencies
	 */
	
	// Public variables.
	public enum GU_Mode {
		GU_AUTO,
		GU_INTERACTIVE
	}
	
	public static int maxWaitTime = 240;	// This is multiplied by 5 as there is sleep(5). So actual time = 1200s = 20m	
	public static int compNumber = 0;
		
	public static Record aRecord = new Record();
	
	// Methods related to screen : GU Common
	
	// Wrapper Method to complete Guided update
	// This for tests that verifies GUI after GU deploy is complete.
	public static boolean performGuidedUpdate(){
		printLogs("Calling performGuidedUpdate");
		
		GuidedUpdatePage GUPAge = GuidedUpdatePage.getInstance();	
		
		try
		{
			if(!handleGUModeAndBaseline(GU_Mode.GU_INTERACTIVE, null, null)){
				throw new Exception("Exception in handleGUModeAndBaseline");
			}
			captureScreenShot();
	
			// 3. Wait for inventory to happen.
			if(!guidedUpdateWaitForInventory(maxWaitTime)){
				throw new Exception("Exception to guidedUpdateWaitForInventory");
			}
			sleep(5);
			captureScreenShot();
			
			// 4. Click on Next.
			if(!GUPAge.GUStep1Next.Click()){
				throw new Exception("Failed to Click on Next");
			}			
			captureScreenShot();
			
			// 5. On Review screen
			if(!PageCommon.WaitForPage(Pages.GuidedUpdateStep2, maxWaitTime)){
				throw new Exception("Failed to Wait for page");
			}
			captureScreenShot();
			
			// 6. Analyze the table content and Click on Deploy.
			if(!getComponentXPath()){
				throw new Exception("Exception in getComponentXPath");
			}	
			
			// Select the components.
			if(compNumber == 0){
				compNumber = 2;
			}
			if(!selectComponents(compNumber, false)){
				throw new Exception("Failed to select components.");
			}
			captureScreenShot();
			
			// Analyze Failed dependency
			if(!checkDependency()){
				throw new Exception("Failed to resolve dependency.");
			}
			
			captureScreenShot();
			
			// Verify button status
			if(!verifyButtonStatus("GuidedUpdate_Step2BackButton", "enabled")) {
				throw new Exception("Back button not enabled.");
			}
			if(!verifyButtonStatus("GuidedUpdate_Step2ResetButton", "enabled")) {
				throw new Exception("Reset button not enabled.");
			}
			
			// Wait for deployment to complete.			
			if(!guidedUpdateWaitForDeploymentToComplete("GuidedUpdate_Step3DeploymentTable")){
				throw new Exception("Exception in guidedUpdateWaitForDeploymentToComplete");
			}
			captureScreenShot();
		}
		catch(Throwable t) {					
			printError(t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
		printLogs("Successfully performed Localhost Guided Update: 'Interactive'.");
		printFunctionReturn(fn_pass);
		return true;
	}
	
	public static boolean guidedUpdateWaitForDeploymentToComplete(){
		return guidedUpdateWaitForDeploymentToComplete("GuidedUpdate_Step3DeploymentTable");
	}
	// Method related to screen: LocalhostGuidedUpdateDialog
	public static boolean handleGUModeAndBaseline(GU_Mode eMode, String sBaselinePath, String sAddBaseline){
		printLogs("Calling handleGUModeAndBaseline with arguments: \n" +		 
					"\teMode: "+ eMode +",\n\tsBaseline: " + sBaselinePath + ",\n\tsAddBaseline: " + sAddBaseline);
		
		GuidedUpdatePage GUPage = null;
		try
		{
			// 1. Select Localhost Guided Update from the Main Menu.
			if(!CommonHpsum.clickLinkOnMainMenu("MainMenu_GuidedUpdate")) {
				throw new Exception("Failed to select Guided Update from Main Menu");
					
				}
						
			int iTimeout = 240;
			// wait for Localhost Guided Update popup to appear
			//waitForDialogBox(GuidedUpdate_PopupDialog);
			
			if(!PageCommon.WaitForPage(Pages.LocalhostGuidedUpdateDialog, iTimeout)) {
				throw new Exception("Failed to wait for page Guided Update");
				
			}
						
			// 2. Select Interactive and Click OK
			// Select Interactive
			GUPage = GuidedUpdatePage.getInstance();
			if (eMode == GU_Mode.GU_INTERACTIVE){	
				printLogs("Selecting the radio button Interactive");
				if(!GUPage.GUInteractive.Select()) {
					throw new Exception("Failed to Select the Radio Button: Interactive");					
				}
			} else {
				if(!GUPage.GUAutomatic.Select()) {
					throw new Exception("Failed to Select the Radio Button: Automatic");					
				}
			}
			
			//3. Select the baseline
			if (sBaselinePath != null || sAddBaseline != null){
				// Verify if the current baseline matches
				if(GUPage.CurrentBaseline.IsEmpty()){
					// Select Assign different Baseline check box
					if (!GUPage.AssignBaseline.Select()){
						throw new Exception("Failed to check Assign Baseline");						
					}
					
					if (sBaselinePath != null) {
						// Select baseline from the search drop down.
						if(!GUPage.SearchBaseline.Select(sBaselinePath)){
							throw new Exception("Failed to Select Baseline");						
						}
					}
					
					if (sAddBaseline != null){
						// Select Additional baseline from the search drop down
						if(!GUPage.SearchAddBaseline.Select(sAddBaseline)){
							throw new Exception("Failed to Select Additional Baseline");						
						}
					}				
				}			
			}
			// Wait for button to be enabled.
			sleep(5);
			
			// Click OK Button				
			if(!GUPage.GUOK.Click()) {
				throw new Exception("Failed to Click on OK Button");				
			}
						
			// Wait for Localhost Guided Update popup to disappear
			if(!waitForNoElementByXpath("GuidedUpdate_PopupDialog")) {
				throw new Exception("Failed to wait for the guided update dialog to close.");				
			}		
		
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while handling the Guided Update Dialog.");
			printLogs("With Exception: " + t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	// 
	public static boolean performInventoryAbort(){
		int iTimeout = 240;
		int i = 0;
		
		String sProgressText = "";
		
		boolean bStatus = false;
		boolean bFound = false;
		boolean bFail = false;
		
		GuidedUpdatePage GuidedUpdatePg = GuidedUpdatePage.getInstance();
		printLogs("Calling performInventoryAbort");
		try {
			// Start hpsum
			if (!CommonHpsum.performTestSetup()) {
				throw new Exception("Failed to start Smart Update Manager");
			}
	
			// Handle the Automatic or interactive popup.
			if(!GuidedUpdate.handleGUModeAndBaseline(GU_Mode.GU_INTERACTIVE, null, null)){
				throw new Exception("Failed to handle Guided Update Dailog");
			}
			
			// Wait for Step 1 screen
			if(!PageCommon.WaitForPage(Pages.GuidedUpdateStep1, iTimeout)){
				throw new Exception("Failed wait for Guided Update page: Step 1.");
			}
			
			// Wait for Abort button to be enabled.
			while(!bStatus){
				bStatus = GuidedUpdatePg.GUInventoryAbort.isEnabled();
				sleep(5);
				i++;							
				if(i > 160){
					throw new Exception("Failed to wait for Abort button to be enabled");
				}			
			}			
				
			// Click on Abort button.
			if(!GuidedUpdatePg.GUInventoryAbort.Click()){
				throw new Exception("Failed to click on Abort");
			}
			
			// Verify Abort progress message.
			while(bFound) {
				// Wait for Abort to start				
				sProgressText = GuidedUpdate.getInventoryStatus(2);			
				
				// 2. Wait for Abort to pass. 
				if (sProgressText.matches("Node inventory aborted.*")) {
					printLogs("Node Deploy aborted successfully");
					bFound = true;
					break;
				}
				sleep(5);
				i++;
				if(i > 240){
					printError("Timeout occured and the expected status did not reach.");
					break;
				}
			}			
	
			if (!bFound) {
				printError("Expected Text: Node inventory aborted");
				printLogs("Actual: " + sProgressText);
				bFail = true;
				throw new Exception("Failed to verify Abort.");
			}
	
			// 3. Verify that "Aborted by user" is displayed.
			if (!GuidedUpdatePg.InventoryStatus.Exists()) {
				printError("Expected: 'Abort by user' Text to be displayed");
				printLogs("Found none.");
				bFail = true;
	
			} else {
				sProgressText = GuidedUpdatePg.InventoryStatus.GetText();
				if (!sProgressText.matches("Aborted by user.*")) {
					printError("'Aborted by user' Text is not displayed. Should have been.");
					bFail = true;
					
				} else {
					printLogs("'Aborted by user' Text was displayed as expected.");
				}
			}
			
			if (bFail) {
				throw new Exception("Error occured in Function");
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while handling the Guided Update Dialog");
			printLogs("With Exception: " + t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	// Methods related to screen : GU Step-1
	public static boolean guidedUpdateWaitForInventory(int iTimeout){
		printLogs("Calling guidedUpdateWaitForInventory");
		maxWaitTime = iTimeout;
		
		int iIndex = 0;
		try
		{
			
			if(!guidedUpdateWaitForInventoryToComplete("GuidedUpdate_BlTable")) {
				printError("This table must have atleast 1 row.");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			if(!guidedUpdateWaitForInventoryToComplete("GuidedUpdate_NodeTable")) {
				printError("This table must have atleast 1 row.");
				printFunctionReturn(fn_fail);
				return false;
			}	
			printLogs("Waiting for 'Next' button to be enabled");
    		// Wait for Next button to be enabled.
    		while(!SelUtil.verifyButtonStatus("GuidedUpdate_Step1NextButton", "enabled")){
    			iIndex++;
    			if(iIndex > 100){
    				printError("ERROR: Failed to wait for 'Next' button to be 'enabled'.");
		    		break; 
    			}
    			sleep(2);    		
    		}
    		iIndex = 0;
    		while(!SelUtil.verifyButtonStatus("GuidedUpdate_Step1ResetButton", "enabled")){
    			iIndex++;
    			if(iIndex > 100){
    				printError("ERROR: Failed to wait for 'StartOver' button to be 'enabled'.");
		    		break; 
    			}
    			sleep(2);    		
    		}
    		
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while waiting for in inventory to complete.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}

	public static String getInventoryStatus(int iTableIndex){
		String[] xPathKeyOfTable = {"GuidedUpdate_BlTable", "GuidedUpdate_NodeTable"};
		printLogs("Calling getInventoryStatus of table: " + iTableIndex);		
		
		try {
			
			printLogs("Waiting for inventory to complete in table: " + xPathKeyOfTable[iTableIndex - 1]);
			
			int rowCount = getTableRowCount(xPathKeyOfTable[iTableIndex - 1]);
			
			if(rowCount == -1) {
				appendHtmlComment("Failed to get the component during the Inventory");
				printFunctionReturn(fn_fail);
				return null;
			}
			if(rowCount == 0) {
				printError("No rows found in table: " + xPathKeyOfTable[iTableIndex - 1]);
				printError("This table must have atleast 1 row.");
				printFunctionReturn(fn_fail);
				return null;
			}
			else {
				printLogs(xPathKeyOfTable[iTableIndex - 1] + " row count = " + rowCount);
			}
			
			// wait for inventory to complete
			String xPathOfTable = OR.getProperty(xPathKeyOfTable[iTableIndex - 1]);
			String xPathOfRow = "";
			String xPathOfName = "";
			String xPathOfProgress = "";
					
			String xPathOfRowPart1 = xPathOfTable + "/tr";
			String xPathOfRowPart2 = "";
			String xPathPartOfName = "/td[2]";
			String xPathPartOfProgress = "/td[3]";			
			
			String itemName = "";
			String inventoryStatus = "";		
			
			
			
			for(int i = 1 ; i <= rowCount ; i++) {
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow 			=  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfName 		= xPathOfRow + xPathPartOfName;
				xPathOfProgress 	=  xPathOfRow + xPathPartOfProgress;				
				
				itemName = getTextByActualXpath(xPathOfName);
				printLogs("Name for row : " + i + " : " + itemName);
								
				inventoryStatus = getTextByActualXpath(xPathOfProgress);		    		 
  		
		    	if (!inventoryStatus.isEmpty()){
					printLogs("Status now for row : " + i + " : " + inventoryStatus);
					break;
				}	
			} 		    		
			printFunctionReturn(fn_pass);
			return inventoryStatus;
	}		
	catch(Throwable t) {
		ErrorUtil.addVerificationFailure(t);			
		printError("Exception occurred while waiting for inventory to complete.");
		printFunctionReturn(fn_fail);
		return null;
	}		
	}
	// guidedUpdateWaitForInventoryToComplete
	public static boolean guidedUpdateWaitForInventoryToComplete(String xPathKeyOfTable) {
		printLogs("Calling guidedUpdateWaitForInventoryToComplete with values: " + xPathKeyOfTable);		
			
		try {
			printLogs("Waiting for inventory to complete in table: " + xPathKeyOfTable);
			
			int rowCount = getTableRowCount(xPathKeyOfTable);
			
			if(rowCount == -1) {
				appendHtmlComment("Failed to get the component during the Inventory");
				printFunctionReturn(fn_fail);
				return false;
			}
			if(rowCount == 0) {
				printError("No rows found in table: " + xPathKeyOfTable);
				printError("This table must have atleast 1 row.");
				printFunctionReturn(fn_fail);
				return false;
			}
			else {
				printLogs(xPathKeyOfTable + " row count = " + rowCount);
			}
			
			// wait for inventory to complete
			String xPathOfTable = OR.getProperty(xPathKeyOfTable);
			String xPathOfRow = "";
			String xPathOfName = "";
			String xPathOfProgress = "";
			String xPathOfFinalStatus = "";
			
			String xPathOfRowPart1 = xPathOfTable + "/tr";
			String xPathOfRowPart2 = "";
			String xPathPartOfName = "/td[2]";
			String xPathPartOfProgress = "/td[3]";
			String xPathPartOfFinalStatus = "/td[5]";
			
			String itemName = "";
			String inventoryStatus = "";
			String inventoryStatusOld = "Fresh";
			String finalStatus = "";
			String inventoryCompleteMsg = "Inventory completed";
			
			int timeout = 0;
			boolean bComplete = false;
			
			for(int i = 1 ; i <= rowCount ; i++) {
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow 			=  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfName 		= xPathOfRow + xPathPartOfName;
				xPathOfProgress 	=  xPathOfRow + xPathPartOfProgress;
				xPathOfFinalStatus	=  xPathOfRow + xPathPartOfFinalStatus;
				
				itemName = getTextByActualXpath(xPathOfName);
				printLogs("Name for row : " + i + " : " + itemName);
				startWatch();
				
				int statusCount = 0;
				
				while(true) {
					bComplete = false;
		    		inventoryStatus = getTextByActualXpath(xPathOfProgress);		    		
		    		
		    		//if(!inventoryStatusOld.equals(inventoryStatus)) {
		    		if(!inventoryStatus.contains(inventoryStatusOld)) {
		    			statusCount = 0;
		    		}
		    		else {
		    			statusCount++;
		    		}
		    		
		    		if(statusCount == 0) {
		    			printLogs("Status now for row : " + i + " : " + inventoryStatus);
		    			
		    			if(!inventoryStatus.contains(inventoryCompleteMsg)) {
		    				printLogs("Any change in the status will be printed. Please wait...");
		    			}
		    			else {
		    				printLogs("Inventory done for row : " + i + " : " + inventoryCompleteMsg);
			    			bComplete = true;
			    			break;
		    			}
		    			
			    		inventoryStatusOld = inventoryStatus;
		    		}
		    		
		    		
		    		//if(inventoryStatus.contains(inventoryCompleteMsg)) {
		    			//printLogs("Inventory done for row : " + i + " : " + inventoryCompleteMsg);
		    			//bComplete = true;
		    			//break;
		    		//}
		    		//else 
		    		if(timeout > maxWaitTime) {
		    			printError("ERROR: Timeout occurred which waiting for inventory to complete");
		    			break; 
		    		}
		    		sleep(5);
		    		timeout++;
			    }
				stopWatch();
				
				// If inventory completed successfully - Get the component count.
				if(bComplete) {
					finalStatus = getTextByActualXpath(xPathOfFinalStatus);
		    		printLogs("Final Status for row : " + i + " : " + finalStatus);		    		
		    	}
			}			
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while waiting for inventory to complete.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
		
	
	// Methods related to screen : GU Step-2
	
	// Get the component XPaths
	public static boolean getComponentXPath(){		
		printLogs("Calling getComponentXPath");
		String[] rowColXPath;
		String[] rowData;
		String colData;	
		
		HashMap<String, Object> hMapGU = new HashMap<>();
		
		try {
		String xPathKeyOfGu2Table = "GuidedUpdate_Step2ComponentsTable";
		String xPathOfTable = OR.getProperty(xPathKeyOfGu2Table);
		
		
		
		printLogs("xPathKeyOfGu2Table of the Table: "+ xPathKeyOfGu2Table);
		printLogs("xPath of the Table: "+ xPathOfTable);
		
		int rowCount = getTableRowCount(xPathKeyOfGu2Table);
		if(rowCount == -1) {
			printFunctionReturn(fn_fail);
			throw new Exception("getTableRowCount Failed");
		}
		if(rowCount == 0) {
			printError("No rows found in table: " + xPathKeyOfGu2Table);
			printFunctionReturn(fn_fail);
			throw new Exception("No rows found");
		}
		
		int colCount = getTableColumnCount(xPathKeyOfGu2Table);
		if(colCount == -1) {
			printFunctionReturn(fn_fail);
			throw new Exception("getTableColumnCount Failed.");
		}
		if(colCount == 0) {
			printError("No cols found in table: " + xPathKeyOfGu2Table);
			printFunctionReturn(fn_fail);
			throw new Exception("Column count is 0. Invalid Count.");
		}
		
		String xPathOfRow = "";
		String xPathOfRowPart1 = xPathOfTable + "/tr";
		String xPathOfRowPart2 = "";
		
		String xPathOfCol = "";
		String xPathOfColPart1 = "";
		String xPathOfColPart2 = "";		
		
		rowColXPath =  new String[rowCount];
		rowData = new String[rowCount];
		
		printLogs("Table Contents::");
		
		// Get the the xPaths for all the components.
		
			for(int i = 1 ; i <= rowCount ; i++) {	
				rowData[i-1] = "";
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow =  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfColPart1 = xPathOfRow + "/td";
				
				for(int j = 1 ; j <= colCount ; j++) {		
					colData = "";
					if (colCount == 1) {
						xPathOfColPart2 = "";
					}
					else {
							xPathOfColPart2 = "[" + j + "]";
					}
					
					xPathOfCol =  xPathOfColPart1 + xPathOfColPart2;	
					rowColXPath[i-1] = xPathOfCol;	
					if (j == 1) {
						xPathOfCol = xPathOfCol + "/a";
						//class="hp-toggle hp-checked"
						//class="hp-toggle"
						if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-toggle hp-checked")) {
							colData = "Checked";							
						}
						else {
							colData = "Unchecked";								
						}						
						colData = colData + "-" + getTextByActualXpath(xPathOfCol);
					}					
					else if (j == 3) {
						xPathOfCol = xPathOfCol + "/div/div";
						if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-error")) {
							colData = "ERROR";
						}
						else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-ok")) {
							colData = "OK";
						}
						else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-disabled")) {
							colData = "DISABLED";
						}
						else {
							colData = "NONE";
						}
					}
					else {
						colData = getTextByActualXpath(xPathOfCol);
					}
					//System.out.println(colData);
					rowData[i-1] = rowData[i-1] + " ==> " + colData;
				}
				printLogs(rowData[i-1]);			
				
			}
			
			hMapGU.put("saXPath",rowColXPath);
			hMapGU.put("iRowCount",rowCount);
			hMapGU.put("iColCount",colCount);
			hMapGU.put("sRowData",rowData);
			aRecord.setRecord(hMapGU, "GU");
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred in getComponentXPath with message: " + t.getMessage());
			printFunctionReturn(fn_fail);			
			return false;
		}		
	}
	
	//Select provided number of components.
	public static boolean selectComponents(int select, boolean bReverse){
		printLogs("Calling selectComponents with argument: select: '" + select + "' and bReverse: '" +bReverse +"'");
		try{
			HashMap<String, Object> hMapGU = Record.getRecord("GU");
			String[] aCompPath = (String[]) hMapGU.get("saXPath");
			String[] sRowData = (String[]) hMapGU.get("sRowData");
			String[] sChecked;			
			String rowData;
			String sXPath;
			
			int repeat = 0;			
			int i = 1;
			int limit = (int) hMapGU.get("iRowCount");		
			int iselected = 0;
			int selectCount = 0;
			
			if(compNumber == 0){
				selectCount = limit;
			}
			
			// If bReverse is set then start from 'select'. Here the variable select will act as the starting index.
			if(bReverse){
				i = select;				
			}
			
			// Select components required.
			for( ; i <= limit ; i++) {
				rowData = sRowData[i-1];				
				sXPath = aCompPath[i-1];
				//Trim this string: ==> Checked-SelectedSelect ==> and get the checked and unchecked data.
				sChecked = rowData.split("==>", 2);
				for (String svalue:sChecked){
					if (svalue.contains("Checked-")){
						repeat = 2;
						break;
					} 
					if(svalue.contains("Unchecked-")){
						repeat = 1;
						break; 
					}						
				}				
				
				printLogs("Selecting: " + rowData);
				if (iselected < selectCount){
					for(int z = 1; z <= repeat ; z++){
						driver.findElement(By.xpath(sXPath)).click();								
						sleep(1);
					}
					// Increment number of selected items.
					iselected++;
				}
				else{
					//If required number of components are selected, then deselect all the other selected components.
					if(repeat == 2){					
						driver.findElement(By.xpath(sXPath)).click();	
						sleep(1);
					}						
				}		
								
			}
			printFunctionReturn(fn_pass);				
			return true;
			
		}
		catch(Throwable t) {					
			printError(t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
	}
	
	// Count Failed dependency and resolve the same.
	public static boolean checkDependency(){
		printLogs("Calling checkDependency.");
		HashMap<String, Object> hMapGU = Record.getRecord("GU");
		boolean dependencySolved = false;
		int countFailDependency = 0;
		int resolve = 0;
		int rowCount = (int) hMapGU.get("iRowCount");
		int colCount = (int) hMapGU.get("iColCount");
		int compCount = 0;
		
		boolean bSelect = false;
		
		try{
			// Click on the Deploy button and Verify any failed dependency
			while(!dependencySolved) {
				captureScreenShot();
						
				countFailDependency = getGu2FailedDependenciesStatus(rowCount, colCount);
				
				if(countFailDependency == -1) {
					throw new Exception();
				}
				else if(countFailDependency == 0) {
					printLogs("No Failed dependency found in the table.");
					//dependencySolved = true;
				}
				else {
					printLogs("Found " + countFailDependency + " failed dependencies in the table. Resolving it.");
					// If any fail dependency exists print all the component attributes
					printLogs("Table Contents having fail dependencies after forcing all and deploying");	
					if(compNumber != 0 & countFailDependency == compNumber){					
						//Select another components.
						bSelect = true;
						if(resolve != 0){
							compCount = compCount + compNumber;
						} else {
							compCount = compNumber;
						}						
					}								
					printLogs("Resolving Failed dependency now.");
					if(!resolveGu2FailedDependencies(rowCount, colCount)) {
						throw new Exception();
					}
					if(bSelect){
						selectComponents(compCount, true);
						bSelect = false;
					}
					resolve++;			
				}
						
				if(checkElementPresenceByXpath("GuidedUpdate_Step2Heading")) {
					// If the component selected had failed dependency then select other components.
					// Check if Failed dependency is equal to 
					
					if(!clickByXpath("GuidedUpdate_Step2DeployButton")) {
						captureScreenShot();
						throw new Exception();
					}	
							
					printLogs("Clicked Deploy button.");
					sleep(20);
							
				}
						
				// If the style attribute of the section id Step-2 (GU step-3) contains 'block'
				// on current screen it will mean that deploy has started.
				if(SelUtil.getAttributeByXpath("GuidedUpdate_Step3Section", "style").contains("block")) {
					printLogs("No fail dependency found and Deploy started.");
					dependencySolved = true;
					break;
				}
						
				if(resolve > 10) {
					printError("Failed dependency not resolved even after trying 10 times.");
					printFunctionReturn(fn_fail);
					return false;
				}
			}
			
		}
		catch(Throwable t) {					
			printError(t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
		printLogs("Successfully checked and resolved failed dependencies.");
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// resolveGu2FailedDependencies
	public static int getGu2FailedDependenciesStatus(int rowCount, int colCount) {
		printLogs("Calling getGu2FailedDependenciesStatus with values: " + rowCount + ", " + colCount);
		
		int countFailDependency = 0;
		String xPathKeyOfGu2Table = "GuidedUpdate_Step2ComponentsTable";
		String xPathOfTable = OR.getProperty(xPathKeyOfGu2Table);
		
		printLogs("xPathKeyOfGu2Table of the Table: "+ xPathKeyOfGu2Table);
		printLogs("xPath of the Table: "+ xPathOfTable);
		
		String xPathOfRow = "";
		String xPathOfRowPart1 = xPathOfTable + "/tr";
		String xPathOfRowPart2 = "";
		String xPathOfCol = "";
		
		try {
			for(int i = 1 ; i <= rowCount ; i++) {
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow =  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfCol = xPathOfRow + "/td[3]/div/div";
				
				if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-error")) {
					countFailDependency++;
				}
			}
			printLogs("Failed dependency count: "+ countFailDependency);
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while getting the failed dependencies.");
			printFunctionReturn(fn_fail);
			return(-1);
		}
		printFunctionReturn(fn_pass);
		return(countFailDependency);
	}

	// resolveGu2FailedDependencies
	public static boolean resolveGu2FailedDependencies(int rowCount, int colCount) {
		printLogs("Calling resolveGu2FailedDependencies with values: " + rowCount + ", " + colCount);
		
		//int countFailDependency = 0;
		String xPathKeyOfGu2Table = "GuidedUpdate_Step2ComponentsTable";
		String xPathOfTable = OR.getProperty(xPathKeyOfGu2Table);
		
		printLogs("xPathKeyOfGu2Table of the Table: "+ xPathKeyOfGu2Table);
		printLogs("xPath of the Table: "+ xPathOfTable);
		
		String xPathOfRow = "";
		String xPathOfRowPart1 = xPathOfTable + "/tr";
		String xPathOfRowPart2 = "";
		String xPathOfCol = "";
		String xPathToClick =  "";
		
		try {
			for(int i = 1 ; i <= rowCount ; i++) {
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow =  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfCol = xPathOfRow + "/td[3]/div/div";
				//xPathToClick =  xPathOfRow + "/td[1]/a";
				xPathToClick =  xPathOfRow + "/td[1]";
				
				if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-error")) {
					printLogs("Row: " + i + " contents:: ");
					String colData = "";
					String xPathOfColPart1 = xPathOfRow + "/td";
					String xPathOfColPart2 = "";
					String rowData = "";
					
					for(int j = 1 ; j <= colCount ; j++) {			
						xPathOfColPart2 = "[" + j + "]";
						
						xPathOfCol =  xPathOfColPart1 + xPathOfColPart2;
						
						if (j == 1) {
							xPathOfCol = xPathOfCol + "/a";
							
							//class="hp-toggle hp-checked"
							//class="hp-toggle"
							if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-toggle hp-checked")) {
								colData = "Checked";								
							}
							else {
								colData = "Unchecked";
							}							
							colData = getTextByActualXpath(xPathOfCol);
						}
						else if (j == 3) {
							xPathOfCol = xPathOfCol + "/div/div";
							if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-error")) {
								colData = "ERROR";
							}
							else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-ok")) {
								colData = "OK";
							}
							else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-disabled")) {
								colData = "DISABLED";
							}
							else {
								colData = "NONE";
							}
						}
						else {
							colData = getTextByActualXpath(xPathOfCol);
						}
						//System.out.println(colData);
						
						rowData = rowData + " ==> " + colData;
					}
					printLogs(rowData);
					
					if(!clickByActualXpath(xPathToClick)) {
						throw new Exception();
					}
					sleep(2);
					printLogs("Unselected row: " + i);
					captureScreenShot();
				}
			}
			sleep(2);
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while getting the failed dependencies.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Method to analyze the component table content and resolve dependencies.
	// analyzeGu2TableContents
	public static boolean analyzeGu2TableContents() {
		printLogs("Calling analyzeGu2TableContents");
		
		String xPathKeyOfGu2Table = "GuidedUpdate_Step2ComponentsTable";
		String xPathOfTable = OR.getProperty(xPathKeyOfGu2Table);
		
		printLogs("xPathKeyOfGu2Table of the Table: "+ xPathKeyOfGu2Table);
		printLogs("xPath of the Table: "+ xPathOfTable);
		
		int rowCount = getTableRowCount(xPathKeyOfGu2Table);
		if(rowCount == -1) {
			printFunctionReturn(fn_fail);
			return false;
		}
		if(rowCount == 0) {
			printError("No rows found in table: " + xPathKeyOfGu2Table);
			printFunctionReturn(fn_fail);
			return false;
		}
				
		//else {
			//printLogs(xPathKey + " row count = " + rowCount);
		//}
		
		int colCount = getTableColumnCount(xPathKeyOfGu2Table);
		if(colCount == -1) {
			printFunctionReturn(fn_fail);
			return false;
		}
		if(colCount == 0) {
			printError("No cols found in table: " + xPathKeyOfGu2Table);
			printFunctionReturn(fn_fail);
			return false;
		}
		
		String xPathOfRow = "";
		String xPathOfRowPart1 = xPathOfTable + "/tr";
		String xPathOfRowPart2 = "";
		
		String xPathOfCol = "";
		String xPathOfColPart1 = "";
		String xPathOfColPart2 = "";
		
		String colData = "";
		String rowData = "";	
		
		int repeat = 1;
		
		
		printLogs("Table Contents::");
		
		try {
			for(int i = 1 ; i <= rowCount ; i++) {
				rowData = "";
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow =  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfColPart1 = xPathOfRow + "/td";
				
				for(int j = 1 ; j <= colCount ; j++) {
					colData = "";
					if (colCount == 1) {
						xPathOfColPart2 = "";
					}
					else {
						xPathOfColPart2 = "[" + j + "]";
					}
					
					xPathOfCol =  xPathOfColPart1 + xPathOfColPart2;
					
					if (j == 1) {
						xPathOfCol = xPathOfCol + "/a";
						//class="hp-toggle hp-checked"
						//class="hp-toggle"
						if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-toggle hp-checked")) {
							colData = "Checked";
							repeat = 2;
						}
						else {
							colData = "Unchecked";
							repeat = 1;
						}
						//if (i==17) {
							//repeat = 0;
						//}
						//colData = colData + "-" + getTextByActualXpath(xPathOfCol);
						
						for(int z = 1; z <= repeat ; z++){
								driver.findElement(By.xpath(xPathOfCol)).click();								
								sleep(1);
						}	
												
						colData = colData + "-" + getTextByActualXpath(xPathOfCol);
						
					}
					else if (j == 3) {
						xPathOfCol = xPathOfCol + "/div/div";
						if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-error")) {
							colData = "ERROR";
						}
						else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-ok")) {
							colData = "OK";
						}
						else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-disabled")) {
							colData = "DISABLED";
						}
						else {
							colData = "NONE";
						}
					}
					else {
						colData = getTextByActualXpath(xPathOfCol);
					}
					//System.out.println(colData);
					
					rowData = rowData + " ==> " + colData;
				}
				printLogs(rowData);
			}
			if(!verifyButtonStatus("GuidedUpdate_Step2BackButton", "enabled")) {
				throw new Exception();
			}
			if(!verifyButtonStatus("GuidedUpdate_Step2ResetButton", "enabled")) {
				throw new Exception();
			}
			
			// Click on the Deploy button and Verify any failed dependency
			boolean dependencySolved = false;
			int countFailDependency = 0;
			int resolve = 0;
			
			while(!dependencySolved) {
				captureScreenShot();
				
				countFailDependency = getGu2FailedDependenciesStatus(rowCount, colCount);
				if(countFailDependency == -1) {
					throw new Exception();
				}
				else if(countFailDependency == 0) {
					printLogs("No Failed dependency found in the table.");
					//dependencySolved = true;
				}
				else {
					printLogs("Found " + countFailDependency + " failed dependencies in the table. Resolving it.");
					
					// If any fail dependency exists print all the component attributes
					printLogs("Table Contents having fail dependencies after forcing all and deploying");					
					for(int ii = 1 ; ii <= rowCount ; ii++) {
						rowData = "";
						if (rowCount == 1) {
							xPathOfRowPart2 = "";
						}
						else {
							xPathOfRowPart2 = "[" + ii + "]";
						}
						xPathOfRow =  xPathOfRowPart1 + xPathOfRowPart2;
						xPathOfColPart1 = xPathOfRow + "/td";
						
						for(int jj = 1 ; jj <= colCount ; jj++) {
							colData = "";
							if (colCount == 1) {
								xPathOfColPart2 = "";
							}
							else {
								xPathOfColPart2 = "[" + jj + "]";
							}
							
							xPathOfCol =  xPathOfColPart1 + xPathOfColPart2;
							
							if (jj == 1) {
								xPathOfCol = xPathOfCol + "/a";
								//class="hp-toggle hp-checked"
								//class="hp-toggle"
								if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-toggle hp-checked")) {
									colData = "Checked";
								}
								else {
									colData = "Unchecked";
								}
								colData = colData + "-" + getTextByActualXpath(xPathOfCol);
							}
							else if (jj == 3) {
								xPathOfCol = xPathOfCol + "/div/div";
								if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-error")) {
									colData = "ERROR";
								}
								else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-ok")) {
									colData = "OK";
								}
								else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-disabled")) {
									colData = "DISABLED";
								}
								else {
									colData = "NONE";
								}
							}
							else {
								colData = getTextByActualXpath(xPathOfCol);
							}
							//System.out.println(colData);
							
							rowData = rowData + " ==> " + colData;
						}
						printLogs(rowData);
						printLogs(ii + "  -> component status -> " + driver.findElement(By.xpath(xPathOfCol)).getAttribute("class"));
					}
					printLogs("Resolving Failed dependency now.");
					if(!resolveGu2FailedDependencies(rowCount, colCount)) {
						throw new Exception();
					}
					resolve++;
				}
				
				if(checkElementPresenceByXpath("GuidedUpdate_Step2Heading")) {
					// If the component selected had failed dependency then select other components.
					// Check if Failed dependency is equal to 
					
					if(!clickByXpath("GuidedUpdate_Step2DeployButton")) {
						captureScreenShot();
						throw new Exception();
					}	
					
					printLogs("Clicked Deploy button.");
					sleep(20);
					
				}
				
				// If the style attribute of the section id Step-2 (GU step-3) contains 'block'
				// on current screen it will mean that deploy has started.
				if(SelUtil.getAttributeByXpath("GuidedUpdate_Step3Section", "style").contains("block")) {
					printLogs("No fail dependency found and Deploy started.");
					dependencySolved = true;
					break;
				}
				
				if(resolve > 10) {
					printError("Failed dependency not resolved even after trying 10 times.");
					printFunctionReturn(fn_fail);
					return false;
				}
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred in analyzeGu2TableContents.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Methods related to screen : GU Step-3
	
	//Analyze view logs table.
	// Methods related to screen : GU Step-3
	

	// analyzeViewGu3Table
	public static boolean analyzeViewGu3Table() {
		printLogs("Calling analyzeViewGu3Table");
		//String screenShotName = filename + "ViewLog_";
		String xPathKeyOfGu3Table = "GuidedUpdate_Step3InstallationResultsTable";
		String xPathKeyViewLogsWindowInstallNotes = "GuidedUpdate_Step3ViewLogsWindowInstallNotes";
		String xPathKeyViewLogsWindowCloseButton = "GuidedUpdate_Step3ViewLogsWindowCloseButton";
		boolean b_failure_flag = false;
		ArrayList<String> tableList = new ArrayList<String>();
		boolean b_printedTable = false;
		
		String xPathOfTable = OR.getProperty(xPathKeyOfGu3Table);
		
		printLogs("xPathKeyOfGu3Table of the Table: "+ xPathKeyOfGu3Table);
		printLogs("xPath of the Table: "+ xPathOfTable);
		
		int rowCount = getTableRowCount(xPathKeyOfGu3Table);
		if(rowCount == -1) {
			printFunctionReturn(fn_fail);
			return false;
		}
		if(rowCount == 0) {
			printError("No rows found in table: " + xPathKeyOfGu3Table);
			printFunctionReturn(fn_fail);
			return false;
		}
		//else {
			//printLogs(xPathKey + " row count = " + rowCount);
		//}
		
		int colCount = getTableColumnCount(xPathKeyOfGu3Table);
		if(colCount == -1) {
			printFunctionReturn(fn_fail);
			return false;
		}
		if(colCount == 0) {
			printError("No cols found in table: " + xPathKeyOfGu3Table);
			printFunctionReturn(fn_fail);
			return false;
		}
		
		String xPathOfRow = "";
		String xPathOfRowPart1 = xPathOfTable + "/tr";
		String xPathOfRowPart2 = "";
		
		String xPathOfCol = "";
		String xPathOfColPart1 = "";
		String xPathOfColPart2 = "";
		
		String colData = "";
		String rowData = "";
		
		//printLogs("Table Contents::");
		tableList.add("Localhost Guided Update Step 3 Table Contents::");
		printLogs("**************View Logs*********************");
		try {
			for(int i = 1 ; i <= rowCount ; i++) {
				rowData = "";
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow =  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfColPart1 = xPathOfRow + "/td";
				
				//******WORKAROUND FOR MOVED SCREEN************
				
				String xPathOfViewLogCol = xPathOfColPart1 + "[5]/a";
				
				// Click on the icon to get the ViewLogs.
				if(!clickByActualXpath(xPathOfViewLogCol)) {
					printError("Got exception while clicking icon. Retrying the click.");
					sleep(1);
					if(!clickByActualXpath(xPathOfViewLogCol)) {
						throw new Exception();
					}
				}
				
				// Wait for View-Log pop-up window to appear
				if(!checkElementPresenceByXpath(xPathKeyViewLogsWindowInstallNotes)) {
					printError("Error:: Did not get the ViewLogs popup window. Retrying the click.");
					captureScreenShot();
					if(!clickByActualXpath(xPathOfViewLogCol)) {
						sleep(1);
						if(!checkElementPresenceByXpath(xPathKeyViewLogsWindowInstallNotes)) {
							printError("Error:: The ViewLogs popup window did not appear even after a retrying. Continuing with the next value if any.");
							//throw new Exception();
						}
					}
				}
				
				// Take the screen-shot of the View-Log window
				captureScreenShot();
				
				// Get the contents of the View-Log window and print
				String sText = getValueByXpath(xPathKeyViewLogsWindowInstallNotes);
				
				if(sText.contains(sErrorString)) {
					b_failure_flag = true;
					printError("Got Error while getting text for row : " + i);
				}
				else {
					printLogs("View Log Text: " + sText);
				}
				
				printLogs("Captured Screen shot of the View-Log and also printed the contents of the same.");
				
				// Close the View-Log pop-up window
				clickByXpath(xPathKeyViewLogsWindowCloseButton);
				printLogs("Closed the View-Log window.");
				sleep(1);
				
				//**************WORKAROUND COMPLETE************				
				
				for(int j = 1 ; j <= colCount ; j++) {
					colData = "";
					if (colCount == 1) {
						xPathOfColPart2 = "";
					}
					else {
						xPathOfColPart2 = "[" + j + "]";
					}
					
					xPathOfCol =  xPathOfColPart1 + xPathOfColPart2;
					
					// First Click on ViewLogs for the column
					// Take the screen shot of the ViewLogs for that component and also print the ViewLogs contents
					// Get the contents of the column
					// Print the final table
					
					if (j == 1) {
						xPathOfCol = xPathOfCol + "/div/div";
						
						/*
						// Click on the icon to get the ViewLogs.
						if(!clickByActualXpath(xPathOfCol)) {
							printError("Got exception while clicking icon. Retrying the click.");
							sleep(1);
							if(!clickByActualXpath(xPathOfCol)) {
								throw new Exception();
							}
						}
						
						// Wait for View-Log pop-up window to appear
						if(!checkElementPresenceByXpath(xPathKeyViewLogsWindowInstallNotes)) {
							throw new Exception();
						}
						
						// Take the screen-shot of the View-Log window
						captureScreenShot(screenShotName + i);
						
						// Get the contents of the View-Log window and print
						String sText = getTextByXpath(xPathKeyViewLogsWindowInstallNotes);
						
						if(sText.contains(sErrorString)) {
							b_failure_flag = true;
							printError("Got Error while getting text for row : " + i);
						}
						else {
							printLogs("View Log Text: " + sText);
						}
						
						printLogs("Captured Screen shot of the View-Log and also printed the contents of the same.");
						
						// Close the View-Log pop-up window
						clickByXpath(xPathKeyViewLogsWindowCloseButton);
						printLogs("Closed the View-Log window.");
						sleep(1);
						*/
						
						
						// Get the icon type.
						if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-error")) {
							colData = "ERROR";
						}
						else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-ok")) {
							colData = "OK";
						}
						else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-disabled")) {
							colData = "DISABLED";
						}
						else {
							colData = "NONE";
						}
					}
					/*
					if (j == 5) {
						xPathOfCol = xPathOfCol + "/a";						
						colData = getTextByActualXpath(xPathOfCol);
					}
					*/
					else {
						colData = getTextByActualXpath(xPathOfCol);
					}
					//System.out.println(colData);
					
					rowData = rowData + " ==> " + colData;
				}
				//printLogs(rowData);
				// Append to the List
				tableList.add(rowData);
				
				// Getting any error or already update component issues.
				String xPathDeploymentStatus = xPathOfTable + "/tr[" + i + "]/td[4]";
				String deployStatus = getTextByActualXpath(xPathDeploymentStatus);
				if(deployStatus.contains("error") || deployStatus.contains("already current")) {
					String xPathCompNumber = xPathOfTable + "/tr[" + i + "]/td[2]";
					String xPathCompName = xPathOfTable + "/tr[" + i + "]/td[3]";
					String compNumber = getTextByActualXpath(xPathCompNumber);
					String compName = getTextByActualXpath(xPathCompName);
					appendHtmlComment(deployStatus + " - " + compNumber + " : " + compName);
				}
			}
			
			// Print the table list
			printLogs("-----------------------------------------------------------------------");
			for(int i = 0; i < tableList.size(); i++) {  
				printLogs(tableList.get(i));
			}
			printLogs("-----------------------------------------------------------------------");
			b_printedTable = true;
			
			if(b_failure_flag) {
				printError("Raising exception as b_failure_flag is true");
				throw new Exception();
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred in analyzeViewGu3Table.");
			
			if(!b_printedTable) {
				// Print the table list
				printLogs("-----------------------------------------------------------------------");
				for(int i = 0; i < tableList.size(); i++) {  
					printLogs(tableList.get(i));
				}
				printLogs("-----------------------------------------------------------------------");
			}
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Method to get deployment status 
	// Method related to deployment in progress 
	public static String guidedUpdateGetInstallProgressText(){
		printLogs("Calling guidedUpdateWaitForDeploymentToStart");
		String xPathKeyOfTable = "GuidedUpdate_Step3DeploymentTable";
		
		try {
			int rowCount = getTableRowCount(xPathKeyOfTable);
			
			if(rowCount == -1) {
				printFunctionReturn(fn_fail);
				return null;
			}
			if(rowCount == 0) {
				printError("No rows found in table: " + xPathKeyOfTable);
				printError("This table must have atleast 1 row.");
				printFunctionReturn(fn_fail);
				return null;
			}
			else {
				printLogs(xPathKeyOfTable + " row count = " + rowCount);
			}
			//int timeout = 0;
			
			String xPathOfTable = OR.getProperty(xPathKeyOfTable);
			String xPathOfRow = "";
			String xPathOfName = "";
			String xPathOfProgress = "";			
			
			String xPathOfRowPart1 = xPathOfTable + "/tr";
			String xPathOfRowPart2 = "";
			String xPathPartOfName = "/td[2]";			
			
			String xPathPartOfProgress = "/td[5]";
			
			String itemName = "";
			String deploymentStatus = "";	
			
			
			for(int i = 1 ; i <= rowCount ; i++) {
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow 			=  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfName 		= xPathOfRow + xPathPartOfName;
				xPathOfProgress 	=  xPathOfRow + xPathPartOfProgress;				
				
				itemName = getTextByActualXpath(xPathOfName);
				printLogs("Name for row : " + i + " : " + itemName);
				
				deploymentStatus = getTextByActualXpath(xPathOfProgress);
				if (!deploymentStatus.isEmpty()){
					printLogs("Status now for row : " + i + " : " + deploymentStatus);
					break;
				}	
			}
	    		return deploymentStatus;
			
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while getText.");
			printFunctionReturn(fn_fail);
			return null;
		}
			
	}
	
	// Method to wait for deployment to start.
	// Method to wait for deploy to start.
	public static boolean guidedUpdateWaitForDeploymentToStart(int iTimeout){
		printLogs("Calling guidedUpdateWaitForDeploymentToStart");
		maxWaitTime = iTimeout;		
		
		try {
				String deploymentStatusOld = "Fresh";
				String deploymentStatus;
				int statusCount = 0;
				int timeout = 0;
				while(true) {
					
		    		deploymentStatus = guidedUpdateGetInstallProgressText();
		    		
		    		if(!deploymentStatus.contains(deploymentStatusOld)) {
		    			statusCount = 0;
		    		}
		    		else {
		    			statusCount++;
		    		}
		    		
		    		if(statusCount == 0) {
			    		printLogs("Status now: " + deploymentStatus);
			    		if(deploymentStatus.contains("Deploying")) {
			    			printLogs("Deployment started successfully.");
			    			break;
			    		}
		    		}
		    			    		
		    		if(timeout > maxWaitTime) {
		    			printError("ERROR: Timeout occurred while waiting for deployment to complete");
		    			break; 
		    		}		    		
		    		timeout++;
			    }
				captureScreenShot();
				stopWatch();
			}	
		
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while waiting for deployment to start.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Method to wait till deployment completes.
	// guidedUpdateWaitForDeploymentToComplete
	public static boolean guidedUpdateWaitForDeploymentToComplete(String xPathKeyOfTable) {
		printLogs("Calling guidedUpdateWaitForDeploymentToComplete with values: " + xPathKeyOfTable);
		int maxWaitTime = 900;	// This is multiplied by 5 as there is sleep(5). So actual time = 4500s = 1hr 15m
		
		try {
			printLogs("Waiting for deployment to complete in table: " + xPathKeyOfTable);
			
			int rowCount = getTableRowCount(xPathKeyOfTable);
			
			if(rowCount == -1) {
				printFunctionReturn(fn_fail);
				return false;
			}
			if(rowCount == 0) {
				printError("No rows found in table: " + xPathKeyOfTable);
				printError("This table must have atleast 1 row.");
				printFunctionReturn(fn_fail);
				return false;
			}
			else {
				printLogs(xPathKeyOfTable + " row count = " + rowCount);
			}
			
			// wait for deployment to complete
			String xPathOfTable = OR.getProperty(xPathKeyOfTable);
			String xPathOfRow = "";
			String xPathOfName = "";
			String xPathOfProgress = "";
			String xPathOfFinalStatus = "";
			
			String xPathOfRowPart1 = xPathOfTable + "/tr";
			String xPathOfRowPart2 = "";
			String xPathPartOfName = "/td[2]";
			//String xPathPartOfProgress = "/td[3]";
			String xPathPartOfFinalStatus = "/td[3]";
			String xPathPartOfProgress = "/td[5]";
			
			String itemName = "";
			String deploymentStatus = "";
			String deploymentStatusOld = "Fresh";
			String finalStatus = "";
			String deploymentCompleteMsg = "Deployment done.";
			
			int timeout = 0;
			boolean bComplete = false;
			
			for(int i = 1 ; i <= rowCount ; i++) {
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow 			=  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfName 		= xPathOfRow + xPathPartOfName;
				xPathOfProgress 	=  xPathOfRow + xPathPartOfProgress;
				xPathOfFinalStatus	=  xPathOfRow + xPathPartOfFinalStatus;
				
				itemName = getTextByActualXpath(xPathOfName);
				printLogs("Name for row : " + i + " : " + itemName);
				startWatch();
				
				int statusCount = 0;
				
				while(true) {
					bComplete = false;
		    		deploymentStatus = getTextByActualXpath(xPathOfProgress);
		    		
		    		//if(!deploymentStatusOld.equals(deploymentStatus)) {
		    		if(!deploymentStatus.contains(deploymentStatusOld)) {
		    			statusCount = 0;
		    		}
		    		else {
		    			statusCount++;
		    		}
		    		
		    		if(statusCount == 0) {
		    			printLogs("Status now for row : " + i + " : " + deploymentStatus);
		    			if(!deploymentStatus.contains(deploymentCompleteMsg)) {
		    				printLogs("Any change in the status will be printed. Please wait...");
		    			}
		    			else {
		    				printLogs("Deployment done for row : " + i + " : " + deploymentCompleteMsg);
			    			bComplete = true;
			    			break;
		    			}
		    			
		    			deploymentStatusOld = deploymentStatus;
		    		}
		    		
		    		//if(deploymentStatus.contains(deploymentCompleteMsg)) {
		    			//printLogs("Deployment done for row : " + i + " : " + deploymentCompleteMsg);
		    			//bComplete = true;
		    			//break;
		    		//}
		    		//else 
		    		if(timeout > maxWaitTime) {
		    			printError("ERROR: Timeout occurred while waiting for deployment to complete");
		    			break; 
		    		}
		    		sleep(5);
		    		timeout++;
			    }
				captureScreenShot();
				stopWatch();
				
				// If deployment completed successfully - Get the final message.
				if(bComplete) {
					finalStatus = getTextByActualXpath(xPathOfFinalStatus);
		    		printLogs("Final Status for row : " + i + " : " + finalStatus);
				}
				else {
					printFunctionReturn(fn_fail);
					return false;
				}
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while waiting for deployment to complete.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Method to get View Logs data.
	// GuStep3ViewLogs
	public static boolean GuStep3ViewLogs() {
		printLogs("Calling GuStep3ViewLogs");
		//Example: filename = timeStamp + "\\" + timeStamp + "_" + test_name + "_";
		//String screenShotName = filename + "ViewLog_";
		String xPathKeyInstallationResultsTable = "GuidedUpdate_Step3InstallationResultsTable";
		String xPathKeyViewLogsWindowInstallNotes = "GuidedUpdate_Step3ViewLogsWindowInstallNotes";
		String xPathKeyViewLogsWindowCloseButton = "GuidedUpdate_Step3ViewLogsWindowCloseButton";
		boolean failure_flag = false;
		
		printLogs("Getting View Logs from the table: " + xPathKeyInstallationResultsTable);
		
		int rowCount = getTableRowCount(xPathKeyInstallationResultsTable);
		if(rowCount == -1) {
			printFunctionReturn(fn_fail);
			return false;
		}
		if(rowCount == 0) {
			printLogs("No View-Logs as No rows found in table: " + xPathKeyInstallationResultsTable);
			printFunctionReturn(fn_pass);
			return true;
		}
		
		// wait for inventory to complete
		String xPathOfTable = OR.getProperty(xPathKeyInstallationResultsTable);
		
		String xPathOfRow = "";
		String xPathOfRowPart1 = xPathOfTable + "/tr";
		String xPathOfRowPart2 = "";
		String xPathOfCol = "";

		printLogs("Clicking View Log one-by-one::");
		
		try {
			for(int i = 1 ; i <= rowCount ; i++) {
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow =  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfCol = xPathOfRow + "/td[5]/a";
				
				printLogs("Clicking View Log for row : " + i);
				
				// Click the View Log Button - link
				if(!clickByActualXpath(xPathOfCol)) {
					printFunctionReturn(fn_fail);
					return false;
				}
				
				// Wait for View-Log pop-up window to appear
				if(!checkElementPresenceByXpath(xPathKeyViewLogsWindowInstallNotes)) {
					printFunctionReturn(fn_fail);
					return false;
				}
				
				// Take the screen-shot of the View-Log window
				//captureScreenShot(screenShotName + i);
				captureScreenShot();
				
				// Get the contents of the View-Log window and print
				String sText = getTextByXpath(xPathKeyViewLogsWindowInstallNotes);
				
				if(sText.contains(sErrorString)) {
					failure_flag = true;
					printError("Got Error while getting text for row : " + i);
				}
				else {
					printLogs("View Log Text: " + sText);
				}
				
				printLogs("Captured Screen shot of the View-Log and also printed the contents of the same.");
				
				// Close the View-Log pop-up window
				clickByXpath(xPathKeyViewLogsWindowCloseButton);
				printLogs("Closed the View-Log window.");
			}
			
			if(failure_flag) {
				printError("Encountered error in the function.");
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while handling View Log.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Methods related to screen : GU Advanced Options	
		public static boolean verifyAdvOptionsPage(){
			printLogs("Calling VerifyAdvOptionsPage");
			
			String sSelectedOption;
			String[] sContents = new String[12];	
			String[] sReboot = new String[3];
			
			boolean bFail = false;
			
			GuidedUpdatePage GUPage = GuidedUpdatePage.getInstance();	
			try
			{
				// Verify list of Install options displayed.
				sSelectedOption = GetSelectedInstallOption();
				if(!sSelectedOption.matches("Upgrade Both - Allow upgrades.*")){
					printError("Expected option: Upgrade Both");
					printLogs("Actual: " + sSelectedOption);
					bFail = true;
				}
				// Get All options.
				sContents = GetInstallOptions();			
				if(sContents.length !=0){
					for (String sContent : sContents){
					  if(!Contains(sContent)){
						  printError("Invalid Install Option: " + sContent);					  
						  bFail = true;
					  }
					}	
				} else {
					throw new Exception("Failed to get Install Options.");
				}			
				// Verify the status of Enable verbose.
				if(GUPage.EnableVerbose.isSelected()){
					printLogs("Verbose checkbox is checked.");
					printError("Expected it to be unchecked.");
				}
				// Verify reboot option.
				sSelectedOption = GetSelectedRebootOption();
				if(!sSelectedOption.matches("Never.*")){
					printError("Expected option: Never");
					printLogs("Actual: " + sSelectedOption);
					bFail = true;
				}
				
				// Get All options.
				sReboot = GetRebootOptions();
				if(sReboot.length != 0){
				for (String s : sReboot){				
					if(s != null ){
						if(!Contains(s)){
							printError("Invalid Reboot Option: " + s);					  
							bFail = true;
						}
					}
				}	
				}	 else {
					throw new Exception("Failed to get Reboot Options.");
				}			
				// Verify button status.
				if(!GUPage.AdvOptionsOK.isEnabled()){
					printError("OK button is not enabled.");
					bFail = true;
				}
				
				if(!GUPage.AdvOptionsClose.isEnabled()){
					printError("Close button is not enabled.");
					bFail = true;
				}
				
				if(!GUPage.AdvOptionsOK.isDefault()){
					printError("OK button is not highlighted.");
					bFail = true;
				}
				
				if(bFail){
					throw new Exception("Invalid details found on the page.");
				}
			}
			catch (Throwable t){
				printError("Exception in method VerifyAdvOptionsPage.");
				printLogs("Exception Returned: " + t.getMessage());
				printFunctionReturn("Function-Fail");
				return false;
			}
			printFunctionReturn("Function-Pass");
			return true;
		}
		
		private static String[] GetRebootOptions() {
			return GuidedUpdatePage.GetRebootOptions();
		}
		private static String[] GetInstallOptions() {
			return GuidedUpdatePage.GetInstallOptions();
		}
		private static String GetSelectedInstallOption() {
			return GuidedUpdatePage.GetSelectedInstallOption();
		}
		
		private static String GetSelectedRebootOption() {
			return GuidedUpdatePage.GetSelectedRebootOption();
		}
		
		private static boolean Contains(String sContent) {		
			boolean bFound = false;
			String[] sSearchList = new String[] {
					"Upgrade Both - Allow upgrades to already installed components and installation of new software packages",			
					"Upgrade Firmware - Allow upgrades to newer versions of already installed firmware components",
					"Upgrade Software - Allow upgrades to already installed software and installation of new packages",
					"Downgrade & Rewrite Firmware - Allow already installed firmware components to be downgraded or rewritten to older or same version",
					"Downgrade & Rewrite Software - Allow already installed software components to be downgraded or rewritten to older or same version",
					"Downgrade & Rewrite Both - Allow already installed components to be downgraded or rewritten to older or same version",
					"Downgrade Firmware - Allow downgrades to older versions of already installed firmware components",
					"Downgrade Software - Allow downgrades to older versions of already installed software components",
					"Downgrade Both - Allow downgrades to older versions of already installed components",
					"Rewrite Firmware - Allow the same version to be rewritten for already installed firmware components",
					"Rewrite Software - Allow the same version to be rewritten for already installed software components",
					"Rewrite Both - Allow the same version to be rewritten for already installed components",
					"Never",
					"If needed",
					"Always"
				};
			printLogs("Verify list contains: "+ sContent);
			try {
				for (String sText:sSearchList){
					if(sText.equals(sContent)){
						bFound = true;
						break;
					}
				}
				if(bFound){
					printLogs("Successfully verified that the list contains: " + sContent);					
				} else {
					printLogs("Unable to find the option: " + sContent);
				}
					
			}
			catch (Throwable t){
				printError("Exception in method Contains.");
				printLogs("Excepton Returned: " + t.getMessage());
				printFunctionReturn("Function-Fail");				
			}
			printFunctionReturn("Function-Pass");
			return bFound;
		}
		
	
	// Methods related to screen : GU Reports
	
	

}
