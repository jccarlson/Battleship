package hw6;

import java.util.List;

public interface BattleshipServerListener {

	void badConnectionMsg();

	void badDisconnectMsg();

	void invalidMessageReceived();

	void acknowledgeUserMsg(boolean accepted, String reason);

	void sizeMsg(int size);

	void updateStrategies(List<String> strategies);

	void updateUsers(List<String> users, List<Boolean> userAvailable);

	void acknowledgeStrategyPlayMsg(boolean accepted, String reason);
	
	void acknowledgeUserPlayMsg(boolean accepted, String reason);

	void acknowledgeShipsMsg(boolean accepted, String reason);

	void turnMsg(boolean playerTurn);

	void acknowledgeShotMsg(int row, int col, boolean shipHit, boolean shipSunk,
			boolean gameOver);

	void shotMsg(int row, int col, boolean shipHit, boolean shipSunk,
			boolean gameOver);

	void ggMsg();

	void acknowledgeGGMsg();

	void byeMsg();

	void disconnectedMsg();

	void userPlayRequestMsg(String string);

}
