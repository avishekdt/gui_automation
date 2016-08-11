package gui.common.base;

//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;

import gui.common.util.ErrorUtil;

import org.openqa.selenium.By;
/*import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.TestException;*/

public class ContextScreen extends SelUtil {
	//Methods related to vc
	/*
	 * 
	 */	
	public static boolean baseline = false,node_loopback = false ;
	
	public static boolean addnodebase (String menuLink, String VerifyPage, String AddButton, 
			String remoteHostIp, String remoteHostDesc,
			   String remoteHostType) {
	printLogs("Calling addNodeBase with values : " + remoteHostIp + ", " + remoteHostDesc + ", " + remoteHostType);
	
	try {
		// 1. Select the VC from the Main Menu.		
		if(!CommonHpsum.clickLinkOnMainMenu(menuLink)) {
		printFunctionReturn(fn_fail);				
		return false;
		}			
		printLogs("Successfully clicked Virtual Connect link of Main Menu");
		sleep(10);			
		
		captureScreenShot();
		
		// Verify Page: Nodes
		if(!verifyPage(VerifyPage)) {
			printFunctionReturn(fn_fail);			
			return false;
		}
		printLogs("Successfully landed to " + VerifyPage + " page");
		
		// 2. Verify		
		// Verify button exists: Add button
		if(!verifyButtonStatus(AddButton, "enabled")) {
		printError("Failed verifyButtonStatus.");
		}
				
		/*
		* 3. Click on Add button.
		* 		Verify Buttons - Add, Start Over, Close
		* 4. Enter all the details of the remote node.
		* 		IP/DNS, Description, Type 
		*/
		
		// 3. Click on Add Node button.
		if(!clickByXpath(AddButton)) {
		printFunctionReturn(fn_fail);				
		return false;
		}
		printLogs("Successfully clicked on " + AddButton + " button");
		sleep(5);
		
		// Wait for Add Node page.
		if(!waitForPage("Common_NewWindow")) {
		printFunctionReturn(fn_fail);				
		return false;
		}
		
		captureScreenShot();
		
		// Verify Buttons - Add, Start Over, Close
		if(!verifyButtonStatus("Nodes_AddNodeAddButton", "enabled")) {
		printError("verifyButtonStatus Nodes_AddNodeAddButton failed.");
		}
		
		if(!verifyButtonStatus("Nodes_AddNodeResetButton", "enabled")) {
		printError("verifyButtonStatus Nodes_AddNodeResetButton failed.");
		}
		
		if(!verifyButtonStatus("Nodes_AddNodeCloseButton", "enabled")) {
		printError("verifyButtonStatus Nodes_AddNodeCloseButton failed.");
		}
		
		// 4. Enter all the details of the remote node.
		// IP/DNS, Description, Type, Baseline, Credentials
		
		if(!sendKeysByXpath("Nodes_AddNodeIPDNSInput", remoteHostIp)) {
		printError("sendKeysByXpath Nodes_AddNodeIPDNSInput failed.");
		}
		
		if(!sendKeysByXpath("Nodes_AddNodeDescriptionInput", remoteHostDesc)) {
		printError("sendKeysByXpath Nodes_AddNodeDescriptionInput failed.");
		}
		
		if(!Nodes.NodesAddSelectType(remoteHostType)) {
			printError("NodesAddSelectType failed.");
		}
		
		//captureScreenShot();
	}
	catch(Throwable t) {
		ErrorUtil.addVerificationFailure(t);			
		printError("Exception occurred while setting base fields on the " + VerifyPage + " page.");
		printFunctionReturn(fn_fail);
		return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// addNodeVc
		// Adds VC node as a target node.
		// 1. Calls addNode function for basic functionality to input ip, desc and to select the Node type dropdown
		// 2. Checks to stop or leave the existing HPSUM Service
		// 3. Checks to add auto assoc node or not
		// 4. Adds baseline library
		// 5. Inputs admin username and password if passed or else checks the use current credentials.
		public static boolean addNodeVc(String remoteHostIp, 
											   String remoteHostDesc,
											   boolean autoAddAssoNode,
											   boolean skipBaseline,
											   String remoteHostUserName,
											   String remoteHostPassword,											   
											   String remoteHostIpOa,
											   String remoteHostUserNameOa,
											   String remoteHostPasswordOa) {
			try {
				String nodeType = "NodeType_vc";
				//String 
				printLogs("Calling addNodeVc with values :" + remoteHostIp + ", " + remoteHostDesc + ", " + ", " + autoAddAssoNode + 
						", " + remoteHostUserName + ", " + remoteHostPassword +
						", " + remoteHostIpOa + ", " + remoteHostUserNameOa + ", " + remoteHostPasswordOa);
				
				
				// Calling addNodeBase to perform basic add node functions
				if(!addnodebase("MainMenu_VirtualConnect","VirtualConnect","VC_AddVCButton", 
						remoteHostIp, remoteHostDesc, nodeType)) {
					printError("addvc failed");
					printFunctionReturn(fn_fail);
					return false;
				}
				// Checking whether to uncheck Auto Add AssoNode
				if(!autoAddAssoNode) {
					if(!Nodes.uncheckAutoAddAssoNode()) {
						printError("Failed to uncheck the Auto Add Associated Node checkbox");
					}
				}
								
				// Select BL: NodesAddSelectBaseline
				if(!skipBaseline){
					if(!Nodes.NodesAddSelectBaseline()) {				
					printError("NodesAddSelectBaseline failed.");
					}
				}
				
				// Check if admin credentials are to be used
				if((remoteHostUserName != null)&& (remoteHostPassword != null)) {
					if(!sendKeysByXpath("Nodes_AddNodeCredentialsUsernameInput", remoteHostUserName)) {
						printError("sendKeysByXpath Nodes_AddNodeCredentialsUsernameInput failed.");
					}
					
					if(!sendKeysByXpath("Nodes_AddNodeCredentialsPasswordInput", remoteHostPassword)) {
						printError("sendKeysByXpath Nodes_AddNodeCredentialsPasswordInput failed.");
					}		
				}
				else{
					printLogs("Selecting the option : Use current credentials.");
					if(!clickByXpath("Nodes_AddNodeCredentialsCurrentRadio")) {
						printError("clickByXpath Nodes_AddNodeCredentialsCurrentRadio failed.");
					}
				}
				
				captureScreenShot();
				
				// Adding OA IP for the VC
								
				if(!(remoteHostUserName.equals(remoteHostUserNameOa) && remoteHostPassword.equals(remoteHostPasswordOa))){
					printLogs("Entering the OA credentials as crdentials are different from VC");				
					if(!clickByXpath("Nodes_AddNodeOACredentialsRadio")) {
						printError("clickByXpath Nodes_AddNodeOACredentialsRadio failed.");
					}
					/*if(!sendKeysByXpath("Nodes_AddNodePartner1IpInput", remoteHostIpOa)) {
						printError("sendKeysByXpath Nodes_AddNodeCredentialsUsernameInput failed.");
					}*/	
					
					if(!sendKeysByXpath("Nodes_AddNodePartner1CredentialsUsernameInput", remoteHostUserNameOa)) {
						printError("sendKeysByXpath Nodes_AddNodePartner1CredentialsUsernameInput failed.");
					}			
					if(!sendKeysByXpath("Nodes_AddNodePartner1CredentialsPasswordInput", remoteHostPasswordOa)) {
						printError("sendKeysByXpath Nodes_AddNodePartner1CredentialsPasswordInput failed.");
					}
				}
				captureScreenShot();
				
				// Click on Add button
				if(!clickByXpath("Nodes_AddNodeAddButton")) {
					printError("clickByXpath Nodes_AddNodeAddButton failed.");
				}
				
				sleep(5);
				captureScreenShot();
				
				// 6. Verify the Add Node screen is closed and control is on Nodes screen
				if(!waitForNoElementByXpath("Common_NewWindow")) {
					printError("ERROR: Known Issue: Add New popup screen not closing after pressing Add button.");
					captureScreenShot();
					printLogs("Closing the Add New popup screen using the Cancel button.");
					if(!clickByXpath("Nodes_AddNodeCloseButton")) {
						printFunctionReturn(fn_fail);					
						return false;
					}
					printFunctionReturn(fn_fail);
				}
				
				// Check if Add nodes page vanished after clicking on Add button
				if(!verifyText("Nodes_Title", remoteHostIp)) {
					printError("The control did not reach the added VC page by default.");
					printError("Setting the control to new added node page.");
					driver.findElement(By.linkText(remoteHostIp)).click();
					printLogs("Clicked the remote host IP: " +  remoteHostIp);
				}
				
				if(node_loopback){
					if(!waitForElementByXpath("Common_PageMsgBar", 60, true))				
					printLogs("node inventory: " + getTextByXpath("NodesInventory_Msg"));				
					if(!getTextByXpath("NodesInventory_Msg").contains(OR.getProperty("MessageNodeAddSecondaryVC"))){
						printError("get text by xpath secondary vc error message is not dispalyed in the node page");
						printFunctionReturn(fn_fail);
						return false;
					}					
				}
			}
			catch(Throwable t) {
				ErrorUtil.addVerificationFailure(t);			
				printError("Exception occurred while adding node on the Add VC page.");
				printFunctionReturn(fn_fail);
				return false;
			}
			printFunctionReturn(fn_pass);
			return true;
		}
		
		// addNodeOa
		// Adds OA node as a target node.
		// 1. Calls addNode function for basic functionality navigate to enclosure page and to input ip, desc and to select the Node type dropdown
		// 2. Checks to add auto assoc node or not
		// 3. Adds baseline library or we can skip the adding baseline.
		// 4. Inputs admin username and password if passed or else checks the use current credentials.
		public static boolean addNodeOa(String remoteHostIp, 
											   String remoteHostDesc,
											   boolean autoAddAssoNode,
											   boolean skipBaseline,
											   String remoteHostUserName,
											   String remoteHostPassword) {
			try {
				String nodeType = "NodeType_enclosure";
				
				printLogs("Calling addNodeOa with values :" + remoteHostIp + ", " + remoteHostDesc + ", " + ", " + autoAddAssoNode + 
						", " + skipBaseline + ", " + remoteHostUserName + ", " + remoteHostPassword);
				
				// Calling addNodeBase to perform basic add node functions
				if(!addnodebase("MainMenu_Enclosures","Enclosures","Enclosures_AddEnclosureLink", 
						remoteHostIp, remoteHostDesc, nodeType)) {
					printError("adding enclosure is failed");
					printFunctionReturn(fn_fail);
					return false;
				}
								
				// Checking whether to uncheck Auto Add AssoNode
				if(!autoAddAssoNode) {
					if(!Nodes.uncheckAutoAddAssoNode()) {
						printError("Failed to uncheck the Auto Add Associated Node checkbox");
					}
				}
							
				// Select BL: NodesAddSelectBaseline
				if(!skipBaseline){
					if(!Nodes.NodesAddSelectBaseline()) {				
					printError("NodesAddSelectBaseline failed.");
					}
				}
				
				// Check if admin credentials are to be used
				if((remoteHostUserName != null)&& (remoteHostPassword != null)) {
					
					if(!sendKeysByXpath("Nodes_AddNodeCredentialsUsernameInput", remoteHostUserName)) {
						printError("sendKeysByXpath Nodes_AddNodeCredentialsUsernameInput failed.");
					}
					
					if(!sendKeysByXpath("Nodes_AddNodeCredentialsPasswordInput", remoteHostPassword)) {
						printError("sendKeysByXpath Nodes_AddNodeCredentialsPasswordInput failed.");
					}		
				}
				else{
					printLogs("Selecting the option : Use current credentials.");
					if(!clickByXpath("Nodes_AddNodeCredentialsCurrentRadio")) {
						printError("clickByXpath Nodes_AddNodeCredentialsCurrentRadio failed.");
					}
				}
				
				captureScreenShot();
				
				// Click on Add button
				if(!clickByXpath("Nodes_AddNodeAddButton")) {
					printError("clickByXpath Nodes_AddNodeAddButton failed.");
				}
				
				sleep(5);
				captureScreenShot();
				
				// 6. Verify the Add Node screen is closed and control is on Nodes screen
				if(!waitForNoElementByXpath("Common_NewWindow")) {
					printError("ERROR: Known Issue: Add New popup screen not closing after pressing Add button.");
					captureScreenShot();
					printLogs("Closing the Add New popup screen using the Cancel button.");
					if(!clickByXpath("Nodes_AddNodeCloseButton")) {
						printFunctionReturn(fn_fail);					
						return false;
					}
					printFunctionReturn(fn_fail);
				}
				
				// Check if Add nodes page vanished after clicking on Add button
				if(!verifyText("Nodes_Title", remoteHostIp)) {
					printError("The control did not reach the added Enclosures page by default.");
					printError("Setting the control to new added Enclosures page.");
					driver.findElement(By.linkText(remoteHostIp)).click();
					printLogs("Clicked the remote host IP: " +  remoteHostIp);
				}
			}
			catch(Throwable t) {
				ErrorUtil.addVerificationFailure(t);			
				printError("Exception occurred while adding node on the Add Enclosures page.");
				printFunctionReturn(fn_fail);
				return false;
			}
			//sleep(5);
			printFunctionReturn(fn_pass);
			return true;
		}
		
		// addNodeIlo
		// Adds iLO node as a target node.
		// 1. Calls addNode function for basic functionality to input ip, desc and to select the Node type dropdown
		// 2. Checks to stop or leave the existing HPSUM Service
		// 3. Checks to add auto assoc node or not
		// 4. Adds baseline lib
		// 5. Inputs admin username and password if passed or else checks the use current credentials.
		public static boolean addNodeIlo(String remoteHostIp, 
											   String remoteHostDesc,
											   boolean autoAddAssoNode,
											   boolean skipBaseline,
											   String remoteHostUserName,
											   String remoteHostPassword){
			
			try{
				String nodeType = "NodeType_ilo";
				
				printLogs("Calling addNodeIlo with values :" + remoteHostIp + ", " + remoteHostDesc + ", " + ", " + autoAddAssoNode + 
						", " + skipBaseline + ", " + remoteHostUserName + ", " + remoteHostPassword);
				
				// Calling addNodeBase to perform basic add node functions
				if(!addnodebase("MainMenu_iLO","iLO","iLO_AddiLOLink", 
						remoteHostIp, remoteHostDesc, nodeType)) {
					printError("adding enclosure is failed");
					printFunctionReturn(fn_fail);
					return false;
				}
				
				
				// Checking whether to uncheck Auto Add AssoNode
				if(!autoAddAssoNode) {
					if(!Nodes.uncheckAutoAddAssoNode()) {
						printError("Failed to uncheck the Auto Add Associated Node checkbox");
					}
				}
										
				// Select BL: NodesAddSelectBaseline
				if(!skipBaseline){
					if(!Nodes.NodesAddSelectBaseline()) {				
					printError("NodesAddSelectBaseline failed.");
					}
				}
				
				// Check if admin credentials are to be used
				if((remoteHostUserName != null)&& (remoteHostPassword != null)) {
					
					if(!sendKeysByXpath("Nodes_AddNodeCredentialsUsernameInput", remoteHostUserName)) {
						printError("sendKeysByXpath Nodes_AddNodeCredentialsUsernameInput failed.");
					}
					
					if(!sendKeysByXpath("Nodes_AddNodeCredentialsPasswordInput", remoteHostPassword)) {
						printError("sendKeysByXpath Nodes_AddNodeCredentialsPasswordInput failed.");
					}		
				}
				else{
					printLogs("Selecting the option : Use current credentials.");
					if(!clickByXpath("Nodes_AddNodeCredentialsCurrentRadio")) {
						printError("clickByXpath Nodes_AddNodeCredentialsCurrentRadio failed.");
					}
				}
				
				captureScreenShot();
				
				// Click on Add button
				if(!clickByXpath("Nodes_AddNodeAddButton")) {
					printError("clickByXpath Nodes_AddNodeAddButton failed.");
				}
				
				sleep(10);
				captureScreenShot();
				
				// 6. Verify the Add Node screen is closed and control is on Nodes screen
				if(!waitForNoElementByXpath("Common_NewWindow")) {
					printError("ERROR: Known Issue: Add New popup screen not closing after pressing Add button.");
					//captureScreenShot();
					printLogs("Closing the Add New popup screen using the Cancel button.");
					if(!clickByXpath("Nodes_AddNodeCloseButton")) {
						captureScreenShot();
						printFunctionReturn(fn_fail);
						return false;
					}
					//printFunctionReturn(fn_fail);
					sleep(5);		
				}
				
				// Check if Add nodes page vanished after clicking on Add button
				if(!verifyText("Nodes_Title", remoteHostIp)) {
					printError("The control did not reach the added iLO page by default.");
					printError("Setting the control to new added iLO page.");
					driver.findElement(By.linkText(remoteHostIp)).click();
					printLogs("Clicked the remote host IP: " +  remoteHostIp);
				}
				captureScreenShot();
			}
			catch(Throwable t) {
				ErrorUtil.addVerificationFailure(t);			
				printError("Exception occurred while adding node on the Add iLO page.");
				printFunctionReturn(fn_fail);
				return false;
			}
			printFunctionReturn(fn_pass);
			return true;
		}
		
}


