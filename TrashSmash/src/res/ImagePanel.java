package res;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Class defines a JPanel with a custom background image
 * @author Tristan Monger
 *
 */
public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 8886187983405191952L;
	private BufferedImage img;

	public ImagePanel(String imgLink) {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setVisible(true);

		try {
			this.img = ImageIO.read(getClass().getClassLoader().getResource(imgLink));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setSize(size);
		setLayout(null);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}
}
