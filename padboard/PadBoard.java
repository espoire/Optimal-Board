package padboard;

import java.util.ArrayList;

public class PadBoard {
	public static final int SIZE_5x4 = -1, SIZE_6x5 = 0, SIZE_7x6 = 1;
	private final int size;
	private final byte[][] orbs;
	
	private int blankRows = 0;
	
	public PadBoard(int size) {
		this.size = size;
		this.orbs = new byte[6 + size][5 + size];
	}
	
	public PadBoard(int size, int offColor) {
		this(size);
		
		for(int i = 0; i < offColor && i < (6 + size) * (5 + size); i++) {
			int x = util.Random.random(0, 6 + size -1),
				y = util.Random.random(0, 5 + size -1);
			
			if(this.orbs[x][y] == 0) {
				this.orbs[x][y] = 1;
			} else {
				i--;
			}
		}
	}
	
	private ArrayList<Match> matches;
	private ArrayList<Match> getMatches() {
		if(this.matches != null) return this.matches;
		
		ArrayList<Match> matchParts = new ArrayList<Match>();
		
		if(this.blankRows > 5 + size - 3) {
			this.matches = matchParts;
			return this.matches;
		}
		
		// width -3, because the first column can't have already been matched, and the final two columns can't create new matches.
		boolean[][] matchedHorizontal = new boolean[6 + size -3][5 + size],
				    matchedVertical   = new boolean[6 + size][5 + size - 3]; // height -3, same idea.
		
		for(int x = 0; x < 6 + size; x++) {
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
									Match matchPart = new Match(attribute, matchLength, this.size);
									for(int partX = x; partX < matchingX; partX++) {
										matchPart.orbs[partX][y] = true;
										if(partX > 0 && partX < 6 + size - 2) matchedHorizontal[partX-1][y] = true;
									}
									matchParts.add(matchPart);
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
									Match matchPart = new Match(attribute, matchLength, this.size);
									for(int partY = y; partY < matchingY; partY++) {
										matchPart.orbs[x][partY] = true;
										if(partY > 0 && partY < 5 + size - 2) matchedVertical[x][partY-1] = true;
									}
									matchParts.add(matchPart);
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
		for(int i = 0; i < matchParts.size(); i++) {
			for(int j = i + 1; j < matchParts.size(); j++) {
				if(matchParts.get(i).mergeWith(matchParts.get(j))) {
					matchParts.remove(j);
					j = i;
				}
			}
		}

		this.matches = matchParts;
		return this.matches;
	}
	
	private PadBoard droppedMatches;
	public PadBoard dropMatches() {
		ArrayList<Match> matches = this.getMatches();
		if(matches.size() == 0) return null;
		
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
		
		return this.droppedMatches;
	}
	
	private boolean[][] matchedArray;
	private boolean[][] getMatchedArray() {
		if(this.matchedArray != null) return this.matchedArray;
		this.matchedArray = new boolean[6 + size][5 + size];
		
		ArrayList<Match> matches = this.getMatches();
		
		for(int x = 0; x < 6 + size; x++) {
			for(int y = this.blankRows; y < 5 + size; y++) {
				for(Match m : matches) {
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

	private int rating = -1;
	public int rate(Rater r) {
		if(this.rating >= 0) return this.rating;
		
		ArrayList<Match> matches = this.getDeepMatches();
		this.rating = r.rate(matches);
		
		return this.rating;
	}

	private ArrayList<Match> getDeepMatches() {
		ArrayList<Match> ret = new ArrayList<Match>();
		ret.addAll(this.getMatches());
		
		PadBoard next = this.dropMatches();
		while(next != null && next.getMatches().size() != 0) {
			ret.addAll(next.getMatches());
			next = next.dropMatches();
		}
		
		return ret;
	}
}
