package main;


import java.util.concurrent.locks.ReentrantReadWriteLock;
import res.Ship;

/**
 * Update class for Trash Smash, updates at 60 ups, runs game logic
 * @author Ben Pinhorn
 *
 */
public class Update implements Runnable {
	private ReentrantReadWriteLock lck = Main.lck;
	private Thread updateThread;
	public static boolean running;
	public Ship ship = new Ship(GraphicsMain.WIDTH/2 - 128, GraphicsMain.HEIGHT - GraphicsMain.HEIGHT/16 - 128);
	
	
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

	public void update() {
		//insert other update methods
		moveShip();
		
	}
	
	public void moveShip() {
		lck.writeLock().lock();
		//insert movement operation
		
		lck.writeLock().unlock();
	}
}
