package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.Main;

/**
 * Button listener for main menu, performs actions when buttons are pressed, not part of update or render threads
 * @author Ben Pinhorn
 *
 */
public class ButtonListener implements ActionListener {
	private Main main;
	
	public ButtonListener(Main main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//if more JSwing buttons are needed, add their methods here
		if(e.getActionCommand().equals("start")) {
			main.gameStart();
		}
	}
}