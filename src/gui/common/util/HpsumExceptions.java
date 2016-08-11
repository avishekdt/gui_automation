package gui.common.util;

public class HpsumExceptions extends RuntimeException {
	private static final long serialVersionUID = 1L;
	int a;
	HpsumExceptions(int b) {
		a=b;
	}
	   
	public String toString() {
		return ("Exception Number =  "+a) ;
	}

}
