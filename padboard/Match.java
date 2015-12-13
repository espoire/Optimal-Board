package padboard;

public class Match {
	public int attribute, breakSize, boardSize;
	public boolean[][] orbs;
	
	public Match(int attribute, int breakSize, int boardSize) {
		this.attribute = attribute;
		this.breakSize = breakSize;
		this.boardSize = boardSize;
		this.orbs = new boolean[6 + boardSize][5 + boardSize];
	}

	public boolean mergeWith(Match other) {
		// Can't be the same combo if they're not even the same orb type.
		if(this.attribute != other.attribute) return false;
		
		for(int x = 0; x < 6 + boardSize; x++) {
			for(int y = 0; y < 5 + boardSize; y++) {
				
				// Merge on overlap (+ and L shaped combo)
				if(this.orbs[x][y] && other.orbs[x][y]) {
					this.doMerge(other);
					return true;
				}
				
				// Merge on adjacency (4-neighborhood), example: 2x3.
				if(this.orbs[x][y] && x+1 < 6 + boardSize && other.orbs[x+1][y]) {
					this.doMerge(other);
					return true;
				}
				if(this.orbs[x][y] && x-1 >= 0             && other.orbs[x-1][y]) {
					this.doMerge(other);
					return true;
				}
				if(this.orbs[x][y] && y+1 < 5 + boardSize && other.orbs[x][y+1]) {
					this.doMerge(other);
					return true;
				}
				if(this.orbs[x][y] && y-1 >= 0             && other.orbs[x][y-1]) {
					this.doMerge(other);
					return true;
				}
			}
		}
		return false;
	}

	private void doMerge(Match other) {
		this.breakSize = 0;
		for(int x = 0; x < 6 + boardSize; x++) {
			for(int y = 0; y < 5 + boardSize; y++) {
				if(this.orbs[x][y] || other.orbs[x][y]) {
					this.orbs[x][y] = true;
					this.breakSize++;
				}
			}
		}
	}
}
