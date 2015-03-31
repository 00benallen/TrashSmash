package res;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet implements Drawable{
	private int x, y, typeCode, velocity;
	public final int GARBAGE = 0, RECYCLING = 1, COMPOST = 2;
	public final static int width = 10, height = 10;
	private BufferedImage image;
	private Rectangle2D boundBox;
	
	public Bullet(int x, int y, int type){
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
		this.boundBox = new Rectangle2D.Double(this.x, this.y, Bullet.width, Bullet.height);
	}
	
	public boolean checkCollision(Enemy enemy) {
		if(enemy.getBoundBox().intersects(this.boundBox)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean checkCollision(Ship ship) {
		if(ship.getBoundBox().intersects(this.boundBox)) {
			return true;
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
		this.boundBox.setRect(this.x, this.y, Bullet.width, Bullet.height);
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

	public Rectangle2D getBoundBox() {
		return boundBox;
	}
}
