package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class TestConfig {

	public static void main(String[] args) throws FileNotFoundException {
		
		// Retrieve values from config file		
		Properties CONFIG = new Properties();
		FileInputStream ip_config = new FileInputStream(System.getProperty("user.dir") + "/src/gui/common/config/config.properties");
		
		try {
			CONFIG.load(ip_config);
		} catch (Throwable t) {
			System.out.println("ERROR: Failed to load the config file");
		}
		
		System.out.println(CONFIG.getProperty("screenShotPath"));
		
		// Retrieve values from OR file		
		Properties OR = new Properties();
		FileInputStream ip_or = new FileInputStream(System.getProperty("user.dir") + "/src/gui/common/config/OR.properties");
		
		try {
			OR.load(ip_or);
		} catch (Throwable t) {
			System.out.println("ERROR: Failed to load the OR file");
		}
		
		System.out.println(OR.getProperty("xPathTemp"));
		
		
		

	}

}
