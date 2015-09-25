package hw3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import boardAPI.BattleshipBoard;
import boardAPI.Ship;
import boardAPI.battleshipInterface.BattleshipEvent;
import boardAPI.battleshipInterface.BattleshipListener;
import boardGUI.BoardPanel;
import boardGUI.BoardSquare;
import boardGUI.ColorScheme;
import boardGUI.StatusPanel;
import sound.WavPlayer;


/**
 * An Applet which plays a game of Battleship, using the boardGUI package for visual elements
 * and the boardAPI package for game logic. Sound is handled with the sound package.
 * 
 * @author Jason Carlson
 * @version 2.1
 * @since 2015-09-24 
 */
@SuppressWarnings("serial")
public class BattleshipApplet extends JApplet implements MouseListener, BattleshipListener{
	
	/** Runnable WavPlayers for playing threaded sound clips */
	private final WavPlayer hitPlay = new WavPlayer("/ship_hit.wav");
	private final WavPlayer sinkPlay = new WavPlayer("/ship_sunk.wav");
	
	/** The logical game board */
	private BattleshipBoard logicalBoard;
	
	/** The visual game board */
	private BoardPanel guiBoard;
	
	/** The visual Status Panel */
	private StatusPanel sPanel;
	
	
	/** To start a new game. */
	private final JButton playButton = new JButton("RESET");
	
	/** Message bar to display the number of shots and the outcome. */
	private final JLabel msgBar = new JLabel("");
	
	/** true if all ships sunk */
	private boolean isGameOver;
	
	/** 
	 * called upon initialization of the applet
	 */
	@Override
	public void init() {
		// create a new logical board of size 10 with 5 ships.
		logicalBoard = new BattleshipBoard(10, 5);
		
		// create the GUI BoardPanel
		guiBoard = new BoardPanel(logicalBoard.getSize(), 20, new ColorScheme(new Color(50, 50, 255), Color.RED, Color.WHITE, Color.BLACK));
		guiBoard.setVisibleShips(true);
		
	
		// Add BattleshipListeners to the logical board which need to know about events.
		logicalBoard.addBattleshipListener(guiBoard);
		logicalBoard.addBattleshipListener(this);
		
		// place the ships randomly on the board.
		placeShips();
				
		// lock the board so no more ships can be placed or moved. 
		logicalBoard.lockBoard();
		
		// create the status panel
		sPanel = new StatusPanel(logicalBoard.getShipNames());
		logicalBoard.addBattleshipListener(sPanel);
		
		// configure the reset button to call resetButtonClicked()
		playButton.addActionListener(this::resetButtonClicked);
		
		// the game is not over.
		isGameOver = false;
				
		// Make this applet a MouseListener for the visual board so it knows when it's clicked.
		guiBoard.addMouseListener(this);
		
		// Place the components in their appropriate positions. 
		configureGui();
	}
	
	/** Configure UI consisting of a play button, a board panel, a status panel, and a message Bar. */
	private void configureGui() {
		setLayout(new BorderLayout());
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttons.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
		buttons.add(playButton);
		
		add(buttons, BorderLayout.NORTH);
		guiBoard.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(guiBoard, BorderLayout.CENTER);
		sPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(sPanel, BorderLayout.WEST);
		msgBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(msgBar, BorderLayout.SOUTH);
	}
	
	/**Display the given message to the message bar. */
	private void displayMessage(String msg) {
		msgBar.setText(msg);
	}
	
	/**
	 * creates a new instance of BattleshipBoard in logicalBoard, resetting it.
	 * Adds the visual elements to the new logicalBoard's listeners.
	 * Resets all the visual elements. Sets gameOver to false.
	 * 
	 * @param event
	 */
	private void resetButtonClicked(ActionEvent event) {
		
		logicalBoard = new BattleshipBoard(10, 5);
		guiBoard.reset();
		guiBoard.setVisibleShips(true);
		sPanel.reset();
		
		logicalBoard.addBattleshipListener(guiBoard);
		logicalBoard.addBattleshipListener(sPanel);
		logicalBoard.addBattleshipListener(this);
		
		placeShips();
		logicalBoard.lockBoard();				
		
		isGameOver = false;
						
		displayMessage("");
				
		repaint();
	}
	
	/** Randomly places 5 ships with predetermined names and sizes on the logicalBoard. */
	private void placeShips() {
		// create the ships

		Ship[] fleet = new Ship[5];

		// creates a new ship named "Aircraft Carrier", which is 5 units long and 
		// horizontal, at (0,0)
		fleet[0] = new Ship("Aircraft Carrier", 5, 0, 0, false);
		fleet[1] = new Ship("Battleship", 4, 0, 0, false);
		fleet[2] = new Ship("Frigate", 3, 0, 0, false);
		fleet[3] = new Ship("Submarine", 3, 0, 0, false);
		fleet[4] = new Ship("Minesweeper", 2, 0, 0, false);
		
		// randomly add the ships to the board and keep trying until a valid placement
		// is found
		Random rng = new Random();
		int n = logicalBoard.getSize();
		for (int i = 0; i < fleet.length; i++) {

			boolean validShip = false;
			
			while (!validShip) {

				fleet[i].move(rng.nextInt(n), rng.nextInt(n), rng.nextBoolean());
				validShip = logicalBoard.addShip(fleet[i]);
			}			
		}
	}

	/**
	 * Gets the coordinates of a click, passes them to the guiBoard which maps them to a 
	 * BoardSquare, then attempts to fire a shot at that square. 
	 * 
	 * @param e
	 * 			A MouseEvent which contains which button was pressed and the coordinates.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1 && !isGameOver) {
			BoardSquare s = guiBoard.squareAt(e.getX(), e.getY());
			logicalBoard.fireShot(s.ROW, s.COL);
		}
			
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Processes the results of a shot fired at the logical board, and updates the GUI
	 * if needed. 
	 * 
	 * @param e
	 * 			This BattleshipEvent contains the status of the shot, the coordinates of 
	 * 			the shot, whether a Ship was sunk, and which Ship was sunk.
	 */
	@Override
	public void shotFired(BattleshipEvent e) {
		
		// determine if the shot was a hit, miss, or invalid.
		char status = ' ';
		
		if ((e.getEvent() & BattleshipEvent.HIT) == BattleshipEvent.HIT)
			status = 'H';

		if ((e.getEvent() & BattleshipEvent.MISS) == BattleshipEvent.MISS)
			status = 'M';
		
		if ((e.getEvent() & BattleshipEvent.INVALID) == BattleshipEvent.INVALID)
			status = ' ';
		
		// if a hit, say it's a hit
		if(status == 'H') {
			String dispMsg = "SHOT FIRED! IT'S A HIT!!! ";
			boolean playHit = true;
			
			// if a ship was sunk append "You sunk the <shipName>!!!" to the display message
			if ((e.getEvent() & BattleshipEvent.SHIP_SUNK) == BattleshipEvent.SHIP_SUNK) {
				dispMsg += "You sunk the " + e.getShip().getName() + "!!! ";
				playHit = false;
			}
			// display the completed message in the message bar. 
			displayMessage(dispMsg);
			
			// play sounds
			if (playHit) {
				new Thread(hitPlay).start();
			}
			else
				new Thread(sinkPlay).start();
			
		}
		
		// if a miss say it's a miss.
		if(status == 'M') displayMessage("SHOT FIRED! MISS!");
		
		// if the shot was invalid, say you can't shoot there. 
		if(status == ' ') displayMessage("Can't shoot there...");
		
	}

	/**
	 * Display GAME OVER in the message bar if all ships are sunk.
	 */
	@Override
	public void gameOver(BattleshipEvent e) {
		isGameOver = true;
		displayMessage("GAME OVER.");		
	}

	@Override
	public void shipMoved(BattleshipEvent e) {
		// TODO Auto-generated method stub
		
	}
}
