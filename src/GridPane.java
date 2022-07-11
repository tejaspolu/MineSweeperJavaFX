import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class can be used to display the data stored in a GridModel of Boolean values as
 * a grid of rectangles with customizable true and false colors.
 * When setModel is called, a BooleanGridPane will add itself as a listener to the model
 * so it can ensure that the data it displays always reflects the data in the model.
 * @author Ted_McLeod
 *
 */
public class GridPane extends Group{
	/** The size of each tile in pixels */
	private double tileSize;

	/** The color to show in cells that have a value of true in the model */
	private Image coveredImage;

	/** The color to show in cells that have a value of false in the model */
	private Image uncoveredImage;

	/** The rectangles shown in the grid */
	private ImageView[][] cells;

	/** The model */
	private MSModel model;

	/**
	 * Construct an empty BooleanGridPane with no model
	 */
	public GridPane() {
		this.model = null;
		this.cells = null;
		this.tileSize = 10;
		try {
			this.coveredImage = new Image(new FileInputStream("minesweeper_images/blank.gif"));
			this.uncoveredImage = new Image(new FileInputStream("minesweeper_images/num_0.gif"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the color to show in cells that have a value of true in the model
	 * and update the cells to reflect the change.
	 * @param color the color to show in cells that have a value of true in the model
	 */
	public void setCoveredImage(Image image) {
		this.coveredImage = image;
		for (int row = 0; row < model.getNumRows(); row++) {
			for (int col = 0; col < model.getNumCols(); col++) {
				if (model.isCovered(row, col)) cells[row][col].setImage(image);
			}
		}
	}

	/**
	 * Get the color to show in cells that have a value of true in the model
	 * @return the color to show in cells that have a value of true in the model
	 */
	public Image getCoveredImage() {
		return this.coveredImage;
	}

	/**
	 * Set the color to show in cells that have a value of false in the model
	 * and update the cells to reflect the change.
	 * @param color the color to show in cells that have a value of false in the model
	 */
	public void setUncoveredImage(Image image) {
		this.uncoveredImage = image;
		for (int row = 0; row < model.getNumRows(); row++) {
			for (int col = 0; col < model.getNumCols(); col++) {
				if (!model.isCovered(row, col)) cells[row][col].setImage(image);
			}
		}
	}

	/**
	 * Get the color to show in cells that have a value of false in the model
	 * @return the color to show in cells that have a value of false in the model
	 */
	public Image getUncoveredImage() {
		return this.uncoveredImage;
	}

	/**
	 * Set the size of each tile in pixels.
	 * @param size the size of each tile in pixels
	 */
	public void setTileSize(double size) {
		this.tileSize = size;
		resetCells();
	}

	/**
	 * Get the size of each tile in pixels.
	 * @return the size of each tile in pixels.
	 */
	public double getTileSize() {
		return this.tileSize;
	}

	/**
	 * Sets the model to reflect in the grid. The BooleanGridPane will remove itself as a listener
	 * from any previous model and add itself to the new model. It will then reset cells to reflect
	 * the new model.
	 * @param model the new model
	 */
	public void setModel(MSModel model) {
		//		if (this.model != null) {
		//			this.model.removeListener(this);
		//		}
		//		model.addListener(this);
		this.model = model;
		resetCells();
	}

	/**
	 * This removes all previous cells and redraws the grid using the data
	 * from the model (if there is one)
	 */
	public void resetCells() {
		getChildren().remove(0, getChildren().size());
		if (model != null) {
			cells = new ImageView[model.getNumRows()][model.getNumCols()];
			for (int row = 0; row < model.getNumRows(); row++) {
				for (int col = 0; col < model.getNumCols(); col++) {
					ImageView cell = new ImageView();
					cell.setFitWidth(tileSize);
					cell.setFitHeight(tileSize);
					if(model.hasClicked()) {
						cell.setImage(model.isCovered(row, col) ? coveredImage : uncoveredImage);
					}else {
						cell.setImage(coveredImage);
					}
					cell.setX(tileSize * col);
					cell.setY(tileSize * row);
					getChildren().add(cell);
					cells[row][col] = cell;
				}
			}
		}
	}

	/**
	 * Returns the Rectangle at the given (row, col)
	 * @param row the row
	 * @param col the column
	 * @return the Rectangle at the given (row, col)
	 */
	public ImageView cellAtGridCoords(int row, int col) {
		return cells[row][col];
	}

	/**
	 * Returns the x coordinate of the left edge of the given column.
	 * @param col the column
	 * @return the x coordinate of the left edge of the given column.
	 */
	public double xPosForCol(int col) {
		return col * tileSize;
	}

	/**
	 * Returns the y coordinate of the top edge of the given row.
	 * @param row the row
	 * @return the y coordinate of the top edge of the given row.
	 */
	public double yPosForRow(int row) {
		return row * tileSize;
	}

	/**
	 * Returns the column that contains the given x coordinate
	 * @param x the x coordinate
	 * @return the column that contains the given x coordinate
	 */
	public int colForXPos(double x) {
		return (int)(x / tileSize);
	}

	/**
	 * Returns the row that contains the given y coordinate
	 * @param y the y coordinate
	 * @return the row that contains the given y coordinate
	 */
	public int rowForYPos(double y) {
		return (int)(y / tileSize);
	}

	/**
	 * Updates a cell in response to a change in the model.
	 */
	public void cellChanged(int row, int col, Boolean oldVal, Boolean newVal) {
		cells[row][col].setImage(newVal ? coveredImage : uncoveredImage);
	}

	/**
	 * Responds to the entire grid being replaced in the model by redrawing the whole grid.
	 */
	public void gridReplaced() {
		resetCells();
	}

}
