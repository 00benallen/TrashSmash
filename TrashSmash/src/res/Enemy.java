package res;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.imageio.ImageIO;

import main.GraphicsMain;
import main.Main;
import main.Render;

public class Enemy implements Drawable{
	
	private int x, y, health, typeCode, trashType, velocity, moveCnt = 0;
	public final int APPLE_CORE = 0, PEN = 1, COFFEE_CUP = 2, BULB = 3, EGG_CARTON = 4, NEWSPAPER = 5, TEABAG = 6, WATER_BOTTLE = 7, WINE_BOTTLE = 8, PEAR = 9, TP = 10, MILK = 11, TRUCK = 12, CHERRY = 13, JUICE = 14, YOGURT = 15, CHIPCAN = 16, COLA = 17, CHIPBAG = 18, DVD = 19;
	public final int GARBAGE = 0, RECYCLING = 1, COMPOST = 2;
	public int explosionCounter = 0;
	private final static int width = 128, height = 128;
	private BufferedImage image;
	private MovePattern movePat;
	private Rectangle2D boundBox;
	private long aniChange = 0, frameLength = 300;
	private int frame = 0;
	private ReentrantReadWriteLock lck;
	private boolean isExplode = false, isDead = false;;
	
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
		this.boundBox = new Rectangle2D.Double(this.x, this.y, Bullet.width, Bullet.height);
	}

	private String getFileString() {
		String fileString = "";
		switch(typeCode) {
			case APPLE_CORE:	fileString = "Assets/Invaders/applecore.png";
								this.setTrashType(COMPOST);
					break;
			case PEN:	fileString = "Assets/Invaders/pen.png";
								this.setTrashType(GARBAGE);
					break;
			case COFFEE_CUP:	fileString = "Assets/Invaders/coffeecup.png";
								this.setTrashType(RECYCLING);
					break;
			case BULB:	fileString = "Assets/Invaders/lightbulb.png";
								this.setTrashType(GARBAGE);
					break;
			case EGG_CARTON:	fileString = "Assets/Invaders/eggcarton.png";
								this.setTrashType(RECYCLING);
					break;
			case NEWSPAPER:	fileString = "Assets/Invaders/newspaper.png";
								this.setTrashType(RECYCLING);
					break;
			case TEABAG:	fileString = "Assets/Invaders/teabag.png";
								this.setTrashType(COMPOST);
					break;
			case WATER_BOTTLE:	fileString = "Assets/Invaders/waterbottle.png";
								this.setTrashType(RECYCLING);
					break;
			case WINE_BOTTLE:	fileString = "Assets/Invaders/winebottle.png";
								this.setTrashType(RECYCLING);
					break;
			case PEAR:	fileString = "Assets/Invaders/pear.png";
								this.setTrashType(COMPOST);
				break;
			case TP:	fileString = "Assets/Invaders/toiletPaper.png";
								this.setTrashType(RECYCLING);
				break;
			case MILK:	fileString = "Assets/Invaders/milkbag.png";
								this.setTrashType(GARBAGE);
				break;
			case TRUCK:	fileString = "Assets/Invaders/truck.png";
								this.setTrashType(GARBAGE);
				break;
			case CHERRY:	fileString = "Assets/Invaders/cherry.png";
								this.setTrashType(COMPOST);
				break;
			case YOGURT:	fileString = "Assets/Invaders/yogurtcup.png";
								this.setTrashType(RECYCLING);
				break;
			case CHIPCAN:	fileString = "Assets/Invaders/pringlesCan.png";
								this.setTrashType(RECYCLING);
								break;
			case COLA:	fileString = "Assets/Invaders/colaCan.png";
								this.setTrashType(RECYCLING);
								break;
			case CHIPBAG:	fileString = "Assets/Invaders/chipbag.png";
								this.setTrashType(GARBAGE);
								break;
			case DVD:	fileString = "Assets/Invaders/dvd.png";
								this.setTrashType(GARBAGE);
								break;
			case JUICE:	fileString = "Assets/Invaders/juicebox.png";
								this.setTrashType(RECYCLING);
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

	public Rectangle2D getBoundBox() {
		return boundBox;
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
	
	public boolean checkCollision(Ship ship) {
		if(this.x + this.getWidth()/2 >= ship.getX() && this.x <= ship.getX() + Ship.getWidth()/2) {
			if(this.y + this.getHeight()/2 >= ship.getY() && this.y <= ship.getY() + Ship.getHeight()/2) {
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

	public void explode() {
		lck = Main.gMain.render.lck;
		isExplode = true;
		lck.readLock().lock();
		if(aniChange == 0) {
			aniChange = System.currentTimeMillis();
		}
		this.image = Render.explosion[0];
		
		if(System.currentTimeMillis() - aniChange > frameLength) {
			aniChange = System.currentTimeMillis();
			frame++;
			if(frame == 10) {
				this.setDead(true);
				lck.readLock().unlock();
				return;
			}
			this.image = Render.explosion[frame];	
		}
		if(explosionCounter < 10){
			explosionCounter++;
			explode();
		}
		lck.readLock().unlock();
	}

	public boolean isExplode() {
		return isExplode;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
}
