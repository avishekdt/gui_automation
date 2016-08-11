package gui.tests.WAT;

import java.io.FileNotFoundException;

import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import gui.common.base.TestBase;
import gui.common.util.TestUtil;
import gui.common.util.Xls_Reader;

public class TestSuiteBase extends TestBase {
	String suite_name = "WAT";
	Xls_Reader suite_xl = new Xls_Reader(System.getProperty("user.dir") + "/src/gui/suites/WAT.xlsx");
	
	// Check if the suite has to be skipped.
	@BeforeSuite
	public void checkSuiteSkip() {		
		try {
			initialize();			
		} 
		catch (FileNotFoundException e) {
			printException(e);
		}
		
		printSuiteStartLine(suite_name);
		
		printLogs("Checking Runmode of the suite.");
		
		if(!TestUtil.isSuiteRunnable(TestSuitesXls, suite_name)) {
			printLogs("WARNING:: Runmode of the suite '" + suite_name + "' set to 'N'. So Skipping all the tests in this.");			// Appears in Logs
			throw new SkipException("WARNING:: Runmode of the suite '" + suite_name + "' set to 'N'. So Skipping all the tests in this.");	// Appears in reports
		}
	}
}
