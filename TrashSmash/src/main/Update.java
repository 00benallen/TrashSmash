package main;

import java.util.concurrent.LinkedBlockingQueue;

import res.Ship;

/**
 * Update class for Trash Smash, updates at 60 ups, runs game logic
 * @author Ben Pinhorn
 *
 */
public class Update implements Runnable {
	private Ship ship = new Ship(GraphicsMain.WIDTH/2 - 63, GraphicsMain.HEIGHT - GraphicsMain.HEIGHT/16 - 88);
	public volatile LinkedBlockingQueue<Object> drawQueue = new LinkedBlockingQueue<Object>();
	public boolean shipUpdated = false;
	private Thread updateThread;
	public static boolean running;
	
	
	/**
	 * Starts update thread
	 */
	public synchronized void start() {
		running = true;
		updateThread = new Thread(this, "Update Thread");
		updateThread.start();
		
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

	public void update() {
		moveShip();
		
	}
	
	public void moveShip() {
		//insert movement operation
		if(shipUpdated) {
			queueObject(ship);
		}
	}
	
	public void queueObject(Object object) {
		try {
			drawQueue.put(object);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
