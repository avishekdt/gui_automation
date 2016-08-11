package gui.common.base;

import gui.common.util.ErrorUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.openqa.selenium.By;

public class CommonHpsum extends SelUtil{
	
	/*
	 * METHODS
	 * clickLinkOnMainMenu
	 * clickLinkOnMsgBar
	 * testCleanupAndReporting
	 * getFilesListInFolder
	 * performTestCleanup
	 * performTestCleanup
	 * performTestSetup
	 * performTestSetup
	 * testCleanupAndReporting 
	 */
		
	// Click by clickLinkOnMsgBar
	public static boolean clickLinkOnMsgBar(String xPathKey) {
		printLogs("Calling clickLinkOnMsgBar with values: " + xPathKey);
	
		try {
			printLogs("Clicking link on Message Bar : " + xPathKey);
			
			// Expand the Message Bar to make the link visible
			// Click on the message bar - Common_PageMsgBar
			if(!clickByXpath("Common_PageMsgBar")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			printLogs("Expanded the Message Bar.");
			
			// Click on the link
			if(!clickByXpath(xPathKey)) {
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while clicking.");
			printFunctionReturn(fn_fail);
			return false;
			//throw new Exception("Exception occurred while clicking.");
		}
		printLogs("Clicked link on Message Bar" + xPathKey);
		printFunctionReturn(fn_pass);
		return true;
	}
	

	// Click the specified option on the Main Menu
	public static boolean clickLinkOnMainMenu(String LinkOnMainMenu) {
		printLogs("Calling clickLinkOnMainMenu with values: " + LinkOnMainMenu);
	
		String xPathKeyMainMenu = "MainMenu";
		//boolean mainMenuVisible = false;
		
		try {
			printLogs("Clicking " + LinkOnMainMenu + " on the Main menu.");
			
			// xPath of Main Menu
			//String mainMenuXpath = OR.getProperty("HomePage_MenuList");
			
			// If the Main Menu is not visible then bring it up
			// Menu-Visible    : class="hp-global hp-menu-boundary hp-inactive"
			// Menu-Not-Visible: class="hp-global hp-menu-boundary"
			if(driver.findElement(By.xpath(OR.getProperty(xPathKeyMainMenu))).getAttribute("class").contains("hp-global hp-menu-boundary hp-inactive")) {
				//mainMenuVisible = true;
				printLogs("Main Menu already active.");
			}
			else if(!clickByXpath("HomePage_MenuLabel")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Click on the link
			if(!clickByLinkText(LinkOnMainMenu)) {				
				printFunctionReturn(fn_fail);				
				return false;
			}
			else {
				printLogs("Successfully clicked " + LinkOnMainMenu + " on the Main menu.");
			}
			sleep(5);
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while clicking the link on Main Menu.");
			printFunctionReturn(fn_fail);			
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	
	// performTestSetup
	// 
	// Setup::
	// 1. Kill the HPSUM Service, if already running, and clean the HPSUM logs and start the service. 
	// 2. Open the Browser and Set Timeouts
	// 3. Go to the Product URL
	// 4. Login
	//
	public static boolean performTestSetup() {
		printLogs("Calling performTestSetup");
		
		try {
			// 1. Kill the HPSUM Service, if already running, and clean the HPSUM logs and start the service.
			if(!hpsumKillCleanStart()) {
				printError("hpsumKillCleanStart failed.");
			}
			
			// 2. Open the Browser and Set Timeouts
			if(!openBrowserAndSetTimeouts()) {
				appendHtmlComment("Failed to open the browser.");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// 3. Go to the Product URL
			if(!openUrl()) {
				appendHtmlComment("Failed to open the URL");
				printFunctionReturn(fn_fail);
				return false;
			}			
			captureScreenShot();
			
			// 4. Login
			if(!Login.login()) {
				appendHtmlComment("Failed to login to HPSUM");
				printFunctionReturn(fn_fail);
				return false;
			}
			captureScreenShot();
		}
		catch (IOException e) {
			printError("Exception occurred while performing setup.");
			e.printStackTrace();
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// performTestSetup
	// 
	// Setup::
	// 1. Kill the HPSUM Service, if already running, and clean the HPSUM logs and start the service. 
	// 2. Open the Browser and Set Timeouts
	// 3. Go to the Product URL
	// 4. Login
	//
	public static boolean performTestSetup(String ftpNumber) {
		printLogs("Calling performTestSetup with values: " + ftpNumber);
			
		try {
			// 1. Kill the HPSUM Service, if already running, and clean the HPSUM logs and start the service.
			if(!hpsumKillCleanStart(ftpNumber)) {
				printError("hpsumKillCleanStart failed.");
			}
				
			// 2. Open the Browser and Set Timeouts
			if(!openBrowserAndSetTimeouts()) {
				appendHtmlComment("Failed to open the browser.");
				printFunctionReturn(fn_fail);
				return false;
			}
				
			// 3. Go to the Product URL
			if(!openUrl()) {
				appendHtmlComment("Failed to open the URL");
				printFunctionReturn(fn_fail);
				return false;
			}			
			captureScreenShot();
				
			// 4. Login
			if(!Login.login()) {
				appendHtmlComment("Failed to login to HPSUM");
				printFunctionReturn(fn_fail);
				return false;
			}
			captureScreenShot();
		}
		catch (IOException e) {
			printError("Exception occurred while performing setup.");
			e.printStackTrace();
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	
	// performTestCleanup
	public static boolean performTestCleanup() {
		printLogs("Calling performTestCleanup");
		try {
			// Close the Browser.
			if(!closeBrowser()) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Kill HP SUM Service.
			if(!killHpsumService()) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			sleep(3);
			printFunctionReturn(fn_pass);
			return true;
		}
		catch (Exception e) {
			printError("Exception occurred while performing cleanup");
			e.printStackTrace();
			printFunctionReturn(fn_fail);
			return false;
		}
	}
	
	public static boolean performTestCleanup(String testName) {
		printLogs("Calling performTestCleanup with values: " + testName);
		try {
			// Get the values from config.properties to check if cleanup needs to be done
			if (CONFIG.getProperty("performExitCleanup").contains("0")) {
				printLogs("performExitCleanup flag found to be 0 in the config file. Skipping the test cleanup.");
				printFunctionReturn(fn_pass);
				return true;
			}
			else {
				if (!performTestCleanup()) {
					printFunctionReturn(fn_fail);
					return false;
				}
			}
	
			// Check if Gatherlogs needs to be executed.
			if (CONFIG.getProperty("gatherLogs").contains("0")) {
				printLogs("gatherLogs flag found to be 0 in the config file. Skipping gatherlogs.");
			}
			else {
				if(!executeGatherLogs(testName)) {
					printFunctionReturn(fn_fail);
					return false;
				}
			}
			
			printLogs("Success: performTestCleanup");
			printFunctionReturn(fn_pass);
			return true;
		}
		catch (Exception e) {
			printError("Exception occurred while performing cleanup.");
			e.printStackTrace();
			printFunctionReturn(fn_fail);
			return false;
		}
	}
	
	public static boolean testCleanupAndReporting(String testName, String result, String execTime) {
		printLogs("Calling testCleanupAndReporting with values: " + testName + ", " + result + ", " + execTime);
		try {
			if (!(result.equalsIgnoreCase("SKIP") || result.equalsIgnoreCase("MANUAL"))) {
				if(!performTestCleanup(testName)) {
					printError("Failed to perform cleanup.");
				}
			}
			if(!testFinalize(testName, result, execTime)) {
				printError("Failed to write the final test result and send email.");
			}
			printFunctionReturn(fn_pass);
			return true;
		}
		catch (Exception e) {
			printError("Exception occurred in method testCleanupAndReporting.");
			e.printStackTrace();
			printFunctionReturn(fn_fail);
			return false;
		}
	}
	
	// getFilesListInFolder
	public static ArrayList<String> getFilesListInFolder(String folderPath, String fileType) {
		printLogs("Calling getFilesListInFolder with values: " + folderPath + ", " + fileType);
		
		File srcDir = new File(folderPath); //source directory
		ArrayList<String> selectedFiles = new ArrayList<String>();
		File fileList[];
		String fileName = "";
		
		// Get list of all the files in the directory
		fileList = srcDir.listFiles();
		
		// Select the only files with fileType
		for (int i = 0 ; i < fileList.length ; i++) {
			fileName = fileList[i].getName();
			
			if(fileName.contains(fileType)) {
				selectedFiles.add(fileName);
			}
		}
		printFunctionReturn(fn_pass);
		return(selectedFiles);
	}
	
	// getElementsInDropDown
	// This method will return the list of elements in a dropdown
	public static ArrayList<String> getElementsInDropDown(String xPathKeyOfDropDown) {
		printLogs("Calling getElementsInDropDown with values: " + xPathKeyOfDropDown);
		ArrayList<String> dropDownItems = new ArrayList<String>();
		
		try {
			// Click on drop-down
			if(!SelUtil.clickByXpath(xPathKeyOfDropDown)){
				printError("Unable to click on drop-down");
			}
			captureScreenShot();
			
			// Get the xPath of Common_DropDownOptions
			String xPathOfDropDownAfterClicking = OR.getProperty("Common_DropDownOptions");
			String xPathOfItemsInDropDownAfterClicking = xPathOfDropDownAfterClicking + "/li";
			int itemCount = driver.findElements(By.xpath(xPathOfItemsInDropDownAfterClicking)).size();
			
			printLogs("Number of items in drop-down: " + itemCount);
			String item = "";
			
			for (int i = 1 ; i <= itemCount ; i++) {
				item = SelUtil.getTextByActualXpath(xPathOfItemsInDropDownAfterClicking + "[" + i + "]/a");
				dropDownItems.add(item);
				printLogs("Item: " + i + ": " + item);
			}
		
		}
		catch (IOException e) {
			printError("Exception occurred while getting drop-down elements.");
			e.printStackTrace();
			printFunctionReturn(fn_fail);
			return null;
		}
		printFunctionReturn(fn_pass);
		return (dropDownItems);
	}
	
	

}
