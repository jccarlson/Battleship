package hw6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import boardAPI.BattleshipBoard;
import boardAPI.Ship;
import boardAPI.battleshipInterface.BattleshipEvent;
import boardGUI.BoardPanel;
import boardGUI.BoardSquare;
import boardGUI.ColorScheme;
import boardGUI.ImprovedStatusPanel;
import sound.WavPlayer;

@SuppressWarnings("serial")
public class BattleshipClient extends JFrame implements BattleshipServerListener {
	
	/** The server application */
	private edu.utep.cs.cs3331.hw5.server.Main server;
	
	/** The player's logical board */
	private BattleshipBoard logicalBoard;
	
	/** The player's graphical board */
	private BoardPanel playerGuiBoard;
	
	/** The opponent's graphical board */
	private BoardPanel opponentGuiBoard;
	
	/** The ColorScheme of the GUIs */
	private static final ColorScheme COLORS = new ColorScheme(new Color(50,50,200), Color.red, Color.white, Color.darkGray, Color.black);
	
	/** The Dimension of the Client Window */
	private static final Dimension DIMENSION = new Dimension(340, 560);
	
	/** The status panel for the player's ships */
	private ImprovedStatusPanel statusPanel;
	
	/** Buttons for the toolbar */
	private JButton playButton, stopButton, launchServerButton, connectServerButton;
	
	/** Names of the player and opponent */
	private String playerName, oppName;

	/** true if there is a game happening */
	private boolean gameInProgress;
	
	/** true if it's the player's turn */
	private boolean playerTurn;
	
	/** the status bar at the bottom of the screen. displays player turn and server info */
	private JLabel statusBar;
	
	/** the area with the player's board and status. useful for updating player's name */
	private JPanel playerArea;
	
	/** the area with the opponent's board and status. useful for updating opponent's name */
	private JPanel opponentArea;
	
	/** the handler class for the server */
	private BattleshipServerHandler serverHandler;
	
	/** sound effects */
	private WavPlayer hitPlayer = new WavPlayer("/ship_hit.wav");
	private WavPlayer sunkPlayer = new WavPlayer("/ship_sunk.wav");
	
	/** updating list of users to play with */
	private List <String> availableUsers;
	
	/** updating list of strategies to play with */
	private List <String> availableStrats;

	/** true if connected to a server */
	private boolean connected;
	
	/** true if a response from the server to the user: msg has arrived */
	private boolean nameAcknowledged;
	
	/** true if the server accepted the name */
	private boolean nameAccepted;
	
	/** sets initial vales and window behavior */
	public BattleshipClient() {
		super();
		playerName = "Player";
		oppName = "None";
		gameInProgress = false;
		playerTurn = false;
		connected = false;
		nameAccepted = false;
		nameAcknowledged = false;
		this.setTitle("Battleship");
		this.setJMenuBar(buildMenu());
		buildGui();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * lays out the GUI for the JFrame
	 */
	private void buildGui() {
		
		//set the size of the window
		this.setSize(DIMENSION);
		
		//get the drawing area for the frame where we lay out components
		Container contentPane = this.getContentPane();
		
		//set a Border Layout
		contentPane.setLayout(new BorderLayout());
		
		//create the toolbar with buttons
		JPanel buttonBar = new JPanel();
		buttonBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		buttonBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		((FlowLayout)buttonBar.getLayout()).setHgap(0);
		
		Dimension button_size = new Dimension(32,32);
		
		playButton = new JButton(new ImageIcon(BattleshipClient.class.getResource("/play.png")));
		playButton.addActionListener(e -> playNewGame());
		playButton.setPreferredSize(button_size);
		playButton.setToolTipText("Play a New Game");
		playButton.setEnabled(true);
		buttonBar.add(playButton);
		
		stopButton = new JButton(new ImageIcon(BattleshipClient.class.getResource("/quit.png")));
		stopButton.addActionListener(e -> resignGame());
		stopButton.setPreferredSize(button_size);
		stopButton.setToolTipText("Resign the Current Game");
		stopButton.setEnabled(false);
		buttonBar.add(stopButton);
		
		connectServerButton = new JButton(new ImageIcon(BattleshipClient.class.getResource("/connect_server.png")));
		connectServerButton.addActionListener(e -> connectServer());
		connectServerButton.setPreferredSize(button_size);
		connectServerButton.setToolTipText("Connect to a Battleship Server");
		buttonBar.add(connectServerButton);
		
		launchServerButton = new JButton(new ImageIcon(BattleshipClient.class.getResource("/launch_server.png")));
		launchServerButton.addActionListener(e -> launchServer());
		launchServerButton.setPreferredSize(button_size);
		launchServerButton.setToolTipText("Launch a Local Battleship Server");
		buttonBar.add(launchServerButton);
		
		contentPane.add(buttonBar, BorderLayout.NORTH);
		
		//Create the player's board and status panel
		playerArea = new JPanel();
		playerArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Player: " + playerName));
		playerArea.setLayout(new FlowLayout(FlowLayout.LEFT));
		((FlowLayout)playerArea.getLayout()).setHgap(20);
		LinkedList <Ship> fleet = new LinkedList<>();
		fleet.add(new Ship("Aircraft carrier", 5, 0, 0, false));
		fleet.add(new Ship("Battleship", 4, 0, 0, false));
		fleet.add(new Ship("Frigate", 3, 0, 0, false));
		fleet.add(new Ship("Submarine", 3, 0, 0, false));
		fleet.add(new Ship("Minesweeper", 2, 0, 0, false));
		
		statusPanel = new ImprovedStatusPanel(fleet, COLORS);
		
		playerArea.add(statusPanel);
		
		playerGuiBoard = new BoardPanel(10, 10, COLORS);
		playerGuiBoard.setVisibleShips(true);
		playerArea.add(playerGuiBoard);	
		
		contentPane.add(playerArea, BorderLayout.CENTER);	
		
		//Create the Opponent's Board (where the player will fire shots)
		JPanel bottomHalf = new JPanel();
		bottomHalf.setLayout(new BorderLayout(10,10));
		
		opponentArea = new JPanel();
		opponentArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Opponent: " + oppName));
		
		opponentGuiBoard = new BoardPanel(10, 20, COLORS);
		opponentGuiBoard.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				clickBoard(e.getX(), e.getY());				
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
			
		});
		opponentArea.add(opponentGuiBoard);
		
		bottomHalf.add(opponentArea, BorderLayout.CENTER);
		
		statusBar = new JLabel(" ");
		statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		bottomHalf.add(statusBar, BorderLayout.SOUTH);
		
		contentPane.add(bottomHalf, BorderLayout.SOUTH);
	}

	/**
	 * Processes a click at an x,y location of the opponent guiBoard.
	 * @param x the x pixel of the click, relative to the upper left of the opponent gui board
	 * @param y the y pixel of the click, relative to the upper left of the opponent gui board
	 */
	protected void clickBoard(int x, int y) {
		if(gameInProgress) {
			if(playerTurn) {
				BoardSquare square = opponentGuiBoard.squareAt(x, y);
				if(square != null) {
					serverHandler.fireShot(square.ROW, square.COL);
				}				
			}
		}
		
	}

	/**
	 * Builds the Menu Bar at the top of the Client Window
	 * 
	 * @return A JMenuBar representing the menu for the game
	 */
	private JMenuBar buildMenu() {
		JMenuBar mbar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenu networkMenu = new JMenu("Network");
		gameMenu.setMnemonic(KeyEvent.VK_G);
		networkMenu.setMnemonic(KeyEvent.VK_N);
		
		//build the Game Menu
		JMenuItem menuItem = new JMenuItem("Play New Game", new ImageIcon(BattleshipClient.class.getResource("/play.png")));
		menuItem.setMnemonic(KeyEvent.VK_P);	
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, java.awt.event.InputEvent.ALT_DOWN_MASK));
		menuItem.addActionListener(e -> playNewGame());
		menuItem.setEnabled(true);
		gameMenu.add(menuItem);
		
		menuItem = new JMenuItem("Resign Current Game", new ImageIcon(BattleshipClient.class.getResource("/quit.png")));
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, java.awt.event.InputEvent.ALT_DOWN_MASK));
		menuItem.addActionListener(e -> resignGame());
		menuItem.setEnabled(false);
		gameMenu.add(menuItem);
		
		gameMenu.add(new JSeparator());
		
		menuItem = new JMenuItem("Quit Battleship", KeyEvent.VK_Q);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
		menuItem.addActionListener(e -> {
			resignGame();
			System.exit(0);
		});
		gameMenu.add(menuItem);
		
		// build the Network Menu
		menuItem = new JMenuItem("Connect to Server", new ImageIcon(BattleshipClient.class.getResource("/connect_server.png")));
		menuItem.setMnemonic(KeyEvent.VK_C);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, java.awt.event.InputEvent.ALT_DOWN_MASK));
		menuItem.addActionListener(e -> connectServer());
		networkMenu.add(menuItem);
		
		menuItem = new JMenuItem("Launch Server", new ImageIcon(BattleshipClient.class.getResource("/launch_server.png")));
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, java.awt.event.InputEvent.ALT_DOWN_MASK));
		menuItem.addActionListener(e -> launchServer());
		networkMenu.add(menuItem);
		
		//build the menu bar
		mbar.add(gameMenu);
		mbar.add(networkMenu);
		
		return mbar;
	}

	/** launches the external server application */
	private void launchServer() {
		
		EventQueue.invokeLater(() -> {
		      if (server == null) {
		          server = new edu.utep.cs.cs3331.hw5.server.Main();
			  server.setLocationRelativeTo(this);
			  server.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			  server.hideOnClose();
		      }
			  server.setVisible(true);;
		    });
	}

	/** opens a dialog which prompts for server info, and connects */
	private void connectServer() {
		
		ConnectServerDialog csDialog = new ConnectServerDialog(this, this, "Input the Server and Port");
		serverHandler = csDialog.showDialog();
		if(serverHandler != null) {
			connected = true;		
			statusBar.setText("Connected to Server.");
			getPlayerName();			
		}
	}

	/** gets a name for the player and checks with the server to see if it's available */
	private void getPlayerName() {
		String name = null;
		nameAccepted = false;
		while(name == null || name.trim().isEmpty())
			name = JOptionPane.showInputDialog(this, "What is your name?", "Username", JOptionPane.QUESTION_MESSAGE).trim();
			
		serverHandler.sendNameMsg(name);
			
		while(!nameAcknowledged) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		};		
		
		if(nameAccepted) {
			playerName = name;
			playerArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Player: " + playerName));
			playerArea.repaint();
		}
		else {
			serverHandler.tryDisconnect();
		}
	}

	
	/** what happens when the stop button is pressed - game is resigned and reset */
	private void resignGame() {
		reset();
		return;
	}

	/** 
	 * what happens when the play button is pressed - dialogs determine if user wants to play vs. strategy or a player, and initializes
	 * the game.
	 */
	private void playNewGame() {
		if(!gameInProgress) {
			if(connected) {
				String [] options = {"Player", "Strategy", "Cancel"};
				int res = JOptionPane.showOptionDialog(this, "Who do you want to play against?", "Choose Opponent", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
				
				//player was picked
				if(res == JOptionPane.YES_OPTION) {
					
					oppName = (String)JOptionPane.showInputDialog(this, "Choose your Opponent:", "Select Opponent", JOptionPane.PLAIN_MESSAGE, null, availableUsers.toArray(), null); 
					
					if(oppName != null) {
						serverHandler.sendUserPlayMsg(oppName);
						gameInProgress = true;
						this.getJMenuBar().getMenu(0).getItem(0).setEnabled(false);
						playButton.setEnabled(false);
						this.getJMenuBar().getMenu(0).getItem(1).setEnabled(true);
						stopButton.setEnabled(true);
					}
				}
				//strategy was picked
				else if(res == JOptionPane.NO_OPTION) {
				
					oppName = (String)JOptionPane.showInputDialog(this, "Choose the Strategy:", "Select Strategy", JOptionPane.PLAIN_MESSAGE, null, availableStrats.toArray(), null); 
					
					if(oppName != null) {
						serverHandler.sendStrategyPlayMsg(oppName);
						gameInProgress = true;
						this.getJMenuBar().getMenu(0).getItem(0).setEnabled(false);
						playButton.setEnabled(false);
						this.getJMenuBar().getMenu(0).getItem(1).setEnabled(true);
						stopButton.setEnabled(true);
					}
				}
				//cancelled or closed				
			}
		}
		return;
	}
	
	/** starts a new game, with a new logical board for the player and randomly placed ships. */
	private void initializeGame() {
		logicalBoard = new BattleshipBoard(10, 5);
		logicalBoard.addBattleshipListener(statusPanel);
		logicalBoard.addBattleshipListener(playerGuiBoard);
		placeShips();
		logicalBoard.lockBoard();
		
		serverHandler.sendShipsMsg(logicalBoard.getFleet());
	}
	
	/** randomly places the ships */
	private void placeShips() {
		Ship[] fleet = new Ship[5];

		// creates a new ship named "Aircraft carrier", which is 5 units long
		// and horizontal, at (0,0)
		fleet[0] = new Ship("Aircraft carrier", 5, 0, 0, false);
		fleet[1] = new Ship("Battleship", 4, 0, 0, false);
		fleet[2] = new Ship("Frigate", 3, 0, 0, false);
		fleet[3] = new Ship("Submarine", 3, 0, 0, false);
		fleet[4] = new Ship("Minesweeper", 2, 0, 0, false);

		// randomly add the ships to the board and keep trying until a valid
		// placement is found
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

	/** resets the gui components and resigns if a game is in progress */
	private void reset() {
		if(gameInProgress)
			serverHandler.sendGGMsg();
		
		gameInProgress = false;
		this.getJMenuBar().getMenu(0).getItem(0).setEnabled(true);
		playButton.setEnabled(true);
		this.getJMenuBar().getMenu(0).getItem(1).setEnabled(false);
		stopButton.setEnabled(false);
		
		statusPanel.reset();
		opponentGuiBoard.reset();
		playerGuiBoard.reset();
		oppName = "Opponent";
		opponentArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Opponent: " + oppName));
		getContentPane().repaint();		
	}
	
	/** main entry point for the application */
	public static void main(String [] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				BattleshipClient client = new BattleshipClient();
				client.setVisible(true);				
			}			
		});
	}


	/////////////////////////////////////////////////////////////////////////////////////
	// The following methods are inherited from the BattleshipServerListener Interface //
	/////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void badConnectionMsg() {
		JOptionPane.showMessageDialog(this, "Bad Connection", "Bad Connection", JOptionPane.ERROR_MESSAGE);
		statusBar.setText("Disconnected from Server.");
		gameInProgress = false;
		playerName = "Player";
		playerArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Player: " + playerName));
		reset();
		connected = false;
	}

	@Override
	public void badDisconnectMsg() {
		JOptionPane.showMessageDialog(this, "Bad Disconnect", "Bad Disconnect", JOptionPane.ERROR_MESSAGE);
		statusBar.setText("Disconnected from Server.");
		gameInProgress = false;
		playerName = "Player";
		playerArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Player: " + playerName));
		reset();
		connected = false;
	}

	@Override
	public void invalidMessageReceived() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acknowledgeUserMsg(boolean accepted, String reason) {
		nameAcknowledged = true;
		nameAccepted = accepted;
		if(!accepted)
			JOptionPane.showMessageDialog(this, "Reason: " + ((reason != null) ? reason : ""), "Name Rejected", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void sizeMsg(int size) {
		if(size != 10)
			serverHandler.tryDisconnect();		
	}

	@Override
	public void updateStrategies(List<String> strategies) {
		availableStrats = strategies;		
	}

	@Override
	public void updateUsers(List<String> users, List<Boolean> userAvailable) {
		availableUsers = new LinkedList<>();
		int i = 0;
		for(String user: users) {
			if(userAvailable.get(i++).booleanValue()) 
				availableUsers.add(user);
		}		
	}

	@Override
	public void acknowledgeStrategyPlayMsg(boolean accepted, String reason) {
		if(accepted){
			JOptionPane.showMessageDialog(this, oppName + " strategy confirmed. Starting game.");
			opponentArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Opponent: " + oppName));
			opponentArea.repaint();
			initializeGame();
		}
		else {
			JOptionPane.showMessageDialog(this, oppName + " strategy failed to start.");
			reset();
		}
	}

	
	@Override
	public void acknowledgeUserPlayMsg(boolean accepted, String reason) {
		if(accepted) {
			JOptionPane.showMessageDialog(this, oppName + " accepted your request. Starting game.");
			opponentArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Opponent: " + oppName));
			opponentArea.repaint();
			initializeGame();
		}
		else {
			JOptionPane.showMessageDialog(this, oppName + " rejected your request.");
			reset();
		}
	}

	@Override
	public void acknowledgeShipsMsg(boolean accepted, String reason) {
		if(!accepted) {
			JOptionPane.showMessageDialog(this, "Ship placement rejected.");
			serverHandler.tryDisconnect();
			reset();
		}	
		repaint();
	}

	@Override
	public void turnMsg(boolean playerTurn) {
		statusBar.setText(playerTurn ? (playerName + "'s turn.") : (oppName + "'s turn."));
		this.playerTurn = playerTurn;
		repaint();
	}

	@Override
	public void acknowledgeShotMsg(int col, int row, boolean shipHit, boolean shipSunk, boolean gameOver) {
		opponentGuiBoard.shotFired(new BattleshipEvent(shipHit ? BattleshipEvent.HIT : BattleshipEvent.MISS, row - 1, col - 1));
		
		if(shipSunk) {
			new Thread(sunkPlayer).start();
		} else if (shipHit) {
			new Thread(hitPlayer).start();
		}
		
		if(!shipHit) {
			turnMsg(false);
		}
		repaint();
		
		if(gameOver) {
			JOptionPane.showMessageDialog(this, playerName + " won the game!");
			reset();
		}		
	}

	@Override
	public void shotMsg(int col, int row, boolean shipHit, boolean shipSunk, boolean gameOver) {
		
		logicalBoard.fireShot(row - 1, col - 1);
		
		if(shipSunk) {
			new Thread(sunkPlayer).start();
		} else if (shipHit) {
			new Thread(hitPlayer).start();
		}
		
		if(!shipHit) {
			turnMsg(true);
		}
		repaint();
		
		if(gameOver) {
			JOptionPane.showMessageDialog(this, oppName + " won the game!");
			reset();
		}
	}

	@Override
	public void ggMsg() {
		JOptionPane.showMessageDialog(this, playerName + " won the game!");
		reset();
		repaint();
	}

	@Override
	public void acknowledgeGGMsg() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void byeMsg() {
		JOptionPane.showMessageDialog(this, playerName + " won the game!");
		reset();	
		repaint();
	}

	@Override
	public void disconnectedMsg() {
		statusBar.setText("Disconnected from Server.");
		gameInProgress = false;
		playerName = "Player";
		playerArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Player: " + playerName));
		reset();
		connected = false;
		repaint();
	}

	@Override
	public void userPlayRequestMsg(String string) {
		if(gameInProgress)
			serverHandler.sendUserPlayResponseMsg(false);
		else {
			int res = JOptionPane.showConfirmDialog(this, string + " requests to play a game of Battleship!", "Play Request", JOptionPane.YES_NO_OPTION);
			if(res == JOptionPane.YES_OPTION) {
				serverHandler.sendUserPlayResponseMsg(true);
				oppName = string;
				opponentArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Opponent: " + oppName));
				opponentArea.repaint();
				gameInProgress = true;
				this.getJMenuBar().getMenu(0).getItem(0).setEnabled(false);
				playButton.setEnabled(false);
				this.getJMenuBar().getMenu(0).getItem(1).setEnabled(true);
				stopButton.setEnabled(true);
				initializeGame();
			}
			else {
				serverHandler.sendUserPlayResponseMsg(false);
			}
		}
	}
		
}
