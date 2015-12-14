package padboard;

public class ShivaDraRater extends Rater {
	private static final byte fireATK = 6, prongVal = 5, fireRows = 9;
	
	@Override
	public int rate(Match[] matches) {
		int combos = matches.length;
		
		int baseDmg = 0;
		int rows = 0;
		
		for(Match m : matches) {
			if(m == null) break;
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
