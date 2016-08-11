package gui.common.pages.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import gui.common.base.*;
import gui.common.util.ErrorUtil;

public class Button {

	private String xPathKey = "";
	private static WebDriver wbDriver = null;
	private String fn_fail = "Function-Fail";
	private String fn_pass = "Function-Pass";
	
	public Button(String key){
			try
			{
				xPathKey = key;				
				
			}
			catch(Throwable t) {		
				printLogs("Exception occured with error: " + t.getMessage());		
			}	
		}
	// Declare all the items on the screen
	public boolean isDisabled(){
		try
		{			
			printLogs("Verify if button is disabled");
			if(getStatus(this.getXPath(), "enabled")) {
				printError("Expected: disabled");
				printLogs("Found: enabled");
				throw new Exception("Expected status not found");				
			}
			else {
				printLogs("Successfully verified the button is disabled.");
				
			}
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Throwable t) {				
			printError("Failed to verify isDisabled for xPath: " + this.getXPath());
			printLogs("Exception occured with error: " + t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
		
				
	}
	public boolean isEnabled (){
		try{			
			printLogs("Verify if button is enabled");
			if(!getStatus(this.getXPath(), "enabled")) {				
				printError("Expected: enabled");
				printLogs("Found: disabled");
				throw new Exception("Expected status not found");					
			}
			else {
				printLogs("Successfully verified the button is enabled.");
			}
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Throwable t) {				
			printError("Failed to verify isEnabled for xPath: " + this.getXPath());
			printLogs("Exception occured with error: " + t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
	}
	
	private boolean getStatus(String xPath, String sStatus) {
		try
		{
			printLogs("Calling getStatus with arguments: ");
			printLogs("xPath: " + xPath);
			printLogs("sStatus: " + sStatus);
			
			String actualStatus;
			String expectedStatus = sStatus;		
			
			wbDriver = getWebDriver();
			
			// Get the status with selenium method.
			if(wbDriver.findElement(By.xpath(xPath)).isEnabled()) {
				actualStatus = "enabled";				
			}
			else {
				actualStatus = "disabled";				
			}
			
			printLogs("Button status as per selenium method: " + xPath + " Actual status: " + actualStatus);
			
			// Check hp-disabled is displayed along the class name like: class="hp-primary h-disabled"
			// If s then the button is disabled.
			if(wbDriver.findElement(By.xpath(xPath)).getAttribute("class").contains("hp-disabled")) {
				actualStatus = "disabled";
			}
			else {
				actualStatus = "enabled";
			}
			
			printLogs("Button as per class attribute(hp-disabled): " + xPath + " Actual status: " + actualStatus);
			
			if(!compareTexts(expectedStatus, actualStatus)) {
				printError("Button: " + xPath + " status did not match. \n" +
						   "Expected status: " + expectedStatus + 
						   "\n Actual status: " + actualStatus);				
				throw new Exception("Error occured in getStatus.");				
			}	
			printLogs("Successfully getStatus for Button: " + xPath);
			printLogs("Actual Status is: " + actualStatus);
			printFunctionReturn(fn_pass);
			return true;			
		}
		catch(Throwable t) {				
			printError("Failed to verify isDisabled for xPath: " + this.getXPath());
			printLogs("Exception occured with error: " + t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}		
	}
		
	public boolean Click() {
		try
		{
			printLogs("Clicking on : '" + this.getClass().getSimpleName() + "'");
			wbDriver = getWebDriver();
			if (this.Exists()){
								
				// Click.
				wbDriver.findElement(By.xpath(this.getXPath())).click();
				sleep(2);
				
			} else {
				printLogs("The button with xPath '" + this.getXPath() + "' was not found.");
				throw new Exception("Failed to click on button");
				
			}
			printLogs("Successfully clicked on : '" + this.getClass().getSimpleName() + "'");
			printLogs("With xPath: " + xPathKey);
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Throwable t) {				
			printError("Exception in method Click.");
			printLogs("Exception occured with error: " + t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
			
	}
		
	public boolean Exists(){		
		printLogs("Calling Exists for class " + this.getClass().getSimpleName());		
		
		try {			
			int count = 0;
			wbDriver = getWebDriver();
			count = wbDriver.findElements(By.xpath(this.getXPath())).size();
			
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
	
	public Button () {
		
	}
	
	private String getXPath() {		
		String key = TestBase.OR.getProperty(xPathKey);		
		return key;		
	}
	public boolean isDefault() {
		boolean bStatus = false;
		try
		{
			printLogs("Checking if button is primary");
			wbDriver = getWebDriver();
			if(wbDriver.findElement(By.xpath(this.getXPath())).getAttribute("class").contains("hp-primary")){
				bStatus = true;
			}
		}
		catch(Throwable t) {				
			printError("Exception in method Click.");
			printLogs("Exception occured with error: " + t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return bStatus;
		
	}
	
	private boolean compareTexts(String sExpected, String sActual) {
		return SelUtil.compareTexts(sExpected, sActual);		
	}
	
	private void printError(String msg) {
		TestBase.printError(msg);
	}

	private static WebDriver getWebDriver() {
		return TestBase.getWebDriver();
	}
	
	private void printFunctionReturn(String sStatus){
		TestBase.printFunctionReturn(sStatus);		
	}	
	
	private void printLogs(String msg) {
		TestBase.printLogs(msg);
	}
	
	private void sleep(int i) {
		TestBase.sleep(i);		
	}
	
}
