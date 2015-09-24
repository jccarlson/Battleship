package boardGUI;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import boardAPI.battleshipInterface.BattleshipEvent;
import boardAPI.battleshipInterface.BattleshipListener;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel implements BattleshipListener{

	private int scale;
	private int gridSize;
	ColorScheme boardColors;
	
	BoardSquare [][] squares;
	
	public BoardPanel(int gs, int s, ColorScheme cs) {
		super();
		scale = s;
		gridSize = gs;
		setPreferredSize(new Dimension(gridSize * scale, gridSize * scale));
		squares = new BoardSquare[gridSize][gridSize];
		boardColors = cs;
		for(int i=0; i < gridSize; i++) {
			for(int j=0; j < gridSize; j++) {
				squares[i][j] = new BoardSquare(i, j, scale, j * scale, i * scale, boardColors);
			}
		}
	}
	
	public void reset() {
		
		for(int i=0; i < gridSize; i++) {
			for(int j=0; j < gridSize; j++) {
				squares[i][j].click(' ');
			}
		}
		//repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		for(int i=0; i < gridSize; i++) {
			for(int j=0; j < gridSize; j++) {
				squares[i][j].paint(g);
			}
		}
	}

	public BoardSquare clickAt(int x, int y) {
		int row = y / scale;
		int col = x / scale;
		
		if(row < gridSize || col < gridSize)
			return squares[row][col];
		return null;	
	}

	@Override
	public void shotFired(BattleshipEvent e) {
		char s = ' ';
		if((e.getEvent() & BattleshipEvent.HIT) == BattleshipEvent.HIT)
			s = 'H';
		if((e.getEvent() & BattleshipEvent.MISS) == BattleshipEvent.MISS)
			s = 'M';
		squares[e.getRow()][e.getCol()].click(s);
		
	}

	@Override
	public void gameOver(BattleshipEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shipMoved(BattleshipEvent e) {
		// TODO Auto-generated method stub
		
	}
}
