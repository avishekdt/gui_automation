package test;

import java.io.File;

public class CreateLogFolder {

	public static void main(String[] args) {
		//String timeStamp = "AJAY";
		//String logsFolder = "logs/" + timeStamp;
		String logsFolder = "AJAY/AMIT";
		
		String presentWorkingDir = System.getProperty("user.dir");
		System.out.println("Present Working Dir = " + presentWorkingDir);
		
		System.out.println("Creating Logs Folder: " + logsFolder);
		boolean success = (new File(logsFolder)).mkdirs();
		
		if (!success) {
		    System.out.println("ERROR:: Failed to create logs folder: " + logsFolder);
		    
		}
		else {
			System.out.println("LOGS FOLDER:: " + logsFolder);
		}
		

	}

}
