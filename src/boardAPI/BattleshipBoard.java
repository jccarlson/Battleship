package boardAPI;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Describes an instance of an {@code n*n} grid square board on which one can
 * play a game of Battleship.
 * <p>
 * Contains methods which can be used to place several instances of class
 * {@link Ship} at various non-overlapping positions and orientations on the
 * board, as well as fire shots at various locations, and retrieve the current
 * status of the board and its ships.
 * 
 * @author Jason Carlson <jcarlson08 @ gmail.com>
 * @version 1.0
 * @since 2015-08-30
 */
public class BattleshipBoard {

	// fields

	/** The size of the board */
	private int n;
	/** The number of ships which will be on the board */
	private int numShips;
	/** number of shots which have been fired at the board */
	private int numShots;
	/** linked list of {@link Ship} objects which are on the board */
	private LinkedList<Ship> fleet;
	/** Matrix of characters which describe the current state of the board */
	private char[][] status;
	/**
	 * if the board is locked, no ships can be moved or added and shots can be
	 * fired
	 */
	private boolean locked;

	// Constructor

	/**
	 * Constructor for BattleshipBoard.
	 * 
	 * @param n
	 *            The size of both dimensions of the board. All boards must be
	 *            square.
	 * @param numShips
	 *            The number of ships which will be in the fleet.
	 */
	public BattleshipBoard(int n, int numShips) {
		this.n = n;
		this.numShips = numShips;
		this.numShots = 0;
		fleet = new LinkedList<Ship>();
		status = new char[n][n];
		locked = false;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				status[i][j] = ' ';
			}
		}
	}

	// methods

	/**
	 * Adds a new {@link Ship} to the fleet.
	 * <p>
	 * Only available when {@code locked == false}.
	 * 
	 * @param newShip
	 *            {@code Ship} object to add to the fleet.
	 * @return {@code true} if added successfully, {@code false} otherwise.
	 */
	public boolean addShip(Ship newShip) {

		// is the board unlocked?
		if (locked)
			return false;

		// do we have enough ships already?
		if (fleet.size() >= numShips)
			return false;

		int r = newShip.getRow();
		int c = newShip.getColumn();
		int s = newShip.getSize();

		// is it within the boundaries of the board?
		if (newShip.isVertical()) {
			if (c < 0 || c >= n)
				return false;
			if (r < 0 || (r + s - 1) >= n)
				return false;
		} else {
			if (r < 0 || r >= n)
				return false;
			if (c < 0 || (c + s - 1) >= n)
				return false;
		}

		// does it conflict with other ships?

		ListIterator<Ship> l = fleet.listIterator();
		while (l.hasNext()) {
			Ship i = l.next();
			if (i.getName().equalsIgnoreCase(newShip.getName()))
				return false;

			if (newShip.isVertical()) {
				for (int j = 0; j < s; j++) {
					if (i.occupies(r + j, c))
						return false;
				}
			}

			else {
				for (int j = 0; j < s; j++) {
					if (i.occupies(r, c + j))
						return false;
				}
			}
		}

		fleet.add(newShip);
		return true;
	}

	/**
	 * Adds a new {@link Ship} to the fleet based on parameters.
	 * <p>
	 * To add a {@code Ship} object which has already been defined, see
	 * {@link #addShip(Ship)}.
	 * <p>
	 * Only available when {@code locked == false}.
	 * 
	 * @param name
	 *            The name of the new ship.
	 * @param size
	 *            The size of the new ship.
	 * @param row
	 *            The row of the top left corner of the new ship.
	 * @param column
	 *            The column of the top left corner of the new ship.
	 * @param isVertical
	 *            {@code true} if the ship is oriented vertically, {@code false}
	 *            if horizontal.
	 * @return {@code true} if ship is successfully added, {@code false}
	 *         otherwise.
	 */
	public boolean addShip(String name, int size, int row, int column, boolean isVertical) {
		if (locked)
			return false;
		Ship t = new Ship(name, size, row, column, isVertical);
		return addShip(t);
	}

	/**
	 * Removes a ship based on the case-insensitive name given.
	 * <p>
	 * Only available when {@code locked == false}.
	 * 
	 * @param name
	 *            The name of the ship to be removed (case insensitive).
	 * @return {@link Ship} object which was removed (null if not found or
	 *         locked).
	 */
	public Ship removeShip(String name) {

		if (locked)
			return null;

		int index = -1;
		ListIterator<Ship> l = fleet.listIterator();
		while (l.hasNext()) {
			if (l.next().getName().equalsIgnoreCase(name)) {
				index = l.previousIndex();
			}
		}

		if (index == -1)
			return null;

		return fleet.remove(index);
	}

	/**
	 * Moves a ship given by a case insensitive name to the location given by
	 * (row,column) with the given orientation, if possible, or does nothing.
	 * <p>
	 * Only available when {@code locked == false}.
	 * 
	 * @param name
	 *            The name of the ship to move (case insensitive).
	 * @param row
	 *            The row containing the new location of the upper left corner
	 *            of the ship.
	 * @param column
	 *            The column containing the new location of the upper left
	 *            corner of the ship.
	 * @param isVertical
	 *            {@code true} if the ship is oriented vertically, {@code false}
	 *            if horizontal.
	 * @return {@code true} if moved successfully, {@code false} otherwise.
	 */
	public boolean moveShip(String name, int row, int column, boolean isVertical) {
		if (locked)
			return false;

		Ship oldShip = removeShip(name);
		if (oldShip == null)
			return false;

		Ship newShip = oldShip.clone();
		newShip.move(row, column, isVertical);

		if (addShip(newShip)) {
			return true;
		}

		addShip(oldShip);
		return false;

	}

	/**
	 * Locks the board, preventing adding, moving, or removing ships. Enables
	 * shots to be fired.
	 * <p>
	 * Only possible if {@code numShips} is equal to the size of {@code fleet}.
	 * 
	 * @return {@code true} if successfully locked, {@code false} otherwise.
	 */
	public boolean lockBoard() {
		if (fleet.size() == numShips)
			return locked = true;
		return false;
	}

	/**
	 * Checks the current status of the board at (row,column).
	 * <p>
	 * Can be used regardless of whether the board is locked or not.
	 * 
	 * @param row
	 *            The row of the space to check.
	 * @param column
	 *            The column of the space to check.
	 * @return ' ' if no shot has been fired, 'H' if a hit was recorded, or 'M'
	 *         if a miss was recorded.
	 */
	public char checkStatus(int row, int column) {

		if (row < 0 || row >= n)
			return ' ';
		if (column < 0 || column >= n)
			return ' ';
		return status[row][column];
	}

	/**
	 * Fires a shot, if valid, at (row, column), and updates the shot counter.
	 * <p>
	 * Valid shots are those within the boundaries of the board, where a shot
	 * hasn't been fired previously.
	 * <p>
	 * To check if the shot hit or missed, use {@link #checkStatus(int, int)}.
	 * 
	 * @param row
	 *            The row of the space to shoot at.
	 * @param column
	 *            The column of the space to shoot at.
	 * @return {@code true} if a valid shot was fired, {@code false} otherwise.
	 */
	public boolean fireShot(int row, int column) {

		// valid shot?
		if (!locked)
			return false;
		if (row < 0 || row >= n)
			return false;
		if (column < 0 || column >= n)
			return false;
		if (status[row][column] != ' ')
			return false;
		
		numShots++;

		// is it a hit?
		ListIterator<Ship> l = fleet.listIterator();
		while (l.hasNext()) {
			if (l.next().tryShot(row, column)) {
				status[row][column] = 'H';
				return true;
			}
		}

		// it was a miss.
		status[row][column] = 'M';
		return true;
	}

	/**
	 * Gets the names of all the ships on the board.
	 * 
	 * @return an array of Strings containing the names of all the ships on the
	 *         board.
	 */
	public String[] getShipNames() {

		int i;
		String[] names = new String[numShips];
		for (i = 0; i < names.length; i++) {
			names[i] = "";
		}
		i = 0;
		ListIterator<Ship> l = fleet.listIterator();
		while (l.hasNext()) {
			names[i] = l.next().getName();
			i++;
		}

		return names;
	}

	/**
	 * Gets an array showing whether each ship in the fleet is sunk or not.
	 * 
	 * @return an array of booleans which are {@code true} if that ship is sunk,
	 *         {@code false} otherwise.
	 */
	public boolean[] getShipSunkStatus() {

		int i;
		boolean[] sunkStatus = new boolean[numShips];
		for (i = 0; i < sunkStatus.length; i++) {
			sunkStatus[i] = false;
		}
		i = 0;
		ListIterator<Ship> l = fleet.listIterator();
		while (l.hasNext()) {
			sunkStatus[i] = l.next().isSunk();
			i++;
		}

		return sunkStatus;
	}
	
	/**
	 * returns the number of (valid) shots fired at the board.
	 * @return the number of (valid) shots fired at the board.
	 * @see #fireShot(int, int)
	 */
	public int getNumShots() {
		return numShots;
	}
	
	/**
	 * returns the square dimension of the board.
	 * @return n - the size of the board in units.
	 */
	public int getSize() {
		return n;
	}
		
	/**
	 * Determines if this board has lost the game (all ships are sunk).
	 * 
	 * @return {@code true} if all ships are sunk, {@code false} otherwise.
	 */
	public boolean gameLost() {
		boolean[] sunkStatus = getShipSunkStatus();
		for (int i = 0; i < sunkStatus.length; i++) {
			if (!sunkStatus[i])
				return false;
		}
		return true;
	}
}