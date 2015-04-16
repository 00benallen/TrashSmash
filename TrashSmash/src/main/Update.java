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
	//Update resources
	public volatile Ship ship = new Ship(GraphicsMain.WIDTH/2 - 96, GraphicsMain.HEIGHT - GraphicsMain.HEIGHT/16 - 96);
	private long lastEnemyGenTime = 2000, lastBulletGenTime = 500, enemyFrequency = 2000;
	public long enemiesGenerated = 0;
	private long lastBuffGenTime = 16000, bulletGenSpeed = 500;
	private boolean stageOne = false, stageTwo = false, stageThree = false;
	public volatile LinkedList<Enemy> enemies = new LinkedList<Enemy>(); 
	public volatile LinkedList<Bullet> bullets = new LinkedList<Bullet>();
	public volatile LinkedList<Buff> buffs = new LinkedList<Buff>();
	public boolean musicSet = false;
	public boolean musicSet2 = false;
	
	//Music resources
	private static BasicPlayer player;
	
	//Thread resources
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
	
	/**
	 * Opens new music stream and begins playing it
	 */
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

	/**
	 * Executes all game actions
	 */
	private void update() {
		//If the Info screen has not been shown, shows the Info screen before any gameplay occurs.
		if(!Main.basics)
			showBasics();
		else{
			controlShip();
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
			setMusic();
			removeShip();
			deadTrigger();
		}
	}
	
	/**
	 * Terminates the showBasics portion if Spacebar is pressed
	 */
	private void showBasics(){
		if(KeyboardListener.shoot){
			Main.basics = true;
		}
	}
	
	/**
	 * Interprets input from keyboard listener and controls the ship accordingly.
	 * Includes shooting, moving, and using EMPs.
	 */
	private void controlShip() {
		lck.writeLock().lock();
		if(KeyboardListener.up) {
			if(ship.getY() > 16) { //accounts for height of window's border
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
		if(KeyboardListener.R){
			KeyboardListener.R = false;
			if(ship.getShockwave() >= 1){
				for(int j = 0; j < enemies.size(); j++) {
					enemies.get(j).explode();
					ship.setScore(ship.getScore() + 500);
				}
				ship.setShockwave(ship.getShockwave() - 1);
			}
		}
		lck.writeLock().unlock();
	}
	
	/**
	 * Spawns new enemy ships, increasing in difficulty with time.
	 * Also spawns boss waves at certain intervals
	 */
	private void generateEnemies() {
		long currentTime = System.currentTimeMillis();
		double milliSecondsElapsed = currentTime - lastEnemyGenTime;
		if(milliSecondsElapsed >= enemyFrequency) { //generates enemies at a certain frequency
			lastEnemyGenTime = System.currentTimeMillis();
			Random r = new Random(); //Randomizes their location
			int x = r.nextInt(GraphicsMain.WIDTH-256) + 128;
			int type = r.nextInt(20);
			lck.writeLock().lock();
			enemies.add(new Enemy(x, -128, type));
			lck.writeLock().unlock();
			enemiesGenerated++;
			if(enemiesGenerated == 12 && stageOne == false){ //If statements change the frequency by stage
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
				ship.setnStage(200);
				ship.setStage(2);
				enemyFrequency -= 1400;
				stageThree = true;
			}
			if(enemiesGenerated > 80 && stageThree == true){
				enemyFrequency += 1400;
				stageThree = false;
			}
		}
	}
	
	/**
	 * Creates power-ups and distributes them randomly in space.
	 */
	private void generateBuffs(){ //similar to enemy generation, creates random buffs at a certain frequency with random location
		long currentTime = System.currentTimeMillis();
		double milliSecondsElapsed = currentTime - lastBuffGenTime;
		if(milliSecondsElapsed >= 12000) { 
			lastBuffGenTime = System.currentTimeMillis();
			Random r = new Random();
			int x = 500;
			int y = 400;
			Rectangle2D buffTest = new Rectangle2D.Double(x, y, 29, 29);
			for(int i = 0; i < buffs.size(); i++){ //Tries to encourage buffs to not spawn on the same place
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
	
	/**
	 * Handles enemy movement - if not dead, they move.
	 */
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
	
	/**
	 * Checks - if enemy is either out of bounds (decrease health and remove), or dead (explode and remove)
	 */
	private void removeEnemies() {
		lck.writeLock().lock();
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if(e.getY() > GraphicsMain.HEIGHT) {
				enemies.remove(i);
				ship.damage();
				ship.setScore(ship.getScore() - 1000);
				ship.setBreach(60);
			}
			if(e.isDead()) {
				enemies.remove(i);
			}
		}
		lck.writeLock().unlock();
	}
	
	/**
	 * Generates new bullet objects if the ship is firing.
	 */
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
	
	/**
	 * Handles bullet movements
	 */
	private void moveBullets() {
		lck.writeLock().lock();
		for(int i = 0; i < bullets.size(); i++) {
			if(!bullets.get(i).isExplode) {
				bullets.get(i).move();
			}
			
		}
		lck.writeLock().unlock();
	}
	
	/**
	 * Checks to remove any bullets that have hit objects or is out of bounds
	 */
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
	
	/**
	 * Checks collision of bullets, ship, buffs, and enemies.
	 */
	private void checkCollisions() {
		checkBulletsWithEnemies();
		checkBulletsWithShip();
		checkEnemiesWithShip();
		checkBuffsWithShip();
	}
	
	/**
	 * Sees if the ship has encountered a power-up and correctly applies the boon.
	 */
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
						ship.setShockwave(ship.getShockwave() + 1);
					}
					if(buffs.get(i).getTypeCode() == 3){ //Complete Ship Repairs
						ship.heal(3);
					}
				}
			}
		}
		lck.writeLock().unlock();
	}

	/**
	 * Checks to see if enemies have been struck by bullets. Checks to see if types match, then sets enemies and bullets to dead.
	 */
	private void checkBulletsWithEnemies() {
		lck.writeLock().lock();
		for(int i = 0; i < bullets.size(); i++) {
			for(int j = 0; j < enemies.size(); j++) {
				if(bullets.get(i).isShip()) {
					if(!enemies.get(j).isDead() && !enemies.get(j).isExplode()) {
						if(bullets.get(i).checkCollision(enemies.get(j))) {
							if(bullets.get(i).getType() == enemies.get(j).getTrashType()){ //Checking for matching type
								ship.setScore(ship.getScore() + 1000);
								bullets.get(i).explode();
								enemies.get(j).explode();
								break;
							}
							else{ //Otherwise penalize the player for shooting incorrectly.
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
	
	/**
	 * Checks to see if any bullets collided with the ship, and decreases health accordingly.
	 */
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
	
	/**
	 * Checks the collision of invading spacecraft with the ship, blowing up invaders but causing score loss and HP loss to the ship in the process.
	 */
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
	
	/**
	 * Switching between Muting and Unmuting the music.
	 */
	private void toggleMusic(){ 
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
	
	/**
	 * Allows the music player to repeat the music once it has ended.
	 */
	public void repeatMusic(){
		if(player.getStatus() == 2){ //If Player status is STOPPED
			try {
				player.play();
			} catch (BasicPlayerException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Changes the music depending on what stage it is, and if the player is dead.
	 */
	public void setMusic(){
		lck.writeLock().lock();
		if(Main.update.ship.getStage() == 2 && !musicSet){ //2nd stage music
			try {
				player.stop();
				player.open(getClass().getClassLoader().getResource("Music/Battle2.mp3"));
			    player.play();
			    musicSet = true;
			} catch (BasicPlayerException e) {
			    e.printStackTrace();
			}
		}
		else if(Main.update.ship.getStage() == 0 && !musicSet2){ //Dead music
			try {
				player.stop();
				player.open(getClass().getClassLoader().getResource("Music/Death.mp3"));
			    player.play();
			    musicSet2 = true;
			} catch (BasicPlayerException e) {
			    e.printStackTrace();
			}
		}
		lck.writeLock().unlock();
	}
	
	/**
	 * Removal of ship after death, and puts it into Death state
	 */
	private void removeShip() { //remove the ship and sets game back to menu
		lck.writeLock().lock();
		
		if(ship.getHealth() <= 0) {
			Main.appState = Main.DEAD_STATE;
		}
		
		lck.writeLock().unlock();
	}
	
	/**
	 * If spacebar is pressed in Death state, the game goes back to Main Menu, stopping all music and running.
	 */
	private void deadTrigger() {
		if(Main.appState == Main.DEAD_STATE) {
			if(KeyboardListener.shoot == true) {
				try {
					player.stop();
				} catch(BasicPlayerException e) {
					e.printStackTrace();
				}
				Main.menuStart();
			}
		}
	}
}
