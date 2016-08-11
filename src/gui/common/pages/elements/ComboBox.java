package gui.common.pages.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import gui.common.base.*;
import gui.common.util.ErrorUtil;

public class ComboBox {

	private String xPathKey = "";
	private static WebDriver wbDriver = null;
	private String fn_fail = "Function-Fail";
	private String fn_pass = "Function-Pass";
	
	public ComboBox(String key){
		
		try
		{
			xPathKey = key;
			
		}
		catch(Throwable t) {		
			printLogs("Exception occured with error: " + t.getMessage());		
		}	
	}
	
	public boolean Select(String sItem){
		printLogs("Calling Select with arguments: ");
		printLogs("sItem: " + sItem);
		WebElement wdComboBox;
		try
		{
			sItem = sItem + "\n";		
			printLogs("Typing '"+ sItem + "' to " + xPathKey);
			//printLogs("Typing 'DUMMY' to " + xPathKey);
			wbDriver = getWebDriver();
			
			if(!Exists()) {
				throw new Exception("Failed to select from drop down: " + this.getXPath());	
			}
			
			wdComboBox = wbDriver.findElement(By.xpath(this.getXPath()));
			wdComboBox.sendKeys(Keys.PAGE_DOWN);
			// Clear the field.			
			wdComboBox.clear();
			
			// Send Keys to Text Field
			wdComboBox.sendKeys(sItem);		
			
		}
		catch(Throwable t) {				
			printError(t.getMessage());
			printFunctionReturn("Function-Fail");
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
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
	
	
	public ComboBox(){		
	}
	
	public String getXPath() {		
		String key = TestBase.OR.getProperty(xPathKey);		
		return key;
	}
	
	private void printLogs(String msg) {
		TestBase.printLogs(msg);
		
	}

	private void printFunctionReturn(String statusToPrint) {
		TestBase.printFunctionReturn(statusToPrint);
		
	}

	private void printError(String msg) {
		TestBase.printError(msg);
		
	}
	
	private WebDriver getWebDriver() {
		return TestBase.getWebDriver();
	}

	
}
