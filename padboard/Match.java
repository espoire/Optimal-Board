package padboard;

public class Match {
	private static final int BOARD_SIZE = Main.BOARD_SIZE,
							 BOARD_WIDTH = 6 + BOARD_SIZE,
							 BOARD_HEIGHT = 5 + BOARD_SIZE;
	
	public int attribute, breakSize;
	public boolean[][] orbs;
	public boolean isRow = false;
	protected int minX = BOARD_WIDTH,
			      minY = BOARD_HEIGHT,
			      maxX = -1,
			      maxY = -1;
	
	public Match(int attribute, int breakSize) {
		this.attribute = attribute;
		this.breakSize = breakSize;
		this.orbs = new boolean[BOARD_WIDTH][BOARD_HEIGHT];
	}

	public boolean mergeWith(Match other) {
		// Can't be the same combo if they're not even the same orb type.
		if(this.attribute != other.attribute) return false;
		
		for(int x = this.minX; x <= this.maxX; x++) {
			for(int y = this.minY; y <= this.maxY; y++) {
				if(this.orbs[x][y]) {
					// Merge on overlap (+ and L shaped combo)
					if(other.orbs[x][y]) {
						this.doMerge(other);
						return true;
					}
					
					// Merge on adjacency (4-neighborhood), example: 2x3.
					if(x+1 < BOARD_WIDTH  && other.orbs[x+1][y]) {
						this.doMerge(other);
						return true;
					}
					if(x-1 >= 0           && other.orbs[x-1][y]) {
						this.doMerge(other);
						return true;
					}
					if(y+1 < BOARD_HEIGHT && other.orbs[x][y+1]) {
						this.doMerge(other);
						return true;
					}
					if(y-1 >= 0           && other.orbs[x][y-1]) {
						this.doMerge(other);
						return true;
					}
				}
			}
		}
		return false;
	}

	private void doMerge(Match other) {
		if(other.minX < this.minX) this.minX = other.minX;
		if(other.minY < this.minY) this.minY = other.minY;
		if(other.maxX > this.maxX) this.maxX = other.maxX;
		if(other.maxY > this.maxY) this.maxY = other.maxY;
		
		this.isRow = this.isRow || other.isRow;
		
		this.breakSize = 0;
		for(int x = this.minX; x <= this.maxX; x++) {
			for(int y = this.minY; y <= this.maxY; y++) {
				if(this.orbs[x][y] || other.orbs[x][y]) {
					this.orbs[x][y] = true;
					this.breakSize++;
				}
			}
		}
	}

	public void setBounds(int x1, int y1, int x2, int y2) {
		this.minX = x1;
		this.minY = y1;
		this.maxX = x2;
		this.maxY = y2;
	}
}
