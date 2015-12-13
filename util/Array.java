package util;

public class Array {
	public static double sum(double[] array) {
		double ret = 0;
		for(double d : array) ret += d;
		return ret;
	}
}
