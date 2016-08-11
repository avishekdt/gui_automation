package gui.common.pages.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import gui.common.base.*;
import gui.common.util.ErrorUtil;

public class RadioButton {
	private String xPathKey = "";
	private static WebDriver wbDriver = null;
	private String fn_fail = "Function-Fail";
	private String fn_pass = "Function-Pass"; 
	
	public RadioButton(String key){
		try
		{
			xPathKey = key;
			
		}
		catch(Throwable t) {		
			printLogs("Exception occured with error: " + t.getMessage());		
		}	
	}
	
	public boolean Select(){
		try
		{
			printLogs("Calling Select with values: " + this.getXPath());
			
			if(!Click()) {
				printError("Failed to select xPath: " + this.getXPath());
				printFunctionReturn(fn_fail);
				return false;
			}
			return true;
		}
		catch(Throwable t) {				
			printError("Exception in method Select.");
			printFunctionReturn(fn_fail);
			return false;
		}
	}
	
	public boolean Click() {
		try
		{
			printLogs("Clicking on : '" + this.getClass().getSimpleName() + "'");
			if (this.Exists()){
				
				// Click.
				wbDriver.findElement(By.xpath(xPathKey)).click();
				sleep(2);
				
			} else {
				printError("The button with xPath '" + this.getXPath() + "' is not enabled.");
				printFunctionReturn(fn_fail);
				return false;
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
	
	private void sleep(int i) {
		TestBase.sleep(i);
		
	}

	private boolean Exists() {
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

	private String getXPath() {		
		String key = TestBase.OR.getProperty(xPathKey);		
		return key;
	}

	public RadioButton(){		
	}
	
	private void printFunctionReturn(String statusToPrint) {
		TestBase.printFunctionReturn(statusToPrint);
		
	}

	private void printError(String msg) {
		TestBase.printError(msg);
		
	}
	
	private void printLogs(String msg) {
		TestBase.printLogs(msg);
		
	}
	
	private static WebDriver getWebDriver() {
		return TestBase.getWebDriver();
	}
}
