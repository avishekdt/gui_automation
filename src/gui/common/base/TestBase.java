package gui.common.base;

import gui.common.record.Record;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;
import gui.common.util.Xls_Reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;

public class TestBase {
	
	/*
	 * METHODS
	 * activeElementPresence
	 * 
	 */
	
	//public static Logger APP_LOGS = Logger.getLogger("hpsumLogger"); 
	public static Properties CONFIG 	= null;
	public static Properties OR 		= null;
	
	public static Xls_Reader TestSuitesXls	= null;
	public static Xls_Reader suite_xl		= null;
	
	public static boolean isInitialized = false;
	
	public static String presentWorkingDir = "";
	public static String currentOsName = "";
	public static String currentOsArch = "";
	public static String currentSppLocation = "";
	public static boolean standaloneHpsum = false;
	
	public static String location_ieDriverServer;
	public static String location_ieDriverServer_x64 	= "../common/bin/IEDriverServer_x64.exe";
	public static String location_ieDriverServer_x86 	= "../common/bin/IEDriverServer_x32.exe";	
	public static String location_chromeDriver			= "../common/bin/chromedriver.exe";
	
	public static String fn_fail = "Function-Fail";
	public static String fn_pass = "Function-Pass";
	public static String sErrorString = "errorString";
	public static String strToBeReplacedByNodeId = "ID_OF_REMOTE_NODE";
	
	public static int iElementDetectionTimeout 	= 45;
	public static int iPageLoadTimeout 			= 60;
	public static int iExecutionTimeout 		= 60;		// This should always be more than iPageLoadTimeout
	
	public static WebDriver driver = null;
	public static String LogFolderTSFile = "LogFolderTimeStamp.txt";
	public static String logFolder = "";
	
	public static ArrayList<String> globalList = new ArrayList<String>();
	//public static Iterator<String> glistIterator = globalList.iterator();
	public static ArrayList<String> testList = new ArrayList<String>();
	
	public static String timeStamp = "";
	public static String logsFolder = "";
	public static String screenShotFolder = "";
	public static String htmlComment = "";
	public static String reportFolder = "";
	
	public static BufferedWriter logWriter = null;
	public static String LogFileName = "";
	
	public static int screenshot_counter;
	public static String screenshot_name = "default_screenshot_name";
	
	public static int activityScreenRows_counter;
	public static int loggerTab_counter;
	
	
	// TestUtil.java has been pasted below
	public static long startTime = 0;
	
	public static InetAddress LocalHostIp = null;
	public static String LocalHostIpAddress = "";
	
	public TestBase(){
		
	}
	public static void SetDriver(WebDriver wbDriver) {
		driver = wbDriver;
	}
	public static WebDriver getWebDriver() {		
		return driver;
	}
	// Finds if test suite is runnable
	public static boolean isSuiteRunnable(Xls_Reader xls, String suiteName) {		
		boolean isExecutable = false;
		
		for(int i = 2 ; i <= xls.getRowCount("TestSuites") ; i++) {
			String suite = xls.getCellData("TestSuites", "TS_Name", i);
			String runmode = xls.getCellData("TestSuites", "Runmode", i);
			
			if(suite.equalsIgnoreCase(suiteName)) {
				if(runmode.equalsIgnoreCase("Y")) {
					isExecutable=true;
				}
				else {
					isExecutable=false;
				}
				break;
			}
		}
		
		xls=null;		// Release memory
		return isExecutable;
		
	}
	
	// Finds if test case is runnable
	public static boolean isTestCaseRunnable(Xls_Reader xls, String testCaseName) {
		boolean isExecutable = false;
		
		for(int i = 2 ; i <= xls.getRowCount("TestCases") ; i++) {
			String tc_name = xls.getCellData("TestCases", "TC_Name", i);
			String tc_runmode = xls.getCellData("TestCases", "Runmode", i);
			//System.out.println(tc_name + "--" + tc_runmode);
			
			if(tc_name.equalsIgnoreCase(testCaseName)) {
				if(tc_runmode.equalsIgnoreCase("Y")) {
					isExecutable=true;
				}
				else {
					isExecutable=false;
				}
				break;
			}
		}
		
		xls=null;		// Release memory
		return isExecutable;
		
	}

	// Returns the test data from a test in a 2D array.
	public static Object[][] getData(Xls_Reader xls, String testCaseName) {
		
		// If the sheet with testcase name is not present - that means - the test needs to be run just once
		if(!xls.isSheetExist(testCaseName)) {
			xls = null;
			return new Object[1][0];
		}
		
		// Else it means that the testcase sheet is present - may be the user needs parameterization
		int rows = xls.getRowCount(testCaseName);
		int cols = xls.getColumnCount(testCaseName);
		
		System.out.println("Rows count = " + rows);
		System.out.println("Cols count = " + cols);
		
		Object[][] data = new Object[rows-1][cols-3];
		
		for(int rowNum = 2; rowNum <= rows ; rowNum++) {
			for(int colNum = 0 ; colNum <cols-3 ; colNum++) {
				//System.out.print(xls.getCellData(testCaseName, colNum, rowNum) + " -- ");
				data[rowNum-2][colNum] = xls.getCellData(testCaseName, colNum, rowNum);
			}
			//System.out.println();
		}
		
		return data;		
	}
	
	// Update results for a particular data set.
	public static void reportDataSetResult(Xls_Reader xls, String testCaseName, int rowNum, String result) {
		xls.setCellData(testCaseName, "Result", rowNum, result);
	}

	// checks RUnmode for dataSet
	public static String[] getDataSetRunmodes(Xls_Reader xlsFile,String sheetName) {
		String[] runmodes=null;
		if(!xlsFile.isSheetExist(sheetName)) {
			xlsFile=null;
			sheetName=null;
			runmodes = new String[1];
			runmodes[0]="Y";
			xlsFile=null;
			sheetName=null;
			return runmodes;
		}
		runmodes = new String[xlsFile.getRowCount(sheetName)-1];
		for(int i=2;i<=runmodes.length+1;i++){
			runmodes[i-2]=xlsFile.getCellData(sheetName, "Runmode", i);
		}
		xlsFile=null;
		sheetName=null;
		return runmodes;
	}
		
	// Return the row number of the Test case in the TestCases sheet of the Suite
	public static int getRowNum(Xls_Reader xls, String testName) {		
		for(int i = 2 ; i <= xls.getRowCount("TestCases") ; i++) {
			String tc_name = xls.getCellData("TestCases", "TC_Name", i);
			
			if(tc_name.equalsIgnoreCase(testName)) {
				xls = null;
				return i;
			}
		}
		return -1;
	}
	
	// Get the architecture of the OS.
	public static String getOsArch() {
		String osArch = System.getProperty("os.arch");
		
		if(osArch.contains("x86")) {
			osArch = "x86";
		}
		else {
			osArch = "x64";
		}
		//printFunctionReturn(fn_pass);
		return (osArch);
	}
	
	// Get the OS Name.
	public static String getOsName() {
		String osName = System.getProperty("os.name");
		
		if(osName.contains("Windows")) {
			osName = "Windows";
		}
		else {
			osName = "Linux";
		}
		//printFunctionReturn(fn_pass);
		return (osName);
	}
	
	public static void startWatch() {
		startTime = System.currentTimeMillis();
	}
	
	public static void stopWatch() {
		String callingMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
		long time = (System.currentTimeMillis() - startTime)/1000;
		startTime = 0;
		TestBase.printLogs("Execution Time for method: " + callingMethod + "() = "+ time + "s");
		TestBase.globalList.add("Execution Time for method: " + callingMethod + "() = "+ time + "s");
	}
	
	public static String getTimeStamp() {
		java.util.Date date= new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = sdf.format(date);
        return(formattedDate);
	}
	
	// TestUtil.java ends here.
	
	// Initializing the Tests
	public static void initialize() throws FileNotFoundException {
		System.out.println("Initializing...");
		
		if (!isInitialized) {
			System.out.println("Inside Is not initialize.");
			try {
				LocalHostIp=InetAddress.getLocalHost();
				LocalHostIpAddress = LocalHostIp.toString();
				
				System.out.println("GUI-Automation started from IP Address:" + LocalHostIpAddress);
			}
			catch (Exception e) {
				System.out.println("Error while getting the local IP");
			}
			/*
			 * Initializing logs
			 */
			
			/*
			System.out.println("Initializing hpsumlogger.");
			BasicConfigurator.configure();
			// APP_LOGS = Logger.getLogger("hpsumLogger");
			System.out.println(System.getProperty("java.class.path"));
			
			//APP_LOGS = Logger.getLogger("hpsumLogger");
			APP_LOGS.debug("== Automation Log ==");
			*/
			
			// Create a global array list to keep the data during the test and print at end.
			// This list will be cleared at the end of the test.
			globalList.clear();
			globalList.add("Global List Contents:");			
			//System.out.println(globalList);
			
			// Find Present Working Directory
			presentWorkingDir = System.getProperty("user.dir");
			System.out.println("Present Working Dir = " + presentWorkingDir);
			
			// Create Logs Folder
			timeStamp = getTimeStamp();
			logsFolder = "logs/" + timeStamp;
			LogFileName = logsFolder + "/automation-run.log";
			
			globalList.add("Suite Logs Folder: " + presentWorkingDir + "/" + logsFolder);
			
			// Get the Log Directory location.
			//logFolder = getLogsFolder();		# This is not needed now.
			if(!createLogsFolder()) {
				System.out.println("ERROR:: initialize method failed to create Logs Folder.");
			}
			else {
				System.out.println("Created Logs Folder : " + logsFolder);
			}
			
			// Create the screenshots folder.
			screenShotFolder = logsFolder + "/screenshots/";
			createFolder(screenShotFolder);
			
			
			/*
			 * This code is not needed now.
			 * May be we will need it in future to create log-files.
			if(!createLogFile()) {
				System.out.println("ERROR:: initialize method failed to create Logs File.");
			}
			else {
				System.out.println("Created Logs File : " + LogFileName);
			}
			*/
			
			// Find the OS Name.
			String osLocalhost = System.getProperty("os.name");
			osLocalhost = osLocalhost.toLowerCase();
			System.out.println("Localhost OS = " + osLocalhost);
			
			if (osLocalhost.contains("windows")) {
				currentOsName = "windows";
			}
			else {
				currentOsName = "linux";
			}
			System.out.println("Current OS Type = " + currentOsName);
			
			// Find the OS arch
			String osArchLocalhost = System.getProperty("os.arch");
			osArchLocalhost = osArchLocalhost.toLowerCase();
			System.out.println("Localhost OS Arch = " + osArchLocalhost);
			
			if (osArchLocalhost.contains("64")) {
				currentOsArch = "x64";
				location_ieDriverServer = location_ieDriverServer_x64;
			}
			else {
				currentOsArch = "x86";
				location_ieDriverServer = location_ieDriverServer_x86;
			}
			System.out.println("Current OS Arch = " + currentOsArch);
			
			
			// Initializing config files.
			// Retrieve values from config file	
			System.out.println("Loading Property files");
				
			CONFIG = new Properties();
			FileInputStream ip_config = new FileInputStream(System.getProperty("user.dir") + "/src/gui/common/config/config.properties");
			
			try {
				CONFIG.load(ip_config);
				CONFIG.getProperty("testSiteName");
				
			} catch(Exception e) {
				System.err.println("ERROR: Failed to load the config file");
				printException(e);
			}
			
			// Retrieve values from OR file
			OR = new Properties();
			FileInputStream ip_or = new FileInputStream(System.getProperty("user.dir") + "/src/gui/common/config/OR.properties");
			
			try {
				OR.load(ip_or);
			} catch(Exception e) {
				System.err.println("ERROR: Failed to load the OR file");
				printException(e);
			}
			//APP_LOGS.debug("Successfully Loaded Property files");
			
			// Store the currentSppLocation	
			currentSppLocation = getSppLocation();
			System.out.println("Current SPP location = " + currentSppLocation);
			
			/*
			 * Initializing xls file
			 */
			//APP_LOGS.debug("Loading TestSuites.xlsx file.");
			printLogs("Loading TestSuites.xlsx file.");
			TestSuitesXls = new Xls_Reader(System.getProperty("user.dir") + "/src/gui/suites/TestSuites.xlsx");
			//APP_LOGS.debug("Successfully Loaded XLS files");
			
			// Create the execution report html before the run.
			htmlExecReportHeaderWriter();
			
			/*
			 * Initializing WebDriver
			 */
			isInitialized = true;
		}
	}
	//Modified the debranding changes to hpsum. Author: Praveen AC
	// htmlExecReportHeaderWriter: Create the main execution html report header and write the SUM and SPP details.
	public static boolean htmlExecReportHeaderWriter() {
		printLogs("Calling htmlExecReportHeaderWriter");
		
		try{
			String summaryFileName = "GUI-Automation.html";
			
			String [] sumVerBuild = getHpsumVerBuild();
			String [] SppVerBuild = getSppVerBuild(); 
			
			File file = new File(summaryFileName);
			 
    		//if file doesn't exists, then create it
    		if(!file.exists()) {
    			file.createNewFile();
    		}
    		printLogs("Creating email html file for the complete execution : " + summaryFileName);
    		
	        String data = "<html>\n" +
							"<style type=\"text/css\">\n" +	
								".pass {background-color:green}\n" +
								".fail {background-color:red}\n" +
								".skip {background-color:yellow}\n" +
								".manual {background-color:blue}\n" +
							"</style>\n" +
							"<body>\n" +
							"<b>Automation Server </b>: " + LocalHostIpAddress + "<br>\n" +
							"<b>SUM Version </b>: " + sumVerBuild[0] + "<br>\n" +
						    "<b>SUM Build </b>: " + sumVerBuild[1] + "<br>\n" +
						    "<b>SPP Version </b>: " + SppVerBuild[0] + "<br>\n" +  //LocalHostIpAddress
						    "<b>SPP Build </b>: " + SppVerBuild[1] + "<br><br>\n" +
								"<table style=\"width:500pxborder=\"2\" border=\"2\">\n" +
									"<tr>\n" +
										"<th colspan=\"4\"><h3>GUI - Automation Result</h3></th>\n" +
									"</tr>\n" +
									"<tr>\n" +
										"<td><h4>Test Case Name</h4></td>\n" +
										"<td><h4>Result    </h4></td>\n" +
										"<td><h4>Execution Time (h:m:s) </h4></td>\n" +
										"<td><h4>Comments </h4></td>\n" +
									"</tr>\n";
	        
	        FileWriter fileWriter2 = new FileWriter(summaryFileName, false); //false means clear and write
	        BufferedWriter bufferWriter2 = new BufferedWriter(fileWriter2);
	        bufferWriter2.write(data);
	        bufferWriter2.close();
	        
	        printFunctionReturn(fn_pass);
	        return true;
    	}
		catch(IOException e){
    		//e.printStackTrace();
			printError("htmlExecReportHeaderWriter failed to write the Final Execution Report html");
    		printFunctionReturn(fn_fail);
    		return false;
    	}
	}
	
	// htmlHeaderWriter: Create the html report file for each testcase and write the HPSUM and SPP details.
	public static boolean htmlHeaderWriter(String testName) {
		printLogs("Calling htmlHeaderWriter with values: " + testName);
		
		try{
			String fileName = logsFolder + "/" + testName + ".html";
			//String summaryFileName = "GUI-Automation.html";
			String [] sumVerBuild = getHpsumVerBuild();
			String [] SppVerBuild = getSppVerBuild(); 
			
			File file = new File(fileName);
			 
    		//if file doesn't exists, then create it
    		if(!file.exists()) {
    			file.createNewFile();
    		}
    		printLogs("Creating email html file for the test : " + fileName);
    		
	        String data = "<html>\n" +
							"<style type=\"text/css\">\n" +	
								".pass {background-color:green}\n" +
								".fail {background-color:red}\n" +
								".skip {background-color:yellow}\n" +
								".manual {background-color:blue}\n" +
							"</style>\n" +
							"<body>\n" +
							"<b>Automation Server </b>: " + LocalHostIpAddress + "<br>\n" +
								"<b>SUM Version </b>: " + sumVerBuild[0] + "<br>\n" +
							    "<b>SUM Build </b>: " + sumVerBuild[1] + "<br>\n" +
							    "<b>SPP Version </b>: " + SppVerBuild[0] + "<br>\n" +
							    "<b>SPP Build </b>: " + SppVerBuild[1] + "<br><br>\n" +
							    "<table style=\"width:500pxborder=\"2\" border=\"2\">\n" +
									"<tr>\n" +
										"<th colspan=\"4\"><h3>GUI - Automation Result</h3></th>\n" +
									"</tr>\n" +
									"<tr>\n" +
										"<td><h4>Test Case Name</h4></td>\n" +
										"<td><h4>Result    </h4></td>\n" +
										"<td><h4>Execution Time (h:m:s) </h4></td>\n" +
										"<td><h4>Comments </h4></td>\n" +
									"</tr>\n";
	        
	        FileWriter fileWriter = new FileWriter(fileName, true);//true means append
	        BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
	        bufferWriter.write(data);
	        bufferWriter.close();
			
	        printFunctionReturn(fn_pass);
	        return true;
    	}
		catch(IOException e){
			printError("htmlHeaderWriter failed to write the test case related html");
    		printFunctionReturn(fn_fail);
    		return false;
    	}
	}
	
	// htmlTestDataWriter: Writes the test result in testname.html
	public static boolean htmlTestDataWriter(String result, String testName, String timeTaken) {
		printLogs("Calling htmlTestDataWriter with values: " + result + ", " + testName + ", " + timeTaken);
		
		try{
			String fileName = logsFolder + "/" + testName + ".html";
			String summaryFileName = "GUI-Automation.html";
			
			File file = new File(fileName);
			String htmlResult;
			
			if (result.equalsIgnoreCase("PASS")) {
				htmlResult = "\t\t\t\t<td class=\"pass\"><b>PASS</b></td>\n";				
			}
			else if (result.equalsIgnoreCase("FAIL")) {
				htmlResult = "\t\t\t\t<td class=\"fail\"><b>FAIL</b></td>\n";
			}
			else if (result.equalsIgnoreCase("MANUAL")) {
				htmlResult = "\t\t\t\t<td class=\"manual\"><b>MANUAL</b></td>\n";
			}
			else {
				htmlResult = "\t\t\t\t<td class=\"skip\"><b>SKIP</b></td>\n";
			}
			
			String data = "\n\t\t\t<tr>\n \t\t\t\t<td>" +  testName + "</td>\n" + htmlResult + "\t\t\t\t<td align=\"center\">" +
						   timeTaken + "</td>\n" + "\t\t\t\t<td>" + htmlComment + "</td>\n\t\t\t</tr>\n";

    		//if file doesn't exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
    		printLogs("Writing test result "+ result + " to file : " + fileName);
    	
    		
    		//true = append file
    		FileWriter fileWritter = new FileWriter(fileName, true);
    		
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        bufferWritter.write(data);
	        bufferWritter.close();
	        
	        FileWriter fileWriter2 = new FileWriter(summaryFileName, true);
	        BufferedWriter bufferWriter2 = new BufferedWriter(fileWriter2);
	        bufferWriter2.write(data);
	        bufferWriter2.close();
	        
	        printFunctionReturn(fn_pass);
	        return true;
    	}
		catch(IOException e){
			printFunctionReturn(fn_fail);
    		return false;
    	}
	}
	
/*	public static boolean systemDetailsHeaderWriter() {
		try{
			String fileName =  "System_details.txt";
			//String summaryFileName = "GUI-Automation.html";
					
			File file = new File(fileName);
			 
    		//if file doesn't exists, then create it
    		if(!file.exists()) {
    			file.createNewFile();
    		}
    		printLogs("Creating System Details Header file: " + fileName);
    		
	        String topBox    = "+----------------+---------------+\n";
	        String topData   = "|   Target IP    |   Target OS   |\n";
	        String bottomBox = "+----------------+---------------+\n";
	        FileWriter fileWriter = new FileWriter(fileName, false);//true means append
	        BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
	        bufferWriter.write(topBox + topData + bottomBox);
	        bufferWriter.close();
			
	        printFunctionReturn(fn_pass);
	        return true;
    	}
		catch(IOException e){
			printError("systemDetailsHeaderWriter failed to write the test case related html");
    		printFunctionReturn(fn_fail);
    		return false;
    	}
	}
	
	public static boolean systemDetailsDataWriter(String testname) {
		try{
			String fileName =  "System_details.txt";
					
			File file = new File(fileName);
			 
    		//if file doesn't exists, then create it
    		if(!file.exists()) {
    			file.createNewFile();
    		}
    		printLogs("Adding System Details Data into file: " + fileName);
    		
    		switch (testname)){
			
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
    		String targetIp = OR.getProperty("")
	        
	        String data      = "| 15.154.112.222 | 15.154.112.222|\n";
	        String bottomBox = "+----------------+---------------+\n";
	        FileWriter fileWriter = new FileWriter(fileName, true);//true means append
	        BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
	        bufferWriter.write(data + bottomBox);
	        bufferWriter.close();
			
	        printFunctionReturn(fn_pass);
	        return true;
    	}
		catch(IOException e){
			printError("systemDetailsHeaderWriter failed to write the test case related html");
    		printFunctionReturn(fn_fail);
    		return false;
    	}
	}*/
	
	// testInitialize: Initializes the testcase.
	public static boolean testInitialize(String testName) {
		printLogs("Calling testInitialize with values : " + testName);
		
		try {
			globalList.add("Test Name: " + testName);
			testList.clear();
			testList.add("Test List Contents:");
			testList.add("Test Name: " + testName);
			
			// Create the email file
			if(!htmlHeaderWriter(testName)) {
				printError("Faied to create email file.");
			}
			printFunctionReturn(fn_pass);
		    return true;
		    
		} catch (Exception e) {
			printFunctionReturn(fn_fail);
		    return false;
		}
	}
	
	//killRemoteHpsum: kills windows and linmux remote HPSUM services.
	public static boolean killRemoteHpsum(String remoteHostOs, String ip, String username, String password) {
		
		try {
			if (currentOsName.contains("windows")) {
				printLogs ("Calling killRemoteHpsum in windows with values: " + remoteHostOs + "," + ip + "," + username + "," + password );
				String remoteKillHpsumService = System.getProperty("user.dir") + "/src/gui/common/util/kill-remote-sum.bat " + remoteHostOs + " " + ip 
						+ " " + username + " " + password;			
				if(!executeCommand(remoteKillsumService)) {
					printFunctionReturn(fn_fail);
					return false;
				}
				printLogs("Killed remote HPSUM service successfully");
			}
			else {
				printLogs ("Calling killRemoteHpsumLinToLin in Linux with values: " + remoteHostOs + "," + ip + "," + username + "," + password );
				String[] cmds = {"pkill -f hpsum_service","pkill -f sum_service",CONFIG.getProperty("logs_temp1_l"), CONFIG.getProperty("logs_temp2_l"), CONFIG.getProperty("logs_var1_l")};
				for(int i=0; i<cmds.length;i++){
					printLogs("Cleaning up the system: " + cmds[i]);
					if(!killRemoteHpsumLinToLin(ip, username, password, cmds[i])) {
						printFunctionReturn(fn_fail);
						if(cmds[i]==cmds[0]){
							return false;
						}						
					}
				}
			}
		}
		catch (Exception e) {
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}

	// killHpsumLinToLin to kill hpsum service from lin to lin
	public static boolean killRemoteHpsumLinToLin(String ip, String remoteUsername, String remotePassword, String cmd)  {
		printLogs("Calling killRemoteHpsumLinToLin with values: " + ip + ", " + remoteUsername + ", " + remotePassword);
		
        String command = cmd;
        JSch jsch = new JSch();
        com.jcraft.jsch.Session session = null;
        boolean fail = false;
        int status;
        
        try {
 		   session = jsch.getSession(remoteUsername, ip, 22);
 		   session.setConfig("StrictHostKeyChecking", "no");
 		   session.setPassword(remotePassword);
 		   session.connect(); 
 		   
 		   Channel channel = session.openChannel("sftp");
 		   channel.connect();
 		   
 		   ChannelExec channels = (ChannelExec)session.openChannel("exec");
 		   ((ChannelExec)channels).setCommand(command);
 		                      
 		   InputStream in=channels.getInputStream();
 		   channels.connect();
 		   
 		   byte[] tmp=new byte[1024];
 		   while(true) {
 			 while(in.available() > 0) {
 			     int i = in.read(tmp, 0, 1024);
 			     if( i < 0) {
 			    	 break;
 			     }
 			     printLogs(new String(tmp, 0, i));
 			 }
 		     if(channels.isClosed()) {
 		       status = channels.getExitStatus();
 		       if(status == 0) {
 		    	   printLogs("Killed remote HPSUM service successfully");
 		       }
 		       else if (status == 1){
 		    	  printLogs("There was not remote HPSUM service found running");
 		       }
 		       else {
 		    	   printError("Failed to Kill remote HPSUM service");
 		    	   fail = true;
 		       }
 		       break;
 		     }
 		   }               
 		   session.disconnect();
        }
        catch (Exception e) {
        	printLogs("Failed to Kill remote HPSUM service. Stack trace:"+ e);
        	printFunctionReturn(fn_fail);
        	return false;
		}
        if (fail) {
        	printLogs("Failed to Kill remote HPSUM service");
			printFunctionReturn(fn_fail);
        	return false;
        }
        printFunctionReturn(fn_pass);
       return true;
    }
	
	// getTimeInMsec: Returns the current time in ms.
	public static long getTimeInMsec() {
		Calendar timeStamp = Calendar.getInstance();
		testList.add("Current TimeStamp : " + timeStamp.getTime());
		printLogs("Current TimeStamp : " + timeStamp.getTime());
		return(timeStamp.getTimeInMillis());
	}
	

	
	// testFinalize: Writes the final test result and sends email.
	public static boolean testFinalize(String testName, String result, String execTime) {
		printLogs("Calling testFinalize with values : " + testName + ", " + result + ", " + execTime);
		
		try {
			// if no applicable components found, mark the result as Skip
			String msgNoComponents = OR.getProperty("MessageNodeInventoryNoApplicableComponents");
			if (htmlComment.contains(msgNoComponents)) {
				result = "SKIP";
			}
			
			testList.add(testName + " : TEST RESULT : " + result);
			globalList.add(testName + " : TEST RESULT : " + result);
			
			// Write the test result.
			htmlTestDataWriter(result, testName, execTime);
			
			// If sendEmailAfterEachTest=1 then call sendEmail
			if(CONFIG.getProperty("sendEmailAfterEachTest").contains("1")) {
				sendEmail(testName);
			}
			else {
				printLogs("Skipped sending email for the test.");
			}
			htmlComment = "";
			printFunctionReturn(fn_pass);
			return true;
		}
		 catch (Exception e) {
        	printError("Exception in testFinalize method");
    		printFunctionReturn(fn_fail);
    		return false;
        }
	}
	
	// calculateTimeInterval: Returns the time difference in h:m:s format
	public static String calculateTimeInterval(long initialTime, long finalTime) {
		printLogs("Calling calculateTimeInterval with values : " + initialTime + ", " + finalTime);
		
		long timeDifInMilliSec;
        if(initialTime >= finalTime) {
            timeDifInMilliSec = initialTime - finalTime;
        }
        else {
            timeDifInMilliSec = finalTime - initialTime;
        }
 
        long timeDifSeconds = (timeDifInMilliSec / 1000)%60;
        long timeDifMinutes = (timeDifInMilliSec / (60 * 1000))%60;
        long timeDifHours = timeDifInMilliSec / (60 * 60 * 1000);
        //long timeDifDays = timeDifInMilliSec / (24 * 60 * 60 * 1000);
        
       /* System.out.println("Time differences expressed in various units are given below");
        System.out.println(timeDifInMilliSec + " Milliseconds");
        System.out.println(timeDifSeconds + " Seconds");
        System.out.println(timeDifMinutes + " Minutes");
        System.out.println(timeDifHours + " Hours");
        System.out.println(timeDifDays + " Days");*/
        
        String hms =  timeDifHours + " : " + timeDifMinutes%60 + " : " + timeDifSeconds%60 ;
        printFunctionReturn(fn_pass);
		return(hms);
	}
	
	// sendEmail: Sends email for each testcase.
    public static boolean sendEmail (String testName) {
    	printLogs("Calling sendEmail");
    	
        try {        	
        	String subject = "GUI-Automation - " + currentOsName + ": " + testName;
            String smtpHostServer = "smtp1.hpe.com";
            String toEmailId = CONFIG.getProperty("emailToAddresses");
            String fromEmailId = CONFIG.getProperty("emailFromAddress");
            String fileName = logsFolder + "/" + testName + ".html";
            
            Properties props = System.getProperties();
            props.put("mail.smtp.host", smtpHostServer);
            Session session = Session.getInstance(props, null);
            
            FileInputStream f = new FileInputStream(new File(fileName));
            
            printLogs("Sending Mail taking the reference from file : " + fileName);
            String htmlText = IOUtils.toString(f, "UTF-8");
	        MimeMessage msg = new MimeMessage(session);
	        
	        //set message headers
	        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	        msg.addHeader("format", "flowed");
	        msg.addHeader("Content-Transfer-Encoding", "8bit");
	        
	        msg.setFrom(new InternetAddress(fromEmailId));
	        msg.setSubject(subject, "UTF-8");
	 
	        msg.setContent("Hi All,<br>Below is the result of Automation run in " + currentOsName + ":</br><br>" + htmlText, "text/html; charset=utf-8");
	        msg.setSentDate(new Date());
	 
	        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailId, false));
	        msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(fromEmailId, false));
	        
	        Transport.send(msg);  
	        
	        printLogs("EMail sent successfully!");
	        printFunctionReturn(fn_pass);
	        return true;
        }
        catch (Exception e) {
        	printError("Email Trasmit error");
    		printFunctionReturn(fn_fail);
    		return false;
        }
    }
	
	// Get the SPP location from the config file based on the OS.
	public static String getSppLocation() {
		printLogs("Calling getSppLocation");
		
		String sppLocation = "";
		
		if (currentOsName.contains("windows")) {
			sppLocation = CONFIG.getProperty("sppLocation_w");
		}
		else {
			sppLocation = CONFIG.getProperty("sppLocation_l");
		}
		
		printFunctionReturn(fn_pass);
		return(sppLocation);
	}
	//Author: Praveen AC
	//Get the HPSUM location from the config file based on the OS.
	public static String getHpsumLocation() {
		printLogs("Calling getSppLocation");
		
		String hpsumLocation = "";
		
		if (currentOsName.contains("windows")) {
			hpsumLocation = CONFIG.getProperty("hpsumLocation_w");
		}
		else {
			hpsumLocation = CONFIG.getProperty("hpsumLocation_l");
		}
		
		printFunctionReturn(fn_pass);
		return(hpsumLocation);
	}
		
	// Every time the test is started - Old HPSUM/SUM service is Killed, Logs cleaned and New Service started 
	public static boolean hpsumKillCleanStart() {
		printLogs("Calling hpsumKillCleanStart");
		
		String command_exec = "";
		//String killHpsumDeleteLogs_w = System.getProperty("user.dir") + "/src/gui/common/util/killHpsumDeleteLogs.bat";
		//String killHpsumDeleteLogs_l  = System.getProperty("user.dir") + "/src/gui/common/util/killHpsumDeleteLogs.pl";
		
		//String rmDirCmd_w = "rmdir /S/Q";
		//C:\Users\Administrator\AppData\Local\Temp
		//C:\\Users\\ADMINI~1\\AppData\\Local\\Temp\\1\\HPSUM
		//String rmDir1_w = "C:\\Users\\Administrator\\AppData\\Local\\Temp\\1\\HPSUM";
		//String rmDir2_w = "C:\\Users\\Administrator\\AppData\\Local\\Temp\\2\\HPSUM";
		
		String[] rmDir1_l = {"rm -rf /tmp/HPSUM", "rm -rf /var/tmp/SUM/", "rm -rf /var/log/sum"};
		
		// HP SUM Kill and Clean Logs
		if(!killHpsumService()) {
			printFunctionReturn(fn_fail);
			return false;
		}
		printLogs("Successfully killed HPSUM/SUM Service.");
		
		sleep(15);
		
		// Clean Logs
		//if deleteHpsumLogs=1 then only clean the logs.
		if (CONFIG.getProperty("deleteHpsumLogs").equals("1")) {
			if (currentOsName.contains("windows")) {
				String fileKillSumDeleteLogs = System.getProperty("user.dir") + "/src/gui/common/util/killSumDeleteLogs.bat";
				
				if(!executeCommand(fileKillSumDeleteLogs)) {
					printFunctionReturn(fn_fail);
					//return false;
				}
			}
			else {
				for(int i=0;i<rmDir1_l.length;i++){
				command_exec = rmDir1_l[i];
					
					if(!executeCommand(command_exec)) {
						printFunctionReturn(fn_fail);
						//return false;
					}
				}
			}
			printLogs("Deleted the old HPSUM/SUM logs.");
		}
		else {
			printLogs("Skipped the deletion the old HPSUM/SUM logs.");
		}
		
		sleep(10);
		
		// HP SUM Start
		if(!startHpsumService()) {
			printFunctionReturn(fn_fail);
			return false;
		}
		printLogs("Started the SUM Service");
		
		sleep(15);
		
		// Starting the HP SUM service using batch file would have started the default browser.
		// We need to close all such browsers.
		printLogs("Killing all the opened browsers.");
		if(!killAllOpenedBrowsers()) {
			printFunctionReturn(fn_fail);
			return false;
		}
		sleep(10);
		printLogs("Successfully handled the old sum service and started the new one.");
		printFunctionReturn(fn_pass);
		return true;
	}
	
	public static boolean hpsumKillCleanStart(String ftpNumber) {
		printLogs("Calling hpsumKillCleanStart with values: " + ftpNumber);
		
		String command_exec = "";
		//String killHpsumDeleteLogs_w = System.getProperty("user.dir") + "/src/gui/common/util/killHpsumDeleteLogs.bat";
		//String killHpsumDeleteLogs_l  = System.getProperty("user.dir") + "/src/gui/common/util/killHpsumDeleteLogs.pl";
		
		//String rmDirCmd_w = "rmdir /S/Q";
		//C:\Users\Administrator\AppData\Local\Temp
		//C:\\Users\\ADMINI~1\\AppData\\Local\\Temp\\1\\HPSUM
		//String rmDir1_w = "C:\\Users\\Administrator\\AppData\\Local\\Temp\\1\\HPSUM";
		//String rmDir2_w = "C:\\Users\\Administrator\\AppData\\Local\\Temp\\2\\HPSUM";
		
	String[] rmDir1_l = {"rm -rf /tmp/HPSUM", "rm -rf /var/tmp/SUM/", "rm -rf /var/log/sum"};
		
		// HP SUM Kill and Clean Logs
		if(!killHpsumService()) {
			printFunctionReturn(fn_fail);
			return false;
		}
		printLogs("Successfully killed HPSUM Service.");
		
		sleep(15);
		
		// Clean Logs
		//if deleteHpsumLogs=1 then only clean the logs.
		if (CONFIG.getProperty("deleteHpsumLogs").equals("1")) {
			if (currentOsName.contains("windows")) {
				String fileKillHpsumDeleteLogs = System.getProperty("user.dir") + "/src/gui/common/util/killSumDeleteLogs.bat";
				
				if(!executeCommand(fileKillHpsumDeleteLogs)) {
					printFunctionReturn(fn_fail);
					//return false;
				}
			}
			else {
				for(int i=0;i<rmDir1_l.length;i++){
				command_exec = rmDir1_l[i];
					
					if(!executeCommand(command_exec)) {
						printFunctionReturn(fn_fail);
						//return false;
					}
				}
			}
			printLogs("Deleted the old HPSUM/SUM logs.");
		}
		else {
			printLogs("Skipped the deletion the old HPSUM/SUM logs.");
		}
		
		sleep(10);
		
		// HP SUM Start
		if(!startHpsumServiceWithFtpNumber(ftpNumber)) {
			printFunctionReturn(fn_fail);
			return false;
		}
		printLogs("Started the HPSUM Service");
		
		sleep(15);
		
		// Starting the HP SUM service using batch file would have started the default browser.
		// We need to close all such browsers.
		printLogs("Killing all the opened browsers.");
		if(!killAllOpenedBrowsers()) {
			printFunctionReturn(fn_fail);
			return false;
		}
		sleep(10);
		printLogs("Successfully handled the old hpsum service and started the new one.");
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Close all the open browsers
	public static boolean killAllOpenedBrowsers() {
		printLogs("Calling killAllOpenedBrowsers");
		
		String killCommand_w1 = "taskkill /f /IM iexplore*";		
		String killCommand_w2 = "taskkill /f /IM chrome*";
		String killCommand_w3 = "taskkill /f /IM firefox*";
		
		String command_exec = "";
		String perlExecuteCommand_l  = System.getProperty("user.dir") + "/src/gui/common/util/perlExecuteCommand.pl";
		String perlExecuteCommandOption_l = "kill-browsers";
				
		if (currentOsName.contains("windows")) {
			if(!executeCommand(killCommand_w1)) {
				printFunctionReturn(fn_fail);
				return false;
			}
			if(!executeCommand(killCommand_w2)) {
				printFunctionReturn(fn_fail);
				return false;
			}
			if(!executeCommand(killCommand_w3)) {
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		else {
			command_exec = "perl " + perlExecuteCommand_l + " " + perlExecuteCommandOption_l;
			
			if(!executeCommand(command_exec)) {
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		printLogs("Executed all the browser kill commands.");
		
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// startHpsumService
	public static boolean startHpsumService() {
		printLogs("Calling startHpsumService");
		
		String command_exec = "";
		//String hpSumLocation = currentSppLocation + "/hp/swpackages";		
		String hpSumLocation = "";	
		if(!standaloneHpsum){
			hpSumLocation = currentSppLocation + "/packages";
			printLogs("Running from SPP location");
		}else{
			hpSumLocation = getHpsumLocation();
			printLogs("Running from HPSUM/SUM folder to run as standalone HPSUM");
		}
		
		String perlStartScript_l = System.getProperty("user.dir") + "/src/gui/common/util/perlStartScript.pl";
		String perlStartScriptOptionPath_l = hpSumLocation;
		String perlStartScriptOptionName_l = "sum";
		
		if (currentOsName.contains("windows")) {
			command_exec = hpSumLocation + "/sum.bat";
		}
		else {
			command_exec = "perl " + perlStartScript_l + " " + perlStartScriptOptionPath_l + " " + perlStartScriptOptionName_l;
		}
		if(!executeCommand(command_exec)) {
			printFunctionReturn(fn_fail);
			return false;
		}
		
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// startHpsumService with ftp port number
	public static boolean startHpsumServiceWithFtpNumber(String ftpNumber) {
		printLogs("Calling startHpsumServiceWithFtpNumber with values: " + ftpNumber);
		String ftpPort=CONFIG.getProperty(ftpNumber);
		
		String command_exec = "";
		//String hpSumLocation = currentSppLocation + "/hp/swpackages";
		String hpSumLocation = "";	
		if(!standaloneHpsum){
			hpSumLocation = currentSppLocation + "/packages";
			printLogs("Running from SPP location");
		}else{
			hpSumLocation = getHpsumLocation();
			printLogs("Running from HPSUM/SUM folder to run as standalone HPSUM");
		}
			
		String perlStartScript_l = System.getProperty("user.dir") + "/src/gui/common/util/perlStartScript.pl";
		String perlStartScriptOptionPath_l = hpSumLocation;
		String perlStartScriptOptionName_l = "sum /ftp_port " + ftpPort;
			
		if (currentOsName.contains("windows")) {
			command_exec = hpSumLocation + "/sum.bat /ftp_port " + ftpPort;
		}
		else {
			command_exec = "perl " + perlStartScript_l + " " + perlStartScriptOptionPath_l + " " + perlStartScriptOptionName_l;
		}
		if(!executeCommand(command_exec)) {
			printFunctionReturn(fn_fail);
			return false;
		}
			
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// killHpsumService
	public static boolean killHpsumService() {
		printLogs("Calling killHpsumService");
		
		String command_exec = "";
		String killCommand_w = "taskkill /f /IM sum_service*";
		String killScript_l  = System.getProperty("user.dir") + "/src/gui/common/util/perlExecuteCommand.pl";
		String killScriptOption_l = "kill-sum";
		//String killCommand_l = "killall -9 hpsum_service*";
		
		if (currentOsName.contains("windows")) {
			command_exec = killCommand_w;
			executeCommand("taskkill /f /IM hpsum_service*");
		}
		else {
			//command_exec = killCommand_l;
			command_exec = "perl " + killScript_l + " " + killScriptOption_l;
		}
		if(!executeCommand(command_exec)) {
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// executeGatherLogs
	public static boolean executeGatherLogs(String testName) {
		printLogs("Calling executeGatherLogs");
		
		String command_exec = "";
		String hpSumLocation =" ";
		if(standaloneHpsum){
			hpSumLocation = getHpsumLocation();
		}else{
			hpSumLocation = currentSppLocation + "/packages";
		}
		String sharePath = "smb://15.154.112.188/HPSUM_Logs/";
		
		String perlStartScript_l = System.getProperty("user.dir") + "/src/gui/common/util/perlStartScript.pl";
		String perlStartScriptOptionPath_l = hpSumLocation;
		String perlStartScriptOptionName_l = "gatherlogs.sh";
		
		if (currentOsName.contains("windows")) {
			command_exec = hpSumLocation + "/gatherlogs.bat";
		}
		else {
			command_exec = "perl " + perlStartScript_l + " " + perlStartScriptOptionPath_l + " " + perlStartScriptOptionName_l;
		}
		if(!executeCommand(command_exec)) {
			printFunctionReturn(fn_fail);
			return false;
		}
		
		printLogs("Sleeping for 30s to wait for gatherlogs to collect the logs");
		sleep(30);
		
		// Copy the collected gatherlog to the logs folder.
		/*	if(!moveGatheredLogsToLogs(testName)) {
			printFunctionReturn(fn_fail);
			return false;
		}*/
		
		if(!moveGatherlogsToShare(testName, hpSumLocation, sharePath)) {
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// moveGatheredLogsToLogs
	public static boolean moveGatheredLogsToLogs(String testName) {
		printLogs("Calling moveGatheredLogsToLogs");
		
		try {
			File srcDir = new File(currentSppLocation + "/packages"); //source directory
			File fileList[];
			String fileType = "";
			String host = "";
			File srcFile;
			File destFile;
			
			if (currentOsName.contains("windows")) {
				fileType = ".zip";
				host = "Win";
			}
			else {
				fileType = ".tar.gz";
				host = "Lin";
			}
			
			// Get a list of all the gatherLogs (.zip/.tar.gz) files
			fileList = srcDir.listFiles();
			String fileName = "";
			
			for (int i = 0 ; i < fileList.length ; i++) {
				//filePathName = fileList[i].getAbsolutePath().trim();
				fileName = fileList[i].getName();
				
				if(fileName.endsWith(fileType) && fileName.contains("SUM_Logs")) {
					srcFile = fileList[i];
					destFile = new File(logsFolder + "/" + host + "_" + testName + "_" + fileList[i].getName());
					FileUtils.copyFile(srcFile, destFile);
					printLogs("Copied to logs folder: " + srcFile.getName() + " to " + destFile.getName());
				}
			}
		}
		catch (Exception e) {
        	printError("Copy of gather logs failed for " + testName);
    		printFunctionReturn(fn_fail);
    		return false;
		}
		
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// moveGatherlogsToShare: moves the gatherlogs the share folder
    public static boolean moveGatherlogsToShare(String testName, String sourceLocalPath, String destinationSharePath) {
    	printLogs("Calling moveGatherlogsToShare with: " + sourceLocalPath + "," + destinationSharePath);
    	
    	try {
		   String userName = "Administrator";
		   String password = "12iso*help";
		   File fileList[];
		   String fileType = "";
		   String host = "";
		   sourceLocalPath = sourceLocalPath.concat("/");
		   File srcDir = new File(sourceLocalPath);
		   fileList = srcDir.listFiles();
		   String fileName = "";
		   String descHpsumVer= "";
		   String descHpsumBuild= "";
		   String hpsum[] = getHpsumVerBuild();
			
		   // Setting the auth token and assigning 
		   String userNamePassword = userName + ":" + password;
		   NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(userNamePassword);
	
		   // Creating folder related to the MAT Structure
		   if(hpsum[0].contains("720")) {
			   descHpsumVer = "MAT_7.2.0";
		   }
		   else if (hpsum[0].contains("730")) {
			   descHpsumVer = "MAT_7.3.0";
		   }
		   else if (hpsum[0].contains("740")) {
			   descHpsumVer = "MAT_7.4.0";
		   }
		   else if (hpsum[0].contains("750")) {
			   descHpsumVer = "MAT_7.5.0";
		   }
		   else if (hpsum[0].contains("760")) {
			   descHpsumVer = "MAT_7.6.0";
		   }
		    else if (hpsum[0].contains("800")) {
			   descHpsumVer = "MAT_8.0.0";
		   }
		   // Add here for more HPSUM version		   
		   
		   descHpsumBuild = hpsum[1].replaceAll("b", "");
		   destinationSharePath = destinationSharePath.concat(descHpsumVer + "/" + descHpsumBuild + "/" + "GUI/");
		   SmbFile destPath = new SmbFile(destinationSharePath, auth);
		   
		   if (!destPath.exists()) {
			   printLogs("Creating folder: " + destPath);
			   destPath.mkdirs();
		   }
		   else {
			   printLogs("Folder " + destPath + " already exists");
		   }

		   // Picking up the right files in OSes
		   if (currentOsName.contains("windows")) {
			   fileType = ".zip";
			   host = "Win";
		   }

		   else {
			   fileType = ".tar.gz";
			   host = "Lin";
		   }
		   			
		   for (int i = 0 ; i < fileList.length ; i++) {
			   fileName = fileList[i].getName();
			   if(fileName.endsWith(fileType) && fileName.contains("SUM_Logs")) {
				   
				   printLogs("Copying " + fileName + " to " + destinationSharePath + "-" + host + "_"+ testName + fileList[i].getName());

				   SmbFile dFile = new SmbFile(destinationSharePath + host + "-"+ testName + "_" + fileList[i].getName(), auth);
				   SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(dFile);
				   File file = new File(sourceLocalPath + fileName);
				   FileInputStream fileInputStream = new FileInputStream(file);
										
				   byte[] buf = new byte[16 * 1024 * 1024];
				   int len;
			   
				   while ((len = fileInputStream.read(buf)) > 0) {
					   smbFileOutputStream.write(buf, 0, len);
				   }
				   fileInputStream.close();
				   smbFileOutputStream.close();
				   if(!file.delete()) {
				    	System.out.println("Unable to delete " + sourceLocalPath + fileName);
				    	throw (new Exception());
				   }
				   appendHtmlComment("Copied logs to: " + destinationSharePath + fileList[i].getName());
			   }
		   }
	   }
	   catch(Exception e) {
        	printError("Copy of gather logs to share failed");
    		printFunctionReturn(fn_fail);
    		return false;
	   }		
	   printFunctionReturn(fn_pass);
	   return true;
    }
	
	// copyXlsToLogs
	public static boolean copyXlsToLogs(String suiteName) {
		printLogs("Calling copyXlsToLogs");
		
		try {
			File srcFile = new File(System.getProperty("user.dir") + "/src/gui/suites/" + suiteName + ".xlsx");
			File destDir = new File(logsFolder);
			
			FileUtils.copyFileToDirectory(srcFile, destDir);
			printLogs("Copied xls file: " + srcFile.getName() + " to logs dir " + destDir.getName());
		} 
		catch (IOException e) {
			printError("Failed to copy xls file for suite : " + suiteName);
    		printFunctionReturn(fn_fail);
    		return false;
		}
		
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// createFolder
	// All non-existent ancestor directories are automatically created.
	public static boolean createFolder(String folderPath) {
		printLogs("Calling createLogsFolder with value: " + folderPath);
		
		File destPath = new File(folderPath);
		
		try {
			if (!destPath.exists()) {
			   printLogs("Creating folder: " + destPath);
			   FileUtils.forceMkdir(destPath);
			}
			else {
			   printLogs("Folder " + destPath + " already exists");
			}
		} 
		catch (IOException e) {
			printError("Exception while creating folder: " + folderPath);
			printFunctionReturn(fn_fail);
		    return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// createLogsFolder
	// This is the first folder created thats why syso is used and not printLogs
	// Do not put printLogs() in this.
	public static boolean createLogsFolder() {
		printLogs("Calling createLogsFolder");
		
		// Create lOG directory; all non-existent ancestor directories are automatically created.
		//File dir = new File("nameoffolder");
		//dir.mkdir();
		System.out.println("Creating Logs Folder: " + logsFolder);
		boolean success = (new File(logsFolder)).mkdirs();
		
		if (!success) {
		    System.out.println("ERROR:: Failed to create logs folder: " + logsFolder);
			printFunctionReturn(fn_fail);
		    return false;
		}
		else {
			System.out.println("LOGS FOLDER:: " + logsFolder);
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// createLogFile
	public static boolean createLogFile() {
		printLogs("Calling createLogFile");
		
        try {
            //String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        	File logFile = new File(LogFileName);
            // This will output the full path where the file will be written to...
            System.out.println("Logs File:: " + logFile.getCanonicalPath());
            
            //BufferedWriter 
            logWriter = new BufferedWriter(new FileWriter(logFile));
            logWriter.write("==== Automation Log ====");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                // Close the writer regardless of what happens...
            	logWriter.close();
            } 
            catch (Exception e) {
            	//Nothing
            }
        }
        printFunctionReturn(fn_pass);
		return true;
	}
	
	// Get Log Folder - read from LogFolderTSFile
	public static String getLogsFolder() {
		printLogs("Calling getLogsFolder");
		
		Scanner s;
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			s = new Scanner(new File(LogFolderTSFile));
			
			while (s.hasNext()){
				list.add(s.next());
			}
			s.close();			
			System.out.println(list);
			
			// Print 1st element.
			printFunctionReturn(fn_pass);
			return(list.get(0));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			printFunctionReturn(fn_fail);
			return (null);
		}
	}
	
	// Function to compare two numbers
	public static boolean compareNumbers(int expectedVal, int actualValue){
		printLogs("Calling compareNumbers with values: " + expectedVal + "," + actualValue);
		
		printLogs("Comparing : " + expectedVal + " and " + actualValue);
		
		try {
			Assert.assertEquals(actualValue, expectedVal);
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("Values do NOT match");
			printError("Expected: " + expectedVal);
			printError("Actual  : " + actualValue);
			printFunctionReturn(fn_fail);			
			return false;
		}
		printLogs("Values matched");
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// Function to compare two texts
	public static boolean compareTexts(String expectedVal, String actualValue){
		printLogs("Calling compareTexts with values: " + expectedVal + "," + actualValue);
		
		boolean matched = false;
		
		printLogs("Comparing : " + expectedVal + " and " + actualValue);
		
		try {
			//Assert.assertEquals(actualValue.toLowerCase(), expectedVal.toLowerCase());
			
			if(actualValue.toLowerCase().contains((expectedVal).toLowerCase())) {
				printLogs("Strings matched.");
				matched = true;
			}
			//if(actualValue.equalsIgnoreCase(expectedVal)) {
				//printLogs("Strings matched.");
				//matched = true;
			//}
			else {
				printLogs("Strings NOT matched.");
				//matched = false;
			}
		}
		catch(Throwable t) {
			printError("Exception occurred while comparing texts.");
			printFunctionReturn(fn_fail);
			//return false;
		}
		
		if(matched) {
			printFunctionReturn(fn_pass);			
			return true;
		}
		else {
			printFunctionReturn(fn_fail);			
			return false;
		}
	}
	
	// Pause the execution for the specified number of seconds.
	public static void sleep(int seconds) {
		try{
			Thread.sleep(seconds * 1000);
		}
		catch(Exception e) {
			printError("Exception:: In sleep.");
		}
	}
	
	// Prints line to separate suites
	public static void printSuiteStartLine(String suite_name) {
		//APP_LOGS.debug("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		//APP_LOGS.debug("Starting Suite : " + suite_name);
		loggerTab_counter = 1;
		printLogs("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		printLogs("Starting Suite : " + suite_name);
	}
	
	// Prints line to separate tests
	public static void printTestStartLine(String test_name) {
		//APP_LOGS.debug("\n-----------------------------------------------------------");
		//APP_LOGS.debug("Starting Test : " + test_name);
		loggerTab_counter = 1;
		printLogs("\n-----------------------------------------------------------");
		printLogs("Starting Test : " + test_name);
	}

	// Prints line to separate parameterized tests
	public static void testParamStart() {
		//APP_LOGS.debug("------------------------------------");
		loggerTab_counter = 1;
		printLogs("\n------------------------------------");
	}
	
	// Prints suite end line
	public static void printSuiteEndLine(String suite_name) {
		printLogs("Suite Complete: " + suite_name);
		printLogs("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}
	
	// Prints test end line
	public static void printTestEndLine(String test_name) {
		printLogs("Test Complete: " + test_name);
		printLogs("-----------------------------------------------------------");
	}

	// Prints param end line
	public static void testParamEnd() {
		//printLogs("------------------------------------\n");
	}
		
	// Prints out the exception
	public static void printException(Exception e) {
		String sErrorMessage;
		String sExceptionOccurredInMethod;
		sErrorMessage = e.getMessage();
		sExceptionOccurredInMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
		
		/*System.err.println("--------------------------------------------");
		System.err.println("Method Name   : " + sExceptionOccurredInMethod);
		System.err.println("Error Message : " + sErrorMessage);
		System.err.println("--------------------------------------------");
		*/
		printLogs("ERROR:: Method Name : " + sExceptionOccurredInMethod);
		printLogs("ERROR:: Message     : " + sErrorMessage);
	}
	
	// Used for printing the methods's final status
	public static void printFunctionReturn(String statusToPrint) {
		String currentMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		printLogs(currentMethodName + ":: " + statusToPrint);
		
		if (statusToPrint.equalsIgnoreCase(fn_fail) && !htmlComment.contains(currentMethodName) && !currentMethodName.contains("getTextByXpath")) {
			appendHtmlComment("Error occurred in " + currentMethodName + " method");
		}
		
		if(currentMethodName.contains("printFunctionReturn")){
			// Do not perform loggerTab_counte--;
			return;
		}
		// Decrement the loggerTab_counter as the method is exiting.
		loggerTab_counter--;
	}
	
	// Print logs on std-out and to the log file
	public static void printLogs(String msg) {
		java.util.Date date= new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-hh:mm:ss a");
        String formattedDate = sdf.format(date);
		String printMsg = formattedDate + ": " + msg;
		String tabsBeforeMsg = "";
		
		// Get tab counter value and prepare for aligning the msg
		for (int i = 0 ; i < loggerTab_counter ; i++) {
			tabsBeforeMsg = tabsBeforeMsg + "\t";
		}
		
		// Reset the loggerTab_counter to zero in case there is any issue in setting it up.
		if (loggerTab_counter < 0) {
			loggerTab_counter = 0;
			System.out.println("--==-- Inform the framework dev team to get this fixed. --==--");
		}
		
		System.out.println(tabsBeforeMsg + printMsg);
		
		if (msg.toLowerCase().startsWith("calling")) {
			loggerTab_counter++;
		}
		
		/*if (msg.toLowerCase().contains("calling")) {
			System.out.println(printMsg);
		}
		else {
			System.out.println("\t" + printMsg);
		}*/
		
		/*
		try {
			logWriter.write(printMsg);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		//APP_LOGS.debug(printMsg);
	}
	
	// Print Error logs on std-out and to the log file
	public static void printError(String msg) {
		try {
			java.util.Date date= new java.util.Date();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-hh:mm:ss a");
	        String formattedDate = sdf.format(date);
	        String printMsg = formattedDate + ": ERROR:: " + msg;
	        String tabsBeforeMsg = "";
	        
	        // Get tab counter value and prepare for aligning the msg
			for (int i = 0 ; i < loggerTab_counter ; i++) {
				tabsBeforeMsg = tabsBeforeMsg + "\t";
			}
	        
	        System.out.println(tabsBeforeMsg + printMsg);
			captureScreenShot("error");
			appendHtmlComment(msg);
			
			// System.err.println(formattedDate + ": ERROR:: " + msg);
			//APP_LOGS.error(formattedDate + ": ERROR:: " + msg);
		} 
		catch (IOException e) {
			System.out.println("Exception occurred in printError.");
			e.printStackTrace();
		}
	}
	
	
	// int rowCount=driver.findElements(By.xpath("//table[@id='DataTable']/tbody/tr")).size();
	// int columnCount=driver.findElements(By.xpath("//table[@id='DataTable']/tbody/tr/td")).size();
	

	

	

	

	
	// Add Node : This method will add the remote node
	// 1. Select the Nodes from the Main Menu.
	// 2. Verify Page.
	// 		- Add Nodes Button 
	// 		- Message: localhost has an Ready to start inventory
	// 		- Inventory Link Presence
	// 		- Actions drop-down and options present in it
	// 
	// 3. Click on Add Node button.
	// 		Verify Buttons - Add, Start Over, Close
	// 4. Enter all the details of the remote node.
	// 		IP/DNS, Description, Type, Baseline, Credentials
	// 5. Click on Add button.
	// 6. Verify the Add Node screen is closed and control is on Nodes screen
	/*
	public static boolean addNode (String remoteHostIp, 
			   					   String remoteHostDesc,
			   					   String remoteHostType,
			   					   String remoteHostUserName1,
			   					   String remoteHostPassword1) {
		printLogs("Calling addNode");
		
		try {
			// 1. Select the Nodes from the Main Menu.
			if(!clickLinkOnMainMenu("MainMenu_Nodes")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			sleep(10);
			
			captureScreenShot();
			
			// Verify Page: Nodes
			if(!verifyPage("Nodes")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			/* 2. Verify
			 * 		- Add Nodes Button 
			 * 		- Message: localhost has an Ready to start inventory
			 * 		- Inventory Link Presence
			 * 		- Actions drop-down and options present in it
			 
			
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
			sleep(10);
			
			/*
			 * 3. Click on Add Node button.
			 * 		Verify Buttons - Add, Start Over, Close
			 * 4. Enter all the details of the remote node.
			 * 		IP/DNS, Description, Type, Baseline, Credentials
			 * 5. Click on Add button.
			 * 6. Verify the Add Node screen is closed and control is on Nodes screen
			 
			
			// 3. Click on Add Node button.
			if(!clickByXpath("Nodes_AddNodeButton")) {
				printFunctionReturn(fn_fail);				
				return false;
			}
			
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
			
			// Select BL: NodesAddSelectBaseline
			if(!NodesAddSelectBaseline()) {
				printError("NodesAddSelectBaseline failed.");
			}
			
			if(!sendKeysByXpath("Nodes_AddNodeCredentialsUsernameInput", remoteHostUserName1)) {
				printError("sendKeysByXpath Nodes_AddNodeCredentialsUsernameInput failed.");
			}
			
			if(!sendKeysByXpath("Nodes_AddNodeCredentialsPasswordInput", remoteHostPassword1)) {
				printError("sendKeysByXpath Nodes_AddNodeCredentialsPasswordInput failed.");
			}			
			
			captureScreenShot();
			
			// 5. Click on Add button.
			if(!clickByXpath("Nodes_AddNodeAddButton")) {
				printError("clickByXpath Nodes_AddNodeAddButton failed.");
			}
			
			sleep(10);
			
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
			
			captureScreenShot();
			
			// Verify Node title
			// Nodes_Title
			if(!verifyText("Nodes_Title", remoteHostIp)) {
				printError("The control did not reach the added node page by default.");
				printError("Setting the control to new added node page.");
				driver.findElement(By.linkText(remoteHostIp)).click();
				printLogs("Clicked the remote host IP: " +  remoteHostIp);
			}
			
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while selecting default Baseline on the Add Node page.");
			printFunctionReturn(fn_fail);			
			return false;
		}
		printFunctionReturn(fn_pass);		
		return true;		
	}
	*/
	
	
	
	// setPage
	public static boolean guiSetPage(String pageToSet) {
		printLogs("Calling guiSetPage with values: " + pageToSet);
		try {
			printLogs("Setting the page: " + pageToSet);
			
			switch (pageToSet) {
			
				case "BaselineLibrary" :
					CommonHpsum.clickLinkOnMainMenu("MainMenu_BaselineLibrary");
					SelUtil.checkElementPresenceByXpath("BaselineLibrary_Heading");
					break;
					
				case "AddBaseline" :
					guiSetPage("BaselineLibrary");
					SelUtil.clickByXpath("BaselineLibrary_AddBaselineButton");
					SelUtil.checkElementPresenceByXpath("BaselineLibrary_AddBaselineHeading");
					break;
					
				case "CreateCustomBaseline" :
					guiSetPage("BaselineLibrary");
					selectActionDropDownOption("CssBaselineActions", "Create Custom");
					SelUtil.checkElementPresenceByXpath("BaselineLibrary_CreateCustomBaselineHeading");
					break;
					
				case "Nodes" :
					CommonHpsum.clickLinkOnMainMenu("MainMenu_Nodes");
					SelUtil.checkElementPresenceByXpath("Nodes_Heading");
					break;
					
				case "AddNode" :
					guiSetPage("Nodes");
					SelUtil.clickByXpath("Nodes_AddNodeButton"); 
					SelUtil.checkElementPresenceByXpath("Nodes_AddNodeHeading");
					break;
					
				case "NodeGroups" :
					CommonHpsum.clickLinkOnMainMenu("MainMenu_NodeGroups");
					SelUtil.checkElementPresenceByXpath("NodeGroups_Heading");
					break;	
					
				case "Activity" :
					CommonHpsum.clickLinkOnMainMenu("MainMenu_Activity");
					SelUtil.checkElementPresenceByXpath("ActivityPage_Heading");
					break;
					
					
					
				default:
					printError("Invalid Page name provided. Please provide the supported page name.");
			}
			captureScreenShot();
		}
		catch(Throwable t) {
			appendHtmlComment("Failed to set page: " + pageToSet);
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	
	// Select option from action drop-down menu
	public static boolean selectActionDropDownOption(String cssKeyOfActionDropDown, String optionName) {
		printLogs("Calling selectActionDropDownOption with values: " + cssKeyOfActionDropDown + ", " + optionName);
		
		try {
			printLogs("Clicking on Actions Drop-down : " + cssKeyOfActionDropDown);
			driver.findElement(By.cssSelector(OR.getProperty(cssKeyOfActionDropDown))).click();
			sleep(2);
			
			captureScreenShot();
			
			// Click on the linkText.
			printLogs("Selecting the option from drop-down.");
			driver.findElement(By.linkText(optionName)).click();
			sleep(3);
			
			printLogs("Successfully selected option from Actions drop-down: " + optionName);
			captureScreenShot();
			printFunctionReturn(fn_pass);
			return true;
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while selecting.");
			printFunctionReturn(fn_fail);			
			return false;
		}
	}		
	
	// captureScreenShot
	public static void captureScreenShot() throws IOException{
		sleep(2);
		screenshot_counter++;						// screenshot_counter is a Global variable
		String counter ="";
		
		if (screenshot_counter <= 9) {
			counter = "00" + screenshot_counter;
		}
		else if (screenshot_counter > 9 && screenshot_counter < 99) {
			counter = "0" + screenshot_counter;
		}
		
		String screenShotExtn = ".png";
		String filename = screenshot_name;			// screenshot_name is a Global variable
		filename = filename + "_" + counter;
		String screenShotFile = screenShotFolder + filename + screenShotExtn;
		
		printLogs("Capturing ScreenShot : " + filename);
		printLogs("ScreenShot : " + screenShotFile);
		
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	    FileUtils.copyFile(scrFile, new File(screenShotFile));
	    sleep(2);
	}
	
	// captureScreenShot: this version of captureScreenShot appends suffix to the filename.
	// Right now used in printError to append 'error' at the end of the screenshot names.
	public static void captureScreenShot(String suffix) throws IOException{
		sleep(2);
		screenshot_counter++;						// screenshot_counter is a Global variable
		String counter ="";
		
		if (screenshot_counter < 9) {
			counter = "00" + screenshot_counter;
		}
		else if (screenshot_counter > 9 && screenshot_counter < 99) {
			counter = "0" + screenshot_counter;
		}
		
		String screenShotExtn = ".png";
		String filename = screenshot_name;			// screenshot_name is a Global variable
		filename = filename + "_" + counter + "_" + suffix;
		String screenShotFile = screenShotFolder + filename + screenShotExtn;		// screenShotFolder is a Global variable
		
		printLogs("Capturing ScreenShot : " + filename);
		printLogs("ScreenShot : " + screenShotFile);
		appendHtmlComment("(" + filename + ") ");			// Don't change this format. Check appendHtmlComment 
		
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	    FileUtils.copyFile(scrFile, new File(screenShotFile));
	    sleep(2);
	}
	
	// Run the script.
	// executeScript: Options from CONFIG.properties: autoItDeployPopUpScript, killAutoItDeployPopUpScript
	public static boolean executeScript(String scriptToExecute, boolean useConfigFile) {
		printLogs("Calling executeScript with values: " + scriptToExecute + ", " + useConfigFile);
		
		printLogs("Executing the script: " + scriptToExecute);
		
		String scriptWithPath = CONFIG.getProperty(scriptToExecute);
		
		printLogs("Script with absolute path: " + scriptWithPath);
		
		try {
			Runtime.getRuntime().exec(scriptWithPath);
		} 
		catch (IOException e) {
			printError("Exception occurred while executing the script.");
			e.printStackTrace();
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// getExecuteCommandOutput : executes the command 
/*	public static BufferedReader getExecuteCommandOutput(String command_exec) {
		//printLogs("Calling getExecuteCommandOutput with values: " + command_exec);
		try {
			String utilForCommandExec = null;
			if (currentOsName.equalsIgnoreCase("windows")) {
				utilForCommandExec = "C:\\automation_gui\\gui_automation\\src\\gui\\common\\util\\executeCommands.bat ";
			}
			
			printLogs("Executing command: " + command_exec);
			Process proc = Runtime.getRuntime().exec(utilForCommandExec + command_exec);
			printLogs("Executed the command:"  + command_exec);
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
			printFunctionReturn(fn_pass);
			return(stdInput);
		}
		catch (IOException e) {
			printError("Exception occurred while executing the command:" + command_exec);
			printFunctionReturn(fn_fail);
			e.printStackTrace();
			return (null);
		}
	}*/
	
	// getHpsumVerBuild: Returns the HPSUM version and build.
	public static String[] getHpsumVerBuild() {
		printLogs("Calling getHpsumVerBuild");
		
		String hpSumLocation = currentSppLocation + "/packages";
		String [] hpsumVerBuild = new String [2];
		String hpsumFileNameExtn = null;
		String hpsumFileName = null;
		String command_exec;
		
		try {
			if (currentOsName.equalsIgnoreCase("windows")) {
				command_exec = "dir /o-d /b sum*_b*.txt"; //dir /o-d /b sum*.txt
			}
			else {
				command_exec = "ls -1t sum*_b*.txt | head -1"; //ls -1t sum*.txt | head -1 
			}
			
			hpsumFileNameExtn = executeCommandReturnOutput(command_exec, hpSumLocation);
			
			hpsumFileName = hpsumFileNameExtn.split("\\.")[0];
			hpsumVerBuild = hpsumFileName.split("\\_");
			
			//hpsumVerBuild[0] = hpsum version
			//hpsumVerBuild[1] = hpsum build
			
			printLogs("SUM -> SUM Version: " + hpsumVerBuild[0] + " - " + "SUM Build: " + hpsumVerBuild[1]);
		}		
		catch (Exception e) {
			printError("Exception occurred while getting the SUM Version and Build.");
			e.printStackTrace();
			printFunctionReturn(fn_fail);
			return (null);
		}
		printFunctionReturn(fn_pass);
		return hpsumVerBuild;
	}
	
	// getSppVerBuild: Returns the SPP version and build
	public static String[] getSppVerBuild() {
		printLogs("Calling getSppVerBuild");
		
		String sppLocationFileName = currentSppLocation + "/system";
				
		String [] sppVerBuild = new String [2];
		String sppFileNameExtn ;
		String []sppFileName = new String [3];
		String command_exec;
		
		try{
			if (currentOsName.equalsIgnoreCase("windows")) {
				command_exec = "dir /b  SPP*";
			}
			else {
				command_exec = "ls -1t SPP*";
			}
			
			sppFileNameExtn = executeCommandReturnOutput(command_exec, sppLocationFileName);
			sppFileName = sppFileNameExtn.split("\\.");
			sppVerBuild[0] = sppFileName[0] + sppFileName[1];
			sppVerBuild[1] = sppFileName[2];
						
			//sppVerBuild[0] = spp version
			//sppVerBuild[1] = spp build
			
			printLogs("SPP -> SPP Version: " + sppVerBuild[0] + " - " + "SPP Build: " + sppVerBuild[1]);
		}		
		catch (Exception e) {
			printError("Exception occurred while getting the SPP Version and Build.");
			e.printStackTrace();
			printFunctionReturn(fn_fail);
			return (null);
		}
		printFunctionReturn(fn_pass);
		return sppVerBuild;
	}
	
	// executeCommand
	public static boolean executeCommand(String command_exec) {
		printLogs("Calling executeCommand with values: " + command_exec);
		
		try {
			//BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			printLogs("Executing command: " + command_exec);
			Runtime.getRuntime().exec(command_exec);
			printLogs("Executed the command:"  + command_exec);
		}
		catch (IOException e) {
			printError("Exception occurred while executing the command:" + command_exec);
			e.printStackTrace();
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}

	// executeCommandReturnOutput
	public static String executeCommandReturnOutput(String command, String startDir) {
		printLogs("Calling executeCommandStoreOutput with values: " + command + ", " + startDir);
		
		String[] commandArray = new String[3];
		//String filename = "automationCommandOutput";
		//String outputFile = presentWorkingDir + "/" + logsFolder + "/" + filename;
		
		try {
			printLogs("Executing command: " + command);
			
			if (currentOsName.equalsIgnoreCase("windows")) {				
				commandArray[0] = "cmd";
				commandArray[1] = "/c";
				commandArray[2] = command;
			}
			else {
				commandArray[0] = "/bin/sh";
				commandArray[1] = "-c";
				commandArray[2] = command;
			}
			
			Runtime runtime = Runtime.getRuntime();
	        Process process = runtime.exec(commandArray, null, new File(startDir));
	        process.waitFor();
	        
			printLogs("Executed the command: "  + command + " from the directory: " + startDir);
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String commandOutput = stdInput.readLine();
			stdInput.close();
			printFunctionReturn(fn_pass);
			return (commandOutput);
		}
		catch (IOException | InterruptedException e) {
			printError("Exception occurred while executing the command:" + command);
			e.printStackTrace();
			printFunctionReturn(fn_fail);
			return("error");
		}
	}

	// appendHtmlComment
	// This appends the message which will be sent in the email 
	public static boolean appendHtmlComment(String htmlMsg) {
		printLogs("Calling appendHtmlComment with value: " + htmlMsg);
		
		try {
			if (htmlMsg.endsWith("_error) ")) {
				htmlComment = htmlComment.concat(htmlMsg);
			}
			else {
				htmlComment = htmlComment.concat(htmlMsg + "<br>");
			}
			//printLogs("Comment now contains: " + htmlComment);
		}
		catch (Exception e) {
			printError("Exception occurred in appending html comment");
			printFunctionReturn(fn_fail);
			return false;
		}
		printFunctionReturn(fn_pass);
		return true;
	}
	
	// helpWindowHeadingCompare:
	// Go to help page - change the handle to help page
	// Read the heading of the newly opened page
	// Compare the heading with the standard
	// Close the newly opened window
	// Bring the handle back to original window - HP SUM
	public static boolean helpWindowHeadingCompare(String headingToCompare) {
		printLogs("Calling helpWindowHeadingCompare with values: " + headingToCompare);
		
		String originalHeadingText = "";
		String mainWinId = "";
		
		try {
			Set<String> winIds = driver.getWindowHandles();
			printLogs("Current number of windows = " + winIds.size());

			Iterator<String> iter = winIds.iterator();
			mainWinId = iter.next();
			String tabWinId = iter.next();
			
			printLogs("Main-id:" + mainWinId);
			printLogs("Tab-id:" + tabWinId);
			
			driver.switchTo().window(tabWinId);
			printLogs("Switched to tab window");
			
			// Get the list of all the frames
		    /*List<WebElement> ele = driver.findElements(By.tagName("frame"));
		    System.out.println("Number of frames in a page :" + ele.size());
		    for(WebElement el : ele){
		      //Returns the Id of a frame.
		        System.out.println("Frame Id :" + el.getAttribute("id"));
		      //Returns the Name of a frame.
		        System.out.println("Frame name :" + el.getAttribute("name"));
		    }*/
			
			// Get the heading text
		    driver.switchTo().frame("mainhelp_pane");
			originalHeadingText = SelUtil.getTextByXpath("HelpPage_HeadingText");

			// Take screenshot
			captureScreenShot();
			
			// Compare
			if(!compareTexts(headingToCompare, originalHeadingText)) {
				printError("Help heading text did not match. Expected heading: " + headingToCompare + " Actual heading: " + originalHeadingText);
				printFunctionReturn(fn_fail);
				return false;
			}
		}
		catch (Exception e) {
			printError("Exception occurred while getting heading text from help window.");
			printFunctionReturn(fn_fail);
			return false;
		}
		finally {
			// close current window
			driver.close();
			
			// Bring control back to main window
			driver.switchTo().window(mainWinId);
		}
		
		printFunctionReturn(fn_pass);
		return true;
	}
	//Author: Praveen
	//Moved these methods to reports class.
	//reportsGenerationCheck
	//Check whether Report is generated correctly 
	//ReportingStage: At what stage report is generated (BaselineInventoryReport,NodeInventoryReport,NodeDeployReport)
	/*public static boolean reportsGenerationCheck(String reportingStage) {
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
			if(!SelUtil.checkElementPresenceByXpath("Common_NewWindowHeading")){
			    printError("Report page is not present.");
			    printFunctionReturn(fn_fail);
			    return false;
			}
			captureScreenShot();
			    	
			//click on html in report format
			if(!SelUtil.clickByXpath("Reports_ReportFrmat_HtmlRadioButton")){
				printError("HTML is not selected as report format.");
			}
			captureScreenShot();
					
			//click on generate button
			if(!SelUtil.clickByXpath("Reports_GenerateButton")){
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
			if(!SelUtil.clickByXpath("Reports_ReportFrmat_XmlRadioButton")){
				printError("XML is not selected as report format.");
			}
			captureScreenShot();
					
			//click on generate button
			if(!SelUtil.clickByXpath("Reports_GenerateButton")){
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
			if(!SelUtil.clickByXpath("Reports_ReportFrmat_CsvRadioButton")){
				printError("CSV is not selected as report format.");
			}
			captureScreenShot();
					
			//click on generate button
			if(!SelUtil.clickByXpath("Reports_GenerateButton")){
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
			if(!SelUtil.clickByXpath("Reports_CloseButton")){
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
	}*/
	
	// activeElementPresence
	// Check for the presence of the active element
	// Element: Name of the active element 
	public static boolean activeElementPresence(String element) {
		printLogs("Calling ActiveElementPresence with values: " + element);
		
		String activeElement = "";
		boolean value=true;
		try {	
		    printLogs("Waiting for " + element + " to disappear.");
		    	
		    while(value){
		    	// Switch to active element
		    	activeElement = driver.switchTo().activeElement().getText();
		    	
		    	// Set to true till active element is present
		    	if(activeElement.contains(element)){
		    		value=true;
		    		sleep(2);
		    		continue;
		    	}
		    	else{
		    		value=false;
		    	}
		    }
		    driver.switchTo().defaultContent();
		    printFunctionReturn(fn_pass);
		    return true;
		}
		catch(Throwable t){
			printError("Error occurred while waiting for active element to disappear.");
		    printFunctionReturn(fn_fail);
		    return false;
		}
	}
	
	// Check whether link in the action drop down is enabled/disabled.
	public static boolean checkActionLinkEnabled(String optionName) {
		printLogs("Calling checkActionLinkEnabled with values: " + optionName);
		
		String actionLinkClassAttr = "";
			
		try {	
			// Click on Action Drop Down
			if(!SelUtil.clickByXpath("Nodes_ActionsDropdownLabel")){
				printError("Unable to click on Action label.");
				printFunctionReturn(fn_fail);
				return false;
			}
				
			captureScreenShot();
				
			actionLinkClassAttr = driver.findElement(By.linkText(optionName)).getAttribute("class");
				
			// Check whether the option is disabled in action drop down
	         if(actionLinkClassAttr.contains("hp-disabled")){
	         	printLogs(optionName + " option is not enabled in action drop down");
					
				// Click on Action Drop Down
				if(!SelUtil.clickByXpath("Nodes_ActionsDropdownLabel")){
					printError("Unable to click on Action label.");
					printFunctionReturn(fn_fail);
					return false;
				}
				printFunctionReturn(fn_pass);
	         	return false;
	         }
	         else{
	         	printLogs(optionName + " option is enabled in action drop down.");
					
				// Click on Action Drop Down
				if(!SelUtil.clickByXpath("Nodes_ActionsDropdownLabel")){
					printError("Unable to click on Action label.");
					printFunctionReturn(fn_fail);
					return false;
				}
				printFunctionReturn(fn_pass);
	         	return true;
	         }	
		}
		catch(Throwable t) {
			printError("Error occurred while checking for action link enabled/disabled.");
			printFunctionReturn(fn_fail);
			return false;
		}
	}
	
	//
	// Method Name: createAblFolder
	// This method will create a folder and
	// a. Put 'x' (noOfCompsToPutInEachFolder) components of type compType in that folder
	// Valid compType = exe, rpm, scexe, zip 
	// mix (NOT-Supported now - will select a type of exe,rpm etc depending on noOfCompsToPutInEachFolder)
	// 	
	// will return the path of folders created. 
	// In windows: C:\SPP\<Automation-ABL>
	// In Linux  : /root/Desktop/SPP/<Automation-ABL>
	// 
	public static String createAblFolder(int noOfCompsToPutInAblFolder, String compType) {
		printLogs("Calling createAblFolder with values : " + noOfCompsToPutInAblFolder + ", " + compType);
		
		sleep(1);		// Added this to avoid same folders created again.
		
		String outputLocation = "";			// Output location will have timestamp appended.
		
		try {
			compType = compType.replace("*", "");
			
			if (!compType.toLowerCase().contains("exe") && !compType.toLowerCase().contains("rpm") &&
					!compType.toLowerCase().contains("scexe") && !compType.toLowerCase().contains("zip")) {
				printLogs("WARNING: Valid compType for ABL folders should be exe, rpm, scexe or zip. Passed value: " + compType);
			}
			
			// Get current OS
			if(currentOsName.contains("windows")) {
				outputLocation = "C:/SPP/Automation-ABL";
			}
			else {
				outputLocation = "/root/Desktop/SPP/Automation-ABL";
			}
			
			// Add the current TimeStamp in the output location
			outputLocation = outputLocation + "_" + getTimeStamp();
			
			File newAblDir = new File(outputLocation);
			
			if (newAblDir.exists()) {
			   printLogs("Deleting old : " + newAblDir);
			   FileUtils.deleteDirectory(newAblDir);
			}
			
			// Create the ABL folder
			createFolder(outputLocation);
			
			// Current SPP components path from where components will be picked up
			String currentSppCompPath = currentSppLocation + "/packages";
			
			// Get the list of all the SPP components
			ArrayList<String> list = CommonHpsum.getFilesListInFolder(currentSppCompPath, compType);
			
			String compName = "";
			
			if (noOfCompsToPutInAblFolder > list.size()) {
				printError("Number of type components in SPP:" + list.size() + 
						" is less that the number of components asked for:"+noOfCompsToPutInAblFolder);
				printError("Failed to create ABL folder with required number of components");
				printFunctionReturn(fn_fail);
				return (null);
			}
			
			// Copy the components in the ABL folder
			for (int i = 0 ; i < noOfCompsToPutInAblFolder ; i++) {
				compName = list.get(i);
				FileUtils.copyFileToDirectory(new File(currentSppCompPath + "/" + compName), new File(outputLocation));
				printLogs("Copied " + compName);
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while creating ABL folders.");
			printFunctionReturn(fn_fail);
			return (null);
		}
		printFunctionReturn(fn_pass);
		return (outputLocation);
	}
	
	// getFileNameFromPath
	// This returns the file/folder name from the path
	public static String getFileNameFromPath(String completeFilePath) {
		printLogs("Calling getFileNameFromPath with values : " + completeFilePath);
		
		String fileName = "";
		
		try {
			fileName = new File(completeFilePath).getName();
			printLogs("File Name = " + fileName);
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred while getting file name from path.");
			printFunctionReturn(fn_fail);
			return(null);
		}
		printFunctionReturn(fn_pass);
		return(fileName);
	}

	// performTestEnv
	public static boolean performTestEnv(){
		printLogs("Performing test setup");
		String sTest;
		Xls_Reader suite_xl;
		try {
			HashMap<String, Object> TestEnv = Record.getRecord("Test");		
			sTest = TestEnv.get("test_name").toString();
			suite_xl = (Xls_Reader)TestEnv.get("suite_xl");
			printTestStartLine(sTest);
			
			// Creating email template for the test.
			if(!testInitialize(sTest)) {
				printError("Failed to perform test initialize.");		
			}					
			printLogs("Checking Runmode of the test.");
			if(!TestUtil.isTestCaseRunnable(suite_xl, sTest)) {
				printLogs("WARNING:: Runmode of the test '" + sTest + "' set to 'N'. So Skipping the test.");
				TestEnv.put("skip",true);
				throw new SkipException("WARNING:: Runmode of the test '" + sTest + "' set to 'N'. So Skipping the test.");
			}
			printLogs("RunMode found as Y. Starting the test.");		
			
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred: " + t.getMessage());			
			return false;
			
		}
		return true;
		
	}
	// gatherTestResult
	public static void gatherTestResult(){
		try {
			
			HashMap<String, Object> TestEnv = Record.getRecord("Test");	
			printLogs("Gathering test result details:");
			if ((boolean) TestEnv.get("skip")) {
				TestEnv.put("test_result", "SKIP");
			}
			else if ((boolean) TestEnv.get("manual")) {
				TestEnv.put("test_result", "MANUAL");
			}
			else {
				TestEnv.put("test_result", "FAIL");				
			}
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred:" + t.getMessage());
			
			
		}
		
	}
	
	//performCleanup
	public static void performCleanup(){
		String sTest;	
		String executionTime = "";
		String test_result = "PASS";
		
		long initialTime;
		long finalTime;	
		
		Xls_Reader suite_xl;
		
		try {
			
			HashMap<String, Object> TestEnv = Record.getRecord("Test");	
			printLogs("Test Cleanup with arguments:");
			printLogs("" + TestEnv);
			
			// Get current time in ms.
			finalTime = getTimeInMsec();
			initialTime = Record.getTime();
			sTest = (String)TestEnv.get("test_name");
			test_result = (String) TestEnv.get("test_result");
			suite_xl = (Xls_Reader)TestEnv.get("suite_xl");
			
			// Calculate test time interval.
			executionTime = calculateTimeInterval(initialTime, finalTime);
			
			// Write the final test result in html file.
			CommonHpsum.testCleanupAndReporting(sTest, test_result, executionTime);
			TestUtil.reportDataSetResult(suite_xl, "TestCases", TestUtil.getRowNum(suite_xl, sTest), test_result);
			printTestEndLine(sTest);
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);			
			printError("Exception occurred: " + t.getMessage());			
			
		}
		
		}
}