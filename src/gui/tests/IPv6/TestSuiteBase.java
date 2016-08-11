package gui.tests.IPv6;

import java.io.FileNotFoundException;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import gui.common.base.TestBase;
import gui.common.util.TestUtil;
import gui.common.util.Xls_Reader;

public class TestSuiteBase extends TestBase {
	String suite_name = "IPv6";
	Xls_Reader suite_xl = new Xls_Reader(System.getProperty("user.dir") + "/src/gui/suites/" + suite_name + ".xlsx");
	
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
		
		//APP_LOGS.debug("Checking Runmode of the suite.");
		printLogs("Checking Runmode of the suite.");
		
		if (!TestUtil.isSuiteRunnable(TestSuitesXls, suite_name)) {
			printLogs("WARNING:: Runmode of the suite '" + suite_name + "' set to 'N'. So Skipping all the tests in this.");			// Appears in Logs
			//APP_LOGS.debug("WARNING:: Runmode of the suite '" + suite_name + "' set to 'N'. So Skipping all the tests in this.");			// Appears in Logs
			throw new SkipException("WARNING:: Runmode of the suite '" + suite_name + "' set to 'N'. So Skipping all the tests in this.");	// Appears in reports
		}
		
		globalList.add("Suite Name: " + suite_name);
		globalList.add("Suite Start TimeStamp: " + getTimeStamp());
	}
	
	@AfterSuite
	public void suiteCleanup() {
		printLogs("Suite Cleanup:");
		
		// Copy the xls suite file to logs folder.
		copyXlsToLogs(suite_name);
		
		globalList.add("Suite End TimeStamp: " + getTimeStamp());
		
		// Print the global list.
		for(int i = 0; i < globalList.size(); i++) {  
			printLogs(globalList.get(i));
		}
		printSuiteEndLine(suite_name);
	}
}
