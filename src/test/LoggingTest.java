package test;

import org.apache.log4j.Logger;

public class LoggingTest {

	public static void main(String[] args) {
		Logger APP_LOGS = Logger.getLogger("hpsumLogger");
		System.out.println(System.getProperty("java.class.path"));
		APP_LOGS.debug("Debug");
		APP_LOGS.info("Info");
		APP_LOGS.warn("Warn");
		APP_LOGS.error("Error");
		APP_LOGS.fatal("Fatal");
		

	}

}
