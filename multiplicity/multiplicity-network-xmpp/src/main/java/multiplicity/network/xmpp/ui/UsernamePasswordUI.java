package multiplicity.network.xmpp.ui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import multiplicity.config.network.xmpp.XMPPConfigPrefsItem;

public class UsernamePasswordUI {
	
	private static boolean userPassOk = false;
	
	public static UsernamePassword promptForUsernameAndPassword() {
		XMPPConfigPrefsItem xmppPrefs = new XMPPConfigPrefsItem();
		String user = xmppPrefs.getXMPPUser();
		String pass = xmppPrefs.getXMPPPassword();
		final JDialog dialog = new JDialog(new JFrame(), true);
	    Container pane = dialog.getContentPane();
	    pane.setLayout(new GridLayout(4, 1));
	    pane.add(new JLabel("Please enter username and password for XMPP"));
	    JTextField usernameField = new JTextField(20);
	    JPanel p2 = new JPanel();
	    p2.add(new JLabel("Username:"));
	    p2.add(usernameField);			   
	    pane.add(p2);
	    JPanel p3 = new JPanel();
	    p3.add(new JLabel("Password:"));
	    JPasswordField passwordField = new JPasswordField(20);
	    p3.add(passwordField);
	    pane.add(p3);
	    JPanel p4 = new JPanel();
	    JButton okButton = new JButton("OK");
	    JButton cancelButton = new JButton("Cancel");
	    p4.add(okButton);
	    p4.add(cancelButton);
	    pane.add(p4);

	    okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				userPassOk = true;
			}			    	
	    });
	    cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				userPassOk = false;
			}			    	
	    });
	    
	    dialog.pack();
	    dialog.setVisible(true);
	    
	    if(userPassOk) {
	    	user = usernameField.getText();
	    	pass = new String(passwordField.getPassword());
	    }
	    return new UsernamePassword(user, pass);
	}
	
	
	
	public static class UsernamePassword {
		private String username;
		private String password;
		public UsernamePassword(String username, String password) {
			this.setUsername(username);
			this.setPassword(password);
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getUsername() {
			return username;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getPassword() {
			return password;
		}
	}
}
