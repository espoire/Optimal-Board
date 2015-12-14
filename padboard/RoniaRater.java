package padboard;

public class RoniaRater extends Rater {
	private static final byte darkATK = 4, prongVal = 2;
	
	@Override
	public int rate(Match[] matches) {
		int combos = matches.length;
		
		int baseDmg = 0;
		
		for(Match m : matches) {
			if(m == null) break;
			if(m.attribute == 0) {
				int ATK = darkATK + (m.breakSize == 4 ? prongVal : 0);
				int sizeMult = 3 + m.breakSize;
				
				baseDmg += ATK * sizeMult;
			}
		}
		
		int comboMult = 3 + combos;
		int leaderComboMult = combos >= 6 ? 6 : 5;
		
		return comboMult * leaderComboMult * baseDmg;
	}
}
