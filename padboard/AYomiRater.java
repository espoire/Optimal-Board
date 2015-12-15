package padboard;

public class AYomiRater implements Rater {
	private static final byte enhancedDark = 12, darkATK = 4, prongVal = 3; // 4:3 is a close approximation of the true ratio, using ints over floats saves ~0.7% run time.
	private static final float enhancedMult = 1 + 0.05f * enhancedDark;
	
	@Override
	public int[] rate(Match[] matches) {
		int combos = 0;
		boolean[] activated = new boolean[2];
		int[] baseDmg = new int[2];
		
		for(Match m : matches) {
			if(m == null) break;
			combos++;
			
			if(m.breakSize == 5) activated[m.attribute] = true;
			
			int ATK = darkATK + (m.breakSize == 4 ? prongVal : 0);
			float enhanceMult = enhancedMult * (1 + 0.06f * m.breakSize);
			int sizeMult = 3 + m.breakSize;
			
			baseDmg[m.attribute] += ATK * enhanceMult * sizeMult;
		}
		
		int comboMult = 3 + combos;
		int leaderComboMult = combos < 6 ? 4 : (combos >= 7 ? 25 : 16);
		int[] leaderFiveMult = new int[] {activated[0] ? 9 : 1, activated[1] ? 9 : 1};
		
		return new int[] {comboMult * leaderComboMult * leaderFiveMult[0] * baseDmg[0],
						  comboMult * leaderComboMult * leaderFiveMult[1] * baseDmg[1]};
	}
}
