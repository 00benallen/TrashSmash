package main;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import listeners.KeyboardListener;
import res.Buff;
import res.Bullet;
import res.Enemy;
import res.Ship;

import java.awt.Graphics2D;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

/**
 * Update class for Trash Smash, updates at 60 ups, runs game logic
 * @author Ben Pinhorn
 *
 */
public class Update implements Runnable {
	public volatile ReentrantReadWriteLock lck = Main.lck;
	private Thread updateThread;
	public volatile static boolean running;
	public volatile Ship ship = new Ship(GraphicsMain.WIDTH/2 - 96, GraphicsMain.HEIGHT - GraphicsMain.HEIGHT/16 - 96);
	private long lastEnemyGenTime = 2000, lastBulletGenTime = 500;
	private long lastBuffGenTime = 16000, bulletGenSpeed = 500;
	public volatile LinkedList<Enemy> enemies = new LinkedList<Enemy>(); 
	public volatile LinkedList<Bullet> bullets = new LinkedList<Bullet>();
	public volatile LinkedList<Buff> buffs = new LinkedList<Buff>();
	private static BasicPlayer player;
	public Graphics2D g;
	
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
		playMusic();
	}

	private void update() {
		//insert other update methods
		changeShip();
		generateEnemies();
		generateBuffs();
		createBullets();
		moveEnemies();
		removeEnemies();
		moveBullets();
		removeBullets();
		checkCollisions();
		toggleMusic();
	}
	
	public void playMusic() {
		File battle;
		battle = new File("Assets/Music/Battle.mp3");
		player = new BasicPlayer();
		try {
		    player.open(battle);
		    player.play();
		} catch (BasicPlayerException e) {
		    e.printStackTrace();
		}
	}
	
	public void toggleMusic(){
		if(!KeyboardListener.toggle) return; 
		else KeyboardListener.toggle = false;
		lck.writeLock().lock(); //not sure if necessary
		if(player.getStatus() != player.PAUSED){
			try {
				player.pause();
			} catch (BasicPlayerException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				player.resume();
			} catch (BasicPlayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		lck.writeLock().unlock();
	}
	
	public void changeShip() {
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
		if(KeyboardListener.Q){
			KeyboardListener.Q = false;
			ship.setGunSet(ship.getGunSet() - 1);
		}
		if(KeyboardListener.E){
			KeyboardListener.E = false;
			ship.setGunSet(ship.getGunSet() + 1);
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
			int type = r.nextInt(20);
			lck.writeLock().lock();
			enemies.add(new Enemy(x, -128, type));
			lck.writeLock().unlock();
		}
	}
	
	private void generateBuffs(){
		long currentTime = System.currentTimeMillis();
		double milliSecondsElapsed = currentTime - lastBuffGenTime;
		if(milliSecondsElapsed >= 15000) {
			lastBuffGenTime = System.currentTimeMillis();
			Random r = new Random();
			int x = r.nextInt(GraphicsMain.WIDTH-128);
			int y = r.nextInt(GraphicsMain.HEIGHT-128);
			int type = r.nextInt(4);
			lck.writeLock().lock();
			buffs.add(new Buff(x, y, type));
			lck.writeLock().unlock();
		}
	}
	
	private void moveEnemies() {
		lck.writeLock().lock();
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if(!e.isExplode()) {
				e.move();
			}
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
			if(e.isDead()) {
				enemies.remove(i);
			}
		}
		lck.writeLock().unlock();
	}
	
	
	private void createBullets() {
		if(KeyboardListener.shoot) {
			if(System.currentTimeMillis() - lastBulletGenTime >= bulletGenSpeed) {
				lastBulletGenTime = System.currentTimeMillis();
				lck.writeLock().lock();
				bullets.add(new Bullet(ship.getX() + Ship.getWidth()/4 - Bullet.width/2, ship.getY(), ship.getGunSet(), true));
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
			else if(bullets.get(i).isDead()){
				bullets.remove(i);
			}
		}
		lck.writeLock().unlock();
	}
	
	private void checkCollisions() {
		checkBulletsWithEnemies();
		checkBulletsWithShip();
		checkEnemiesWithShip();
		checkBuffsWithShip();
	}
	
	private void checkBuffsWithShip() {
		lck.writeLock().lock();
		for(int i = 0; i < buffs.size(); i++) {
			if(!buffs.get(i).isDead()) {
				if(buffs.get(i).checkCollision(ship)) {
					buffs.get(i).setDead(true);
					ship.setScore(ship.getScore() + 150);
					if(buffs.get(i).getTypeCode() == 0){ //HP Restore
						ship.heal(2);
					}
					if(buffs.get(i).getTypeCode() == 1){ //Attack Speed Buff
						ship.heal(1);
						bulletGenSpeed = (long) Math.floor(bulletGenSpeed * 0.95);
					}
					if(buffs.get(i).getTypeCode() == 2){ //Shockwave
						ship.heal(1);
						for(int j = 0; j < enemies.size(); j++) {
							enemies.get(j).explode();
						}
					}
					if(buffs.get(i).getTypeCode() == 3){ //Dunno. Make something up.
						ship.heal(1);
					}
				}
			}
		}
		lck.writeLock().unlock();
	}

	private void checkBulletsWithEnemies() {
		lck.writeLock().lock();
		for(int i = 0; i < bullets.size(); i++) {
			for(int j = 0; j < enemies.size(); j++) {
				if(bullets.get(i).isShip()) {
					if(!enemies.get(j).isDead() && !enemies.get(j).isExplode()) {
						if(bullets.get(i).checkCollision(enemies.get(j))) {
							if(bullets.get(i).getType() == enemies.get(j).getTrashType()){
								ship.setScore(ship.getScore() + 1000);
								bullets.get(i).explode();
								enemies.get(j).explode();
								break;
							}
							else{
								ship.setScore(ship.getScore() - 20);
								bullets.get(i).explode();
							}
						}
					}
				}
			}
		}
		lck.writeLock().unlock();
	}
	
	private void checkBulletsWithShip() {
		lck.writeLock().lock();
		for(int i = 0; i < bullets.size(); i++) {
			if(!bullets.get(i).isShip()) {
				if(bullets.get(i).checkCollision(ship)) {
					bullets.get(i).explode();
					ship.damage();
				}
			}
		}
		lck.writeLock().unlock();
	}
	
	private void checkEnemiesWithShip() {
		lck.writeLock().lock();
		for(int i = 0; i < enemies.size(); i++) {
			if(!enemies.get(i).isDead() && !enemies.get(i).isExplode()) {
				if(enemies.get(i).checkCollision(ship)) {
					ship.setScore(ship.getScore() - 150);
					enemies.get(i).explode();
					ship.damage();
				}
			}
		}
		lck.writeLock().unlock();
	}
}
