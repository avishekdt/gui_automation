package gui.common.util;

import java.text.SimpleDateFormat;

import gui.common.base.TestBase;


public class TestUtil {
	public static long startTime = 0;
	
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
	
	
}

