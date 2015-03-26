package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.imageio.ImageIO;

import res.Drawable;
import res.Enemy;
import res.Ship;

/**
 * Render class for Trash Smash, runs on render thread, contains draw methods
 * @author Ben Pinhorn
 */
public class Render implements Runnable {
	private Graphics2D g;
	private ReentrantReadWriteLock lck = Main.lck;
	private BufferedImage[] gunSetIcons;
	
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
		gunSetIcons = new BufferedImage[3];
		try {
			gunSetIcons[0] = ImageIO.read(new File("Assets/Other/GarbageIcon.png"));
			gunSetIcons[1] = ImageIO.read(new File("Assets/Other/RecycleIcon.png"));
			gunSetIcons[1] = ImageIO.read(new File("Assets/Other/RecycleIcon.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void draw() {
		//add methods for drawing screen
		drawBackground();
		drawShip();
		drawEnemies();
		drawHealth();
		drawGunSet();
		
	}
	
	private void drawBackground() {
		Rectangle2D background = new Rectangle2D.Double(0, 0, GraphicsMain.WIDTH, GraphicsMain.HEIGHT);
		g.setColor(Color.black);
		g.fill(background);
	}
	
	private void drawShip() {
		lck.readLock().lock();
		Ship ship = Main.update.ship;
		lck.readLock().unlock();
		g.drawImage(ship.getImage(), ship.getX(), ship.getY(), null);
	}
	
	private void drawEnemies() {
		lck.readLock().lock();
		LinkedList<Enemy> enemies = Main.update.enemies;
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			g.drawImage(e.getImage(), e.getX(), e.getY(), null);
		}
		lck.readLock().unlock();	
	}
	
	
	private void drawHealth() {
		Rectangle2D healthBar1 = new Rectangle2D.Double(GraphicsMain.WIDTH - 170, 40, 50, 50);
		Rectangle2D healthBar2 = new Rectangle2D.Double(GraphicsMain.WIDTH - 119, 40, 50, 50);
		Rectangle2D healthBar3 = new Rectangle2D.Double(GraphicsMain.WIDTH - 68, 40, 50, 50);
		g.setColor(Color.red);
		lck.readLock().lock();
		if(Main.update.ship.getHealth() >= 1) {
			g.fill(healthBar1);
			if(Main.update.ship.getHealth() >= 2) {
				g.fill(healthBar2);
				if(Main.update.ship.getHealth() == 3) {
					g.fill(healthBar3);
				}
			}
		}
		lck.readLock().unlock();
	}
	
	private void drawGunSet() {
		lck.readLock().lock();
		g.drawImage(gunSetIcons[Main.update.ship.getGunSet()], 16, GraphicsMain.HEIGHT - 80, 64, 64, null);
		lck.readLock().unlock();
	}
}
