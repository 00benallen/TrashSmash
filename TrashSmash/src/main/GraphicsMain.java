package main;

import java.awt.Dimension;

import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import java.net.MalformedURLException;
import java.net.URL;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import res.ImagePanel;
import listeners.ButtonListener;
import listeners.KeyboardListener;

/**
 * Main graphics class for Trash Smash, generates window, starts render thread, creates main menu
 * @author Ben Pinhorn
 *
 */
public class GraphicsMain {
	public JFrame window = new JFrame("Trash Smash");
	public final static int WIDTH = 1024;
	public final static int HEIGHT = 768;
	public BasicPlayer player = new BasicPlayer();
	private Graphics2D g;
	private Thread renderThread;
	public Render render;
	private ButtonListener l;
	private KeyboardListener kl;
	ImageIcon sbutton = new ImageIcon("Assets/Menu and UI/stbutton.png");
	ImageIcon qbutton = new ImageIcon("Assets/Menu and UI/qbutton.png");
	
	//graphics objects should not be stored here, for drawing game stuff, go to Render.java
	
	public GraphicsMain(KeyboardListener k) {
		l = new ButtonListener();
		kl = k;
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		window.setResizable(false);
		window.pack();
		window.setFocusable(true);
		window.addKeyListener(kl);
	}
	
	public void init() {
		
	}
	

	/**
	 * Starts render thread 
	 */
	public synchronized void start() {
		renderThread = new Thread(render, "Render Thread");
		renderThread.start();
		try {
			player.stop();
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void playMusic() {
		File battle;
		battle = new File("Assets/Music/Menu.mp3");
		try {
		    player.open(battle);
		    player.play();
		} catch (BasicPlayerException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * Generates the main menu
	 */
	public void createContentPane() {
		playMusic();
		ImagePanel imgPanel = new ImagePanel("Assets/Menu and UI/MenuMockup.png");
		
		//GridBagLayout grid = new GridBagLayout();
		JPanel contentPane = new JPanel();
		JPanel controlPane = new JPanel();
		JPanel animatePane = new JPanel();
		JPanel buttonsPane = new JPanel();
		//window.setContentPane(contentPane);		
		
		contentPane.setLayout(new OverlayLayout(contentPane));
		GridBagConstraints c = new GridBagConstraints();

		URL url = GraphicsMain.class.getResource("SpinningEarth.gif");
		ImageIcon imageIcon = new ImageIcon(url);
		JLabel label = new JLabel(imageIcon);
		
		JButton startButton = new JButton();
		JButton quitButton = new JButton();
		
		startButton.setIcon(sbutton);
		startButton.setBorder(null);
		startButton.addActionListener(l);
		startButton.setActionCommand("start");
		
		
		quitButton.setIcon(qbutton);
		quitButton.setBorder(null);		
		quitButton.addActionListener(l);
		quitButton.setActionCommand("quit");
		
		
		controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.Y_AXIS));
		buttonsPane.setLayout(new BoxLayout(buttonsPane, BoxLayout.X_AXIS));
		
		controlPane.add(Box.createRigidArea(new Dimension(20, 80)));
		controlPane.add(startButton);
		
		controlPane.add(Box.createRigidArea(new Dimension(0, 40)));
		controlPane.add(quitButton);
		
		buttonsPane.add(controlPane);
		buttonsPane.add(Box.createRigidArea(new Dimension(670, 0)));
		animatePane.add(label);
		
		controlPane.setOpaque(false);
		animatePane.setOpaque(false);
		buttonsPane.setOpaque(false);
		
		animatePane.add(Box.createRigidArea(new Dimension(0,800)));
		contentPane.add(buttonsPane);
		contentPane.add(animatePane);
		
		
		
		//more buttons go here
		contentPane.add(imgPanel);
		
		//window.pack();
		window.setContentPane(contentPane);
		window.setVisible(true);

	}
	
	/**
	 * Changes the contentPane of the window to the game pane, repacks the window
	 */
	public void gameStart() {
		window.remove(window.getContentPane());
		JPanel gamePanel = new JPanel();
		window.setContentPane(gamePanel);
		window.pack();
		render = new Render((Graphics2D) window.getGraphics());
	}
}
