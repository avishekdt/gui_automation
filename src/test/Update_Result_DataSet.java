package test;

import gui.common.util.Xls_Reader;

public class Update_Result_DataSet {

	public static void main(String[] args) {
		Xls_Reader x = new Xls_Reader(System.getProperty("user.dir") + "/src/gui/suites/MAT.xlsx");
		reportDataSetResult(x, "GU_Interactive", 2, "Pass");
		reportDataSetResult(x, "GU_Interactive", 2, "Fail");

	}
	
	
	// Update results for a particular data set.
	public static void reportDataSetResult(Xls_Reader xls, String testCaseName, int rowNum, String result) {
		
		xls.setCellData(testCaseName, "Result", rowNum, result);
		
	}

}
