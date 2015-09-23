package boardGUI;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import boardAPI.BattleshipBoard;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel {
	
	BattleshipBoard logicalBoard;
	JLabel [] shipStatus;
	final JLabel whitespace = new JLabel("          ");
	JLabel shotsFired;
	
	public StatusPanel(BattleshipBoard l) {
		super();
		logicalBoard = l;
		shipStatus = new JLabel[logicalBoard.getShipSunkStatus().length];
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		String [] names = logicalBoard.getShipNames();
		boolean [] isSunk = logicalBoard.getShipSunkStatus();
		int shots = logicalBoard.getNumShots();
		
		for(int i = 0; i < shipStatus.length; i++) {
			add(shipStatus[i] = new JLabel(names[i] + ": " + (isSunk[i] ? "SUNK" : "NOT SUNK")));
		}
		shotsFired = new JLabel("Shots Fired: " + shots);
		add(whitespace);
		add(shotsFired);
		this.setPreferredSize(new Dimension(200,200));
	}

	public void refresh() {
		String [] names = logicalBoard.getShipNames();
		boolean [] isSunk = logicalBoard.getShipSunkStatus();
		int shots = logicalBoard.getNumShots();
		
		for(int i = 0; i < shipStatus.length; i++) {
			shipStatus[i].setText(names[i] + ": " + (isSunk[i] ? "SUNK" : "NOT SUNK"));
		}
		
		shotsFired.setText("Shots Fired: " + shots);
		
		repaint();		
	}

	public void resetLogicalBoard(BattleshipBoard logicalBoard2) {
		logicalBoard = logicalBoard2;
		refresh();
	}
}
