package main;

import java.util.concurrent.LinkedBlockingQueue;

import res.Ship;

/**
 * Update class for Trash Smash, updates at 60 ups, runs game logic
 * @author Ben Pinhorn
 *
 */
public class Update {
	private Ship ship = new Ship(GraphicsMain.WIDTH/2 - 63, GraphicsMain.HEIGHT - GraphicsMain.HEIGHT/16 - 88);
	public volatile LinkedBlockingQueue<Object> drawQueue = new LinkedBlockingQueue<Object>();
	public boolean shipUpdated = false;

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
