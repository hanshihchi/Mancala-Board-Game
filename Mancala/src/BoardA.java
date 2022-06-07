/**
* CS 151 Team Project (Mancala Game)
* @author 
* @version 1.0 10/16/21
*/
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Mancala Game Board style 1
 */
public class BoardA extends JPanel implements Board {
	/**
	 * Read in the Board style image
	 */
	public BoardA() {
		try {
            image = ImageIO.read(new File("./src/images/board1.png"));
            stone = ImageIO.read(new File("./src/images/stone1.png"));
        }
        catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}

	/**
	 * Draws the board
	 * @param g2 - the graphics context
	 */
	public void drawBoard(Graphics2D g2) {
        g2.drawImage(image, 0, 50, null);
	}

	/**
	 * Draws the stones
	 * @param g - the graphics context
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param num - number of stones
	 * @param rangeX - x coordinate draw range
	 * @param rangeY - y coordinate draw range
	 */
	public void drawStones(Graphics g, int x, int y, int num, int rangeX, int rangeY) {
		for (int i=0; i<num; i++) {
			int wid = 0;
			int len = 0;
			wid = (int)(Math.random()*rangeX);
			len = (int)(Math.random()*rangeY);
			g.drawImage(stone, x+wid, y+len, null);
		}
	}
		
	Image image;
	Image stone;
}
