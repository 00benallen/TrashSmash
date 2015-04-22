package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

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
	public static long score;
	
	//thread resources
	public static Update update;
	
	//Twitter resources
	public static String htmlFilePath;
	public static Twitter twitter;
	public static RequestToken requestToken = null;
	public static AccessToken accessToken = null;
	public static String pin;
	public static String msg1 = "I just got a score of ", msg2 = " in TrashSmash. Can you stop the garbage?";
	
	/**
	 * Builds game, can be used to reset game state
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		kl = new KeyboardListener();
		initTwitter();
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
	
	/**
	 * Initializes twitter capabilities.
	 */
	public static void initTwitter(){
		twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer("nQZ4c9w1beTSPR4fT7HaJ1hGN",
				"dMXkSXV0FZDGTyQdBOypHxii8Xty8r0QqqGUjwBfozpoSU7m6q");
		try {
			requestToken = twitter.getOAuthRequestToken();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		htmlFilePath = requestToken.getAuthorizationURL().toString();
	}
	
	public static void openTwitter(){
		try {
			Desktop.getDesktop().browse(new URI(htmlFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks validity of data entered and tweets a message if the authorization checks out
	 */
	public static void tweet(){
		try {
			if (pin.length() > 0) {
				accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			} else {
				accessToken = twitter.getOAuthAccessToken();
			}
			try {
				twitter.updateStatus(msg1 + "" + score + msg2);
			} catch (TwitterException e) {
				if(e.getErrorCode() == 187) System.out.println("Sorry, duplicate tweets not allowed");
				else e.printStackTrace();
			}
		} catch (TwitterException te) {
			if (401 == te.getStatusCode()) {
				System.out.println("Unable to get the access token.");
			} else {
				te.printStackTrace();
			}
		}
		pin = new String("");
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
