package boardGUI;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import boardAPI.BattleshipBoard;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

	private BattleshipBoard logicalBoard;
	private int scale;
	private int gridSize;
	ColorScheme boardColors;
	
	BoardSquare [][] squares;
	
	public BoardPanel(BattleshipBoard l, int s, ColorScheme cs) {
		super();
		logicalBoard = l;
		scale = s;
		gridSize = logicalBoard.getSize();
		setPreferredSize(new Dimension(gridSize * scale, gridSize * scale));
		squares = new BoardSquare[gridSize][gridSize];
		boardColors = cs;
		for(int i=0; i < gridSize; i++) {
			for(int j=0; j < gridSize; j++) {
				squares[i][j] = new BoardSquare(logicalBoard, i, j, scale, j * scale, i * scale, boardColors);
			}
		}
	}
	
	public void refresh() {
		
		for(int i=0; i < gridSize; i++) {
			for(int j=0; j < gridSize; j++) {
				squares[i][j].update();
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

	public void resetLogicalBoard(BattleshipBoard logicalBoard2) {
		
		logicalBoard = logicalBoard2;
		gridSize = logicalBoard.getSize();
		setPreferredSize(new Dimension(gridSize * scale, gridSize * scale));
		squares = new BoardSquare[gridSize][gridSize];
		
		for(int i=0; i < gridSize; i++) {
			for(int j=0; j < gridSize; j++) {
				squares[i][j] = new BoardSquare(logicalBoard, i, j, scale, j * scale, i * scale, boardColors);
			}
		}
		
	}
	
	public char clickAt(int x, int y) {
		int row = y / scale;
		int col = x / scale;
		if(row >= gridSize || col >= gridSize) return ' ';
		return squares[row][col].click();
		
	}
}
