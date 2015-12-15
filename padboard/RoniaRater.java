package padboard;

public class RoniaRater implements Rater {
	private static final byte darkATK = 4, prongVal = 2;
	
	@Override
	public int[] rate(Match[] matches) {
		int combos = 0;
		int[] baseDmg = new int[2];
		
		for(Match m : matches) {
			if(m == null) break;
			combos++;
			
			int ATK = darkATK + (m.breakSize == 4 ? prongVal : 0);
			int sizeMult = 3 + m.breakSize;
			
			baseDmg[m.attribute] += ATK * sizeMult;
		}
		
		int comboMult = 3 + combos;
		int leaderComboMult = combos >= 6 ? 6 : 5;
		
		return new int[] {comboMult * leaderComboMult * baseDmg[0],
						  comboMult * leaderComboMult * baseDmg[1]};
	}
}
