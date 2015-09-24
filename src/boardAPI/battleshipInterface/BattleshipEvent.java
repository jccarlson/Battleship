package boardAPI.battleshipInterface;

import boardAPI.Ship;

public class BattleshipEvent {
	public static final int GAME_OVER = 1;
	public static final int HIT = 2;
	public static final int MISS = 4;
	public static final int INVALID = 8;
	public static final int SHIP_SUNK = 16;
	public static final int SHIP_MOVED = 32;
	
	private Ship ship;
	private int event;
	private int row = -1, col = -1;
	
	public BattleshipEvent(int e) {
		event = e;
	}
	
	public BattleshipEvent(int e, int r, int c) {
		this(e);
		row = r;
		col = c;
	}
	
	public BattleshipEvent(int e, int r, int c, Ship s) {
		this(e,r,c);
		ship = s;
	}
	
	public BattleshipEvent(int e, Ship s) {
		this(e);
		ship = s;
	}
	
	public Ship getShip() {
		return ship;
	}
	
	public int getEvent() {
		return event;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
}
