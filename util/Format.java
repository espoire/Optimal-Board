package util;

public class Format {
	public static String format(double d, int chars) {
		String ret = "" + d;
		if(ret.length() > chars) ret = ret.substring(0, chars);
		return ret;
	}
}
