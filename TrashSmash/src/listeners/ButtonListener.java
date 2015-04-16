package listeners;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.Main;

/**
 * Button listener for main menu, performs actions when buttons are pressed, not part of update or render threads
 * @author Ben Pinhorn
 * @version 1.0
 */
public class ButtonListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("start")) {
			Main.gameStart();
		}
		
		if(e.getActionCommand().equals("quit")) {
			Main.exit();
		}
		
		if(e.getActionCommand().equals("scores")) {
			CardLayout layout = (CardLayout) Main.gMain.window.getContentPane().getLayout();
			layout.show(Main.gMain.window.getContentPane(), Main.gMain.SCORES_MENU);
		}
	}
}
