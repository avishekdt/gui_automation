package gui.common.base;

import java.io.IOException;
import gui.common.util.ErrorUtil;
import gui.common.util.TestUtil;

public class Login extends SelUtil{
	
	/*
	 * METHODS
	 * login
	 */
	

	// Login
	public static boolean login() {
		printLogs("Calling login");
		
		try {
			printLogs("Attempting login");
			
			String osName = "";
			String loginUserName = "";
			String loginPassword = "";
			
			osName = TestUtil.getOsName();
			
			if(osName.equalsIgnoreCase("Windows")) {
				loginUserName = CONFIG.getProperty("loginUserName_w");
				loginPassword = CONFIG.getProperty("loginPassword_w");
			}
			else {
				loginUserName = CONFIG.getProperty("loginUserName_l");
				loginPassword = CONFIG.getProperty("loginPassword_l");
			}
			
			// Enter the Username
			if(!sendKeysByXpath("LoginPage_Username", loginUserName)) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Enter the Password
			if(!sendKeysByXpath("LoginPage_Password", loginPassword)) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Click the login button
			if(!clickByXpath("LoginPage_LoginButton")) {
				printFunctionReturn(fn_fail);
				return false;
			}
			
			// Wait for user to login:  HomePage_Heading
			if(!checkElementPresenceByXpath("HomePage_Heading")) {
				printFunctionReturn(fn_fail);
				return false;
			}
		} 
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			printError("login: Exception occurred while login.");
			//t.printStackTrace();
			printFunctionReturn(fn_fail);			
			return false;
		}
		printLogs("User Logged-in.");
		
		printFunctionReturn(fn_pass);
		return true;
	}

}
