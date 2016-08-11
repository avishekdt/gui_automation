package test;

import gui.common.util.Xls_Reader;

public class TestRunmode {

	public static void main(String[] args) {
		
		Xls_Reader x = new Xls_Reader(System.getProperty("user.dir") + "/src/gui/suites/MAT.xlsx");
		System.out.println(isTestCaseRunnable(x, "GU_Interactive"));
		System.out.println(isTestCaseRunnable(x, "LocalDeploy_L"));
		

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
	

}
