package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class createLogFile {


	public static void main(String[] args) {
		
		BufferedWriter writer = null;
		
		
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File logFile = new File(timeLog + ".log");

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("Hello world!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
		
		/*
		String presentWorkingDir = System.getProperty("user.dir");
		System.out.println("Present Working Dir = " + presentWorkingDir);
		
		try {
			//create a file named "testfile.txt" in the current working directory
			File myFile = new File("AJAY/AMIT/testfile.txt");
			if ( myFile.createNewFile() ) {
				System.out.println("Success!");
			} 
			else {
				System.out.println("Failure!");
			}
		}
		catch (IOException ioe) { 
			ioe.printStackTrace(); 
		}
		
		*/
	}
	
	

}
