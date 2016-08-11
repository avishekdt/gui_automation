package gui.common.base;

import gui.common.pages.BaselinePage;
import gui.common.pages.BaselinePage.CompType;
import gui.common.util.ErrorUtil;

import java.io.File;
import java.io.IOException;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileDeleteStrategy;
import org.openqa.selenium.By;

public class BaselineLibrary extends SelUtil{
	
	/*
	 * METHODS
	 * addAdditionalBaseline
	 * addBlPfwSelectProxyDdOption
	 * addBlSelectLocTypeDdOption
	 * closeCblPageUsingCloseButton
	 * createCblSelectCsmDefXmlFileDdOption
	 * createCblSelectProxyOptionsDdOption
	 * createCustomBaseline
	 * deleteAbl
	 * getBlCount
	 * getCblPageBlCount
	 * performBaselineInventory
	 * WaitForBaselineInventoryToComplete
	 * WaitForPage
	 * addAblWithUncPath
	 * ifBlExists
	 */
	
	// Methods related to screen : BL
	
	// Public variables
	
	
	// ifBlExists
	public static boolean ifBlExists (String blNameToFind) {
		printLogs("Calling verifyBlExists with value: " + blNameToFind);
		/*String blName = "";
		String xPathBlHeader;
		boolean blFound = false;*/
		String xPathAdditionalBaselineName 		= "";
		String xPathAdditionalBaselineLocation 	= "";
		String blName = "";
		//String blHeader;
		boolean blFound = false;
		
		String xPathAdditionalBaseline1 = OR.getProperty("BaselineLibrary_SPPLink");
		String xPathAdditionalBaseline2 = "[";
		String xPathAdditionalBaseline_name = "]/header/div[2]";
		String xPathAdditionalBaseline_loc  = "]/div/ol/li[3]/div";
		
		try {
			captureScreenShot();
			
			// Get the total no of BLs present.
			int totalBlAvailable = getBlCount();
			
			// Get the folder name whose BL needs to be checked.  
			String blFolderName = getFileNameFromPath(blNameToFind);
			
			for (int i = 1 ; i <= totalBlAvailable ; i++) {
				
				/*xPathBlHeader = OR.getProperty("BaselineLibrary_SPPLink") + "[" + i + "]/header";
				blName = driver.findElement(By.xpath(xPathBlHeader)).getText();
				printLogs(blName);
				if (blName.contains(blNameToFind)) {
					printLogs("Found Bl: " + blName);
					blFound = true;
					break;
				}*/
				
				xPathAdditionalBaselineName 	= xPathAdditionalBaseline1 + xPathAdditionalBaseline2 + i + xPathAdditionalBaseline_name;
				printLogs("baseline exists" + xPathAdditionalBaselineName);
				xPathAdditionalBaselineLocation = xPathAdditionalBaseline1 + xPathAdditionalBaseline2 + i + xPathAdditionalBaseline_loc;
				
				blName = getTextByActualXpath(xPathAdditionalBaselineLocation);
				String currentBlName = getFileNameFromPath(blName);
				printLogs("currentBlName: " + currentBlName);
				// Checking the BL is deleted.
				if (currentBlName.contains(blFolderName)) {								
					printLogs("Found Bl: " + blNameToFind);
					blFound = true;
					break;
				}
			}
			
			if (!blFound) {
				printLogs("BL not found: " + blNameToFind);
				printFunctionReturn(fn_pass);
				return(false);
			}
			printFunctionReturn(fn_pass);
			return(true);
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Exception occurred while getting BL name on the BL page.");
			printFunctionReturn(fn_fail);
			return(false);
		}
	}
	
	// getBlCountTextFromBlPageTopHeading
	public static String getBlCountTextFromBlPageTopHeading () {
		printLogs("Calling getBlCountTextFromBlPageTopHeading");
		String blCountText = "";
		
		try {
			blCountText = driver.findElement(By.xpath(OR.getProperty("BaselineLibrary_BlCountText"))).getText();
			printLogs("BL count on top of BL page: " + blCountText);
			//captureScreenShot();
			printFunctionReturn(fn_pass);
			return(blCountText);
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Exception occurred while getting BL count text on the BL page.");
			printFunctionReturn(fn_fail);
			return(null);
		}	
	}
	
	//
  	// performBaselineInventory : This method will do baseline inventory and wait for it to complete
	// 1. Select Baseline Library link from Main Menu.
	// 2. Wait till Inventory completes 
	public static boolean performBaselineInventory () {
		printLogs("Calling performBaselineInventory");
		
		try {
			// 1. Go to BL page.
			if(!guiSetPage("BaselineLibrary")) {
				printError("Failed to reach Baseline Library page.");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			/*	// 1. Select Baseline Library from the Main Menu.
			if(!CommonHpsum.clickLinkOnMainMenu("MainMenu_BaselineLibrary")) {
				appendHtmlComment("Failed to click on the Main Menu Baseline Library link");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			// Verify Page: BaselineLibrary
			if(!verifyPage("BaselineLibrary")) {
				appendHtmlComment("Failed to verify Baseline Library page");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Verify button exists: Add Baselines
			if(!verifyButtonStatus("BaselineLibrary_AddBaselineButton", "enabled")) {
				printError("Add Baseline button was not enabled as expected.");
				//printFunctionReturn(fn_fail);
			}*/
			
			// Modify the implicit wait time to 5s. -- reason?
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			printLogs("Setting the implicit wait to 5s.");
			
			// Get the total no of BLs present.
			int totalBlAvailable = getBlCount();
			
			// 2. Click on All Link and wait for inventory to complete
			sleep(5);	// waiting for tables to get populated.
			
			String xPathAdditionalBaselineName = "";
			String xPathAdditionalBaselineLocation = "";
			
			String xPathAdditionalBaseline1 = OR.getProperty("BaselineLibrary_SPPLink");
			String xPathAdditionalBaseline2 = "[";
			String xPathAdditionalBaseline_name = "]/header/div[2]";
			String xPathAdditionalBaseline_loc  = "]/div/ol/li[3]/div";

			// Checking all baselines
			for(int j = 1 ; j <= totalBlAvailable ; j++){
				xPathAdditionalBaselineName		= xPathAdditionalBaseline1 + xPathAdditionalBaseline2 + j + xPathAdditionalBaseline_name;
				xPathAdditionalBaselineLocation	= xPathAdditionalBaseline1 + xPathAdditionalBaseline2 + j + xPathAdditionalBaseline_loc;
				
				if(!clickByActualXpath(xPathAdditionalBaselineName)){
					printError("Error on clicking " + getTextByActualXpath(xPathAdditionalBaselineName) + "Baseline (" +
								getTextByActualXpath(xPathAdditionalBaselineLocation) + ")");
					//appendHtmlComment("Failed to click on other baselines present");
					printFunctionReturn(fn_fail);
					return false;
				}
				sleep(7);
				printLogs("Inventory happening on " + getTextByActualXpath(xPathAdditionalBaselineName) + " on " + 
							getTextByActualXpath(xPathAdditionalBaselineLocation));
				
				if(!WaitForBaselineInventoryToComplete()) {
					printError("WaitForBaselineInventoryToComplete failed.");
					printFunctionReturn(fn_fail);		
					return false;
				}
				captureScreenShot();
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred in performBaselineInventory method.");
			printFunctionReturn(fn_fail);
			return false;
		}
		finally {
			driver.manage().timeouts().implicitlyWait(iElementDetectionTimeout, TimeUnit.SECONDS);
			printLogs("Reset the implicit wait.");
		}
		printFunctionReturn(fn_pass);		
		return true;
	}
	
	
	// WaitForBaselineInventoryToComplete - On Baseline Library Page
	public static boolean WaitForBaselineInventoryToComplete() {
		printLogs("Calling WaitForBaselineInventoryToComplete");
		
		String msgInProgress = OR.getProperty("MessageBaselineInventoryInProgress");
		String msgCompleted  = OR.getProperty("MessageBaselineInventoryComplete");
		
		int maxWaitTime = 900;	// This is multiplied by ~5s as there is sleep(5)+ other operations.
					// So actual time ~= 900*5 = 4500s = 75m of wait time
		
		
		try {
			printLogs("Waiting for Baseline inventory to complete");
			
			// Click on the message bar - Common_PageMsgBar
			if(!clickByXpath("Common_PageMsgBar")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			//String inventoryStatusComponentOld = "Fresh";
			String inventoryStatus = "";
			//String inventoryStatusComponent = "";
			int refresh = 0;
			int timeout = 0;
			
			startWatch();
			
			System.out.print("Baseline inventory in progress:");
			while(true) {
				timeout++;
	    		inventoryStatus = getTextByXpath("BaselineLibrary_InventoryMsg");
	    		
	    		if(inventoryStatus.contains(sErrorString)) {
	    			System.out.print("Retrying");
	    			continue;
	    		}	    		
	    		else if(inventoryStatus.contains(msgInProgress)) {
	    			System.out.print(".");
	    			refresh++;
    				
    				// Sleeping for 5s to avoid continuous pinging of browser. 
    				sleep(5);
    				
    				// WORKAROUND: REFRESH THE PAGE EVERY 15 MINS if status does not change.
    	    		if(refresh > 180) {
    	    			printLogs("WARNING:: BL inventory took more than 15 mins, refreshing the page .");
    	    			driver.navigate().refresh();
    	    			sleep(10);
    	    			refresh = 0;
    	    		}
    	    			
	    			// Commenting the below code as components are not getting printed on BL Msg bar now.
	    			/*inventoryStatusComponent = getTextByXpath("BaselineLibrary_InventoryComponentMsg");
	    			if(!inventoryStatusComponent.contains(inventoryStatusComponentOld)) {
		    			printLogs("inventoryStatus = " + inventoryStatusComponent);
		    			inventoryStatusComponentOld = inventoryStatusComponent;
		    			refresh = 0;
		    		}
	    			else {
	    				refresh++;
	    				
	    				// Sleeping for 5s to avoid continuous pinging of browser. 
	    				sleep(5);
	    				
	    				// WORKAROUND: REFRESH THE PAGE EVERY 15 MINS if status does not change.
	    	    		if(refresh > 180) {
	    	    			printLogs("WORKAROUND: Refreshing the page.");
	    	    			driver.navigate().refresh();
	    	    			sleep(10);
	    	    			refresh = 0;
	    	    			//timeout+=2;
	    	    		}
	    			}*/
	    		}
	    		else if(inventoryStatus.contains(msgCompleted)) {
	    			printLogs("Baseline inventory completed: " + inventoryStatus);
	    			break;
	    		}
	    		else {
	    			printError("Check screenshot if MessageBar is actually present. Unknown Baseline inventory message found: " + inventoryStatus);
	    			sleep(10);
	    			printLogs("Report to Automation team if screenshot shows the MessageBar.");
	    		}
	    		if(timeout > maxWaitTime) {
	    			printError("Baseline Inventory Max Wait time is over. Exiting.");
	    			stopWatch();
	    			printFunctionReturn(fn_fail);
					return false;
	    		}
    		}
			stopWatch();
			// Click on the message bar - Common_PageMsgBar - to close the Message bar.
			if(!clickByXpath("Common_PageMsgBar")) {
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while waiting for Baseline inventory to complete.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// WaitForBaselineInventoryToComplete - On Baseline Library Page
	public static void WaitForPage(String sPageName) {
			printLogs("Calling WaitForPAge");
		try
		{
			if (!waitForElementByXpath(sPageName, 60, true)) {
				printError(sPageName + " was expected "
						+ "after selecting Delete for a BL but was not found.");
				printFunctionReturn(fn_fail);
				return;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while waiting for Baseline inventory to complete.");
			printFunctionReturn(fn_fail);
			return;
		}
		printFunctionReturn(fn_pass);
		return;
	}	
	
	// getBlCount
	// Returns count of BLs present on the BL page.
	public static int getBlCount() {
		printLogs("Calling getBlCount");
		int blCount = 0;
		
		try {
			blCount = driver.findElements(By.xpath(OR.getProperty("BaselineLibrary_SPPLink"))).size();
			printLogs("Found " + blCount + " baselines on the left pane of BL page.");
			captureScreenShot();
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Exception occurred while counting the BLs on the BL page.");
			printFunctionReturn(fn_fail);
			return(-1);
		}
		printFunctionReturn(fn_pass);
		return(blCount);
	}
	
	// getCblPageBlCount
	// Returns count of BLs present on the CBL page.
	public static int getCblPageBlCount() {
		printLogs("Calling getCblPageBlCount");
		int blCount = 0;
		
		try {
			String xPathOfTableRows = OR.getProperty("BL_CBL_Table_Baselines") + "/tbody/tr";
			
			blCount = driver.findElements(By.xpath(xPathOfTableRows)).size();
			printLogs("Found " + blCount + " baselines on CBL page.");
			captureScreenShot();
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Exception occurred while counting the BLs on the CBL page.");
			printFunctionReturn(fn_fail);
			return(-1);
		}
		printFunctionReturn(fn_pass);
		return(blCount);
	}
	
	// deleteAbl
	// Deletes the ABL whose name/location is passed.
	public static boolean deleteAbl(String ablLocation) {
		printLogs("Calling deleteAbl with values: " + ablLocation);
		
		try {
			// 1. Go to BL page.
			if(!guiSetPage("BaselineLibrary")) {
				printError("Failed to reach Baseline Library page.");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			sleep(5);	// waiting for tables to get populated.
			
			// 2. Modify the implicit wait time to 5s so as to find the no of BLs faster.
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			printLogs("Setting the implicit wait to 5s.");
			
			// 3. Find how many BLs exist
			int totalBlAvailable = getBlCount();
			
			// 4. Get the folder name whose BL needs to be deleted.  
			String blFolderName = getFileNameFromPath(ablLocation);
			
			// 5. Get the names of all the existing BLs and identify that needs to be deleted 
			String xPathAdditionalBaselineName 		= "";
			String xPathAdditionalBaselineLocation 	= "";
			String blName = "";
			String blHeader;
			boolean blFound = false;
			
			String xPathAdditionalBaseline1 = OR.getProperty("BaselineLibrary_SPPLink");
			String xPathAdditionalBaseline2 = "[";
			String xPathAdditionalBaseline_name = "]/header/div[2]";
			String xPathAdditionalBaseline_loc  = "]/div/ol/li[3]/div";
			
			for (int i = 1 ; i <= totalBlAvailable ; i++) {
				xPathAdditionalBaselineName 	= xPathAdditionalBaseline1 + xPathAdditionalBaseline2 + i + xPathAdditionalBaseline_name;
				xPathAdditionalBaselineLocation = xPathAdditionalBaseline1 + xPathAdditionalBaseline2 + i + xPathAdditionalBaseline_loc;
				
				blName = getTextByActualXpath(xPathAdditionalBaselineLocation);
				String currentBlName = getFileNameFromPath(blName);
				
				// Select the BL which needs to be deleted.
				if (currentBlName.contains(blFolderName)) {
					blFound = true;
					printLogs("Found the ABL " + blFolderName + " on the left pane. Selecting and deleting it now.");
					
					if(!clickByActualXpath(xPathAdditionalBaselineName)){
						printError("Error on clicking BL left pane for BL: " + currentBlName);
						printFunctionReturn(fn_fail);
						return false;
					}
					
					// Wait for BL page to load for this BL.
					sleep(15);
					captureScreenShot();
					
					// Get the BL name from page heading and see if it matches
					blHeader = getTextByXpath("BaselineLibrary_SPPDetailsTitle");
					if (!blHeader.contains(blName)) {
						printLogs("Looks like page did not load. Sleeping for 15s");
						sleep(15);
						captureScreenShot();
					}
					
					// Select the Actions->Delete
					selectActionDropDownOption("CssBaselineActions", "Delete");
					sleep(30);
					captureScreenShot();
					
					// Wait for Delete Baseline popup to appear
					if (!waitForElementByXpath("BaselineLibrary_DeleteConfirmPopupHeading", 60, true)) {
						printError("BaselineLibrary_DeleteConfirmPopupHeading was expected "
								+ "after selecting Delete for a BL but was not found.");
						printFunctionReturn(fn_fail);
						//return false;
					}
					else {						
						// Click on Yes, Delete button
						if (!clickByXpath("BaselineLibrary_DeleteConfirmPopupYes")) {
							printError("Failed to click button BaselineLibrary_DeleteConfirmPopupYes");
							printFunctionReturn(fn_fail);
							return false;
						}
					}
					sleep(30);
					captureScreenShot();
				}
			}
			if(!blFound) {
				printLogs("WARNING:: BL to delete was not found in the current set of BLs available in GUI");
				captureScreenShot();
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Exception occurred while doing the Node inventory.");
			appendHtmlComment("Error occurred in performBaselineInventory method");
			printFunctionReturn(fn_fail);
			return false;
		}
		finally {
			driver.manage().timeouts().implicitlyWait(iElementDetectionTimeout, TimeUnit.SECONDS);
			printLogs("Reset the implicit wait.");
		}
		printFunctionReturn(fn_pass);		
		return true;
	}
	
	
	// Methods related to screen : BL Add
	
	
	// Select option from Location Type drop-down on Add BL page
	/*public static boolean addBlSelectLocTypeDdOption(String optionToSelect) {
		ArrayList <String> dropDownOptions = new ArrayList<String>();
		dropDownOptions.add("Browse HP SUM server path");
		dropDownOptions.add("UNC path  (for example \\\\host\\dir )");
		dropDownOptions.add("Download from hp.com");
		dropDownOptions.add("Download from http share");
		//String optionsCount = Integer.toString(dropDownOptions.size());
		
		if ((!dropDownOptions.contains(optionToSelect)) || (dropDownOptions.size() != Integer.parseInt(optionToSelect))) {
			printError("Incorrect option passed: " + optionToSelect);
			printLogs("Valid Optios: ");
			printLogs("Either pass the index of the option to select. Max allowed: " + dropDownOptions.size());
			printLogs("OR select from any of these options:");
			for (String option:dropDownOptions) {
				printLogs(option);
			}
			printFunctionReturn(fn_fail);
			return false;
		}
		else {
			printLogs("Selecting : " + optionToSelect);
		}
		printFunctionReturn(fn_pass);
		return true;
	}*/
	
	// Select option from Location Type drop-down on Add BL page
	public static boolean addBlSelectLocTypeDdOption(int indexOfOptionToSelect) {
		printLogs("Calling addBlSelectLocTypeDdOption with values: " + indexOfOptionToSelect);
		
		int noOfOptions = 4;
		String[] dropDownOptionsLocType;
		
		if (currentOsName.contains("windows")) {
			noOfOptions = 4;
			dropDownOptionsLocType = new String [noOfOptions];
			dropDownOptionsLocType[0] = "Browse HP SUM server path";
			dropDownOptionsLocType[1] = "UNC path (for example \\\\host\\dir )";
			dropDownOptionsLocType[2] = "Download from hp.com";
			dropDownOptionsLocType[3] = "Download from http share";
		}
		else {
			noOfOptions = 3;
			dropDownOptionsLocType = new String [noOfOptions];
			dropDownOptionsLocType[0] = "Browse HP SUM server path";
			dropDownOptionsLocType[1] = "Download from hp.com";
			dropDownOptionsLocType[2] = "Download from http share";
		}
		
		
		if (indexOfOptionToSelect > noOfOptions) {
			printError("Max number of options: " + noOfOptions + "Passed value: " + indexOfOptionToSelect);
			printLogs("Please correct the index of the option that needs to be selected.");
			printFunctionReturn(fn_fail);
			return false;
		}
		else {
			printLogs("Selecting option at index : " + indexOfOptionToSelect + " : " + dropDownOptionsLocType[indexOfOptionToSelect-1]);
		}
		
		try {
			// Click on select location type drop down
			if(!clickByXpath("BaselineLibrary_AddBaselineLocationTypeDropDown")){
				printError("Failed to click on BaselineLibrary_AddBaselineLocationTypeDropDown");
				printFunctionReturn(fn_fail);
				return false;
			}
			captureScreenShot();
			
			String xPathOfDropDownAfterClicking = OR.getProperty("Common_DropDownOptions");
			String xPathOfOptionToSelect = xPathOfDropDownAfterClicking + "/li[" + indexOfOptionToSelect + "]/a";
			
			String textOfOption = getTextByActualXpath(xPathOfOptionToSelect);
			
			// Match the text of the drop-down option.
			if(!dropDownOptionsLocType[indexOfOptionToSelect-1].equalsIgnoreCase(textOfOption)) {
				printError("The text of the Location Type drop-down option " + indexOfOptionToSelect + " has changed. Report bug.");
				printLogs("Expected Text: " + dropDownOptionsLocType[indexOfOptionToSelect-1]);
				printLogs("Actual Text: " + textOfOption);
			}			
			
			// Select the option
			if(!clickByActualXpath(xPathOfOptionToSelect)) {
				printError("Failed to select the option : " + xPathOfOptionToSelect);
			}
			else {
				printLogs("Successfully selected option with index: " + indexOfOptionToSelect);
			}
			captureScreenShot();
		}
		catch (IOException e) {
			printError("Exception occurred while selecting option " + indexOfOptionToSelect + " from Location Type drop-down on Add BL page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// addBlPfwSelectProxyDdOption
	// Select an option from Proxy Settings drop-down after selecting 
	// PFW option from Location Type drop-down on Add BL page 
	public static boolean addBlPfwSelectProxyDdOption(int indexOfOptionToSelect) {
		printLogs("Calling addBlPfwSelectProxyDdOption with values: " + indexOfOptionToSelect);
		
		int noOfOptions = 3;
		String[] dropDownOptionsLocType = new String [noOfOptions];
		dropDownOptionsLocType[0] = "No Proxy Needed";
		dropDownOptionsLocType[1] = "Proxy server";
		dropDownOptionsLocType[2] = "Proxy script";
		
		if (indexOfOptionToSelect > noOfOptions) {
			printError("Max number of options: " + noOfOptions + "Passed value: " + indexOfOptionToSelect);
			printLogs("Please correct the index of the option that needs to be selected.");
			printFunctionReturn(fn_fail);
			return false;
		}
		else {
			printLogs("Selecting option at index : " + indexOfOptionToSelect + " : " + dropDownOptionsLocType[indexOfOptionToSelect-1]);
		}
		
		try {
			// Click on Proxy Settings drop down
			if(!clickByXpath("BaselineLibrary_AddBaselineHpProxyDropDown")){
				printError("Failed to click on BaselineLibrary_AddBaselineHpProxyDropDown");
				printFunctionReturn(fn_fail);
				return false;
			}
			captureScreenShot();
			String xPathOfDropDownAfterClicking = OR.getProperty("BaselineLibrary_AddBaselineHpProxyDropDownOption");
			String xPathOfOptionToSelect = xPathOfDropDownAfterClicking + "/li[" + indexOfOptionToSelect + "]/a";
			
			String textOfOption = getTextByActualXpath(xPathOfOptionToSelect);
			
			// Match the text of the drop-down option.
			if(!dropDownOptionsLocType[indexOfOptionToSelect-1].equalsIgnoreCase(textOfOption)) {
				printError("The text of the Proxy Settings drop-down option " + indexOfOptionToSelect + " has changed. Report bug.");
				printLogs("Expected Text: " + dropDownOptionsLocType[indexOfOptionToSelect-1]);
				printLogs("Actual Text: " + textOfOption);
			}
			
			// Select the option
			if(!clickByActualXpath(xPathOfOptionToSelect)) {
				printError("Failed to select the option : " + xPathOfOptionToSelect);
			}
			else {
				printLogs("Successfully selected option with index: " + indexOfOptionToSelect);
			}
			captureScreenShot();
		}
		catch (IOException e) {
			printError("Exception occurred while selecting option " + indexOfOptionToSelect + " from PFW Proxy Settings drop-down on Add BL page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	
	// addAdditionalBaseline
	public static boolean addAdditionalBaseline(String additionalPkgLocationKey) {
		printLogs("Calling addAdditionalBaseline with values: " + additionalPkgLocationKey);
		
		String additional_sppLocation= "";
		
		try {
			// Select Baseline Library from the Main Menu.
			if(!CommonHpsum.clickLinkOnMainMenu("MainMenu_BaselineLibrary")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			// Verify Page: BaselineLibrary
			if(!verifyPage("BaselineLibrary")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Verify button exists: Add Baselines
			if(!verifyButtonStatus("BaselineLibrary_AddBaselineButton", "enabled")) {
				printError("Add Baseline button was not enabled as expected.");
				//printFunctionReturn(fn_fail);
			}
			
			// Click on add baseline button
			if(!clickByXpath("BaselineLibrary_AddBaselineButton")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			// Click on add baseline button from pop up
			if(!standaloneHpsum){
				if(!clickByXpath("BaselineLibrary_AddBaselineButtonPopup")) {
					printFunctionReturn(fn_fail);
					return false;
				}
			}
								
			// If additionalPkgLocationKey is already the Actual ABL Location then
			// getting the additional_sppLocation from CONFIG.properties is NOT needed.
			if ((additionalPkgLocationKey.startsWith("/root")) || (additionalPkgLocationKey.contains(":"))) {
				additional_sppLocation = additionalPkgLocationKey;
			}
			else {
				additional_sppLocation = CONFIG.getProperty(additionalPkgLocationKey);
			}
			
			// Verify page title 'add baseline' is present or not
			if(!waitForPage("Common_NewWindowHeading")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Type additional baseline path
			if(!sendKeysByXpath("BaselineLibrary_AddBaselineEnterDirectoryPathInput", additional_sppLocation)) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			// Click on browse button
			if(!clickByXpath("BaselineLibrary_AddBaselineEnterDirectoryPathBrowseButton")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Check browse title is present
			if(!waitForPage("BaselineLibrary_AddBaselineEnterDirectoryPathBrowseHeading")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			// Click on the path so that ok button gets activated
			if(!clickByXpath("BaselineLibrary_AddBaselineBrowsepath")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			// Click on ok button
			if(!clickByXpath("BaselineLibrary_AddBaselineEnterDirectoryPathBrowseOkButton")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
	
			if(!verifyButtonStatus("BaselineLibrary_AddBaselineStartOverButton", "enabled")) {
				printError("Failed verifyButtonStatus.");
			}
			
			if(!verifyButtonStatus("BaselineLibrary_AddBaselineCloseButton", "enabled")) {
				printError("Failed verifyButtonStatus.");
			}
			
			// Click on add button
			if(!clickByXpath("BaselineLibrary_AddBaselineAddButton")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			sleep(10);
			
			// Verify the Add baseline screen is closed 
			if(!waitForNoElementByXpath("Common_NewWindowHeading")) {
				printError("Known Issue: Add New Baseline popup screen not closing after pressing Add button.");
				captureScreenShot();
				printLogs("Closing the Add New Baseline popup screen using the Close button.");
				if(checkElementPresenceByXpath("BaselineLibrary_AddBaselineCloseButton")) {
				if(!clickByXpath("BaselineLibrary_AddBaselineCloseButton")) {
					printFunctionReturn(fn_fail);
					return false;
					}
				}
				printFunctionReturn(fn_fail);
			}
			
			if(!verifyButtonStatus("BaselineLibrary_AddBaselineButton", "enabled")) {
				printError("Failed verifyButtonStatus.");
			}
			
			if(!WaitForBaselineInventoryToComplete()) {
				printError("WaitForBaselineInventoryToComplete failed.");
				printFunctionReturn(fn_fail);		
				return false;
			}
			captureScreenShot();
		}
		catch (IOException e) {
			printError("Exception occurred while adding additional baseline.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// addAdditionalBaseline
	public static boolean addAblWithUncPath(String uncPathKey, String usernameKey, String passwordKey) {
		printLogs("Calling addAblWithUncPath with values: " + uncPathKey + ", " + usernameKey + ", " + passwordKey);
		
		String additional_sppLocation= "";
		
		try {
			// Select Baseline Library from the Main Menu.
			if(!CommonHpsum.clickLinkOnMainMenu("MainMenu_BaselineLibrary")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			// Verify Page: BaselineLibrary
			if(!verifyPage("BaselineLibrary")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Click on add baseline button
			if(!clickByXpath("BaselineLibrary_AddBaselineButton")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			// Click on add baseline button from pop up
			if(!standaloneHpsum){
				if(!clickByXpath("BaselineLibrary_AddBaselineButtonPopup")) {
				printFunctionReturn(fn_fail);
				return false;
				}
			}
			
			captureScreenShot();
			
			// If additionalPkgLocationKey is already the Actual ABL Location then
			// getting the additional_sppLocation from CONFIG.properties is NOT needed.
			if ((uncPathKey.startsWith("/root")) || (uncPathKey.contains(":"))) {
				additional_sppLocation = uncPathKey;
			}
			else {
				additional_sppLocation = CONFIG.getProperty(uncPathKey);
			}
			
			// Verify page title 'add baseline' is present or not
			if(!waitForPage("Common_NewWindowHeading")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Select the 'UNC path (for example \\host\dir)' (Option-2) from the Location Type drop-down
			BaselineLibrary.addBlSelectLocTypeDdOption(2);			
			captureScreenShot();
			
			// Type additional baseline path
			if(!sendKeysByXpath("BaselineLibrary_AddBaselineUriEnterUriInput", additional_sppLocation)) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			/*
			// Click on 2nd radio button
			if(!clickByXpath("BaselineLibrary_AddBaselineUriCredRadio2")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			captureScreenShot();
			
			// Provide username and password
			String uncUserName = CONFIG.getProperty(usernameKey);
			String uncPassword = CONFIG.getProperty(passwordKey);
			if(!sendKeysByXpath("BaselineLibrary_AddBaselineUriAdminUserInput", uncUserName)) {
				printFunctionReturn(fn_fail);
				return false;
			}
			if(!sendKeysByXpath("BaselineLibrary_AddBaselineUriAdminPwdInput", uncPassword)) {
				printFunctionReturn(fn_fail);
				return false;
			}
			captureScreenShot();*/
			
			
			// Verify button status
			if(!verifyButtonStatus("BaselineLibrary_AddBaselineStartOverButton", "enabled")) {
				printError("Failed verifyButtonStatus.");
			}
			
			if(!verifyButtonStatus("BaselineLibrary_AddBaselineCloseButton", "enabled")) {
				printError("Failed verifyButtonStatus.");
			}
			
			// Click on add button
			if(!clickByXpath("BaselineLibrary_AddBaselineAddButton")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			sleep(10);
			
			// Verify the Add baseline screen is closed 
			if(!waitForNoElementByXpath("Common_NewWindowHeading")) {
				printError("Add Baseline popup screen did not close after pressing Add button.");
				captureScreenShot();
				printLogs("Closing the Add Baseline popup screen using the Close button.");
				if(!clickByXpath("BaselineLibrary_AddBaselineCloseButton")) {
					printFunctionReturn(fn_fail);
					return false;
				}
				printFunctionReturn(fn_fail);
			}
			
			if(!verifyButtonStatus("BaselineLibrary_AddBaselineButton", "enabled")) {
				printError("Failed verifyButtonStatus for BaselineLibrary_AddBaselineButton.");
			}
			
			if(!WaitForBaselineInventoryToComplete()) {
				printError("WaitForBaselineInventoryToComplete failed.");
				printFunctionReturn(fn_fail);		
				return false;
			}
			captureScreenShot();
		}
		catch (IOException e) {
			printError("Exception occurred while adding UNC baseline.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Methods related to screen : BL Create Custom
	public static boolean createCustomBaseline() {
		printLogs("Calling createCustomBaseline");
		String [] hpsumVerBuild = getHpsumVerBuild();
		String [] SppVerBuild = getSppVerBuild(); 
		//String cmdExec = "";
		String outputLocation = "";
		String verText = "";
		int count = 0, maxTime = 270;
		try {
			// Set output location and Version name
			if(currentOsName.contains("windows")) {
				outputLocation = "C:/SPP/Custom-Baseline";
				verText = "W";
			}
			else {
				outputLocation = "/root/Desktop/SPP/Custom-Baseline";
				verText = "L";
			}
			
			// 1. Go to Create Custom Baseline Page
			if(!guiSetPage("CreateCustomBaseline")) {
				appendHtmlComment("Failed to click on Create custom option under Action dropdown");
				printFunctionReturn(fn_fail);
				return false;
			}
			sleep(2);
			
			// 2. Specify description
			String descText = currentOsName + "_" + hpsumVerBuild[0] + "_" + hpsumVerBuild[1] + "_" + SppVerBuild[0] + "_" + SppVerBuild[1];
			
			if (!sendKeysByXpath("BL_CBL_TextBox_Description", descText)) {
				printError("sendKeysByXpath BL_CBL_TextBox_Description failed.");
			}
			sleep(2);
			
			// 3. Specify version text
			if (!sendKeysByXpath("BL_CBL_TextBox_Version", verText)) {
				printError("sendKeysByXpath BL_CBL_TextBox_Version failed.");
			}
			sleep(2);

			// 4. Set the output location by creating a new one and emptying it
			// if folder exists clear the contents, if not create one.
			File folder = new File(outputLocation);
			if(folder.exists())
			{
				printLogs("Folder exists, clearing the contents");
				for (File file : folder.listFiles()) {
					FileDeleteStrategy.FORCE.delete(file);
				}
			} 
			else {
				printLogs("Folder doesn't exists, creating folder");
				folder.mkdir();
			}
			
			// 5. Specify the Output location
			if(!sendKeysByXpath("BL_CBL_TextBox_OutLoc", outputLocation)) {
				printError("sendKeysByXpath BL_CBL_TextBox_OutLoc failed.");
				captureScreenShot();
				appendHtmlComment("Failed to send the key to Output Location field");
				printFunctionReturn(fn_fail);
				return false;
			}
			sleep(2);
			
			// 6. Check the Bootable iso creation checkbox.
			if(!clickByXpath("BL_CBL_CheckBox_MakeBootableIso")) {
				printError("Failed to click on BL_CBL_CheckBox_MakeBootableIso");
				captureScreenShot();
				appendHtmlComment("Failed to set the Bootable ISO checkbox");
				printFunctionReturn(fn_fail);
				return false;
			}
			sleep(2);
			// 7. If linux, check Create Non-UEFI bootable ISO checkbox.
			
			if(!System.getProperty("os.name").contains("Win") && checkElementPresenceByXpath("BL_CBL_CheckBox_Non-UEFI_bootableISO")) {
				if(!clickByXpath("BL_CBL_CheckBox_Non-UEFI_bootableISO")) {
				printError("Failed to click on BL_CBL_CheckBox_MakeBootableIso");
				captureScreenShot();
				appendHtmlComment("Failed to click on Create Non-UEFI bootable ISO checkbox");
				printFunctionReturn(fn_fail);
				return false;
				}
			}
			else {
				printError("This is a Windows system, hence BL_CBL_CheckBox_Non-UEFI_bootableISO checkbox is not present.");
			}
			sleep(2);
			
			// 7. Input the bootable iso location in SPP
			if(!sendKeysByXpath("BL_CBL_TextBox_ExtractSourceIsoLoc", getSppLocation())) {
				printError("sendKeysByXpath BL_CBL_TextBox_ExtractSourceIsoLoc failed.");
			}
			captureScreenShot();
			sleep(2);
			
			// 8. Check how many Baseline exists and select it if not selected.
			int blCount = getCblPageBlCount();
			String toggleXpath1 = OR.getProperty("BL_CBL_Table_Baselines") + "/tbody/tr[";
			String toggleXpath2 = "]/td[1]/a";
			
			for (int i = 1; i <= blCount ; i++) {
				if (!isCheckboxChecked(toggleXpath1 + i + toggleXpath2)) {
					if(!clickByActualXpath(toggleXpath1 + i + toggleXpath2)) {
						printError("Failed to click on " + toggleXpath1 + i + toggleXpath2);
						printFunctionReturn(fn_fail);
					}
				}
			}
			
			// 9. Click on Apply Filters button.
			if(!clickByXpath("BL_CBL_Button_ApplyFilters")) {
				sleep(15);
				printError("Failed to click on BL_CBL_Button_ApplyFilters");
				captureScreenShot();
				appendHtmlComment("Failed to click on Apply Filters button");
				printFunctionReturn(fn_fail);
				return false;
			}
			sleep(20);
			
			// 10. Click on the button
			if(checkElementPresenceByXpath("BL_CBL_PopUp_FilterComps")){
				printLogs("Waiting for filtering to complete.");
				sleep(20);
			}
			if(!getTextByXpath("BL_CBL_Text_BottomMsg").contains("Completed retrieving components") && verifyButtonStatus("BaselineLibrary_CreateCustomBaselineCreateISOButton","enabled"));
			{
				if(!clickByXpath("BaselineLibrary_CreateCustomBaselineCreateISOButton")) {
				printError("Failed to click on BaselineLibrary_CreateCustomBaselineCreateISOButton");
				captureScreenShot();
				appendHtmlComment("Failed to click on Create ISO button");
				printFunctionReturn(fn_fail);
				return false;
			}
			}
			sleep(20);
			captureScreenShot();
			
			// 11. Click on the bottom message.
			if (!clickByXpath("BL_CBL_Text_BottomMsg")) {
				printError("Failed to click on BL_CBL_Text_BottomMsg");
				printFunctionReturn(fn_fail);
				captureScreenShot();
			}
			captureScreenShot();
			
			// 12. Wait for the text "After ISO creation completes"
			// Msg before 20150406: "After ISO creation completes"
			// Msg on 20150406: Baseline has been saved successfully. ISO creation was successful. Baseline has been added successfully.
			String taskCompleteMsg = "successful";
			printLogs(getTextByXpath("BL_CBL_Text_BottomMsg"));
			
			while (!getTextByXpath("BL_CBL_Text_BottomMsg").contains(taskCompleteMsg)) {
				sleep(10);
				printLogs(getTextByXpath("BL_CBL_Text_BottomMsg"));
				/*if(getTextByXpath("BaselineLibrary_CreateCustomBaselineReviewTextWhole").contains("Copying") || 
						getTextByXpath("BaselineLibrary_CreateCustomBaselineReviewTextWhole").contains("Removing")) {
					continue;
				}*/
				count ++;
				if(count > maxTime) {
					captureScreenShot();
					String errorMsg = "Custom Baseline creation took longer than " + ((maxTime * 10)/60) + "mins to finish";
					printError(errorMsg);
					//appendHtmlComment(errorMsg);
					//captureScreenShot();
					printFunctionReturn(fn_fail);
					return false;
				}
			}
			captureScreenShot();
			//printLogs(getTextByXpath("BaselineLibrary_CreateCustomBaselineReviewTextWhole"));
			
			// 13. Close the CBL page.
			if (!closeCblPageUsingCloseButton()) {
				printError("Failed to close the CBL page.");
			}
			printLogs("Created Custom baseline in the folder " + outputLocation);
			appendHtmlComment("Created Custom baseline in " + currentOsName + " and copied to the folder " + outputLocation);
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Exception occurred while creating the Custom Baseline");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// cblApplyFilteriLO
	// This method applies iLO filter and creates two baselines for each type of component, like exe and .rpm	
	public static boolean cblApplyFilteriLO(){
		printLogs("Calling cblApplyFilteriLO");
		
		String [] hpsumVerBuild = getHpsumVerBuild();
		String [] SppVerBuild = getSppVerBuild(); 
		//String cmdExec = "";
		String outputLocation = "";
		String verText = "";
		String sLoc = "custom_exe";
		
		boolean bStatus = true;
		
		BaselinePage BLPage = null;
		
		try {
			// Set output location and Version name
			if(currentOsName.contains("windows")) {
				outputLocation = "C:/SPP/";				
				verText = "W";
			}
			else {
				outputLocation = "/root/Desktop/SPP/";				
				verText = "L";
			}
						
			// 1. Go to Create Custom Baseline Page
			
			if(!guiSetPage("BaselineLibrary")) {
				throw new Exception("Failed to set page.");
			}
			
			if(!WaitForBaselineInventoryToComplete()) {
				throw new Exception("Failed to wait for baseline inventory.");
			}
			captureScreenShot();
			
			if(!guiSetPage("CreateCustomBaseline")) {
				throw new Exception("Failed to set page.");
			}
			
			sleep(2);
			captureScreenShot();
			
			// 2. Specify description
			BLPage = BaselinePage.getInstance();			
			String descText = currentOsName + "_" + hpsumVerBuild[0] + "_" +
								hpsumVerBuild[1] + "_" + SppVerBuild[0] + "_" + SppVerBuild[1];
			
			// Wait for populating data.
			if(BLPage.CBLMessageBar.Exists()){
				if(!BLPage.CBLMessageBar.WaitForElementNotExists(120)){
					printLogs("Failed to wait for 'Populate data' Text to disappear. Trying to continue still...");
				}					
			}
			
			if (!BLPage.CustomBaselineDesc.TypeKeys(descText)) {
				throw new Exception("Failed to type in description.");
			}
			sleep(2);
			captureScreenShot();
			
			// 3. Specify version text
			if (!BLPage.CustomBaselineVer.TypeKeys(verText)) {
				printError("Failed to type in version.");
			}
			sleep(2);
			captureScreenShot();
			
			// 4. Set the output location by creating a new one and emptying it
			// if folder exists clear the contents, if not create one.
			File folder = new File(outputLocation + sLoc);
			if(folder.exists())
			{
				printLogs("Folder exists, clearing the contents");
				for (File file : folder.listFiles()) {
					FileDeleteStrategy.FORCE.delete(file);
				}
			} 
			else {
				printLogs("Folder doesn't exists, creating folder");
				folder.mkdir();
			}
			
			// 5. Specify the Output location
			if(!BLPage.CustomBaselineLoc.TypeKeys(sLoc)) {
				throw new Exception("Failed to send the key to Output Location field");
				
			}
			sleep(2);
			captureScreenShot();
			
			// Scroll the page down to bring the elements into view.
			SelUtil.scrollPageDown();
			
			//6. Select Firmware option.
			BaselinePage.SelectComponentType(CompType.Firmware);
			
			BLPage.CBLAdvancedFilter.ScrollIntoView();
			
			//7. Select Advanced filter option.
			if(!BLPage.CBLAdvancedFilter.Click()){
				throw new Exception("Failed to Click on Advanced Filter");
			}
			
			sleep(2);
			captureScreenShot();
			
			//8. Select the Advanced filter Enclosure
			if(!BLPage.CBLAdvFilEnclosure.Click()){
				throw new Exception("Failed to Click on Enclosure filter");
			}
			
			sleep(2);
			//9. Select iLO			
			if(!BLPage.CBLILOFilter.Select()){
				throw new Exception("Failed to Select iLO Checkbox");
			}
			sleep(2);
			captureScreenShot();
			
			//10. Click on Apply Filter
			if(!BLPage.CBLApplyFilter.Click()){
				throw new Exception("Failed to Select iLO Checkbox");
			}
			sleep(5);
			captureScreenShot();
			
			while(bStatus){
				if(!BLPage.CBLApplyFilterDialog.Exists()){
					bStatus = false;
					break;
				}
				sleep(2);
			}
			
			// Check if components retrieve is successful
			if(!BLPage.CBLMessageBar.GetText().contains("Completed")){
				throw new Exception("Failed retrieve components");
			}
			
			// Select deselect All
			if(!BLPage.CBLDeselectAll.Select()){
				throw new Exception("Failed to check 'Deselect all'");
			}
			
			sleep(5);
			captureScreenShot();
			//11. Search for .exe components
			if(!BLPage.CBLSearchBox.TypeKeys(".exe")){
				throw new Exception("Failed type in search box");
			}
			
			sleep(5);
			captureScreenShot();
			
			//12. Get the Table contents.
			BLPage.CBLApplyFilterTable.GetTableContent();
			
			// Select select All
			if(!BLPage.CBLSelectAll.Select()){
				throw new Exception("Failed to check 'Select all'");
			}
			
			// 13. Click on Save Baseline.
			if(!BLPage.CBLSaveBaseline.Click()){
				throw new Exception("Failed to click SaveBaseline.");
			}
			
			sleep(10);
			captureScreenShot();
			// Check if components retrieve is successful
			if(!BLPage.CBLMessageBar.GetText().contains("The custom baseline has been saved")){
				throw new Exception("Failed save baseline");
			}
			
			// 14. Search for .rpm
			if(!BLPage.CBLDeselectAll.Select()){
				throw new Exception("Failed to check 'Deselect all'");
			}
			
			folder = new File(outputLocation + "custom_rpm");
			if(folder.exists())
			{
				printLogs("Folder exists, clearing the contents");
				for (File file : folder.listFiles()) {
					FileDeleteStrategy.FORCE.delete(file);
				}
			} 
			else {
				printLogs("Folder doesn't exists, creating folder");
				folder.mkdir();
			}
			
			if(!BLPage.CustomBaselineLoc.TypeKeys(sLoc)) {
				throw new Exception("Failed to send the key to Output Location field");
				
			}
			sleep(2);
			captureScreenShot();
			
			if(!BLPage.CBLSearchBox.TypeKeys(".rpm")){
				throw new Exception("Failed type in search box");
			}
			
			sleep(5);
			captureScreenShot();
			// Select select All
			if(!BLPage.CBLSelectAll.Select()){
				throw new Exception("Failed to check 'Select all'");
			}
			
			if(!BLPage.CBLSaveBaseline.Click()){
				throw new Exception("Failed to click SaveBaseline.");
			}
			
			sleep(10);
			captureScreenShot();
			// Check if components retrieve is successful
			if(!BLPage.CBLMessageBar.GetText().contains("The custom baseline has been saved")){
				throw new Exception("Failed save baseline.");
			}
			
			if(!BLPage.CBLClose.Click()){
				throw new Exception("Failed to close Custom Baseline page.");
			}
			printLogs("Successfully created 2 baseline libraries for iLO firmware.");
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Exception occurred while creating the Custom Baseline");
			printLogs("Error returned: " + t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;		
		
	}
	
	public static boolean closeCblPageUsingCloseButton() {
		printLogs("Calling closeCblPageUsingCloseButton");
		
		try {
			// Click on the Close button
			if (!clickByXpath("BL_CBL_Button_Close")) {
				printError("Failed to click BL_CBL_Button_Close");
				printFunctionReturn(fn_fail);
				return false;
			}
			captureScreenShot();
			
			// If the Confirm close window appears - Close it
			// NOTE: This window will not appear if the Create operation has started/finished.
			if (checkElementPresenceByXpath("BL_CBL_CloseConfirm_Window", 15)) {
				captureScreenShot();
				if (!clickByXpath("BL_CBL_CloseConfirm_Yes")) {
					printError("Failed to click BL_CBL_CloseConfirm_Yes");
					printFunctionReturn(fn_fail);
					return false;
				}
			}
			captureScreenShot();
			
			// Verify that control reaches Baseline Library page.
			if (!checkElementPresenceByXpath("BaselineLibrary_Heading")) {
				printError("Control did not reach BL page after closing the CBL page.");
				printFunctionReturn(fn_fail);
			}
			else {
				printLogs("Success: Create CBL page closed and control reached BL page.");
			}
			captureScreenShot();
		}
		catch (IOException e) {
			printError("Exception occurred while closing CBL page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Select option from CSM Definition XML File drop-down on CBL page
	public static boolean createCblSelectCsmDefXmlFileDdOption(int indexOfOptionToSelect) {
		printLogs("Calling createCblSelectCsmDefXmlFileDdOption with value: " + indexOfOptionToSelect);
		
		int noOfOptions = 2;
		String[] dropDownOptionsLocType = new String [noOfOptions];
		dropDownOptionsLocType[0] = "Latest MatrixConfig.xml from local";
		dropDownOptionsLocType[1] = "Latest MatrixConfig.xml from hp.com";
		
		if (indexOfOptionToSelect > noOfOptions) {
			printError("Max number of options: " + noOfOptions + "Passed value: " + indexOfOptionToSelect);
			printLogs("Please correct the index of the option that needs to be selected.");
			printFunctionReturn(fn_fail);
			return false;
		}
		else {
			printLogs("Selecting option at index : " + indexOfOptionToSelect + " : " + dropDownOptionsLocType[indexOfOptionToSelect-1]);
		}
		
		try {
			String xPathDdArrow = OR.getProperty("BL_CBL_DropDown_CsmDefXmlFile") + "/span[2]";
			//String xPathDdItem = OR.getProperty("BL_CBL_DropDown_CsmDefXmlFile") + "/span[1]";
			
			// Click on the drop down
			if(!clickByActualXpath(xPathDdArrow)){
				printError("Failed to click on drop-down arrow.");
				printFunctionReturn(fn_fail);
				return false;
			}
			captureScreenShot();
			
			String xPathOfDropDownAfterClicking = OR.getProperty("Common_DropDownOptions");
			String xPathOfOptionToSelect = xPathOfDropDownAfterClicking + "/li[" + indexOfOptionToSelect + "]/a";
			
			String textOfOption = getTextByActualXpath(xPathOfOptionToSelect);
			
			// Match the text of the drop-down option.
			if(!dropDownOptionsLocType[indexOfOptionToSelect-1].equalsIgnoreCase(textOfOption)) {
				printError("The text of the drop-down option " + indexOfOptionToSelect + " has changed. Report bug.");
				printLogs("Expected Text: " + dropDownOptionsLocType[indexOfOptionToSelect-1]);
				printLogs("Actual Text: " + textOfOption);
			}			
			
			// Select the option
			if(!clickByActualXpath(xPathOfOptionToSelect)) {
				printError("Failed to select the option : " + xPathOfOptionToSelect);
			}
			else {
				printLogs("Successfully selected option with index: " + indexOfOptionToSelect);
			}
			captureScreenShot();
		}
		catch (IOException e) {
			printError("Exception occurred while selecting option " + indexOfOptionToSelect + " from CSM Definition XML File drop-down on CBL page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Select option from Proxy Options drop-down on CBL page
	// (after selecting hp.com option from drop-down CSM Definition XML File)
	public static boolean createCblSelectProxyOptionsDdOption(int indexOfOptionToSelect) {
		printLogs("Calling createCblSelectProxyOptionsDdOption with value: " + indexOfOptionToSelect);
		
		int noOfOptions = 3;
		String[] dropDownOptionsLocType = new String [noOfOptions];
		dropDownOptionsLocType[0] = "No Proxy Needed";
		dropDownOptionsLocType[1] = "Proxy server";
		dropDownOptionsLocType[2] = "Proxy script";
		
		if (indexOfOptionToSelect > noOfOptions) {
			printError("Max number of options: " + noOfOptions + "Passed value: " + indexOfOptionToSelect);
			printLogs("Please correct the index of the option that needs to be selected.");
			printFunctionReturn(fn_fail);
			return false;
		}
		else {
			printLogs("Selecting option at index : " + indexOfOptionToSelect + " : " + dropDownOptionsLocType[indexOfOptionToSelect-1]);
		}
		
		try {
			String xPathDdArrow = OR.getProperty("BL_CBL_DropDown_ProxyOptions") + "/span[2]";
			//String xPathDdItem = OR.getProperty("BL_CBL_DropDown_CsmDefXmlFile") + "/span[1]";
			
			// Click on the drop down
			if(!clickByActualXpath(xPathDdArrow)){
				printError("Failed to click on drop-down arrow.");
				printFunctionReturn(fn_fail);
				return false;
			}
			captureScreenShot();
			
			String xPathOfDropDownAfterClicking = OR.getProperty("Common_DropDownOptions");
			String xPathOfOptionToSelect = xPathOfDropDownAfterClicking + "/li[" + indexOfOptionToSelect + "]/a";
			
			String textOfOption = getTextByActualXpath(xPathOfOptionToSelect);
			
			// Match the text of the drop-down option.
			if(!dropDownOptionsLocType[indexOfOptionToSelect-1].equalsIgnoreCase(textOfOption)) {
				printError("The text of the drop-down option " + indexOfOptionToSelect + " has changed. Report bug.");
				printLogs("Expected Text: " + dropDownOptionsLocType[indexOfOptionToSelect-1]);
				printLogs("Actual Text: " + textOfOption);
			}
			
			// Select the option
			if(!clickByActualXpath(xPathOfOptionToSelect)) {
				printError("Failed to select the option : " + xPathOfOptionToSelect);
			}
			else {
				printLogs("Successfully selected option with index: " + indexOfOptionToSelect);
			}
			captureScreenShot();
		}
		catch (IOException e) {
			printError("Exception occurred while selecting option " + indexOfOptionToSelect + " from Proxy Options drop-down on CBL page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	
	
	
	
	// Methods related to screen : BL Reports
	
	
	
	// Methods related to screen : BL Validate
	
	
	
	
	
	
	
}
