package boardGUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import boardAPI.Ship;
import boardAPI.battleshipInterface.BattleshipEvent;
import boardAPI.battleshipInterface.BattleshipListener;

@SuppressWarnings("serial")
public class ImprovedStatusPanel extends JPanel implements BattleshipListener {
	JPanel [] statusContainers;
	JLabel [] shipStatus;
	ShipDisplay [] hitGraphics;
	final JLabel whitespace = new JLabel("          ");
	JLabel shotsFired;
	String [] names;
	int shots;
	ColorScheme colors;
	
	public ImprovedStatusPanel(Iterable <Ship> ships, ColorScheme c) {
		super();
		colors = c;
		int size = 0;
		
		LinkedList <String> names = new LinkedList<String>();
		for(Ship s: ships) {
			size++;
			names.add(s.getName());
		}		
		this.names = names.toArray(new String[0]);
		shipStatus = new JLabel[size];
		hitGraphics = new ShipDisplay[size];
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		statusContainers = new JPanel[size];
		Iterator<Ship> s = ships.iterator();
		for(int i = 0; i < hitGraphics.length; i++) {
			hitGraphics[i] = new ShipDisplay(s.next().getSize(), 10, colors);
		}
		
		shots = 0;
		
		for(int i = 0; i < shipStatus.length; i++) {
			statusContainers[i] = new JPanel();
			statusContainers[i].add(shipStatus[i] = new JLabel(this.names[i]));
			statusContainers[i].add(hitGraphics[i]);
			add(statusContainers[i]);
		}
		
		shotsFired = new JLabel("Shots Fired: " + shots);
		add(whitespace);
		add(shotsFired);
	//	this.setPreferredSize(new Dimension(200,200));
	}

	public void reset() {
		
		for(int i = 0; i < hitGraphics.length; i++) {
			hitGraphics[i].reset();
		}
		
		shots = 0;
		
		shotsFired.setText("Shots Fired: " + shots);
		
		repaint();		
	}

	@Override
	public void shotFired(BattleshipEvent e) {
		if(!((e.getEvent() & BattleshipEvent.INVALID) == BattleshipEvent.INVALID)) {
			shots++;
			shotsFired.setText("Shots Fired: " + shots);
		}
		
		if(((e.getEvent() & BattleshipEvent.HIT) == BattleshipEvent.HIT)) {
			String n = e.getShip().getName();
			for(int i = 0; i < names.length; i++) {
				if (n.equals(names[i])) {
					for(int j = 0; j < e.getShip().getSize(); j++) {
						if(e.getShip().isHit(j)) {
							hitGraphics[i].squareAt(j).click('H');
						}
					}
					
					break;					
				}
			}
		}		
	}

	@Override
	public void gameOver(BattleshipEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shipMoved(BattleshipEvent e) {
		// TODO Auto-generated method stub
		
	}

	private class ShipDisplay extends JPanel {
		/** The scale in pixels and gridSize of the board. */
		private int scale;
		private int gridSize;
		
		/** The colorscheme of the board */
		ColorScheme boardColors;
		
		/** The BoardSquares which make up the board */
		BoardSquare [] squares;
		
		public ShipDisplay(int gs, int s, ColorScheme cs) {
			super();
			scale = s;
			gridSize = gs;
			
			// Set the preferred size of this component to the width and height of the board in pixels,
			// based on the gridSize and scale.
			setPreferredSize(new Dimension(gridSize * scale + 1, scale + 1));
			
			squares = new BoardSquare[gridSize];
			boardColors = cs;
			
			// populate the display with BoardSquares
			for(int i=0; i < gridSize; i++) {
				squares[i] = new BoardSquare(0, i, scale, i*scale, 0, boardColors);
			}
		}
		
		/**
		 * Resets the visual representation of the board.
		 */
		public void reset() {
			
			for(int i=0; i < gridSize; i++) {
				squares[i].reset();
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
				squares[i].paint(g);
			}
		}
		
		public BoardSquare squareAt(int c) {
			return squares[c];	
		}
	}	
}
