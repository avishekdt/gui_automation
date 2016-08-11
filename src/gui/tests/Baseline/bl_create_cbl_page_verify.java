package gui.tests.Baseline;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.SelUtil;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;
import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

// Assertion: 
// Verify all the elements on the Add Baseline screen and check the
// functionality of Close button.
// 

public class bl_create_cbl_page_verify extends TestSuiteBase {
	String test_name = this.getClass().getSimpleName();
	
	static boolean skip = false;
	static boolean fail = false;
	String test_result = "PASS";
	
	long initialTime;
	long finalTime;
	String executionTime = "";
	
	@Test
	public void test_bl_create_cbl_page_verify() {
		screenshot_name = test_name;
		screenshot_counter = 0;
		
		try {
			//
			// Environment:
			//
			printTestStartLine(test_name);
			
			// Get the test start time.
			initialTime = getTimeInMsec();

			// Creating email template for the test.
			if(!testInitialize(test_name)) {
				printError("Failed to perform test initialize.");
			}
			
			printLogs("Checking Runmode of the test.");
			if(!TestUtil.isTestCaseRunnable(suite_xl, test_name)) {
				printLogs("WARNING:: Runmode of the test '" + test_name + "' set to 'N'. So Skipping the test.");
				skip = true;
				throw new SkipException("WARNING:: Runmode of the test '" + test_name + "' set to 'N'. So Skipping the test.");
			}
			printLogs("RunMode found as Y. Starting the test.");
			
			//
			// Setup:
			//
			// 1. Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
			// 2. Wait for all the Baseline inventory to complete.
			if(!BaselineLibrary.performBaselineInventory()){
				throw new TestException("Test failed");
			}
			
			// 3. Go to Create Custom Baseline Page
			if(!guiSetPage("CreateCustomBaseline")) {
				throw new TestException("Test Failed");
			}
			
			// 
			// Execution::
			//
			// Verify all the screen elements.
			// 
			
			// *********************************************************************
			// Elements:
			//	Heading - Create Custom Baseline
			//	Button	- Help
			
			//	Heading	- Overview
			//	Label	- Description *
			//	TextBox	- Description
			//	Label	- Version *
			//	Field	- Version-Calendar
			//	TextBox	- Version
			//	Label	- Baseline Name
			//	Label	- bp-(DATE)-  Example:bp-2015-03-17-
			//	Label	- Output Location *
			//	TextBox	- Output Location
			//	Button	- Browse - Enabled
			//	Label	- Make Bootable ISO file
			//	CheckB	- Bootable
			//	Label	- Run in background
			//	CheckB	- Background
			
			//	Heading	- Step 1 - Baseline Sources
			//	Table	- Baselines (//*[@id='baseline-sources-table_wrapper'])
			//	TableHeading	??
			
			//	Heading	- Step 2 - Filters
			//	Label	- Match CloudSystem Matrix Version
			//	CheckB	- CSM
			//	Label	- Component Type
			//	DropD	- Comp Type			
			//	Label	- Critical Updates
			//	CheckB	- Select Critical
			//	Label	- Recommended Updates
			//	CheckB	- Select Recommended
			//	Label	- Optional Updates
			//	CheckB	- Select Optional
			
			// Check the checkbox 'CheckB	- CSM' and verify the fields
			//	Label	- CloudSystem Matrix Definition XML File
			//	DropD	- CSM definition XML file
			//	Label	- Enter working directory
			//	TextBox	- Working dir
			//	Button	- Browse
			
			// Select the 'hp.com' value from DropDown 'CloudSystem Matrix Definition XML File'
			//	Label	- Enter working directory
			//	TextBox	- Working dir
			//	Button	- Browse
			//	Label	- Proxy Options
			//	DropD	- Proxy Options
			//	Button	- Retrieve File
			
			// Change DropDown value to select 2nd option: Proxy Server and verify the fields
			//	Label 	- Address
			//	TextBox	- Address
			//	Label 	- Port
			//	TextBox	- Port
			//	Label 	- Username
			//	TextBox	- Username
			//	Label 	- Password
			//	TextBox	- Password
			
			// Change DropDown value to select 3rd option: Proxy Script and verify the fields
			//	Label 	- Script
			//	TextBox	- Script
			//	Label 	- Username
			//	TextBox	- Username
			//	Label 	- Password
			//	TextBox	- Password
			
			// Uncheck the checkbox after verification.
			
			//	Label	- Advanced Filters
			//	Label	- OS list under OS Type filter and Server list under Server Model filter are populated dynamically from the selected baseline(s). 
			//	Label	- Architecture
			//	CheckB	- x86
			//	Label	- x86
			//	CheckB	- x64
			//	Label	- x64
			//	Label	- Operating System
			//	CheckB	- RHEL 6
			//	CheckB	- RHEL 7
			//	CheckB	- SLES 11
			//	CheckB	- SLES 12
			//	CheckB	- VMware ESXi 5.0
			//	CheckB	- VMware vSphere 5.1
			//	CheckB	- VMware vSphere 5.5
			//	CheckB	- VMware vSphere 6.0
			//	CheckB	- Windows 2008
			//	CheckB	- Windows 2008 R2
			//	CheckB	- Windows 2012
			//	CheckB	- Windows 2012 R2			
			//	Label	- Enclosure and Individual Device Types [EARLIER- Enclosure and Non-Server devices]
			//	CheckB	- All Thunderbird Enclosure Components
			//	CheckB	- C7000 Enclosure Components
			//	CheckB	- OA
			//	CheckB	- Virtual Connect (VC)
			//	CheckB	- iLO
			//	CheckB	- SAS BL switch Interconnects
			//	CheckB	- Individual Device Types
			//	CheckB	- FC HBA/CNA
			//	CheckB	- NIC
			//	CheckB	- System ROM
			//	CheckB	- Tape
			//	CheckB	- Array Controller
			//	CheckB	- Hard Drive
			//	CheckB	- Fibre Channel Switches			
			//	Label	- Server Model
			//	Label	- HP ProLiant DL Series
			//	CheckB	- Select all
			//	CheckB	- Deselect all
			//	Label	- HP ProLiant ML Series
			//	CheckB	- Select all
			//	CheckB	- Deselect all
			//	Label	- HP ProLiant BL Series
			//	CheckB	- Select all
			//	CheckB	- Deselect all
			//	Label	- HP ProLiant SL Series
			//	CheckB	- Select all
			//	CheckB	- Deselect all
			//	Label	- HP ProLiant XL Series
			
			//	Heading	- Step 3 - Review 
			//	Button	- Apply Filters
			//	Button	- Create ISO
			//	Button	- Save Baseline
			//	Button	- Reset
			//	Button	- Close
			
			// Press the Close button and Verify:
			//	Confirm Close Dialog box exists
			// 	Text1	- Warning: Your changes will be lost.
			// 	Text2	- Proceed to new page?
			//	Button	- Yes, Proceed
			//	Button	- Cancel
			// Press Cancel and control must be returned to the CBL page.
			// Press the Close again and Press 'Yes, Proceed' button on confirm close dialog box
			// Verify that Dialog box closes and control reaches BL screen.
			
			// *********************************************************************
			
			//	Heading - Create Custom Baseline
			//	Button	- Help
			SelUtil.verifyText("BL_CBL_Heading", "Create Custom Baseline");
			
			if (!SelUtil.checkElementPresenceByXpath("Common_NewWindowHelp")) {
				printError("Failed to verify Common_NewWindowHelp");
			}
			
			//   Click on the Help (?) button.
			if(!SelUtil.clickByXpath("Common_NewWindowHelp")) {
				printError("Failed to click on Help button.");
			}
			
			// Verify that the heading on the help page matches the expected text.
			if (!helpWindowHeadingCompare("Creating a custom baseline and ISO")) {
				printError("Create custom baseline help page heading not as expected.");
			}
			
			//	Heading	- Overview
			//	Label	- Description *
			//	TextBox	- Description
			//	Label	- Version *
			//	Field	- Version-Calendar
			//	TextBox	- Version
			//	Label	- Baseline Name
			//	Label	- bp-(DATE)-  Example:bp-2015-03-17-
			//	Label	- Output Location *
			//	TextBox	- Output Location
			//	Button	- Browse - Enabled
			//	Label	- Make Bootable ISO file
			//	CheckB	- Bootable
			//	Label	- Run in background
			//	CheckB	- Background
			SelUtil.verifyText("BL_CBL_SubHeading_Overview", "Overview");
			
			SelUtil.verifyText("BL_CBL_Label_Description", "Description *");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_TextBox_Description")) {
				printError("Failed to find BL_CBL_TextBox_Description");
			}
			
			SelUtil.verifyText("BL_CBL_Label_Version", "Version *");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_Calendar_Version")) {
				printError("Failed to find BL_CBL_Calendar_Version");
			}
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_TextBox_Version")) {
				printError("Failed to find BL_CBL_TextBox_Version");
			}
			
			SelUtil.verifyText("BL_CBL_Label_BlName", "Baseline Name");
			SelUtil.verifyText("BL_CBL_Label_BlId", "bp-");
			
			SelUtil.verifyText("BL_CBL_Label_OutLoc", "Output Location *");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_TextBox_OutLoc")) {
				printError("Failed to find BL_CBL_TextBox_OutLoc");
			}
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_Button_OutLocBrowse")) {
				printError("Failed to find BL_CBL_Button_OutLocBrowse");
			}
			
			SelUtil.verifyText("BL_CBL_Label_MakeBootableIso", "Make Bootable ISO file");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_CheckBox_MakeBootableIso")) {
				printError("Failed to find BL_CBL_CheckBox_MakeBootableIso");
			}
			
			SelUtil.verifyText("BL_CBL_Label_RunBackInBackground", "Run in background");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_CheckBox_RunBackInBackground")) {
				printError("Failed to find BL_CBL_CheckBox_RunBackInBackground");
			}
			
			//	Heading	- Step 1 - Baseline Sources
			//	Table	- Baselines (//*[@id='baseline-sources-table_wrapper'])
			//	TableHeading	??
			SelUtil.verifyText("BL_CBL_SubHeading_Step1", "Step 1 - Baseline Sources");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_Table_Baselines")) {
				printError("Failed to find BL_CBL_Table_Baselines");
			}
			
			//	Heading	- Step 2 - Filters
			//	Label	- Match CloudSystem Matrix Version
			//	CheckB	- CSM
			//	Label	- Component Type
			//	DropD	- Comp Type			
			//	Label	- Critical Updates
			//	CheckB	- Select Critical
			//	Label	- Recommended Updates
			//	CheckB	- Select Recommended
			//	Label	- Optional Updates
			//	CheckB	- Select Optional
			SelUtil.verifyText("BL_CBL_SubHeading_Step2", "Step 2 - Filters");
			
			SelUtil.verifyText("BL_CBL_Label_Csm", "Match CloudSystem Matrix Version");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_CheckBox_Csm")) {
				printError("Failed to find BL_CBL_CheckBox_Csm");
			}
			
			SelUtil.verifyText("BL_CBL_Label_CompType", "Component Type");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_DropDown_CompType")) {
				printError("Failed to find BL_CBL_DropDown_CompType");
			}
			
			SelUtil.verifyText("BL_CBL_Label_UpdateCritical", "Critical Updates");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_CheckBox_Critical")) {
				printError("Failed to find BL_CBL_CheckBox_Critical");
			}
			
			SelUtil.verifyText("BL_CBL_Label_UpdateRecom", "Recommended Updates");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_CheckBox_Recom")) {
				printError("Failed to find BL_CBL_CheckBox_Recom");
			}
			
			SelUtil.verifyText("BL_CBL_Label_UpdateOptional", "Optional Updates");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_CheckBox_Optional")) {
				printError("Failed to find BL_CBL_CheckBox_Optional");
			}
			
			// Check the checkbox 'Match CloudSystem Matrix Version' and verify the fields
			// Uncheck the checkbox after verification.
			SelUtil.clickByXpath("BL_CBL_CheckBox_Csm");
			printLogs("Checked the BL_CBL_CheckBox_Csm checkBox and now verifying the new screen elements.");
			
			//	Label	- CloudSystem Matrix Definition XML File
			//	DropD	- CSM definition XML file
			//	Label	- Select CloudSystem Matrix XML
			//	TextBox	- CloudSystem Matrix XML
			//	Button	- Browse			
			SelUtil.verifyText("BL_CBL_Label_CsmDefXmlFile", "CloudSystem Matrix Definition XML File");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_DropDown_CsmDefXmlFile")) {
				printError("Failed to find BL_CBL_DropDown_CsmDefXmlFile");
			}
			SelUtil.verifyText("BL_CBL_Label_CsmDefXml", "Select CloudSystem Matrix XML");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_TextBox_CsmDefXml")) {
				printError("Failed to find BL_CBL_TextBox_CsmDefXml");
			}
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_Button_BrowseCsmDefXml")) {
				printError("Failed to find BL_CBL_Button_BrowseCsmDefXml");
			}
			
			// Select the 'hp.com' value from DropDown 'CloudSystem Matrix Definition XML File'
			//	Label	- Enter working directory
			//	TextBox	- Working dir
			//	Button	- Browse
			//	Label	- Proxy Options
			//	DropD	- Proxy Options
			//	Button	- Retrieve File
			BaselineLibrary.createCblSelectCsmDefXmlFileDdOption(2);
			
			SelUtil.verifyText("BL_CBL_Label_WorkDir", "Enter working directory");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_TextBox_WorkDir")) {
				printError("Failed to find BL_CBL_TextBox_WorkDir");
			}
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_Button_BrowseWorkDir")) {
				printError("Failed to find BL_CBL_Button_BrowseWorkDir");
			}
			
			SelUtil.verifyText("BL_CBL_Label_ProxyOptions", "Proxy Options");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_DropDown_ProxyOptions")) {
				printError("Failed to find BL_CBL_DropDown_ProxyOptions");
			}
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_Button_RetrieveFile")) {
				printError("Failed to find BL_CBL_Button_RetrieveFile");
			}
			
			// Change DropDown value to select 2nd option: Proxy Server and verify the fields
			//	Label 	- Address
			//	TextBox	- Address
			//	Label 	- Port
			//	TextBox	- Port
			//	Label 	- Username
			//	TextBox	- Username
			//	Label 	- Password
			//	TextBox	- Password
			BaselineLibrary.createCblSelectProxyOptionsDdOption(2);
			
			SelUtil.verifyText("BL_CBL_Label_ProxyServerAddress", "Address");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_TextBox_ProxyServerAddress")) {
				printError("Failed to find BL_CBL_TextBox_ProxyServerAddress");
			}
			
			SelUtil.verifyText("BL_CBL_Label_ProxyServerPort", "Port");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_TextBox_ProxyServerPort")) {
				printError("Failed to find BL_CBL_TextBox_ProxyServerPort");
			}
			
			SelUtil.verifyText("BL_CBL_Label_ProxyServerUser", "Username");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_TextBox_ProxyServerUser")) {
				printError("Failed to find BL_CBL_TextBox_ProxyServerUser");
			}
			
			SelUtil.verifyText("BL_CBL_Label_ProxyServerPwd", "Password");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_TextBox_ProxyServerPwd")) {
				printError("Failed to find BL_CBL_TextBox_ProxyServerPwd");
			}
			
			// Change DropDown value to select 3rd option: Proxy Script and verify the fields
			//	Label 	- Script
			//	TextBox	- Script
			//	Label 	- Username
			//	TextBox	- Username
			//	Label 	- Password
			//	TextBox	- Password
			BaselineLibrary.createCblSelectProxyOptionsDdOption(3);
			
			SelUtil.verifyText("BL_CBL_Label_ProxyScriptScript", "Script");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_TextBox_ProxyScriptScript")) {
				printError("Failed to find BL_CBL_TextBox_ProxyScriptScript");
			}
			
			SelUtil.verifyText("BL_CBL_Label_ProxyScriptUser", "Username");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_TextBox_ProxyScriptUser")) {
				printError("Failed to find BL_CBL_TextBox_ProxyScriptUser");
			}
			
			SelUtil.verifyText("BL_CBL_Label_ProxyScriptPwd", "Password");
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_TextBox_ProxyScriptPwd")) {
				printError("Failed to find BL_CBL_TextBox_ProxyScriptPwd");
			}
			
			// Uncheck the checkbox after verification.
			SelUtil.clickByXpath("BL_CBL_CheckBox_Csm");
			printLogs("UN-Checked the BL_CBL_CheckBox_Csm checkBox and now continuing with the earlier screen elements.");
			
			//	Label	- Advanced Filters
			// Click Advanced Filters
			//	Label	- OS list under OS Type filter and Server list under Server Model filter are populated dynamically from the selected baseline(s). 
			//	Label	- Architecture
			//	CheckB	- x86
			//	Label	- x86
			//	CheckB	- x64
			//	Label	- x64
			SelUtil.verifyText("BL_CBL_Label_AdvFilters", "Advanced Filters");
			SelUtil.clickByXpath("BL_CBL_Label_AdvFilters");
			captureScreenShot();
			
			SelUtil.verifyText("BL_CBL_Label_AdvFiltersMsg", "OS list under OS Type filter and Server list under Server Model filter are populated dynamically from the selected baseline(s).");
			
			SelUtil.verifyText("BL_CBL_Label_Arch", "Architecture");
			SelUtil.clickByXpath("BL_CBL_Label_Arch");
			captureScreenShot();
			
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_CheckBox_x86")) {
				printError("Failed to find BL_CBL_CheckBox_x86");
			}
			SelUtil.verifyText("BL_CBL_Label_x86", "x86");
			
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_CheckBox_x64")) {
				printError("Failed to find BL_CBL_CheckBox_x64");
			}
			SelUtil.verifyText("BL_CBL_Label_x64", "x64");
			
			//	Label	- Operating System
			//	CheckB	- RHEL 6
			//	CheckB	- RHEL 7
			//	CheckB	- SLES 11
			//	CheckB	- SLES 12
			//	CheckB	- VMware ESXi 5.0
			//	CheckB	- VMware vSphere 5.1
			//	CheckB	- VMware vSphere 5.5
			//	CheckB	- VMware vSphere 6.0
			//	CheckB	- Windows 2008
			//	CheckB	- Windows 2008 R2
			//	CheckB	- Windows 2012
			//	CheckB	- Windows 2012 R2
			SelUtil.verifyText("BL_CBL_Label_OS", "Operating System");
			SelUtil.clickByXpath("BL_CBL_Label_OS");
			captureScreenShot();
			
			
			
			//	Label	- Enclosure and Individual Device Types [Earlier - Enclosure and Non-Server devices]
			//	CheckB	- All Thunderbird Enclosure Components
			//	CheckB	- C7000 Enclosure Components
			//	CheckB	- OA
			//	CheckB	- Virtual Connect (VC)
			//	CheckB	- iLO
			//	CheckB	- SAS BL switch Interconnects
			//	CheckB	- Individual Device Types
			//	CheckB	- FC HBA/CNA
			//	CheckB	- NIC
			//	CheckB	- System ROM
			//	CheckB	- Tape
			//	CheckB	- Array Controller
			//	CheckB	- Hard Drive
			//	CheckB	- Fibre Channel Switches
			SelUtil.verifyText("BL_CBL_Label_EncNonServerDevices", "Enclosure and Individual Device Types");
			SelUtil.clickByXpath("BL_CBL_Label_EncNonServerDevices");
			captureScreenShot();
			
			
			
			//	Label	- Server Model
			//	Label	- HP ProLiant DL Series
			//	CheckB	- Select all
			//	CheckB	- Deselect all
			//	Label	- HP ProLiant ML Series
			//	CheckB	- Select all
			//	CheckB	- Deselect all
			//	Label	- HP ProLiant BL Series
			//	CheckB	- Select all
			//	CheckB	- Deselect all
			//	Label	- HP ProLiant SL Series
			//	CheckB	- Select all
			//	CheckB	- Deselect all
			//	Label	- HP ProLiant XL Series
			SelUtil.verifyText("BL_CBL_Label_ServerModel", "Server Model");
			SelUtil.clickByXpath("BL_CBL_Label_ServerModel");
			captureScreenShot();
			
			
			
			//	Heading	- Step 3 - Review 
			//	Button	- Apply Filters
			//	Button	- Create ISO
			//	Button	- Save Baseline
			//	Button	- Reset
			//	Button	- Close
			SelUtil.verifyText("BL_CBL_SubHeading_Step3", "Step 3 - Review");
			
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_Button_ApplyFilters")) {
				printError("Failed to find BL_CBL_Button_ApplyFilters");
			}
			
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_Button_CreateIso")) {
				printError("Failed to find BL_CBL_Button_CreateIso");
			}
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_Button_SaveBaseline")) {
				printError("Failed to find BL_CBL_Button_SaveBaseline");
			}
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_Button_Reset")) {
				printError("Failed to find BL_CBL_Button_Reset");
			}
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_Button_Close")) {
				printError("Failed to find BL_CBL_Button_Close");
			}
			
			// Press the Close button and Verify:
			//	Confirm Close Dialog box exists
			// 	Text1	- Warning: Your changes will be lost.
			// 	Text2	- Proceed to new page?
			//	Button	- Yes, Proceed
			//	Button	- Cancel
			if (!SelUtil.clickByXpath("BL_CBL_Button_Close")) {
				printError("Failed to click BL_CBL_Button_Close");
			}
			captureScreenShot();
			
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_CloseConfirm_Window")) {
				printError("Failed to find BL_CBL_CloseConfirm_Window");
			}
			
			String expectedCloseConfirmText1 = "Warning: Your changes will be lost.";
			String expectedCloseConfirmText2 = "Proceed to new page?";
			
			String xPathBL_CBL_CloseConfirm_Text1 = OR.getProperty("BL_CBL_CloseConfirm_Text") + "[1]";
			String xPathBL_CBL_CloseConfirm_Text2 = OR.getProperty("BL_CBL_CloseConfirm_Text") + "[2]";
			
			String actualCloseConfirmText1 = SelUtil.getTextByActualXpath(xPathBL_CBL_CloseConfirm_Text1);
			String actualCloseConfirmText2 = SelUtil.getTextByActualXpath(xPathBL_CBL_CloseConfirm_Text2);
			
			if (!actualCloseConfirmText1.equalsIgnoreCase(expectedCloseConfirmText1)) {
				printError("Texts did not match. Expected: " + expectedCloseConfirmText1 + " Actual: " + actualCloseConfirmText1);
			}
			if (!actualCloseConfirmText2.equalsIgnoreCase(expectedCloseConfirmText2)) {
				printError("Texts did not match. Expected: " + expectedCloseConfirmText1 + " Actual: " + actualCloseConfirmText1);
			}
			
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_CloseConfirm_Yes")) {
				printError("Failed to find BL_CBL_CloseConfirm_Yes");
			}
			
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_CloseConfirm_Cancel")) {
				printError("Failed to find BL_CBL_CloseConfirm_Cancel");
			}
			
			// Press Cancel and control must be returned to the CBL page.
			// Press the Close again and Press 'Yes, Proceed' button on confirm close dialog box
			// Verify that Dialog box closes and control reaches BL screen.
			if (!SelUtil.clickByXpath("BL_CBL_CloseConfirm_Cancel")) {
				printError("Failed to click BL_CBL_CloseConfirm_Cancel");
			}
			captureScreenShot();
			SelUtil.checkElementPresenceByXpath("BaselineLibrary_CreateCustomBaselineHeading");
			printLogs("Success: control reached CBL page.");
			
			if (!SelUtil.clickByXpath("BL_CBL_Button_Close")) {
				printError("Failed to click BL_CBL_Button_Close");
			}
			captureScreenShot();
			
			//
			// Verification:
			//
			// The CBLwindow closes
			if (!SelUtil.checkElementPresenceByXpath("BL_CBL_CloseConfirm_Window")) {
				printError("Failed to find BL_CBL_CloseConfirm_Window");
			}
			captureScreenShot();
			if (!SelUtil.clickByXpath("BL_CBL_CloseConfirm_Yes")) {
				printError("Failed to click BL_CBL_CloseConfirm_Yes");
			}
			captureScreenShot();
			SelUtil.checkElementPresenceByXpath("BaselineLibrary_Heading");
			printLogs("Success: control reached BL page.");
			
			captureScreenShot();
			
			printLogs("DONE.");
		}
		catch (Throwable t) {
			if (skip) {
				test_result = "SKIP";
			}
			else {
				test_result = "FAIL";
				ErrorUtil.addVerificationFailure(t);
			}
		}
		finally {
			try {
				//
				// Cleanup:
				//
				printLogs("Test Cleanup:");
				
				// Get current time in ms.
				finalTime = getTimeInMsec();
				
				// Calculate test time interval.
				executionTime = calculateTimeInterval(initialTime, finalTime);
				
				// Write the final test result in html file.
				CommonHpsum.testCleanupAndReporting(test_name, test_result, executionTime);
				TestUtil.reportDataSetResult(suite_xl, "TestCases", TestUtil.getRowNum(suite_xl, test_name), test_result);
				printTestEndLine(test_name);
			}
			catch (Throwable t) {
				printError("Exception occurred in finally.");
			}
		}
	}
}
