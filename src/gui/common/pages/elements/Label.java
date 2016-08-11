package gui.common.pages.elements;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import gui.common.base.*;
import gui.common.util.ErrorUtil;

public class Label{

	private String xPathKey = "";
	private static WebDriver wbDriver = getWebDriver();
	private String fn_fail = "Function-Fail";
	private String fn_pass = "Function-Pass"; 
	
	public Label(String key){
		try
		{			
			xPathKey = key;		
			
		}
		catch(Throwable t) {		
			printLogs("Exception occured with error: " + t.getMessage());		
		}	
	}
	
	public String GetText(){
		String sText;
		printLogs("Calling GetText for xPath: " + this.getXPath());
		try
		{	
			wbDriver = getWebDriver();
			sText = wbDriver.findElement(By.xpath(this.getXPath())).getText();			
			printLogs("Successfully GetText Text: '" + sText + "' from xPath: " + this.getXPath());
			
		}
		catch(Throwable t) {				
			printError("Exception in method GetText.");
			printLogs(t.getMessage());
			printFunctionReturn(fn_fail);
			return null;
		}
		printFunctionReturn(fn_pass);
		return sText;
	}
	
	public boolean IsEmpty(){
		printLogs("Calling IsEmpty: "+ this.getXPath());
		try
		{
		String sText = this.GetText();
		if (sText.isEmpty()){
			return true;
		} else {
			return false;
		}
		}
		catch(Throwable t) {				
			printError("Exception in method IsEmpty.");
			printLogs(t.getMessage());
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
			count = wbDriver.findElements(By.xpath(this.getXPath())).size();			
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
	
	public boolean Select(String optionName){
		printLogs("Calling Select");
		
		try {
			String xPath = this.getXPath();
			printLogs("Clicking on Actions Drop-down : " + xPath);
			wbDriver = getWebDriver();
			wbDriver.findElement(By.cssSelector(xPath)).click();
			sleep(2);
			
			captureScreenShot();
			
			// Click on the linkText.
			printLogs("Selecting the option from drop-down.");
			wbDriver.findElement(By.linkText(optionName)).click();
			sleep(3);
			
			printLogs("Successfully selected option from Actions drop-down: " + optionName);
			captureScreenShot();			
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while selecting.");
			printLogs(t.getMessage());
			printFunctionReturn(fn_fail);			
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	
	public boolean WaitForText(String sText,int iTimeout){
		printLogs("Calling WaitForText: " +  this.getXPath() + 
				  " with arguments: " +
					"sText: " + sText +
					" iTimeout: " + iTimeout);
		try{
			int iIndex=0;
			boolean bFound = false;
			String sActual = "";
			for (iIndex=0; iIndex<=iTimeout; iIndex++){
				if(this.Exists()){
					sActual = this.GetText();
					if(sActual.matches(sText)){
						bFound = true;
						break;
					}	
					sleep(5);
				}
					
			}
			return bFound;
		}
		catch(Throwable t) {				
			printError("Exception in method WaitForText.");
			printLogs(t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
	}
	
	public boolean WaitForElement(int iTimeout){
		printLogs("Calling WaitForElement:" + this.getXPath());	
		printLogs("With Timeout: " + iTimeout);
		boolean bFound = false;
		int iIndex=0;
		try{
			wbDriver = getWebDriver();
			for (iIndex=0; iIndex<=iTimeout; iIndex++){
					if(wbDriver.findElements(By.xpath(this.getXPath())).size() > 0){
						bFound = true;
						break;					
					}
					sleep(2);
			}
			
		}
		catch (Throwable t){
			printError("Exception in method WaitForElement.");
			printLogs("Exception Returned: " + t.getMessage());
			printFunctionReturn(fn_fail);	
		}
		printFunctionReturn(fn_pass);
		return bFound;
	}
	
	public boolean WaitForElementNotExists(int iTimeout){
		printLogs("Calling WaitForElementNotExists:" + this.getXPath());	
		printLogs("With Timeout: " + iTimeout);
		boolean bFound = false;
		try{
			if(SelUtil.waitForNoElementByXpath(this.getXPath(), iTimeout)){
				bFound = true;
			}
		}
		catch (Throwable t){
			printError("Exception in method WaitForElementNotExists.");
			printLogs("Exception Returned: " + t.getMessage());
			printFunctionReturn(fn_fail);	
		}
		printFunctionReturn(fn_pass);
		return bFound;
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
			
			wbLabel = wbDriver.findElement(By.xpath(this.getXPath()));
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
	
	private void captureScreenShot() throws IOException {
		TestBase.captureScreenShot();
		
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
}
