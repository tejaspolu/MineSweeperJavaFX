import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MSModel implements MSModelInterface {

	Cell cellArr[][];
	private int numRows;
	private int numCols;
	private int numMines;
	private boolean isLost = false;
	private boolean isWon = false;
	private int numFlags;
	private boolean hasClicked = false;
	private int notRow;
	private int notCol;

	public MSModel(int rows, int cols, int mines){
		cellArr = new Cell[rows][cols];
		numRows = rows;
		numCols = cols;
		numMines = mines;
	}

	@Override
	public int getNumRows() {
		return numRows;
	}

	@Override
	public int getNumCols() {
		return numCols;
	}

	@Override
	public boolean hasLost() {
		return isLost;
	}

	@Override
	public boolean hasWon() {
		return isWon;
	}

	@Override
	public void randomizeBoard() {
		boolean[][] randArr = new boolean[numRows][numCols];
		int row;
		int col;
		for(int i = 0; i < numMines; i++) {
			do {
				row = new Random().nextInt(numRows);
				col = new Random().nextInt(numCols);
			} while(randArr[row][col] || (row == notRow && col == notCol) ||
					(row - 1 >= 0 && col - 1 >= 0 && row - 1 == notRow && col - 1 == notCol) ||
					(row - 1 >= 0 && row - 1 == notRow && col == notCol) ||
					(row - 1 >= 0 && col + 1 >= 0 && row - 1 == notRow && col + 1 == notCol) ||
					(col - 1 >= 0 && row == notRow && col - 1 == notCol) ||
					(col + 1 < getNumCols() && row == notRow && col + 1 == notCol) ||
					(row + 1 < getNumRows() && col - 1 >= 0 && row + 1 == notRow && col - 1 == notCol) ||
					(row + 1 < getNumRows() && row + 1 == notRow && col == notCol) ||
					(row + 1 < getNumRows() && col + 1 < getNumCols() && row + 1 == notRow && col + 1 == notCol));
			randArr[row][col] = true;
		}
		for(int rowNum = 0; rowNum < numRows; rowNum++) {
			for(int colNum = 0; colNum < numCols; colNum++) {
				if(randArr[rowNum][colNum]) cellArr[rowNum][colNum] = new Cell(true);
				else cellArr[rowNum][colNum] = new Cell(false);
			}
		}
	}

	@Override
	public int getTotalMines() {
		return numMines;
	}

	@Override
	public int getTotalFlags() {
		return numFlags;
	}

	@Override
	public int getNumAdjMines(int row, int col) {
		int neighbors = 0;
		if(row - 1 >= 0 && col - 1 >= 0 && cellArr[row - 1][col - 1].isMine()) neighbors++;
		if(row - 1 >= 0 && cellArr[row - 1][col].isMine()) neighbors++;
		if(row - 1 >= 0 && col + 1 < getNumCols() && cellArr[row - 1][col + 1].isMine()) neighbors++;
		if(col - 1 >= 0 && cellArr[row][col - 1].isMine()) neighbors++;
		if(col + 1 < getNumCols() && cellArr[row][col + 1].isMine()) neighbors++;
		if(row + 1 < getNumRows() && col - 1 >= 0 && cellArr[row + 1][col - 1].isMine()) neighbors++;
		if(row + 1 < getNumRows() && cellArr[row + 1][col].isMine()) neighbors++;
		if(row + 1 < getNumRows() && col + 1 < getNumCols() && cellArr[row + 1][col + 1].isMine()) neighbors++;
		return neighbors;
	}

	@Override
	public boolean isMine(int row, int col) {
		if(cellArr[row][col].isMine()) return true;
		return false;
	}

	@Override
	public boolean isQuestion(int row, int col) {
		if(cellArr[row][col].isQuestion()) return true;
		return false;
	}

	@Override
	public boolean isFlag(int row, int col) {
		if(cellArr[row][col].isFlag()) return true;
		return false;
	}

	@Override
	public boolean isCovered(int row, int col) {
		if(cellArr[row][col].isCovered()) return true;
		return false;
	}

	@Override
	public void setMine(int row, int col) {
		cellArr[row][col].setMine();
		numMines++;
	}

	@Override
	public void setQuestion(int row, int col, boolean question) {
		if(cellArr[row][col].isCovered() && question) cellArr[row][col].setQuestion(true);
		else cellArr[row][col].setQuestion(false);
	}

	@Override
	public void setFlag(int row, int col, boolean flag) {
		if(cellArr[row][col].isCovered()) {
			if(flag) {
				cellArr[row][col].setFlag(true);
				numFlags++;
			}else {
				cellArr[row][col].setFlag(false);
				numFlags--;
			}
		}
	}

	@Override
	public boolean isRevealed(int row, int col) {
		if(!cellArr[row][col].isCovered()) return true;
		return false;
	}

	@Override
	public void reveal(int row, int col) {
		if(row >= 0 && row < numRows && col >= 0 && col < numCols && !cellArr[row][col].isMine() && cellArr[row][col].isCovered()) {
			cellArr[row][col].setCovered(false);
			if(getNumAdjMines(row, col) == 0) {
				reveal(row - 1, col - 1);
				reveal(row - 1, col);
				reveal(row - 1, col + 1);
				reveal(row, col + 1);
				reveal(row + 1, col + 1);
				reveal(row + 1, col);
				reveal(row + 1, col - 1);
				reveal(row, col - 1);
			}
		}
	}
	
	@Override
	public boolean revealWithFlag(int row, int col) {
		boolean isMine = false;
		if(row >= 0 && row < numRows && col >=0 && col < numCols) {
			if(row - 1 >= 0 && col - 1 >= 0 && cellArr[row - 1][col - 1].isCovered() && !cellArr[row - 1][col - 1].isFlag()) {
				if(getNumAdjMines(row - 1, col - 1) > 0) cellArr[row - 1][col - 1].setCovered(false);
				if(cellArr[row - 1][col - 1].isMine()) isMine = true;
				reveal(row - 1, col - 1);
			}
			if(row - 1 >= 0 && cellArr[row - 1][col].isCovered() && !cellArr[row - 1][col].isFlag()) {
				if(getNumAdjMines(row - 1, col) > 0) cellArr[row - 1][col].setCovered(false);
				if(cellArr[row - 1][col].isMine()) isMine = true;
				reveal(row - 1, col);
			}
			if(row - 1 >= 0 && col + 1 < getNumCols() && cellArr[row - 1][col + 1].isCovered() && !cellArr[row - 1][col + 1].isFlag()) {
				if(getNumAdjMines(row - 1, col + 1) > 0) cellArr[row - 1][col + 1].setCovered(false);
				if(cellArr[row - 1][col + 1].isMine()) isMine = true;
				reveal(row - 1, col + 1);
			}
			if(col - 1 >= 0 && cellArr[row][col - 1].isCovered() && !cellArr[row][col - 1].isFlag()) {
				if(getNumAdjMines(row, col - 1) > 0) cellArr[row][col - 1].setCovered(false);
				if(cellArr[row][col - 1].isMine()) isMine = true;
				reveal(row, col - 1);
			}
			if(col + 1 < getNumCols() && cellArr[row][col + 1].isCovered() && !cellArr[row][col + 1].isFlag()) {
				if(getNumAdjMines(row, col + 1) > 0) cellArr[row][col + 1].setCovered(false);
				if(cellArr[row][col + 1].isMine()) isMine = true;
				reveal(row, col + 1);
			}
			if(row + 1 < getNumRows() && col - 1 >= 0 && cellArr[row + 1][col - 1].isCovered() && !cellArr[row + 1][col - 1].isFlag()) {
				if(getNumAdjMines(row + 1, col - 1) > 0) cellArr[row + 1][col - 1].setCovered(false);
				if(cellArr[row + 1][col - 1].isMine()) isMine = true;
				reveal(row + 1, col - 1);
			}
			if(row + 1 < getNumRows() && cellArr[row + 1][col].isCovered() && !cellArr[row + 1][col].isFlag()) {
				if(getNumAdjMines(row + 1, col) > 0) cellArr[row + 1][col].setCovered(false);
				if(cellArr[row + 1][col].isMine()) isMine = true;
				reveal(row + 1, col);
			}
			if(row + 1 < getNumRows() && col + 1 < getNumCols() && cellArr[row + 1][col + 1].isCovered() && !cellArr[row + 1][col + 1].isFlag()) {
				if(getNumAdjMines(row + 1, col + 1) > 0) cellArr[row + 1][col + 1].setCovered(false);
				if(cellArr[row + 1][col + 1].isMine()) isMine = true;
				reveal(row + 1, col + 1);
			}
		}
		return isMine;
	}

	@Override
	public void resetGame(int rows, int cols, int mines) {
		cellArr = new Cell[rows][cols];
		numRows = rows;
		numCols = cols;
		numMines = mines;
		numFlags = 0;
		isLost = false;
		isWon = false;
		setClicked(false, 0, 0);
	}

	@Override
	public void gameOver() {
		isLost = true;
	}
	
	@Override
	public void gameWon() {
		isWon = true;
	}

	@Override
	public boolean allSafeCellsRemoved() {
		for(int i = 0; i < cellArr.length; i++) {
			for(int j = 0; j < cellArr[i].length; j++) {
				if(!cellArr[i][j].isMine() && cellArr[i][j].isCovered()) return false;
			}
		}
		return true;
	}

	@Override
	public boolean hasClicked() {
		return hasClicked;
	}

	@Override
	public void setClicked(boolean clicked, int row, int col) {
		hasClicked = clicked;
		notRow = row;
		notCol = col;
		if(hasClicked == true) {
			randomizeBoard();
		}
	}

	@Override
	public void revealCell(int row, int col) {
		cellArr[row][col].setCovered(false);
	}

	@Override
	public void revealUncoveredBombs() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				if(cellArr[i][j].isMine() && cellArr[i][j].isCovered()) {
					cellArr[i][j].setCovered(false);
				}
			}
		}
	}

	@Override
	public void revealWrongFlags() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				if(!cellArr[i][j].isMine() && cellArr[i][j].isFlag()) {
					cellArr[i][j].setCovered(false);
				}
			}
		}
	}

	@Override
	public int getNumAdjFlags(int row, int col) {
		int neighbors = 0;
		if(row - 1 >= 0 && col - 1 >= 0 && cellArr[row - 1][col - 1].isFlag()) neighbors++;
		if(row - 1 >= 0 && cellArr[row - 1][col].isFlag()) neighbors++;
		if(row - 1 >= 0 && col + 1 < getNumCols() && cellArr[row - 1][col + 1].isFlag()) neighbors++;
		if(col - 1 >= 0 && cellArr[row][col - 1].isFlag()) neighbors++;
		if(col + 1 < getNumCols() && cellArr[row][col + 1].isFlag()) neighbors++;
		if(row + 1 < getNumRows() && col - 1 >= 0 && cellArr[row + 1][col - 1].isFlag()) neighbors++;
		if(row + 1 < getNumRows() && cellArr[row + 1][col].isFlag()) neighbors++;
		if(row + 1 < getNumRows() && col + 1 < getNumCols() && cellArr[row + 1][col + 1].isFlag()) neighbors++;
		return neighbors;
	}
}
