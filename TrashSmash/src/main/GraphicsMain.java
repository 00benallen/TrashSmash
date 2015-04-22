package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;

import java.net.URL;

import basicplayer1.BasicPlayer;
import res.ImagePanel;
import res.ScoreList;
import listeners.ButtonListener;
import listeners.KeyboardListener;

/**
 * Main graphics class for Trash Smash, generates window, starts render thread, creates main menu
 * @author Ben Pinhorn
 * @author Brian Chen
 * @author Tristan Monger
 */
public class GraphicsMain {
	
	//Window variables
	public JFrame window = new JFrame("Trash Smash");
	public JPanel infoPane;
	public final static int WIDTH = 1024;
	public final static int HEIGHT = 768;
	
	//Music variables
	public BasicPlayer player = new BasicPlayer();
	
	//Thread variables
	private Thread renderThread;
	public Render render;
	
	//Listeners
	private ButtonListener l;
	private KeyboardListener kl;
	
	//Menu variables
	private ImageIcon sButton, qButton, scButton;
	public final String MAIN_MENU = "MAINMENU", SCORES_MENU = "SCORESMENU", DEAD_MENU = "DEADMENU";
	public String menuPane;
	ScoreList scores;
	JPanel gamePanel;
	
	public String accessPin;
	public JTextField code;
	
	
	/**
	 * Constructor for graphics main, opens the window
	 * @param k
	 */
	public GraphicsMain(KeyboardListener k) {
		l = new ButtonListener();
		kl = k;
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		window.setResizable(false);
		window.pack();
		window.setFocusable(true);
		window.addKeyListener(kl);
		init();
	}
	
	/**
	 * Loads images for use in menus.
	 */
	private void init() {
		sButton = new ImageIcon(getClass().getClassLoader().getResource("MenuandUI/stbutton.png"));
		qButton = new ImageIcon(getClass().getClassLoader().getResource("MenuandUI/qbutton.png"));
		scButton = new ImageIcon(getClass().getClassLoader().getResource("MenuandUI/scButton.png"));
	}
	
	/**
	 * Starts render thread 
	 */
	public synchronized void start() {
		renderThread = new Thread(render, "Render Thread");
		renderThread.start();
		try {
			player.stop();
		} catch (basicplayer1.BasicPlayerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops any previous music and starts the menu theme music (Giant Woman by Steven Universe)
	 */
	private void playMusic() {
		try {
			player.stop();
		    player.open(getClass().getClassLoader().getResource("Music/Menu2.mp3"));
		    player.play();
		} catch (basicplayer1.BasicPlayerException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * Generates the Main Menu (and clears any other display)
	 */
	public JPanel createContentPane() {
		playMusic();
		window.setVisible(false);
		window.getContentPane().removeAll();
		
		ImagePanel imgPanel = new ImagePanel("MenuandUI/MenuMockup.png");
		
		JPanel mainMenuPane = new JPanel();
		JPanel controlPane = new JPanel();
		JPanel animatePane = new JPanel();
		JPanel buttonsPane = new JPanel();
		
		mainMenuPane.setLayout(new OverlayLayout(mainMenuPane));

		URL url = GraphicsMain.class.getResource("SpinningEarth.gif");
		ImageIcon imageIcon = new ImageIcon(url);
		JLabel label = new JLabel(imageIcon);
		
		JButton startButton = new JButton();
		JButton quitButton = new JButton();
		JButton scoresButton = new JButton();
		
		startButton.setIcon(sButton);
		startButton.setBorder(null);
		startButton.addActionListener(l);
		startButton.setActionCommand("start");
		
		quitButton.setIcon(qButton);
		quitButton.setBorder(null);		
		quitButton.addActionListener(l);
		quitButton.setActionCommand("quit");
		
		scoresButton.setIcon(scButton);
		scoresButton.setBorder(null);
		scoresButton.addActionListener(l);
		scoresButton.setActionCommand("scores");
		
		controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.Y_AXIS));
		buttonsPane.setLayout(new BoxLayout(buttonsPane, BoxLayout.X_AXIS));
		
		controlPane.add(Box.createRigidArea(new Dimension(20, 120)));
		controlPane.add(startButton);
		
		controlPane.add(Box.createRigidArea(new Dimension(0, 40)));
		controlPane.add(scoresButton);
		
		controlPane.add(Box.createRigidArea(new Dimension(0, 40)));
		controlPane.add(quitButton);
		
		buttonsPane.add(controlPane);
		buttonsPane.add(Box.createRigidArea(new Dimension(670, 0)));
		animatePane.add(label);
		
		controlPane.setOpaque(false);
		animatePane.setOpaque(false);
		buttonsPane.setOpaque(false);
		
		animatePane.add(Box.createRigidArea(new Dimension(0,800)));
		
		mainMenuPane.add(buttonsPane);
		mainMenuPane.add(animatePane);
		mainMenuPane.add(imgPanel);
		
		JPanel contentPane = new JPanel(new CardLayout());
		contentPane.add(mainMenuPane, MAIN_MENU);
		
		JPanel highscoresPanel = new JPanel();
		highscoresPanel.setLayout(new OverlayLayout(highscoresPanel));
		ImagePanel highscoresBack = new ImagePanel("MenuandUI/scBackground.png");
		JPanel scorePanel = new JPanel();
		
		JTextArea scores = new JTextArea();
		scores.setEditable(false);
		scores.setBorder(null);
		scores.setBackground(null);
		scores.setText(generateScores());
		scorePanel.add(scores);
		
		code = new JTextField(15);
		
		scorePanel.add(code); //cant get this to appear otherwise for some reason
		
		highscoresPanel.add(highscoresBack);
		highscoresPanel.add(scorePanel);
		//highscoresPanel.add(code);
		
		contentPane.add(highscoresPanel, SCORES_MENU);
		
		JPanel deadPanel = new JPanel();
		deadPanel.setLayout(new OverlayLayout(deadPanel));
		ImagePanel deadBack = new ImagePanel("MenuandUI/deadScreen.png");
		JPanel scoreEnterPanel = new JPanel();
		
		JTextField scoreEnter = new JTextField();
		scoreEnterPanel.add(scoreEnter);
		
		//deadPanel.add(scoreEnterPanel);
		deadPanel.add(deadBack);
		
		contentPane.add(deadPanel, DEAD_MENU);
		
		window.setContentPane(contentPane);
		window.setVisible(true);
		window.repaint();
		return contentPane;

	}
	
	public void drawTextEntry(){
		
	}
	
	private String generateScores() {
		scores = ScoreList.load();
		String scoreString = "";
		for(int i = 0; i < scores.size(); i++) {
			scoreString = scoreString + scores.getScoreObject(i).toString();
			scoreString = scoreString + "\n";
		}
		return scoreString;
	}
	
	/**
	 * Changes the contentPane of the window to the game pane, repacks the window
	 */
	public void gameStart() {
		window.remove(window.getContentPane());
		gamePanel = new JPanel();
		window.setContentPane(gamePanel);
		window.pack();
		render = new Render((Graphics2D) gamePanel.getGraphics());
	}
	
	/**
	 * Removes the game pane
	 */
	public void menuStart() {
		window.getGraphics().dispose();
	}
}
