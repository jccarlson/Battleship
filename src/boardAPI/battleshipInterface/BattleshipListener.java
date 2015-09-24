package boardAPI.battleshipInterface;

public interface BattleshipListener {
	
	public void shotFired(BattleshipEvent e);
	public void gameOver(BattleshipEvent e);
	public void shipMoved(BattleshipEvent e);
}