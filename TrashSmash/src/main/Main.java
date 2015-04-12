package main;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import basicplayer1.BasicPlayer;
import basicplayer1.BasicPlayerException;
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
	public static GraphicsMain gMain;
	public static int appState = 0;
	public static final int MENU_BUILD_STATE = 0, GAME_STATE = 1, MENU_STATE = 2, INFO_STATE = 3, DEAD_STATE = 4;
	public static final ReentrantReadWriteLock lck = new ReentrantReadWriteLock();
	private static KeyboardListener kl;
	private static boolean isNew = true;
	
	//game variables should not be stored here, for game logic and updates, go to Update.java
	
	public static void main(String[] args) throws Exception {
		kl = new KeyboardListener();
		if(appState == GAME_STATE) {
			update = new Update();
			update.start();
			init();
		}
		else if(appState == MENU_BUILD_STATE) {
			if(isNew) {
				gMain = new GraphicsMain(kl);
				isNew = false;
			}
			
			gMain.createContentPane();
			appState = MENU_STATE;
		}
		else if(appState == INFO_STATE){
			
		}
		else if(appState == DEAD_STATE){
			
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
		try {
			Main.main(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
