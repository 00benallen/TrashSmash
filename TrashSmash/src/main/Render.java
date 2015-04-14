package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

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
	//graphics resources
	private Graphics2D g;
	private BufferedImage[] gunSetIcons;
	private BufferedImage hpBar;
	private BufferedImage bronze, silv, gold, diam, mstr;
	private BufferedImage gHP, oHP, rHP;
	private BufferedImage background, background2, infoScreen, deadScreen;
	private BufferedImage EMP;
	private BufferedImage[] buffIcons;
	public static BufferedImage[] explosion = new BufferedImage[10];
	public static BufferedImage[] redBulletExplosion = new BufferedImage[6];
	public static BufferedImage[] blueBulletExplosion = new BufferedImage[6];
	public static BufferedImage[] greenBulletExplosion = new BufferedImage[6];
	private Queue<BufferedImage> dblBuffer = new LinkedList<BufferedImage>();
	
	//thread resources
	public volatile ReentrantReadWriteLock lck = Main.lck;
	
	/**
	 * Constructs the render object
	 * @param g
	 */
	public Render(Graphics2D g) {
		this.g = g;
	}

	/**
	 * Run method for render thread, triggers the draw list, keeps its own fps
	 */
	@Override
	public void run() {
		init();
		long lastTime = System.nanoTime();
		double nanoPerUpdate = 1000000000D/50D;
		double delta = 0D;
		
		
			while(Update.running) {
				long now = System.nanoTime();
				delta += (now - lastTime) / nanoPerUpdate;
				lastTime = now;
				
				while(delta >= 1) {
					draw();
					delta--;
				}
			}
		
		if(Update.running = false) {
			return;
		}
	}
	
	private void init() { //loads all non object images
		gunSetIcons = new BufferedImage[3];
		try {
			gunSetIcons[0] = ImageIO.read(getClass().getClassLoader().getResource("Other/GarbageIcon.png"));
			gunSetIcons[1] = ImageIO.read(getClass().getClassLoader().getResource("Other/RecycleIcon.png"));
			gunSetIcons[2] = ImageIO.read(getClass().getClassLoader().getResource("Other/CompostIcon.png"));
			hpBar = ImageIO.read(getClass().getClassLoader().getResource("MenuandUI/smallerhpInfoBar.png"));
			background = ImageIO.read(getClass().getClassLoader().getResource("Other/backGround Game.png"));
			background2 = ImageIO.read(getClass().getClassLoader().getResource("Other/background.png"));
			infoScreen = ImageIO.read(getClass().getClassLoader().getResource("InfoScreen/INFOMove.png"));	
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bronze = ImageIO.read(getClass().getClassLoader().getResource("MenuandUI/bronzeRank.png"));
			silv = ImageIO.read(getClass().getClassLoader().getResource("MenuandUI/silverRank.png"));
			gold = ImageIO.read(getClass().getClassLoader().getResource("MenuandUI/goldRank.png"));
			diam = ImageIO.read(getClass().getClassLoader().getResource("MenuandUI/diamondRank.png"));
			mstr = ImageIO.read(getClass().getClassLoader().getResource("MenuandUI/masterRank.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffIcons = new BufferedImage[4];
		try {
			buffIcons[0] = ImageIO.read(getClass().getClassLoader().getResource("BuffIcons/hpBuff.png"));
			buffIcons[1] = ImageIO.read(getClass().getClassLoader().getResource("BuffIcons/shockwaveBuff.png"));
			buffIcons[2] = ImageIO.read(getClass().getClassLoader().getResource("BuffIcons/reinforceBuff.png"));
			buffIcons[3] = ImageIO.read(getClass().getClassLoader().getResource("BuffIcons/speedBuff.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			explosion[0] = ImageIO.read(getClass().getClassLoader().getResource("ExplosionGIF/1.png"));
			explosion[1] = ImageIO.read(getClass().getClassLoader().getResource("ExplosionGIF/2.png"));
			explosion[2] = ImageIO.read(getClass().getClassLoader().getResource("ExplosionGIF/3.png"));
			explosion[3] = ImageIO.read(getClass().getClassLoader().getResource("ExplosionGIF/4.png"));
			explosion[4] = ImageIO.read(getClass().getClassLoader().getResource("ExplosionGIF/5.png"));
			explosion[5] = ImageIO.read(getClass().getClassLoader().getResource("ExplosionGIF/6.png"));
			explosion[6] = ImageIO.read(getClass().getClassLoader().getResource("ExplosionGIF/7.png"));
			explosion[7] = ImageIO.read(getClass().getClassLoader().getResource("ExplosionGIF/8.png"));
			explosion[8] = ImageIO.read(getClass().getClassLoader().getResource("ExplosionGIF/9.png"));
			explosion[9] = ImageIO.read(getClass().getClassLoader().getResource("ExplosionGIF/final.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			redBulletExplosion[0] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/redBulletExplode1.png"));
			redBulletExplosion[1] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/redBulletExplode2.png"));
			redBulletExplosion[2] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/redBulletExplode3.png"));
			redBulletExplosion[3] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/redBulletExplode4.png"));
			redBulletExplosion[4] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/redBulletExplode5.png"));
			redBulletExplosion[5] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/redBulletExplode6.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			blueBulletExplosion[0] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/blueBulletExplode1.png"));
			blueBulletExplosion[1] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/blueBulletExplode2.png"));
			blueBulletExplosion[2] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/blueBulletExplode3.png"));
			blueBulletExplosion[3] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/blueBulletExplode4.png"));
			blueBulletExplosion[4] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/blueBulletExplode5.png"));
			blueBulletExplosion[5] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/blueBulletExplode6.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			greenBulletExplosion[0] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/greenBulletExplode1.png"));
			greenBulletExplosion[1] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/greenBulletExplode2.png"));
			greenBulletExplosion[2] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/greenBulletExplode3.png"));
			greenBulletExplosion[3] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/greenBulletExplode4.png"));
			greenBulletExplosion[4] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/greenBulletExplode5.png"));
			greenBulletExplosion[5] = ImageIO.read(getClass().getClassLoader().getResource("bulletExplosion/greenBulletExplode6.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			gHP = ImageIO.read(getClass().getClassLoader().getResource("Other/greenHp.png"));
			oHP = ImageIO.read(getClass().getClassLoader().getResource("Other/orangeHP.png"));
			rHP = ImageIO.read(getClass().getClassLoader().getResource("Other/redHp.png"));
			EMP = ImageIO.read(getClass().getClassLoader().getResource("MenuandUI/EMP.png"));			
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			deadScreen = ImageIO.read(getClass().getClassLoader().getResource("MenuandUI/deadScreen.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void draw() { //triggers draw methods, double buffers the screen
		BufferedImage screen = new BufferedImage(GraphicsMain.WIDTH, GraphicsMain.HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) screen.getGraphics();
		if(!Main.update.ship.basics)
			showBasics(g);
		else if(Main.appState == Main.GAME_STATE){
			drawBackground(g);
			drawShip(g);
			drawEnemies(g);
			drawGunSet(g);
			drawBullets(g);
			drawBuffs(g);
			drawHealth(g);
			checkBreach(g);
			checkStage(g);
			runExplosions();
		}
		else if(Main.appState == Main.DEAD_STATE) {
			drawDeadScreen(g);
		}
		dblBuffer.add(screen);
		if(dblBuffer.size() == 2) {
			this.g.drawImage(dblBuffer.poll(), 0, 0, GraphicsMain.WIDTH, GraphicsMain.HEIGHT, null);
		}
	}
	
	private void showBasics(Graphics2D g){
		g.drawImage(infoScreen, 0, 0, 1024, 768, null);
	}
	
	private void drawBackground(Graphics2D g) {
		lck.readLock().lock();
		if(Main.update.ship.getStage() == 1){
			g.drawImage(background, 0, 0, GraphicsMain.WIDTH, GraphicsMain.HEIGHT, null);
		}
		else{
			g.drawImage(background2, 0, 0, GraphicsMain.WIDTH, GraphicsMain.HEIGHT, null);
		}
		lck.readLock().unlock();
	}
	
	private void drawShip(Graphics2D g) {
		lck.readLock().lock();
		Ship ship = Main.update.ship;
		lck.readLock().unlock();
		g.drawImage(ship.getImage(), ship.getX(), ship.getY(), Ship.getWidth()/2, Ship.getHeight()/2, null);
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
	
	private void drawHealth(Graphics2D g) { //draws health bar 
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		lck.readLock().lock();
		g.drawImage(hpBar, GraphicsMain.WIDTH - 197, 25, 197, 99, null);
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
		
		if(Main.update.ship.getHealth() >= 1) {
			g.drawImage(rHP, GraphicsMain.WIDTH-135, 33, 20, 20, null);
			if(Main.update.ship.getHealth() >= 2) {
				g.drawImage(oHP, GraphicsMain.WIDTH-110, 33, 20, 20, null);
				if(Main.update.ship.getHealth() == 3) {
					g.setColor(Color.green);
					g.drawImage(gHP, GraphicsMain.WIDTH -85, 33, 20, 20, null);
				}
			}
			
		}
		if(Main.update.ship.getShockwave() >= 1){
			g.drawImage(EMP, GraphicsMain.WIDTH - 193, 73, 20, 20, null);
			if(Main.update.ship.getShockwave() >= 2){
				g.drawImage(EMP, GraphicsMain.WIDTH - 193, 93, 20, 20, null);
			}
		}
		lck.readLock().unlock();
	}
	
	private void drawGunSet(Graphics2D g) { //draws the icon for the current gun setting
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
	
	private void checkBreach(Graphics2D g) {
		if(Main.update.ship.getBreach() > 0){
			g.setColor(Color.WHITE);
			g.setFont(new Font("OCR A Extended", Font.BOLD, 34));
			g.drawString("BREACH!", GraphicsMain.WIDTH/2 - 60, GraphicsMain.HEIGHT/2 - 30);
			Main.update.ship.setBreach(Main.update.ship.getBreach() - 1);
		}
	}
	
	private void checkStage(Graphics2D g) {
		if(Main.update.ship.getnStage() > 0){
			g.setColor(Color.WHITE);
			g.setFont(new Font("OCR A Extended", Font.BOLD, 34));
			g.drawString("NEW STAGE DISCOVERED!", 245, 100);
			Main.update.ship.setnStage(Main.update.ship.getnStage() - 1);
		}
	}
	
	private void runExplosions() { //triggers the animation methods inside of the dying objects, running the explosion
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
	
	private void drawDeadScreen(Graphics2D g) {
		g.drawImage(deadScreen, 0, 0, GraphicsMain.WIDTH, GraphicsMain.HEIGHT, null);
	}
}
