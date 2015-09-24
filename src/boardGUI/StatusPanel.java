package boardGUI;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import boardAPI.BattleshipBoard;
import boardAPI.battleshipInterface.BattleshipEvent;
import boardAPI.battleshipInterface.BattleshipListener;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel implements BattleshipListener{
	
	BattleshipBoard logicalBoard;
	JLabel [] shipStatus;
	final JLabel whitespace = new JLabel("          ");
	JLabel shotsFired;
	String [] names;
	boolean [] isSunk;
	int shots;
	
	public StatusPanel(String [] n) {
		super();
		names = n;
		shipStatus = new JLabel[names.length];
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		isSunk = new boolean[names.length];
		for(int i = 0; i < isSunk.length; i++) {
			isSunk[i] = false;
		}
		
		shots = 0;
		
		for(int i = 0; i < shipStatus.length; i++) {
			add(shipStatus[i] = new JLabel(names[i] + ": " + (isSunk[i] ? "SUNK" : "NOT SUNK")));
		}
		
		shotsFired = new JLabel("Shots Fired: " + shots);
		add(whitespace);
		add(shotsFired);
		this.setPreferredSize(new Dimension(200,200));
	}

	public void reset() {
		
		for(int i = 0; i < isSunk.length; i++) {
			isSunk[i] = false;
		}
		
		shots = 0;
		
		for(int i = 0; i < shipStatus.length; i++) {
			shipStatus[i].setText(names[i] + ": " + (isSunk[i] ? "SUNK" : "NOT SUNK"));
		}
		
		shotsFired.setText("Shots Fired: " + shots);
		
		repaint();		
	}

	@Override
	public void shotFired(BattleshipEvent e) {
		if(!((e.getEvent() & BattleshipEvent.INVALID) == BattleshipEvent.INVALID)) {
			shots++;
			shotsFired.setText("Shots Fired: " + shots);
		}
		
		if(((e.getEvent() & BattleshipEvent.SHIP_SUNK) == BattleshipEvent.SHIP_SUNK)) {
			String n = e.getShip().getName();
			for(int i = 0; i < names.length; i++) {
				if (n.equals(names[i])) {
					isSunk[i] = true;
					shipStatus[i].setText(names[i] + ": " + (isSunk[i] ? "SUNK" : "NOT SUNK"));
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

}
