package gui.tests.VMHosts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.NodeGroups;
import gui.common.base.Nodes;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

import org.testng.SkipException;
import org.testng.TestException;
import org.testng.annotations.Test;

// Assertion: 
// VM deploy and check log files
// 

public class vmhost_deploy_and_check_logs extends TestSuiteBase {
	String test_name 		= this.getClass().getSimpleName();
	String test_result 		= "PASS";
	static boolean manual 	= false;
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	long initialTime;
	long finalTime;
	String executionTime = "";
	
	String remoteHostIp_vm = "";
	String remoteHostUserName_vm = "";
	String remoteHostPassword_vm = "";
	String remoteHostDesc_vm = "";
	String remoteHostType_vm = "";
	String nodeGroupName = "";
	String nodeGroupDesc = "";
	ArrayList<String> nodesInGroup = new ArrayList<String>();
	Date date= new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("MMM-d-yyyy hh:mm:ss:SSS a");
    String formattedDate = "";
    String lastLineInScoutingLog = "";
    String currentLineInScoutingLog = "";
    String scoutingCompletedMsgInScoutingLog = "Scouting completed for ipAddress ";
    Boolean flag = false; 
    String timeInLog = "";
    long diff;
    long diffInMinutes;
    
	// Start the test.
	@Test
	public void test_vmhost_deploy_and_check_logs() {
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
			
			// THIS TEST IS MANUAL RIGHT NOW. RETURN MANUAL NOW.
			if(manual) {
				printLogs("Set up is not available. Let the test be manual.");
				throw new Exception("INFO:: Manual Test");
			}
			
			//
			// Setup:
			//
			remoteHostIp_vm = CONFIG.getProperty("remoteHostIp_vm1");
			remoteHostUserName_vm = CONFIG.getProperty("remoteHostUserName1_vm1");
			remoteHostPassword_vm = CONFIG.getProperty("remoteHostPassword1_vm1");
			remoteHostDesc_vm = "VMware";
			remoteHostType_vm = "NodeType_vmware";
			nodeGroupName = "Group-A";
			nodeGroupDesc = "VM Host";
			nodesInGroup.add(remoteHostIp_vm);
			scoutingCompletedMsgInScoutingLog = scoutingCompletedMsgInScoutingLog + remoteHostIp_vm; 			
			String vcenterIP_vm1 		= null;
			String vcentertUserName1_vm1 	= null;
			String vcenterPassword1_vm1 	= null;
			
			// Do test setup
			if(!CommonHpsum.performTestSetup()) {
				throw new TestException("Test Failed");
			}
			
						
			// 2. Do Baseline inventory
			if(!BaselineLibrary.performBaselineInventory()) {
				throw new TestException("Test Failed");
			}
			
			// 
			// Execution::
			//
			
			// Add VM node
			if(!Nodes.addNodeVm(remoteHostIp_vm, remoteHostDesc_vm,	remoteHostUserName_vm, 
					remoteHostPassword_vm,false, vcenterIP_vm1, vcentertUserName1_vm1, vcenterPassword1_vm1)) {
				throw new TestException("Test Failed");
			} 
			
			// Get the system time.
			formattedDate = sdf.format(date);
			Date timeRecorded = sdf.parse(formattedDate);
			
			printLogs("System time after adding VM node is: " + formattedDate);
			
			// Create Node group.
			if(!NodeGroups.createNodeGroups(nodeGroupName, nodeGroupDesc, nodesInGroup)){
				printError("Failed to create node group.");
			}
				
			// Go to Nodes page
			if(!guiSetPage("Nodes")){
				printError("Failed to load Node Groups page.");
			}
			
		 	// Do Node inventory
			if(!Nodes.performNodeInventory()) {
				throw new TestException("Test Failed");
			}
			
			// Deploy Node.
			if(!Nodes.deployNode(remoteHostIp_vm, remoteHostType_vm)) {
				throw new TestException("Test Failed");
			} 
						
			//
			// Verification:
			//
			
			File deployLogFile = new  File(System.getProperty("java.io.tmpdir") + "HPSUM\\" + remoteHostIp_vm + "\\deploy.log");
			File inventoryLogFile = new  File(System.getProperty("java.io.tmpdir") + "HPSUM\\" + remoteHostIp_vm + "\\inventory.log");
			File scoutingLogFile = new  File(System.getProperty("java.io.tmpdir") + "HPSUM\\" + remoteHostIp_vm + "\\scouting.log");
			File nodeLogFile = new  File(System.getProperty("java.io.tmpdir") + "HPSUM\\" + remoteHostIp_vm + "\\node.log");
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("java.io.tmpdir") + "HPSUM\\" + remoteHostIp_vm + "\\scouting.log"));
			
			// Check whether deploy log file is present in the path
			if(!deployLogFile.exists()){
				printError("Deploy log file is not present in the path: %temp%\\HPSUM\\" + remoteHostIp_vm + "\\deploy.log");
			}
			else{
				printLogs("Deploy log file is present in the path: %temp%\\HPSUM\\" + remoteHostIp_vm + "\\deploy.log");
			}
			
			// Check whether inventory log file is present in the path
			if(!inventoryLogFile.exists()){
				printError("Inventory log file is not present in the path: %temp%\\HPSUM\\" + remoteHostIp_vm + "\\inventory.log");
			}
			else{
				printLogs("Inventory log file is present in the path: %temp%\\HPSUM\\" + remoteHostIp_vm + "\\inventory.log");
			}
			
			// Check whether scouting log file is present in the path 
			if(!scoutingLogFile.exists()){
				printError("Scouting log file is not present in the path: %temp%\\HPSUM\\" + remoteHostIp_vm + "\\scouting.log");
			}
			else{
				printLogs("Scouting log file is present in the path: %temp%\\HPSUM\\" + remoteHostIp_vm + "\\scouting.log");
			}
			
			// Check whether node log file is present in the path 
			if(!nodeLogFile.exists()){
				printError("Node log file is not present in the path: %temp%\\HPSUM\\" + remoteHostIp_vm + "\\node.log");
			}
			else{
				printLogs("Node log file is present in the path: %temp%\\HPSUM\\" + remoteHostIp_vm + "\\node.log");
			}
			
			// Get the every line present in scouting log
			while ((currentLineInScoutingLog = br.readLine()) != null){
				
				// Check whether scouting completed message is present in scouting log.			
				if(currentLineInScoutingLog.contains(scoutingCompletedMsgInScoutingLog)){
					printLogs(scoutingCompletedMsgInScoutingLog + " message found in the scouting log file");
				
					// Separate the time from the last line 
					timeInLog = currentLineInScoutingLog.substring(10, 37).toUpperCase();
					
					// Convert time in string format to date format 
					Date timeRecordedInLog = sdf.parse(timeInLog);
					printLogs("Recorded Add Node time in Scouting log: " + remoteHostIp_vm + " is " + timeInLog);
					
					// Calculate difference in time (time recorded after add node - time recorded in the scouting log)
					diff = Math.abs(timeRecorded.getTime() - timeRecordedInLog.getTime());	
					diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
					
					// Check whether the difference in time is less than 10.
					if(diffInMinutes > 10) {
						printError("Mismatch in the time at which the node is added and the time that is recorded in the scouting log.");
					}
					else{
						printLogs("Add node time matched. Scouting log file is verified.");
						break;
					}
				}	
			} 
			
			captureScreenShot();
			printLogs("DONE.");
		}
		catch (Throwable t) {
			if (skip) {
				test_result = "SKIP";
			}
			else if (manual) {
				test_result = "MANUAL";
			}
			else {
				test_result = "FAIL";
				ErrorUtil.addVerificationFailure(t);
			}
		}
		finally {
			try {
				// Cleanup:
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
			printLogs("DONE");
		}	
	}
}
