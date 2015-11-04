package opponentAPI;

import java.awt.Point;

import boardAPI.battleshipInterface.BattleshipEvent;

/**
 * Interface for interacting with Opponents
 * 
 * @author Jason Carlson (jccarlson@miners.utep.edu)
 * @version 1.0
 * @since 2015-11-01
 */
public interface Opponent {

	/**
	 * Returns the Name of the Opponent.
	 * 
	 * @return <code>String</code> representing the Opponent's name
	 */
	public String getName();

	/**
	 * Gets the next shot of the opponent as a <code>Point</code> object.
	 * 
	 * @return <code>Point</code> object containing the x,y coordinates of the
	 *         next shot
	 */
	public Point getNextShot();

	/**
	 * Sends the result of the shot to the Opponent
	 * 
	 * @param e
	 *            <code>BattleshipEvent</code> describing the results of the
	 *            shot
	 * @see boardAPI.battleshipInterface.BattleshipEvent
	 */
	public void sendShotResult(BattleshipEvent e);

	/**
	 * gets a string representation of the strategy the opponent is using
	 * 
	 * @return <code>String</code> representation of the opponents strategy
	 */
	public String getStrategy();
}
