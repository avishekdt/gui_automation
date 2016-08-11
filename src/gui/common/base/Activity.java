package gui.common.base;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.testng.TestException;

public class Activity extends SelUtil{
	
	/*
	 * METHODS
	 * getNewlyAddedActivityRows
	 * printNewlyAddedActivityRows
	 */
	
	// Methods related to screen : Activity
	
	//printNewlyAddedActivityRows
	//Print newly added activity rows
	public static boolean printNewlyAddedActivityRows(){
		printLogs("Calling printNewlyAddedActivityRows");
		
		ArrayList<String> activityRows = new ArrayList<String>();
		
		try{
			// get the new Activities and print.
			activityRows = getNewlyAddedActivityRows();
			
			if(activityRows == null) {
				printError("Failed to get activities from activity table.");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			/*
			// This is removed now as print is now part of getNewlyAddedActivityRows itself.
			printLogs("New rows on the Activity Page:");
			for(int i = 0 ; i < activityRows.size() ; i++){
				printLogs(activityRows.get(i));
			}*/
			
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Throwable t){
		     printError("Exception occurred while printing activity table contents.");
		     printFunctionReturn(fn_fail);
		     return false;
		}
	}
	
	// getNewlyAddedActivityRows
	// Returns newly added activity rows
	public static ArrayList<String> getNewlyAddedActivityRows(){
		printLogs("Calling getNewlyAddedActivityRows");
		
		ArrayList<String> newActivityRows = new ArrayList<String>();

		int currentRowCount = 0;
		int newlyAddedRowCount;
		// Note: activityScreenRows_counter - this should initialized in testcase
		
		String xPathActivityTable = "";
		String xPathActivityTableRowCol = "";
		String xPathActivityTableRowColItem = "";
		String rowData = "";
		String colData = "";
		
		try{
			// Go to the Activity Page
			if(!guiSetPage("Activity")) {
				throw new TestException("Failed to load Activity Page.");
			}
			
			xPathActivityTable = OR.getProperty("ActivityPage_ActivityTable");
			
			// Get the current row count
			currentRowCount = getTableRowCount(xPathActivityTable);
			printLogs("Total Activity row Count: " + currentRowCount);
			
			printLogs("Old row count: " + activityScreenRows_counter);
			
			// Calculate the newlyAddedRowCount
			newlyAddedRowCount = currentRowCount - activityScreenRows_counter;
			printLogs("Newly added row count: " + newlyAddedRowCount);
			
			// Assign the new Row count to the Global variable.
			activityScreenRows_counter = currentRowCount;
		   
			printLogs("New rows on the Activity Page:");
			for(int i=1; i <= newlyAddedRowCount; i++){
				for(int j=1; j <= 5; j++){
					xPathActivityTableRowCol = xPathActivityTable + "/tr[" + i + "]/td[" + j + "]";
			      
					if(j==1){
						xPathActivityTableRowColItem = xPathActivityTableRowCol + "/div/div/span";
					    colData = "Status: "+ driver.findElement(By.xpath(xPathActivityTableRowColItem)).getAttribute("innerHTML");
					}
			      
					if(j==2){
						colData = " Source: "+ getTextByActualXpath(xPathActivityTableRowCol);
					}
			      
					if(j==3){
						colData= " Message: "+ getTextByActualXpath(xPathActivityTableRowCol);
					}
			      
					if(j==4){
						colData = "  State: "+ getTextByActualXpath(xPathActivityTableRowCol);
					}
			      
					if(j==5){
						colData = "  Last Updated: "+ getTextByActualXpath(xPathActivityTableRowCol);
					}
			   
					rowData = rowData + colData;
				}
				// Add the row contents to the array list
				printLogs(rowData);
				newActivityRows.add(rowData);
				rowData = "";
			}
			
			printFunctionReturn(fn_pass);
			return newActivityRows;
		}
		catch(Throwable t){
		     printError("Error occured while getting activity table contents.");
		     printFunctionReturn(fn_fail);
		     return null;
		}
	}
	
	
	
	

}
