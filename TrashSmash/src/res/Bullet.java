package res;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet implements Drawable {
	private int x, y, typeCode, velocity;
	public final int GARBAGE = 0, RECYCLING = 1, COMPOST = 2;
	public final static int width = 10, height = 10;
	private BufferedImage image;
	private boolean isShip;
	
	public Bullet(int x, int y, int type, boolean isShip){
		setVelocity(6);
		setType(type);
		setX(x);
		setY(y);
		try {
			if(this.typeCode == GARBAGE) {
				this.image = ImageIO.read(new File("Assets/Other/redBullet.png"));
			}
			else if(this.typeCode == RECYCLING) {
				this.image = ImageIO.read(new File("Assets/Other/blueBullet.png"));
			}
			else if(this.typeCode == COMPOST) {
				this.image = ImageIO.read(new File("Assets/Other/greenBullet.png"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.isShip = isShip;
	}
	
	public boolean checkCollision(Enemy enemy) {
		if(this.x+this.getWidth()/2 >= enemy.getX() && this.x <= enemy.getX() + enemy.getWidth()/2) {
			if(this.y+this.getHeight()/2 >= enemy.getY() && this.y <= enemy.getY() + enemy.getHeight()/2) {
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
	
	public boolean checkCollision(Ship ship) {
		if(this.x >= ship.getX() && this.x <= ship.getX() + Ship.getWidth()) {
			if(this.y >= ship.getY() && this.y <= ship.getY() + Ship.getHeight()) {
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
	
	public void setType(int typeCode){
		this.typeCode = typeCode;
	}
	
	public void setVelocity(int newV){
		velocity = newV;
	}
	
	public void move(){
		this.y -= velocity;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}
	
	public void setX(int newx){
		x = newx;
	}

	@Override
	public int getX() {
		return x;
	}

	public void setY(int newy){
		y = newy;
	}
	
	@Override
	public int getY() {
		return y;
	}

	public boolean isShip() {
		return isShip;
	}
}
