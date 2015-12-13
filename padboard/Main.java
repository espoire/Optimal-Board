package padboard;

public class Main {
	private static final Rater rater = new AYomiRater();
	
	public static void main(String... s) {
		long startTime = System.currentTimeMillis();
		
		PadBoard[] bestByOff = new PadBoard[44];
		int[] bestScoreByOff = new int[44];
		long[] evaluatedByOff = new long[44];
		
		long totalEvaluated = 0;
		
		for(int offColor = 9; offColor <= 9; offColor++) {
			PadBoard best = null;
			int bestScore = 0;
			
			long evaluated = 0;
			int boardsSinceImproved = 0;
			while(boardsSinceImproved < 1500000) {
				PadBoard p = new PadBoard(PadBoard.SIZE_7x6, offColor);
				int score = p.rate(rater);
				
				if(score > bestScore) {
					best = p;
					bestScore = score;
					
					boardsSinceImproved = 0;
	
					System.out.println();
					best.displayBoardLayout();
					System.out.println(bestScore);
				} else {
					boardsSinceImproved++;
					
					if(boardsSinceImproved % 150000 == 0) System.out.print(boardsSinceImproved / 150000 + " ");
				}
				
				evaluated++;
			}
			
			bestByOff[offColor] = best;
			bestScoreByOff[offColor] = bestScore;
			evaluatedByOff[offColor] = evaluated;
			totalEvaluated += evaluated;
		}
		
		long endTime = System.currentTimeMillis();
		
		for(int offColor = 9; offColor <= 9; offColor++) {
			System.out.println();
			System.out.println("Best " + (44 - offColor) + "-" + offColor + " board of " + evaluatedByOff[offColor] + " boards.");
			bestByOff[offColor].displayBoardLayout();
			System.out.println(bestScoreByOff[offColor]);
		}

		System.out.println();
		System.out.println("Run complete!");
		System.out.println("Boards considered: " + totalEvaluated);
		System.out.println("Elapsed time: " + (endTime - startTime) / (1000) + " seconds");
	}
}
