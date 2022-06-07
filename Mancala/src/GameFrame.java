/**
* CS 151 Team Project (Mancala Game)
* @author 
* @version 1.0 10/16/21
*/
import java.awt.*;
//import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.imageio.ImageIO;
//import java.awt.geom.Rectangle2D;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A GameFrame class 
 */
public class GameFrame extends JFrame implements ChangeListener, MouseListener {
	public static final int DEFAULT_WIDTH = 1100;
	public static final int DEFAULT_HEIGHT = 750;
	
	GameModel dataModel;
	int[] stoneNumX, stoneX, pressX;
	int aNumY, aStoneY, aPressY;
	int bNumY, bStoneY, bPressY;
	int wid, len;
	Font countf = new Font("Helvetica",Font.BOLD,18);
	Font turnf = new Font("Helvetica",Font.BOLD,22);

	private Board board;
	private MenuFrame subWindow;
	
	private JPanel btnPanel;
	private JButton btnUndo;
	private JButton btnRule;
	private JTextArea gameRule;
	private JButton backButton;

	/**
	 * Constructs the game frame
	 * @param model -
	 * precondition: 
	 * postcondition: 
	 */
	public GameFrame(GameModel model) {
		// This is to set up sub window (Menu) object => This will create the second window
		// "this" is a reference to the main window (game frame) which will be used by sub window later
		subWindow = new MenuFrame(this);
		

		dataModel = model;

		board = new BoardA();
		stoneNumX = new int[] {230, 350, 470, 590, 710, 830}; //pits_width = 90, stone_width = 30
		aNumY = 425;
		bNumY = 360;
		stoneX = new int[] {160, 280, 400, 520, 640, 760}; //60 
		aStoneY = 430; // 430-545
		bStoneY = 130; // 130-245
		pressX = new int[] {190, 310, 430, 550, 670, 790};  
		aPressY = 460;
		bPressY = 160;
		wid = 95;
		len = 150;

		setLayout(new BorderLayout());
		// top
		btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.LINE_AXIS));
		
		add(btnPanel, BorderLayout.NORTH);
		
		btnUndo = new JButton("Undo");
		btnUndo.setPreferredSize(new Dimension(500, 30));
		btnUndo.addActionListener(event -> {
			model.undo();
		});
		
		btnRule = new JButton("Rule");
		btnUndo.setPreferredSize(new Dimension(100, 30));
		btnRule.addActionListener(event -> {
			showRule();
		});
		
		btnPanel.add(Box.createRigidArea(new Dimension(480, 0)));
		btnPanel.add(btnUndo);
		btnPanel.add(Box.createHorizontalGlue());
		btnPanel.add(btnRule);
		
		// center
		add((JPanel)board, BorderLayout.CENTER);
		addMouseListener(this);

		// bottom
		backButton = new JButton("Back");
		backButton.addActionListener(event -> {
			setVisible(false);
			subWindow.setVisible(true);
		});
		add(backButton, BorderLayout.SOUTH);

		setTitle("Mancala Game");
		setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(false);
	}
	
	/**
	 * Change to the board style 1
	 * precondition: 
	 * postcondition: 
	 */
	public void changeBoardA() {
		board = new BoardA();
	}
	
	/**
	 * Change to the board style 2
	 * precondition: 
	 * postcondition: 
	 */
	public void changeBoardB() {
		board = new BoardB();
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		char turn = dataModel.getTurn();
		int hitNum = -1;
		
		if (turn == 'A') {
			for (int i=0; i<6; i++) {
				if ((pressX[i]<=x && x<=pressX[i]+wid) && (aPressY<=y && y<=aPressY+len)) {
					hitNum = i;
					break;
				}
			}
		}
		else {
			for (int i=0; i<6; i++) {
				if ((pressX[i]<=x && x<=pressX[i]+wid) && (bPressY<=y && y<=bPressY+len)) {
					hitNum = 12-i;
					break;
				}
			}
		}

		if (hitNum == -1)
			return ;
		
		dataModel.move(hitNum);

		if (dataModel.isOver()) {
			// Font Size
			JPanel resultPanel = new JPanel();
			resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));
			JLabel resultLabel = new JLabel(dataModel.gameOver());
			resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
			resultLabel.setAlignmentX(Label.CENTER_ALIGNMENT);
			JLabel quesLabel = new JLabel("\nWould you like to play again?\n\n");
			quesLabel.setFont(new Font("Arial", Font.BOLD, 14));
			quesLabel.setAlignmentX(Label.CENTER_ALIGNMENT);
			
			resultPanel.add(resultLabel);
			resultPanel.add(Box.createRigidArea(new Dimension(0, 15)));
			resultPanel.add(quesLabel);
			resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
 
			int n = JOptionPane.showConfirmDialog(
 	                null, resultPanel,
 	                "Game Result",
 	                JOptionPane.YES_NO_OPTION);

 	        if (n == JOptionPane.YES_OPTION) {
 	        	this.setVisible(false);
 	          	subWindow.setVisible(true);
 	        } else if (n == JOptionPane.NO_OPTION) {
 	            dispose();
 	        } else {
 	            dispose();
 	        }
 		}
	}
	
	/**
	 * Show the game rule
	 * precondition: 
	 * postcondition: 
	 */
	public void showRule() {
		gameRule = new JTextArea("* The board consists of two rows of pits, each. Three pieces of stones are placed in each of the 12 holes. \n" +
				"* Each player has a large store called Mancala to the right side of the board. \n" +
				"* One player starts the game by picking up all of the stones in any one of his own pits. \n" +
				"* Moving counter-clock wise, the player places one in each pit starting with the next pit until the stones run out. \n" +
				"* If you run into your own Mancala, place one stone in it. If there are more stones to go past your own Mancala, continue placing them into the opponent's pits. \n" +
				"* However, skip your opponent's Mancala. If the last stone you drop is your own Mancala, you get a free turn .\n" +
				"* If the last stone you drop is in an empty pit on your side, you get to take that stone and all of your opponents stones that are in the opposite pit. \n" +
				"* Place all captured stones in your own Mancala. The game ends when all six pits on one side of the Mancala board are empty.\n" +
				"* The player who still has stones on his side of the board when the game ends captures all of those pieces and place them in his Mancala.\n" +
				"* The player who has the most stones in his Mancala wins.\n" +
				"\n" +
				"* Undo Button: \n" +
				" + Before the other player takes a turn, the current player can undo what he has just selected. \n" +
				"    The state of the board is going back to the state before the player makes a selection of a pit. The player is not allowed to make multiple undos in a row.\n" +
				" + The player is allowed to undo again after making a choice. The player can make undo at most 3 times at their turn.");
		Font rulef = new Font("Helvetica",Font.PLAIN,13);
		gameRule.setFont(rulef);
		gameRule.setEditable(false);
		JScrollPane ruleScroll = new JScrollPane(gameRule);
		
		JOptionPane.showMessageDialog(null, ruleScroll, "Rule",
                JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * 
	 * @param event -
	 * precondition: 
	 * postcondition: 
	 */
	public void stateChanged(ChangeEvent event) {
		repaint();
	}
	
	/**
	 * 
	 * @param g -
	 * precondition: 
	 * postcondition: 
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paint(g);
		
		board.drawBoard(g2);
				
		int[] data = dataModel.getData();
		
		g.setColor(Color.yellow);
		g.setFont(turnf);
		g.drawString("Player  " + String.valueOf(dataModel.getTurn()), 490, 390);
		
		g.setColor(Color.white);
		g.setFont(countf);
		// A
		for (int i=0; i<6; i++) {
			g.drawString(String.valueOf(data[i]), stoneNumX[i], aNumY);
			board.drawStones(g, stoneX[i], aStoneY, data[i], 55, 115);
		}
		g.drawString(String.valueOf(data[6]), 960, 130);
		board.drawStones(g, 880, 115, data[6], 65, 445);
		
		// B
		for (int i=0; i<6; i++) {
			g.drawString(String.valueOf(data[12-i]), stoneNumX[i], bNumY);
			board.drawStones(g, stoneX[i], bStoneY, data[12-i], 55, 115);
		}
		g.drawString(String.valueOf(data[13]), 100, 655);
		board.drawStones(g, 30, 115, data[13], 65, 445);
		
		// undo num
		g.setColor(Color.black);
		if (dataModel.getPreTurn() == 'A')
			g.drawString(String.valueOf(dataModel.getAUndoNum()), 530, 80);
		else
			g.drawString(String.valueOf(dataModel.getBUndoNum()), 530, 80);
	}


	private class MenuFrame extends JFrame  {
		private GameFrame gameWindow;
		private int numberOfStoneToPLay;
		
		private JRadioButton board_style1;
		private JRadioButton board_style2;
		private JRadioButton stone3;
		private JRadioButton stone4;
		private ButtonGroup group;
		private ButtonGroup group2;
		private JPanel stoneQuestion;
		private JLabel questionLabel;
		private JButton confirmButton;

		/**
		 * Constructs the menu frame
		 * @param model -
		 * precondition: 
		 * postcondition: 
		 */
		public MenuFrame (GameFrame gf) {
			super ("Menu Window");
			// Set GameFrame (Game Window to the GameFrame gf coming in
			// This is what connects this subwindow (MenuFrame) to the game window in GUI
			gameWindow = gf;

			setupUI();
		}
		
		/**
		 * Set up the menu window
		 * @param model -
		 * precondition: 
		 * postcondition: 
		 */
		private void setupUI() {
			// Choose style
			JLabel label = new JLabel("Please choose a style that you want to play below");
			JPanel textPanel = new JPanel(new FlowLayout());
			textPanel.add(label);
			this.add(textPanel, BorderLayout.NORTH);

			try {
				Image smallerBoard1 = ImageIO.read(new File("./src/images/board1.png"));
				Image newSmallerBoard1 = smallerBoard1.getScaledInstance(300, 200, Image.SCALE_SMOOTH);
				JLabel image1 = new JLabel(new ImageIcon(newSmallerBoard1));
				image1.addMouseListener(new MouseAdapter(){
			         public void mousePressed(MouseEvent e) {
			        	 board_style1.setSelected(true);
			         }
				});
				board_style1 = new JRadioButton("Style 1", true);
				Image smallerBoard2 = ImageIO.read(new File("./src/images/SimpleBoard1.png"));
				Image newSmallerBoard2 = smallerBoard2.getScaledInstance(300, 200, Image.SCALE_SMOOTH);
				JLabel image2 = new JLabel(new ImageIcon(newSmallerBoard2));
				image2.addMouseListener(new MouseAdapter(){
			         public void mousePressed(MouseEvent e) {
			        	 board_style2.setSelected(true);
			         }
				});
				board_style2 = new JRadioButton("Style 2");
				
				group = new ButtonGroup();
				group.add(board_style1);
			    group.add(board_style2);
				
				JPanel boardStylePanel = new JPanel(new FlowLayout());
				this.add(boardStylePanel, BorderLayout.CENTER);
				
				// add the style to the panel
				boardStylePanel.add(board_style1);
				boardStylePanel.add(image1);
				boardStylePanel.add(board_style2);
				boardStylePanel.add(image2);
				
				this.add(boardStylePanel);
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			// Choose number of stones
			stoneQuestion = new JPanel();
			stoneQuestion.setLayout(new FlowLayout());
			numberOfStoneToPLay = 0;
			questionLabel = new JLabel("Enter how many stone you want to play: ");
			
			stone3 = new JRadioButton("3", true);
			stone4 = new JRadioButton("4");
			
			group2 = new ButtonGroup();
			group2.add(stone3);
			group2.add(stone4);
			
			confirmButton = new JButton("Confirm");
			confirmButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					numberOfStoneToPLay = stone3.isSelected()? 3: 4;
					
					if (board_style1.isSelected())
						gameWindow.changeBoardA();
					else
						gameWindow.changeBoardB();

					setVisible(false);
					gameWindow.dataModel.init(numberOfStoneToPLay);
					gameWindow.setVisible(true);
				}
			});
			
			stoneQuestion.add(questionLabel);
			stoneQuestion.add(stone3);
			stoneQuestion.add(stone4);
			stoneQuestion.add(confirmButton);
			this.add(stoneQuestion, BorderLayout.SOUTH);
			
			
			setTitle("Mancala Game Menu");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setPreferredSize(new Dimension(450, 530));
			pack();
			setVisible(true);
		}
		// End Menu Frame
	}
}


