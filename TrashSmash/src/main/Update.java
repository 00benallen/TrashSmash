package main;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import listeners.KeyboardListener;
import res.Bullet;
import res.Enemy;
import res.Ship;

/**
 * Update class for Trash Smash, updates at 60 ups, runs game logic
 * @author Ben Pinhorn
 *
 */
public class Update implements Runnable {
	private ReentrantReadWriteLock lck = Main.lck;
	private Thread updateThread;
	public volatile static boolean running;
	public volatile Ship ship = new Ship(GraphicsMain.WIDTH/2 - 96, GraphicsMain.HEIGHT - GraphicsMain.HEIGHT/16 - 96);
	private long lastEnemyGenTime = 2000, lastBulletGenTime = 500;
	public volatile LinkedList<Enemy> enemies = new LinkedList<Enemy>(); 
	public volatile LinkedList<Bullet> bullets = new LinkedList<Bullet>();
	
	
	
	/**
	 * Starts update thread
	 */
	public synchronized void start() {
		running = true;
		updateThread = new Thread(this, "Update Thread");
		updateThread.start();
		init();
	}
	
	public void run() { 
		long lastTime = System.nanoTime();
		double nanoPerUpdate = 1000000000D/60D;
		double delta = 0D;
		if(Main.appState == Main.GAME_STATE) {
			while(running) {
				long now = System.nanoTime();
				delta += (now - lastTime) / nanoPerUpdate;
				lastTime = now;
				
				while(delta >= 1) {
					update();
					delta--;
				}
			}
		}
		if(running = false) {
			return;
		}
	}
	
	/**
	 * Closes app
	 */
	public synchronized void stop() { //quits app
		running  = false;
	}
	
	public void init() {
	}

	private void update() {
		//insert other update methods
		moveShip();
		generateEnemies();
		moveEnemies();
		removeEnemies();
		createBullets();
		moveBullets();
		removeBullets();
		
	}
	
	public void moveShip() {
		lck.writeLock().lock();
		if(KeyboardListener.up) {
			ship.setY(ship.getY() - Ship.getVelocity());
		}
		if(KeyboardListener.down) {
			ship.setY(ship.getY() + Ship.getVelocity());
		}
		if(KeyboardListener.left) {
			ship.move(2);
		}
		if(KeyboardListener.right) {
			ship.move(1);
		}
		lck.writeLock().unlock();
	}
	
	private void generateEnemies() {
		long currentTime = System.currentTimeMillis();
		double milliSecondsElapsed = currentTime - lastEnemyGenTime;
		if(milliSecondsElapsed >= 2000) {
			lastEnemyGenTime = System.currentTimeMillis();
			Random r = new Random();
			int x = r.nextInt(GraphicsMain.WIDTH-128);
			int type = r.nextInt(9);
			lck.writeLock().lock();
			enemies.add(new Enemy(x, -128, type));
			lck.writeLock().unlock();
		}
	}
	
	private void moveEnemies() {
		lck.writeLock().lock();
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.move();
			
		}
		lck.writeLock().unlock();
	}
	
	private void removeEnemies() {
		lck.writeLock().lock();
			for(int i = 0; i < enemies.size(); i++) {
				Enemy e = enemies.get(i);
				if(e.getY() > GraphicsMain.HEIGHT) {
					enemies.remove(i);
			}
		}
		lck.writeLock().unlock();
	}
	
	
	private void createBullets() {
		if(KeyboardListener.shoot) {
			if(System.currentTimeMillis() - lastBulletGenTime >= 500) {
				lastBulletGenTime = System.currentTimeMillis();
				lck.writeLock().lock();
				bullets.add(new Bullet(ship.getX() + Ship.getWidth()/4 - Bullet.width/2, ship.getY(), ship.getGunSet()));
				lck.writeLock().unlock();
			}
		}
	}
	
	private void moveBullets() {
		lck.writeLock().lock();
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).move();
		}
		lck.writeLock().unlock();
	}
	
	private void removeBullets() {
		lck.writeLock().lock();
		for(int i = 0; i < bullets.size(); i++) {
			if(bullets.get(i).getY() < 10) {
				bullets.remove(i);
			}
		}
		lck.writeLock().unlock();
	}
}
