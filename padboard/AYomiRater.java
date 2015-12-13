package padboard;

public class AYomiRater extends Rater {
	private static final byte enhancedDark = 12, darkATK = 4, prongVal = 3; // 4:3 is a close approximation of the true ratio, using ints over floats saves ~0.7% run time.
	private static final float enhancedMult = 1 + 0.05f * enhancedDark;
	
	@Override
	public int rate(Match[] matches) {
		int combos = matches.length;
		boolean activated = false;
		
		int baseDmg = 0;
		
		for(Match m : matches) {
			if(m == null) break;
			if(m.attribute == 0) {
				if(m.breakSize == 5) activated = true;
				int ATK = darkATK + (m.breakSize == 4 ? prongVal : 0);
				float enhanceMult = enhancedMult * (1 + 0.06f * m.breakSize);
				int sizeMult = 3 + m.breakSize;
				
				baseDmg += ATK * enhanceMult * sizeMult;
			}
		}
		
		int comboMult = 3 + combos;
		int leaderComboMult = combos < 6 ? 4 : (combos >= 7 ? 25 : 16);
		int leaderFiveMult = activated ? 9 : 1;
		
		return comboMult * leaderComboMult * leaderFiveMult * baseDmg;
	}
}
