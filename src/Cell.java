public class Cell {

	int row, col;
	int value;

	public Cell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

}
