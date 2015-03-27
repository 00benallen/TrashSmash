package res;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet implements Drawable{
	private int x, y, typeCode, trashType, velocity;
	public final int GARBAGE = 0, RECYCLING = 1, COMPOST = 2;
	private final static int width = 128, height = 128;
	private BufferedImage image;
	
	public Bullet(int x, int y, int type){
		setVelocity(3);
		setType(type);
		setX(x);
		setY(y);
		try {
			this.image = ImageIO.read(new File("Assets/Other/Button.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkCollision(Enemy enemy){
		if(/*collision*/ false) return true;
		return false;
	}
	
	public void setType(int type){
		trashType = type;
	}
	
	public void setVelocity(int newV){
		velocity = newV;
	}
	
	public void move(){
		this.y+=velocity;
	}
	
	public int getWidth() {
		return width;
	}

	@Override
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
	
	
	public void draw(Graphics2D g) {
		g.drawImage(image, null, x, y);
	}
	
	@Override
	public int getY() {
		return y;
	}

}
