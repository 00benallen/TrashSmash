package main;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import basicplayer1.BasicPlayer;
import basicplayer1.BasicPlayerException;
import listeners.KeyboardListener;
import res.Buff;
import res.Bullet;
import res.Enemy;
import res.Ship;

/**
 * Update class for Trash Smash, updates at 60 ups, runs game logic
 * @author Ben Pinhorn
 *
 */
public class Update implements Runnable {
	//update resources
	public volatile Ship ship = new Ship(GraphicsMain.WIDTH/2 - 96, GraphicsMain.HEIGHT - GraphicsMain.HEIGHT/16 - 96);
	private long lastEnemyGenTime = 2000, lastBulletGenTime = 500, enemyFrequency = 2000, enemiesGenerated = 0;
	private long lastBuffGenTime = 16000, bulletGenSpeed = 500;
	private boolean stageOne = false, stageTwo = false, stageThree = false;
	public volatile LinkedList<Enemy> enemies = new LinkedList<Enemy>(); 
	public volatile LinkedList<Bullet> bullets = new LinkedList<Bullet>();
	public volatile LinkedList<Buff> buffs = new LinkedList<Buff>();
	
	//music resources
	private static BasicPlayer player;
	
	//thread resources
	public volatile ReentrantReadWriteLock lck = Main.lck;
	private Thread updateThread;
	public volatile static boolean running;
	
	/**
	 * Starts update thread
	 */
	public synchronized void start() {
		running = true;
		updateThread = new Thread(this, "Update Thread");
		updateThread.start();
		init();
	}
	
	/**
	 * Run loop for update thread, keeps its own UPS
	 */
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
	 * Terminates update thread
	 */
	public synchronized void stop() {
		running  = false;
	}
	
	private void init() {
		playMusic();
	}
	
	private void playMusic() {
		player = new BasicPlayer();
		try {
			player.stop();
		    player.open(getClass().getClassLoader().getResource("Music/Battle.mp3"));
		    player.play();
		} catch (BasicPlayerException e) {
		    e.printStackTrace();
		}
	}

	private void update() {
		moveShip();
		generateEnemies();
		generateBuffs();
		createBullets();
		moveEnemies();
		removeEnemies();
		moveBullets();
		removeBullets();
		checkCollisions();
		toggleMusic();
		repeatMusic();
		removeShip();
	}
	
	private void moveShip() {
		lck.writeLock().lock();
		if(KeyboardListener.up) {
			if(ship.getY() - Ship.getVelocity() > 0) {
				ship.setY(ship.getY() - Ship.getVelocity());
			}	
		}
		if(KeyboardListener.down) {
			if(ship.getY() + Ship.getWidth()/2 + Ship.getVelocity() < GraphicsMain.HEIGHT) {
				ship.setY(ship.getY() + Ship.getVelocity());
			}	
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
		if(milliSecondsElapsed >= enemyFrequency) { //generates enemies at a certain frequency
			lastEnemyGenTime = System.currentTimeMillis();
			Random r = new Random(); //randomizes their location
			int x = r.nextInt(GraphicsMain.WIDTH-256) + 128;
			int type = r.nextInt(20);
			lck.writeLock().lock();
			enemies.add(new Enemy(x, -128, type));
			lck.writeLock().unlock();
			enemiesGenerated++;
			if(enemiesGenerated == 12 && stageOne == false){ //if statements change the frequency by stage
				enemyFrequency -= 1200;
				stageOne = true;
			}
			if(enemiesGenerated > 16 && stageOne == true){
				enemyFrequency += 1200;
				stageOne = false;
			}
			if(enemiesGenerated == 30 && stageTwo == false){
				enemyFrequency -= 1300;
				stageTwo = true;
			}
			if(enemiesGenerated > 40 && stageTwo == true){
				enemyFrequency += 1300;
				stageTwo = false;
			}
			if(enemiesGenerated == 65 && stageThree == false){
				enemyFrequency -= 1400;
				stageThree = true;
			}
			if(enemiesGenerated > 80 && stageThree == true){
				enemyFrequency += 1400;
				stageThree = false;
			}
		}
	}
	
	private void generateBuffs(){ //similar to enemy generation, creates random buffs at a certain frequency with random location
		long currentTime = System.currentTimeMillis();
		double milliSecondsElapsed = currentTime - lastBuffGenTime;
		if(milliSecondsElapsed >= 14000) { 
			lastBuffGenTime = System.currentTimeMillis();
			Random r = new Random();
			int x = 0;
			int y = 0;
			Rectangle2D buffTest = new Rectangle2D.Double(x, y, 29, 29);
			for(int i = 0; i < buffs.size(); i++){
				if(buffTest.contains(buffs.get(i).getBoundBox())){
					x = r.nextInt(GraphicsMain.WIDTH-128);
					y = r.nextInt(GraphicsMain.HEIGHT-128);
					buffTest = new Rectangle2D.Double(x, y, 29, 29);
				}				
			}
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
			if(!bullets.get(i).isExplode) {
				bullets.get(i).move();
			}
			
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
							ship.setScore(ship.getScore() + 500);
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
	
	private void toggleMusic(){ //allows player to mute music
		if(!KeyboardListener.toggle) return; 
		else KeyboardListener.toggle = false;
		lck.writeLock().lock();
		if(player.getStatus() != BasicPlayer.PAUSED){
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
				e.printStackTrace();
			}
		}
		lck.writeLock().unlock();
	}
	
	public void repeatMusic(){
		if(player.getStatus() == 2){
			try {
				player.play();
			} catch (BasicPlayerException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void removeShip() { //remove the ship and sets game back to menu
		lck.writeLock().lock();
		
		if(ship.getHealth() <= 0) {
			Main.menuStart();
			try {
				player.stop();
			} catch (BasicPlayerException e) {
				e.printStackTrace();
			}
		}
		
		lck.writeLock().unlock();
	}
}
