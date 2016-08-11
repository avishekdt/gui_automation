package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class logBatFileData {
	
	public static void main(String[] args) {
		create_file();
		print_first();

	}
	
	public static void create_file () {
		System.out.println("Creating file.");
		
		String text = "Hello world\n";
        try {
        	File file = new File("example.txt");
        	BufferedWriter output = new BufferedWriter(new FileWriter(file));
        	output.write(text + "\n");
        	output.close();
        } 
        catch ( IOException e ) {
           e.printStackTrace();
        }
	}
	
	public static void print_first () {
		System.out.println("Writing first line");
		
		try {
		    String filename= "example.txt";
		    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
		    fw.write("add a line\n");//appends the string to the file
		    fw.close();
		}
		catch(IOException ioe) {
		    System.err.println("IOException: " + ioe.getMessage());
		}
	}

}
