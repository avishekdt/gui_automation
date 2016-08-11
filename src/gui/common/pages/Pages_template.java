package gui.common.pages;

public class Pages_template {
	private static Pages_template singleton= null;
	
	   private Pages_template(){
		   
	   }
	   
	   public static Pages_template getInstance(){
		   if(singleton == null)
		   {
			   singleton = new Pages_template();
			   return singleton;
		   }else{
			   return singleton;
		   }
	   }
}
