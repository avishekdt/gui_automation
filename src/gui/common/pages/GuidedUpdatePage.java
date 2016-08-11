package gui.common.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import gui.common.base.SelUtil;
import gui.common.base.TestBase;
import gui.common.pages.elements.*;


/* Web Element Event related Methods.
 * Author: nimi.ummer@hpe.com
 * GetSelectedInstallOption
 * GetInstallOptions
 * SelectAdvancedOption
 * GetRebootOption
 * SelectRebootOption
 * SelectInstallOption 
 * WaitForElement
 */
public class GuidedUpdatePage {
		// This class is for all operations on GU screen.	
		
		// All Radio Button declaration
		public RadioButton GUInteractive;	
		public RadioButton GUAutomatic;	
		public RadioButton GULocalhostOnly;
		
		// All Radio Button declaration
		public CheckBox AssignBaseline;
		public CheckBox EnableVerbose;
		
		// All Radio Button declaration
		public ComboBox SearchBaseline;
		public ComboBox SearchAddBaseline;
		public ComboBox InstallOptions;
		public ComboBox RebootOptions;
		
		// All Radio Button declaration
		public Button GUOK;
		public Button GUStep1Next;
		public Button GUDeploy;
		public Button GUDeployAbort;
		public Button GUStartOver, GUInventoryStartOver;
		public Button GUInventoryAbort;
		public Button GUInventoryReboot;
		public Button AdvOptionsOK;
		public Button AdvOptionsClose;
		public Button ReviewPreviousStep;

		// All Radio Button declaration
		public Label CurrentBaseline, CurrentAddBaseline;
		public Label GUInstallProgress, GUDeployAbortText;
		public Label ActionDropDown;
		public Label AdvancedOptionsHeader;
		public Label GuidedUpdateStep1Header, GuidedUpdateDialogHeader;
		public Label InventoryProgress, InventoryStatus; 
		public Label GUActionDropDown;
		
		// All Radio Button declaration
		public TextField RebootDelay;
		public TextField RebootMessage;	
		public TextField ReviewTableSearch;
		
		// Table declaration
		public Table ReviewDeployTable;
		
		private static WebDriver wbDriver = getWebDriver();
		
		// Enum for Select Baseline dropdown.
		public enum BL {
			Baseline,
			AdditionalBaseline
		}
		
		// Enum for Install options
		public enum InstallOptions {
			Software,
			Firmware,
			Both,
			UpgradeBoth,
			UpgradeFirmware,
			UpgradeSoftware,
			DowngradeFirmware,
			DowngradeSoftware,
			DowngradeBoth,
			RewriteFirmware,
			RewriteSoftware,
			RewriteBoth
		}
		
		// Enum for Reboot options
		public enum RebootOption {
			Never,
			IfNeeded,
			Always
		}
		private static GuidedUpdatePage GUPage= null;	
			
		   
		// Declare all the items on the screen	
		private GuidedUpdatePage(){
			GUInteractive 		= new RadioButton("GuidedUpdate_InteractiveRadioButton");	
			GUAutomatic 		= new RadioButton("GuidedUpdate_AutomaticRadioButton");		
			GULocalhostOnly		= new RadioButton("GuidedUpdate_LocalhostOnlyRadioButton");
			
			GUOK 				= new Button("GuidedUpdate_OkButton");
			GUStep1Next			= new Button("GuidedUpdate_Step1NextButton");
			GUDeploy 			= new Button("GuidedUpdate_Step2DeployButton");
			GUDeployAbort		= new Button("GuidedUpdate_Step3AbortButton");
			GUInventoryStartOver = new Button("GuidedUpdate_Step1ResetButton");
			GUStartOver 		= new Button("GuidedUpdate_Step3ResetButton");
			GUInventoryAbort	= new Button("GuidedUpdate_Step1AbortButton");
			GUInventoryReboot	= new Button("GuidedUpdate_Step1Reboot");
			AdvOptionsOK		= new Button("GuidedUpdate_AdvOptionOK");
			AdvOptionsClose		= new Button("GuidedUpdate_AdvOptionClose");
			ReviewPreviousStep		= new Button("GuidedUpdate_Step2BackButton");
			
			AssignBaseline		= new CheckBox("GuidedUpdate_AssignBaseline");
			EnableVerbose		= new CheckBox("GuidedUpdate_EnableVerbose");
			
			SearchBaseline 		= new ComboBox("GuidedUpdate_BaselineSearch");
			SearchAddBaseline 	= new ComboBox("GuidedUpdate_AdditonalSearch");
			InstallOptions      = new ComboBox("GuidedUpdate_InstallOption");
			RebootOptions		= new ComboBox("GuidedUpdate_RebootOption");
			
			CurrentBaseline 	= new Label("GuidedUpdate_CurrentBaseline");
			CurrentAddBaseline 	= new Label("GuidedUpdate_CurrentAddBaseline");
			GUInstallProgress	= new Label("GuidedUpdate_InstallProgressText");
			InventoryProgress   = new Label("GuidedUpdate_InventoryProgress");
			InventoryStatus		= new Label("GuidedUpdate_InventoryStatus");					
			GUDeployAbortText   = new Label("GuidedUpdate_Step3Text");
			GUActionDropDown 	= new Label("GuidedUpdate_ActionsLabel");
			ActionDropDown		= new Label("GuidedUpdate_ActionsLabel");
			AdvancedOptionsHeader = new Label("GuidedUpdate_AdvOptionHeader");
			GuidedUpdateStep1Header = new Label("GuidedUpdate_Step1Heading");
			GuidedUpdateDialogHeader = new Label("GuidedUpdate_PopupDialog");
			
			RebootDelay			= new TextField("GuidedUpdate_RebootDelay");
			RebootMessage		= new TextField("GuidedUpdate_RebootMessage");
			ReviewTableSearch   = new TextField("GuidedUpdate_Step2SearchInput");
			
			ReviewDeployTable	= new Table("GuidedUpdate_ReviewDeployTable");			
			
		}
		
		public static GuidedUpdatePage getInstance(){
			   if(GUPage == null)
			   {
				   GUPage = new GuidedUpdatePage();
				   return GUPage;
			   }else{
				   return GUPage;
			   }
		   }
		
		/* Method: GetSelectedInstallOption 
		 * Gets the selected Install Option.
		 */				

		public static String GetSelectedInstallOption(){
			printLogs("Calling getSelectedInstallOption");	
			String xPath;
			String sText = null;
			WebElement wbElement, wbSpan;
			try
			{
				xPath = getCurrentClass().InstallOptions.getXPath();
				// Get the combobox webElement object.			
				wbElement = wbDriver.findElement(By.xpath(xPath));
				wbSpan = wbElement.findElement(By.xpath("//span[@class='selectBox-label']"));
				sText = wbSpan.getText();
				printLogs("The selected item is:" + sText);	
									
			}
			catch (Throwable t){
				printError("Exception in method Select.");
				printLogs("Excepton Returned: " + t.getMessage());
				printFunctionReturn("Function-Fail");				
			}	
			printFunctionReturn("Function-Pass");
			return sText;
		}
		
		/* Method: GetSelectedRebootOption 
		 * Gets the selected Reboot Option.
		 */				
		public static String GetSelectedRebootOption(){
			printLogs("Calling GetSelectedRebootOption");	
			String xPath;
			String sText = null;
			WebElement wbElement, wbSpan;
			try
			{
				xPath = getCurrentClass().RebootOptions.getXPath();
				// Get the combobox webElement object.			
				wbElement = wbDriver.findElement(By.xpath(xPath));
				wbSpan = wbElement.findElement(By.xpath("//span[@class='selectBox-label']"));
				sText = wbSpan.getText();
				printLogs("The selected item is:" + sText);	
									
			}
			catch (Throwable t){
				printError("Exception in method Select.");
				printLogs("Excepton Returned: " + t.getMessage());
				printFunctionReturn("Function-Fail");				
			}	
			printFunctionReturn("Function-Pass");
			return sText;
		}
		
		/* Method: GetSelectedInstallOption 
		 * Gets the content of Install Option drop down.
		 */
		
		public static String[] GetInstallOptions(){
			printLogs("Calling GetInstallOptions");	
			String xPath; 
			String[] sContent = new String[12];
			
			int i = 0;
			int ul_index = 0;
			
			boolean bFound = false;
			
			WebElement wbElement;
			try
			{
				
				xPath = getCurrentClass().InstallOptions.getXPath();
				// Clcik on the compobox to expand the drop down.
				// So that all the elements are visible.
				printLogs("Clicking on the drop down to display all the options available");
				wbElement = wbDriver.findElement(By.xpath(xPath));
				wbElement.click();
				
				// Get all references of element a
				printLogs("Get the list that does not have the attribute set as 'display: none;'");
				List<WebElement> aElemnet = wbDriver.findElements(By.xpath("//ul[@class='selectBox-dropdown-menu selectBox-options']"));
				printLogs("Successfully got the values: " + aElemnet);
				ul_index = 1;
				for (WebElement we:aElemnet){	
					String sRel = we.getAttribute("style"); 
					if(!bFound){
						if (!sRel.contains("display: none")){
							printLogs("we.getAttributes " + we.getAttribute("style"));							
							bFound = true;					
						}
					}
					if(bFound){
						printLogs("Found ul at " + ul_index + " webelement " + we.getTagName());
						List<WebElement> aList = we.findElements(By.tagName("a"));
						printLogs("" + aList.size());
						for (WebElement lia: aList){
							if(lia.getText()!= ""){
								sContent[i] = lia.getText();
								printLogs("the lia list"+ lia.getText());
								i++;
							}
						}
						break;
					} 
					ul_index ++;
				}
				if(!bFound){
					throw new Exception("Unable to get the contents.");
				}
				// Collapse the combobox
				wbElement.click();
				
				
				
			}
			catch (Throwable t){
				printError("Exception in method Select.");
				printLogs("Excepton Returned: " + t.getMessage());
				printFunctionReturn("Function-Fail");				
			}	
			printFunctionReturn("Function-Pass");
			return sContent;
		}
		
		/* Method: SelectAdvancedOption
		 * Select the option 'Advanced Option' from 'Actions' Menu.
		*/		
		public static boolean SelectAdvancedOption() {
			return getCurrentClass().ActionDropDown.Select("Advanced options"); 
		}
		
		/* Method: GetRebootOptions
		 * Select the reboot options from the reboot option drop down.
		*/
		public static String[] GetRebootOptions(){
			printLogs("Calling GetRebootOptions");	
			String xPath; 
			String[] sContent = new String[12];
			
			int i = 0;
			int ul_index = 0; 
			boolean bFound = false;
			
			WebElement wbElement;
			try
			{
				
				xPath = getCurrentClass().RebootOptions.getXPath();
				// Clcik on the compobox to expand the drop down.
				// So that all the elements are visible.
				printLogs("Clicking on the drop down to display all the options available");
				wbElement = wbDriver.findElement(By.xpath(xPath));
				wbElement.click();
				
				// Get all references of element a
				printLogs("Get the list that does not have the attribute set as 'display: none;'");
				List<WebElement> aElemnet = wbDriver.findElements(By.xpath("//ul[@class='selectBox-dropdown-menu selectBox-options']"));
				printLogs("Successfully got the values: " + aElemnet);
				ul_index = 1;
				for (WebElement we:aElemnet){	
					String sRel = we.getAttribute("style"); 
					if(!bFound){
						if (!sRel.contains("display: none")){
							printLogs("we.getAttributes " + we.getAttribute("style"));							
							bFound = true;					
						}
					}
					if(bFound){
						printLogs("Found ul at " + ul_index + " webelement " + we.getTagName());
						List<WebElement> aList = we.findElements(By.tagName("a"));
						printLogs("" + aList.size());
						for (WebElement lia: aList){
							if(lia.getText()!= null && !(lia.getText().isEmpty()) && lia.getText() != ""){
								sContent[i] = lia.getText();
								printLogs("the lia list"+ lia.getText());
								i++;
							}
						}
						break;
					} 
					ul_index ++;
				}
				if(!bFound){
					throw new Exception("Unable to get the contents.");
				}
				// Collapse the combobox
				wbElement.click();
			}
			catch (Throwable t){
				printError("Exception in method Select.");
				printLogs("Excepton Returned: " + t.getMessage());
				printFunctionReturn("Function-Fail");				
			}	
			printFunctionReturn("Function-Pass");
			return sContent;
		}
		
		/* Method: SelectRebootOption
		 * Select a reboot option.
		*/
		public static void SelectRebootOption(RebootOption sOption){
			printLogs("Calling SelectInstallOption with arguments: ");
			printLogs("Option: " + sOption);
			try
			{
				String xPath = getCurrentClass().RebootOptions.getXPath();
				// Clcik on the compobox to expand the drop down.
				// So that all the elements are visible.
				WebElement wbElement = wbDriver.findElement(By.xpath(xPath));
				wbElement.click();
				
				// Get all references of element a
				List<WebElement> aElemnet = wbDriver.findElements(By.xpath("//*[@rel]"));
				
				for (WebElement we:aElemnet){	
					String sRel = we.getAttribute("rel");
					if (!sRel.contains("localize")){
						switch(sOption){
						case Never:
							if(sRel.contains("Never")){						
								we.click();							
							}
							break;
						case IfNeeded:
							if(sRel.contains("If Needed")){						
								we.click();							
							}
							break;
						case Always:
							if(sRel.contains("Always")){						
								we.click();							
							}
							break;
						default:
							throw new Exception("Invalid Option specified" + sOption);
						}
					}					
				}				
				printFunctionReturn("Function-Pass");	
			}
			catch (Throwable t){
				printError("Exception in method Select.");
				printLogs("Excepton Returned: " + t.getMessage());
				printFunctionReturn("Function-Fail");				
			}
			
		}
		
		/* Method: SelectInstallOption
		 * Select a Install option.
		*/
		public static void SelectInstallOption(InstallOptions Option){
			printLogs("Calling SelectInstallOption with arguments: ");
			printLogs("Option: " + Option);
			try
			{
				String xPath = getCurrentClass().InstallOptions.getXPath();
				
				// Clcik on the compobox to expand the drop down.
				// So that all the elements are visible.
				WebElement wbElement = wbDriver.findElement(By.xpath(xPath));
				wbElement.click();
				
				// Get all references of element a
				List<WebElement> aElemnet = wbDriver.findElements(By.xpath("//*[@rel]"));
				
				for (WebElement we:aElemnet){		
					//get the attribute of references, like software, firmware etc.
					// The html structure is:
					// <ul class="selectBox-dropdown-menu selectBox-options" style="left: 336px; top: 247px; display: none;">
					// <li><a rel="upgradeBoth">Upgrade Both - Allow upgrades to already installed components and installation of new software packages</a>
					// </li><li><a rel="upgradeFirmware">Upgrade Firmware - Allow upgrades to newer versions of already installed firmware components</a>
					// .....
					//
					String sRel = we.getAttribute("rel");
					if (!sRel.contains("localize")){
						
						switch(Option){
						case Software:
							if(sRel.contains("software")){						
								we.click();							
							}
							break;
						case Firmware:
							if(sRel.contains("firmware")){						
								we.click();							
							}
							break;
						case Both:
							if(sRel.contains("both")){						
								we.click();							
							}
							break;
						case UpgradeBoth:
							if(sRel.contains("upgradeBoth")){						
								we.click();							
							}
							break;
						case UpgradeFirmware:
							if(sRel.contains("upgradeFirmware")){						
								we.click();							
							}
							break;
						case UpgradeSoftware:
							if(sRel.contains("upgradeSoftware")){						
								we.click();							
							}
							break;
						case DowngradeFirmware:
							if(sRel.contains("downgradeFirmware")){						
								we.click();							
							}
							break;
						case DowngradeSoftware:
							if(sRel.contains("downgradeSoftware")){						
								we.click();							
							}
							break;
						case DowngradeBoth:
							if(sRel.contains("downgradeBoth")){						
								we.click();							
							}
							break;
						case RewriteFirmware:
							if(sRel.contains("rewriteFirmware")){						
								we.click();							
							}
							break;
						case RewriteSoftware:
							if(sRel.contains("rewriteSoftware")){						
								we.click();							
							}
							break;
						case RewriteBoth:
							if(sRel.contains("rewriteBoth")){						
								we.click();							
							}
							break;
						default:
							throw new Exception("Invalid Option specified" + Option);
						}				
					}
				}				
				printFunctionReturn("Function-Pass");
				
			}
			catch (Throwable t){
				printError("Exception in method Select.");
				printLogs("Excepton Returned: " + t.getMessage());
				printFunctionReturn("Function-Fail");				
			}
			
		}
		public static boolean ClickOnTableContent(String xPath){
			printLogs("Calling ClickOnTableContent with arguments: " + xPath);			
			try{
				SelUtil.clickByXpath(xPath);
				
			}
			catch(Throwable t){
				printError("Exception in method getRowData.");
				printLogs("Exception returned"+ t.getMessage());
				printFunctionReturn("Function-Fail");	
				return false;
			}	
			printFunctionReturn("Function-Pass");			
			
			return true;
		}
				
		public static GuidedUpdatePage getCurrentClass() {
		    return new GuidedUpdatePage();
		  }

		private static void printFunctionReturn(String statusToPrint) {
			TestBase.printFunctionReturn(statusToPrint);
			
		}

		private static void printError(String msg) {
			TestBase.printError(msg);
			
		}

		private static void printLogs(String msg) {
			TestBase.printLogs(msg); 
			
		}

		private static WebDriver getWebDriver() {
			return TestBase.getWebDriver();
		}

		
		
}


