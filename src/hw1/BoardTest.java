package hw1;

import java.util.*;

public class BoardTest {
	
	static void displayBoard(BattleshipBoard dBoard) {
		
		int n = dBoard.getSize();
		
		System.out.printf("     1");
		for(int i = 1; i < n; i++) {
			System.out.printf("%1$2d", i+1);
		}
		System.out.printf("\n    \u250C\u2500");
		for(int i = 1; i < n; i++){
			System.out.printf("\u252C\u2500");
		}
		System.out.printf("\u2510\n");
		for(int i = 0; i < n - 1; i++) {
			System.out.printf("%1$3d \u2502", i+1);
			for(int j = 0; j < n; j++) {
				System.out.printf(dBoard.checkStatus(i, j) + "\u2502");
			}			
			System.out.printf("\n    \u251C\u2500");
			for(int x = 1; x < n; x++) {
				System.out.printf("\u253C\u2500");
			}
			System.out.printf("\u2524\n");
		}
		System.out.printf("%1$3d \u2502", n);
		for(int j = 0; j < n; j++) {
			System.out.printf(dBoard.checkStatus(n - 1, j) + "\u2502");
		}			
		System.out.printf("\n    \u2514\u2500");
		for(int i = 1; i < n; i++) {
			System.out.printf("\u2534\u2500");
		}
		System.out.printf("\u2518\n");
		
		System.out.println();
		System.out.println("Number of Shots: " + dBoard.getNumShots());
		
		System.out.println();
		System.out.printf("%1$17s \u2502 Sunk\n", "Ship Name");
		for(int i = 0; i < 18; i++) {	
			System.out.printf("\u2500");
		}
		System.out.printf("\u253C\u2500\u2500\u2500\u2500\u2500\u2500\n");
				
		String [] names = dBoard.getShipNames();
		boolean [] isSunk = dBoard.getShipSunkStatus();
		
		for(int i = 0; i < names.length; i++) {
			System.out.printf("%1$17s \u2502 %2$b \n", names[i], isSunk[i]);
		}		
	}
	
	
	public static void main(String [] args) {
		
		int n = 0;
		int numShips = 5;
		
		// Get input to build the board 
		
		Scanner cin = new Scanner(System.in);
           
        // checks for valid input (n is an integer >= 5)
        boolean v = false;
        while(!v){
            System.out.print("Enter the board dimension: ");
                                          
            try {    
                n = cin.nextInt();
                if(n >= 5) v = true;
                else System.out.println("Please input an integer value >= 5. ");
            } 
            catch(InputMismatchException e) {
              System.out.println("Caught: InputMismatchException -- Please input an integer value >= 5. ");
              cin.next();
            }
        }
        
       
        // create the board
        BattleshipBoard testBoard = new BattleshipBoard(n, numShips);
        
        // manually place ships?
        boolean manual = false;
        String manInput = "";
        v = false;
        while(!v){
        	System.out.println();
            System.out.print("Would you like to manually place your ships (Y/N)? ");
                                          
            manInput = cin.next();
            if(manInput.equalsIgnoreCase("Y")) {
            	v = true;
            	manual = true;
            }
            else if(manInput.equalsIgnoreCase("N")) {
            	v = true;
            	manual = false;
            }
            else System.out.println("Please input Y or N.");                    
        }
        
        // create the ships
        
        Ship [] fleet = new Ship[5];
        
        fleet[0] = new Ship("Aircraft Carrier", 5, 0, 0, false);
        fleet[1] = new Ship("Battleship", 4, 0, 0, false);
        fleet[2] = new Ship("Frigate", 3, 0, 0, false);
        fleet[3] = new Ship("Submarine", 3, 0, 0, false);
        fleet[4] = new Ship("Minesweeper", 2, 0, 0, false);
        
        //fill the board with ships manually
        if(manual) {
        	for(int i = 0; i < fleet.length; i++) {
        		
        		
        		String vertInput = "";
        		int sRow, sCol;
        		sRow = sCol = 0;
        		boolean sVertical = true;
        		
        		boolean validShip = false;
        		
        		// get each ship's position, and orientation:
        		while(!validShip) {
        		
        			System.out.println();
        			       			
        			v = false;
        			while(!v){
        				System.out.print("Enter the row of the " + fleet[i].getName() + " (size " + fleet[i].getSize() + "): ");
        				
        				try {    
        					sRow = cin.nextInt();
        					if(sRow >= 1) v = true;
        					else System.out.println("Please input an integer value >= 1. ");
        				} 
        				catch(InputMismatchException e) {
        					System.out.println("Caught: InputMismatchException -- Please input an integer value >= 0. ");
        					cin.next();
        				}
        			}
                
	                v = false;
	                while(!v){
	                	System.out.print("Enter the column of the " + fleet[i].getName() + " (size " + fleet[i].getSize() + "): ");
	                                                  
	                    try {    
	                        sCol  = cin.nextInt();
	                        if(sCol >= 1) v = true;
	                        else System.out.println("Please input an integer value >= 1. ");
	                    } 
	                    catch(InputMismatchException e) {
	                      System.out.println("Caught: InputMismatchException -- Please input an integer value >= 0. ");
	                      cin.next();
	                    }
	                }
                
	                v = false;
	                while(!v){
	                    System.out.print("Is the " + fleet[i].getName() + " vertical? (Y/N): ");
	                                                  
	                    vertInput = cin.next();
	                    if(vertInput.equalsIgnoreCase("Y")) {
	                    	v = true;
	                    	sVertical = true;
	                    }
	                    else if(vertInput.equalsIgnoreCase("N")) {
	                    	v = true;
	                    	sVertical = false;
	                    }
	                    else System.out.println("Please input Y or N.");                    
	                }
	            
	                fleet[i].move(sRow - 1, sCol - 1, sVertical);
	                validShip = testBoard.addShip(fleet[i]);
	                if(!validShip) System.out.println("Not a valid placement. Please try again.");
        		}
        	}
        }
        else {
        	System.out.println();
        	System.out.println("Ships will be placed randomly.");
        	Random rng = new Random();
        	for(int i = 0; i < fleet.length; i++) {
        		
        		boolean validShip = false;
        		
        		while(!validShip) {
        			        		
        			fleet[i].move(rng.nextInt(n), rng.nextInt(n), rng.nextBoolean());
        			validShip = testBoard.addShip(fleet[i]);
        		}
        		System.out.println(fleet[i].getName() + " placed successfully.");
        	}    	
        }
        System.out.println();
        System.out.println("Loading Ships successful.");
        
        
        // lock the board and allow shots to be fired until all ships are sunk
        if(!testBoard.lockBoard()){
        	System.out.println();
        	System.out.println("An error occured. Board could not lock. Program terminating.");
        }
        else {
        	System.out.println();
        	System.out.println("Board is locked -- Commence Firing!");
        	
        	int r, c;
        	r = c = 0;
        	boolean [] isSunk = testBoard.getShipSunkStatus();
        	
        	while(!testBoard.gameLost()) {
        		System.out.println();
        		displayBoard(testBoard);
        		System.out.println();
        		
        		System.out.print("Enter the row and column of the grid you want to shoot at: ");
        		v = false;
                while(!v){
                	//System.out.print("Enter a row to shoot at: ");
                                                  
                    try {    
                        r  = cin.nextInt();
                        if(r >= 1) v = true;
                        else System.out.print("Please input an integer value >= 1: ");
                    } 
                    catch(InputMismatchException e) {
                      System.out.print("Caught: InputMismatchException -- Please input an integer value >= 1. ");
                      cin.next();
                    }
                }
                v = false;
                while(!v){
                	//System.out.print("Enter a column to shoot at: ");
                                                  
                    try {    
                        c  = cin.nextInt();
                        if(c >= 1) v = true;
                        else System.out.println("Please input an integer value >= 1: ");
                    } 
                    catch(InputMismatchException e) {
                      System.out.println("Caught: InputMismatchException -- Please input an integer value >= 1: ");
                      cin.next();
                    }
                }
                
                r--;
                c--;
                
                System.out.println();
                if(testBoard.fireShot(r, c)) {
                	System.out.print("Valid shot! It's a ");
                	if(testBoard.checkStatus(r, c) == 'H') {
                		System.out.println("HIT!");
                		boolean [] newIsSunk = testBoard.getShipSunkStatus();
                		for(int i = 0; i < isSunk.length; i++) {
                			if(isSunk[i] != newIsSunk[i]){
                				System.out.println("You sunk the " + testBoard.getShipNames()[i] + "!!!");
                			}
                		}
                		isSunk = newIsSunk;
                	}
                	else {
                		System.out.println("MISS.");
                	}
                }
                else System.out.println("Invalid shot: You have already shot there or it is out of bounds. Try again!");
        	}
        	
        	System.out.println();
        	System.out.println("All ships sunk! Total shots fired: " + testBoard.getNumShots() + ".");
        	System.out.println("Game terminated.");
        }
        cin.close();
	}
}
