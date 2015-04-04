package main;

import java.awt.Dimension;

import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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
	private static JFrame window = new JFrame("Trash Smash");
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
	
	/**
	 * Generates the main menu
	 */
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
	public void createContentPane() {
		playMusic();
		ImagePanel imgPanel = new ImagePanel("Assets/Menu and UI/MenuMockup.png");
		
		//GridBagLayout grid = new GridBagLayout();
		JPanel contentPane = new JPanel();
		//window.setContentPane(contentPane);		
		contentPane.setLayout(new OverlayLayout(contentPane));
		GridBagConstraints c = new GridBagConstraints();

		URL url = GraphicsMain.class.getResource("SpinningEarth.gif");
		ImageIcon imageIcon = new ImageIcon(url);
		JLabel label = new JLabel(imageIcon);
		
		JButton startButton = new JButton();
		JButton quitButton = new JButton();
		
		startButton.setIcon(sbutton);
		quitButton.setIcon(qbutton);
		startButton.setBorder(null);
		quitButton.setBorder(null);
		startButton.setLocation(650, 100);
		quitButton.setLocation(650, 100);
		//c.anchor = GridBagConstraints.LINE_START;
		//c.gridx = 2;
		//c.gridy = 1;
		
		startButton.setLocation(400, 500);
		quitButton.setLocation(500, 600);
		
		startButton.addActionListener(l);
		startButton.setActionCommand("start");
		contentPane.add(startButton);
		contentPane.add(label);

		//c.anchor = GridBagConstraints.CENTER;
		//c.gridx = 2;
		//c.gridy = 3;
		
		quitButton.addActionListener(l);
		quitButton.setActionCommand("quit");
		contentPane.add(quitButton);
		
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
