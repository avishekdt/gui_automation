package gui.common.pages.elements;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import gui.common.base.*;
import gui.common.util.ErrorUtil;

public class Table{

	private String xPathKey = "";
	private static WebDriver wbDriver = null;
	private String fn_fail = "Function-Fail";
	private String fn_pass = "Function-Pass"; 
	
	public static ArrayList<LinkedHashMap<String, String>> aTBContent = new ArrayList<LinkedHashMap<String, String>>();
	public static int iValueIndex = 0;
	public static Collection<String> sValueKey;
	public static Set<String> sTableItemxPath;
	
	
	public Table(String key){
		try
		{			
			xPathKey = key;
			
		}
		catch(Throwable t) {		
			printLogs("Exception occured with error: " + t.getMessage());		
		}	
	}	
	
	public ArrayList<LinkedHashMap<String, String>> GetContents(){
		this.GetTableContent();
		return aTBContent;
	}
	
	public void GetTableContent(){
		printLogs("Calling getTableContent");	 
		
		String xPath = "";
		
		int row_num,col_num;
		int colCount = 0;		
		try{
			xPath = this.getXPath();
			wbDriver = getWebDriver();
			WebElement wbElement = wbDriver.findElement(By.xpath(xPath));
			List<WebElement> tableHeader = wbElement.findElements(By.xpath(xPath+"/thead/tr"));
			
			List<WebElement> tr_collection = wbElement.findElements(By.xpath(xPath+"/tbody/tr"));
			List<WebElement> td_collection;
			LinkedHashMap<String, String>  rTBheader = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String>  rTBRow = null;	
			
			// Get All the header.			
			row_num=0;
			rTBheader.put("path", "XPathKey");
		    for(WebElement trElement : tableHeader)
		    {
		      td_collection =trElement.findElements(By.xpath("td"));
		      colCount = td_collection.size();
		      col_num=1;
		      	
		      for(WebElement tdElement : td_collection)
		      {
		    	System.out.println("col # "+col_num+ "text="+tdElement.getText());
		    	rTBheader.put("h"+col_num, tdElement.getText());
		        col_num++;
		      }		      
		    }			
		    aTBContent.add(0, rTBheader);	
		    printLogs("" + rTBheader);
		    row_num=1;
		    for(WebElement trElement : tr_collection)
		    {
		      rTBRow = new LinkedHashMap<String, String>();
		      td_collection=trElement.findElements(By.xpath("td"));
		      colCount = td_collection.size();
		      col_num=1;
		      for(WebElement tdElement : td_collection)
		      {  	
		    	  //System.out.println("row # "+row_num+", col # "+col_num+ "text="+tdElement.getText());
		    	  rTBRow.put(xPath+"/tbody/tr"+"["+row_num+"]"+"/td"+"["+col_num+"]",tdElement.getText());		    	     	  
		        col_num++;
		      }
		      printLogs("" + rTBRow);		      
		      aTBContent.add(row_num, rTBRow);		      
		      row_num++;
		    }	
		   
		    //print the table content.
		    printLogs("Number of rows: "+ aTBContent.size());
		    printLogs("Number of columns: " + colCount);
		    printLogs("The Table contents are: ");
		    rTBRow = aTBContent.get(0);
		    printLogs("-------------------------------------------------------------------");
		    printLogs("" + rTBRow.values().toString());
		    printLogs("-------------------------------------------------------------------");		    
		    for (int a =1; a<aTBContent.size();a++){   		
		    	printLogs("" + aTBContent.get(a).values().toString());
		    	
		    }      
		    printLogs("-------------------------------------------------------------------");
		    
		}
		catch(Throwable t){
			printError("Exception in method getTableContent.");
			printLogs("Exception returned"+ t.getMessage());
			printFunctionReturn(fn_fail);			
		}		
		printFunctionReturn(fn_pass);		
	}
	
	public boolean VerifyTableContent(String sContains){
		boolean bFound = false;
		try{
			if(aTBContent.isEmpty()){
				this.GetTableContent();
			}	
			for (int a =1; a<aTBContent.size();a++){   		
		    	if(aTBContent.get(a).containsValue(sContains)){
		    		printLogs("Found the value in Table as:");
		    		printLogs("" + aTBContent.get(a).values().toString());		    		
		    		bFound = true;
		    		break;
		    	}		    	
		    }  
			if(!bFound){
				throw new Exception("The table does not contain the value: " + sContains);
			}
		}
		catch(Throwable t){
			printError("Exception in method VerifyTableContent.");
			printLogs("Exception returned"+ t.getMessage());
			printFunctionReturn(fn_fail);	
			return false;
		}	
		printFunctionReturn(fn_pass);
		return bFound;
	}
	
	public Collection<String> GetRowData(String sRowValue){
		boolean bFound = false;
		try{
			if(aTBContent.isEmpty()){
				this.GetTableContent();
			}	
			for (int a =1; a<aTBContent.size();a++){   		
		    	if(aTBContent.get(a).containsValue(sRowValue)){
		    		printLogs("Found the value in Table as:");
		    		printLogs("" + aTBContent.get(a).values().toString());		    		
		    		sValueKey = aTBContent.get(a).values();
		    		iValueIndex = a;
		    		bFound = true;
		    		break;
		    	}		    	
		    }  
			if(!bFound){
				throw new Exception("The table does not contain the value: " + sRowValue);
			}
		}
		catch(Throwable t){
			printError("Exception in method getRowData.");
			printLogs("Exception returned"+ t.getMessage());
			printFunctionReturn(fn_fail);	
			return null;
		}	
		printFunctionReturn(fn_pass);		
		return sValueKey;
	}
	
	public LinkedHashMap<String, String> GetAllRowsWith(String sValue){
		boolean bFound = false;
		LinkedHashMap<String, String> allRowData = new LinkedHashMap<String, String>();
		try{
			if(aTBContent.isEmpty()){
				this.GetTableContent();
			}	
			for (int a =1; a<aTBContent.size();a++){   		
		    	if(aTBContent.get(a).containsValue(sValue)){
		    		printLogs("Found the value in Table as:");
		    		printLogs("" + aTBContent.get(a).values().toString());		    		
		    		allRowData.put(""+a, aTBContent.get(a).values().toString());		    			    		
		    	}		    	
		    }  
			if(!bFound){
				throw new Exception("The table does not contain the value: " + sValue);
			}
		}
		catch(Throwable t){
			printError("Exception in method getRowData.");
			printLogs("Exception returned"+ t.getMessage());
			printFunctionReturn(fn_fail);	
			
		}	
		printFunctionReturn(fn_pass);		
		return allRowData;
	}
	
	public String[] FindContentIn(ArrayList<LinkedHashMap<String, String>> Table, String sContent){
		String[] sTableContent = null;		
		int i =0;
		try{
			
			for (int a =1; a<Table.size();a++){   		
		    	if(Table.get(a).containsValue(sContent)){
		    		printLogs("Found the value in Table as:");
		    		printLogs("" + Table.get(a).values().toString());		    		
		    		sTableContent = new String[Table.get(a).values().size()];
		    		
		    		i++;
		    	}		    	
		    }  
			
		}
		catch(Throwable t){
			printError("Exception in method getRowData.");
			printLogs("Exception returned"+ t.getMessage());
			printFunctionReturn(fn_fail);	
			
		}	
		printFunctionReturn(fn_pass);		
		return sTableContent;
	}
	
	public Set<String> GetDataXPath(String sRowValue){
		boolean bFound = false;
		try{
			if(aTBContent.isEmpty()){
				this.GetTableContent();
			}			
			for (int a =1; a<aTBContent.size();a++){   		
		    	if(aTBContent.get(a).containsValue(sRowValue)){
		    		printLogs("Found the value in Table as:");
		    		printLogs("" + aTBContent.get(a).values().toString());		    		
		    		sTableItemxPath = aTBContent.get(a).keySet();
		    		bFound = true;
		    		break;
		    	}		    	
		    }  
			if(!bFound){
				throw new Exception("The table does not contain the value: " + sRowValue);
			}
		}
		catch(Throwable t){
			printError("Exception in method getRowData.");
			printLogs("Exception returned"+ t.getMessage());
			printFunctionReturn(fn_fail);	
			return null;
		}	
		printFunctionReturn(fn_pass);		
		return sTableItemxPath;
	}
	
	public String GetXpathAtIndex(int iIndex){
		printLogs("Calling GetXpathAtIndex with arguments: " + iIndex);
		String[] xPath;
		try{
			if(aTBContent.isEmpty()){
				this.GetTableContent();
			}			
			xPath = (String[]) aTBContent.get(iIndex).keySet().toArray();
			
		}
		catch(Throwable t){
			printError("Exception in method getRowData.");
			printLogs("Exception returned"+ t.getMessage());
			printFunctionReturn(fn_fail);	
			return null;
		}	
		printFunctionReturn(fn_pass);			
		return xPath[0];
	}
	
	public boolean Exists(){
		printLogs("Calling Exists: " +  this.getXPath());
		try
		{
			int count = 0;			
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
			wbDriver.findElement(By.xpath(xPath)).click();
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
	
	
	public boolean WaitForElement(int iTimeout){
		printLogs("Calling WaitForElement: " + this.getXPath());		
		boolean bFound = false;
		int iIndex=0;
		try{
			wbDriver = getWebDriver();
			for (iIndex=0; iIndex<=iTimeout; iIndex++){
					if(wbDriver.findElements(By.xpath(this.getXPath())).size() > 0){
						bFound = true;
						break;					
					}
					sleep(5);
			}
			
		}
		catch (Throwable t){
			printError("Exception in method WaitForElement.");
			printLogs("Excepton Returned: " + t.getMessage());
			printFunctionReturn(fn_fail);	
		}
		printFunctionReturn(fn_pass);
		return bFound;
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

	
}
