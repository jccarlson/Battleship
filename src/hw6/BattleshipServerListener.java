package hw6;

import java.util.List;

/**
 * Large interface for receiving messages from a Battleship Server
 * @author Jason Carlson
 * @version 1.0
 * @since 2015-12-01
 */
public interface BattleshipServerListener {

	/**
	 * called if the connection is bad for some reason
	 */
	void badConnectionMsg();

	/**
	 * called if the disconnect process is bad for some reason
	 */
	void badDisconnectMsg();

	/**
	 * called if handler receives an invalid message from the server
	 */
	void invalidMessageReceived();

	/**
	 * called when user_ack: is received
	 * @param accepted true if 1st param is true
	 * @param reason optional reason parameter
	 */
	void acknowledgeUserMsg(boolean accepted, String reason);

	/**
	 * called when size: is received
	 * @param size the size of the board
	 */
	void sizeMsg(int size);

	/**
	 * called when strategies: is received
	 * @param strategies a List of available strategies
	 */
	void updateStrategies(List<String> strategies);

	/**
	 * called when users: is received
	 * @param users the list of user names
	 * @param userAvailable whether the corresponding user is available to play
	 */
	void updateUsers(List<String> users, List<Boolean> userAvailable);

	/**
	 * called when ack_strategy_play: is received
	 * @param accepted true if 1st param is true
	 * @param reason optional 2nd param
	 */
	void acknowledgeStrategyPlayMsg(boolean accepted, String reason);
	
	/**
	 * called when ack_user_play: is received
	 * @param accepted true if 1st param is true
	 * @param reason optional 2nd param
	 */
	void acknowledgeUserPlayMsg(boolean accepted, String reason);

	/**
	 * called when ack_ships: is receivedt
	 * @param accepted true if 1st param is true
	 * @param reason optional 2nd param
	 */
	void acknowledgeShipsMsg(boolean accepted, String reason);

	/**
	 * called when turn: is received
	 * @param playerTurn true if it's the player's turn
	 */
	void turnMsg(boolean playerTurn);

	/**
	 * called when ack_shot: received
	 * @param row row of the shot
	 * @param col col of the shot
	 * @param shipHit true if ship was hit
	 * @param shipSunk true if ship was sunk
	 * @param gameOver true if player won 
	 */
	void acknowledgeShotMsg(int row, int col, boolean shipHit, boolean shipSunk,
			boolean gameOver);

	/**
	 * called when shot: received
	 * @param row row of shot
	 * @param col col of shot
	 * @param shipHit true if ship was hit
	 * @param shipSunk true if ship was sunk
	 * @param gameOver true if opponent won
	 */
	void shotMsg(int row, int col, boolean shipHit, boolean shipSunk,
			boolean gameOver);

	/**
	 * called when gg: received
	 */
	void ggMsg();

	/**
	 * called when ack_gg: received
	 */
	void acknowledgeGGMsg();

	/**
	 * called when bye: received
	 */
	void byeMsg();

	/**
	 * called when disconnected: received
	 */
	void disconnectedMsg();

	/**
	 * called when user_play: received 
	 * @param oppName the opponent who wants to play
	 */
	void userPlayRequestMsg(String oppName);

}
