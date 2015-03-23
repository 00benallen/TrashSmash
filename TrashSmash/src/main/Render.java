package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import res.Ship;

/**
 * Render class for Trash Smash, runs on render thread, contains draw methods
 * @author Ben Pinhorn
 *
 */
public class Render implements Runnable {
	private Graphics2D g;
	private Ship ship = new Ship(0, 0);
	private Update update;
	
	public Render(Graphics2D g) {
		this.g = g;
		update = Main.update;
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
			update();
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
	
	private void update() {
		//add objects that need to be updated per cycle
		if(update.shipUpdated) {
			updateShip();
		}
	}
	
	private void updateShip() {
		for(int i = 0; i < update.drawQueue.size(); i++) {
			if(update.drawQueue.peek() instanceof Ship) {
				this.ship = ship.clone();
			}
			else {
				try {
					update.drawQueue.add(update.drawQueue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void draw() {
		//add methods for drawing screen
		drawBackground();
		drawHealth();
	}
	
	private void drawBackground() {
		Rectangle2D background = new Rectangle2D.Double(0, 0, GraphicsMain.WIDTH, GraphicsMain.HEIGHT);
		g.setColor(Color.cyan);
		g.fill(background);
	}
	
	private void drawHealth() {
		Rectangle2D healthBar1 = new Rectangle2D.Double(GraphicsMain.WIDTH - 75, 0, 25, 25);
		Rectangle2D healthBar2 = new Rectangle2D.Double(GraphicsMain.WIDTH - 50, 0, 25, 25);
		Rectangle2D healthBar3 = new Rectangle2D.Double(GraphicsMain.WIDTH - 25, 0, 25, 25);
		g.setColor(Color.red);
		g.fill(healthBar1);
		g.fill(healthBar2);
		g.fill(healthBar3);
	}
}
