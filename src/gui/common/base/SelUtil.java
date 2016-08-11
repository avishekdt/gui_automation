package gui.common.base;

import gui.common.util.ErrorUtil;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class SelUtil extends TestBase{
	
	/* METHODS
	 * checkElementPresenceByXpath - 2 Versions
	 * clickByXpath
	 * clickById
	 * clickByLinkText
	 * closeBrowser
	 * getTableColumnCount
	 * getTableRowCount
	 * getTextByXpath
	 * getValueByXpath
	 * replaceNodeIdDummyTextByActualId
	 * openBrowserAndSetTimeouts
	 * openUrl
	 * sendKeysByXpath
	 * setTimeOuts
	 * sortTable
	 * verifyButtonStatus - 2 VERSIONS
	 * verifyCheckboxStatus
	 * isCheckboxChecked
	 * verifyElementPresenceAndText
	 * verifyPage
	 * verifyTableContents
	 * verifyText
	 * waitForNoElementByXpath
	 * waitForPage
	 * getAttributeByXpath
	 * scrollPageUp
	 * scrollPageDown
	 * waitForElementByXpath
	 */
	
	
	// checkElementPresenceByActualXpath
	public static boolean checkElementPresenceByActualXpath(String xPathString) {
		return checkElementPresenceByXpath(xPathString);
	}
	
	// checkElementPresenceByXpath
	// Verify the presence of an element whose xPathKey is passed.
	public static boolean checkElementPresenceByXpath(String xPathKey) {
		printLogs("Calling checkElementPresenceByXpath with values: " + xPathKey);
		String xPathValue = "";
		
		try {
			// Get xPath value if xPathKey is passed.
			if (xPathKey.startsWith("//")) {
				xPathValue = xPathKey;
			}
			else {
				xPathValue = OR.getProperty(xPathKey);
			}
			
			int count = 0;
			count = driver.findElements(By.xpath(xPathValue)).size();
			
			if (count > 0) {
				printLogs("Found the element: " + xPathKey);
				printFunctionReturn(fn_pass);
				return true;
			}
			else {
				printError("Element not found: " + xPathKey);
				printFunctionReturn(fn_pass);
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Exception occurred while checking element presence: " + xPathKey);
			printFunctionReturn(fn_fail);
			printLogs("Exception returned: "+ t.getMessage());
			return false;
		}
	}
	
	// checkElementPresenceByXpath
	// Verify the presence of an element whose xPathKey is passed.
	public static boolean checkElementPresenceByXpath(String xPathKey, boolean checkDisplay) {
		printLogs("Calling checkElementPresenceByXpath with values: " + xPathKey);
		String xPathValue = "";
		
		try {
			// Get xPath value if xPathKey is passed.
			if (xPathKey.startsWith("//")) {
				xPathValue = xPathKey;
			}
			else {
				xPathValue = OR.getProperty(xPathKey);
			}
			
			int count = 0;
			count = driver.findElements(By.xpath(xPathValue)).size();
			
			if (count > 0) {
				printLogs("Found the element: " + xPathKey);
				printFunctionReturn(fn_pass);
				return true;
			}
			else {
				printError("Element not found: " + xPathKey);
				printFunctionReturn(fn_pass);
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Exception occurred while checking element presence: " + xPathKey);
			printLogs("Exception returned: "+ t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
	}	
	
	// checkElementPresenceByXpath - with polling interval
	// Verify the presence of an element whose xPathKey is passed and max wait for maxWaitTime for element to appear.
	public static boolean checkElementPresenceByXpath(String xPathKey, int maxWaitTime) {
		printLogs("Calling checkElementPresenceByXpath with values: " + xPathKey + ", " + maxWaitTime);
		
		String xPathValue = "";
		try {
			xPathValue = OR.getProperty(xPathKey);
			
			// Modify the implicit wait time to maxWaitTime
			driver.manage().timeouts().implicitlyWait(maxWaitTime, TimeUnit.SECONDS);
			printLogs("Setting the implicit wait to " + maxWaitTime + "s");
			
			int count = 0;
			count = driver.findElements(By.xpath(xPathValue)).size();
			
			if (count>0) {
				printLogs("Found the element: " + xPathKey);
				printFunctionReturn(fn_pass);
				return true;
			}
			else {
				printLogs("Element not found: " + xPathKey);
				printFunctionReturn(fn_pass);
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Exception occurred Element Not Found with xPath : " + xPathValue);
			printLogs("Exception returned: "+ t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
		finally {
			driver.manage().timeouts().implicitlyWait(iElementDetectionTimeout, TimeUnit.SECONDS);
			printLogs("Reset the implicit wait to default.");
		}
	}
	
	// clickByActualXpath
	public static boolean clickByActualXpath(String xPath) {
		return clickByXpath(xPath);
	}
	
	// clickByXpath
	public static boolean clickByXpath(String xPathKey) {
		printLogs("Calling clickByXpath with values: " + xPathKey);
		String xPathValue = "";
		
		try {
			// Get xPath value if xPathKey is passed.
			if (xPathKey.startsWith("//")) {
				xPathValue = xPathKey;
			}
			else {
				xPathValue = OR.getProperty(xPathKey);
			}
			
			// Verify element presence.
			if(!checkElementPresenceByXpath(xPathValue)) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// If this is a button - verify if it is enabled.
			if(driver.findElement(By.xpath(xPathValue)).getAttribute("class").contains("hp-button")) {
				// Verify if Button is Enabled.
				if(driver.findElement(By.xpath(xPathValue)).getAttribute("class").contains("hp-disabled")) {
					printLogs("Button found as disabled.");
					printFunctionReturn(fn_fail);					
					return false;
				}
			}
			
			// Click.
			driver.findElement(By.xpath(xPathValue)).click();
			sleep(2);
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while clicking xpath: " + xPathKey);
			printLogs("Exception returned: "+ t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}	
	}
	
	// Click by element by id.
	public static boolean clickById(String idKey) {
		printLogs("Calling clickById with values: " + idKey);
		
		try {
			//printLogs("Clicking " + idKey);
			//table[@id='hpsum-otu-installables-N1localhost-table']/thead/tr/td[1]
			// Verify element presence.
			//if(!checkElementPresenceByXpath(xPathKey)) {
				//printFunctionReturn(fn_fail);
				//return false;
			//}
			
			// If this is a button - verify if it is enabled.
			if(driver.findElement(By.id(OR.getProperty(idKey))).getAttribute("class").contains("hp-button")) {
				// Verify if Button is Enabled.
				if(driver.findElement(By.id(OR.getProperty(idKey))).getAttribute("class").contains("hp-disabled")) {
					printLogs("Button found as disabled.");
					printFunctionReturn(fn_fail);					
					return false;
				}
			}
			
			// Click.
			driver.findElement(By.id(OR.getProperty(idKey))).click();
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while clicking by id.");
			printFunctionReturn(fn_fail);			
			return false;
		}
	}
	
	// clickByLinkText
	public static boolean clickByLinkText(String linkText) {
		printLogs("Calling clickByLinkText with values: " + linkText);
		
		try {
			// Click.
			driver.findElement(By.linkText(OR.getProperty(linkText))).click();
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while clicking link.");
			printFunctionReturn(fn_fail);			
			return false;
		}
	}
	
	// Closes the browser
	public static boolean closeBrowser() {
		printLogs("Calling closeBrowser");
		
		try {
			driver.quit();
			printLogs("Closed the Browser.");
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Exception e) {
			printException(e);
			printFunctionReturn(fn_fail);			
			return false;
		}
	}
	
	// Get the number of column in a table.
	public static int getTableColumnCount(String xPathKey) {
		printLogs("Calling getTableColumnCount with values: " + xPathKey);
		//printLogs("Getting column count of table " + xPathKey);
		
		int rowCount = 0;
		int colCount = 0;
		String xPathValue = "";
		String xPathValueBase = "";
		
		rowCount = getTableRowCount(xPathKey);
		
		if(rowCount == -1) {
			printFunctionReturn(fn_fail);
			return(-1);
		}
		if(rowCount == 0) {
			printLogs("No rows found in table: " + xPathKey);
			printLogs("No columns will exist.");
			printFunctionReturn(fn_pass);
			return(0);
		}
		else {
			printLogs(xPathKey + " row count = " + rowCount);
		}
		
		if (xPathKey.startsWith("//")) {
			xPathValueBase = xPathKey;
		}
		else {
			xPathValueBase = OR.getProperty(xPathKey);
		}
		
		if (rowCount == 1) {
			xPathValue = xPathValueBase + "/tr/td";
		}
		else {
			xPathValue = xPathValueBase + "/tr[1]/td";
		}
		
		try {
			colCount = driver.findElements(By.xpath(xPathValue)).size();
			printLogs(xPathKey + " col count = " + colCount);
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while counting the columns.");
			printFunctionReturn(fn_fail);			
			return(-1);
		}
		printFunctionReturn(fn_pass);
		return(colCount);
	}
	
	// Get the number of rows in a table.
	public static int getTableRowCount(String xPathKey) {
		printLogs("Calling getTableRowCount with values: " + xPathKey);
		
		int rowCount = 0;
		String xPathValue = "";
		
		// If the xPathKey is already the Actual xPath then getting the xPath from OR.properties is NOT needed.
		if (xPathKey.startsWith("//")) {
			xPathValue = xPathKey + "/tr";
		}
		else {
			xPathValue = OR.getProperty(xPathKey) + "/tr";
		}
		
		try {
			printLogs("Getting row count of table " + xPathValue);
			rowCount = driver.findElements(By.xpath(xPathValue)).size();
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Exception occurred while counting the rows.");
			printFunctionReturn(fn_fail);
			return(-1);
		}
		printFunctionReturn(fn_pass);
		return(rowCount);
	}
	
	// Get the text of the element whose xPath is provided.
	public static String getTextByActualXpath(String xPath) {
		return getTextByXpath(xPath);
	}
	
	// Get the text of the element whose xPath is provided.
	public static String getTextByXpath(String xPathKey) {
		//printLogs("Calling getTextByXpath with values: " + xPathKey);
		String sText = "";
		String xPathValue = "";
		
		try {
			// Get xPath value if xPathKey is passed.
			if (xPathKey.startsWith("//")) {
				xPathValue = xPathKey;
			}
			else {
				xPathValue = OR.getProperty(xPathKey);
			}
			
			sText = driver.findElement(By.xpath(xPathValue)).getText();
			//printFunctionReturn(fn_pass);
			return(sText);
		}
		catch(Throwable t) {
			// If this is for BaselineLibrary_InventoryMsg - there are chances that refresh is happening 
			// We will skip the error logging for such conditions.
			if (xPathKey.contains("BaselineLibrary_InventoryMsg")) {
				printLogs("Warning:: Did not find the BaselineLibrary_InventoryMsg. May be the message bar is refreshing");
				//printFunctionReturn(fn_fail);
				printLogs("Exception returned: "+ t.getMessage());
				return(sErrorString);
			}
			else {
				ErrorUtil.addVerificationFailure(t);
				printError("Exception occurred while getting the text: " + xPathKey);
				//printFunctionReturn(fn_fail);
				printLogs("Exception returned: "+ t.getMessage());
				return(sErrorString);
			}
		}
	}
	
	// Get the value/text of the element whose xPath is provided.
	public static String getValueByXpath(String xPathKey) {
		printLogs("Calling getValueByXpath with values: " + xPathKey);
		String sText = "";
		
		try {
			//printLogs("Getting value of " + xPathKey);
			sText = driver.findElement(By.xpath(OR.getProperty(xPathKey))).getAttribute("value");
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while getting the value");
			printFunctionReturn(fn_fail);			
			return(sErrorString);
		}
		printFunctionReturn(fn_pass);
		return(sText);
	}
	
	// replaceNodeIdDummyTextByActualId
	public static String replaceNodeIdDummyTextByActualId(String xPathActual, String remoteNodeIp) {
		printLogs("Calling createActualxPathOfTable with values: "+ xPathActual + ", " + remoteNodeIp);
		
		// EXAMPLE - //*[@id='hpsum-otu-installables-ID_OF_REMOTE_NODE-table']/tbody
		
		String nodeId = "";
		
		if (remoteNodeIp.toLowerCase().contains("localhost")) {
			nodeId = "N1" + "localhost";
		}
		else if(remoteNodeIp.toLowerCase().contains(":")){
			nodeId = "N1" + remoteNodeIp.replace(":", "h");
		}
		else {
			nodeId = "N1" + remoteNodeIp.replace(".", "h");
		}
		
		String xpathActual = xPathActual.replace(strToBeReplacedByNodeId, nodeId);
		printFunctionReturn(fn_pass);
		return(xpathActual);
	}
	
	// Open Browser - value from CONFIG File.
	public static boolean openBrowserAndSetTimeouts() {
		printLogs("Calling openBrowserAndSetTimeouts");
		
		try {
			String browserType = CONFIG.getProperty("browserType");
			printLogs("Opening Browser : " + browserType);
			browserType = browserType.toLowerCase();
			
			switch (browserType) {
			
			case "ie":
				printLogs("Setting browser driver: " + location_ieDriverServer);
				System.setProperty("webdriver.ie.driver", location_ieDriverServer);
				driver = new InternetExplorerDriver();
				break;
				
			case "chrome":
				System.setProperty("webdriver.chrome.driver", location_chromeDriver);
				driver = new ChromeDriver();
				break;
			
			default:
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("dom.max_chrome_script_run_time" , "0");
				profile.setPreference("dom.max_script_run_time" , "0");
				
				// Code to start Firefox from a set location.
				//File pathToBinary = new File("C:\\testing\\ff15\\firefox.exe");
				//FirefoxBinary binary = new FirefoxBinary(pathToBinary);
				//FirefoxDriver driver = new FirefoxDriver(binary, new FirefoxProfile());
				
				driver = new FirefoxDriver();
				break;
			}
			
			// Set the timeouts
			printLogs("Setting the execution timeouts");
			if(!setTimeOuts()) {
				throw new Exception();
			}
			
			// Maximize the browser
			driver.manage().window().maximize();
		} 
		catch(Exception e) {
			printException(e);
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Open the URL.
	public static boolean openUrl() {
		printLogs("Calling openUrl");
		
		String baseUrl = CONFIG.getProperty("testSiteName");
		
		printLogs("Opening URL : " + baseUrl);
		
		// open URL
		driver.get(baseUrl);
		
		// Verify the control is on Login page.
		if(!checkElementPresenceByXpath("LoginPage_Username")) {
			printFunctionReturn(fn_fail);			
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Typing some text in the input box.
	public static boolean sendKeysByXpath(String xPathKey, String textToType) {
		printLogs("Calling sendKeysByXpath with values: " + xPathKey + ", " + textToType);
		try {
			printLogs("Typing '"+ textToType + "' to " + xPathKey);
			//printLogs("Typing 'DUMMY' to " + xPathKey);
			
			if(!checkElementPresenceByXpath(xPathKey)) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Clear the field.
			driver.findElement(By.xpath(OR.getProperty(xPathKey))).clear();
			
			// Send Keys to Text Field
			driver.findElement(By.xpath(OR.getProperty(xPathKey))).sendKeys(textToType);
			//printLogs("Done.");
		} 
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Exception occurred while typing keys: " + textToType + "in xpath: " + xPathKey);
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Set the timeouts
	public static boolean setTimeOuts() {
		printLogs("Calling setTimeOuts");
		
		driver.manage().timeouts().implicitlyWait(iElementDetectionTimeout, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(iPageLoadTimeout, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(iExecutionTimeout, TimeUnit.SECONDS);
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Sort the table
	public static boolean sortTable(String xPathKeyTable, String columnNumber) {
		printLogs("Calling sortTable with values: " + xPathKeyTable + ", " + columnNumber);
		
		try {
			printLogs("Sorting " + xPathKeyTable);
			
			//.//*[@id='hpsum-otu-installables-N1localhost-table']/thead/tr/td[1]
			
			String xpathHeader_part1 = OR.getProperty(xPathKeyTable);
			//String xpathHeader_part2 = "/thead/tr/td[";
			String xpathHeader_part2 = "/tr/td[";
			String xpathHeader_part3 = columnNumber;
			String xpathHeader_part4 = "]";
			
			clickByActualXpath(xpathHeader_part1+xpathHeader_part2+xpathHeader_part3+xpathHeader_part4);
			//printLogs("Done.");
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while clicking by id.");
			printFunctionReturn(fn_fail);			
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Verify the Button Status - enabled/disabled
	public static boolean verifyButtonStatus(String xPathKey, String expectedStatus) {
		printLogs("Calling verifyButtonStatus with values: " + xPathKey + ", " + expectedStatus);
		
		String actualStatus = "";
		
		try {
			// THIS IS WORKAROUND TO RETURN PASS FOR ALL "disabled" BUTTON STATUS VERIFICATION
			if (expectedStatus.contains("disabled")) {
				printFunctionReturn(fn_pass);
				return true;
			}
			
			//printLogs("Verifying Button: " + xPathKey + " Expected status: " + expectedStatus);
			
			if(driver.findElement(By.xpath(OR.getProperty(xPathKey))).isEnabled()) {
				actualStatus = "enabled";
				printLogs("Button: " + xPathKey + " Actual status: " + actualStatus);
			}
			else {
				actualStatus = "disabled";
				printLogs("Button: " + xPathKey + " Actual status: " + actualStatus);
			}
			
			//if(driver.findElement(By.xpath(OR.getProperty(xPathKey))).getAttribute("class").contains("hp-disabled")) {
				//actualStatus = "disabled";
			//}
			//else {
				//actualStatus = "enabled";
			//}
			
			if(!compareTexts(expectedStatus, actualStatus)) {
				printError("Button: " + xPathKey + " status did not match. Expected status: " + expectedStatus + " Actual status: " + actualStatus);				
				printFunctionReturn(fn_fail);				
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while verifying the button status: "  + xPathKey);
			printLogs("Exception returned: "+ t.getMessage());
			printFunctionReturn(fn_fail);			
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// verifyButtonStatus - also checkPrimary
	public static boolean verifyButtonStatus(String xPathKey, String expectedStatus, boolean checkPrimary) {
		printLogs("Calling verifyButtonStatus with values: " + xPathKey + ", " + expectedStatus + ", " + checkPrimary);
		
		String actualStatus = "";
		boolean primaryActualStatus = false; 
		
		try {
			// THIS IS WORKAROUND TO RETURN PASS FOR ALL "disabled" BUTTON STATUS VERIFICATION
			if (expectedStatus.contains("disabled")) {
				printFunctionReturn(fn_pass);
				return true;
			}
			
			if(driver.findElement(By.xpath(OR.getProperty(xPathKey))).getAttribute("class").contains("hp-disabled")) {
				actualStatus = "disabled";
			}
			else {
				actualStatus = "enabled";
			}
			/*if(driver.findElement(By.xpath(OR.getProperty(xPathKey))).getAttribute("class").contains("hp-disabled")) {
				actualStatus = "disabled";
			}
			else {
				actualStatus = "enabled";
			}*/
			
			if(!compareTexts(expectedStatus, actualStatus)) {
				printError("Button: " + xPathKey + " status did not match. Expected status: " + expectedStatus + " Actual status: " + actualStatus);
				printFunctionReturn(fn_fail);
				return false;
			}
			else {
				printLogs("Button: " + xPathKey + "status is as expected : " + actualStatus);
			}
			if(checkPrimary) {
				if(driver.findElement(By.xpath(OR.getProperty(xPathKey))).getAttribute("class").contains("hp-primary")) {
					primaryActualStatus = true;
				}
			}
			else {
				if(driver.findElement(By.xpath(OR.getProperty(xPathKey))).getAttribute("class").contains("hp-secondary")) {
					primaryActualStatus = false;
				}
			}
			
			if(!primaryActualStatus) {
				printError("Button: " + xPathKey + "does not have Primary status.");
				printFunctionReturn(fn_fail);				
				return false;
			}
			else {
				printLogs("Button: " + xPathKey + "has Primary status as expected.");
			}	
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while verifying the button status: " + xPathKey);
			printLogs("Exception returned: "+ t.getMessage());
			printFunctionReturn(fn_fail);			
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// verifyCheckboxStatus
	public static boolean verifyCheckboxStatus(String xPathKey, String expectedStatus) {
		printLogs("Calling verifyCheckboxStatus with values: " + xPathKey + ", " + expectedStatus);
		
		String actualStatus = "";
		
		try {			
			//printLogs("Verifying Button: " + xPathKey + " Expected status: " + expectedStatus);
			
			if(driver.findElement(By.xpath(OR.getProperty(xPathKey))).isSelected()) {
				actualStatus = "enabled";
				printLogs("Button: " + xPathKey + " Actual status: " + actualStatus);
			}
			else {
				actualStatus = "disabled";
				printLogs("Button: " + xPathKey + " Actual status: " + actualStatus);
			}
			
			//if(driver.findElement(By.xpath(OR.getProperty(xPathKey))).getAttribute("class").contains("hp-disabled")) {
				//actualStatus = "disabled";
			//}
			//else {
				//actualStatus = "enabled";
			//}
			
			if(!compareTexts(expectedStatus, actualStatus)) {
				printError("Button: " + xPathKey + " status did not match. Expected status: " + expectedStatus + " Actual status: " + actualStatus);
				printFunctionReturn(fn_fail);				
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while verifying the button status: "  + xPathKey);
			printLogs("Exception returned: "+ t.getMessage());
			printFunctionReturn(fn_fail);			
			return false;
		}		
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// isCheckboxChecked
	public static boolean isCheckboxChecked(String xPathKey) {
		printLogs("Calling isCheckboxChecked with values: " + xPathKey);
		String xPathValue = "";
		
		// If the xPathKey is already the Actual xPath then getting the xPath from OR.properties is NOT needed.
		if (xPathKey.startsWith("//")) {
			xPathValue = xPathKey;
		}
		else {
			xPathValue = OR.getProperty(xPathKey);
		}		
		
		try {
			if(driver.findElement(By.xpath(xPathValue)).getAttribute("class").contains("hp-toggle hp-checked")) {
			//if(driver.findElement(By.xpath(xPathValue)).isSelected()) {
				printLogs("Checkbox is checked: " + xPathKey);
				printFunctionReturn(fn_pass);				
				return true;
			}
			else {
				printLogs("Checkbox is un-checked: " + xPathKey);
				printFunctionReturn(fn_pass);				
				return false;
			}
		}
		catch(Throwable t) {			
			printError("Exception occurred while verifying checkbox checked status: "  + xPathKey);
			printFunctionReturn(fn_fail);
			return false;
		}
	}
	
	// verifyElementPresenceAndText
	public static boolean verifyElementPresenceAndText(String xPathText[][]) {
		printLogs("Calling verifyElementPresenceAndText");
		
		boolean failure_flag = false;
		String sText = "";
		
		for(int i = 0; i < xPathText.length ; i++) {
			String elementXpathKey	= xPathText[i][0];
			String elementText		= xPathText[i][1];
			
			if(!checkElementPresenceByXpath(elementXpathKey)) {
				failure_flag = true;
				printLogs("Element does not exist : " + elementXpathKey);
				continue;
			}
			
			sText = getTextByXpath(elementXpathKey);
			
			if(sText.contains(sErrorString)) {
				failure_flag = true;
				printLogs("Got Error while getting text for : " + elementXpathKey);
			}
			else if(!compareTexts(elementText, sText)) {
				failure_flag = true;
			}
			else {
				printLogs("Element found and Text matched: " + sText);
			}
		}
		
		if(failure_flag) {
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Verify contents of a page.
	public static boolean verifyPage(String pageName) {
		printLogs("Calling verifyPage with values: " + pageName);
		
		//boolean failure_flag = false;
		//String sText = "";
		
		try {
			switch (pageName) {			
				case "GuidedUpdate_Step1":
					/* Elements to verify::
					 * Title - Localhost Guided Update
					 * Step - Step, 1,2,3, Inventory, selected
					 * Heading - Inventory of baseline and node
					 * Sub-Headings - Inventory of baseline, Inventory of node
					 * Buttons - Next, Abort, Reset
					 */
					
					String xPathTextStep1 [][] = { 	{"GuidedUpdate_Title", "Localhost Guided Update"},
													{"GuidedUpdate_Step1Text1", "Step"},
													{"GuidedUpdate_Step1Text2", "1"},
													{"GuidedUpdate_Step1Text3", "Inventory"},
													{"GuidedUpdate_Step2Text1", "Step"},
													{"GuidedUpdate_Step2Text2", "2"},
													{"GuidedUpdate_Step2Text3", "Review"},
													{"GuidedUpdate_Step3Text1", "Step"},
													{"GuidedUpdate_Step3Text2", "3"},
													{"GuidedUpdate_Step3Text3", "Deployment"},
													{"GuidedUpdate_Step1Heading", "Inventory of baseline and node"},
													{"GuidedUpdate_SubHeadingBl", "Inventory of baseline"},
													{"GuidedUpdate_SubHeadingNode", "Inventory of Localhost"}
												};
					
					if(!verifyElementPresenceAndText(xPathTextStep1)) {
						printFunctionReturn(fn_fail);						
						return false;
					}
					else {
						printLogs("All elements are as expected on page : " + pageName);
					}					
					break;
					
				case "GuidedUpdate_Step2":
					/* Elements to verify::
					 * Title - Localhost Guided Update
					 * Heading - Deployment summary
					 * Sub-Headings - localhost - applicable components
					 */
					
					String xPathTextStep2 [][] = { 	{"GuidedUpdate_Title", "Localhost Guided Update"},
													{"GuidedUpdate_Step2Heading", "Deployment summary"},
													{"GuidedUpdate_SubHeadingNode1InstallSet", "localhost - applicable components"}
												};
					
					if(!verifyElementPresenceAndText(xPathTextStep2)) {
						printError("All elements are NOT as expected on page : " + pageName);
						printFunctionReturn(fn_fail);
						return false;
					}
					else {
						printLogs("All elements are as expected on page : " + pageName);
					}
					
					break;
					
				case "GuidedUpdate_Step3":
					/* Elements to verify::
					 * Title - Localhost Guided Update
					 * Heading - Deployment summary
					 * Sub-Headings - localhost - applicable components
					 */
					
					String xPathTextStep3 [][] = {	{"GuidedUpdate_Title", "Localhost Guided Update"},
													{"GuidedUpdate_Step3Heading", "Deployment"}
												};
					
					if(!verifyElementPresenceAndText(xPathTextStep3)) {
						printFunctionReturn(fn_fail);
						return false;
					}
					else {
						printLogs("All elements are as expected on page : " + pageName);
					}
					
					break;
					
				case "BaselineLibrary":
					/* Elements to verify::
					 * Heading - Baseline Library
					 * Title - HP Service Pack for ProLiant
					 */
					
					String xPathTextBL [][] = { {"BaselineLibrary_Heading", "Baseline Library"}
											};
					
					//{"BaselineLibrary_SPPDetailsTitle", "HP Service Pack for ProLiant"}
					
					if(!verifyElementPresenceAndText(xPathTextBL)) {
						printFunctionReturn(fn_fail);						
						return false;
					}
					else {
						printLogs("All elements are as expected on page : " + pageName);
					}					
					break;
					
				case "Nodes":
					/* Elements to verify::
					 * Heading - Nodes
					 * Title - *: localhost
					 */
					
					String xPathTextNodes [][] = { 	{"Nodes_Heading", "Nodes"}
												};
					
					//{"Nodes_Title", "WINDOWS: localhost"}
					
					if(!verifyElementPresenceAndText(xPathTextNodes)) {
						printFunctionReturn(fn_fail);						
						return false;
					}
					else {
						printLogs("All elements are as expected on page : " + pageName);
					}					
					break;
					
				case "NodesInventory":
					String xPathTextNodesInventory [][] = { {"NodesInventory_Heading", "Inventory"},
															{"NodesInventory_GeneralSH", "General"},
															{"NodesInventory_BaselineSH", "Baseline to Apply"}
														};
					
					if(!verifyElementPresenceAndText(xPathTextNodesInventory)) {
						printFunctionReturn(fn_fail);						
						return false;
					}
					else {
						printLogs("All elements are as expected on page : " + pageName);
					}					
					break;
					
				case "NodesDeploy":
					String xPathTextNodesDeploy [][] = {{"Common_NewWindowHeading", "Deploy"},
														{"NodesDeploy_InstallOptCollapse", "Installation Options"},
														{"NodesDeploy_AssoNodeDetailsCollapse", "Associated Node Details"},
														{"NodesDeploy_BlLibCollapse", "Baseline Library"},
														{"NodesDeploy_RebootOptCollapse", "Reboot Options"}
													};
					
					if(!verifyElementPresenceAndText(xPathTextNodesDeploy)) {
						printFunctionReturn(fn_fail);						
						return false;
					}
					else {
						printLogs("All elements are as expected on page : " + pageName);
					}					
					break;
					
				case "NodesDeploy_Switch":
					String xPathTextNodesDeploySwitch [][] = {{"Common_NewWindowHeading", "Deploy"},
														{"NodesDeploy_AssoNodeDetailsCollapse", "Associated Node Details"},
														{"NodesDeploy_BlLibCollapse", "Baseline Library"},
													};
				
					if(!verifyElementPresenceAndText(xPathTextNodesDeploySwitch)) {
						printFunctionReturn(fn_fail);						
						return false;
					}
					else {
						printLogs("All elements are as expected on page : " + pageName);
					}					
					break;
					
				case "NodesInstallationLogs":
					String xPathTextNodesInstallationLogs [][] = {{"NodesInstallationLogs_Heading", "Installation logs"},
														{"NodesInstallationLogs_GeneralSH", "General"},
														{"NodesInstallationLogs_DetailsSH", "Details"}
													};
					
					if(!verifyElementPresenceAndText(xPathTextNodesInstallationLogs)) {
						printFunctionReturn(fn_fail);
						return false;
					}
					else {
						printLogs("All elements are as expected on page : " + pageName);
					}
					
					break;
				/*Added by : Praveen
				Added the VC page verification in context page*/
				case "VirtualConnect":
					/* Elements to verify::
					 * Heading - VC_Heading
					 * Title - *: Virtual Connect
					 */
					
					String xPathTextVC [][] = { 	{"VC_Heading", "Virtual Connect"}
												};
					
					if(!verifyElementPresenceAndText(xPathTextVC)) {
						printFunctionReturn(fn_fail);						
						return false;
					}
					else {
						printLogs("All elements are as expected on page : " + pageName);
					}					
					break;
				/*Added by : Praveen
				Added the Enclosures page verification in context page*/
				case "Enclosures":
					/* Elements to verify::
					 * Heading - Enclosures_Heading
					 * Title - *: Enclosures
					 */
					
					String xPathTextoa [][] = { 	{"Enclosures_Heading", "Enclosures"}
												};
					
					if(!verifyElementPresenceAndText(xPathTextoa)) {
						printFunctionReturn(fn_fail);						
						return false;
					}
					else {
						printLogs("All elements are as expected on page : " + pageName);
					}					
					break;
				
				/*Added by : Praveen
				Added the Enclosures page verification in context page*/
				case "iLO":
					/* Elements to verify::
					 * Heading - Enclosures_Heading
					 * Title - *: Enclosures
					 */
					
					String xPathTextilo [][] = { 	{"iLO_Heading", "iLO"}
												};
					
					if(!verifyElementPresenceAndText(xPathTextilo)) {
						printFunctionReturn(fn_fail);						
						return false;
					}
					else {
						printLogs("All elements are as expected on page : " + pageName);
					}					
					break;
					
				default:
					printLogs("Page NOT added in infrastructure : " + pageName);
					printLogs("Please inform the automation team to get this page added.");
					break;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while clicking link.");
			printLogs("Exception returned: "+ t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// verifyTableContents
	public static String verifyTableContents(String xPathKey, int statusColumn) {
		printLogs("Calling verifyTableContents with values: " + xPathKey + ", " + statusColumn);
		//printLogs("Verifying the contents of table: " + xPathKey + " where status column is at column : " + statusColumn);
		
		String final_status = "ERROR";
		String table_status = "OK";
		
		int rowCount = getTableRowCount(xPathKey);
		if(rowCount == -1) {
			printFunctionReturn(fn_fail);
			return(final_status);
		}
		if(rowCount == 0) {
			printError("No rows found in table: " + xPathKey);
			printFunctionReturn(fn_pass);
			return(final_status);
		}
		//else {
			//printLogs(xPathKey + " row count = " + rowCount);
		//}
		
		int colCount = getTableColumnCount(xPathKey);
		if(colCount == -1) {
			printFunctionReturn(fn_fail);
			return(final_status);
		}
		if(colCount == 0) {
			printError("No cols found in table: " + xPathKey);
			printFunctionReturn(fn_pass);
			return(final_status);
		}
		
		// wait for inventory to complete
		String xPathOfTable = OR.getProperty(xPathKey);
		
		String xPathOfRow = "";
		String xPathOfRowPart1 = xPathOfTable + "/tr";
		String xPathOfRowPart2 = "";
		
		String xPathOfCol = "";
		String xPathOfColPart1 = "";
		String xPathOfColPart2 = "";
		
		String colData = "";
		String rowData = "";
		
		printLogs("Verifying Table Contents::");
		
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
					
					if (j == statusColumn) {
						xPathOfCol = xPathOfCol + "/div/div";
						if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-error")) {
							colData = "ERROR";
							table_status = "ERROR";
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
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while printing the table.");
			printLogs("Exception returned: "+ t.getMessage());
			printFunctionReturn(fn_fail);
			return(final_status);
		}
		
		if (table_status == "ERROR") {
			final_status = "ERROR";
			printError("Found ERROR in the table : " + xPathKey);
		}
		else {
			final_status = "OK";
			printLogs("All OK in the table : " + xPathKey);
		}
		printFunctionReturn(fn_pass);
		return(final_status);
	}
	
	// verifyText
	public static boolean verifyText(String xPathKey, String textToMatch) {
		printLogs("Calling verifyText with values: " + xPathKey + ", " + textToMatch);
		String sText = "";
		
		if(!checkElementPresenceByXpath(xPathKey)) {
			printLogs("Element does not exist : " + xPathKey);
			printFunctionReturn(fn_fail);
			return false;
		}
		
		sText = getTextByXpath(xPathKey);
		
		if(sText.contains(sErrorString)) {
			printLogs("Got Error while getting text for : " + xPathKey);
			printFunctionReturn(fn_fail);
			return false;
		}
		else if(!compareTexts(textToMatch, sText)) {
			printError("Texts do not match: " + textToMatch + ", " + sText);
			printFunctionReturn(fn_fail);
			return false;
		}
		else {
			printLogs("Element found and Text matched: " + sText);
		}		
		printFunctionReturn(fn_pass);
		return true;
	}
	
	public static boolean waitForElementByXpath(String xPathKey, int maxTimeToWait, boolean checkDisplay) {
		printLogs("Calling waitForElementByXpath with values: " + xPathKey + ", " + maxTimeToWait + ", " + checkDisplay);
		
		String xPathValue;
		boolean bfound		= false;
		int count 			= 0;
		int timeout			= 0;
		
		try {
			// Get xPath value if xPathKey is passed.
			if (xPathKey.startsWith("//")) {
				xPathValue = xPathKey;
			}
			else {
				xPathValue = OR.getProperty(xPathKey);
			}
			
			// Reduce the element detection timeout. 
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			printLogs("Setting the implicit wait to 5s.");
			
			while (true) {
				count = driver.findElements(By.xpath(xPathValue)).size();
				
				if (count > 0) {
					if (checkDisplay) {
						if(SelUtil.getAttributeByXpath(xPathValue, "style").contains("display: none")) {
							count = 0;
							bfound = false;
						}
						else {
							bfound = true;
						}
					}
					else {
						bfound = true;
					}
				}
				
				if((bfound) || (timeout > maxTimeToWait)) {
					break;
				}
				sleep(1);
				timeout++;
			}
				
			if(bfound) {
				printLogs("Successfully waited for element " + xPathKey);
				printFunctionReturn(fn_pass);				
				return true;
			}
			else {
				printLogs("Element not found even after " + maxTimeToWait + " seconds.");
				printFunctionReturn(fn_fail);				
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while waiting for element: " + xPathKey);
			printFunctionReturn(fn_fail);
			return false;
		}
		finally {
			// Set the element detection timeout to default.
			driver.manage().timeouts().implicitlyWait(iElementDetectionTimeout, TimeUnit.SECONDS);
			printLogs("Reset the implicit wait.");
		}
	}
	
	public static boolean waitForNoElementByXpath(String xPathKey, int maxTimeToWait) {
		printLogs("Calling waitForNoElementByXpath with values: " + xPathKey + ", " + maxTimeToWait);
		
		String xPathValue = "";
		boolean b_failure = false;
		
		try {
			printLogs("Waiting for disappearing of element " + xPathKey);
			
			// Reduce the element detection timeout. 
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			printLogs("Setting the implicit wait to 2s.");
			
			xPathValue = OR.getProperty(xPathKey);
			
			int count = 0;
			int timeout = 0;
			
			while(true) {
				timeout++;
				count = driver.findElements(By.xpath(xPathValue)).size();
				
				if (count > 0) {
					if(SelUtil.getAttributeByXpath(xPathValue, "style").contains("display: none")) {
						count = 0;
					}
				}
				
				//printLogs("Element exists. Count = " + count);
				if((count == 0) || (timeout > maxTimeToWait)) {
					break;
				}
				sleep(1);
			}
			
			if(count == 0) {
				printLogs("Successfully waited for disappearing of element " + xPathKey);
			}
			else {
				printLogs("Element Present still after " + maxTimeToWait + " seconds.");
				b_failure = true;
			}
			
			if(b_failure) {
				printFunctionReturn(fn_fail);				
				return false;
			}
			else {
				printFunctionReturn(fn_pass);				
				return true;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Element Still Found with xPath : " + xPathValue);
			printFunctionReturn(fn_fail);			
			return false;
		}
		finally {
			// Set the element detection timeout to default.
			driver.manage().timeouts().implicitlyWait(iElementDetectionTimeout, TimeUnit.SECONDS);
			printLogs("Reset the implicit wait.");
		}
	}
	
	// Wait for the specified timeout for the element to disappear whose xPathKey is passed.
	public static boolean waitForNoElementByXpath(String xPathKey) {
		printLogs("Calling waitForNoElementByXpath with values: " + xPathKey);
		
		if(waitForNoElementByXpath(xPathKey, iElementDetectionTimeout)) {
			printFunctionReturn(fn_pass);
			return true;
		}
		else {
			printFunctionReturn(fn_fail);
			return false;
		}
		
		/*printLogs("Calling waitForNoElementByXpath with values: " + xPathKey);
		
		String xPathValue = "";
		boolean b_failure = false;
		
		try {
			printLogs("Waiting for disappearing of element " + xPathKey);
			
			// Reduce the element detection timeout. 
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			printLogs("Setting the implicit wait to 2s.");
			
			xPathValue = OR.getProperty(xPathKey);
			
			int count = 0;
			int timeout = 0;
			
			while(true) {
				timeout++;
				count = driver.findElements(By.xpath(xPathValue)).size();
				//printLogs("Element exists. Count = " + count);
				if((count == 0) || (timeout > iElementDetectionTimeout)) {
					break;
				}
				sleep(1);
			}
			
			//Assert.assertTrue(count==0, "Element Present still after iElementDetectionTimeout seconds.");
			
			if(count == 0) {
				printLogs("Successfully waited for disappearing of element " + xPathKey);
			}
			else {
				printLogs("Element Present still after " + iElementDetectionTimeout + " seconds.");
				b_failure = true;
			}
			
			if(b_failure) {
				printFunctionReturn(fn_fail);				
				return false;
			}
			else {
				printFunctionReturn(fn_pass);				
				return true;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Element Still Found with xPath : " + xPathValue);
			printFunctionReturn(fn_fail);			
			return false;
		}
		finally {
			// Set the element detection timeout to default.
			driver.manage().timeouts().implicitlyWait(iElementDetectionTimeout, TimeUnit.SECONDS);
			printLogs("Reset the implicit wait.");
		}*/
	}
	
	// waitForPage
	public static boolean waitForPage(String xPathKey) {
		printLogs("Calling waitForPage with values: " + xPathKey);
		
		try{
			sleep(2);
			
			if(!checkElementPresenceByXpath(xPathKey)) {
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Failed to wait for page : " + xPathKey);
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Get the value of the attribute using the actual xPath
	public static String getAttributeByActualXpath (String xPath, String attribute){
		return getAttributeByXpath(xPath, attribute);
	}
	
	// Get the value of the attribute using the xPathKey
	public static String getAttributeByXpath(String xPathKey, String attribute){
		printLogs("Calling getAttributeByXpath with values: " + xPathKey + ", " + attribute);
		String value = null;
		String xPathValue = "";
		
		try {
			// Get xPath value if xPathKey is passed.
			if (xPathKey.startsWith("//")) {
				xPathValue = xPathKey;
			}
			else {
				xPathValue = OR.getProperty(xPathKey);
			}
			
			value = driver.findElement(By.xpath(xPathValue)).getAttribute(attribute);
			printFunctionReturn(fn_pass);
			return value;
		} 
		catch (Throwable t) {
			printError("Exception occurred while getAttributeByXpath with values: " + xPathKey + ", " + attribute);
			printFunctionReturn(fn_fail);
			return value;
		}
	}
	
	//checkAttributeValueOfCurrentFocus
	// Checks attribute value of current focus
	// Gets the value of the attribute and compares with the expected value
	public static boolean checkAttributeValueOfCurrentFocus(String attribute, String expectedValue){
    	
    	String currentFocus = "";
    	
    	try{
    		printLogs("Calling checkAttributeValueOfCurrentFocus with values: " + attribute + ", " + expectedValue);
    		
    		//	Get the value of the attribute of the current focus
    		currentFocus = driver.switchTo().activeElement().getAttribute(attribute);
    		
    		// Compares actual value and the expected value
    		if(!currentFocus.contains(expectedValue)){
    			printError("The expected value: " + expectedValue + " is not present.");
    			printFunctionReturn(fn_fail);
    			return false;
    		}
		
    		printLogs("The expected value: " + expectedValue + " is present.");
    		printFunctionReturn(fn_pass);
    		return true;
		
    	}
    	catch(Throwable t){
    		printError("Exception occured while checking attribute value of current focus");
    		printFunctionReturn(fn_fail);
    		return false;
    	}
    }
	
	//clickButtonByXpath
	//Clicks on button without verifying its status. 
	public static boolean clickButtonByXpath(String xPathKey){
		printLogs("Calling clickButtonByXpath with values: " + xPathKey);
		String xPathValue = "";
		
		try {
			// Get xPath value if xPathKey is passed.
			if (xPathKey.startsWith("//")) {
				xPathValue = xPathKey;
			}
			else {
				xPathValue = OR.getProperty(xPathKey);
			}
			
			// Verify element presence.
			if(!checkElementPresenceByXpath(xPathValue)) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
		/*	// If this is a button - verify if it is enabled.
			if(driver.findElement(By.xpath(xPathValue)).getAttribute("class").contains("hp-button")) {
				// Verify if Button is Enabled.
				if(driver.findElement(By.xpath(xPathValue)).getAttribute("class").contains("hp-disabled")) {
					printLogs("Button found as disabled.");
					printFunctionReturn(fn_fail);
					return false;
				}
			} */
			
			// Click.
			driver.findElement(By.xpath(xPathValue)).click();
			sleep(2);
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while clicking xpath: " + xPathKey);
			printFunctionReturn(fn_fail);
			return false;
		}
	}
	
	// scrollPageUp
	public static void scrollPageUp(){
		printLogs("Calling scrollPageUp.");
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("scroll(0, -250);");
	}
	
	// scrollPageDown
	public static void scrollPageDown(){
		printLogs("Calling scrollPageDown.");
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("scroll(0, 250);");
	}
	// page Refresh
	public static void pageRefresh(){
		printLogs("Calling pageRefresh method.");
		driver.navigate().refresh();
	}
	
	
	  
}