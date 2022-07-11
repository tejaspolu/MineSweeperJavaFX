/* Name: Tejas Polu
 * Date: 3/28/22
 * Period: 4 
 * 
 * This program took me around 2 hours to complete. 
 * 
 * Is this lab fully working?  Yes
 * If not complete and working, what are you stuck on?  What is your action plan to complete the work?
 * N/A

 * Resubmit date, if applicable:
 * (Explain what you fixed and what was wrong)

 * Reflection Paragraph:
 * Overall, the program went pretty smoothly. I struggled with understanding
 * the model and controller relationship at first, but once I figured it out it wasn't 
 * too hard. I learned to keep trying things out until you understand 
 * what is going on. I would like to make more complex things next program. 

 */

import java.util.Scanner;

public class MSController {

	public static void main(String[]args) {
		MSController controller = new MSController(10, 10, 91);
		controller.play();
	}

	MSModel model;
	private int numRows;
	private int numCols;
	private int numMines;

	public MSController(int rows, int cols, int mines) {
		numRows = rows;
		numCols = cols;
		numMines = mines;
		model = new MSModel(numRows, numCols, numMines);
	}

	public void printDummyBoard() {
		System.out.print("  ");
		int counter = 0;
		for(int i = 0; i < model.getNumCols(); i++) {
			if(i == 10) counter = 0;
			System.out.print(counter + " ");
			counter++;
		}
		System.out.print("\t   ");
		counter = 0;
		for(int i = 0; i < model.getNumCols(); i++) {
			if(i == 10) counter = 0;
			System.out.print(counter + " ");
			counter++;
		}
		for(int row = 0; row < model.getNumRows(); row++) {
			System.out.print("\n" + row + " ");
			for(int col = 0; col < model.getNumCols(); col++) {
				System.out.print("_ ");
			}
			System.out.print("\t " + row + " ");
			for(int col = 0; col < model.getNumCols(); col++) {
				System.out.print("  ");
			}
		}
	}

	public void printBoard() {
		System.out.println(model.hasClicked());
		if(model.hasClicked()) {
			System.out.print("  ");
			int counter = 0;
			for(int i = 0; i < model.getNumCols(); i++) {
				if(i == 10) counter = 0;
				System.out.print(counter + " ");
				counter++;
			}
			System.out.print("\t   ");
			counter = 0;
			for(int i = 0; i < model.getNumCols(); i++) {
				if(i == 10) counter = 0;
				System.out.print(counter + " ");
				counter++;
			}
			for(int row = 0; row < model.getNumRows(); row++) {
				System.out.print("\n" + row + " ");
				for(int col = 0; col < model.getNumCols(); col++) {
					String alive = "_ ";
					if(!model.isCovered(row, col) && model.getNumAdjMines(row, col) == 0) alive = "  ";
					else if(!model.isCovered(row, col) && model.getNumAdjMines(row, col) > 0) {
						alive = model.getNumAdjMines(row, col) + " ";
					}
					else if(model.isFlag(row, col)) alive = "F ";
					System.out.print(alive);
				}
				System.out.print("\t " + row + " ");
				for(int col = 0; col < model.getNumCols(); col++) {
					String alive = "  ";
					if(!model.isMine(row, col) && model.getNumAdjMines(row, col) > 0) {
						alive = model.getNumAdjMines(row, col) + " ";
					}else if(model.isMine(row, col)) alive = "* ";
					System.out.print(alive);
				}
			}
		}else{
			printDummyBoard();
		}
	}

	public void play() {
		System.out.println("        _                                                   \n"
				+ "  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __ \n"
				+ " /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|\n"
				+ "/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |   \n"
				+ "\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|   \n"
				+ "                                           |_|              \n");
		while(!model.hasLost() && !model.hasWon()) {
			Scanner in = new Scanner(System.in);
			printBoard();
			System.out.println("\n\nThere are " + (model.getTotalMines() - model.getTotalFlags()) + " mines left.");
			System.out.print("Would you like to flag a cell or reveal a cell?\nEnter 'f' or 'r' > ");
			String next = in.next();
			System.out.print("Enter row: ");
			int row = in.nextInt();
			System.out.print("Enter col: ");
			int col = in.nextInt();
			if(!model.hasClicked()) model.setClicked(true, row, col);
			if(next.equals("f")) {
				model.setFlag(row, col, true);
			}else if(next.equals("r")) {
				model.reveal(row, col);
				if(model.isMine(row, col)) {
					model.gameOver();
					Scanner userIn = new Scanner(System.in);
					System.out.print("\nYou Lose!\n\nWant to play again? (Enter 'y' or 'n')");
					String s = userIn.next();
					if(s.equals("y")) {
						model.resetGame(numRows, numCols, numMines);
					}else if(s.equals("n")) {
						System.out.println("Goodbye. Thanks for playing!");
					}
				}
				else if(model.allSafeCellsRemoved()) {
					model.gameWon();
					Scanner userIn = new Scanner(System.in);
					System.out.println("\nYou Won!\n\nWant to play again? (Enter 'y' or 'n')");
					String s = userIn.next();
					if(s.equals("y")) {
						model.resetGame(numRows, numCols, numMines);
					}else if(s.equals("n")) {
						System.out.println("Goodbye. Thanks for playing!");
					}
				}
			}else {
				System.out.println("Invalid input;");
			}
		}
	}

}
