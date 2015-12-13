package util;

import java.util.Vector;

public class Random {
	public static int random(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}

	public static long random(long min, long max) {
		return (long) (Math.random() * (max - min + 1) + min);
	}
	
	public static double random(double min, double max) {
		return (Math.random() * (max - min) + min);
	}
	
	/** Returns a random double in the range [-d .. d]. */
	public static double random(double d) {
		return (Math.random() - 0.5) * 2.0 * d;
	}
	
	/** Returns a random double in the range [-1 .. 1]. <br />
	 *  Numbers near zero are favored heavily. */
	public static double randomCubic() {
		return randomCubic(1);
	}

	/** Returns a random double in the range [-d .. d]. <br />
	 *  Numbers near zero are favored heavily. */
	public static double randomCubic(double d) {
		return Math.pow(random(1.0), 3) * d;
	}

	/** Returns a random double in the range [min .. max]. <br />
	 *  Numbers near the mean are favored heavily. */
	public static double randomCubic(double min, double max) {
		double spread = (max - min) / 2;
		return randomCubic(spread) + spread + min;
	}
	
	/** Rounds <b>val</b>/<b>step</b> to an adjacent unit, randomly.<br>
	 * Odds of rounding up are (<b>val</b> % <b>step</b>) / <b>step</b>.*/
	public static int roundRandom(int val, int step) {
		return (val + random(0, step-1)) / step;
	}
	
	/** Rounds input to an adjacent int, randomly. */
	public static int roundRandom(double d) {
		return (int) (Math.random() < d - (int) d ? d + 1 : d);
	}
	
	public static int random(int[] i) {
		if(i == null) return 0; //util.Err.err("Attempted to select from a null int array.");
		return i[random(0, i.length-1)];
	}
	
	public static double random(double[] d) {
		if(d == null) return 0.0; //util.Err.err("Attempted to select from a null double array.");
		return d[random(0, d.length-1)];
	}

	public static String random(String[] s) {
		if(s == null) return null; //util.Err.err("Attempted to select from a null String array.");
		return s[random(0, s.length-1)];
	}

	public static Object random(Vector<?> v) {
		if(v == null || v.size() == 0) return null;
		return v.get(random(0, v.size() -1));
	}

	public static boolean random() {
		return Math.random() < 0.5;
	}

	public static int plusOrMinus(int i) {
		return (random() ? i : -i);
	}
	
	public static int weightedRandom(double[] rates) {
		double roll = random(0.0, Array.sum(rates));
		for(int i = 0; i < rates.length; i++) {
			roll -= rates[i];
			if(roll < 0.00001) return i;
		}
		return 0;
	}
}
