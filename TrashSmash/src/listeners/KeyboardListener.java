package listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Main;

/**
 * key listening class for Trash Smash. Reads key input,sends info to Update thread.
 * @author- Tristan Monger
 */

public class KeyboardListener implements KeyListener {
	public static boolean left = false, right = false;
	public static boolean up = false, down = false;
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			right = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			left = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_UP){
			up = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			down = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			up = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			down = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
}
