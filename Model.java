public class Model {
	public final static int UP = 0;
	public final static int RIGHT = 1;
	public final static int DOWN = 2;
	public final static int LEFT = 3;

	private int rows;
	private int cols;

	private int row;
	private int col;
	private int dir;

	public Model(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.row = 0;
		this.col = 0;
		this.dir = RIGHT;
	}

	public void move() {
		switch (dir) {
		case UP:
			--row;
			row = (row == -1) ? rows - 1 : row;
			break;
		case RIGHT:
			++col;
			col = (col == cols) ? 0 : col;
			break;
		case DOWN:
			++row;
			row = (row == rows) ? 0 : row;
			break;
		case LEFT:
			--col;
			col = (col == -1) ? cols - 1 : col;
			break;
		}
	}
    public int getDir() {
    	return dir;
    }
	public void setDir(int dir) {
		this.dir = dir;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}
}