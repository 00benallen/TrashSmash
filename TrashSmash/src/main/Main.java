package main;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import listeners.KeyboardListener;

/**
 * Main class for Trash Smash, maintains the game state, initializes game start and game threads, keeps game running in standard time
 * @author Ben Pinhorn
 * 
 */
public class Main {
	
	//game variables should not be stored here, for game logic and updates, go to Update.java
	
	//app resources
	public static final int MENU_BUILD_STATE = 0, GAME_STATE = 1, MENU_STATE = 2, INFO_STATE = 3, DEAD_STATE = 4;
	public static GraphicsMain gMain;
	public static int appState = 0;
	public static final ReentrantReadWriteLock lck = new ReentrantReadWriteLock();
	private static KeyboardListener kl;
	public static boolean isNew = true, basics = false;
	
	//thread resources
	public static Update update;
	
	/**
	 * Builds game, can be used to reset game state
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		kl = new KeyboardListener();
		if(appState == GAME_STATE) {
			update = new Update();
			update.start();
			init();
		}
		else if(appState == MENU_BUILD_STATE) {
			if(isNew) { //stops game from rebuilding entire thing if it is already running
				gMain = new GraphicsMain(kl);
				isNew = false;
			}
			gMain.createContentPane();
			appState = MENU_STATE;
		}
	}
	
	private static void init() {
		gMain.start();
	}
	
	/**
	 * Triggers a change in game state from menu to game, triggers GraphicsMain to change its content pane
	 */
	public static void gameStart() { 
		appState = GAME_STATE;
		gMain.gameStart();
		try {
			Main.main(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Triggers a change in game state from game to menu
	 */
	public static void menuStart() {
		appState = MENU_BUILD_STATE;
		Update.running = false;
		gMain.menuStart();
		try {
			Main.main(null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Exits game safely by terminating the threads, stopping the music player, and disposing the window
	 */
	public static void exit(){
		Update.running = false;
		try {
			gMain.player.stop();
		} catch (basicplayer1.BasicPlayerException e) {
			e.printStackTrace();
		}
		gMain.window.dispose();
		
	}
}
