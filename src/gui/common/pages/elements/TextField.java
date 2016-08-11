package gui.common.pages.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import gui.common.base.*;
import gui.common.util.ErrorUtil;

public class TextField {

	// Declare all the items on the screen
		private String xPathKey = "";
		private static WebDriver wbDriver = null;
		private String fn_fail = "Function-Fail";
		private String fn_pass = "Function-Pass";
		
		public TextField(String key){
			try
			{
				xPathKey = key;					
			}
			catch(Throwable t) {		
				printLogs("Exception occured with error: " + t.getMessage());		
			}	
		}
		public boolean TypeKeys (String Text){
			printLogs("Calling TypeKeys with values: " + Text);
			try {
				printLogs("Typing '"+ Text + "' to " + this.getXPath());
				//printLogs("Typing 'DUMMY' to " + xPathKey);
				
				if(!this.Exists()) {
					throw new Exception("Element does not exists");
				}
				
				// Clear the field.
				wbDriver.findElement(By.xpath(xPathKey)).clear();
				
				// Send Keys to Text Field
				wbDriver.findElement(By.xpath(xPathKey)).sendKeys(Text);
				//printLogs("Done.");
			} 
			catch(Throwable t) {
				ErrorUtil.addVerificationFailure(t);
				printError("Exception " + t.getMessage() + "occurred while typing keys: " + Text + "in xpath: " + xPathKey );
				printFunctionReturn(fn_fail);
				return false;
			}
			printFunctionReturn(fn_pass);
			return true;
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

		
		
		public TextField(){
			
		}
		private String getXPath() {	
			String key = TestBase.OR.getProperty(xPathKey);		
			return key;
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
