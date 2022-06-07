/**
* CS 151 Team Project (Mancala Game)
* @author Shih-Chi Han
* @version 1.0 10/16/21
*/


/**
 * 
 */
public class GameTester {
	/**
	 * 
	 * 
	 */
	public static void main(String[] args) {
		final GameModel model = new GameModel();
		GameFrame frame = new GameFrame(model);
		model.attach(frame);
	}
}