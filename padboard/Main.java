package padboard;

public class Main {
	private static final Rater rater = new ShivaDraRater();
	private static final int BOARD_AREA_5x4 = 20,
							 BOARD_AREA_6x5 = 30,
							 BOARD_AREA_7x6 = 42;
	private static final long EXHAUSTIVE_5x4_MAX = 1L << 19, // Half; we skip the top half of the range via complement board evaluation.
			                  EXHAUSTIVE_6x5_MAX = 1L << 29,
			                  EXHAUSTIVE_7x6_MAX = 1L << 41;
	
	public static final int BOARD_SIZE = PadBoard.SIZE_6x5;
	public static final int BOARD_ROWS = 5 + BOARD_SIZE,
			             BOARD_COLUMNS = 6 + BOARD_SIZE;
	public static final int BOARD_AREA = BOARD_ROWS * BOARD_COLUMNS;
	private static long exhaustiveMax = new long[] {EXHAUSTIVE_5x4_MAX, EXHAUSTIVE_6x5_MAX, EXHAUSTIVE_7x6_MAX}[BOARD_SIZE +1];
	
	public static void main(String... s) {
//		long testSeed = 0b11011101101001011110 ^ 0xFFFFF;
//		System.out.println(Long.toBinaryString(testSeed));
//		System.out.println(PadBoard.mirrorPrecedes(testSeed)); // 
//		System.exit(0);
		
		long startTime = System.currentTimeMillis();
		
		long[] bestByOff      = new long[BOARD_AREA +1];
		int[] bestScoreByOff  = new  int[BOARD_AREA +1];
		long[] evaluatedByOff = new long[BOARD_AREA +1];
		
		long totalEvaluated = 0;
		
		for(long seed = 0; seed < exhaustiveMax; seed++) {
			if(PadBoard.mirrorPrecedes(seed)) continue;
			
			PadBoard p = new PadBoard(BOARD_SIZE, seed);
			int[] scores = p.rate(rater);
			int offColor = p.getOffColor();

			if(scores[0] > bestScoreByOff[offColor]) {
				bestByOff[offColor] = seed;
				bestScoreByOff[offColor] = scores[0];

				System.out.println();
				System.out.println(seed);
				System.out.println(scores[0]);
			}
			if(scores[1] > bestScoreByOff[BOARD_AREA - offColor]) {
				bestByOff[BOARD_AREA - offColor] = seed ^ ((1 << BOARD_AREA) - 1);
				bestScoreByOff[BOARD_AREA - offColor] = scores[1];

				System.out.println();
				System.out.println(seed);
				System.out.println(scores[1]);
			}

			evaluatedByOff[offColor]++;
			evaluatedByOff[BOARD_AREA - offColor]++;
			totalEvaluated++;
		}
		
		long endTime = System.currentTimeMillis();
		
		for(int offColor = 0; offColor <= BOARD_AREA; offColor++) {
			if(bestByOff[offColor] != 0) {
				System.out.println();
				System.out.println("Best " + (BOARD_AREA - offColor) + "-" + offColor + " board of " + evaluatedByOff[offColor] + " boards.");
				new PadBoard(BOARD_SIZE, bestByOff[offColor]).displayBoardLayout();
				System.out.println(bestScoreByOff[offColor]);
			}
		}

		System.out.println();
		System.out.println("Run complete!");
		System.out.println("Boards considered: " + totalEvaluated * 2);
		
		long millis = endTime - startTime;
		long seconds = millis / 1000;
		long minutes = seconds / 60;

		if(minutes > 5) {
			System.out.println("Elapsed time: " + minutes + " minutes");
		} else if(seconds > 20) {
			System.out.println("Elapsed time: " + seconds + " seconds");
		} else {
			System.out.println("Elapsed time: " + millis + " ms");
		}
	}
}
