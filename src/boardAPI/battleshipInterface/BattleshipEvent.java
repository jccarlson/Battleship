package boardAPI.battleshipInterface;

import boardAPI.Ship;

public class BattleshipEvent {
	
	/** Constants which can be bitwise OR'd together to creat an {@link event}. */
	public static final int GAME_OVER = 1;
	public static final int HIT = 2;
	public static final int MISS = 4;
	public static final int INVALID = 8;
	public static final int SHIP_SUNK = 16;
	public static final int SHIP_MOVED = 32;
	
	/** The Ship described by this event, if necessary */ 
	private Ship ship = null;
	
	/** Contains one or more bits set to correspond to constants above */
	private int event;
	
	/** row and column of the event on the board, if necessary */
	private int row = -1, col = -1;
	
	/** Creates an event which is described only by one or more event codes. */
	public BattleshipEvent(int e) {
		event = e;
	}
	
	/** Creates an event which is described by an event code and a row and column of a board */
	public BattleshipEvent(int e, int r, int c) {
		this(e);
		row = r;
		col = c;
	}
	
	/** Creates an event which is described by an event code, row, column, and Ship */
	public BattleshipEvent(int e, int r, int c, Ship s) {
		this(e,r,c);
		ship = s;
	}
	
	/** Creates an event described by an event code and a Ship */
	public BattleshipEvent(int e, Ship s) {
		this(e);
		ship = s;
	}
	
	/**
	 * Getter for ship
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
}
