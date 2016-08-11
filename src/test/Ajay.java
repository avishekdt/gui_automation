package test;

import gui.common.base.BaselineLibrary;
import gui.common.base.CommonHpsum;
import gui.common.base.TestBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ajay {
	public static void main(String[] args) throws IOException {
		System.out.println("------- Test Started : Ajay -----------");
		
	
		// Close the browser and end execution.
		//CommonHpsum.closeBrowser();
		//CommonHpsum.killHpsumService();
		
		
		String str = "15.154.115.52,15.154.115.192";
		
		List<String> NodeList = Arrays.asList(str.split(","));
		for(int i = 0; i < NodeList.size(); i++) {
            System.out.println(NodeList.get(i));
        }
     }
}
