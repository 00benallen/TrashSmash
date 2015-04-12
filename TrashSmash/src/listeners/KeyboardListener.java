package listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Key listening class for Trash Smash. Reads key input,sends info to Update thread.
 * @author Tristan Monger
 * @version 1.0
 */
public class KeyboardListener implements KeyListener {
	//Button switches
	public static boolean left = false, right = false;
	public static boolean up = false, down = false, shoot = false;
	public static boolean E = false, Q = false, R = false;
	public static boolean toggle = false;

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
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			shoot = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_E){
			E = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_Q){
			Q = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_C){
			toggle = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_R){
			R = true;
		}
	}

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
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			shoot = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_E){
			E = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_Q){
			Q = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_R){
			R = false;
		}
	}

	public void keyTyped(KeyEvent e) {
		
	}
	
}
