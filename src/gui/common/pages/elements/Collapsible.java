package gui.common.pages.elements;


import gui.common.base.TestBase;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Collapsible {
	private String xPathKey = "";
	private static WebDriver wbDriver = getWebDriver();
	private String fn_fail = "Function-Fail";
	private String fn_pass = "Function-Pass"; 
	
	public Collapsible(String key){
		try
		{			
			xPathKey = key;		
			
		}
		catch(Throwable t) {		
			printLogs("Exception occured with error: " + t.getMessage());		
		}	
	}
	
	public boolean Click() {
		try
		{
			printLogs("Clicking on : '" + this.getClass().getSimpleName() + "'");
			if (this.Exists()){
								
				// Click.
				wbDriver.findElement(By.cssSelector(xPathKey)).click();
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
		printLogs("Calling Exists: " +  this.getXPath());
		try
		{
			int count = 0;		
			wbDriver = getWebDriver();
			count = wbDriver.findElements(By.cssSelector(this.getXPath())).size();			
			if (count > 0) {
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
			printError("Exception in method Exists.");
			printLogs(t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
		
	}
	
	public void ScrollIntoView(){
		printLogs("Calling ScrollIntoView ");		
		WebElement wbLabel;
		try
		{		
			wbDriver = getWebDriver();
			
			if(!Exists()) {
				throw new Exception("Failed to select from drop down: " + this.getXPath());	
			}
			
			wbLabel = wbDriver.findElement(By.cssSelector(this.getXPath()));
			wbLabel.sendKeys(Keys.PAGE_DOWN);	
			printLogs("Successfully scrolled the Element into view: " + this.getXPath());		
			
		}
		catch(Throwable t) {				
			printError(t.getMessage());
			printFunctionReturn("Function-Fail");
			
		}
		printFunctionReturn(fn_pass);
		
	}
	
	private String getXPath() {		
		String key = TestBase.OR.getProperty(xPathKey);		
		return key;
		
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
