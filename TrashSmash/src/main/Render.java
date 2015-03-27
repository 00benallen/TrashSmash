package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.imageio.ImageIO;

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
	private BufferedImage[] rankIcons;
	private BufferedImage hpBar;
	private Queue<BufferedImage> dblBuffer = new LinkedList<BufferedImage>();
	
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
			hpBar = ImageIO.read(new File("Assets/Menu and UI/smallerhpInfoBar.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void draw() {
		//add methods for drawing screen
		BufferedImage screen = new BufferedImage(GraphicsMain.WIDTH, GraphicsMain.HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) screen.getGraphics();
		drawBackground(g);
		drawShip(g);
		drawEnemies(g);
		drawHealth(g);
		drawGunSet(g);
		dblBuffer.add(screen);
		if(dblBuffer.size() == 2) {
			this.g.drawImage(dblBuffer.poll(), 0, 0, GraphicsMain.WIDTH, GraphicsMain.HEIGHT, null);
		}
	}
	
	private void drawBackground(Graphics2D g) {
		Rectangle2D background = new Rectangle2D.Double(0, 0, GraphicsMain.WIDTH, GraphicsMain.HEIGHT);
		g.setColor(Color.black);
		g.fill(background);
	}
	
	private void drawShip(Graphics2D g) {
		lck.readLock().lock();
		Ship ship = Main.update.ship;
		lck.readLock().unlock();
		g.drawImage(ship.getImage(), ship.getX(), ship.getY(), ship.getWidth()/2, ship.getHeight()/2, null);
	}
	
	private void drawEnemies(Graphics2D g) {
		lck.readLock().lock();
		LinkedList<Enemy> enemies = Main.update.enemies;
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			g.drawImage(e.getImage(), e.getX(), e.getY(), e.getWidth()/2, e.getHeight()/2, null);
		}
		lck.readLock().unlock();	
	}
	
	
	private void drawHealth(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		Ellipse2D.Double healthBar1 = new Ellipse2D.Double(GraphicsMain.WIDTH -135, 33, 20, 20);
		Ellipse2D.Double healthBar2 = new Ellipse2D.Double(GraphicsMain.WIDTH -110, 33, 20, 20);
		Ellipse2D.Double healthBar3 = new Ellipse2D.Double(GraphicsMain.WIDTH -85, 33, 20, 20);
		lck.readLock().lock();
		//Draws the HP Bar image
		g.drawImage(hpBar, GraphicsMain.WIDTH - 197, 25, 197, 99, null);
		//Fills in healthbar info as necessary
		if(Main.update.ship.getHealth() >= 1) {
			g.setColor(Color.red);
			g.fill(healthBar1);
			if(Main.update.ship.getHealth() >= 2) {
				g.setColor(Color.orange);
				g.fill(healthBar2);
				if(Main.update.ship.getHealth() == 3) {
					g.setColor(Color.green);
					g.fill(healthBar3);
				}
			}
		}
		lck.readLock().unlock();
	}

	
	private void drawGunSet(Graphics2D g) {
		lck.readLock().lock();
		g.drawImage(gunSetIcons[Main.update.ship.getGunSet()], 16, GraphicsMain.HEIGHT - 80, 64, 64, null);
		lck.readLock().unlock();
	}
}
