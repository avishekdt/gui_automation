package gui.common.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import gui.common.pages.elements.*;
import gui.common.base.TestBase;


public class BaselinePage {
	// This class is for all operations on Baseline screen.
	
	public String sPageName;
	
	public Button AddBaseLine;	
	public Button PopUpAddBaseline;	
	public Button DeleteConfirmYes;
	public Button DeleteConfirmCancel;
	public Button AddBaseline;	
	public Button CBLApplyFilter;
	public Button CBLSaveBaseline;
	public Button CBLClose;
	
	public TextField CustomBaselineDesc, CustomBaselineVer, CustomBaselineLoc;
	public TextField CBLSearchBox;
	
	public Label CustomBaselineHeader, CBLAdvFilEnclosure;

	public Collapsible CBLAdvancedFilter;
	public Label CBLApplyFilterDialog; //Need to change this to Dialog class
	public Label CBLMessageBar;
	
	public Table CBLApplyFilterTable;
	
	public ComboBox CBLComponentType;
	
	public CheckBox CBLILOFilter;
	public CheckBox CBLDeselectAll;
	public CheckBox CBLSelectAll;
	
	private static BaselinePage BLPage= null;	
	private static WebDriver wbDriver = getWebDriver();
	
	// Enum for Install options
	public enum CompType {
				Software,
				Firmware,
				Both			
	}
	
	// Declare all the items on the screen	
	public BaselinePage(){
		AddBaseLine 		= new Button("BaselineLibrary_AddBaselineButton");	
		PopUpAddBaseline 	= new Button("BaselineLibrary_AddBaselineButtonPopup");		
		DeleteConfirmYes 	= new Button("BaselineLibrary_DeleteConfirmPopupYes");
		DeleteConfirmCancel = new Button("BaselineLibrary_DeleteConfirmPopupCancel");
		CBLApplyFilter		= new Button("BaselineLibrary_CreateCustomBaselineApplyFiltersLabel");
		CBLSaveBaseline		= new Button("BL_CBL_Button_SaveBaseline");
		CBLClose 			= new Button("BL_CBL_Button_Close");
		
		CustomBaselineDesc 	= new TextField("BL_CBL_TextBox_Description");
		CustomBaselineVer	= new TextField("BL_CBL_TextBox_Version");
		CustomBaselineLoc 	= new TextField("BL_CBL_TextBox_OutLoc");
		CBLSearchBox		= new TextField("BL_CBL_Label_Search");
		
		CustomBaselineHeader = new Label("BaselineLibrary_CreateCustomBaselineHeading");
		CBLAdvancedFilter	 = new Collapsible("CBL_Advanced_Filter_css");
		CBLAdvFilEnclosure	 = new Label("BL_CBL_Label_EncNonServerDevices");
		CBLMessageBar		 = new Label("BL_CBL_Text_BottomMsg");
		CBLApplyFilterDialog = new Label("BL_CBL_PopUp_FilterComps");
		
		CBLComponentType	 = new ComboBox("BL_CBL_DropDown_CompType");
		
		CBLILOFilter		 = new CheckBox("BaselineLibrary_CreateCustomBaselineDeviceTypeiLOCheckbox");
		CBLDeselectAll		 = new CheckBox("BL_CBL_CheckBox_DeselectAll");	
		CBLSelectAll		 = new CheckBox("BL_CBL_CheckBox_SelectAll");
		
		CBLApplyFilterTable = new Table("BL_CBL_Table_ReviewComponents");
		
		
		//.//*[@id='hpsum-custom-baselines-panels']
	}
	
	public static BaselinePage getInstance(){
		   if(BLPage == null)
		   {
			   BLPage = new BaselinePage();
			   return BLPage;
		   }else{
			   return BLPage;
		   }
	   }
	
	public static void SelectComponentType(CompType ctType){
		printLogs("Calling SelectComponentType with arguments: ");
		printLogs("Option: " + ctType);
		try
		{
			String xPath = getCurrentClass().CBLComponentType.getXPath();
			
			// Clcik on the compobox to expand the drop down.
			// So that all the elements are visible.
			WebElement wbElement = wbDriver.findElement(By.xpath(xPath));
			wbElement.click();
			
			// Get all references of element a
			List<WebElement> aElemnet = wbDriver.findElements(By.xpath("//*[@rel]"));
			
			for (WebElement we:aElemnet){		
				//get the attribute of references, like software, firmware etc.
				
				String sRel = we.getAttribute("rel");
				if (!sRel.contains("localize")){					
					switch(ctType){
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
					default:
						throw new Exception("Invalid Option specified" + ctType);
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

	public static BaselinePage getCurrentClass() {
	    return new BaselinePage();
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




