package boardGUI;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import boardAPI.battleshipInterface.BattleshipEvent;
import boardAPI.battleshipInterface.BattleshipListener;

/**
 * Visual representation of a {@link boardAPI.BattleshipBoard} which extends JPanel
 * <p>
 * Allows for varying amounts of BoardSquares and has a scale attribute which determines how large
 * each BoardSquare should be painted. 
 * 
 * @author Jason Carlson
 * @version 2.1
 * @since 2015-09-24 
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel implements BattleshipListener{
	
	/** The scale in pixels and gridSize of the board. */
	private int scale;
	private int gridSize;
	
	/** The colorscheme of the board */
	ColorScheme boardColors;
	
	/** The BoardSquares which make up the board */
	BoardSquare [][] squares;
	
	/** Show ships from the BattleshipBoard this panel is listening to? */
	private boolean visShips = false;
		
	/**
	 * Constructor for BoardPanel
	 * 
	 * @param gs
	 * 			The gridSize. should be equal to a {@link BattleshipBoard.getSize()}
	 * @param s
	 * 			The scale of each BoardSquare in pixels.
	 * @param cs
	 * 			A {@link ColorScheme} describing the colors of the board.
	 */
	public BoardPanel(int gs, int s, ColorScheme cs) {
		super();
		scale = s;
		gridSize = gs;
		
		// Set the preferred size of this component to the width and height of the board in pixels,
		// based on the gridSize and scale.
		setPreferredSize(new Dimension(gridSize * scale, gridSize * scale));
		
		squares = new BoardSquare[gridSize][gridSize];
		boardColors = cs;
		
		// populate the board with BoardSquares
		for(int i=0; i < gridSize; i++) {
			for(int j=0; j < gridSize; j++) {
				squares[i][j] = new BoardSquare(i, j, scale, j * scale, i * scale, boardColors);
			}
		}
	}
	
	/**
	 * Resets the visual representation of the board.
	 */
	public void reset() {
		
		for(int i=0; i < gridSize; i++) {
			for(int j=0; j < gridSize; j++) {
				squares[i][j].reset();
			}
		}
		//repaint();
	}
	
	/** 
	 * Overrides JComponent's paint(g) method, in order to properly paint the board to the screen.
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		
		for(int i=0; i < gridSize; i++) {
			for(int j=0; j < gridSize; j++) {
				squares[i][j].paint(g);
			}
		}
	}

	/**
	 * Gets a BoardSquare by relative pixel location.
	 * 
	 * @param x
	 * 			x-coordinate of the pixel which was clicked in relation to the top-left of this 
	 * 			component.
	 * @param y
	 * 			y-coordinate of the pixel which was clicked in relation to the top-left of this 
	 * 			component.
	 * @return the BoardSquare which was clicked.
	 */
	public BoardSquare squareAt(int x, int y) {
		int row = y / scale;
		int col = x / scale;
		
		if(row < gridSize && col < gridSize)
			return squares[row][col];
		return null;	
	}
	
	/**
	 * Gets a BoardSquare by logical board location.
	 * 
	 * @param x
	 * 			x-coordinate of the pixel which was clicked in relation to the top-left of this 
	 * 			component.
	 * @param y
	 * 			y-coordinate of the pixel which was clicked in relation to the top-left of this 
	 * 			component.
	 * @return the BoardSquare which was clicked.
	 */
	public BoardSquare squareAtGrid(int r, int c) {
		return squares[r][c];	
	}
	
	public void setVisibleShips(boolean s) {
		visShips = s;
		if(!visShips) {
			for(int r = 0; r < gridSize; r++) {
				for(int c = 0; c < gridSize; c++) {
					this.squareAtGrid(r, c).setHasShip(false);
				}					
			}
		}
	}
	
	/**
	 * shotFired method required by BattleshipListener Interface. 
	 * <p>
	 * Lets the appropriate BoardSquare know it was fired at, and the status of that shot, so it 
	 * can change color if needed.
	 *  
	 * @see boardAPI.battleshipInterface.BattleshipListener#shotFired(boardAPI.battleshipInterface.BattleshipEvent)
	 */
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
		if(((e.getEvent() & BattleshipEvent.SHIP_MOVED) == BattleshipEvent.SHIP_MOVED) && visShips) {
			for(int r = 0; r < gridSize; r++) {
				for(int c = 0; c < gridSize; c++) {
					if((e.getPrevShip() != null) && e.getPrevShip().occupies(r, c)) {
						this.squareAtGrid(r, c).setHasShip(false);
					}
					
					if(e.getShip().occupies(r, c)) {
						this.squareAtGrid(r, c).setHasShip(true);
					}
					
				}
			}
		}
		
	}
}
