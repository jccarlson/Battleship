package boardAPI;

/**
 * Describes an instance of a ship in a game of Battleship.
 * <p>
 * Implements a ship of the kind used when playing a game of Battleship.
 * Contains fields and methods which describe the name, size, position,
 * orientation, and hit status of a particular ship.
 * 
 * @author Jason Carlson <jcarlson08 @ gmail.com>
 * @version 1.0
 * @since 2015-08-30
 */

public class Ship {

	// fields

	/** The name of the ship. */
	private String name;
	/** The size of the ship. */
	private int size;
	/** Location of the ship's top left square. */
	private int row, column;
	/** Tracks hits on the ship. */
	private boolean[] isHit;
	/** {@code true} if vertical orientation, {@code false} if horizontal. */
	private boolean isVertical;

	/**
	 * Class Constructor
	 * 
	 * @param name
	 *            The name of the ship to be constructed.
	 * @param size
	 *            The size of the ship.
	 * @param row
	 *            The row containing the top left corner of the ship.
	 * @param column
	 *            The column containing the top left corner of the ship
	 * @param isVertical
	 *            {@code true} if ship is vertically oriented, {@code false} if
	 *            horizontal.
	 */
	public Ship(String name, int size, int row, int column, boolean isVertical) {
		this.name = name;
		this.size = size;
		this.row = row;
		this.column = column;
		isHit = new boolean[size];
		for (int i = 0; i < size; i++) {
			isHit[i] = false;
		}
		this.isVertical = isVertical;
	}

	// methods

	/**
	 * Moves a ship from one location/orientation to another.
	 * 
	 * @param row
	 *            The row to move the upper left corner of the ship to.
	 * @param column
	 *            The column to move the upper left corner of the ship to.
	 * @param isVertical
	 *            {@code true} if ship is to be oriented vertically,
	 *            {@code false} otherwise.
	 */
	public void move(int row, int column, boolean isVertical) {
		this.row = row;
		this.column = column;
		this.isVertical = isVertical;
	}

	/**
	 * Checks whether a ship occupies a space on a board given by a row and
	 * column.
	 * 
	 * @param row
	 *            The row of the board containing the space to check.
	 * @param column
	 *            The column of the board containing the space to check.
	 * @return {@code true} if the space denoted by (row,column) is occupied by
	 *         this ship, {@code false} otherwise.
	 */
	public boolean occupies(int row, int column) {
		if (isVertical) {
			if ((column == this.column) && (this.row <= row) && (this.row + (size - 1) >= row))
				return true;
		} else {
			if ((row == this.row) && (this.column <= column) && (this.column + (size - 1) >= column))
				return true;
		}
		return false;
	}

	/**
	 * Fires a shot at ({@code row}, {@code column}) and returns whether this
	 * ship was hit, reflecting the change in {@code isHit[]}.
	 * 
	 * @param row
	 *            The row at which to shoot.
	 * @param column
	 *            The column at which to shoot.
	 * @return {@code true} if this ship was hit, {@code false} otherwise.
	 */
	public boolean tryShot(int row, int column) {
		boolean hit = occupies(row, column);
		if (hit) {
			if (isVertical)
				isHit[row - this.row] = true;
			else
				isHit[column - this.column] = true;
		}
		return hit;
	}

	/**
	 * Checks the {@code isHit[]} array for a hit at a particular section of the
	 * ship.
	 * 
	 * @param i
	 *            The index of the section of the ship to check. On a grid, this
	 *            section is {@code i} spaces right of ({@code row},
	 *            {@code column}) if {@code isVertical} == {@code false}, or \
	 *            {@code i} spaces below ({@code row}, {@code column}) if
	 *            {@code true}.
	 * @return {@code true} if that grid of the ship has been hit, {@code false}
	 *         otherwise.
	 */
	public boolean isHit(int i) {
		if ((i < 0) || (i > (size - 1)))
			return false;
		return isHit[i];
	}

	/**
	 * Checks whether the ship is sunk.
	 * 
	 * @return {@code true} if all indices of {@code isHit[]} are {@code true},
	 *         {@code false} otherwise.
	 */
	public boolean isSunk() {
		for (int i = 0; i < size; i++) {
			if (!isHit[i])
				return false;
		}
		return true;
	}

	/**
	 * Getter for {@code size}.
	 * 
	 * @return The size of the ship in grid square units.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Getter for {@code row}.
	 * 
	 * @return The row containing the top left corner of the ship.
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Getter for {@code column}.
	 * 
	 * @return The column containing the top left corner of the ship.
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Getter for {@code name}.
	 * 
	 * @return The name of the ship.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for {@code isVertical}.
	 * 
	 * @return {@code true} if ship is oriented vertically, {@code false} if
	 *         oriented horizontally.
	 */
	public boolean isVertical() {
		return isVertical;
	}

	/**
	 * Creates a new ship which has the same fields as this ship.
	 * 
	 * @return a new {@link Ship} with the exact same field values as this one.
	 */
	public Ship clone() {
		return new Ship(name, size, row, column, isVertical);
	}

}
