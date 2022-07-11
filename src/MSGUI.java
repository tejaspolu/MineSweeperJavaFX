import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MSGUI extends Application{

	MSModel model;
	private GridPane gridPane;
	private Stage stage;
	private Scene mainScene;
	private BorderPane bPane;
	private int totalTime;
	private Text minesLeft;
	private Text timeElapsed;
	private int tileSize = 30;
	private ImageView emoji;
	private VBox topPanel;
	private HBox topRow;
	private MenuBar menuBar;
	private AnimationTimer timer;
	private boolean timerStarted = false;
	private int[] clickedBombArr = new int[2];

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		
		
		model = new MSModel(10, 10, 10);
		stage.setTitle("Minesweeper");
		stage.setResizable(true);
		stage.sizeToScene();
		bPane = new BorderPane();

		topPanel = new VBox();
		Menu gameMenu = new Menu("Game");
		Menu optionsMenu = new Menu("Options");
		Menu helpMenu = new Menu("Help");
		MenuItem item1 = new MenuItem("New Beginner Game");
		item1.setOnAction(e -> {
			model.resetGame(8, 8, 10);
			gridPane.setModel(model);
			newGame();
			stage.setWidth(250);
			stage.setHeight(250);
		});
		MenuItem item2 = new MenuItem("New Intermediate Game");
		item2.setOnAction(e -> {
			model.resetGame(16, 16, 40);
			gridPane.setModel(model);
			newGame();
			stage.setWidth(665);
			stage.setHeight(700);
		});
		MenuItem item3 = new MenuItem("New Expert Game");
		item3.setOnAction(e -> {
			model.resetGame(16, 31, 50);
			gridPane.setModel(model);
			newGame();
			stage.setWidth(1000);
			stage.setHeight(700);
		});
		MenuItem item4 = new MenuItem("New Custom Game");
		item4.setOnAction(new CustomGameHandler());	
		MenuItem item5 = new MenuItem("Exit");
		item5.setOnAction(e -> Platform.exit());
		MenuItem item6 = new MenuItem("Set Number of Mines");
		item6.setOnAction(new NumberOfMinesHandler());
		MenuItem item7 = new MenuItem("About");
		item7.setOnAction(new AboutHandler());
		MenuItem item8 = new MenuItem("How to Play");
		item8.setOnAction(new InstructionsHandler());
		gameMenu.getItems().addAll(item1, item2, item3, item4, item5);
		optionsMenu.getItems().addAll(item6);
		helpMenu.getItems().addAll(item7, item8);
		menuBar = new MenuBar(gameMenu, optionsMenu, helpMenu);


		topRow = new HBox();
		topRow.setSpacing(15);
		topRow.setPadding(new Insets(15,0,15,0));
		topRow.setAlignment(Pos.BASELINE_CENTER);
		minesLeft = new Text("Mines Remaining: " + (model.getTotalMines() - model.getTotalFlags()));
		minesLeft.setFont(Font.font("Verdana", 16));
		emoji = new ImageView(new Image(new FileInputStream("minesweeper_images/face_smile.gif"))); 
		emoji.setOnMouseClicked(e -> {
			model.resetGame(model.getNumRows(), model.getNumCols(), model.getTotalMines());
			gridPane.setModel(model);
			newGame();
			
		});
		emoji.setFitWidth(50);
		emoji.setFitHeight(50);
		timeElapsed = new Text("Time Elapsed: " + totalTime);

		timeElapsed.setFont(Font.font("Verdana", 16));
		topRow.getChildren().addAll(minesLeft, emoji, timeElapsed);
		topPanel.getChildren().addAll(menuBar, topRow);
		bPane.setTop(topPanel);

		gridPane = new GridPane();
		gridPane.setModel(model);
		tileSize = 30;
		gridPane.setTileSize(tileSize);
		gridPane.setOnMousePressed(new GridMouseHandler());
		bPane.setCenter(gridPane);
		gridPane.requestFocus();

		mainScene = new Scene(bPane);
		stage.setScene(mainScene);
		stage.show();
		stage.setWidth(500);
		stage.setHeight(500);
		stage.setMinWidth(mainScene.getWidth());
		stage.setMinHeight(mainScene.getHeight());

		timer = new AnimationTimer() {
			private long then = Long.MIN_VALUE;
			public void handle(long now) {
				if(then == Long.MIN_VALUE) {
					then = now;
				} else {
					long dt = now - then;
					if(dt >= (long) (1e9)) {
						totalTime++;
						timeElapsed.setText("Time Elapsed: " + totalTime);
						then = now;
					}
				}
			}
		};
	}

	public void newGame() {
		timerStarted = false;
		timer.stop();
		totalTime = 0;
		timeElapsed.setText("Time Elapsed: " + totalTime);
		minesLeft.setText("Mines Remaining: " + (model.getTotalMines() - model.getTotalFlags()));
		try {
			emoji.setImage(new Image(new FileInputStream("minesweeper_images/face_smile.gif")));
			emoji.setFitWidth(50);
			emoji.setFitHeight(50);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		stage.setWidth(stage.getWidth());
		stage.setHeight(stage.getHeight());
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
	}

	private class CustomGameHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			int numRows = 0;
			int numCols = 0;
			int numMines = 0;
			//rows
			TextInputDialog input = new TextInputDialog();
			input.setHeaderText("How many rows would you like?");
			input.showAndWait();
			try {
				numRows = Integer.parseInt(input.getEditor().getText());
				if(numRows <= 50 && numRows >= 4) {
					model.resetGame(model.getNumRows(), model.getNumCols(), numRows);
					gridPane.setModel(model);
					newGame();
				} else {
					Alert a;
					if(numRows < 4) a = new Alert(AlertType.ERROR, "Not Enough Rows!", ButtonType.OK);
					else a = new Alert(AlertType.ERROR, "Too Many Rows!", ButtonType.OK);
					a.showAndWait();
					return;
				}
			} catch (NumberFormatException i) {
				Alert a = new Alert(AlertType.WARNING, "Invalid Input.", ButtonType.OK);
				a.showAndWait();
				return;
			}
			//cols
			input = new TextInputDialog();
			input.setHeaderText("How many columns would you like?");
			input.showAndWait();
			try {
				numCols = Integer.parseInt(input.getEditor().getText());
				if(numCols <= 50 && numCols >= 4) {
					model.resetGame(model.getNumRows(), model.getNumCols(), numCols);
					gridPane.setModel(model);
					newGame();
				} else {
					Alert a;
					if(numCols < 4) a = new Alert(AlertType.ERROR, "Not Enough Columns!", ButtonType.OK);
					else a = new Alert(AlertType.ERROR, "Too Many Columns!", ButtonType.OK);
					a.showAndWait();
					return;
				}
			} catch (NumberFormatException i) {
				Alert a = new Alert(AlertType.WARNING, "Invalid Input.", ButtonType.OK);
				a.showAndWait();
				return;
			}
			//mines
			input = new TextInputDialog();
			input.setHeaderText("How many mines would you like?");
			input.showAndWait();
			try {
				numMines = Integer.parseInt(input.getEditor().getText());
				if(numMines + 9 <= model.getNumRows() * model.getNumCols() && numMines >= 1) {
					model.resetGame(model.getNumRows(), model.getNumCols(), numMines);
					gridPane.setModel(model);
					newGame();
				} else {
					Alert a;
					if(numMines < 1) a = new Alert(AlertType.ERROR, "Not Enough Mines!", ButtonType.OK);
					else a = new Alert(AlertType.ERROR, "Too Many Mines!", ButtonType.OK);
					a.showAndWait();
					return;
				}
			} catch (NumberFormatException i) {
				Alert a = new Alert(AlertType.WARNING, "Invalid Input.", ButtonType.OK);
				a.showAndWait();
				return;
			}
			model.resetGame(numRows, numCols, numMines);
			gridPane.setModel(model);
			newGame();
		}
	}

	private class InstructionsHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
	        WebView webView = new WebView();
	        
	        webView.setPrefSize(550, 300);
			File f = new  File("instructions.html");
			String url = "file:///" + f.getAbsolutePath();
			webView.getEngine().load(url);
			
			Stage instructionsStage = new Stage();
			Scene scene = new Scene(webView);
			instructionsStage.setTitle("How to Play");
			instructionsStage.setScene(scene);
			instructionsStage.show();
			instructionsStage.sizeToScene();
			instructionsStage.setMinWidth(mainScene.getWidth());
			instructionsStage.setMinHeight(mainScene.getHeight());
		}
	}

	private class AboutHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			WebView webView = new WebView();
	        webView.setPrefSize(550, 300);
			File f = new File("about.html");
			String url = "file:///" + f.getAbsolutePath();
			webView.getEngine().load(url);
			
			Stage aboutStage = new Stage();
			Scene scene = new Scene(webView);
			aboutStage.setTitle("About");
			aboutStage.setScene(scene);
			aboutStage.show();
			aboutStage.sizeToScene();
			aboutStage.setMinWidth(mainScene.getWidth());
			aboutStage.setMinHeight(mainScene.getHeight());
		}
	}
	
	private class MouseEnterHandler implements EventHandler<MouseEvent> {
		public void handle(MouseEvent event) {
			try {
				emoji.setImage(new Image(new FileInputStream("minesweeper_images/face_ooh.gif")));
				emoji.setFitWidth(50);
				emoji.setFitHeight(50);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
		}
	}
	
	private class MouseExitHandler implements EventHandler<MouseEvent> {
		public void handle(MouseEvent event) {
			try {
				emoji.setImage(new Image(new FileInputStream("minesweeper_images/face_smile.gif")));
				emoji.setFitWidth(50);
				emoji.setFitHeight(50);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
		}
	}

	private class NumberOfMinesHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			TextInputDialog input = new TextInputDialog();
			input.setHeaderText("How many mines would you like?");
			input.showAndWait();
			try {
				int answer = Integer.parseInt(input.getEditor().getText());
				if(answer + 9 <= model.getNumRows() * model.getNumCols() && answer >= 1) {
					model.resetGame(model.getNumRows(), model.getNumCols(), answer);
					gridPane.setModel(model);
					newGame();
				} else {
					Alert a;
					if(answer < 1) a = new Alert(AlertType.ERROR, "Not Enough Mines!", ButtonType.OK);
					else a = new Alert(AlertType.ERROR, "Too Many Mines!", ButtonType.OK);
					a.showAndWait();
				}
			} catch (NumberFormatException i) {
				Alert a = new Alert(AlertType.WARNING, "Invalid Input.", ButtonType.OK);
				a.showAndWait();
			}
		}
	}

	private class GridMouseHandler implements EventHandler<MouseEvent>{
		public void handle(MouseEvent event) {
			if(!model.hasLost() && !model.hasWon()) {
				int row = gridPane.rowForYPos(event.getY());
				int col = gridPane.colForXPos(event.getX());
				if(event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
					if(event.getButton() == MouseButton.PRIMARY) {
						if(!timerStarted) {
							timer.start();
							timerStarted = true;
						}
						if(!model.hasClicked()) model.setClicked(true, row, col);
						model.reveal(row, col);
						winGame(row, col);
						updateView();
						if(model.isMine(row, col) && !model.isFlag(row, col) && !model.isQuestion(row, col)) {
							loseGame(row, col);
						}
					} if(event.getButton() == MouseButton.SECONDARY) {
						if(!model.isQuestion(row, col) && !model.isFlag(row, col)) {
							model.setFlag(row, col, true);
							minesLeft.setText("Mines Remaining: " + (model.getTotalMines() - model.getTotalFlags()));
						}
						else if(model.isFlag(row, col)) {
							model.setQuestion(row, col, true);
							model.setFlag(row, col, false);
						}
						else if(model.isQuestion(row, col)) {
							model.setQuestion(row, col, false);
						}
						if(!model.isCovered(row, col) && model.getNumAdjMines(row, col) > 0 && model.getNumAdjFlags(row, col) == model.getNumAdjMines(row, col)) {
							boolean isMine = model.revealWithFlag(row, col);
							if(isMine) loseGame(row, col);
							if(model.allSafeCellsRemoved()) winGame(row, col);
							updateView();
						}
						updateView();
					}
				} else {
					return;
				}
			}
		}
	}
	
	public void winGame(int row, int col) {
		if(model.allSafeCellsRemoved()) {
			timer.stop();
			model.gameWon();
			try {
				emoji.setImage(new Image(new FileInputStream("minesweeper_images/face_win.gif")));
				emoji.setFitWidth(50);
				emoji.setFitHeight(50);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			updateView();
			Alert a = new Alert(AlertType.NONE, "You Won! :)", ButtonType.OK);
			a.showAndWait();
		}
	}
	
	public void loseGame(int row, int col) {
		try {
			emoji.setImage(new Image(new FileInputStream("minesweeper_images/face_dead.gif")));
			emoji.setFitWidth(50);
			emoji.setFitHeight(50);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		timer.stop();
		model.gameOver();
		model.revealCell(row, col);
		clickedBombArr[0] = row;
		clickedBombArr[1] = col;
		updateView();
		model.revealUncoveredBombs();
		updateView();
		model.revealWrongFlags();
		updateView();
		Alert a = new Alert(AlertType.NONE, "You Lost :(", ButtonType.OK);
		a.showAndWait();
	}

	public void updateView() {
		try {
			for(int row = 0; row < model.getNumRows(); row++) {
				for(int col = 0; col < model.getNumCols(); col++) {
					if (model.isCovered(row, col)) {
						if (model.isFlag(row, col)) {
							Image flag = new Image(new FileInputStream("minesweeper_images/bomb_flagged.gif"));
							gridPane.cellAtGridCoords(row, col).setImage(flag); 
							gridPane.cellAtGridCoords(row, col).setOnMouseEntered(new MouseEnterHandler());
							gridPane.cellAtGridCoords(row, col).setOnMouseExited(new MouseExitHandler());
						} else if (model.isQuestion(row, col)) {
							Image question = new Image(new FileInputStream("minesweeper_images/bomb_question.gif"));
							gridPane.cellAtGridCoords(row, col).setImage(question);
							gridPane.cellAtGridCoords(row, col).setOnMouseEntered(new MouseEnterHandler());
							gridPane.cellAtGridCoords(row, col).setOnMouseExited(new MouseExitHandler());
						} else {
							gridPane.cellAtGridCoords(row, col).setImage(gridPane.getCoveredImage());
						}
					}else if (!model.isCovered(row, col)) {
						gridPane.cellAtGridCoords(row, col).setImage(gridPane.getUncoveredImage());
						if(model.isMine(row, col) && row == clickedBombArr[0] && col == clickedBombArr[1]) {
							Image bombDeath = new Image(new FileInputStream("minesweeper_images/bomb_death.gif"));
							gridPane.cellAtGridCoords(row, col).setImage(bombDeath);
						} else if(model.isMine(row, col) && !(row == clickedBombArr[0] && col == clickedBombArr[1])) {
							Image bombRevealed = new Image(new FileInputStream("minesweeper_images/bomb_revealed.gif"));
							gridPane.cellAtGridCoords(row, col).setImage(bombRevealed);
						} else if(model.isFlag(row, col)) {
							Image wrongFlag = new Image(new FileInputStream("minesweeper_images/bomb_wrong.gif"));
							gridPane.cellAtGridCoords(row, col).setImage(wrongFlag);
						} else if(model.getNumAdjMines(row, col) > 0) {
							if(model.getNumAdjMines(row, col) == 1) {
								Image num1 = new Image(new FileInputStream("minesweeper_images/num_1.gif"));
								gridPane.cellAtGridCoords(row, col).setImage(num1);
							}else if(model.getNumAdjMines(row, col) == 2) {
								Image num2 = new Image(new FileInputStream("minesweeper_images/num_2.gif"));
								gridPane.cellAtGridCoords(row, col).setImage(num2);
							}else if(model.getNumAdjMines(row, col) == 3) {
								Image num3 = new Image(new FileInputStream("minesweeper_images/num_3.gif"));
								gridPane.cellAtGridCoords(row, col).setImage(num3);
							}else if(model.getNumAdjMines(row, col) == 4) {
								Image num4 = new Image(new FileInputStream("minesweeper_images/num_4.gif"));
								gridPane.cellAtGridCoords(row, col).setImage(num4);
							}else if(model.getNumAdjMines(row, col) == 5) {
								Image num5 = new Image(new FileInputStream("minesweeper_images/num_5.gif"));
								gridPane.cellAtGridCoords(row, col).setImage(num5);
							}else if(model.getNumAdjMines(row, col) == 6) {
								Image num6 = new Image(new FileInputStream("minesweeper_images/num_6.gif"));
								gridPane.cellAtGridCoords(row, col).setImage(num6);
							}else if(model.getNumAdjMines(row, col) == 7) {
								Image num7 = new Image(new FileInputStream("minesweeper_images/num_7.gif"));
								gridPane.cellAtGridCoords(row, col).setImage(num7);
							}else if(model.getNumAdjMines(row, col) == 8) {
								Image num8 = new Image(new FileInputStream("minesweeper_images/num_8.gif"));
								gridPane.cellAtGridCoords(row, col).setImage(num8);
							}
						}
					}
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
