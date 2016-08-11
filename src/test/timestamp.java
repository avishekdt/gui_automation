package test;
import java.text.SimpleDateFormat;

public class timestamp {
	public static void main(String[] args) {
		java.util.Date date= new java.util.Date();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-hh:mm:ss a");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);
	

	}

}
