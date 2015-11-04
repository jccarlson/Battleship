package hw5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import boardAPI.BattleshipBoard;
import boardAPI.Ship;
import boardAPI.battleshipInterface.BattleshipEvent;
import boardGUI.*;
import hw3.BattleshipApplet;
import opponentAPI.Opponent;
import opponentAPI.*;

/**
 * An Applet which plays a game of Battleship against an AI, using the boardGUI
 * package for visual elements and the boardAPI package for game logic. AI is
 * handled by the opponentAPI package. Sound is handled with the sound package.
 * Extends the BattleshipApplet class from HW3.
 * 
 * @author Jason Carlson
 * @version 1.0
 * @since 2015-11-03
 * @see hw3.BattleshipApplet
 */
@SuppressWarnings("serial")
public class BattleshipAIApplet extends BattleshipApplet {

	/** the board the opponent will shoot at housing the players ships. */
	protected BattleshipBoard opponentBoard;
	/** the visual representation of the opponentBoard */
	protected BoardPanel oppGui;
	/** an Opponent Interface used to interact with the opponent */
	protected Opponent opp;
	/** true if it's the player's turn, false otherwise */
	protected boolean isPlayerTurn;
	/**
	 * an improved version of the StatusPanel which displays color coded squares
	 * as the ship's status
	 */
	protected ImprovedStatusPanel sPanel;
	/** the ColorScheme of the Board */
	protected ColorScheme colors = new ColorScheme(new Color(50, 50, 255), Color.RED, Color.WHITE, Color.DARK_GRAY,
			Color.BLACK);

	/**
	 * Initializes the applet with 2 boards, one for the opponent and one for
	 * the player, with the same set of 5 ships. Also sets up the GUI layout
	 * with a call to ConfigureGui();
	 */
	@Override
	public void init() {
		// create a new logical board of size 10 with 5 ships.
		logicalBoard = new BattleshipBoard(10, 5);
		opponentBoard = new BattleshipBoard(10, 5);
		opp = new RandomAI(new Dimension(opponentBoard.getSize(), opponentBoard.getSize()));

		// create the GUI BoardPanel
		guiBoard = new BoardPanel(logicalBoard.getSize(), 20, colors);
		guiBoard.setVisibleShips(VISIBLE_SHIPS);

		// create the opponent GUI BoardPanel
		oppGui = new BoardPanel(opponentBoard.getSize(), 10, colors);
		oppGui.setVisibleShips(true);

		// Add BattleshipListeners to the logical board which need to know about
		// events.
		logicalBoard.addBattleshipListener(guiBoard);
		logicalBoard.addBattleshipListener(this);

		opponentBoard.addBattleshipListener(oppGui);
		opponentBoard.addBattleshipListener(this);

		// place the ships randomly on the board.
		placeShips();

		// lock the board so no more ships can be placed or moved.
		logicalBoard.lockBoard();
		opponentBoard.lockBoard();

		// create the status panel
		sPanel = new ImprovedStatusPanel(opponentBoard.getFleet(), colors);
		opponentBoard.addBattleshipListener(sPanel);

		// configure the reset button to call resetButtonClicked()
		playButton.addActionListener(this::resetButtonClicked);

		// the game is not over.
		isGameOver = false;

		// Make this applet a MouseListener for the visual board so it knows
		// when it's clicked.
		guiBoard.addMouseListener(this);

		// Place the components in their appropriate positions.
		configureGui();

		displayMessage("PLAYER'S TURN");

		isPlayerTurn = true;
	}

	/**
	 * Configure UI consisting of a play button, a board panel, an opponent's
	 * board area with a status panel, and a message Bar.
	 */
	@Override
	protected void configureGui() {
		setLayout(new BorderLayout());
		JPanel topBuffer = new JPanel();
		JPanel buttonPanel = new JPanel();
		buttonPanel.setAlignmentY(JPanel.LEFT_ALIGNMENT);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		topBuffer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		topBuffer.add(oppGui);
		JPanel centerBuffer = new JPanel();
		centerBuffer.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
		centerBuffer.add(guiBoard);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		JPanel oppPanel = new JPanel();
		oppPanel.setLayout(new BoxLayout(oppPanel, BoxLayout.X_AXIS));
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		playButton.setHorizontalAlignment(JButton.LEFT);
		buttonPanel.add(playButton);
		topPanel.add(buttonPanel);
		sPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		oppPanel.add(sPanel);
		oppPanel.add(topBuffer);
		oppPanel.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Opponent: " + opp.getName()));
		;
		topPanel.add(oppPanel);
		add(topPanel, BorderLayout.NORTH);
		add(centerBuffer, BorderLayout.CENTER);
		msgBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		msgBar.setAlignmentY(JLabel.LEFT_ALIGNMENT);
		msgBar.setHorizontalAlignment(JLabel.LEFT);
		add(msgBar, BorderLayout.SOUTH);
	}

	/**
	 * Randomly places 5 ships with predetermined names and sizes on the
	 * logicalBoard and on the opponentBoard.
	 */
	@Override
	protected void placeShips() {
		super.placeShips();
		Ship[] fleet = new Ship[5];

		// creates a new ship named "Aircraft Carrier", which is 5 units long
		// and
		// horizontal, at (0,0)
		fleet[0] = new Ship("Aircraft Carrier", 5, 0, 0, false);
		fleet[1] = new Ship("Battleship", 4, 0, 0, false);
		fleet[2] = new Ship("Frigate", 3, 0, 0, false);
		fleet[3] = new Ship("Submarine", 3, 0, 0, false);
		fleet[4] = new Ship("Minesweeper", 2, 0, 0, false);

		// randomly add the ships to the board and keep trying until a valid
		// placement
		// is found
		Random rng = new Random();
		int n = opponentBoard.getSize();
		for (int i = 0; i < fleet.length; i++) {

			boolean validShip = false;

			while (!validShip) {

				fleet[i].move(rng.nextInt(n), rng.nextInt(n), rng.nextBoolean());
				validShip = opponentBoard.addShip(fleet[i]);
			}
		}
	}

	/**
	 * Overrides super.mouseClicked(e) in order to check if it's the players
	 * turn before calling
	 * 
	 * @see hw3.BattleshipApplet
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (isPlayerTurn)
			super.mouseClicked(e);
	}

	/**
	 * Creates a new instance of BattleshipBoard in logicalBoard and
	 * opponentBoard, resetting them. Prompts the user for confirmation if the
	 * game isn't over yet. Adds the visual elements to the new logicalBoard's
	 * and opponentBoard's listeners. Resets all the visual elements. Sets
	 * gameOver to false.
	 * 
	 * @param event
	 */
	@Override
	protected void resetButtonClicked(ActionEvent event) {
		int response = JOptionPane.YES_OPTION;
		if (!isGameOver) {
			response = JOptionPane.showConfirmDialog(this, "Do you want to reset?", "Confirm",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		}

		if (response == JOptionPane.YES_OPTION) {
			logicalBoard = new BattleshipBoard(10, 5);
			opponentBoard = new BattleshipBoard(10, 5);
			guiBoard.reset();
			oppGui.reset();
			guiBoard.setVisibleShips(VISIBLE_SHIPS);
			oppGui.setVisibleShips(true);
			sPanel.reset();

			logicalBoard.addBattleshipListener(guiBoard);
			logicalBoard.addBattleshipListener(this);

			opponentBoard.addBattleshipListener(oppGui);
			opponentBoard.addBattleshipListener(this);
			opponentBoard.addBattleshipListener(sPanel);

			placeShips();
			logicalBoard.lockBoard();
			opponentBoard.lockBoard();

			isGameOver = false;

			opp = new RandomAI(new Dimension(opponentBoard.getSize(), opponentBoard.getSize()));

			displayMessage("PLAYER'S TURN.");
			isPlayerTurn = true;
			repaint();
		}
	}

	/**
	 * Processes the results of a shot fired at the logical board or
	 * opponentBoard, and updates the GUI if needed. Also lets the opponent
	 * shoot once it's their turn.
	 * 
	 * @param e
	 *            This BattleshipEvent contains the status of the shot, the
	 *            coordinates of the shot, whether a Ship was sunk, and which
	 *            Ship was sunk.
	 */
	@Override
	public void shotFired(BattleshipEvent e) {
		super.shotFired(e);
		if (isPlayerTurn) {
			if (((e.getEvent() & BattleshipEvent.MISS) == BattleshipEvent.MISS)) {

				isPlayerTurn = false;
				while (!isPlayerTurn && !isGameOver) {
					displayMessage(msgBar.getText() + " OPPONENT'S TURN");
					Point shot = opp.getNextShot();
					opponentBoard.fireShot(shot.y, shot.x);
					repaint();
				}
			} else
				displayMessage(msgBar.getText() + " PLAYER'S TURN");

		} else {
			opp.sendShotResult(e);
			if (!((e.getEvent() & BattleshipEvent.HIT) == BattleshipEvent.HIT)) {
				isPlayerTurn = true;
				displayMessage(msgBar.getText() + " PLAYER'S TURN");
			}
		}
		repaint();
	}

	/**
	 * Display GAME OVER in the message bar if all ships are sunk, plus who won.
	 * 
	 * @param e
	 *            The BattleshipEvent describing the game over status.
	 */
	@Override
	public void gameOver(BattleshipEvent e) {
		super.gameOver(e);
		String winner;

		if (isPlayerTurn)
			winner = "Player";
		else
			winner = "Opponent";

		displayMessage(msgBar.getText() + " Winner: " + winner);
	}
}
