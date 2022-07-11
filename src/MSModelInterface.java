/* Name: Tejas Polu
 * Date: 3/22/22
 * Period: 4 
 * 
 * This program took me around 10 minutes to complete. 
 * 
 * Is this lab fully working?  Yes
 * If not complete and working, what are you stuck on?  What is your action plan to complete the work?

 * Resubmit date, if applicable:
 * (Explain what you fixed and what was wrong)
 */


public interface MSModelInterface {
	public int getNumRows();
	public int getNumCols();
	public boolean hasLost();
	public boolean hasWon();
	public boolean hasClicked();
	public int getTotalMines();
	public int getTotalFlags();
	public void gameOver();
	public void gameWon();
	public boolean allSafeCellsRemoved();
	public void randomizeBoard();
	public void revealUncoveredBombs();
	public void revealWrongFlags();
	public void revealCell(int row, int col);
	public int getNumAdjFlags(int row, int col);
	public int getNumAdjMines(int row, int col);
	public boolean isMine(int row, int col);
	public boolean isQuestion(int row, int col);
	public boolean isFlag(int row, int col);
	public boolean isCovered(int row, int col);
	public void setMine(int row, int col);
	public boolean isRevealed(int row, int col);
	public void reveal(int row, int col);
	public boolean revealWithFlag(int row, int col);
	public void setFlag(int row, int col, boolean flag);
	public void setQuestion(int row, int col, boolean question);
	public void resetGame(int rows, int cols, int mines);
	public void setClicked(boolean clicked, int row, int col);
}
