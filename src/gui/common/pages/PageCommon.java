package gui.common.pages;

import gui.common.base.SelUtil;
import gui.common.util.ErrorUtil;


public class PageCommon extends SelUtil{

	public enum Pages{
		LocalhostGuidedUpdateDialog,
		GuidedUpdateStep1,
		GuidedUpdateStep2,
		GuidedUpdateStep3,
		BaselineDeleteConfirmDialog,
		BaselineLibrary,
		GuidedUpdateAdvancedOptions,		
	}
	
	public static boolean WaitForPage(Pages pPageName, int iTimeout){
	printLogs("Calling WaitForPAge");	
	
	int iMaxWaitTime = 0;
	iMaxWaitTime = iTimeout;	
	
	boolean bFail = false; 
	
	GuidedUpdatePage GUPage = GuidedUpdatePage.getInstance();;
	
	if (iMaxWaitTime == 0){
		iMaxWaitTime = 60;
	}
	
	try
	{		
		switch(pPageName){	
		case LocalhostGuidedUpdateDialog:
			if(!GUPage.GuidedUpdateDialogHeader.WaitForElement(iMaxWaitTime)) {
				bFail = true;
				break;
			}
			break;
		case GuidedUpdateStep1:
			if(!GUPage.GuidedUpdateStep1Header.WaitForElement(iMaxWaitTime)){
				bFail = true;
				break;
			}	
			break;
		case GuidedUpdateStep2:
			if(!waitForElementByXpath("GuidedUpdate_Step2Heading", iMaxWaitTime, true)){
				bFail = true;
				break;
			}		
			break;
		case GuidedUpdateStep3:
			if(!waitForElementByXpath("GuidedUpdate_Step3Heading", iMaxWaitTime, true)){
				bFail = true;
				break;
			}			
			break;
		case BaselineDeleteConfirmDialog:
			if(!waitForElementByXpath("BaselineLibrary_DeleteConfirmPopupHeading", iMaxWaitTime, true)){
				bFail = true;
				break;
			}	
			break;
		case BaselineLibrary:
			if(!waitForElementByXpath("BaselineLibrary_Heading", iMaxWaitTime, true)){
				bFail = true;
				break;
			}	
			break;
		case GuidedUpdateAdvancedOptions:
			if(!GUPage.AdvancedOptionsHeader.WaitForElement(iMaxWaitTime)){
				bFail = true;
				break;
			}	
			break;
		default:
			break;		
		
		}
		if(bFail){
			printError("Failed to wait for page: '" + pPageName +"'");
			printFunctionReturn(fn_fail);
			return false;
		}
		printLogs("Successfully waited for page.");
		printFunctionReturn(fn_pass);
		return true;
	}
	catch(Throwable t) {
		ErrorUtil.addVerificationFailure(t);			
		printError("Exception occurred while waiting for page.");
		printFunctionReturn(fn_fail);
		return false;
	}
		
	}

	
	
}
