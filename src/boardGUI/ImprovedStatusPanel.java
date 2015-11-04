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

	/**
	 * the Panels which contain the Labels of the ship names plus the visual
	 * status representations
	 */
	JPanel[] statusContainers;
	/** The Labels which contain the names of the ships */
	JLabel[] shipStatus;
	/** The visual status representations of the ships */
	ShipDisplay[] hitGraphics;
	/** whitespace */
	final JLabel whitespace = new JLabel("          ");
	/** The Label which displays the number of shots fired by the opponent */
	JLabel shotsFired;
	/** The names of the ships */
	String[] names;
	/** The number of shots fired */
	int shots;
	/** The ColorScheme for hitGraphics */
	ColorScheme colors;

	/**
	 * Constructor to make the status panel
	 * 
	 * @param ships
	 *            The ships to display
	 * @param c
	 *            The ColorScheme of the visual status bars
	 */
	public ImprovedStatusPanel(Iterable<Ship> ships, ColorScheme c) {
		super();
		colors = c;
		int size = 0;

		LinkedList<String> names = new LinkedList<String>();
		for (Ship s : ships) {
			size++;
			names.add(s.getName());
		}
		this.names = names.toArray(new String[0]);
		shipStatus = new JLabel[size];
		hitGraphics = new ShipDisplay[size];
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		statusContainers = new JPanel[size];
		Iterator<Ship> s = ships.iterator();
		for (int i = 0; i < hitGraphics.length; i++) {
			hitGraphics[i] = new ShipDisplay(s.next().getSize(), 10, colors);
		}

		shots = 0;

		for (int i = 0; i < shipStatus.length; i++) {
			statusContainers[i] = new JPanel();
			statusContainers[i].add(shipStatus[i] = new JLabel(this.names[i]));
			statusContainers[i].add(hitGraphics[i]);
			add(statusContainers[i]);
		}

		shotsFired = new JLabel("Shots Fired: " + shots);
		shotsFired.setAlignmentY(JLabel.LEFT_ALIGNMENT);
		shotsFired.setHorizontalAlignment(JLabel.LEFT);
		add(whitespace);
		add(shotsFired);
		// this.setPreferredSize(new Dimension(200,200));
	}

	/**
	 * Resets the panel to a default state, with no ships hit and 0 shots fired.
	 */
	public void reset() {

		for (int i = 0; i < hitGraphics.length; i++) {
			hitGraphics[i].reset();
		}

		shots = 0;

		shotsFired.setText("Shots Fired: " + shots);

		repaint();
	}

	/**
	 * Event handler which updates the visual status bars when a shot is fired,
	 * if necessary
	 * 
	 * @param e
	 *            The event describing the shot which was fired
	 * @see boardAPI.battleshipInterface.BattleshipListener
	 */
	@Override
	public void shotFired(BattleshipEvent e) {
		if (!((e.getEvent() & BattleshipEvent.INVALID) == BattleshipEvent.INVALID)) {
			shots++;
			shotsFired.setText("Shots Fired: " + shots);
		}

		if (((e.getEvent() & BattleshipEvent.HIT) == BattleshipEvent.HIT)) {
			String n = e.getShip().getName();
			for (int i = 0; i < names.length; i++) {
				if (n.equals(names[i])) {
					for (int j = 0; j < e.getShip().getSize(); j++) {
						if (e.getShip().isHit(j)) {
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

	/**
	 * Private Class describing the visual status bars displayed next to the
	 * ship names.
	 * 
	 * @author Jason Carlson *
	 */
	private class ShipDisplay extends JPanel {
		/** The scale in pixels and size in places of the display. */
		private int scale;
		private int size;

		/** The colorscheme of the board */
		ColorScheme boardColors;

		/** The BoardSquares which make up the board */
		BoardSquare[] squares;

		/**
		 * Constructs a new display
		 * 
		 * @param gs
		 *            the size of the ship
		 * @param s
		 *            the scale of each of the squares
		 * @param cs
		 *            the ColorScheme of the display
		 */
		public ShipDisplay(int gs, int s, ColorScheme cs) {
			super();
			scale = s;
			size = gs;

			// Set the preferred size of this component to the width and height
			// of the board in pixels,
			// based on the gridSize and scale.
			setPreferredSize(new Dimension(size * scale + 1, scale + 1));

			squares = new BoardSquare[size];
			boardColors = cs;

			// populate the display with BoardSquares
			for (int i = 0; i < size; i++) {
				squares[i] = new BoardSquare(0, i, scale, i * scale, 0, boardColors);
			}
		}

		/**
		 * Resets the visual representation of the display.
		 */
		public void reset() {

			for (int i = 0; i < size; i++) {
				squares[i].reset();
			}
			// repaint();
		}

		/**
		 * Overrides JComponent's paint(g) method, in order to properly paint
		 * the board to the screen.
		 * 
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		public void paintComponent(Graphics g) {

			for (int i = 0; i < size; i++) {
				squares[i].paint(g);
			}
		}

		/**
		 * Returns the BoardSquare at c
		 * 
		 * @param c
		 *            the place of the square
		 * @return the square at the cth place
		 */
		public BoardSquare squareAt(int c) {
			return squares[c];
		}
	}
}
