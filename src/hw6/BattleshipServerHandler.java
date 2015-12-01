package hw6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import boardAPI.Ship;

public class BattleshipServerHandler {
	
	/** Socket for connecting to a server */
	Socket chatSocket;
	PrintWriter serverOut;
	BufferedReader serverIn;
	
	/** true if connected to a server */
	private boolean connected;
	
	/** the client this handler serves */
	BattleshipServerListener client;
	
	/**
	 * constructs a handler with an attached client for server at hostname:port 
	 * @param client client class
	 * @param hostName server name
	 * @param port server port
	 */
	public BattleshipServerHandler(BattleshipServerListener client, String hostName, int port) {
		this.client = client;
		try {
			chatSocket = new Socket(hostName, port);
			serverOut = new PrintWriter(chatSocket.getOutputStream());
			serverIn = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
			connected = true;
		} catch (Exception e) {
			client.badConnectionMsg();
		}
		
		new Thread(() -> {
			try {
				String line;
				line = serverIn.readLine();
				while(line != null) {
					processMessage(line);
					line = serverIn.readLine();
				}
			}catch (Exception e) {
				// Disconnect from the server
				tryDisconnect(); 
			}
		}).start();
	}

	/**
	 * parses a message from the server
	 * @param line the raw message
	 */
	private void processMessage(String line) {
		
		String msg = null;
		List <String> args = new LinkedList<>();
		StringReader in = new StringReader(line);
		Scanner sc = new Scanner(in);
		sc.useDelimiter(":");
		msg = sc.next().trim();
		sc.useDelimiter("[:,]");
		while(sc.hasNext()) {
			args.add(sc.next().trim());
		}
		sc.close();
		System.out.print("Received message: " + msg + " with arguments: ");
		for(String s: args)
			System.out.print(s + " ");
		System.out.println();
		
		// call methods on listener based on msg
		if(msg == null) {
			client.invalidMessageReceived();
			return;
		}
		try {
			if(msg.equals("ack_user")) {
				client.acknowledgeUserMsg(Boolean.parseBoolean(args.get(0)), (args.size() > 1) ? args.get(1) : null);
			}
			if(msg.equals("size")) {
				client.sizeMsg(Integer.parseInt(args.get(0)));
			}
			
			if(msg.equals("strategies")) {
				client.updateStrategies(args);
			}
			
			if(msg.equals("users")) {
				List <Boolean> userAvailable = new LinkedList<>();
				int i = 0;
				for(String user: args) {
					if(user.charAt(0) == '+')
						userAvailable.add(i, new Boolean(true));
					else
						userAvailable.add(i,new Boolean(false));
					args.set(i, user.substring(1));				
				}
				client.updateUsers(args, userAvailable);
			}
			
			if(msg.equals("ack_strategy_play")) {
				client.acknowledgeStrategyPlayMsg(Boolean.parseBoolean(args.get(0)), (args.size() > 1) ? args.get(1) : null);
				return;
			}
			
			if(msg.equals("user_play")) {
				client.userPlayRequestMsg(args.get(0));
			}
			
			if(msg.equals("ack_user_play")) {
				client.acknowledgeUserPlayMsg(Boolean.parseBoolean(args.get(0)), (args.size() > 1) ? args.get(1) : null);
				return;
			}
			
			if(msg.equals("ack_ships")) {
				client.acknowledgeShipsMsg(Boolean.parseBoolean(args.get(0)), (args.size() > 1) ? args.get(1) : null);
				return;
			}
			
			if(msg.equals("turn")) {
				client.turnMsg(Boolean.parseBoolean(args.get(0)));
				return;
			}
			
			if(msg.equals("ack_shot")) {
				client.acknowledgeShotMsg(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)), 
						Boolean.parseBoolean(args.get(2)), Boolean.parseBoolean(args.get(3)), Boolean.parseBoolean(args.get(4)));
				return;
			}
			
			if(msg.equals("shot")) {
				client.shotMsg(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)), 
						Boolean.parseBoolean(args.get(2)), Boolean.parseBoolean(args.get(3)), Boolean.parseBoolean(args.get(4)));
				return;
			}
			
			if(msg.equals("gg")) {
				client.ggMsg();
				return;
			}
			
			if(msg.equals("ack_gg")) {
				client.acknowledgeGGMsg();
				return;
			}
			
			if(msg.equals("bye")) {
				client.byeMsg();
				return;
			}
			
			if(msg.equals("disconnected")) {
				client.disconnectedMsg();
				return;
			}
		}catch(IndexOutOfBoundsException e) {
			System.err.println("Message has wrong number of arguments");
		}
			
		client.invalidMessageReceived();
		return;		
	}

	/**
	 * attempts a clean disconnect from the server
	 */
	public void tryDisconnect() {
		
		if(connected) {
			try {
				serverOut.close();
				serverIn.close();
				chatSocket.close();				
				connected = false;
				client.disconnectedMsg();
				
			} catch (IOException e) {
				client.badDisconnectMsg();
			}
		}
	}

	/**
	 * @return true if connected to a server
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * sends a user: message
	 * @param name the player's name
	 */
	public void sendNameMsg(String name) {
		serverOut.println("user:" + name);	
		serverOut.flush();
	}
	
	/**
	 * sends a shot: message
	 * @param r the row of the shot
	 * @param c the column of the shot
	 */
	public void fireShot(int r, int c) {
		serverOut.println("shot:" + (c+1) + "," + (r+1));
		serverOut.flush();
	}

	/**
	 * resigns from the game
	 */
	public void sendGGMsg() {
		serverOut.println("gg:");	
		serverOut.flush();
		
	}

	/**
	 * requests for an opponent to play
	 * @param opponent the opponent
	 */
	public void sendUserPlayMsg(String opponent) {
		serverOut.println("user_play:" + opponent);	
		serverOut.flush();
	}

	/**
	 * requests to play against a strategy
	 * @param opponent the opposing strategy
	 */
	public void sendStrategyPlayMsg(String opponent) {
		serverOut.println("strategy_play:" + opponent);	
		serverOut.flush();
	}

	/**
	 * sends a ships: message
	 * @param fleet the fleet of ships and how they are aligned
	 * @see boardAPI.Ship
	 */
	public void sendShipsMsg(List <Ship> fleet) {
		String msg = "ships:";
		for(Ship s: fleet) {
			msg += s.getName() + ",";
			msg += s.getSize() + ",";
			msg += (s.getColumn() + 1) + ",";
			msg += (s.getRow() + 1) + ",";
			msg += (!s.isVertical()) + ",";
		}
		
		msg = msg.substring(0, msg.length() - 1);
		
	//	msg = "ships:Aircraft carrier,5,10,1,false,Battleship,4,2,1,true,Frigate,3,2,3,false,Submarine,3,3,9,true,Minesweeper,2,4,10,true";
		
		System.out.println("Sending ship message: " + msg);
		
		serverOut.println(msg);
		serverOut.flush();		
	}

	/**
	 * responds to a user_play: message with an ack_user_play: message
	 * @param b true if agree to play
	 */
	public void sendUserPlayResponseMsg(boolean b) {
		serverOut.println("ack_user_play:" + b);
		serverOut.flush();		
	}

}
