package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import res.Ship;

/**
 * Render class for Trash Smash, runs on render thread, contains draw methods
 * @author Ben Pinhorn
 */
public class Render implements Runnable {
	private Graphics2D g;
	private ReentrantReadWriteLock lck = Main.lck;
	
	public Render(Graphics2D g) {
		this.g = g;
	}

	/**
	 * Run method for render thread, triggers the draw list, waits until notified by update thread
	 */
	public void run() {
		init();
		long lastTime = System.nanoTime();
		double nanoPerUpdate = 1000000000D/60D;
		double delta = 0D;
		
		if(Main.appState == Main.GAME_STATE) {
			while(Update.running) {
				
				long now = System.nanoTime();
				delta += (now - lastTime) / nanoPerUpdate;
				lastTime = now;
				
				while(delta >= 1) {
					draw();
					delta--;
				}
			}
		}
		
		if(Update.running = false) {
			return;
		}
	}
	
	private void init() {
		
	}
	
	private void drawBackground() {
		Rectangle2D background = new Rectangle2D.Double(0, 0, GraphicsMain.WIDTH, GraphicsMain.HEIGHT);
		g.setColor(Color.cyan);
		g.fill(background);
	}
	
	private void draw() {
		//add methods for drawing screen
		drawBackground();
		drawHealth();
		
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
