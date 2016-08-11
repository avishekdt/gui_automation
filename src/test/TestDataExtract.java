package test;

import gui.common.util.Xls_Reader;

public class TestDataExtract {

	public static void main(String[] args) {
		
		Xls_Reader x = new Xls_Reader(System.getProperty("user.dir") + "/src/gui/suites/MAT.xlsx");
		getData(x, "GU_Interactive");

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
	

}
