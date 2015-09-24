package boardAPI.battleshipInterface;

/**
 * The interface for a BattleshipListener. 
 * <p>
 * Allows the implementing class to receive events from a BattleshipBoard.
 * 
 * @author Jason Carlson <jccarlson @ miners.utep.edu>
 * @version 1.0
 * @since 2015-09-24
 */
public interface BattleshipListener {
	
	/**
	 * Method Called when a shot is fired at the board.
	 * 
	 * @param e 
	 * 			A BattleshipEvent which will contain the code for HIT, MISS, or INVALID and
	 * 			possibly SHIP_SUNK. Will contain row and col and if SHIP_SUNK will contain the
	 * 			sunken Ship.
	 */
	public void shotFired(BattleshipEvent e);
	
	/**
	 * Method called when the game is over (all ships sunk).
	 * @param e
	 * 			A BattleshipEvent which will contain the code for GAME_OVER.
	 */
	public void gameOver(BattleshipEvent e);
	
	/**
	 * Method called when a ship is moved. Not currently implemented.
	 * 
	 * @param e
	 * 			A BattleshipEvent which will contain the code for SHIP_MOVED and the Ship.
	 */
	public void shipMoved(BattleshipEvent e);
}