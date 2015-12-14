package padboard;

public class Main {
	private static final Rater rater = new ShivaDraRater();
	private static final int BOARD_AREA_5x4 = 20;
	private static final long EXHAUTIVE_5x4_MAX = 1048576;
	
	private static int BOARD_AREA = BOARD_AREA_5x4;
	
	public static void main(String... s) {
		long startTime = System.currentTimeMillis();
		
		PadBoard[] bestByOff = new PadBoard[43];
		int[] bestScoreByOff = new int[43];
		long[] evaluatedByOff = new long[43];
		
		long totalEvaluated = 0;
		
		for(long seed = 0; seed <= EXHAUTIVE_5x4_MAX; seed++) {
			PadBoard p = new PadBoard(PadBoard.SIZE_5x4, seed);
			int score = p.rate(rater);
			int offColor = p.getOffColor();
			
			if(score > bestScoreByOff[offColor]) {
				bestByOff[offColor] = p;
				bestScoreByOff[offColor] = score;

				System.out.println();
				p.displayBoardLayout();
				System.out.println(score);
			}
			
			evaluatedByOff[offColor]++;
			totalEvaluated++;
		}
		
		long endTime = System.currentTimeMillis();
		
		for(int offColor = 0; offColor <= BOARD_AREA; offColor++) {
			if(bestByOff[offColor] != null) {
				System.out.println();
				System.out.println("Best " + (BOARD_AREA - offColor) + "-" + offColor + " board of " + evaluatedByOff[offColor] + " boards.");
				bestByOff[offColor].displayBoardLayout();
				System.out.println(bestScoreByOff[offColor]);
			}
		}

		System.out.println();
		System.out.println("Run complete!");
		System.out.println("Boards considered: " + totalEvaluated);
		System.out.println("Elapsed time: " + (endTime - startTime) / (1000) + " seconds");
	}
}
