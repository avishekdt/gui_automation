package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFileInArrayList {
	public static String LogFolderTSFile = "C:/automation_gui/gui_automation/LogFolderTimeStamp.txt";
	
	public static void main(String[] args) {
		Scanner s;
		
		try {
			s = new Scanner(new File(LogFolderTSFile));
			
			ArrayList<String> list = new ArrayList<String>();
			while (s.hasNext()){
			    list.add(s.next());
			}
			s.close();
			
			System.out.println(list);
			
			// Print 1st element.
			System.out.println(list.get(0));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
