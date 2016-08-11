package gui.common.base;

import java.util.ArrayList;
import java.util.List;
import gui.common.util.ErrorUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.TestException;

public class Nodes extends SelUtil {
	
	/*
	 * METHODS
	 addNodeBase
	 addNodeBase 
	 addNodeWindows
	 addNodeVm
	 addNodeLinux
	 addNodeIlo
	 addNodeHpux
	 addNodeVc
	 addNodeOa
	 addNodeSwitch
	 NodesAddSelectType
	 uncheckAutoAddAssoNode
	 NodesAddSelectAdditionalBaseline
	 NodesAddSelectBaseline
	 NodesInventorySelectBaseline
	 selectBlOnNodeInventoryPage
	 selectAblOnAddNodePage
	 findNodeOnLeftPane
	 specifyBlAndPerformNodeInventory 
	 performNodeInventory 
	 performNodeInventory 
	 WaitForNodesInventoryToComplete
	 WaitForNodesInventoryToComplete
	 deployNode 
	 unselectFailedDependencies
	 resolveFailedDependencyAndDeploy
	 printTableContents
	 viewLogsAfterDeploy
	 WaitForNodesDeployToComplete
	 deployNodeAdvMode
	 deployNodeEasyMode
	 selectFormAssignToGroupDropDown
	 */
	
	//Methods related to screen : Nodes
	
	
	
	//Methods related to screen : Nodes Add
	
	public static boolean click_on_reset_button = false,node_loopback = false, invalidIP = false, use_current_cred = false, 
			unknown_node = false, duplicate_entry = false, inv_error = false,without_baseline = false, inventory_abort = false, 
			without_spp = false, no_app_comp = false, wait_deploy = false, wait_inv = false, fipsON = false, fips_faildep = false,
			fips_encKey = false;
	
	// addNodeBase
	public static boolean addNodeBase (String remoteHostIp, 
									   String remoteHostDesc,
									   String remoteHostType) {
		printLogs("Calling addNodeBase with values : " + remoteHostIp + ", " + remoteHostDesc + ", " + remoteHostType);
		
		try {
		// 1. Select the Nodes from the Main Menu.	
			
			if(!CommonHpsum.clickLinkOnMainMenu("MainMenu_Nodes")) {
				printFunctionReturn(fn_fail);				
				return false;
			}			
			printLogs("Successfully clicked Nodes link of Main Menu");
			sleep(10);			
			
			captureScreenShot();
			
			// Verify Page: Nodes
			if(!verifyPage("Nodes")) {
				printFunctionReturn(fn_fail);			
				return false;
			}
			printLogs("Successfully landed to Nodes page");
			/* 2. Verify
			* 		- Add Nodes Button 
			* 		- Message: localhost has an Ready to start inventory
			* 		- Inventory Link Presence
			* 		- Actions drop-down and options present in it
			*/
			
			// Verify button exists: Add Baselines
			if(!verifyButtonStatus("Nodes_AddNodeButton", "enabled")) {
				printError("Failed verifyButtonStatus.");
			}
			
			// Verify message-bar messages.
			// Inventory to get the complete details
			// localhost: Ready to start inventory..
			// Inventory
			// PENDING
			
			// Verify combo-box exists: Actions
			//sleep(10);
			
			/*
			* 3. Click on Add Node button.
			* 		Verify Buttons - Add, Start Over, Close
			* 4. Enter all the details of the remote node.
			* 		IP/DNS, Description, Type, Baseline, Credentials
			* 5. Click on Add button.
			* 6. Verify the Add Node screen is closed and control is on Nodes screen
			*/
			
			// 3. Click on Add Node button.
			if(!clickByXpath("Nodes_AddNodeButton")) {
				printFunctionReturn(fn_fail);				
				return false;
			}
			printLogs("Successfully clicked on + Add Node button");
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
			
			if(!NodesAddSelectType(remoteHostType)) {
				printError("NodesAddSelectType failed.");
			}
			
			//captureScreenShot();
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while setting base fields on the Add Node page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// addNodeWindows
	// Adds windows node as a target node.
	// 1. Calls addNode function for basic functionality to input ip, desc and to select the Node type dropdown
	// 2. Checks to stop or leave the existing HPSUM Service
	// 3. Adds baseline lib
	// 4. Inputs admin username and password if passed or else checks the use current credentials.
	public static boolean addNodeWindows(String remoteHostIp, 
										   String remoteHostDesc,
										   boolean stopExistingHpsumServiceOnRemote,
										   String remoteHostUserName,
										   String remoteHostPassword){
		
		String nodeType = null;
		
		try{
			if(!unknown_node){
				nodeType = "NodeType_windows";
			}
			else {
				nodeType = "NodeType_unknown";
			}
			
			printLogs("Calling addNodeWindows with values :" + remoteHostIp + ", " + remoteHostDesc + ", " + 
					   stopExistingHpsumServiceOnRemote + ", " + remoteHostUserName + ", " + remoteHostPassword);
			
			// Calling addNodeBase to perform basic add node functions
			if(!addNodeBase(remoteHostIp, remoteHostDesc, nodeType)) {
				printError("Failed to enter the IP, Description and Node type");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Check to stop existing HPSUM Service
			if(!unknown_node)
			{
				if(!stopExistingHpsumServiceOnRemote) {
					if(!clickByXpath("Nodes_AddNodeLeaveExistingHpsumService")) {
					printError("clickByXpath Nodes_AddNodeLeaveExistingHpsumService failed.");
					// failure_flag = true;
				}
				printLogs("Selected the option Leave the remote HPSUM service running.");
				captureScreenShot();
			}
			else {
				if(!clickByXpath("Nodes_AddNodeStopExistingHpsumService")) {
					printError("clickByXpath Nodes_AddNodeStopExistingHpsumService failed.");
					// failure_flag = true;
				}
				printLogs("Selected the option stop the remote HPSUM service running.");
				captureScreenShot();
			}
			}
			// Select BL: NodesAddSelectBaseline
			
			if(!without_baseline){
				if(!NodesAddSelectBaseline()) {
					printError("NodesAddSelectBaseline failed.");
				}
			}
			else{
				printLogs(" Skipping NodesAddSelectBaseline. Adding node without assigning baseline.");
			}
					
			// Check if admin credentials are to be used
			if((remoteHostUserName != null)&& (remoteHostPassword != null)&& use_current_cred == false ) {
				
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
			
			if(!click_on_reset_button){
				// Click on Add button
				if(!clickByXpath("Nodes_AddNodeAddButton")) {
					printError("clickByXpath Nodes_AddNodeAddButton failed.");
				}
			}
			else {
				// Click on Reset button
				if(!clickByXpath("Nodes_AddNodeResetButton")) {
					printError("clickByXpath Nodes_AddNodeResetButton failed.");
				}
			}
			sleep(10);
			
			if(node_loopback){
				printLogs("loopback error message: " + getTextByXpath("Nodes_AddNodeIPDNSError"));
				if(!getTextByXpath("Nodes_AddNodeIPDNSError").contains(OR.getProperty("MessageNodeAddLoopback"))){
					printError("get text by xpath Loopback address test is not displayed in the IP/DNS field");
					printFunctionReturn(fn_fail);
					return false;
				}					
			}
			
			if(invalidIP){
				printLogs("Invalid IP error message: " + getTextByXpath("Nodes_AddNodeIPDNSError"));
				if(!((getTextByXpath("Nodes_AddNodeIPDNSError").contains("Provide a valid IP/DNS or range")) ||
					(getTextByXpath("Nodes_AddNodeIPDNSError").contains(OR.getProperty("MessageNodeAddInvalidIP"))) ||
					(getTextByXpath("Nodes_AddNodeIPDNSError").contains("Invalid IP")))
					){
					printError("get text by xpath invalid address test is not displayed in the IP/DNS field");
					printFunctionReturn(fn_fail);
					return false;
				}					
			}
			
			if(duplicate_entry){
				printLogs("Checking if adding same node throws errors or not.");
				
				if(!verifyText("Nodes_AddNodeIPDNSError","Node already added. Use Edit Node option to change attributes")){ 
					printError("Nodes_AddNodeIPDNSError message for duplicate node is not visible.");
					printFunctionReturn(fn_fail);
					return false;
				}
			}
			
			sleep(30);
			captureScreenShot();		
			
			// 6. Verify the Add Node screen is closed and control is on Nodes screen
			if(!waitForNoElementByXpath("Common_NewWindow")) {
				printError("ERROR: Known Issue: Add New popup screen not closing after pressing Add button.");
				captureScreenShot();
				printLogs("Closing the Add New popup screen using the Cancel button.");
				if(!clickByXpath("Nodes_AddNodeCloseButton")) {
					printError("Failed to close the add Node window");
					printFunctionReturn(fn_fail);
					return false;
				}
				printFunctionReturn(fn_fail);
			}
			
			// Check if Add nodes page vanished after clicking on Add button
			//Nodes_AddNodeAddButton 
			if(!verifyButtonStatus("Nodes_AddNodeAddButton", "enabled")) {
				printError("The control did not reach the added node page by default.");
				printError("Setting the control to new added node page.");
				//driver.findElement(By.linkText(remoteHostIp)).click();
				//printLogs("Clicked the remote host IP: " +  remoteHostIp);
			}
			if(unknown_node){
				printLogs("Checking if actual node type is shown in nodes page after adding the node");
				if(!verifyText("Nodes_Title", remoteHostDesc+": "+remoteHostIp)){ 
					printError("Failed to get the actual type of node even though specified as unknown while adding.");
					printFunctionReturn(fn_fail);
					return false;
					}
				}			
			captureScreenShot();
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while adding node on the Add Node page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// addNodeVm
	// Adds VM node as a target node.
	// 1. Calls addNode function for basic functionality to input ip, desc and to select the Node type dropdown
	// 2. Checks to stop or leave the existing HPSUM Service
	// 3. Adds baseline lib
	// 4. Inputs admin username and password if passed or else checks the use current credentials.
	public static boolean addNodeVm(String remoteHostIp, 
										   String remoteHostDesc,										   
										   String remoteHostUserName,
										   String remoteHostPassword, boolean vcenter,
										   String vcenterip,
										   String vcneterUserName,
										   String vcenterPassWord){
		
		String nodeType = null;
		
		try{
			if(!unknown_node){
				nodeType = "NodeType_vmware";
			}
			else {
				nodeType = "NodeType_unknown";
			}
			
			printLogs("Calling addNodeVm with values :" + remoteHostIp + ", " + remoteHostDesc + ", " + 
					    remoteHostUserName + ", " + remoteHostPassword + ", " + vcenter + ", " +
					    vcenterip + ", " +vcneterUserName  + ", " + vcenterPassWord);
			
			// Calling addNodeBase to perform basic add node functions
			if(!addNodeBase(remoteHostIp, remoteHostDesc, nodeType)) {
				printError("Failed to enter the IP, Description and Node type");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Check to stop existing HPSUM Service
			/*if(!unknown_node)
			{
				if(!stopExistingHpsumServiceOnRemote) {
					if(!clickByXpath("Nodes_AddNodeLeaveExistingHpsumService")) {
					printError("clickByXpath Nodes_AddNodeLeaveExistingHpsumService failed.");
					// failure_flag = true;
				}
				printLogs("Selected the option Leave the remote HPSUM service running.");
				captureScreenShot();
			}
			else {
				if(!clickByXpath("Nodes_AddNodeStopExistingHpsumService")) {
					printError("clickByXpath Nodes_AddNodeStopExistingHpsumService failed.");
					// failure_flag = true;
				}
				printLogs("Selected the option stop the remote HPSUM service running.");
				captureScreenShot();
			}
			}*/
			// Select BL: NodesAddSelectBaseline
			
			if(!without_baseline){
				if(!NodesAddSelectBaseline()) {
					printError("NodesAddSelectBaseline failed.");
				}
			}
			else{
				printLogs(" Skipping NodesAddSelectBaseline. Adding node without assigning baseline.");
			}
			
			//Checking the vcenter flag and adding the vm host node.
			if(vcenter){
				if(vcenterip == null || vcneterUserName == null || vcenterPassWord == null){
					printLogs("Selecting the option : Enter vCenter details to get ticket(for authentication to host)");
					if(!clickByXpath("Nodes_AddNodeCredentialVcenter")) {
						printError("clickByXpath Nodes_AddNodeCredentialsCurrentRadio failed.");
					}
					
					if(!sendKeysByXpath("Nodes_AddNodeVcenterIp", vcenterip)) {
						printError("sendKeysByXpath Nodes_AddNodeVcenterIp failed.");
					}
					
					if(!sendKeysByXpath("Nodes_AddNodeVcenterUserName", vcneterUserName)) {
						printError("sendKeysByXpath Nodes_AddNodeVcenterUserName failed.");
					}
					
					if(!sendKeysByXpath("Nodes_AddNodeVcenterPassword", vcenterPassWord)) {
						printError("sendKeysByXpath Nodes_AddNodeVcenterPassword failed.");
					}
				}
				else{
					printLogs("vcenter ip or vcneter UserName or vcenter PassWord is not provided or its marked as null");
				}
			}
			else{
			// Check if admin credentials are to be used
			if((remoteHostUserName != null)&& (remoteHostPassword != null)) {
				
				if(!sendKeysByXpath("Nodes_AddNodeCredentialsUsernameInput", remoteHostUserName)) {
					printError("sendKeysByXpath Nodes_AddNodeCredentialsUsernameInput failed.");
				}
				
				if(!sendKeysByXpath("Nodes_AddNodeCredentialsPasswordInput", remoteHostPassword)) {
					printError("sendKeysByXpath Nodes_AddNodeCredentialsPasswordInput failed.");
				}
			}
			/*else{
				printLogs("Selecting the option : Use current credentials.");
				if(!clickByXpath("Nodes_AddNodeCredentialsCurrentRadio")) {
					printError("clickByXpath Nodes_AddNodeCredentialsCurrentRadio failed.");
				}
			}*/
			}
			captureScreenShot();
			
			if(!click_on_reset_button){
				// Click on Add button
				if(!clickByXpath("Nodes_AddNodeAddButton")) {
					printError("clickByXpath Nodes_AddNodeAddButton failed.");
				}
				
				
				if(duplicate_entry){
					printLogs("Checking if adding same node throws errors or not.");
					
					if(!verifyText("Nodes_AddNodeIPDNSError","Node already added. Use Edit Node option to change attributes")){ 
						printError("Nodes_AddNodeIPDNSError message for duplicate node is not visible.");
						printFunctionReturn(fn_fail);
						return false;
					}
				}
				
				sleep(30);
				captureScreenShot();
				
				// 6. Verify the Add Node screen is closed and control is on Nodes screen
				if(!waitForNoElementByXpath("Common_NewWindow")) {
					printError("ERROR: Known Issue: Add New popup screen not closing after pressing Add button.");
					captureScreenShot();
					printLogs("Closing the Add New popup screen using the Cancel button.");
					if(!clickByXpath("Nodes_AddNodeCloseButton")) {
						printError("Failed to close the add Node window");
						printFunctionReturn(fn_fail);
						return false;
					}
					printFunctionReturn(fn_fail);
				}
				
				// Check if Add nodes page vanished after clicking on Add button
				
				if(!verifyButtonStatus("Nodes_AddNodeButton", "enabled")) {
					printError("The control did not reach the added node page by default.");
					printError("Setting the control to new added node page.");
					//driver.findElement(By.linkText(remoteHostIp)).click();
					//printLogs("Clicked the remote host IP: " +  remoteHostIp);
				}	
			
				if(unknown_node){
					printLogs("Checking if actual node type is shown in nodes page after adding the node");
					if(!verifyText("Nodes_Title", remoteHostDesc+": "+remoteHostIp)){ 
						printError("Failed to get the actual type of node even though specified as unknown while adding.");
						printFunctionReturn(fn_fail);
						return false;
						}
					}
				
			}
			else {
				// Click on Reset button
				if(!clickByXpath("Nodes_AddNodeResetButton")) {
					printError("clickByXpath Nodes_AddNodeResetButton failed.");
				}
			}
			sleep(10);
			
			captureScreenShot();
			
			// 6. Verify the Add Node screen is closed and control is on Nodes screen
			if(!waitForNoElementByXpath("Common_NewWindow")) {
				printError("ERROR: Known Issue: Add New popup screen not closing after pressing Add button.");
				captureScreenShot();
				printLogs("Closing the Add New popup screen using the Cancel button.");
				if(!clickByXpath("Nodes_AddNodeCloseButton")) {
					printError("Failed to close the add Node window");
					printFunctionReturn(fn_fail);
					return false;
				}
				printFunctionReturn(fn_fail);
			}
			
			// Check if Add nodes page vanished after clicking on Add button
			//Nodes_AddNodeAddButton 
			if(!verifyButtonStatus("Nodes_AddNodeAddButton", "enabled")) {
				printError("The control did not reach the added node page by default.");
				printError("Setting the control to new added node page.");
				//driver.findElement(By.linkText(remoteHostIp)).click();
				//printLogs("Clicked the remote host IP: " +  remoteHostIp);
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while adding VM on the Add Node page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// addNodeLinux
	// Adds Linux node as a target node.
	// 1. Calls addNode function for basic functionality to input ip, desc and to select the Node type dropdown
	// 2. Checks to stop or leave the existing HPSUM Service
	// 3. Adds baseline lib
	// 4. Inputs admin username and password if passed or else checks the use current credentials.
	public static boolean addNodeLinux(String remoteHostIp, 
										   String remoteHostDesc,
										   boolean stopExistingHpsumServiceOnRemote,
										   String remoteHostUserName,
										   String remoteHostPassword,
										   String remoteHostAccessLevel,
										   String remoteHostRootUserName,
										   String remoteHostRootPassword){
		
		try{
			String nodeType = "";
			if(!unknown_node){
				nodeType = "NodeType_windows";
			}
			else {
				nodeType = "NodeType_unknown";
			}
						
			printLogs("Calling addNodeLinux with values :" + remoteHostIp + ", " + remoteHostDesc + ", " + 
					   stopExistingHpsumServiceOnRemote + ", " + remoteHostUserName + ", " + remoteHostPassword + ", " + 
					   remoteHostAccessLevel + ", " + remoteHostRootUserName + ", " + remoteHostRootPassword);
			
			// Calling addNodeBase to perform basic add node functions
			if(!addNodeBase(remoteHostIp, remoteHostDesc, nodeType)) {
				printError("addNodeLinux failed: Failed to enter the IP, Description and Node type");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Check to stop existing HPSUM Service
			if(!unknown_node){
				if(!stopExistingHpsumServiceOnRemote) {
					if(!clickByXpath("Nodes_AddNodeLeaveExistingHpsumService")) {
						printError("clickByXpath Nodes_AddNodeLeaveExistingHpsumService failed.");
						// failure_flag = true;
					}
					printLogs("Selected the option Leave the remote HPSUM service running.");
					captureScreenShot();
				}
				else {
					if(!clickByXpath("Nodes_AddNodeStopExistingHpsumService")) {
						printError("clickByXpath Nodes_AddNodeStopExistingHpsumService failed.");
						// failure_flag = true;
					}
					printLogs("Selected the option stop the remote HPSUM service running.");
					captureScreenShot();
				}
			}
			// Select BL: NodesAddSelectBaseline
			if(!without_baseline){
				if(!NodesAddSelectBaseline()) {
					printError("NodesAddSelectBaseline failed.");
				}
			}
			else{
				printLogs(" Skipping NodesAddSelectBaseline. Adding node without assigning baseline.");
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
			
			// Checking what access level to select
			switch(remoteHostAccessLevel){			
				case "none" :
					printLogs("Leaving the Access Level selection to None");
					break;
					
				case "sudo" :
					if(!clickByXpath("Nodes_AddNodeLinuxAccessLevelSudoRadio")) {
						printError("clickByXpath Nodes_AddNodeLinuxAccessLevelSudoRadio failed.");
					}
					printLogs("Selected Use sudo with credentials entered above radio button");
					break;
					
				case "super" :
					if(remoteHostRootUserName!=null && remoteHostRootPassword!=null) {
						if(!clickByXpath("Nodes_AddNodeLinuxAccessLevelSuperRadio")) {
							printError("clickByXpath Nodes_AddNodeLinuxAccessLevelSuperRadio failed.");
						}
						printLogs("Selected Enter super user credentials to update components radio button");
						printLogs("Entering SuperUser credentials");
						if(!sendKeysByXpath("Nodes_AddNodeLinuxAccessLevelSuperUserNameInput", remoteHostRootUserName)) {
							printError("sendKeysByXpath Nodes_AddNodeLinuxAccessLevelSuperUserNameInput failed.");
						}
						
						if(!sendKeysByXpath("Nodes_AddNodeLinuxAccessLevelSuperPasswordInput", remoteHostRootPassword)) {
							printError("sendKeysByXpath Nodes_AddNodeLinuxAccessLevelSuperPasswordInput failed.");
						}
					}
					else{
						printError("Failed to set the superuser name and password as it was not sent");
						printFunctionReturn(fn_fail);
						return false;
					}
					break;
					
				default :
					printError("Failed to set the access level. Invalid Access Level provided. Valid values: none/sudo/super");
					printFunctionReturn(fn_fail);
					return false;
			}
			
			captureScreenShot();
			if(!click_on_reset_button){
				// Click on Add button
				if(!clickByXpath("Nodes_AddNodeAddButton")) {
					printError("clickByXpath Nodes_AddNodeAddButton failed.");
				}
			}
			else {
				// Click on Reset button
				if(!clickByXpath("Nodes_AddNodeResetButton")) {
					printError("clickByXpath Nodes_AddNodeResetButton failed.");
				}
			}
			//Checking the loop back address error message
			if(node_loopback){
					printLogs("loopback error message: " + getTextByXpath("Nodes_AddNodeIPDNSError"));
					if(!getTextByXpath("Nodes_AddNodeIPDNSError").contains(OR.getProperty("MessageNodeAddLoopback"))){
						printError("get test by xpath Loopback address test is not displayed in the IP/DNS field");
						printFunctionReturn(fn_fail);
						return false;
					}					
				}
			if(invalidIP){
				printLogs("Invalid IP error message: " + getTextByXpath("Nodes_AddNodeIPDNSError"));
				if(!((getTextByXpath("Nodes_AddNodeIPDNSError").contains("Provide a valid IP/DNS or range")) ||
					(getTextByXpath("Nodes_AddNodeIPDNSError").contains(OR.getProperty("MessageNodeAddInvalidIP"))) ||
					(getTextByXpath("Nodes_AddNodeIPDNSError").contains("Invalid IP")))
					){
					printError("get text by xpath invalid address test is not displayed in the IP/DNS field");
					printFunctionReturn(fn_fail);
					return false;
				}					
			}
			
			if(duplicate_entry){
				printLogs("Checking if adding same node throws errors or not.");
				
				if(!verifyText("Nodes_AddNodeIPDNSError","Node already added. Use Edit Node option to change attributes")){ 
					printError("Nodes_AddNodeIPDNSError message for duplicate node is not visible.");
					printFunctionReturn(fn_fail);
					return false;
				}
			}
			
			//sleep(30);
			captureScreenShot();
				
			// 6. Verify the Add Node screen is closed and control is on Nodes screen
			if(!waitForNoElementByXpath("Common_NewWindow")) {
				printError("Known Issue: Add New popup screen not closing after pressing Add button.");
				captureScreenShot();
				printLogs("Closing the Add New popup screen using the Cancel button.");
				if(!clickByXpath("Nodes_AddNodeCloseButton")) {
					appendHtmlComment("Failed to close the add Node window");
					printFunctionReturn(fn_fail);
					return false;
				}
				printFunctionReturn(fn_fail);
			}
			
			// Check if Add nodes page vanished after clicking on Add button
			if(!verifyButtonStatus("Nodes_AddNodeAddButton", "enabled")) {
				printError("The control did not reach the added node page by default.");
				printError("Setting the control to new added node page.");
				//driver.findElement(By.linkText(remoteHostIp)).click();
				//printLogs("Clicked the remote host IP: " +  remoteHostIp);
			}
			//Verifying the unknown node
			if(unknown_node){
				printLogs("Checking if actual node type is shown in nodes page after adding the node");
				if(!verifyText("Nodes_Title", remoteHostDesc+": "+remoteHostIp)){ 
					printError("Failed to get the actual type of node even though specified as unknown while adding.");
					printFunctionReturn(fn_fail);
					return false;
					}
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while adding Linux on the Add Node page.");
			printFunctionReturn(fn_fail);
			return false;
		}
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
										   boolean stopExistingHpsumServiceOnRemote,
										   String remoteHostUserName,
										   String remoteHostPassword){
		
		try{
			String nodeType = "NodeType_ilo";
			
			printLogs("Calling addNodeIlo with values :" + remoteHostIp + ", " + remoteHostDesc + ", " + ", " + autoAddAssoNode + 
					", " + stopExistingHpsumServiceOnRemote + ", " + remoteHostUserName + ", " + remoteHostPassword);
			
			// Calling addNodeBase to perform basic add node functions
			if(!addNodeBase(remoteHostIp, remoteHostDesc, nodeType)) {
				printError("addNodeBase failed");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Checking whether to uncheck Auto Add AssoNode
			if(!autoAddAssoNode) {
				if(!uncheckAutoAddAssoNode()) {
					printError("Failed to uncheck the Auto Add Associated Node checkbox");
				}
			}
			
			// Check to stop existing HPSUM Service
			if(!stopExistingHpsumServiceOnRemote) {
				if(!clickByXpath("Nodes_AddNodeLeaveExistingHpsumService")) {
					printError("clickByXpath Nodes_AddNodeLeaveExistingHpsumService failed.");
					// failure_flag = true;
				}
				printLogs("Selected the option Leave the remote HPSUM service running.");
				captureScreenShot();
			}
			
			// Select BL: NodesAddSelectBaseline
			if(!NodesAddSelectBaseline()) {
				printError("NodesAddSelectBaseline failed.");
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
				printError("The control did not reach the added node page by default.");
				printError("Setting the control to new added node page.");
				driver.findElement(By.linkText(remoteHostIp)).click();
				printLogs("Clicked the remote host IP: " +  remoteHostIp);
			}
			captureScreenShot();
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while adding node on the Add Node page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// addNodeHpux
	// addNodeHpux will add an HP-UX node
	public static boolean addNodeHpux(String remoteHostIp, 
									   String remoteHostDesc,
									   boolean stopExistingHpsumServiceOnRemote,
									   String remoteHostUserName,
									   String remoteHostPassword) {
		try {
			String nodeType = "NodeType_hpux";

			printLogs("Calling addNodeHpux with values :" + remoteHostIp + ", " + remoteHostDesc + ", " + 
					   stopExistingHpsumServiceOnRemote + ", " + remoteHostUserName + ", " + remoteHostPassword);

			// Calling addNodeBase to perform basic add node functions
			if(!addNodeBase(remoteHostIp, remoteHostDesc, nodeType)) {
				printError("addNodeBase failed");
				printFunctionReturn(fn_fail);
				return false;
			}

			// Check to stop existing HPSUM Service
			if(!stopExistingHpsumServiceOnRemote) {
				if(!clickByXpath("Nodes_AddNodeLeaveExistingHpsumService")) {
					printError("clickByXpath Nodes_AddNodeLeaveExistingHpsumService failed.");
					// failure_flag = true;
				}
				printLogs("Selected the option Leave the remote HPSUM service running.");
				captureScreenShot();
			}
			
			// Select ABL: NodesAddSelectAdditionalBaseline
			String additionPkgLocation = "";
			if (currentOsName.contains("windows")) {
				additionPkgLocation = "additionalPkg_hpux_Location_w";
			}
			else {
				additionPkgLocation = "additionalPkg_hpux_Location_l";
			}
			
			if(!NodesAddSelectAdditionalBaseline(additionPkgLocation)) {
				printError("NodesAddSelectAdditionalBaseline failed.");
			}
			
			captureScreenShot();
			
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

			// Verify the Add Node screen is closed and control is on Nodes screen
			if(!waitForNoElementByXpath("Common_NewWindow")) {
				printError("Known Issue: Add New popup screen not closing after pressing Add button.");
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
				printError("The control did not reach the added node page by default.");
				printError("Setting the control to new added node page.");
				driver.findElement(By.linkText(remoteHostIp)).click();
				printLogs("Clicked the remote host IP: " +  remoteHostIp);
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while adding node on the Add Node page.");
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
	// 4. Adds baseline lib
	// 5. Inputs admin username and password if passed or else checks the use current credentials.
	public static boolean addNodeVc(String remoteHostIp, 
										   String remoteHostDesc,
										   boolean autoAddAssoNode,
										   boolean skipbaseline,
										   String remoteHostUserName,
										   String remoteHostPassword,
										   String remoteHostIpOa,
										   String remoteHostUserNameOa,
										   String remoteHostPasswordOa) {
		try {
			String nodeType = "NodeType_vc";
			//boolean skipbaseline,
			printLogs("Calling addNodeVc with values :" + remoteHostIp + ", " + remoteHostDesc + ", " + ", " + autoAddAssoNode + 
					", " + skipbaseline + ", " + remoteHostUserName + ", " + remoteHostPassword +
					", " + remoteHostIpOa + ", " + remoteHostUserNameOa + ", " + remoteHostPasswordOa);
			
			// Calling addNodeBase to perform basic add node functions
			if(!addNodeBase(remoteHostIp, remoteHostDesc, nodeType)) {
				printError("addNodeBase failed");
				printFunctionReturn(fn_fail);
				return false;
			}
			// Checking whether to uncheck Auto Add AssoNode
			if(!autoAddAssoNode) {
				if(!uncheckAutoAddAssoNode()) {
					printError("Failed to uncheck the Auto Add Associated Node checkbox");
				}
			}
							
			// Select BL: NodesAddSelectBaseline
			if(!skipbaseline){
				if(!NodesAddSelectBaseline()) {
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
			
			if((remoteHostUserNameOa!=null) && (remoteHostPasswordOa!=null)){
				if(!(remoteHostUserName.equals(remoteHostUserNameOa) && remoteHostPassword.equals(remoteHostPasswordOa))){
					printLogs("Entering the OA credentials as crdentials are different from VC");
					if(!clickByXpath("Nodes_AddNodeOACredentialsRadio")) {
						printError("clickByXpath Nodes_AddNodeOACredentialsRadio failed.");
					}
					// Adding OA IP for the VC
					if(!sendKeysByXpath("Nodes_AddNodePartner1IpInput", remoteHostIpOa)) {
						printError("sendKeysByXpath Nodes_AddNodeCredentialsUsernameInput failed.");
					}			
					if(!sendKeysByXpath("Nodes_AddNodePartner1CredentialsUsernameInput", remoteHostUserNameOa)) {
						printError("sendKeysByXpath Nodes_AddNodeCredentialsPasswordInput failed.");
					}			
					if(!sendKeysByXpath("Nodes_AddNodePartner1CredentialsPasswordInput", remoteHostPasswordOa)) {
						printError("sendKeysByXpath Nodes_AddNodeCredentialsPasswordInput failed.");
					}
				}
			}else{
				printLogs("Using the credentials above for OA as the credentials are same.");
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
				printError("The control did not reach the added node page by default.");
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
			printError("Exception occurred while adding node on the Add Node page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	public static boolean editNodeCredentials(String remoteHostIp, String remoteHostUserNameOa, String remoteHostPasswordOa){
		printLogs("Calling editNodeCredentials with " );
		try{
			//Clicking on the message bar.
			if(!clickByXpath("Common_PageMsgBar")) {
				printError("clickByXpath on comman message bar is failed.");
				throw new TestException("Test failed while clicking on comman message bar");
			}
			
			//Clicking on the Enter associated Onboard Administrator details 
			if(!clickByXpath("Common_PageMsgBarLink")) {
			printError("clickByXpath on edit link on message bar is failed.");
			throw new TestException("Test failed while clicking on edit link on message bar");
			}
			if(!waitForElementByXpath("Nodes_EditNodeOACredentialWindowHeading", 60, true)){
				printError("The control did not reach the Associated Node Details screen for entering credentials.");
				throw new TestException("Test failed while waiting for the Associated Node Details screen");
			}
			
			if(!sendKeysByXpath("Nodes_EditNodeOACredentialsUsername", remoteHostUserNameOa)) {
				printError("sendKeysByXpath Nodes_EditNodeOACredentialsUsername failed.");
			}			
			if(!sendKeysByXpath("Nodes_EditNodeOACredentialsPassword", remoteHostPasswordOa)) {
				printError("sendKeysByXpath Nodes_EditNodeOACredentialsPassword failed.");
			}
			captureScreenShot();
			// Click on Add button
			if(!clickByXpath("Nodes_EditNodeOACredentialsOkButton")) {
				printError("clickByXpath Nodes_EditNodeOACredentialsOkButton failed.");
			}
			
			sleep(5);
			captureScreenShot();
			
			// 6. Verify the Add Node screen is closed and control is on Nodes screen
			if(!waitForNoElementByXpath("Nodes_EditNodeOACredentialWindowHeading")) {
				printError("ERROR: Associated Node Details screen not closing after pressing OK button.");
				captureScreenShot();
				throw new TestException("ERROR: Associated Node Details screen not closing after pressing OK button.");
			}
			
			// Check if Associated Node Details page vanished after clicking on OK button
			if(!verifyText("Nodes_Title", remoteHostIp)) {
				printError("The control did not reach the added node page by default.");
				printError("Setting the control to new added node page.");
				driver.findElement(By.linkText(remoteHostIp)).click();
				printLogs("Clicked the remote host IP: " +  remoteHostIp);
			}
			
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while editing the node on the Associated Node Details page. "+t.getMessage());
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// addNodeOa
	// Adds OA node as a target node.
	// 1. Calls addNode function for basic functionality to input ip, desc and to select the Node type dropdown
	// 2. Checks to stop or leave the existing HPSUM Service
	// 3. Checks to add auto assoc node or not
	// 4. Adds baseline lib
	// 5. Inputs admin username and password if passed or else checks the use current credentials.
	public static boolean addNodeOa(String remoteHostIp, 
										   String remoteHostDesc,
										   boolean autoAddAssoNode,
										   boolean stopExistingHpsumServiceOnRemote,
										   String remoteHostUserName,
										   String remoteHostPassword) {
		try {
			String nodeType = "NodeType_enclosure";
			
			printLogs("Calling addNodeOa with values :" + remoteHostIp + ", " + remoteHostDesc + ", " + ", " + autoAddAssoNode + 
					", " + stopExistingHpsumServiceOnRemote + ", " + remoteHostUserName + ", " + remoteHostPassword);
			
			// Calling addNodeBase to perform basic add node functions
			if(!addNodeBase(remoteHostIp, remoteHostDesc, nodeType)) {
				printError("addNodeBase failed");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			
			// Checking whether to uncheck Auto Add AssoNode
			if(!autoAddAssoNode) {
				if(!uncheckAutoAddAssoNode()) {
					printError("Failed to uncheck the Auto Add Associated Node checkbox");
				}
			}
			
			// Check to stop existing HPSUM Service
			if(!stopExistingHpsumServiceOnRemote) {
				if(!clickByXpath("Nodes_AddNodeLeaveExistingHpsumService")) {
					printError("clickByXpath Nodes_AddNodeLeaveExistingHpsumService failed.");
					// failure_flag = true;
				}
				printLogs("Selected the option Leave the remote HPSUM service running.");
				captureScreenShot();
			}
			
			// Select BL: NodesAddSelectBaseline
			if(!NodesAddSelectBaseline()) {
				printError("NodesAddSelectBaseline failed.");
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
				printError("The control did not reach the added node page by default.");
				printError("Setting the control to new added node page.");
				driver.findElement(By.linkText(remoteHostIp)).click();
				printLogs("Clicked the remote host IP: " +  remoteHostIp);
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while adding node on the Add Node page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		//sleep(5);
		printFunctionReturn(fn_pass);
		return true;
	}
	
	
	// addNodeSwitch
	// Adds switch as a target node.
	// 1. Calls addNode function for basic functionality to input ip, desc and to select the Node type dropdown
	// 2. Check whether to uncheck auto associate node
	// 3. Adds baseline lib
	// 4. Inputs admin username and password if passed or else checks the use current credentials.
	public static boolean addNodeSwitch(String remoteHostIp, 
											   String remoteHostDesc,
											   boolean autoAddAssoNode,
											   String remoteHostUserName,
											   String remoteHostPassword,
											   String additionalPkgLoacationKey){
			
		try{
			String nodeType = "NodeType_fc_switch";
			
			String additionalPkgLoacation = CONFIG.getProperty(additionalPkgLoacationKey);
				
			printLogs("Calling addNodeSwitch with values :" + remoteHostIp + ", " + remoteHostDesc + ", " + remoteHostUserName + ", " + remoteHostPassword);
				
			// 1. Calling addNodeBase to perform basic add node functions
			if(!addNodeBase(remoteHostIp, remoteHostDesc, nodeType)) {
				printError("addNodeBase: Failed to enter the IP, Description and Node type");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// 2. Checking whether to uncheck Auto Add AssoNode
			if(!autoAddAssoNode) {
				if(!uncheckAutoAddAssoNode()) {
					printError("Failed to uncheck the Auto Add Associated Node checkbox");
				}
			}
				
			// 3. Select BL: selectAblOnAddNodePage
			if(!selectAblOnAddNodePage(additionalPkgLoacation)) {
				printError("selectAdditionalBaseline failed.");
			}
				
			// 4. Check if admin credentials are to be used
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
				captureScreenShot();
				printLogs("Closing the Add New popup screen using the Cancel button.");
				if(!clickByXpath("Nodes_AddNodeCloseButton")) {
					appendHtmlComment("Failed to close the add Node window");
					printFunctionReturn(fn_fail);
					return false;
				}
				printFunctionReturn(fn_fail);
			}
				
			// Check if Add nodes page vanished after clicking on Add button
			if(!verifyText("Nodes_Title", remoteHostIp)) {
				printError("The control did not reach the added node page by default.");
				printError("Setting the control to new added node page.");
				driver.findElement(By.linkText(remoteHostIp)).click();
				printLogs("Clicked the remote host IP: " +  remoteHostIp);
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while adding switch on the Add Node page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// addNodeSAS
		// Adds VC node as a target node.
		// 1. Calls addNode function for basic functionality to input ip, desc and to select the Node type dropdown
		// 2. Checks to stop or leave the existing HPSUM Service
		// 3. Checks to add auto assoc node or not
		// 4. Adds baseline library
		// 5. Inputs admin username and password if passed or else checks the use current credentials.
		public static boolean addNodeSas(String remoteHostIp, 
											   String remoteHostDesc,
											   boolean autoAddAssoNode,
											   boolean skipbaseline,
											   String remoteHostUserName,
											   String remoteHostPassword,
											   String remoteHostIpOa,
											   String remoteHostUserNameOa,
											   String remoteHostPasswordOa) {
			try {
				String nodeType = "NodeType_sas_switch";
				
				printLogs("Calling addNodeVc with values :" + remoteHostIp + ", " + remoteHostDesc + ", " + ", " + autoAddAssoNode + 
						", " + skipbaseline + ", " + remoteHostUserName + ", " + remoteHostPassword +
						", " + remoteHostIpOa + ", " + remoteHostUserNameOa + ", " + remoteHostPasswordOa);
				
				// Calling addNodeBase to perform basic add node functions
				if(!addNodeBase(remoteHostIp, remoteHostDesc, nodeType)) {
					printError("addNodeBase failed");
					printFunctionReturn(fn_fail);
					return false;
				}
				// Checking whether to uncheck Auto Add AssoNode
				if(!autoAddAssoNode) {
					if(!uncheckAutoAddAssoNode()) {
						printError("Failed to uncheck the Auto Add Associated Node checkbox");
					}
				}
								
				// Select BL: NodesAddSelectBaseline
				if(!skipbaseline){
					if(!NodesAddSelectBaseline()) {
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
								
				if((remoteHostUserNameOa!=null) && (remoteHostPasswordOa!=null)){
					if(!(remoteHostUserName.equals(remoteHostUserNameOa) && remoteHostPassword.equals(remoteHostPasswordOa))){
						printLogs("Entering the OA credentials as crdentials are different from VC");
						if(!clickByXpath("Nodes_AddNodeOACredentialsRadio")) {
							printError("clickByXpath Nodes_AddNodeOACredentialsRadio failed.");
						}
						// Adding OA IP for the VC
						if(!sendKeysByXpath("Nodes_AddNodePartner1IpInput", remoteHostIpOa)) {
							printError("sendKeysByXpath Nodes_AddNodeCredentialsUsernameInput failed.");
						}			
						if(!sendKeysByXpath("Nodes_AddNodePartner1CredentialsUsernameInput", remoteHostUserNameOa)) {
							printError("sendKeysByXpath Nodes_AddNodeCredentialsPasswordInput failed.");
						}			
						if(!sendKeysByXpath("Nodes_AddNodePartner1CredentialsPasswordInput", remoteHostPasswordOa)) {
							printError("sendKeysByXpath Nodes_AddNodeCredentialsPasswordInput failed.");
						}
					}
				}else{
					printLogs("Using the credentials above for OA as the credentials are same.");
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
					printError("The control did not reach the added node page by default.");
					printError("Setting the control to new added node page.");
					driver.findElement(By.linkText(remoteHostIp)).click();
					printLogs("Clicked the remote host IP: " +  remoteHostIp);
				}
			}
			catch(Throwable t) {
				ErrorUtil.addVerificationFailure(t);			
				printError("Exception occurred while adding node on the Add Node page.");
				printFunctionReturn(fn_fail);
				return false;
			}
			printFunctionReturn(fn_pass);
			return true;
		}
	
	
	// addNodeCommaSeparatedList
	// 
	public static boolean addNodeCommaSeparatedList(String remoteHostIpList){
		try{
			
		
		
		
		
		
		
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while adding node on the Add Node page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	
	// NodesAddSelectType
	// Select Node Type on Add Node page.	
	public static boolean NodesAddSelectType(String nodeType) {
		printLogs("Calling NodesAddSelectType with values: " + nodeType);
		
		try {
			WebElement descriptionInput = driver.findElement(By.xpath(OR.getProperty("Nodes_AddNodeDescriptionInput")));
			WebElement nodeTypeDropdown = driver.findElement(By.xpath(OR.getProperty("Nodes_AddNodeTypeDropDown")));
			
			/* Valid nodeType values:
			         {"NodeType_selecttype",
			 		  "NodeType_windows",
					  "NodeType_linux",
					  "NodeType_hpux",
					  "NodeType_vmware",
					  "NodeType_enclosure",
					  "NodeType_sd2oa",
					  "NodeType_sas_switch",
					  "NodeType_ilo",
					  "NodeType_ilofederation",
					  "NodeType_moonshot",
					  "NodeType_ipdu",
					  "NodeType_vc",
					  "NodeType_unknown"};
			*/
			
			printLogs("Pressing Tab on Description");
			descriptionInput.sendKeys(Keys.TAB);
			
			sleep(1);
			
			printLogs("Selecting " + nodeType + " Node Type on the Add Node page.");
			
			switch (nodeType){
			
				case "NodeType_windows" : 
					nodeTypeDropdown.sendKeys("Win\n");
					printLogs("Selected Windows from the Nodes Type drop down");
					break;		
					
				case "NodeType_linux" :
					nodeTypeDropdown.sendKeys("Lin\n");
					printLogs("Selected Linux from the Nodes Type drop down");
					break;
					
				case "NodeType_vmware" :
					nodeTypeDropdown.sendKeys("VMware\n");
					printLogs("Selected VMware from the Nodes Type drop down");
					break;
					
				case "NodeType_hpux" :
					nodeTypeDropdown.sendKeys("HP-UX\n");
					printLogs("Selected HP-UX from the Nodes Type drop down");
					break;
					
				case "NodeType_enclosure" :
					nodeTypeDropdown.sendKeys("Onboard\n");
					printLogs("Selected OA from the Nodes Type drop down");
					break;
					
				case "NodeType_sd2oa" :
					nodeTypeDropdown.sendKeys("Super\n");
					printLogs("Selected SuperDome 2 OA from the Nodes Type drop down");
					break;

				case "NodeType_sas_switch" :
					nodeTypeDropdown.sendKeys("HP SAS\n");
					printLogs("Selected HP SAS B/L Interconnect Switch from the Nodes Type drop down");
					break;

				case "NodeType_fc_switch" :
					nodeTypeDropdown.sendKeys("Fibre\n");
					printLogs("Selected Fibre Channel Switch from the Nodes Type drop down");
					break;

				case "NodeType_ilo" :
					nodeTypeDropdown.sendKeys("iLO\n");
					printLogs("Selected iLO from the Nodes Type drop down");
					break;

				case "NodeType_mooonshot" :
					nodeTypeDropdown.sendKeys("Moon\n");
					printLogs("Selected Moonshot from the Nodes Type drop down");
					break;

				case "NodeType_ipdu" :
					nodeTypeDropdown.sendKeys("Intell\n");
					printLogs("Selected iPDU from the Nodes Type drop down");
					break;

				case "NodeType_vc" :
					nodeTypeDropdown.sendKeys("Virtual\n");
					printLogs("Selected Virtual Connect from the Nodes Type drop down");
					break;

				case "NodeType_unknown" :
					nodeTypeDropdown.sendKeys("Unknown\n");
					printLogs("Selected Unknown from the Nodes Type drop down");
					break;
					
				default:
					printError("Invalid NodeType sent to the NodesAddSelectType method. Correct the test.");
					printLogs("WARNING:: Selecting Unknown from the Nodes Type drop down");
					nodeTypeDropdown.sendKeys("Unknown\n");
					printLogs("Selected Unknown from the Nodes Type drop down");
			}
			sleep(1);
			captureScreenShot();
		}		
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while selecting node type on the Add Node page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
		
	// uncheckAutoAddAssoNode: Unchecks the add associated node checkbox
	public static boolean uncheckAutoAddAssoNode(){
		printLogs("Calling uncheckAutoAddAssoNode");
		
		try{
			if(verifyCheckboxStatus("Nodes_AddNodeAssocCheckbox", "enabled")) {
				printLogs("Add Associated Nodes checkbox is enabled. Disabling that.");
				
				if(!clickByXpath("Nodes_AddNodeAssocCheckbox")) {
					printError("Failed to Uncheck Add Associated Nodes checkbox.");
					printFunctionReturn(fn_fail);
					return false;
				}
				else {
					printLogs("Successfully unchecked Add Associated Nodes checkbox");
				}
			}
			else {
				printLogs("Add Associated Nodes checkbox is already unchecked. So no changes done to the checkbox");
			}
			captureScreenShot();
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while unchecking add asso node checking.");
			printFunctionReturn(fn_fail);			
			return false;
		}
		printFunctionReturn(fn_pass);		
		return true;
	}
		
	// NodesAddSelectAdditionalBaseline: Select additional baseline for node
	public static boolean NodesAddSelectAdditionalBaseline(String additionalPkgLocationKey) {
		printLogs("Calling NodesAddSelectAdditionalBaseline with values: " + additionalPkgLocationKey);
		
		try {
			printLogs("Selecting additional Baseline on the Add Node page.");
			String additional_baseline_location = CONFIG.getProperty(additionalPkgLocationKey);
			
			// Type the path of the additional baseline location in additional package textbox
			if(!sendKeysByXpath("Nodes_AddNodeAddPkgSearchInput",additional_baseline_location)) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			driver.findElement(By.xpath(OR.getProperty("Nodes_AddNodeAddPkgSearchInput"))).sendKeys(Keys.DOWN);
			sleep(3);
			driver.findElement(By.xpath(OR.getProperty("Nodes_AddNodeAddPkgSearchInput"))).sendKeys(Keys.ENTER);
			sleep(3);
			
			captureScreenShot();
			printLogs("Selected Applicable Baseline on the Add Node page.");
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while selecting additional Baseline on the Add Node page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// NodesAddSelectBaseline
	// Select Baseline on Add Node page.
	public static boolean NodesAddSelectBaseline() {
		printLogs("Calling NodesAddSelectBaseline");
		
		try {
			printLogs("Selecting default Baseline and additional baseline on the Add Node page.");
			
			// Click the search button
			if(!clickByXpath("Nodes_AddNodeBlInputSearch")) {
				printFunctionReturn(fn_fail);				
				return false;
			}
			
			// Click the default Baseline  
			if(!clickByXpath("Nodes_AddNodeBlInputValue")) {
				printFunctionReturn(fn_fail);				
				return false;
			}
			printLogs("Selected default Baseline on the Add Node page.");
			captureScreenShot();
			
			// Select the default Additional Baseline
			// Click the search button
			if(!clickByXpath("Nodes_AddNodeAddPkgSearchDropDown")) {
				printFunctionReturn(fn_fail);				
				return false;
			}			
			// Click the additional Baseline
			if(!clickByXpath("Nodes_AddNodeAddPkgInputList")) {
					printFunctionReturn(fn_fail);	
					return false;
			}
			
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while selecting default Baseline and additional baseline on the Add Node page.");
			printFunctionReturn(fn_fail);			
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// NodesAddUnSelectBaseline
	// Unselect Baseline on Add Node page.
	public static boolean NodesAddUnSelectBaseline() {
		printLogs("Calling NodesAddUnSelectBaseline");
		
		try {
			printLogs("Unselecting default Baseline and additional baseline on the Add Node page.");
			//Nodes_AddNodeBlInputClose 
			
			if(!clickByXpath("Nodes_AddNodeBlInputClose")) {
				printFunctionReturn(fn_fail);
				captureScreenShot();
				return false;
			}
				//Nodes_AddNodeBlText
			if(!clickByXpath("Nodes_AddNodeBlText")) {
				printFunctionReturn(fn_fail);
				captureScreenShot();
				return false;
			}
			
			if(!clickByXpath("Nodes_AddNodeAddPkgInputClose")) {
					printFunctionReturn(fn_fail);	
					captureScreenShot();
					return false;
			}
			
			if(!clickByXpath("Nodes_AddNodeBlText")) {
				printFunctionReturn(fn_fail);
				captureScreenShot();
				return false;
			}
			
			if(!verifyText("Nodes_AddNodeBlInput", "")){
				printFunctionReturn(fn_fail);
				captureScreenShot();
				return false;
			}
			
			if(!verifyText("Nodes_AddNodeAddPkgSearchInput", "")){
				printFunctionReturn(fn_fail);
				captureScreenShot();
				return false;
			}
			
			
			
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while unselecting default Baseline on the Add Node page.");
			printFunctionReturn(fn_fail);			
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	
	// Select Baseline on Node Inventory page.
	public static boolean NodesInventorySelectBaseline() {
		printLogs("Calling NodesInventorySelectBaseline");
		try {
			printLogs("Selecting default Baseline on the Nodes Inventory page.");
			
			// Click the search button
			if(!clickByXpath("NodesInventory_BlInputSearch")) {
				printFunctionReturn(fn_fail);				
				return false;
			}
			
			// Click the default Baseline
			if(!clickByXpath("NodesInventory_BlInputValue")) {
				printFunctionReturn(fn_fail);				
				return false;
			}
			printLogs("Selected default Baseline on the Nodes Inventory page.");
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while selecting default Baseline on the Nodes Inventory page.");
			printFunctionReturn(fn_fail);			
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	public static boolean baselineToSelect(List<WebElement> allElements,String baselineToSelect){
		printLogs("Calling baselineToSelect with value: " + allElements + " " + baselineToSelect);
		String addNodeAddPkgListValue = "";
		try {
			// Selects the required package from the list  NodesInventory_BlInputValue
			for (WebElement element: allElements) {
				addNodeAddPkgListValue = element.getText();
				if(currentOsName.contains("windows")) {					
					if(addNodeAddPkgListValue.contains(baselineToSelect.replace('/', '\\'))){
						element.click();
						printLogs("Successfully selected the baseline :" + addNodeAddPkgListValue);						
						return true;
					}
				}
				else{					
					if(addNodeAddPkgListValue.contains(baselineToSelect)){					
						element.click();
						printLogs("Successfully selected the baseline :" + addNodeAddPkgListValue);	
						return true;
					}
				}
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while selecting Baseline on the Nodes Inventory page.");
			return true;
		}
		return false;
	}
	
	// selectBlOnNodeInventoryPage
	// Select Baseline on Node Inventory page.
	public static boolean selectBlOnNodeInventoryPage(String SelectBaseline) {
		printLogs("Calling selectBlOnNodeInventoryPage with value: " + SelectBaseline);	
		try {
			// Click the search button  
			if(!clickByXpath("NodesInventory_BlInputSearch")) {
				printFunctionReturn(fn_fail);				
				return false;
			}			
			List<WebElement> allElements = driver.findElements(By.xpath(OR.getProperty("NodesInventory_BlInputValue"))); 
			
			if(!baselineToSelect(allElements, SelectBaseline)){
				//Click on the SPP baseline arrow again.
				if(!clickByXpath("NodesInventory_BlInputSearch")) {
					printFunctionReturn(fn_fail);				
					return false;
				}
				
				// Click the additional package search button  
				if(!clickByXpath("NodesInventory_AddPkgInputSearch")) {
					printFunctionReturn(fn_fail);				
					return false;
				}
				allElements = driver.findElements(By.xpath(OR.getProperty("NodesInventory_AddPkgInputValue")));
				printLogs("Checking in the additional package baseline for the baseline for the inventory");
				baselineToSelect(allElements, SelectBaseline);
			}
			captureScreenShot();
			printLogs("Selected Baseline on the Nodes Inventory page: " + SelectBaseline);
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while selecting Baseline on the Nodes Inventory page.");
			printFunctionReturn(fn_fail);			
			return false;
		}
	}
	
	// selectAblOnAddNodePage
	// Select additional baseline from Additional Package drop down on Add Node Page.
	public static boolean selectAblOnAddNodePage(String additionalPkgLoacation){
		printLogs("Calling selectAblOnAddNodePage with values :" + additionalPkgLoacation);
		
		String addNodeAddPkgListValue = "";
		
		try{
			// Click on Additional package search drop down
			if(!clickByXpath("Nodes_AddNodeAddPkgSearchDropDown")){
				printFunctionReturn(fn_fail);
				return false;
			}
			
			List<WebElement> allElements = driver.findElements(By.xpath(OR.getProperty("Nodes_AddNodeAddPkgInputList"))); 
			            
			// Selects the required package from the list
			for (WebElement element: allElements) {
				addNodeAddPkgListValue = element.getText();
								
				if(currentOsName.equalsIgnoreCase("Windows")) {
					if(addNodeAddPkgListValue.contains(additionalPkgLoacation.replace('/', '\\'))){
						element.click();
						printLogs("Successfully selected the additional baseline :" + addNodeAddPkgListValue);
						break;
					}
				}
				else{
					if(addNodeAddPkgListValue.contains(additionalPkgLoacation)){
						element.click();
						printLogs("Successfully selected the additional baseline :" + addNodeAddPkgListValue);
						break;
					}
				}
			}
			captureScreenShot();
		}
		catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while selecting required additional baseline.");
			printFunctionReturn(fn_fail);
			return false;	
		}
		printFunctionReturn(fn_pass);	
		return true;
	}
	
	// getNodesListFromLeftPane
	// Get List of nodes added from the left pane
	public static ArrayList<String> getNodesListFromLeftPane(){
		printLogs("Calling getNodesListFromLeftPane.");
		
		int nodesLeftPaneTableRowCount = 0;
		String xPathLeftPaneNode = "";
		String nodeNameLeftPane = "";
		ArrayList<String> nodes = new ArrayList<String>();
		
		try{
			nodesLeftPaneTableRowCount = SelUtil.getTableRowCount("Nodes_LeftPaneTable");
			
			for(int i = 1; i <= nodesLeftPaneTableRowCount; i++){            	
				if(nodesLeftPaneTableRowCount == 1) {
            		xPathLeftPaneNode = OR.getProperty("Nodes_LeftPaneTable") + "/tr/td[2]"; 
            	}
            	else{
            		xPathLeftPaneNode = OR.getProperty("Nodes_LeftPaneTable") + "/tr[" + i + "]/td[2]";
            	}
            	
            	nodeNameLeftPane = SelUtil.getTextByActualXpath(xPathLeftPaneNode);
            	nodes.add(nodeNameLeftPane);
			}
			printFunctionReturn(fn_pass);
			return nodes;
			
		}
		catch(Throwable t){
			nodes.clear();
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while getting list of nodes from left pane.");
			printFunctionReturn(fn_fail);
			return nodes;
		}
	}
	
	// findNodeOnLeftPane
	// Finds whether a node is present in the left pane.
	public static boolean findNodeOnLeftPane(String expectedNodeIp){
		printLogs("Calling findNodeOnLeftPane with values: " + expectedNodeIp);
		String actualNodeIp = "";
		boolean bFoundNode = false;
		
		try{
			ArrayList<String> nodes = new ArrayList<String>();
			
			// Get the nodes list from the left pane. 
			nodes = getNodesListFromLeftPane();
			
			for(int i = 0; i < nodes.size(); i++){
				actualNodeIp = nodes.get(i);
				if(actualNodeIp.contains(expectedNodeIp)){
					bFoundNode = true;
					break;
				}
			}
			
			if (bFoundNode) {
				printLogs("The Node: " + expectedNodeIp + " is found on the left pane.");
				printFunctionReturn(fn_pass);
				return true;
			}
			else {
				printLogs("The Node: " + expectedNodeIp + " is not found on the left pane.");
				printFunctionReturn(fn_pass);
				return false;
			}
		}
		catch(Throwable t){
			printError("Exception occurred while finding node on left pane.");
			printFunctionReturn(fn_fail);
			return false;
		}
	}
	
	// Methods related to screen : Nodes Edit
	
	
	
	// Methods related to screen : Nodes Inventory 
	
	// specifyBlAndPerformNodeInventory
	public static boolean specifyBlAndPerformNodeInventory (String baselineToSelect) {
		printLogs("Calling specifyBlAndPerformNodeInventory with value " + baselineToSelect);
		
		try {
			// 1. Click on Inventory Link from Actions drop-down.
			sleep(5);
			if(!selectActionDropDownOption("CssNodesActions", "Inventory")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			sleep(5);
			captureScreenShot();
			
			if(!waitForPage("NodesInventory_window")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// 2. On Inventory window, verify:
			// 	- Heading, Buttons
			// 	- Select Baseline
			if(!verifyPage("NodesInventory")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// 3. Select BL: selectBlOnNodeInventoryPage
			if(!selectBlOnNodeInventoryPage(baselineToSelect)) {
				printError("Failed to select BL on Node Inventory page.");
			}
			
			// 3. Click Inventory Button - Make sure Inventory window closes.
			if(!clickByXpath("NodesInventory_InventoryButton")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// 4. Wait for Inventory window to disappear
			if(!waitForNoElementByXpath("NodesInventory_window")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			// Verify message-bar messages.
			// PENDING
			
			
			// 5. Wait for Nodes Inventory to complete
			
			if (!wait_inv){
				if(!WaitForNodesInventoryToComplete()) {
				printFunctionReturn(fn_fail);
				return false;
				}
			}
			
			captureScreenShot();
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while doing the Node inventory with selected BL.");
			printFunctionReturn(fn_fail);
			return false;
		}
	}
	
	// performNodeInventory : This method will do node inventory and wait for it to complete
	// 1. Click on Inventory Link from message.(or Actions->Message)
	// 2. On Inventory window, verify:
	// 		- Heading, Buttons
	// 		- Select Baseline
	// 3. Click Inventory Button
	// 		- Make sure Inventory window closes.
	// 4. Wait till Nodes Inventory completes 
	// 		- Message: Ready for Deployment
	// 		- Message: localhost has an User Action Need
	// 		- Link: Deploy	
	public static boolean performNodeInventory () {
		printLogs("Calling performNodeInventory");
		
		try {
			// 1. Click on Inventory Link from Actions drop-down.
			if(!selectActionDropDownOption("CssNodesActions", "Inventory")) {
				printError("Failed to click on Inventory from the Action dropdown");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			sleep(5);
			captureScreenShot();
			
			// Commenting the code to select BL from inventory screen
			// HP SUM GUI is now modified to behave like this.
			// For local host deploy then below sequence should work 
						
			if(verifyText(OR.getProperty("Nodes_AddNodeIPDNSInput"), "localhost")) {
			if(!waitForPage("NodesInventory_window")) {
				printFunctionReturn(fn_fail);
				appendHtmlComment("Failed to open Node Inventory window");
				return false;
			}
			
			// 2. On Inventory window, verify:
			// 	- Heading, Buttons
			// 	- Select Baseline
			if(!verifyPage("NodesInventory")) {
				printFunctionReturn(fn_fail);
				appendHtmlComment("Failed to verify Node Inventory window");
				return false;
			}
			
			// 3. Select BL
			// Skipping BL selection as we have already done that at Add Node screen.
			if(!NodesInventorySelectBaseline()) {
				printFunctionReturn(fn_fail);
				appendHtmlComment("Failed to select the baseline library on the Node Inventory window");
				return false;
			}
			
			captureScreenShot();
			
			// 4. Click Inventory Button - Make sure Inventory window closes.
			if(!clickByXpath("NodesInventory_InventoryButton")) {
				printFunctionReturn(fn_fail);
				appendHtmlComment("Failed to click on Inventory button");
				return false;
			}
			
			// 5. Wait for Inventory window to disappear
			if(!waitForNoElementByXpath("NodesInventory_window")) {
				printFunctionReturn(fn_fail);
				appendHtmlComment("Failed to close Node Inventory window");
				return false;
			}
			
			captureScreenShot();
			}
			
			// Verify message-bar messages.
			// PENDING
			
			// 6. Wait for Nodes Inventory to complete
			if (!wait_inv){
				if(!WaitForNodesInventoryToComplete()) {
					appendHtmlComment("Failed to complete Node Inventory");
					printFunctionReturn(fn_fail);
					return false;
				}
			}
			captureScreenShot();
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while doing the Node inventory.");;
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	 // performNodeInventory overloaded with remoteHostType (for HPUX)
	public static boolean performNodeInventory (String remoteHostType) {
		printLogs("Calling performNodeInventory with value " + remoteHostType);
		
		try {
			// 1. Click on Inventory Link from Actions drop-down.
			sleep(5);
			if(!selectActionDropDownOption("CssNodesActions", "Inventory")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			sleep(5);
			captureScreenShot();
			
			if(!waitForPage("NodesInventory_window")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// 2. On Inventory window, verify:
			// 	- Heading, Buttons
			// 	- Select Baseline
			if(!verifyPage("NodesInventory")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// 3. Click Inventory Button - Make sure Inventory window closes.
			if(!clickByXpath("NodesInventory_InventoryButton")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// 4. Wait for Inventory window to disappear
			if(!waitForNoElementByXpath("NodesInventory_window")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			// Verify message-bar messages.
			// PENDING
			
			// 5. Wait for Nodes Inventory to complete
			if(remoteHostType.toLowerCase().contains("hpux")) {
				if(!WaitForNodesInventoryToComplete(remoteHostType)) {
					printFunctionReturn(fn_fail);
					return false;
				}
			}
			else {
				if(!WaitForNodesInventoryToComplete()) {
					printFunctionReturn(fn_fail);
					return false;
				}
			}
			
			captureScreenShot();
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while doing the Node inventory.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// WaitForNodesInventoryToComplete - On Nodes Page
	public static boolean WaitForNodesInventoryToComplete() {
		printLogs("Calling WaitForNodesInventoryToComplete");
		
		//String msgSlfInProgress = "Self-inventory in progress";
		//String msgInProgress = "Inventory in progress";
		//String msgCompleted  = "Ready for deployment.";
		String msgInProgress = OR.getProperty("MessageNodeInventoryInProgress");
		String msgCompleted  = OR.getProperty("MessageNodeInventoryComplete");
		String msgNoComponents = OR.getProperty("MessageNodeInventoryNoApplicableComponents");
		String msgAlreadyBasleine = OR.getProperty("MessageNodeInventoryAlreadyAtBaseline");
		String msgAboveBasleine = OR.getProperty("MessageNodeInventoryAboveBaseline");
		
		int maxWaitTime = 1080;	// This is multiplied by 5 as there is sleep(5). So actual time = 1080*5 = 5400s = 90m
		
		String inventoryStatusOld = "Fresh";
		String inventoryStatus = "";
		int statusCount = 0;
		int timeout = 0;
		
		try {
			printLogs("Waiting for Nodes inventory to complete");
			
			// Click on the message bar - Common_PageMsgBar
			if(!clickByXpath("Common_PageMsgBar")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			startWatch();
			int refresh = 0;
			
			while(true) {
	    		inventoryStatus = getTextByXpath("NodesInventory_Msg");
	    		
	    		if(inventoryStatus.contains(msgCompleted) || inventoryStatus.contains(msgAlreadyBasleine) || inventoryStatus.contains(msgAboveBasleine)) {
	    			printLogs("Nodes inventory completed");
	    			break;
	    		}
	    		
	    		if(!inventoryStatus.contains(inventoryStatusOld)) {
	    			statusCount = 0;
	    			refresh = 0;
	    		}
	    		else {
	    			statusCount++;
	    			refresh++;
	    		}
	    		
	    		if(statusCount == 0) {
	    			//printLogs("nodeInventoryStatus = " + inventoryStatus);
	    			
	    			if(!inventoryStatus.contains(msgInProgress)) {
	    				printLogs("nodeInventoryStatus = " + inventoryStatus);
	    				//printLogs("Nodes inventory in progress...");
		    			//printLogs("Any change in the status will be printed. Please wait...");
		    		}
		    		
	    			if(inventoryStatus.contains(msgCompleted)) {
		    			printLogs("Nodes inventory completed");
		    			break;
		    		}
	    			
	    			if (inventoryStatus.contains(msgNoComponents)) {
	    				printLogs(msgNoComponents + ", Skiping the test case. Rerun with new set of components.");
	    				appendHtmlComment(msgNoComponents + ". Rerun the test with new set of components.");
						printFunctionReturn(fn_fail);
		    			return false;
	    			}
	    			if(driver.findElement(By.xpath(OR.getProperty("Nodes_Inventory_msg_err"))).getAttribute("class").contains("hp-status-error")) {
	    				if(inv_error){
	    					printLogs("Associated node details: " + getTextByXpath("Common_PageMsgBarMessage2p"));
	    					if(!getTextByXpath("Common_PageMsgBar").contains(OR.getProperty("MessageNodeAddAssociatedNodeDetaisl"))){
	    						printLogs("Looking for the Enter associated Onboard Administrator details link in message bar");	    						
	    						printError("get text by xpath Loopback address test is not displayed in the IP/DNS field");
	    						printFunctionReturn(fn_fail);
	    						return false;
	    					}	    					
	    					if(!checkElementPresenceByXpath("Common_PageMsgBarLink")){
    							printError("Enter associated Onboard Administrator details link is not avaible in the message bar");
	    						printFunctionReturn(fn_fail);
	    						return false;
    						}
	    					printLogs(" Expected error message is avaible in the message bar and the edit link is also enabled");
	    					return true;
	    				}else{
	    					printLogs(inventoryStatus + ", Check the test case with all preconditions. Rerun after resolving the errors.");
		    				appendHtmlComment(inventoryStatus + ". Rerun the test by resolving the error.");
		    				printFunctionReturn(fn_fail);
							return false;
	    				}
	    				
					}
	    			inventoryStatusOld = inventoryStatus;
	    		}
	    		
	    		// WORKAROUND: REFRESH THE PAGE EVERY 40 MINS if the processing component remain the same.
	    		if(refresh > 480 && statusCount != 0) {
	    			printLogs("WORKAROUND: Refreshing the page.");
	    			driver.navigate().refresh();
	    			sleep(10);
	    			refresh = 0;
	    		}
	    		
	    		if(timeout > maxWaitTime) {
	    			printError("Timeout occurred while waiting for Nodes inventory to complete");
	    			stopWatch();
					appendHtmlComment("Failed to complete the node inventory in " + maxWaitTime * 5 + "s");
					printFunctionReturn(fn_fail);
	    			return false;
	    		}
	    		sleep(5);
	    		timeout++;
		    }
			stopWatch();
			// Click on the message bar - Common_PageMsgBar - To close the message bar.
			if(!clickByXpath("Common_PageMsgBar")) {
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while waiting for Nodes inventory to complete.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// WaitForNodesInventoryToComplete overloaded with parameter remoteHostType for HPUX
	public static boolean WaitForNodesInventoryToComplete(String remoteHostType) {
		printLogs("Calling WaitForNodesInventoryToComplete with values: " + remoteHostType);
		
		String msgInProgress = OR.getProperty("MessageNodeInventoryInProgress");
		String msgCompleted  = OR.getProperty("MessageNodeInventoryComplete");
		 
		int maxWaitTime = 480;	// This is multiplied by 5 as there is sleep(5). So actual time = 2400s = 40m
		
		String inventoryStatusOld = "Fresh";
		String inventoryStatus = "";
		int statusCount = 0;
		int timeout = 0;
		
		try {
			printLogs("Waiting for Nodes inventory to complete");
			
			// Click on the message bar - Common_PageMsgBar
			if(!clickByXpath("Common_PageMsgBar")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			startWatch();
			int refresh = 0;
			
			while(true) {
	    		inventoryStatus = getTextByXpath("NodesInventory_Msg");
	    		
	    		if(remoteHostType.toLowerCase().contains("hpux")) {
	    			String msgCompletedHpux  = OR.getProperty("MessageNodeInventoryCompleteHpux");
	    			if(inventoryStatus.contains(msgCompletedHpux)) {
		    			printLogs("HP-UX Node inventory completed");
						
		    			if(!clickByXpath("Common_PageMsgBar")) {
		    				printFunctionReturn(fn_fail);
		    				return false;
		    			}
		    			captureScreenShot();
						
		    			//Click on Nodes_HpuxConfirmLink
		    			if(!clickByXpath("Nodes_HpuxConfirmLink")) {
		    				printFunctionReturn(fn_fail);
		    				return false;
		    			}
						
		    			sleep(20);
		    			captureScreenShot();
		    			break;
		    		}	    			
	    		}
	    		
	    		if(!inventoryStatus.contains(inventoryStatusOld)) {
	    			statusCount = 0;
	    		}
	    		else {
	    			statusCount++;
	    		}
	    		
	    		if(statusCount == 0) {
	    			//printLogs("nodeInventoryStatus = " + inventoryStatus);
	    			
	    			if(!inventoryStatus.contains(msgInProgress)) {
	    				printLogs("nodeInventoryStatus = " + inventoryStatus);
	    				
	    				//printLogs("Nodes inventory in progress...");
		    			//printLogs("Any change in the status will be printed. Please wait...");
		    		}
		    		
	    			if(inventoryStatus.contains(msgCompleted)) {
		    			printLogs("Nodes inventory completed");
		    			break;
		    		}
	    			
	    			inventoryStatusOld = inventoryStatus;
	    		}
	    		
	    		// WORKAROUND: REFRESH THE PAGE EVERY 5 MINS.
	    		if(refresh > 60) {
	    			printLogs("WORKAROUND: Refreshing the page.");
	    			driver.navigate().refresh();
	    			sleep(10);
	    			refresh = 0;
	    		}
	    		
	    		if(timeout > maxWaitTime) {
	    			printError("Timeout occurred which waiting for Nodes inventory to complete");
	    			break; 
	    		}
	    		sleep(5);
	    		timeout++;
	    		refresh++;
		    }
			stopWatch();
			
			// Click on the message bar - Common_PageMsgBar - To close the message bar.
			if(!clickByXpath("Common_PageMsgBar")) {
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while waiting for Nodes inventory to complete.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	
	//Methods related to screen : Nodes Deploy
	
	// deployNode : This method will deploy the node
	// In case of Localhost, Pass: hostIp="localhost" and hostType="local"
	public static boolean deployNode (String hostIp,
									  String hostType) {
		
		printLogs("Calling deployNode");
		
		try {
			// 1. Click the Deploy link. (or Actions->Deploy)
			if(!selectActionDropDownOption("CssNodesActions", "Review/ Deploy")) {
				printError("Failed to click on Review/Deploy option under Action dropdown");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			if(!waitForPage("Common_NewWindow")) {
				printError("Failed to open Deploy page");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			/* 2. On Deploy Screen, verify
			 * 		- Name
			 * 		- Buttons present
			 */
			
			// WORKAROUND:: As selenium is NOT able to getText from Reboot Options 
			// So we are collapsing the default opened options - Associated Node Details and Baseline Library
			if(!clickByXpath("NodesDeploy_AssoNodeDetailsCollapse")) {
				printError("Error: Failed to collapse AssoNodeDetails");
			}
			
			if(hostType.contains("NodeType_fc_switch")){
				if(!verifyPage("NodesDeploy_Switch")){
					printError("Error: Failed to verify deploy page.");
				}
			}
			else{	
				if(!verifyPage("NodesDeploy")) {
					printError("Error: Failed to verify deploy page.");
				}
			}
			
			// Print the Baseline Library table contents
			if(!printTableContents("NodesDeploy_BlLibTableForRemoteNodes", 3, true, hostIp)) {
				printError("Failed to print the table contents");
			}
			
			if(!verifyButtonStatus("NodesDeploy_CancelButton", "enabled")) {
				printError("Error: Failed to verify Cancel Button.");
			}
						
			 /* 
			  * 4. Click Deploy button - But first start the autoIT script if it is windows and running on localhost
			  * 		- Verify Deploy window closes
			 */
			 
			// Run the AutoIT script to close the popups which come for unauthorized components.
			// Uncomment this with new/unreleased SPPs.
			if (hostIp.toLowerCase().contains("localhost")) {
				if (currentOsName.contains("windows")) {
					if(!executeScript("autoItDeployPopUpScript", false)) {
						printError("Error: Failed to run autoIt script.");
					}
				}
			}
			 
			captureScreenShot();
			
			if(!resolveFailedDependencyAndDeploy("NodesDeploy_BlLibTableForRemoteNodes", "NodesDeploy_DeployButton", hostIp)) {
				printError("Failed to resolve the dependencies.");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			/*if(!clickByXpath("NodesDeploy_DeployButton")) {
				printError("Error: Failed to click Deploy Button.");
			}
			sleep(10);
			captureScreenShot();
			
			// Wait for Deploy window to disappear
			if(waitForNoElementByXpath("Common_NewWindow")) {
				printLogs("Deploy started.");
			}*/
			
			captureScreenShot();
			
			/* int temp_count = 1;
			while (temp_count <= 3) {
				if (temp_count == 2) {
					printLogs("Retry:: clicking Deploy button.");
				}
				if (temp_count == 3) {
					printLogs("Closing the Deploy popup screen using the Cancel button.");
					if(!clickByXpath("NodesDeploy_CancelButton")) {
						printError("Error: Failed to click Deploy Cancel Button.");
					}
					break;
				}
				
				if(!clickByXpath("NodesDeploy_DeployButton")) {
					printError("Error: Failed to click Deploy Button.");
				}
				
				// Wait for Deploy window to disappear
				if(!waitForNoElementByXpath("Common_NewWindow")) {
					printError("Deploy popup screen not closing after pressing deploy button.");
					
					captureScreenShot();
				}
				else {
					break;
				}
				temp_count++;
			}*/
			
			// Verify that deploy has started. If yes, then continue to wait for deploy to finish
			// else, return false saying - deploy button click has failed.
			// PENDING

			// 5. Wait for Deployment to complete - Message: Install Done
			captureScreenShot();
			if(!WaitForNodesDeployToComplete()) {
				appendHtmlComment("Failed to complete deployment");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			// Kill the autoItDeployscript
			//if (hostIp.contains("localhost")) {
			if (hostIp.toLowerCase().contains("localhost")) {
				if (currentOsName.contains("windows")) {
					if(!executeScript("killAutoItDeployPopUpScript", false)) {
						printError("Error: Failed to kill autoIt script.");
					}
				}
			}
			
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while doing the node deploy.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// unselectFailedDependencies
	public static boolean unselectFailedDependencies(String xPathKey) {
		printLogs("Calling unselectFailedDependencies with values: " + xPathKey);
		printLogs("Unselecting the failed dependencies if any in table: " + xPathKey);
		
		int rowCount = getTableRowCount(xPathKey);
		if(rowCount == -1) {
			printFunctionReturn(fn_fail);
			return false;
		}
		if(rowCount == 0) {
			printError("No rows found in table: " + xPathKey);
			printError("No rows found in the table.");
			printFunctionReturn(fn_fail);
			return false;
		}
		
		int colCount = getTableColumnCount(xPathKey);
		if(colCount == -1) {
			printFunctionReturn(fn_fail);
			return false;
		}
		if(colCount == 0) {
			printError("No cols found in table: " + xPathKey);
			printError("No cols found in the table.");
			printFunctionReturn(fn_fail);
			return false;
		}
		
		String xPathOfTable = "";
		
		// wait for inventory to complete
		//String xPathOfTable = OR.getProperty(xPathKey);
		
		// If the xPathKey is already the Actual xPath then getting the xPath from OR.properties is NOT needed.
		if (xPathKey.startsWith("//")) {
			xPathOfTable = xPathKey;
		}
		else {
			xPathOfTable = OR.getProperty(xPathKey);
		}
		
		String xPathOfRow = "";
		String xPathOfRowPart1 = xPathOfTable + "/tr";
		String xPathOfRowPart2 = "";
		String xPathOfCol = "";
		
		try {
			for(int i = 1 ; i <= rowCount ; i++) {
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow =  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfCol = xPathOfRow + "/td[3]/div/div";
				String xPathToClick =  xPathOfRowPart1 + xPathOfRowPart2 + "/td[1]";
				
				// If the row is just a heading because of many elements of same family
				// Handle that condition
				String xPathOfHeadingRow = xPathOfRow + "/td";
				
				if (driver.findElements(By.xpath(xPathOfHeadingRow)).size() < 2) {
					printLogs("WARNING:: Found heading row in the table");
					//String xPathOfHeading = xPathOfHeadingRow + "/b";
					//printLogs(driver.findElement(By.xpath(xPathOfHeading)).getText());
					continue;
				}
				
				if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-error")) {
					if(!clickByActualXpath(xPathToClick)) {
						printFunctionReturn(fn_fail);
						return false;
					}
					printLogs("Unselected row: " + i);
				}
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while printing the table.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// getFailedDependenciesStatus
	public static int getFailedDependenciesStatus(String xPathKey) {
		printLogs("Calling getFailedDependenciesStatus with values: " + xPathKey);
		printLogs("Getting the failed dependencies in table: " + xPathKey);
		
		int countFailDependency = 0;
		int rowCount = getTableRowCount(xPathKey);
		if(rowCount == -1) {
			printFunctionReturn(fn_fail);
			return(-1);
		}
		if(rowCount == 0) {
			printLogs("No rows found in table: " + xPathKey);
			printError("So there will not be any failed dependencies.");
			printFunctionReturn(fn_pass);
			return(0);
		}
		
		int colCount = getTableColumnCount(xPathKey);
		if(colCount == -1) {
			printFunctionReturn(fn_fail);
			return(-1);
		}
		if(colCount == 0) {
			printLogs("No cols found in table: " + xPathKey);
			printLogs("So there will not be any failed dependencies.");
			printError("This should never be true as per HPSUM functionality.");
			printFunctionReturn(fn_pass);
			return(0);
		}
		
		String xPathOfTable = "";
		
		// wait for inventory to complete
		//xPathOfTable = OR.getProperty(xPathKey);
		
		// If the xPathKey is already the Actual xPath then getting the xPath from OR.properties is NOT needed.
		if (xPathKey.startsWith("//")) {
			xPathOfTable = xPathKey;
		}
		else {
			xPathOfTable = OR.getProperty(xPathKey);
		}
		
		String xPathOfRow = "";
		String xPathOfRowPart1 = xPathOfTable + "/tr";
		String xPathOfRowPart2 = "";
		String xPathOfCol = "";
		printLogs("========================================");
		try {
			for(int i = 1 ; i <= rowCount ; i++) {
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow =  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfCol = xPathOfRow + "/td[3]/div/div";
				
				// If the row is just a heading because of many elements of same family
				// Handle that condition
				String xPathOfHeadingRow = xPathOfRow + "/td";
				
				if (driver.findElements(By.xpath(xPathOfHeadingRow)).size() < 2) {
					printLogs("WARNING:: Found heading row in the table");
					//String xPathOfHeading = xPathOfHeadingRow + "/b";
					//printLogs(driver.findElement(By.xpath(xPathOfHeading)).getText());
					continue;
				}
				
				printLogs(i + "  -> component status after clicking deploy -> " + driver.findElement(By.xpath(xPathOfCol)).getAttribute("class"));
				
				if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-error")) {
					if(fips_faildep){						
						if(!clickByXpath("NodesDeploy_faildep_error")){
							captureScreenShot();
							printError("Failed to click on failed dependency error link and verify the xpath");
							appendHtmlComment("Failed to click on failed dependency error link");
							throw new TestException("Failed to click on failed dependency error link and verify the xpath");
						}
						captureScreenShot();
						printLogs("Error message for VC " + getTextByXpath("NodesDeploy_faildep_Downgade" + OR.getProperty("MessagefaildepVCrollback")));
						if(!getTextByXpath("NodesDeploy_faildep_Downgade").toLowerCase().contains(OR.getProperty("MessagefaildepVCrollback"))){
							printError("VC downgrade message is not populated in the failed dependency and the value is " + getTextByXpath("NodesDeploy_faildep_Downgade"));
							appendHtmlComment("VC rollback message is not displayed in the failed dependency setion on component details screen");
							//throw new TestException("VC downgrade message is not populated in the failed dependency");
						}
						if(!clickByXpath("NodesDeploy_faildep_Close_Button")){
							captureScreenShot();
							printError("Failed to click on NodesDeploy_faildep_Close_Button");							
							//throw new TestException("Failed to click on NodesDeploy_faildep_Close_Button");
						}
						return(-2);
					}
					countFailDependency++;
				}				
			}
			printLogs("========================================");			
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while getting the failed dependencies. " + t.getMessage());
			printFunctionReturn(fn_fail);
			return(-1);
		}
		printFunctionReturn(fn_pass);
		return(countFailDependency);
	}
	

	
	// resolveFailedDependencyAndDeploy
	public static boolean resolveFailedDependencyAndDeploy(String xPathKeyTable,
															  String xPathKeyDeployButton, 
															  String remoteNodeIp) {
		printLogs("Calling resolveFailedDependencyRemoteDeploy with values: " + xPathKeyTable + ", " + xPathKeyDeployButton);
		sleep(2);
					
		boolean dependencySolved = false;
		int countFailDependency = 0;
		//int j = 1;
		
		try {
			String xPathOfTable = OR.getProperty(xPathKeyTable);
			
			if(remoteNodeIp.toLowerCase().indexOf("none") == -1) {
				xPathOfTable = replaceNodeIdDummyTextByActualId(xPathOfTable, remoteNodeIp);
				printLogs("Actual xPath of table = " + xPathOfTable);
			}
			else {
				xPathOfTable = replaceNodeIdDummyTextByActualId(xPathOfTable, "none");
				printLogs("Actual xPath of table = " + xPathOfTable);
			}
			
			int resolveCount = 0;
			while(!dependencySolved) {
				countFailDependency = getFailedDependenciesStatus(xPathOfTable);
				if(fips_faildep){
					if(countFailDependency == -2){
						printLogs("proper error message is displayed in the component details screen for rollback option");
						return false;
					}
				}
				if(countFailDependency == -1) {
					throw new Exception();
				}
				else if(countFailDependency == 0) {
					printLogs("No Failed dependency found in the table.");
					// Click on the Deploy button
					if(!clickByXpath(xPathKeyDeployButton)) {
						captureScreenShot();
						throw new Exception();
					}
					printLogs("Clicked Deploy button.");
					sleep(20);
					// Wait for Deploy window to disappear
					if(waitForNoElementByXpath("Common_NewWindow")) {
						printLogs("Deploy started.");
						break;
					}
				}
				else {
					printLogs("Found " + countFailDependency + " failed dependencies in the table. Resolving it.");
					
					if(!unselectFailedDependencies(xPathOfTable)) {
						throw new Exception();
					}
					sleep(5);
					// Click on the Deploy button
					if(!clickByXpath(xPathKeyDeployButton)) {
						captureScreenShot();
						throw new Exception();
					}
					printLogs("Clicked Deploy button.");
					sleep(20);
					// Wait for Deploy window to disappear
					if(waitForNoElementByXpath("Common_NewWindow")) {
						printLogs("Deploy started.");
						break;
					}
					else {
						printLogs("Still have failed dependencies. Resolving it again.");
						resolveCount++;
						if (resolveCount == 10) {
							printError("Dependency not solved even after retrying for 10 times.");
							printFunctionReturn(fn_fail);
							return false;
						}
					}
				}
			}
		}
		catch (Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while resolving the failed dependency on the remote deploy screen.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// printTableContents
	// Prints the contents of a table
	// Please note some tables change the xPaths as per the IP of the node. 
	// E.g. BL table on Remote Nodes Deploy screen. NodesDeploy_BlLibTableForRemoteNodes
	// Pass the IP of the Remote Node in such cases. This function will convert the xPath wrt remote node.
	// printTableContents: For Localhost pass remoteNodeIp="localhost"
	public static boolean printTableContents(String xPathKey, int statusColumn, boolean forceAll, String hostIp) {
		printLogs("Calling printTableContents with values: " + xPathKey + ", " + statusColumn + ", " + forceAll + ", " + hostIp);
		
		String xPathOfTable = OR.getProperty(xPathKey);
		
		//if(hostIp.toLowerCase().indexOf("none") == -1) {
			//xPathOfTable = replaceNodeIdDummyTextByActualId(xPathOfTable, hostIp);
			//printLogs("Actual xPath of table = " + xPathOfTable);
		//}
		//else {
			//xPathOfTable = replaceNodeIdDummyTextByActualId(xPathOfTable, "none");
			//printLogs("Actual xPath of table = " + xPathOfTable);
		//}
		
		xPathOfTable = replaceNodeIdDummyTextByActualId(xPathOfTable, hostIp);
		printLogs("Actual xPath of table = " + xPathOfTable);
		
		//int rowCount = getTableRowCount(xPathKey);
		int rowCount = getTableRowCount(xPathOfTable);
		if(rowCount == -1) {
			printFunctionReturn(fn_fail);
			return false;
		}
		if(rowCount == 0) {
			printError("No rows found in table: " + xPathOfTable);
			printFunctionReturn(fn_fail);
			return false;
		}
		//else {
			//printLogs(xPathKey + " row count = " + rowCount);
		//}
		
		int colCount = getTableColumnCount(xPathOfTable);
		if(colCount == -1) {
			printFunctionReturn(fn_fail);
			return false;
		}
		if(colCount == 0) {
			printError("No cols found in table: " + xPathOfTable);
			printFunctionReturn(fn_fail);
			return false;
		}
		
		// wait for inventory to complete
		
		
		String xPathOfRow = "";
		String xPathOfRowPart1 = xPathOfTable + "/tr";
		String xPathOfRowPart2 = "";
		
		String xPathOfCol = "";
		String xPathOfColPart1 = "";
		String xPathOfColPart2 = "";
		
		String colData = "";
		String rowData = "";
		
		printLogs("Table Contents::");
		
		try {
			for(int i = 1 ; i <= rowCount ; i++) {
				rowData = "";
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow =  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfColPart1 = xPathOfRow + "/td";
				int repeat = 0;
				
				// If the row is just a heading because of many elements of same family
				// Handle that condition
				if (driver.findElements(By.xpath(xPathOfColPart1)).size() < 2) {
					printLogs("WARNING:: Found heading row in the table");
					String xPathOfHeading = xPathOfColPart1 + "/b";
					printLogs(driver.findElement(By.xpath(xPathOfHeading)).getText());
					continue;
				}
				
				for(int j = 1 ; j <= colCount ; j++) {
					colData = "";
					if (colCount == 1) {
						xPathOfColPart2 = "";
					}
					else {
						xPathOfColPart2 = "[" + j + "]";
					}
					
					xPathOfCol =  xPathOfColPart1 + xPathOfColPart2;
					repeat = 0;
					
					if (j == 1) {
						xPathOfCol = xPathOfCol + "/a";
						
						if (forceAll) {
						
							//class="hp-toggle hp-checked"
							//class="hp-toggle"
							if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-toggle hp-checked")) {
								colData = "Checked";
								repeat = 2;
							}
							else {
								colData = "Unchecked";
								repeat = 1;
							}
							for(int z = 1; z <= repeat ; z++){
								driver.findElement(By.xpath(xPathOfCol)).click();
								sleep(1);
							}
						}
						colData = colData + "-" + getTextByActualXpath(xPathOfCol);
					}
					else if (j == statusColumn) {
						xPathOfCol = xPathOfCol + "/div/div";
						if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-error")) {
							colData = "ERROR";
						}
						else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-ok")) {
							colData = "OK";
						}
						else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-disabled")) {
							colData = "DISABLED";
						}
						else {
							colData = "NONE";
						}
					}
					else {
						colData = getTextByActualXpath(xPathOfCol);
					}
					//System.out.println(colData);
					
					rowData = rowData + " ==> " + colData;
				}
				printLogs(rowData);
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while printing the table.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// viewLogsAfterDeploy
	// This should be called after the node deploy is done and the 'View Logs' button is visible.
	// The control is expected to be on 'Nodes' Page.
	// This method will click the 'View Logs' button -> Wait for 'Installation logs' to load -> 
	// Print the table contents -> Click each of the component logs -> Copy the logs and put in log file 
	public static boolean viewLogsAfterDeploy(String hostIp, String nodeType) {
		printLogs("Calling viewLogsAfterDeploy for: " + hostIp);
		
		boolean failure_flag = false;
		String xPathKeyInstallationResultsTable = "NodesInstallationLogs_InstallationResultsTable";
		String xPathKeyViewLogsWindowDetails = "NodesInstallationLogs_DetailsLogs";
		String xPathViewLogsButton = "";
		ArrayList<String> tableList = new ArrayList<String>();
		boolean b_printedTable = false;
		
		try {
			// Take screenshot
			// Click the Nodes_DeployViewLogButton
			// Wait for Common_NewWindow exists
			// Take screenshot
			// Verify Page will verify:
			//    heading NodesInstallationLogs_Heading = "Installation logs" text
			//    NodesInstallationLogs_GeneralSH text
			//    NodesInstallationLogs_DetailsSH text
			// Verify title NodesInstallationLogs_Title = <hostIp>
			// Verify NodesInstallationLogs_CloseButton exists and isEnabled
			// For each component::
			//    Click 'View log'
			//    Verify NodesInstallationLogs_DetailsComponentId
			//    Copy the NodesInstallationLogs_DetailsLogs and put in log-file
			//    Take screenshot
			// Print the table contents
			// Click the Close button on the Common_NewWindow
			// Wait for Common_NewWindow to close.
			// Take screenshot
			
			
			captureScreenShot();
			switch (nodeType) {
			
				case "NodeType_windows" : 
				case "NodeType_vmware"  :
				case "NodeType_linux"   :
				case "NodeType_hpux"    :
				case "localhost"        :
					xPathViewLogsButton="Nodes_DeployViewLogButton_Server";
					break;
					
				case "NodeType_ilo" :
					xPathViewLogsButton="Nodes_DeployViewLogButton_Ilo";
					break;
					
				case "NodeType_enclosure" :
					xPathViewLogsButton="Nodes_DeployViewLogButton_Oa";
					break;
					
				case "NodeType_vc" :
					xPathViewLogsButton="Nodes_DeployViewLogButton_Vc";
					break;
					
				case "NodeType_fc_switch" :
					xPathViewLogsButton="Nodes_DeployViewLogButton_SanSwitch";
					break;
					
				default:
					printError("Error: Unable to click View Logs button");
					
			}
			if(!clickByXpath(xPathViewLogsButton)) {
				printError("Error: Failed to click View Log Button.");
			}
			captureScreenShot();
			
			// Wait for Installation logs window
			if(!waitForPage("Common_NewWindow")) {
				appendHtmlComment("Failed to open the View Logs page");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Verify page.
			if(!verifyPage("NodesInstallationLogs")) {
				printError("Error: Failed to verify page Installation logs.");
			}
			
			// Verify title NodesInstallationLogs_Title = <hostIp>
			if(!verifyText("NodesInstallationLogs_Title", hostIp)) {
				printError("Error: Host IP does not match.");
			}
			
			// Verify Close button exists and isEnabled
			if(!verifyButtonStatus("NodesInstallationLogs_CloseButton", "enabled")) {
				printError("Error: Failed to verify Close button status.");
			}
			
			// ******** Go through each component ***********
			
			printLogs("Getting View Logs from the table: " + xPathKeyInstallationResultsTable);
			
			int rowCount = getTableRowCount(xPathKeyInstallationResultsTable);
			if(rowCount == -1) {
				appendHtmlComment("Error occurred in getTableRowCount method");
				printFunctionReturn(fn_fail);
				return false;
			}
			if(rowCount == 0) {
				printLogs("No View-Logs as No rows found in table: " + xPathKeyInstallationResultsTable);
				appendHtmlComment("Failed to list the component in Veiw Logs page even though deployment happened");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			int colCount = getTableColumnCount(xPathKeyInstallationResultsTable);
			if(colCount == -1) {
				appendHtmlComment("Error occurred in getTableColumnCount method");
				printFunctionReturn(fn_fail);
				return false;
			}
			if(colCount == 1) {
				printLogs("No View-Logs as No columns found in table: " + xPathKeyInstallationResultsTable);
				appendHtmlComment("Failed to list the component in Veiw Logs page with right number of columns");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			String xPathOfTable = OR.getProperty(xPathKeyInstallationResultsTable);
			String xPathOfRow = "";
			String xPathOfRowPart1 = xPathOfTable + "/tr";
			String xPathOfRowPart2 = "";
			String xPathOfCol = "";
			String xPathOfDeployStatus = "";
			String xPathOfComponent= "";
			String xPathOfPackage = "";
			String deployStatus = "";
			
			printLogs("Clicking View Log one-by-one::");
			
			for(int i = 1 ; i <= rowCount ; i++) {
				if (rowCount == 1) {
					xPathOfRowPart2 = "";
				}
				else {
					xPathOfRowPart2 = "[" + i + "]";
				}
				xPathOfRow =  xPathOfRowPart1 + xPathOfRowPart2;
				xPathOfCol = xPathOfRow + "/td[5]/a";
				xPathOfDeployStatus = xPathOfRow +  "/td[4]";				
				xPathOfPackage = xPathOfRow +  "/td[3]";
				xPathOfComponent = xPathOfRow +  "/td[2]";
				
				//checking error or already current in logs
				if ((deployStatus  = getTextByActualXpath(xPathOfDeployStatus)).contains("error")) {					
					String errorComponent = getTextByActualXpath(xPathOfComponent);
					String errorPackage = getTextByActualXpath(xPathOfPackage);
					appendHtmlComment(deployStatus + " - " + errorComponent + " : " + errorPackage);
				}
				else if ((deployStatus = getTextByActualXpath(xPathOfDeployStatus)).contains("already current")) {
					String updatedComponent = getTextByActualXpath(xPathOfComponent);
					String updatedPackage = getTextByActualXpath(xPathOfPackage);
					appendHtmlComment(deployStatus + " - " + updatedComponent + " : " + updatedPackage);
				}
				printLogs("Clicking View Log for row : " + i);

				// Click the View Log Button - link
				if(!clickByActualXpath(xPathOfCol)) {
					appendHtmlComment("Failed to click on the View log components of row " + i);
					printFunctionReturn(fn_fail);
					return false;
				}
				
				// Take the screen-shot of the View-Log window
				captureScreenShot();
				
				// Get the contents of the View-Log window and print
				String sText = getValueByXpath(xPathKeyViewLogsWindowDetails);
				
				if(sText.contains(sErrorString)) {
					failure_flag = true;
					printError("Got Error while getting text for row : " + i);
				}
				else {
					printLogs("View Log Text: " + sText);
				}
				
				printLogs("Captured Screen shot of the View-Log and also printed the contents of the same.");
				
				//String xPathOfCol = "";
				String xPathOfColPart1 = "";
				String xPathOfColPart2 = "";
				
				String colData = "";
				String rowData = "";
				
				xPathOfColPart1 = xPathOfRow + "/td";
				
				for(int j = 1 ; j <= colCount ; j++) {
					colData = "";
					if (colCount == 1) {
						xPathOfColPart2 = "";
					}
					else {
						xPathOfColPart2 = "[" + j + "]";
					}
					
					xPathOfCol =  xPathOfColPart1 + xPathOfColPart2;
					
					// First column is Status.  j=1
					if (j == 1) {
						xPathOfCol = xPathOfCol + "/div/div";
						
						// Get the icon type.
						if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-error")) {
							colData = "ERROR";
						}
						else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-ok")) {
							colData = "OK";
						}
						else if(driver.findElement(By.xpath(xPathOfCol)).getAttribute("class").contains("hp-status-disabled")) {
							colData = "DISABLED";
						}
						else {
							colData = "NONE";
						}
					}
					/*
					if (j == 5) {
						xPathOfCol = xPathOfCol + "/a";						
						colData = getTextByActualXpath(xPathOfCol);
					}
					*/
					else {
						colData = getTextByActualXpath(xPathOfCol);
					}
					//System.out.println(colData);
					
					rowData = rowData + " ==> " + colData;
				}
				//printLogs(rowData);
				// Append to the List
				tableList.add(rowData);
			}
			
			// ******** All components done ************
			
			// Print the table contents
			printLogs("-----------------------------------------------------------------------");
			for(int i = 0; i < tableList.size(); i++) {  
				printLogs(tableList.get(i));
			}
			printLogs("-----------------------------------------------------------------------");
			b_printedTable = true;
			
			if(failure_flag) {
				printError("Encountered error in the method.");
				appendHtmlComment("Failed as there is an error in the component name");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Click on the Close button
			if(!clickByXpath("NodesInstallationLogs_CloseButton")) {
				printError("Error: Failed to verify Close button status.");
			}
			captureScreenShot();
			
			// Wait for window to close.
			if(!waitForNoElementByXpath("Common_NewWindow")) {
				appendHtmlComment("Failed to close the View Logs Window");
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while handling View Log.");
			
			if(!b_printedTable) {
				// Print the table list
				printLogs("-----------------------------------------------------------------------");
				for(int i = 0; i < tableList.size(); i++) {  
					printLogs(tableList.get(i));
				}
				printLogs("-----------------------------------------------------------------------");
			}
			appendHtmlComment("Error occured in viewLogsAfterDeploy method");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// WaitForNodesDeployToComplete - On Nodes Page
	public static boolean WaitForNodesDeployToComplete() {
		printLogs("Calling WaitForNodesDeployToComplete");
		
		//String msgInstallDoneWithErrors = "Install done with error(s)";
		//String msgInProgress = "Install in progress";
		//String msgCompleted  = "Install done";
		String msgInProgress = OR.getProperty("MessageNodeDeployInProgress");
		String msgCompleted  = OR.getProperty("MessageNodeDeployComplete");
		String msgError = OR.getProperty("MessageNodeDeployError");
		
		int maxWaitTime = 1080;	// This is multiplied by 5 as there is sleep(5). So actual time = 5400s = 1hr 30m
		
		String deployStatusOld = "Fresh";
		String deployStatus = "";
		int statusCount = 0;
		int timeout = 0;
		boolean failureFlag = false;
		
		try {
			printLogs("Waiting for Nodes inventory to complete");
			
			// Click on the message bar - Common_PageMsgBar
			if(!clickByXpath("Common_PageMsgBar")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			startWatch();
			int refresh = 0;
			int pendingRefresh = 0;
			
			while(true) {
	    		deployStatus = getTextByXpath("NodesDeploy_Msg");
	    		
	    		if(deployStatus.contains(sErrorString)) {
	    			printLogs("nodeDeployStatus = " + deployStatus);
	    			pendingRefresh++;
	    			
	    			if (pendingRefresh == 2) {
		    			printLogs("WORKAROUND: Refreshing the page.");
		    			driver.navigate().refresh();
		    			sleep(10);
		    			continue;
		    		}
	    			if (pendingRefresh > 2) {
		    			printError("Failed to get deploy text from message bar even after refreshing the page.");
		    			failureFlag = true;
						break;
		    		}
	    			continue;
	    		}
	    		else {
	    			pendingRefresh = 0;
	    		}
	    		
	    		//if(!deployStatusOld.equals(deployStatus)) {
	    		if(!deployStatus.contains(deployStatusOld)) {
	    			statusCount = 0;
	    			refresh = 0;
	    		}
	    		else {
	    			statusCount++;
	    			refresh++;
	    		}
	    		
	    		if(statusCount == 0) {
	    			printLogs("nodeDeployStatus = " + deployStatus);
	    			
	    			if(deployStatus.contains(msgInProgress)) {
	    				printLogs("Nodes deployment in progress: " + deployStatus);
		    			//printLogs("Any change in the status will be printed. Please wait...");
		    		}
		    		
	    			if(deployStatus.contains(msgCompleted)) {
	    				printLogs("Nodes deployment completed: " + deployStatus);
		    			break;
	    			}
	    			
	    			if(deployStatus.contains(msgError)) {
	    				printLogs("Nodes deployment errored: " + deployStatus);
		    			return false;
	    			}
	    			
		    		deployStatusOld = deployStatus;
	    		}
	    		
	    		// WORKAROUND: REFRESH THE PAGE EVERY 40 MINS, if same component is being deployed.
	    		if(refresh > 480 && statusCount != 0) {
	    			printLogs("WORKAROUND: Refreshing the page.");
	    			driver.navigate().refresh();
	    			sleep(10);
	    			refresh = 0;
	    			continue;
	    		}
	    		
	    		if(timeout > maxWaitTime) {
	    			// Refresh the page so that a page stuck like situation can be handled.
	    			printError("WARNING: Timeout occurred while waiting for Nodes deployment to complete. Exiting");
	    			failureFlag = true;
					break;
	    		}
	    		sleep(5);
	    		timeout++;
		    }
			stopWatch();
			
			if (failureFlag) {
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while waiting for Nodes deploy to complete.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Deploy node advance mode method
			public static boolean deployNodeAdvMode(String hostIp, String hostType) {
					
			printLogs("Calling deployNodeEasyMode");
			try
				{
				if(!selectActionDropDownOption("CssNodesActions", "Review/ Deploy")) {
					printError("Failed to click on Review/Deploy option under Action dropdown");
					printFunctionReturn(fn_fail);
					return false;
				}
				
				captureScreenShot();
				
				if(!waitForPage("Common_NewWindow")) {
					printError("Failed to open Deploy page");
					printFunctionReturn(fn_fail);
					return false;
				}
				
				if(fips_encKey){
					if(checkElementPresenceByXpath("NodesDeploy_FIPS_EncryptionKey_textBox")){
						if(!clickByXpath("NodesDeploy_Easy_Adv_Switch_Toggle")) {
							printError("Failed to click on NodesDeploy_Easy_Adv_Switch_Toggle");
							captureScreenShot();
							appendHtmlComment("Failed to click NodesDeploy_Easy_Adv_Switch_Toggle");
							printFunctionReturn(fn_fail);
							return false;
						} 
						if(!printTableContents("NodesDeploy_BlLibTableForRemoteNodes", 3, true, hostIp)) {
							printError("Failed to print the table contents");
						}
						
						if(!sendKeysByXpath("NodesDeploy_FIPS_EncryptionKey_textBox", "12345")){
							printError("Failed to enter the FIPS encryption keys");
						}
						if(!verifyButtonStatus("NodesDeploy_DeployButton", "disabled")){
							printError("Deploy button is enabled with lessthan 8 keys for fips encryption key");
							printFunctionReturn(fn_fail);
							return false;
						}
						printLogs("deploy button is not enabled as encryption key is lessthan 8 digits.");
						return true;
						
					}else{
						printError("Run the test with FIPS mode on");
						appendHtmlComment("Skipping the test and run the test with FIPS mode ON");
						return false;
					}
				}
				//If TPM is enabled then clicking on ignore warnings check box
				//Author: Praveen Dated: 06/13/2016
				//Quix ID: QXCR1001490016(Fixed)
				if(checkElementPresenceByXpath("NodesDeploy_IgnoreWarningCheckbox")){
					if(getTextByXpath("NodesDeploy_TPMWarning").contains(OR.getProperty("MessageNodesDeployTpmWarning"))){
						printLogs("Node is enabled with TPM, so checking the Ignore warnings check box to proceed with deploymnet");
						if(!clickByXpath("NodesDeploy_IgnoreWarningCheckbox")){
							printError("Failed to click on NodesDeploy_IgnoreWarningCheckbox");
							captureScreenShot();
							appendHtmlComment("Failed to click on NodesDeploy_IgnoreWarningCheckbox");
							printFunctionReturn(fn_fail);
							return false;
						}
					}
				}
				
				//Checking the FIPS mode ON message when perofiming OA deployment.					
				if(getTextByXpath("NodesDeploy_FipsOnOa").toUpperCase().contains("FIPS")){
					printLogs("FIPS mode is ON message for OA downgrade/rewrite: " + getTextByXpath("NodesDeploy_FipsOnOa"));
					captureScreenShot();
					if(fipsON){						
						if(!clickByXpath("NodesDeploy_Easy_Adv_Switch_Toggle")) {
							printError("Failed to click on NodesDeploy_Easy_Adv_Switch_Toggle");
							captureScreenShot();
							appendHtmlComment("Failed to click NodesDeploy_Easy_Adv_Switch_Toggle");
							printFunctionReturn(fn_fail);
							return false;
						} 
						if(!printTableContents("NodesDeploy_BlLibTableForRemoteNodes", 3, true, hostIp)) {
							printError("Failed to print the table contents");
						}
						
						if(!verifyButtonStatus("NodesDeploy_DeployButton", "enabled")) {
							printLogs("Deploy button is disabled as expected.");
						}else{
							printError("Deploy button is enabled. So test is failed");
							appendHtmlComment("In FIPS mode ON, OA downgrade/rewrite is not supported but deploy button is enabled.");
							printFunctionReturn(fn_fail);
							return false;
						}
						return true;
					}else{
						printError("Try the deployment with fips mode OFF or use the different OA where FIPS is Mode is OFF. "
								+ "Run the test again.");					
						appendHtmlComment("Try the deployment with fips mode OFF or use the different OA where FIPS is Mode is OFF. "
								+ "Run the test again.");
						printFunctionReturn(fn_fail);
						return false;
						}
					
				}else{
					if(fipsON){
						printLogs("Testcase is to run for FIPS mode ON on OA or VC");
						appendHtmlComment("Skipping the test case and run the test again with FIPS mode On");
						printFunctionReturn(fn_fail);
						return false;
					}
				}
								
				
				// Click on On/Off toggle for Enable Advanced Mode for more options 				
				 if(!clickByXpath("NodesDeploy_Easy_Adv_Switch_Toggle")) {
					printError("Failed to click on NodesDeploy_Easy_Adv_Switch_Toggle");
					captureScreenShot();
					appendHtmlComment("Failed to click NodesDeploy_Easy_Adv_Switch_Toggle");
					printFunctionReturn(fn_fail);
					return false;
				} 
				sleep(2);
				captureScreenShot();
				
				// Click on proceed warning
				// QXCR1001458495: HPSUM-Test
				// QXCR1001440150: HPSUM-Dev
				// Usability change: Proceed pop-up does not come up if there is no change done to easy mode.
				// Hence commenting out this code.
				/* if(!clickByXpath("NodesDeploy_Proceed_Warning_Button")) {
					printError("Failed to click on NodesDeploy_Proceed_Warning_Button");
					captureScreenShot();
					appendHtmlComment("Failed to click NodesDeploy_Proceed_Warning_Button");
					printFunctionReturn(fn_fail);
					return false;
				}
				sleep(2);
				captureScreenShot(); */
				/*Author: Praveen
				*Date: 05/23/2016
				* Added to handle the vc deployment by providing Encryption Key
				*/
				//looking for the Encryption Key in the deploy page
				if(hostType.toLowerCase().contains("vc") && checkElementPresenceByXpath(OR.getProperty("NodesDeploy_FIPS_EncryptionKey_textBox"))){
					String enckey =  CONFIG.getProperty("remoteHostVCEncKey");
					printLogs("VC FIPS Encryption : " + enckey);
					if(!sendKeysByXpath("NodesDeploy_FIPS_EncryptionKey_textBox", enckey )) {
						printError("sendKeysByXpath NodesDeploy_FIPS_EncryptionKey_textBox failed.");
					}
				}
				// Print the Baseline Library table contents
				if(!printTableContents("NodesDeploy_BlLibTableForRemoteNodes", 3, true, hostIp)) {
					printError("Failed to print the table contents");
				}
				
				if(!verifyButtonStatus("NodesDeploy_CancelButton", "enabled")) {
					printError("Error: Failed to verify Cancel Button.");
				}
							
				 /* 
				  * 4. Click Deploy button - But first start the autoIT script if it is windows and running on localhost
				  * 		- Verify Deploy window closes
				 */
				 
				// Run the AutoIT script to close the popups which come for unauthorized components.
				// Uncomment this with new/unreleased SPPs.
				if (hostIp.toLowerCase().contains("localhost")) {
					if (currentOsName.contains("windows")) {
						if(!executeScript("autoItDeployPopUpScript", false)) {
							printError("Error: Failed to run autoIt script.");
						}
					}
				}
				 
				captureScreenShot();
				
				if(!resolveFailedDependencyAndDeploy("NodesDeploy_BlLibTableForRemoteNodes", "NodesDeploy_DeployButton", hostIp)) {
					if(fips_faildep){
						printLogs("vc rollback option is showing the failed dependcy");
						return true;
					}else{
						printError("Failed to resolve the dependencies.");
						printFunctionReturn(fn_fail);
						return false;
					}
				}
				
				captureScreenShot();
				if(!wait_deploy){
				if(!WaitForNodesDeployToComplete()) {
					appendHtmlComment("Failed to complete deployment");
					printFunctionReturn(fn_fail);
					return false;
				}}
				
				captureScreenShot();
				// Kill the autoItDeployscript
				//if (hostIp.contains("localhost")) {
				if (hostIp.toLowerCase().contains("localhost")) {
					if (currentOsName.contains("windows")) {
						if(!executeScript("killAutoItDeployPopUpScript", false)) {
							printError("Error: Failed to kill autoIt script.");
						}
					}
				}
				
				}
			catch(Throwable t) {
			
				ErrorUtil.addVerificationFailure(t);			
				printError("Exception occurred while doing the node deploy.");
				printFunctionReturn(fn_fail);
				return false;
			}
			printFunctionReturn(fn_pass);
			return true;
			}
	//Installtion options drop down select
			
	public static boolean Installation_options_for_Deploy(String InstallOption){
		String xpathInital = "//a[starts-with(text(),'";
		String xpathValue = xpathInital + InstallOption + "')]";
		
		/*Installtion options:
		 *Value - Description
		 *upgradeBoth - Upgrade Firmware and Software
		upgradeFirmware - upgrade Firmware - 
		upgradeSoftware - Upgrade Software
		firmware - Downgrade & Rewrite Firmware
		software - Downgrade & Rewrite Software
		both - Downgrade & Rewrite Both
		downgradeFirmware - Downgrade Firmware
		downgradeSoftware - Downgrade Software
		downgradeBoth - Downgrade Both
		rewriteFirmware - Rewrite Firmware
		rewriteSoftware - Rewrite Software
		rewriteBoth - Rewrite Both
		 */
		printLogs("Calling Installation_options_for_Deploy");
		//WebElement descriptionInput = driver.findElement(By.xpath(OR.getProperty("Nodes_AddNodeDescriptionInput")));
		//String NodesDeploy_Install_option_dropdown = OR.getProperty("NodesDeploy_Install_option_dropdown");
		//String NodesDeploy_Install_option_dropdownArrow = OR.getProperty("NodesDeploy_Install_option_dropdownArrow");
		try {
			//Clicking installation option in the deploy page
			if(!clickByXpath("NodesDeploy_installation_option_selection")){
				printFunctionReturn(fn_fail);				
				return false;
			}
			
			//Clicking the installtion option dropdown arrow
			if(!clickByXpath("NodesDeploy_Install_option_dropdownArrow")) {
				printFunctionReturn(fn_fail);				
				return false;
			}
			
			//WebElement InstallOptionDropdown = driver.findElement(By.xpath(NodesDeploy_Install_option_dropdown));
			//Select InstallOptionDropdown =new Select(driver.findElement(By.id("hpsum-installation-option")));
			printLogs("Selecting " + InstallOption + " installation option on the deploy page.");
			//InstallOptionDropdown.sendKeys(InstallOption+"\n");
			if(InstallOption =="Upgrade Firmware"){				 
				 driver.findElement(By.xpath("html/body/ul[4]/li[2]/a")).click();				 
			}
			else{ 
			 driver.findElement(By.xpath(xpathValue)).click();
			}
			 printLogs("Selected " + InstallOption + " from the Install Option Drop down");			
			captureScreenShot();
			// 4. Wait for analysis to complete
			if(!waitForElementByXpath("NodesDeploy_window",60,true)) {
				printFunctionReturn(fn_fail);
				//return false;
			}
			captureScreenShot();		
		} catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while selecting installtion option on the deploy page.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;	
	}
			
	//Deploy node in advacned mode by selecting the installation options.
public static boolean deployNodeAdvMode_installation_Options(String hostIp, String hostType, String Install_options) {
	
	printLogs("Calling deployNodeAdvMode_installation_Options");
	try
		{
		if(!selectActionDropDownOption("CssNodesActions", "Review/ Deploy")) {
			printError("Failed to click on Review/Deploy option under Action dropdown");
			printFunctionReturn(fn_fail);
			return false;
		}
		
		captureScreenShot();
		
		if(!waitForPage("Common_NewWindow")) {
			printError("Failed to open Deploy page");
			printFunctionReturn(fn_fail);
			return false;
		}
		
		// Click on On/Off toggle for Enable Advanced Mode for more options 				
		 if(!clickByXpath("NodesDeploy_Easy_Adv_Switch_Toggle")) {
			printError("Failed to click on NodesDeploy_Easy_Adv_Switch_Toggle");
			captureScreenShot();
			appendHtmlComment("Failed to click NodesDeploy_Easy_Adv_Switch_Toggle");
			printFunctionReturn(fn_fail);
			return false;
		} 
		sleep(2);
		captureScreenShot();
		
		// Click on proceed warning
		// QXCR1001458495: HPSUM-Test
		// QXCR1001440150: HPSUM-Dev
		// Usability change: Proceed pop-up does not come up if there is no change done to easy mode.
		// Hence commenting out this code.
		/* if(!clickByXpath("NodesDeploy_Proceed_Warning_Button")) {
			printError("Failed to click on NodesDeploy_Proceed_Warning_Button");
			captureScreenShot();
			appendHtmlComment("Failed to click NodesDeploy_Proceed_Warning_Button");
			printFunctionReturn(fn_fail);
			return false;
		}
		sleep(2);
		captureScreenShot(); */
		
		// Selectiong the installtion options
		if(!Installation_options_for_Deploy(Install_options)){
			printError("Failed to select the installtion options");
			return false;
		}		
		// Print the Baseline Library table contents
		if(!printTableContents("NodesDeploy_BlLibTableForRemoteNodes", 3, false, hostIp)) {
			printError("Failed to print the table contents");
		}
		
		if(!verifyButtonStatus("NodesDeploy_CancelButton", "enabled")) {
			printError("Error: Failed to verify Cancel Button.");
		}
					
		 /* 
		  * 4. Click Deploy button - But first start the autoIT script if it is windows and running on localhost
		  * 		- Verify Deploy window closes
		 */
		 
		// Run the AutoIT script to close the popups which come for unauthorized components.
		// Uncomment this with new/unreleased SPPs.
		if (hostIp.toLowerCase().contains("localhost")) {
			if (currentOsName.contains("windows")) {
				if(!executeScript("autoItDeployPopUpScript", false)) {
					printError("Error: Failed to run autoIt script.");
				}
			}
		}
		 
		captureScreenShot();
		
		if(!resolveFailedDependencyAndDeploy("NodesDeploy_BlLibTableForRemoteNodes", "NodesDeploy_DeployButton", hostIp)) {
			if(fips_faildep){
				printLogs("unable to resolve failed dependency as roll back is not supported.");
				return true;
			}else{
				printError("Failed to resolve the dependencies.");
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		
		captureScreenShot();
		if(!wait_deploy){
			if(!WaitForNodesDeployToComplete()) {
				appendHtmlComment("Failed to complete deployment");
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		
		captureScreenShot();
		// Kill the autoItDeployscript
		//if (hostIp.contains("localhost")) {
		if (hostIp.toLowerCase().contains("localhost")) {
			if (currentOsName.contains("windows")) {
				if(!executeScript("killAutoItDeployPopUpScript", false)) {
					printError("Error: Failed to kill autoIt script.");
				}
			}
		}
		}
	catch(Throwable t) {
	
		ErrorUtil.addVerificationFailure(t);			
		printError("Exception occurred while doing the deployNodeAdvMode_installation_Options.");
		printFunctionReturn(fn_fail);
		return false;
	}
	printFunctionReturn(fn_pass);
	return true;
	}
	// Deploy node easy mode method
		public static boolean deployNodeEasyMode(String hostIp, String hostType) {
				
		printLogs("Calling deployNodeEasyMode");
		
		try {
			// 1. Click the Deploy link. (or Actions->Deploy)
			if(!selectActionDropDownOption("CssNodesActions", "Review/ Deploy")) {
				printError("Failed to click on Review/Deploy option under Action dropdown");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			captureScreenShot();
			
			if(!waitForPage("Common_NewWindow")) {
				printError("Failed to open Deploy page");
				printFunctionReturn(fn_fail);
				return false;
			}
			
			
			/* 2. On Deploy Screen, verify
			 * 		- Name
			 * 		- Buttons present
			 */
			
			// WORKAROUND:: As selenium is NOT able to getText from Reboot Options 
			// So we are collapsing the default opened options - Associated Node Details and Baseline Library
			//if(!clickByXpath("NodesDeploy_AssoNodeDetailsCollapse")) {
			//printError("Error: Failed to collapse AssoNodeDetails");
			//}
			//if(hostType.contains("NodeType_fc_switch")){
			//if(!verifyPage("NodesDeploy_Switch")){
			//printError("Error: Failed to verify deploy page.");
			//}
			//}
			//else{	
			//if(!verifyPage("NodesDeploy")) {
			//printError("Error: Failed to verify deploy page.");
			//}
			//}
			
			// Click on Downgrade Checkbox
			if(!clickByXpath("NodesDeploy_EasyMode_Downgrade_Checkbox")) {
				printError("Failed to click on NodesDeploy_EasyMode_Downgrade_Checkbox");
				captureScreenShot();
				appendHtmlComment("Failed to click Easy Mode Enable Downgrade checkbox");
				printFunctionReturn(fn_fail);
				return false;
			}
			sleep(2);
			captureScreenShot();
			// Click on Re-write Checkbox
			if(!clickByXpath("NodesDeploy_EasyMode_Rewrite_Checkbox")) {
				printError("Failed to click on NodesDeploy_EasyMode_Rewrite_Checkbox");
				captureScreenShot();
				appendHtmlComment("Failed to click Easy Mode Enable Re-write checkbox");
				printFunctionReturn(fn_fail);
				return false;
			}
			sleep(2);
			captureScreenShot();
			 if (hostType.equalsIgnoreCase("vm")){
				String NodesDeploy_EasyMode_Rewrite_Checkbox_Warning_VM = OR.getProperty("MessageDeployEasymodeVmwareWarning"); 
				if(!verifyText("NodesDeploy_EasyMode_Rewrite_Checkbox_Warning_VM", NodesDeploy_EasyMode_Rewrite_Checkbox_Warning_VM)) {
				printError("Failed to verify warning message for VMware component re-write");
				captureScreenShot();
				appendHtmlComment("Failed to verify warning message for VMware component re-write");
				printFunctionReturn(fn_fail);
				return false;
			}
			 }
			
			// Click on Skip Components with Dependency Failures Checkbox if enabled
			if(verifyCheckboxStatus("NodesDeploy_EasyMode_SkipDependency_Checkbox", "enabled")){
				if(!clickByXpath("NodesDeploy_EasyMode_SkipDependency_Checkbox")) {
				printError("Failed to click on NodesDeploy_EasyMode_SkipDependency_Checkbox");
				captureScreenShot();
				appendHtmlComment("Failed to click Skip Components with Dependency Failures checkbox");
				printFunctionReturn(fn_fail);
				return false;
				}
			}
			else printError("Failed to click on Skip Components with Dependency Failures checkbox as it is disabled.");
			sleep(2);	
			captureScreenShot();
//			// Print the Baseline Library table contents
//			if(!printTableContents("NodesDeploy_BlLibTableForRemoteNodes", 3, true, hostIp)) {
//				printError("Failed to print the table contents");
//			}
//			
			if(!verifyButtonStatus("NodesDeploy_CancelButton", "enabled")) {
				printError("Error: Failed to verify Cancel Button.");
			}
			
		 /* 
			  * 4. Click Deploy button - But first start the autoIT script if it is windows and running on localhost
			  * 		- Verify Deploy window closes
			 */
			 
			// Run the AutoIT script to close the popups which come for unauthorized components.
			// Uncomment this with new/unreleased SPPs.
			if (hostIp.toLowerCase().contains("localhost")) {
				if (currentOsName.contains("windows")) {
					if(!executeScript("autoItDeployPopUpScript", false)) {
						printError("Error: Failed to run autoIt script.");
					}
				}
			}
		 
			captureScreenShot();
			// Click on the Deploy button
			if(!clickByXpath("NodesDeploy_DeployButton")) {
					captureScreenShot();
					throw new Exception();
					}
					printLogs("Clicked Deploy button.");
					sleep(30);
					//if page is hanging we will refresh the page as a work around.
					pageRefresh();
					// Wait for Deploy window to disappear
					if(waitForNoElementByXpath("Common_NewWindow")) {
						printLogs("Deploy started.");
					}
					 
//			if(!resolveFailedDependencyAndDeploy("NodesDeploy_BlLibTableForRemoteNodes", "NodesDeploy_DeployButton", hostIp)) {
//				printError("Failed to resolve the dependencies.");
//				printFunctionReturn(fn_fail);
//				return false;
//			}
			
			/*if(!clickByXpath("NodesDeploy_DeployButton")) {
			printError("Error: Failed to click Deploy Button.");
			}
			sleep(10);
			captureScreenShot();
			
			// Wait for Deploy window to disappear
			if(waitForNoElementByXpath("Common_NewWindow")) {
				printLogs("Deploy started.");
			}*/
			
			captureScreenShot();
			
			/* int temp_count = 1;
			while (temp_count <= 3) {
				if (temp_count == 2) {
					printLogs("Retry:: clicking Deploy button.");
				}
				if (temp_count == 3) {
					printLogs("Closing the Deploy popup screen using the Cancel button.");
					if(!clickByXpath("NodesDeploy_CancelButton")) {
						printError("Error: Failed to click Deploy Cancel Button.");
					}
					break;
				}
				
				if(!clickByXpath("NodesDeploy_DeployButton")) {
					printError("Error: Failed to click Deploy Button.");
				}
				
				// Wait for Deploy window to disappear
				if(!waitForNoElementByXpath("Common_NewWindow")) {
					printError("Deploy popup screen not closing after pressing deploy button.");
					
					captureScreenShot();
				}
				else {
					break;
				}
				temp_count++;
			}*/
			
			// Verify that deploy has started. If yes, then continue to wait for deploy to finish
		// else, return false saying - deploy button click has failed.
			// PENDING

			// 5. Wait for Deployment to complete - Message: Install Done
			
			if(!wait_deploy){
				if(!WaitForNodesDeployToComplete()) {			
				appendHtmlComment("Failed to complete deployment");
				printFunctionReturn(fn_fail);
				return false;
				}
			}
		
			captureScreenShot();

		// Kill the autoItDeployscript
			//if (hostIp.contains("localhost")) {
			if (hostIp.toLowerCase().contains("localhost")) {
				if (currentOsName.contains("windows")) {
					if(!executeScript("killAutoItDeployPopUpScript", false)) {
					printError("Error: Failed to kill autoIt script.");
					}
				}
			}
			
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while doing the node deploy.");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	// selectFormAssignToGroupDropDown
	// Select an option from Asign to Group drop down list
	public static boolean selectFormAssignToGroupDropDown(String option){
		printLogs("Calling selectFormAssignToGroupDropDown with values: " + option);
			
		try{
			// Click on Assign to drop down
			if(!SelUtil.clickByXpath("Nodes_AddNodeAssignToGroupDropDown")){
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Click on the group name.
			driver.findElement(By.partialLinkText(option)).click();
			
			captureScreenShot();
		}
		catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while selecting an option from drop down.");
			printFunctionReturn(fn_fail);
			return false;	
		}
		printFunctionReturn(fn_pass);	
		return true;
	}
	

	// Methods related to screen : Nodes Reports 
	
	
	//Method to verify if start over or reset button has worked properly.
	public static boolean verify_Start_Over(){
		printLogs("Calling verify_Start_Over");
		String Nodes_AddNodeIPDNSInput = OR.getProperty("Nodes_AddNodeIPDNSInput");
		String Nodes_AddNodeDescriptionInput = OR.getProperty("Nodes_AddNodeDescriptionInput");
		String Nodes_AddNodeTypeDropDown = OR.getProperty("Nodes_AddNodeTypeDropDown");
		String Nodes_AddNodeBlInput = OR.getProperty("Nodes_AddNodeBlInput");
		String Nodes_AddNodeAddPkgSearchInput = OR.getProperty("Nodes_AddNodeAddPkgSearchInput");
		String Nodes_AddNodeCredentialsUsernameInput = OR.getProperty("Nodes_AddNodeCredentialsUsernameInput");
		String Nodes_AddNodeCredentialsPasswordInput = OR.getProperty("Nodes_AddNodeCredentialsPasswordInput");
		
		try{
			//check IP/DNS field is empty.
			if(!verifyText(Nodes_AddNodeIPDNSInput,"")) {
				printError("IP/DNS field is not empty after clicking start over on add node page.");
				captureScreenShot();
			}
			
			//check Description field is empty.
			if(!verifyText(Nodes_AddNodeDescriptionInput, "")) {
				printError("Description field is not empty after clicking start over on add node page.");
				captureScreenShot();
			}
			
			//check Type of node to add is reset to default value: "Select Type"
			//String nodeTypeDropdown = driver.findElement(By.xpath(OR.getProperty("Nodes_AddNodeTypeDropDown"))).getText();
			if(!verifyText(Nodes_AddNodeTypeDropDown, "Select Type")) {
				printError("Type of node to add is not reset to default value: Select Type after clicking start over on add node page.");
				captureScreenShot();
			}
			
			//check Baseline input field is empty.Nodes_AddNodeBlInput
			if(!verifyText(Nodes_AddNodeBlInput,"")) {
				printError("Baseline field is not empty after clicking start over on add node page.");
				captureScreenShot();
			}
						
			//check additional Baseline input field is empty.
			if(!verifyText(Nodes_AddNodeAddPkgSearchInput, "")) {
				printError("Additional Baseline field is not empty after clicking start over on add node page.");
				captureScreenShot();
			}
				
			//check Username is empty Nodes_AddNodeCredentialsUsernameInput
			if(!verifyText(Nodes_AddNodeCredentialsUsernameInput,"")) {
				printError("Username field is not empty after clicking start over on add node page.");
				captureScreenShot();
			}
			//check Password is empty Nodes_AddNodeCredentialsPasswordInput
			if(!verifyText(Nodes_AddNodeCredentialsPasswordInput,"")) {
				printError("Password field is not empty after clicking start over on add node page.");
				captureScreenShot();
			}
		}	
		catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while verifying fields after start over in add node page.");
			printFunctionReturn(fn_fail);
			return false;	
		}
		printFunctionReturn(fn_pass);	
		return true;
	}
	public static boolean deleteNode(String remoteHostIp) {
		printLogs("Calling deleteNode to delete "+ remoteHostIp );
		
		try{
			
			// Go to Add Node Page
			if(!guiSetPage("Nodes")) {
				throw new TestException("Test Failed");
			}
			
			// Select the node from nodes page.
			if(!Nodes.findNodeOnLeftPane(remoteHostIp)){
				throw new TestException("Test Failed");
			}
			
			int nodesLeftPaneTableRowCount = 0;
			String xPathLeftPaneNode = "";
			String nodeNameLeftPane = "";
			nodesLeftPaneTableRowCount = SelUtil.getTableRowCount("Nodes_LeftPaneTable");
			
			for(int i = 1; i <= nodesLeftPaneTableRowCount; i++){            	
				if(nodesLeftPaneTableRowCount == 1) {
            		xPathLeftPaneNode = OR.getProperty("Nodes_LeftPaneTable") + "/tr/td[2]"; 
            	}
            	else{
            		xPathLeftPaneNode = OR.getProperty("Nodes_LeftPaneTable") + "/tr[" + i + "]/td[2]";
            	}
            	
            	nodeNameLeftPane = SelUtil.getTextByActualXpath(xPathLeftPaneNode);
            	if(nodeNameLeftPane.contentEquals(remoteHostIp))
            		{
            			if(!SelUtil.clickByActualXpath(xPathLeftPaneNode)){
        				captureScreenShot();
        				printFunctionReturn(fn_fail);
        				return false;
        			}
            	}
			}
			
			printLogs("Clicked the remote host IP: " +  remoteHostIp);
			captureScreenShot();
			
			// Click on delete from actions menu in nodes page.
			sleep(5);
			if(!selectActionDropDownOption("CssNodesActions", "Delete")) {
				captureScreenShot();
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Click on cancel button.
			if(!SelUtil.clickByXpath("Nodes_ActionDeleteCancelButton")){
				captureScreenShot();
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Click on delete from actions menu in nodes page.
			sleep(5);
			if(!selectActionDropDownOption("CssNodesActions", "Delete")) {
				captureScreenShot();
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Confirm delete.
			if(!SelUtil.clickByXpath("Nodes_ActionDeleteYesDeleteButton")){
				captureScreenShot();
				printFunctionReturn(fn_fail);
				return false;
			}
			
		}
		catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while deleting node.");
			printFunctionReturn(fn_fail);
			return false;	
		}
		printFunctionReturn(fn_pass);	
		return true;
	}
}
