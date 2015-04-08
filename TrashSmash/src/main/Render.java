package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.imageio.ImageIO;

import res.Buff;
import res.Bullet;
import res.Enemy;
import res.Ship;

/**
 * Render class for Trash Smash, runs on render thread, contains draw methods
 * @author Ben Pinhorn
 */
public class Render implements Runnable {
	private Graphics2D g;
	public volatile ReentrantReadWriteLock lck = Main.lck;
	private BufferedImage[] gunSetIcons;
	private BufferedImage hpBar;
	private BufferedImage bronze;
	private BufferedImage silv;
	private BufferedImage gold;
	private BufferedImage diam;
	private BufferedImage mstr;
	private BufferedImage background;
	private BufferedImage[] buffIcons;
	public static BufferedImage[] explosion = new BufferedImage[10];
	public static BufferedImage[] redBulletExplosion = new BufferedImage[6];
	public static BufferedImage[] blueBulletExplosion = new BufferedImage[6];
	public static BufferedImage[] greenBulletExplosion = new BufferedImage[6];
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
			gunSetIcons[2] = ImageIO.read(new File("Assets/Other/CompostIcon.png"));
			hpBar = ImageIO.read(new File("Assets/Menu and UI/smallerhpInfoBar.png"));
			background = ImageIO.read(new File("Assets/Other/backGround Game.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bronze = ImageIO.read(new File("Assets/Menu and UI/bronzeRank.png"));
			silv = ImageIO.read(new File("Assets/Menu and UI/silverRank.png"));
			gold = ImageIO.read(new File("Assets/Menu and UI/goldRank.png"));
			diam = ImageIO.read(new File("Assets/Menu and UI/diamondRank.png"));
			mstr = ImageIO.read(new File("Assets/Menu and UI/masterRank.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffIcons = new BufferedImage[4];
		try {
			buffIcons[0] = ImageIO.read(new File("Assets/BuffIcons/hpBuff.png"));
			buffIcons[1] = ImageIO.read(new File("Assets/BuffIcons/shockwaveBuff.png"));
			buffIcons[2] = ImageIO.read(new File("Assets/BuffIcons/reinforceBuff.png"));
			buffIcons[3] = ImageIO.read(new File("Assets/BuffIcons/speedBuff.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			explosion[0] = ImageIO.read(new File("Assets/explosion GIF/1.png"));
			explosion[1] = ImageIO.read(new File("Assets/explosion GIF/2.png"));
			explosion[2] = ImageIO.read(new File("Assets/explosion GIF/3.png"));
			explosion[3] = ImageIO.read(new File("Assets/explosion GIF/4.png"));
			explosion[4] = ImageIO.read(new File("Assets/explosion GIF/5.png"));
			explosion[5] = ImageIO.read(new File("Assets/explosion GIF/6.png"));
			explosion[6] = ImageIO.read(new File("Assets/explosion GIF/7.png"));
			explosion[7] = ImageIO.read(new File("Assets/explosion GIF/8.png"));
			explosion[8] = ImageIO.read(new File("Assets/explosion GIF/9.png"));
			explosion[9] = ImageIO.read(new File("Assets/explosion GIF/final.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			redBulletExplosion[0] = ImageIO.read(new File("Assets/bulletExplosion/redBulletExplode1.png"));
			redBulletExplosion[1] = ImageIO.read(new File("Assets/bulletExplosion/redBulletExplode2.png"));
			redBulletExplosion[2] = ImageIO.read(new File("Assets/bulletExplosion/redBulletExplode3.png"));
			redBulletExplosion[3] = ImageIO.read(new File("Assets/bulletExplosion/redBulletExplode4.png"));
			redBulletExplosion[4] = ImageIO.read(new File("Assets/bulletExplosion/redBulletExplode5.png"));
			redBulletExplosion[5] = ImageIO.read(new File("Assets/bulletExplosion/redBulletExplode6.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			blueBulletExplosion[0] = ImageIO.read(new File("Assets/bulletExplosion/blueBulletExplode1.png"));
			blueBulletExplosion[1] = ImageIO.read(new File("Assets/bulletExplosion/blueBulletExplode2.png"));
			blueBulletExplosion[2] = ImageIO.read(new File("Assets/bulletExplosion/blueBulletExplode3.png"));
			blueBulletExplosion[3] = ImageIO.read(new File("Assets/bulletExplosion/blueBulletExplode4.png"));
			blueBulletExplosion[4] = ImageIO.read(new File("Assets/bulletExplosion/blueBulletExplode5.png"));
			blueBulletExplosion[5] = ImageIO.read(new File("Assets/bulletExplosion/blueBulletExplode6.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			greenBulletExplosion[0] = ImageIO.read(new File("Assets/bulletExplosion/greenBulletExplode1.png"));
			greenBulletExplosion[1] = ImageIO.read(new File("Assets/bulletExplosion/greenBulletExplode2.png"));
			greenBulletExplosion[2] = ImageIO.read(new File("Assets/bulletExplosion/greenBulletExplode3.png"));
			greenBulletExplosion[3] = ImageIO.read(new File("Assets/bulletExplosion/greenBulletExplode4.png"));
			greenBulletExplosion[4] = ImageIO.read(new File("Assets/bulletExplosion/greenBulletExplode5.png"));
			greenBulletExplosion[5] = ImageIO.read(new File("Assets/bulletExplosion/greenBulletExplode6.png"));
		} catch(IOException e) {
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
		drawGunSet(g);
		drawBullets(g);
		drawBuffs(g);
		drawHealth(g);
		runExplosions();
		dblBuffer.add(screen);
		if(dblBuffer.size() == 2) {
			this.g.drawImage(dblBuffer.poll(), 0, 0, GraphicsMain.WIDTH, GraphicsMain.HEIGHT, null);
		}
	}

	private void drawBackground(Graphics2D g) {
		g.drawImage(background, 0, 0, GraphicsMain.WIDTH, GraphicsMain.HEIGHT, null);
	}
	
	private void drawShip(Graphics2D g) {
		lck.readLock().lock();
		Ship ship = Main.update.ship;
		lck.readLock().unlock();
		g.drawImage(ship.getImage(), ship.getX(), ship.getY(), ship.getWidth()/2, ship.getHeight()/2, null);
	}
	
	private void drawBuffs(Graphics2D g){
		lck.readLock().lock();
		LinkedList<Buff> buffs = Main.update.buffs;
		for(int i = 0; i < buffs.size(); i++) {
			Buff e = buffs.get(i);
			if(buffs.get(i).isDead() == false){
				g.drawImage(e.getImage(), e.getX(), e.getY(), e.getWidth(), e.getHeight(), null);
			}
			else{
				buffs.remove(buffs.get(i));
			}
		}
		lck.readLock().unlock();	
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
		//g.drawImage(bronze, 954, 78, 40, 32, null);
		g.setColor(Color.CYAN);
		g.setFont(new Font("OCR A Extended", Font.BOLD, 16));
		g.drawString("" + Main.update.ship.getScore(), 865, 102);
		if(Main.update.ship.getScore() < 10000)
			g.drawImage(bronze, 954, 78, 40, 32, null);
		else if(Main.update.ship.getScore() < 25000)
			g.drawImage(silv, 954, 74, 40, 35, null);
		else if(Main.update.ship.getScore() < 100000)
			g.drawImage(gold, 954, 74, 40, 35, null);
		else if(Main.update.ship.getScore() < 200000)
			g.drawImage(diam, 954, 74, 40, 35, null);
		else
			g.drawImage(mstr, 954, 72, 40, 40, null);
		
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
	
	private void drawBullets(Graphics2D g) {
		lck.readLock().lock();
		LinkedList<Bullet> bullets = Main.update.bullets;
		for(int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			g.drawImage(b.getImage(), b.getX(), b.getY(), b.getWidth(), b.getHeight(), null);
		}
		lck.readLock().unlock();	
	}
	
	private void runExplosions() {
		lck.readLock().lock();
		LinkedList<Enemy> enemies = Main.update.enemies;
		LinkedList<Bullet> bullets = Main.update.bullets;
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if(e.isExplode() && !e.isDead()) {
				e.explode();
			}
		}
		for(int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if(b.isExplode && !b.isDead) {
				b.explode();
			}
		}
		lck.readLock().unlock();
	}
}
