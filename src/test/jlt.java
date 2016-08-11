package test;

import java.util.Properties;

public class jlt {
	public static long startTime = 0;
	//public static long stopTime = 0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//System.out.println(System.getProperty("os.arch"));
		//Properties p = System.getProperties();
		//p.list(System.out);


		startWatch();
		try{Thread.sleep(3000);
		}catch(Exception e) {}
		stopWatch();
		
	}
	
	public static void startWatch() {
		startTime = System.currentTimeMillis();
	}
	
	public static void stopWatch() {
		String callingMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
		long time = (System.currentTimeMillis() - startTime)/1000;
		startTime = 0;
		System.out.println("Execution Time for method: " + callingMethod + "() = "+ time + "s");
	}

}
