package res;

import java.awt.image.BufferedImage;

/**
 * Interface defines common methods for a drawable moving object on screen
 * @author Ben Pinhorn
 *
 */
public interface Drawable {
	
	public int getX();
	public int getY();
	public BufferedImage getImage();
}
