package main;

/**
 * Main class for Trash Smash, maintains the game state, initializes game start and game threads, keeps game running in standard time
 * @author Ben Pinhorn
 *
 */
public class Main implements Runnable {
	//app resources
	private Thread updateThread;
	public static boolean running;
	public static Update update;
	private static GraphicsMain gMain;
	private static int appState = 0;
	public static final int MENU_BUILD_STATE = 0, GAME_STATE = 1, MENU_STATE = 2;
	
	//game variables should not be stored here, for game logic and updates, go to Update.java
	
	public static void main(String[] args) {
		Main main = new Main();
		if(appState == GAME_STATE) {
			main.init();
			main.start();
		}
		else if(appState == MENU_BUILD_STATE) {
			update = new Update();
			gMain = new GraphicsMain(main);
			gMain.createContentPane();
			appState = MENU_STATE;
		}
	}
	
	private void init() {
		gMain.init();
		gMain.start();
		//load non graphical resources
	}
	
	/**
	 * Starts update thread
	 */
	public synchronized void start() {
		running = true;
		updateThread = new Thread(this, "Update Thread");
		updateThread.start();
		
	}
	
	/**
	 * Closes app
	 */
	public synchronized void stop() { //quits app
		running  = false;
	}

	/**
	 * Keeps game running in standard time by triggering updates, and notifying the render thread when it should redraw the screen
	 */
	public void run() { 
		long lastTime = System.nanoTime();
		double nanoPerUpdate = 1000000000D/60D;
		double delta = 0D;
		
		if(appState == GAME_STATE) {
			while(running) {
				
				long now = System.nanoTime();
				delta += (now - lastTime) / nanoPerUpdate;
				lastTime = now;
				boolean shouldRender = false;
				
				while(delta >= 1) {
					update.update();
					delta--;
					shouldRender = true;
				}
				
				if(shouldRender) {
					gMain.resume();
				}
			}
		}
		
		
		if(running = false) {
			return;
		}
	}
	
	/**
	 * Triggers a change in game state from menu to game, triggers GraphicsMain to change its content pane
	 */
	public void gameStart() { 
		appState = GAME_STATE;
		gMain.gameStart();
		Main.main(null);
	}
}
