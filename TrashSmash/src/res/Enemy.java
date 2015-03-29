package res;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GraphicsMain;

public class Enemy implements Drawable{
	
	private int x, y, health, typeCode, trashType, velocity, moveCnt = 0;
	public final int APPLE_CORE = 0, PEN = 1, COFFEE_CUP = 2, BULB = 3, EGG_CARTON = 4, NEWSPAPER = 5, TEABAG = 6, WATER_BOTTLE = 7, WINE_BOTTLE = 8;
	public final int GARBAGE = 0, RECYCLING = 1, COMPOST = 2;
	private final static int width = 128, height = 128;
	private BufferedImage image;
	private MovePattern movePat;
	
	public Enemy(int x, int y, int typeCode) {
		this.setX(x);
		this.setY(y);
		this.setHealth(1);
		this.velocity = 3;
		this.typeCode = typeCode;
		try {
			this.image = ImageIO.read(new File(this.getFileString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.movePat = new MovePattern(typeCode);
	}

	private String getFileString() {
		String fileString = "";
		switch(typeCode) {
			case APPLE_CORE:	fileString = "Assets/Invaders/applecore.png";
					break;
			case PEN:	fileString = "Assets/Invaders/pen.png";
					break;
			case COFFEE_CUP:	fileString = "Assets/Invaders/coffeecup.png";
					break;
			case BULB:	fileString = "Assets/Invaders/lightbulb.png";
					break;
			case EGG_CARTON:	fileString = "Assets/Invaders/eggcarton.png";
					break;
			case NEWSPAPER:	fileString = "Assets/Invaders/newspaper.png";
					break;
			case TEABAG:	fileString = "Assets/Invaders/teabag.png";
					break;
			case WATER_BOTTLE:	fileString = "Assets/Invaders/waterbottle.png";
					break;
			case WINE_BOTTLE:	fileString = "Assets/Invaders/winebottle.png";
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
	
	public int getVelocity() {
		return this.velocity;
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
	
	public void move() {
		if(movePat.getMoveArray()[moveCnt/8] == MovePattern.STRAIGHT) {
			moveCnt++;
		}
		else if(movePat.getMoveArray()[moveCnt/8] == MovePattern.RIGHT) {
			if(this.x + Enemy.width + this.velocity < GraphicsMain.WIDTH) {
				this.x += velocity;
			}
			moveCnt++;
		}
		else if(movePat.getMoveArray()[moveCnt/8] == MovePattern.LEFT) {
			if(this.x > 0) {
				this.x -= velocity;
			}
			moveCnt++;
		}
		if(moveCnt == 64) {
			moveCnt = 0;
		}
		this.y += 1;
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

}
