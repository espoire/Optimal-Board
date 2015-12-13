package padboard;

public class Main {
	private static final Rater rater = new AYomiRater();
	
	public static void main(String... s) {
		long startTime = System.currentTimeMillis();
		
		PadBoard[] bestByOff = new PadBoard[43];
		int[] bestScoreByOff = new int[43];
		long[] evaluatedByOff = new long[43];
		
		long totalEvaluated = 0;
		
		for(long seed = 0; seed <= 240000; seed++) {
			PadBoard p = new PadBoard(PadBoard.SIZE_7x6, seed);
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
		
		for(int offColor = 0; offColor <= 42; offColor++) {
			if(bestByOff[offColor] != null) {
				System.out.println();
				System.out.println("Best " + (44 - offColor) + "-" + offColor + " board of " + evaluatedByOff[offColor] + " boards.");
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
