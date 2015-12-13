package util;

public class Err {
	public static void err(String msg) {
		new Error(msg).printStackTrace();
	}
	
	public static void fatalErr(String msg) {
		err(msg);
		System.exit(1);
	}
}
