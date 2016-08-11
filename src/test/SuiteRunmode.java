package test;

import gui.common.util.Xls_Reader;

public class SuiteRunmode {

	public static void main(String[] args) {
		//System.out.println(System.getProperty("user.dir"));
		Xls_Reader x = new Xls_Reader(System.getProperty("user.dir") + "/src/gui/suites/TestSuites.xlsx");
		System.out.println(isSuiteRunnable(x, "MAT"));
		System.out.println(isSuiteRunnable(x, "WAT"));

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

}
