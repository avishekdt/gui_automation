package test;

import gui.common.util.Zip;

public class TestZip {

	public static void main(String[] args) {
		try {
			Zip.zipFolder("C:/automation_gui/gui_automation/src/gui/logs", "c:/myzippedFolder.zip");
		} catch (Throwable t) {
			//t.printStackTrace();
			System.out.println("ERROR:: Failed to zip the folder.");
		}
	}

}
