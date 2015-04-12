package res;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GraphicsMain;

public class Ship implements Drawable{
	private int x, y, health, gunSet, score;
	private static final int width = 128, height = 128, velocity = 3;
	private BufferedImage image;
	private BufferedImage blue;
	private BufferedImage red;
	private BufferedImage green;
	private BufferedImage invin;
	private Rectangle2D boundBox;
	
	public Ship(int x, int y) {
		this.setX(x);
		this.setY(y);
		this.setHealth(3);
		this.setGunSet(0);
		this.setScore(0);
		try {
			this.blue = ImageIO.read(getClass().getClassLoader().getResource("Ships/Blue.png"));
			this.green = ImageIO.read(getClass().getClassLoader().getResource("Ships/Green.png"));
			this.red = ImageIO.read(getClass().getClassLoader().getResource("Ships/Red.png"));
			this.invin = ImageIO.read(getClass().getClassLoader().getResource("Ships/Invinci.png"));
		} catch(IOException e) {
			System.out.println("Cannot find Blue/1.png");
		}
		this.setImage(red);
		this.boundBox = new Rectangle2D.Double(this.x, this.y, Bullet.width, Bullet.height);
	}

	@Override
	public int getX() {
		return x;
	}

	private void setX(int x) {
		this.x = x;
	}

	@Override
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHealth() {
		return health;
	}

	private void setHealth(int health) {
		this.health = health;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getGunSet() {
		return gunSet;
	}

	public void setGunSet(int gunSet) {
		if(gunSet < 0) gunSet = 2;
		gunSet %= 3;
		if(gunSet >= 0 && gunSet < 3){
			this.gunSet = gunSet;
			if(this.gunSet == 0){
				this.setImage(red);
			}
			else if(this.gunSet == 1){
				this.setImage(blue);
			}
			else if(this.gunSet == 2){
				this.setImage(green);
			}
		}
	}
	
	public static int getVelocity() {
		return velocity;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, null, x, y);
	}
	
	public void move(int direction) {
		if(direction == MovePattern.RIGHT && getX()+getWidth()/2+velocity < GraphicsMain.WIDTH) {
			this.x += velocity;
		}
		if(direction == MovePattern.LEFT && getX()-velocity > 0) {
			this.x -= velocity;
		}
		this.boundBox.setRect(this.x, this.y, Bullet.width, Bullet.height);
	}
	
	public void damage() {
		this.health -= 1;
		if(health < 0) {
			health = 0;
		}
	}
	
	public void cycleGun() {
		this.gunSet++;
		if(gunSet == 2) {
			gunSet = 0;
		}
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}
	
	@Override
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public Rectangle2D getBoundBox() {
		return boundBox;
	}
	
	public void heal(int i){
		if(health + i <= 3)
			this.health += i;
	}
	
	public boolean checkCollision(Bullet bullet) {
		if(this.x >= bullet.getX() && this.x <= bullet.getX() + bullet.getWidth()/2) {
			if(this.y >= bullet.getY() && this.y <= bullet.getY() + bullet.getHeight()/2) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public boolean checkCollision(Enemy enemy) {
		if(this.x >= enemy.getX() && this.x <= enemy.getX() + enemy.getWidth()/2) {
			if(this.y >= enemy.getY() && this.y <= enemy.getY() + enemy.getHeight()/2) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
}
