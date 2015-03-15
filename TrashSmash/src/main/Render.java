package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Render class for Trash Smash, runs on render thread, contains draw methods
 * @author Ben Pinhorn
 *
 */
public class Render implements Runnable {
	Graphics2D g;
	
	public Render(Graphics2D g) {
		this.g = g;
	}
	
	/**
	 * Resumes the draw thread when the game is ready for another frame
	 */
	public synchronized void resume() {
		notify();
	}

	/**
	 * Run method for render thread, triggers the draw list, waits until notified by update thread
	 */
	public void run() {
		while(Main.running) {
			draw();
			synchronized(this) 
			{
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(Main.running = false) {
			return;
		}
	}
	
	private void draw() {
		//add methods for drawing screen
		drawBackground();
	}
	
	private void drawBackground() {
		Rectangle2D background = new Rectangle2D.Double(0, 0, GraphicsMain.WIDTH, GraphicsMain.HEIGHT);
		g.setColor(Color.cyan);
		g.fill(background);
		
	}
	

}
