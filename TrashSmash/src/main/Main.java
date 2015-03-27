package main;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import listeners.KeyboardListener;

/**
 * Main class for Trash Smash, maintains the game state, initializes game start and game threads, keeps game running in standard time
 * @author Ben Pinhorn
 *
 */
public class Main {
	//app resources
	//test
	public static Update update;
	private static GraphicsMain gMain;
	public static int appState = 0;
	public static final int MENU_BUILD_STATE = 0, GAME_STATE = 1, MENU_STATE = 2;
	public static final ReentrantReadWriteLock lck = new ReentrantReadWriteLock();
	private static KeyboardListener kl;
	
	//game variables should not be stored here, for game logic and updates, go to Update.java
	
	public static void main(String[] args) {
		kl = new KeyboardListener();
		if(appState == GAME_STATE) {
			update = new Update();
			update.start();
			init();
		}
		else if(appState == MENU_BUILD_STATE) {
			gMain = new GraphicsMain(kl);
			gMain.createContentPane();
			appState = MENU_STATE;
		}
	}
	
	private static void init() {
		gMain.init();
		gMain.start();
		//load non graphical resources
	}
	
	/**
	 * Triggers a change in game state from menu to game, triggers GraphicsMain to change its content pane
	 */
	public static void gameStart() { 
		appState = GAME_STATE;
		gMain.gameStart();
		Main.main(null);
	}
	
	public static void exit(){
		System.exit(0);
	}
}
