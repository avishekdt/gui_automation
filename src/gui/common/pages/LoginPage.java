
package gui.common.pages;

import gui.common.pages.elements.*;

public class LoginPage {
	// This class is for all operations on login screen.
	
	public  Button LoginButton;
	public  TextField LoginUsername;
	public  TextField LoginPassword;

	// Declare all the items on the screen	
	public LoginPage(){
		LoginButton = new Button("LoginPage_LoginButton");	
		LoginUsername = new TextField("LoginPage_Username");
		LoginPassword = new TextField("LoginPage_Password");
	}
	
	
}

