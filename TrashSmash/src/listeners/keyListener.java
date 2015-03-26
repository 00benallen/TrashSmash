package listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Main;

/**
 * key listening class for Trash Smash. Reads key input,sends info to Update thread.
 * @author- Tristan Monger
 */

public class keyListener implements KeyListener{
	
	public keyListener(){
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			Main.update.setShipDirection(1);
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			Main.update.setShipDirection(2);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Main.update.setShipDirection(0);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
}
