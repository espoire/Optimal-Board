package padboard;

public class ShivaDraRater implements Rater {
	private static final byte fireATK = 6, prongVal = 5, fireRows = 9;
	
	@Override
	public int[] rate(Match[] matches) {
		int combos = 0;
		int baseDmg[] = new int[2];
		int rows[] = new int[2];
		
		for(Match m : matches) {
			if(m == null) break;
			combos++;
			int ATK = fireATK + (m.breakSize == 4 ? prongVal : 0);
			int sizeMult = 3 + m.breakSize;
			
			if(m.isRow) rows[m.attribute]++;
			
			baseDmg[m.attribute] += ATK * sizeMult;
		}
		
		int comboMult = 3 + combos;
		int[] rowMult = new int[] {10 + rows[0] * fireRows, 10 + rows[1] * fireRows};
		
		return new int[] {comboMult * rowMult[0] * baseDmg[0],
						  comboMult * rowMult[1] * baseDmg[1]};
	}
}
