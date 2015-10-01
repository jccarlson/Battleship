package boardAPI.battleshipInterface;

import boardAPI.Ship;

/**
 * Describes an event on a BattleshipBoard. 
 * 
 * @author Jason Carlson
 * @version 2.1
 * @since 2015-09-23
 */
public class BattleshipEvent {
	
	/** Constants which can be bitwise OR'd together to create an {@link event}. */
	public static final int GAME_OVER = 1;
	public static final int HIT = 2;
	public static final int MISS = 4;
	public static final int INVALID = 8;
	public static final int SHIP_SUNK = 16;
	public static final int SHIP_MOVED = 32;
	
	/** The Ships described by this event, if necessary. Ship is current ship at location, prevShip is identical but at former location */ 
	private Ship ship = null;
	private Ship prevShip = null;
	
	/** Contains one or more bits set to correspond to constants above */
	private int event;
	
	/** row and column of the event on the board, if necessary */
	private int row = -1, col = -1;
	
	/** Creates an event which is described only by one or more event codes. Convenient for gameOver(e)
	 * @param e the event code
	 */
	public BattleshipEvent(int e) {
		event = e;
	}
	
	/** Creates an event which is described by an event code and a row and column of a board. Convenient for shotFired(e) without SHIP_SUNK
	 * @param e the event code
	 * @param r the row of the event
	 * @param c the column of the event
	 */
	public BattleshipEvent(int e, int r, int c) {
		this(e);
		row = r;
		col = c;
	}
	
	/** Creates an event which is described by an event code, row, column, and Ship. Convenient for shotFired(e) with SHIP_SUNK
	 * @param e the event code
	 * @param r the row of the event
	 * @param c the column of the event
	 * @param s the ship affected
	 */
	public BattleshipEvent(int e, int r, int c, Ship s) {
	
		this(e,r,c);
		ship = s;
	}
	
	/** Creates an event described by an event code, the previous ship location, and a Ship. Convenient for shipMoved(e) 
	 * @param e the event code
	 * @param p the previous ship
	 * @param s the current ship
	 */
	public BattleshipEvent(int e, Ship p, Ship s) {
		this(e);
		ship = s;
		prevShip = p;
	}
	
	/** Generic Constructor for all possible Events 
	 * @param e the event code
	 * @param r the row of the event
	 * @param c the column of the event
	 * @param p the previous ship
	 * @param s the current ship
	 */
	public BattleshipEvent(int e, int r, int c, Ship p, Ship s) {
		this(e,r,c, s);
		prevShip = p;
	}
	
	/**
	 * Gets the ship at its current location
	 * @return ship
	 */
	public Ship getShip() {
		return ship;
	}
	
	/**
	 * getter for the event code.
	 * @return event
	 */
	public int getEvent() {
		return event;
	}
	
	/**
	 * getter for the row
	 * @return row
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * getter for the column
	 * @return col
	 */
	public int getCol() {
		return col;
	}

	/**
	 * gets the ship at it's previous location
	 * @return prevShip
	 */
	public Ship getPrevShip() {
		return prevShip;
	}
}
