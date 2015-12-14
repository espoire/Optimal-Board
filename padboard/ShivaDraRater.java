package padboard;

public class ShivaDraRater implements Rater {
	private static final byte fireATK = 6, prongVal = 5, fireRows = 9;
	
	@Override
	public int rate(Match[] matches) {
		int combos = 0;
		int baseDmg = 0;
		int rows = 0;
		
		for(Match m : matches) {
			if(m == null) break;
			combos++;
			if(m.attribute == 0) {
				int ATK = fireATK + (m.breakSize == 4 ? prongVal : 0);
				int sizeMult = 3 + m.breakSize;
				
				if(m.isRow) rows++;
				
				baseDmg += ATK * sizeMult;
			}
		}
		
		int comboMult = 3 + combos;
		int rowMult = 10 + rows * fireRows;
		
		return comboMult * rowMult * baseDmg;
	}
}
