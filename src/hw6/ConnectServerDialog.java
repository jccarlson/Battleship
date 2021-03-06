package hw6;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * a package visibility class which calls a customized dialog to get server info from the user
 * @author Jason Carlson
 * @version 1.0
 * @since 2015-12-01
 */
@SuppressWarnings("serial")
class ConnectServerDialog extends JDialog{
	
	/** the server handler to return */
	BattleshipServerHandler sh = null;
	
	/** the server listener to attach to the handler */
	BattleshipServerListener l;
	
	/** text input fields */
	JTextField serverInput;
	JTextField portInput;
	
	/** buttons */
	JButton connectButton;
	JButton cancelButton;
	
	/** true if the window is still visible */
	boolean prompting;
	
	
	/**
	 * Constructs a dialog which asks for server and port info
	 * @param l a listener which will be attached to the handler
	 * @param owner the frame which owns this dialog
	 * @param title the title of the dialog
	 */
	ConnectServerDialog(BattleshipServerListener l, Frame owner, String title) {
		super(owner, title, true);
		this.l = l;
		this.setLocationRelativeTo(owner);
		getContentPane().setLayout(new BorderLayout(10,10));
		
		// create the input area
		JPanel inputBox = new JPanel();
		inputBox.setLayout(new FlowLayout(FlowLayout.CENTER));
		inputBox.setPreferredSize(new Dimension(330,30));
		inputBox.add(new JLabel("Server:"));
		serverInput = new JTextField("localhost", 15);
		serverInput.setEditable(true);
		inputBox.add(serverInput);
		inputBox.add(new JLabel("Port:"));
		portInput = new JTextField("8000", 4);
		serverInput.setEditable(true);
		inputBox.add(portInput);
		getContentPane().add(inputBox, BorderLayout.CENTER);
		
		//add buttons
		JPanel buttonBox = new JPanel();
		buttonBox.setLayout(new FlowLayout(FlowLayout.CENTER));
		connectButton = new JButton("Connect");
		cancelButton = new JButton("Cancel");
		
		connectButton.addActionListener(e -> connect());
		cancelButton.addActionListener(e -> cancel());
		
		buttonBox.add(connectButton);
		buttonBox.add(cancelButton);
		getContentPane().add(buttonBox, BorderLayout.SOUTH);
		
		//set dialog behavior
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.addWindowListener(new WindowListener(){

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				prompting = false;
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		this.pack();
		prompting = false;
	}

	/** when the cancel button is clicked */
	private void cancel() {
		sh = null;
		prompting = false;
		this.setVisible(false);
	}

	/** when the connect button is clicked */
	private void connect() {
		try {
			sh = new BattleshipServerHandler(l, serverInput.getText().trim(), Integer.parseInt(portInput.getText().trim()));
			if(sh.isConnected()) {
				prompting = false;
				this.setVisible(false);
			}
			else {
				JOptionPane.showMessageDialog(this, "Invalid Server/Port", "Invalid Server/Port", JOptionPane.ERROR_MESSAGE);
				sh = null;
			}
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Invalid Server/Port", "Invalid Server/Port", JOptionPane.ERROR_MESSAGE);
			sh = null;
		}
		
	}
	
	/** shows the dialog and waits til it's closed to return the server handler it connected to (or null if cancelled) */
	BattleshipServerHandler showDialog() {
		setVisible(true);
		while(prompting){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		};
		return sh;
	}

}
