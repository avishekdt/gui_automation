package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
 
public class ReadInputFile {
	public static String LogFolderTSFile = "C:/automation_gui/gui_automation/LogFolderTimeStamp.txt";
 
	public static void main(String[] args) {		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(LogFolderTSFile));
			String sCurrentLine;
 
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		//close(br);
 
	}
}
