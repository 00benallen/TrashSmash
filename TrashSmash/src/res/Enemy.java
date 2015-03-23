package res;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Enemy {
	
	private int x, y, health, typeCode, trashType;
	public final int APPLE_CORE = 0, BATTERY = 1, COFFEE_CUP = 2, EGG = 3, EGG_CARTON = 4, NEWSPAPER = 5, TEABAG = 6, WATER_BOTTLE = 7, WINE_BOTTLE = 8;
	public final int GARBAGE = 0, RECYCLING = 1, COMPOST = 2;
	private BufferedImage image;
	
	public Enemy(int x, int y, int typeCode) throws IOException {
		this.setX(x);
		this.setY(y);
		this.setHealth(1);
		this.image = ImageIO.read(new File(this.getFileString()));
	}

	private String getFileString() {
		String fileString = "";
		switch(typeCode) {
			case 0:	fileString = "applecore.png";
					break;
			case 1:	fileString = "applecore.png";
					break;
			case 2:	fileString = "applecore.png";
					break;
			case 3:	fileString = "applecore.png";
					break;
			case 4:	fileString = "applecore.png";
					break;
			case 5:	fileString = "applecore.png";
					break;
			case 6:	fileString = "applecore.png";
					break;
			case 7:	fileString = "applecore.png";
					break;
			case 8:	fileString = "applecore.png";
					break;
		}
		
		return fileString;
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
	
	public void draw(Graphics2D g) {
		g.drawImage(image, null, x, y);
	}

	public int getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}

	public int getTrashType() {
		return trashType;
	}

	public void setTrashType(int trashType) {
		this.trashType = trashType;
	}

}
