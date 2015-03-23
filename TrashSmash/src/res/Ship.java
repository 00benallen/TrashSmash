package res;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Ship {
	private int x, y, health, gunSet;
	private BufferedImage image;
	
	public Ship(int x, int y) throws IOException {
		this.setX(x);
		this.setY(y);
		this.setHealth(3);
		this.setGunSet(1);
		this.image = ImageIO.read(new File("Assets/Blue/1.png"));
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getGunSet() {
		return gunSet;
	}

	public void setGunSet(int gunSet) {
		this.gunSet = gunSet;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, null, x, y);
	}

}
