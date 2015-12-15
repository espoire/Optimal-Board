package padboard;

public class PadBoard {
	public static final int SIZE_5x4 = -1, SIZE_6x5 = 0, SIZE_7x6 = 1;
	private final int size;
	private final byte[][] orbs;
	
	private int blankRows = 0,
				offColor  = 0;
	private final int rows, columns;
	
	public PadBoard(int size) {
		this.size = size;
		this.columns = 6 + size;
		this.rows = 5 + size;
		this.orbs = new byte[columns][rows];
	}
	
	/* seed -> board mapping
	 * 
	 * 5x4
	 * 0  4  8 16 12
	 * 1  5  9 17 13
	 * 2  6 10 18 14
	 * 3  7 11 19 15
	 * 
	 * 
	 */
	public PadBoard(int size, long seed) {
		this(size);

		for(int x = 0; x <= columns/2; x++) {
			for(int y = 0; y < rows; y++) {
				if((seed & 1l) == 1l) {
					this.orbs[x][y] = 1;
					this.offColor++;
				}
				seed = seed >> 1;
			}
		}
		for(int x = columns -1; x > columns/2; x--) {
			for(int y = 0; y < rows; y++) {
				if((seed & 1l) == 1l) {
					this.orbs[x][y] = 1;
					this.offColor++;
				}
				seed = seed >> 1;
			}
		}
		
		this.getMatches();
	}
	
	public int getOffColor() { return this.offColor; }
	
	private Match[] matches;
	private Match[] getMatches() {
		if(this.matches != null) return this.matches;
		
		int matchPartCount = 0;
		Match[] matchParts = new Match[26]; // Unless I've made a mistake, max should be 12 horizontal and 14 vertical (pre-merge).
		
		if(this.blankRows > 5 + size - 3) {
			this.matches = new Match[] {};
			return this.matches;
		}
		
		// width -3, because the first column can't have already been matched, and the final two columns can't create new matches.
		boolean[][] matchedHorizontal = new boolean[6 + size -3][5 + size],
				    matchedVertical   = new boolean[6 + size][5 + size - 3]; // height -3, same idea.
		
		for(int x = 0; x < 6 + size; x++) {
			if(this.orbs[x][5 + size -1] == -1) continue;
			
			for(int y = this.blankRows; y < 5 + size; y++) {
				int attribute = this.orbs[x][y];
				
				if(attribute != -1) {
					// Match horizontal
					// See comment on matchedHorizontal declaration above.
					if(x < 6 + size - 2 && (x == 0 || !matchedHorizontal[x-1][y])) {
						for(int matchingX = x + 1; matchingX <= 6 + size; matchingX++) {
							if(matchingX >= 6 + size || this.orbs[matchingX][y] != attribute) {
								int matchLength = matchingX - x;
								if(matchLength >= 3) {
									Match matchPart = new Match(attribute, matchLength);
									for(int partX = x; partX < matchingX; partX++) {
										matchPart.orbs[partX][y] = true;
										if(partX > 0 && partX < 6 + size - 2) matchedHorizontal[partX-1][y] = true;
									}
									matchPart.setBounds(x, y, matchingX -1, y);
									if(matchLength == 6 + size) matchPart.isRow = true;
									matchParts[matchPartCount++] = matchPart;
								}
								break;
							}
						}
					}
					
					// Match vertical
					// See comment on matchedVertical declaration above.
					if(y < 5 + size - 2 && (y == 0 || !matchedVertical[x][y-1])) {
						for(int matchingY = y + 1; matchingY <= 5 + size; matchingY++) {
							if(matchingY >= 5 + size || this.orbs[x][matchingY] != attribute) {
								int matchLength = matchingY - y;
								if(matchLength >= 3) {
									Match matchPart = new Match(attribute, matchLength);
									for(int partY = y; partY < matchingY; partY++) {
										matchPart.orbs[x][partY] = true;
										if(partY > 0 && partY < 5 + size - 2) matchedVertical[x][partY-1] = true;
									}
									matchPart.setBounds(x, y, x, matchingY -1);
									matchParts[matchPartCount++] = matchPart;
								}
								break;
							}
						}
					}
				}
				
			}
		}
		
		// Consolidate
		// Merges intersecting and adjacent (4-neighborhood) matches, just like the game does.
		for(int i = 0; i < matchPartCount; i++) {
			for(int j = i + 1; j < matchPartCount; j++) {
				if(matchParts[i].mergeWith(matchParts[j])) {
					matchParts[j] = matchParts[matchPartCount -1];
					matchParts[matchPartCount -1] = null;
					matchPartCount--;
					j = i;
				}
			}
		}
		
		this.matches = new Match[matchPartCount];
		for(int i = 0; i < matchPartCount; i++) {
			this.matches[i] = matchParts[i];
		}
		
		return this.matches;
	}
	
	private PadBoard droppedMatches;
	public PadBoard dropMatches() {
		if(this.matches.length == 0) return null;
		
		if(this.droppedMatches != null) return this.droppedMatches;
		this.droppedMatches = new PadBoard(this.size);
		this.droppedMatches.blankRows = 6;
		
		boolean[][] matched = this.getMatchedArray();

		for(int x = 0; x < 6 + size; x++) {
			int columnHeight = 0;
			for(int y = 5 + size -1; y >= this.blankRows; y--) {
				if(!matched[x][y]) {
					this.droppedMatches.orbs[x][5 + size -1 - columnHeight] = this.orbs[x][y];
					columnHeight++;
				}
			}
			for(int y = 0; y < 5 + size - columnHeight; y++) {
				this.droppedMatches.orbs[x][y] = -1;
			}
			
			if(5 + size - columnHeight < this.droppedMatches.blankRows) this.droppedMatches.blankRows = 5 + size - columnHeight;
		}
		
		this.droppedMatches.getMatches();
		
		return this.droppedMatches;
	}
	
	private boolean[][] matchedArray;
	private boolean[][] getMatchedArray() {
		if(this.matchedArray != null) return this.matchedArray;
		
		this.matchedArray = new boolean[6 + size][5 + size];
		
		for(int x = 0; x < 6 + size; x++) {
			for(int y = this.blankRows; y < 5 + size; y++) {
				for(Match m : this.matches) {
					if(m.orbs[x][y]) {
						this.matchedArray[x][y] = true;
						break;
					}
				}
			}
		}
		
		return this.matchedArray;
	}
	
	public void displayBoardLayout() {
		for(int y = 0; y < 5 + size; y++) {
			for(int x = 0; x < 6 + size; x++) {
				System.out.print(this.orbs[x][y] == -1 ? " " : this.orbs[x][y]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Returns the board's and its compliment's (on/off color orb switched) score, per the provider Rater.
	 * 
	 * @param r - Rater to score the board's combos.
	 * @return int[] {board's score, compliment's score}
	 */
	public int[] rate(Rater r) {
		return r.rate(this.getDeepMatches());
	}

	private Match[] getDeepMatches() {
		int matchCount = this.matches.length;
		
		Match[] ret = new Match[14];
		for(int i = 0; i < matchCount; i++) {
			ret[i] = this.matches[i];
		}
		
		PadBoard next = this.dropMatches();
		while(next != null && next.matches.length != 0) {
			for(int i = 0; i < next.matches.length; i++) {
				ret[i + matchCount] = next.matches[i];
			}
			matchCount += next.matches.length;
			
			next = next.dropMatches();
		}
		
		return ret;
	}

	private static final long LEFT_SIDE_MASK = (1L << ((Main.BOARD_COLUMNS/2) * Main.BOARD_ROWS)) - 1;
	private static final int RIGHT_SIDE_POSITION = Main.BOARD_AREA - ((Main.BOARD_COLUMNS/2) * Main.BOARD_ROWS);
	/** Returns true if [the board for this seed]'s left-right mirror will already have been produced by a lesser seed.
	 * 
	 * @param seed
	 */
	public static boolean mirrorPrecedes(long seed) {
		return ((seed & LEFT_SIDE_MASK) < (seed >> RIGHT_SIDE_POSITION)) || ((seed & LEFT_SIDE_MASK) > ((seed >> RIGHT_SIDE_POSITION) ^ LEFT_SIDE_MASK));
		      //  ????????xxxxABCDABCD  <  ABCDABCDxxxx????????
	}
}
