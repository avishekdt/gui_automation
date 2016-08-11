package gui.common.record;

import java.util.HashMap;

import gui.common.base.TestBase;
import gui.common.util.*;



public class Record {
	
	public void setRecord(HashMap<String, Object> H,String sPage){
		printLogs("Set Record for: " + sPage);
		printLogs("With arguments: " + H);
		try
		{
			switch(sPage){			
			case "Test":			
				new TestEnv().setRecord(H);
				printLogs("Set Test Record");
				TestEnv.CurrentHashMap = H;	
				printLogs("Set current Record of TestEnv");
				break;
			case "GU":			
				new GuidedUpdateRecord().setRecord(H);
				GuidedUpdateRecord.CurrentHashMap = H;
				break;
			default:
				printLogs("Invalid sPage argument");
				break;
			}
		}
		catch(Throwable t){
			printLogs(t.getMessage());
		}
	}
	
	public static long getTime(){
		return TestEnv.initialTime;
	}
	public static void printLogs(String msg) {
		TestBase.printLogs(msg);
		
	}

	public static HashMap<String, Object> getRecord(String sPage){
		printLogs("Set Record for: " + sPage);		
		try
		{
			switch(sPage){			
			case "Test":			
				return TestEnv.CurrentHashMap;
			case "GU":			
				return GuidedUpdateRecord.CurrentHashMap;
			}
		}
		catch(Throwable t){
			printLogs(t.getMessage());
		}
		return null;
		
	}
}

class GuidedUpdateRecord extends Record{
	
	// Guided Update
	static String[] saXPath;
	static String[] sFailedDependency;
	static String[] sRowData;
	static String[] sColData;
	static int iRowCount;
	static int iColCount;
	public static HashMap<String, Object>CurrentHashMap = null;
	
	public void setRecord(HashMap<String, Object> H){
		saXPath = (String[])H.get("saXPath");
		sFailedDependency = (String[])H.get("sFailedDependency");
		sRowData = (String[]) H.get("sRowData");
		sColData = (String[]) H.get("sColData");
		iRowCount = (int) H.get("iRowCount");
		iColCount = (int) H.get("iColCount");
		CurrentHashMap = H;
	}
	
}

class TestEnv extends Record{
	static Xls_Reader suite_xl;
	static String test_name = "";
	static String test_result = "PASS";
	
	static long initialTime;
	static long finalTime;
	static String executionTime 	= "";
	
	static boolean manual 	= true;			
	static boolean skip 	= false;
	static boolean fail 	= false;
	
	public static HashMap<String, Object>CurrentHashMap = null;
	
	public void setRecord(HashMap<String, Object> H){
		try
		{
			suite_xl = (Xls_Reader)H.get("suite_xl");
			test_name = (String) H.get("test_name");
			test_result = (String) H.get("test_result");						
			executionTime = (String) H.get("executionTime");
			manual = (boolean) H.get("manual");
			skip = (boolean) H.get("skip");
			fail = (boolean) H.get("fail");
			initialTime = getTimeInMsec();
			
		}
		catch(Throwable t){
			printLogs(t.getMessage());
		}		
	}

	private long getTimeInMsec() {
		return TestBase.getTimeInMsec();		
	}
	
	
}

