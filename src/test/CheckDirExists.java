package test;

import java.io.File;

public class CheckDirExists {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File dir = new File("C:\\AAP");
		boolean exists = dir.exists();
		System.out.println("Directory " + dir.getPath() + " exists: " + exists);

	}

}
