package gui.common.base;

import org.openqa.selenium.By;
import org.testng.TestException;

public class Reports extends SelUtil {
	
	//Method to get the reports:
		/*
		 * 1. Check for inventory or deploy phase
		 * 2. In inventory phase deploy details check box is disabled.
		 * 3. 
		 */
		
	public static String GetReportTypeXpath(String reportType){
		try{
			
		}
		catch(Throwable t){
			
		}
		return "";
	}
	
		public static boolean GetReport(String Report){
			printLogs("Calling get report functions");
			try{
				String[] Reportypesarry = {"Reports_InventoryCheckBox", "Reports_FirmwareDetailsCheckBox", 
				                           "Reports_DeployReviewCheckBox", "Reports_FailedDependencyDetailsCheckBox",
				                           "Reports_DeployDetyailsCheckBox", "Reports_CombinedReportsCheckBox"};
				if(driver.findElement(By.xpath(OR.getProperty("Report_ActionDropDown"))).getAttribute("class").contains("hp-disabled")){
					throw new TestException("Reports option is not enabled in the action's dropdown");
				}else{
					printLogs("Reports option is enabled and continuing the operation");
				}
				// Wait for Reports page.
				if(!waitForPage("Common_NewWindow")) {
				printFunctionReturn(fn_fail);				
				return false;
				}
				Report.toLowerCase();
				if(Report.contains("inventory")){
					for(int i=0;i<Reportypesarry.length;i++){
						
						
						
					}
					
					
				
				}
			
			}
			catch(Throwable t){
				printError("Error occurred while getting the reports :" + t.getMessage());
				printFunctionReturn(fn_fail);
				return false;
			}
			printFunctionReturn(fn_pass);	
			return true;		
		}
		
	 public static boolean selectReportType(String NodeStatus, String reportType){
		 printLogs("Calling selectReportType with parameters "+ NodeStatus + " " + reportType);
		 try{		 
			 if((NodeStatus.toLowerCase().contains("inventory"))&& 
					 (reportType.toLowerCase().contains("Reports_DeployDetyailsCheckBox"))){
				 if(!driver.findElement(By.xpath(OR.getProperty("Reports_DeployDetyailsCheckBox"))).isEnabled()){
						printError("Deploy details check box is enabled in the Reports page when invenotry is completed");
						appendHtmlComment("Deploy details check box is enabled in the Reports page when invenotry is completed");
					}
				printFunctionReturn(fn_pass);
				return true;
			 }
			 if(!driver.findElement(By.xpath(OR.getProperty(reportType))).isEnabled()){
					printError("Report type check box for "+ reportType + " is not enabled");
					appendHtmlComment("Report type check box for "+ reportType + " is not enabled");
				}
			 if(!driver.findElement(By.xpath(OR.getProperty(reportType))).isSelected()){
				 driver.findElement(By.xpath(OR.getProperty(reportType))).click();
				 printLogs("Checked the "+ reportType + " checkbox");
				}
			 else{
				 printError("Select report type check box for "+ reportType + " is not enabled");
				 appendHtmlComment("Select report type check box for "+ reportType + " is not enabled");
			 }
		 }
		 catch(Throwable t){
				printError("Error occurred while checking the checkbox :" + t.getMessage());
				printFunctionReturn(fn_fail);
				return false;
			}
			printFunctionReturn(fn_pass);	
			return true;	
		}
	 
	 public static boolean unSelectReportType(String NodeStatus, String reportType){
		 printLogs("Calling selectReportType with parameters "+ NodeStatus + " " + reportType);
		 try{	 
			
			 if(driver.findElement(By.xpath(OR.getProperty(reportType))).isEnabled()){
				 
				 if(!driver.findElement(By.xpath(OR.getProperty(reportType))).isSelected()){
					 driver.findElement(By.xpath(OR.getProperty(reportType))).click();
					 printLogs("Checked the "+ reportType + " checkbox");
					}
				 else{
					 printError("Select report type check box for "+ reportType + " is not enabled");
					 appendHtmlComment("Select report type check box for "+ reportType + " is not enabled");
				 	}				
				}
			 else{
				 printError("Report type check box for "+ reportType + " is not enabled");
				appendHtmlComment("Report type check box for "+ reportType + " is not enabled");
			 }
			 
		 }
		 catch(Throwable t){
				printError("Error occurred while checking the checkbox :" + t.getMessage());
				printFunctionReturn(fn_fail);
				return false;
			}
			printFunctionReturn(fn_pass);	
			return true;	
		}
	 
		//reportsGenerationCheck
		//Check whether Report is generated correctly 
		//ReportingStage: At what stage report is generated (BaselineInventoryReport,NodeInventoryReport,NodeDeployReport)
		public static boolean reportsGenerationCheck(String reportingStage) {
			printLogs("Calling ReportsGenerationCheck with values: " + reportingStage);
			
			String xPathReportCenterTable = "";
			try{ 
			    xPathReportCenterTable = OR.getProperty("Reports_ReportCenterTable");
					
			    if(reportingStage.equals("BaselineInventoryReport")){
					 
			    	//check whether Report:Inventory success image is present
					if(!SelUtil.checkElementPresenceByActualXpath(xPathReportCenterTable + "/tr[1]/td[7]/a/img")){
						printError("Report generation success image is not present ");
						printFunctionReturn(fn_fail);
			    		return false;
					}
					
					//check whether Report:Inventory 'view details' link is present 
					if(!SelUtil.checkElementPresenceByActualXpath(xPathReportCenterTable + "/tr[1]/td[7]/a")){
						printError("Report 'view details' link is not present");
						printFunctionReturn(fn_fail);
			    		return false;
					}
					printLogs("Report contains information on all selected report types.");
					printFunctionReturn(fn_pass); 
					return true;
				} 
				 
				if(reportingStage.equals("NodeInventoryReport")){
					
					for(int i=3; i<=8; i++){
						 
						if(i==5){
							continue;
						 }
						 
						 //check whether Report generated image is present
						 if(!SelUtil.checkElementPresenceByActualXpath(xPathReportCenterTable + "/tr[1]/td[" + i + "]/a/img")){
							 printError("Report generation success image is not present.");
							 printFunctionReturn(fn_fail);
					    	 return false;
						 }
						
						 //check whether Report 'view details' link is present 
						 if(!SelUtil.checkElementPresenceByActualXpath(xPathReportCenterTable + "/tr[1]/td[" + i + "]/a")){
							 printError("Report 'view details' link is not present.");
							 printFunctionReturn(fn_fail);
					    	 return false;
						 }
					 }
					 printLogs("Report contains information on all selected report types.");
					 printFunctionReturn(fn_pass); 
					 return true;
				}
				 
				if(reportingStage.equals("NodeDeployReport")){
					 
					for(int i=3; i<=8; i++){
						 
						//check whether Report generated image is present
						if(!SelUtil.checkElementPresenceByActualXpath(xPathReportCenterTable + "/tr[1]/td[" + i + "]/a/img")){
							printError("Report generation success image is not present.");
							printFunctionReturn(fn_fail);
					    	return false;
						}
						
						//check whether Report 'view details' link is present 
						if(!SelUtil.checkElementPresenceByActualXpath(xPathReportCenterTable + "/tr[1]/td[" + i + "]/a")){
							printError("Report 'view details' link is not present.");
							printFunctionReturn(fn_fail);
					    	return false;
						}
					}
					printLogs("Report contains information on all selected report types.");
					printFunctionReturn(fn_pass); 
					return true;
				} 
				printFunctionReturn(fn_pass);
				return true;
			}
			catch(Throwable t){
				printError("Error occurred while checking for successful report generation.");
				printFunctionReturn(fn_fail);
			    return false;
			}
		}
		
		//reportGeneration
		//option: CssBaselineActions(Baseline) or CssNodesActions(Nodes) 
		//ReportingStage: At what stage report is generated (BaselineInventoryReport,NodeInventoryReport,NodeDeployReport)
		public static boolean reportGeneration(String option,String reportingStage){
			printLogs("Calling ReportGeneration with values: " + option + ", " + reportingStage);
			
			String xPathReportsCenterFormat = "";
			String ReportsCenterFormat = "" ;
			String ReportsCenterFormatHtml = "html";
			String ReportsCenterFormatXml = "xml";
			String ReportsCenterFormatCsv = "csv";
			String xPathReportsCenterStatus = "";
			String ReportsCenterStatus = "";
			String ReportsCenterStatusCompleted = "Completed";
			
			// Create the screenshots folder.
			// Find Present Working Directory			
			System.out.println("Present Working Dir = " + presentWorkingDir);
			
			String reportLoc = logsFolder + "/report/" + "/" + reportFolder + "/";
			createFolder(reportLoc);
			reportLoc = presentWorkingDir + "/" + logsFolder + "/report/" + "/" + reportFolder + "/";   
			try{
				xPathReportsCenterFormat = OR.getProperty("Reports_ReportCenterTable")+"/tr[1]/td[9]";
				xPathReportsCenterStatus = OR.getProperty("Reports_ReportCenterTable")+"/tr[1]/td[11]";
				
				//select report from action drop down
				if(!selectActionDropDownOption(option,"Reports")){
				    printError("Unable to select on Action: Reports.");
				    printFunctionReturn(fn_fail);
				    return false;
				}
					
				//wait for the Report window
				if(!checkElementPresenceByXpath("Common_NewWindowHeading")){
				    printError("Report page is not present.");
				    printFunctionReturn(fn_fail);
				    return false;
				}
				captureScreenShot();
				
				//if(reportLoc!=null){
				// Type reports path
				if(!sendKeysByXpath("Reports_DirectoryPathTextBox", reportLoc)) {
					printFunctionReturn(fn_fail);
					return false;
				}
				captureScreenShot();
				
				//click on Browse button
				if(!clickByXpath("Reports_ReportFrmat_HtmlRadioButton")){
					printError("HTML is not selected as report format.");
				}
				captureScreenShot();
				
				// Check browse title is present
				if(!waitForPage("BaselineLibrary_AddBaselineEnterDirectoryPathBrowseHeading")) {
					printFunctionReturn(fn_fail);
					//return false;
				}
				
				captureScreenShot();
				
				//click on ok button
				if(!clickByXpath("Reports_ReportPathEnterDirectoryPathBrowseOkButton")){
					printError("HTML is not selected as report format.");
				}				
				/*}else{
					printLogs("Reports are saved in the SPP location");
				}*/
				    	
				//click on html in report format
				if(!clickByXpath("Reports_ReportFrmat_HtmlRadioButton")){
					printError("HTML is not selected as report format.");
				}
				captureScreenShot();
						
				//click on generate button
				if(!clickByXpath("Reports_GenerateButton")){
					printError("Unable to click on Generate button.");
				    printFunctionReturn(fn_fail);
				    return false;
				}
						
				//wait till report generation gets over
				if(!activeElementPresence("Generate Report in Background")){
					printError("Failed to wait for html report generation."); 
				    printFunctionReturn(fn_fail);
				    return false;
				}
						
				//check whether html is present in report center table
			    ReportsCenterFormat = SelUtil.getTextByActualXpath(xPathReportsCenterFormat);
						
				if(!compareTexts(ReportsCenterFormatHtml,ReportsCenterFormat)){
					printError("HTML report is not generated.");
				}
				captureScreenShot();
						
				//check whether completed status has been displayed in report center table
				ReportsCenterStatus = SelUtil.getTextByActualXpath(xPathReportsCenterStatus);
						
				if(!compareTexts(ReportsCenterStatusCompleted,ReportsCenterStatus)){
					printError("HTML Report Generation is not successful.");
				}
						
				//check whether report generation is indicated in respective columns. 
				if(!reportsGenerationCheck(reportingStage)){
					printFunctionReturn(fn_fail);
				    return false;
				}
						
				printLogs("HTML report generation is successful.");
						
				//click on xml in report format
				if(!clickByXpath("Reports_ReportFrmat_XmlRadioButton")){
					printError("XML is not selected as report format.");
				}
				captureScreenShot();
						
				//click on generate button
				if(!clickByXpath("Reports_GenerateButton")){
					printError("Unable to click on Generate button.");
				    printFunctionReturn(fn_fail);
				    return false;
				}
						
				//wait till report generation gets over
				if(!activeElementPresence("Generate Report in Background")){
					printError("Failed to wait for xml report generation.");
				    printFunctionReturn(fn_fail);
				    return false;
				}
						
				//check whether xml is present in report center table
			    ReportsCenterFormat = SelUtil.getTextByActualXpath(xPathReportsCenterFormat);
						
				if(!compareTexts(ReportsCenterFormatXml,ReportsCenterFormat)){
					printError("XML report is not generated.");
				}
				captureScreenShot();
					
				//check whether completed status has been displayed in report center table
			    ReportsCenterStatus = SelUtil.getTextByActualXpath(xPathReportsCenterStatus);
						
				if(!compareTexts(ReportsCenterStatusCompleted,ReportsCenterStatus)){
					printError("XML Report Generation is not successful.");
				}
						
				//check whether report generation is indicated in respective columns. 
				if(!reportsGenerationCheck(reportingStage)){
					printFunctionReturn(fn_fail);
				    return false;
				}
						
				printLogs("XML report generation is successful.");
						
				//click on csv in report format
				if(!clickByXpath("Reports_ReportFrmat_CsvRadioButton")){
					printError("CSV is not selected as report format.");
				}
				captureScreenShot();
						
				//click on generate button
				if(!clickByXpath("Reports_GenerateButton")){
					printError("Unable to click on Generate button.");
				    printFunctionReturn(fn_fail);
				    return false;
				}
						
				//wait till report generation gets over
				if(!activeElementPresence("Generate Report in Background")){
					printError("Failed to wait for xml report generation.");
				    printFunctionReturn(fn_fail);
				    return false;
				}
						
				//check whether html is present in report center table
			    ReportsCenterFormat = SelUtil.getTextByActualXpath(xPathReportsCenterFormat);
						
				if(!compareTexts(ReportsCenterFormatCsv,ReportsCenterFormat)){
					printError("CSV report is not generated.");
				}
				captureScreenShot();
						
				//check whether completed status has been displayed in report center table
			    ReportsCenterStatus = SelUtil.getTextByActualXpath(xPathReportsCenterStatus);
						
				if(!compareTexts(ReportsCenterStatusCompleted,ReportsCenterStatus)){
					printError("CSV Report Generation is not successful");
				}
						
				//check whether report generaiton is indicated in respective columns. 
				if(!reportsGenerationCheck(reportingStage)){
					printFunctionReturn(fn_fail);
				    return false;
				}
						
				printLogs("CSV report generation is successful.");
						
				//click on close button
				if(!clickByXpath("Reports_CloseButton")){
					printError("Failed to click on close button");
				    printFunctionReturn(fn_fail);
				    return false;
				}
				captureScreenShot();
					
				printFunctionReturn(fn_pass);	
				return true;
				    	
			}
			catch(Throwable t){
				printError("Error occurred while generating report.");
				printFunctionReturn(fn_fail);
				return false;
			}
		}

}
