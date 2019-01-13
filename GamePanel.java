import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel {
	private MainFrame mainFrame;
	private CardLayout card;
	private String nextPanel;
	static Counter counter;
	private static Font fontInfo = new Font(Font.SERIF, Font.BOLD, 32);
	private int score = 0;
	private boolean isStopped = false;
	AnimationPanel animationPanel = new AnimationPanel();

	Random rnd = new Random();
	Model model;
	Timer timer;

	public GamePanel(MainFrame mainFrame, CardLayout cardLayout, String nextPanel)
			throws NumberFormatException, IOException {

		this.mainFrame = mainFrame;
		this.card = cardLayout;
		this.nextPanel = nextPanel;

		setFocusable(true);

		addKeyListener(new KeyListenerEvent());
		addKeyListener(new AnimationPanelKeyAdapter());

		model = new Model(20, 20);

		timer = new Timer(50, new TimerListener());

		setLayout(new BorderLayout());

		add(new InfoPanel(), BorderLayout.SOUTH);
		add(animationPanel, BorderLayout.CENTER);

		timer.start();
	}

	public int getScore() {
		return score;
	}

	private class InfoPanel extends JPanel {

		private JLabel lblRow;
		private JLabel lblCol;

		public InfoPanel() {
			setBackground(Color.BLACK);

			setLayout(new GridLayout(1, 0));
			lblRow = new JLabel(Integer.toString(model.getRow()), SwingConstants.CENTER);
			lblRow.setFont(fontInfo);
			add(lblRow);

			lblCol = new JLabel(Integer.toString(model.getCol()), SwingConstants.CENTER);
			lblCol.setFont(fontInfo);
			add(lblCol);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.green);
			lblRow.setForeground(Color.GREEN );
			lblRow.setText("Score: " + score);
			lblCol.setForeground(Color.RED);
			try {
				lblCol.setText("Record: " + RecordsFile.getRecord());
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		}

	}

	private class AnimationPanel extends JPanel {
		boolean newRecord = false;
		private Color backColor = Color.BLUE;
		private Color lineColor = Color.YELLOW;
		private Color ballColor = Color.RED;
		private Color foodColor = Color.GREEN;
		private int xFood, yFood = 0;
		private final static int defaultLength = 3;
		private int x, y;
		private int xEye, yEye;
		int[] tailX,tailY = {0,0,0};

		ArrayList<Integer> bodyX = new ArrayList<>();
		ArrayList<Integer> bodyY = new ArrayList<>();

		public AnimationPanel() {
			addKeyListener(new AnimationPanelKeyAdapter());
		}

		@Override
		protected void paintComponent(Graphics g) {
			timer.start();
			timer.setDelay(400 / counter.getValue());
			super.paintComponent(g);
			int size = Math.min(getWidth(), getHeight());
			int cellWidthSize = size / model.getCols();
			int cellHeightSize = size / model.getRows();

			int cellSize = Math.min(cellWidthSize, cellHeightSize);
			int leftX = getWidth() / 2 - cellSize * model.getCols() / 2;
			int upperY = getHeight() / 2 - cellSize * model.getRows() / 2;
			drawModel(leftX, upperY, cellSize, g);

			

			x = leftX + cellSize * model.getCol();
			y = upperY + cellSize * model.getRow();
			drawFood(leftX, upperY, cellSize, g);
			g.setColor(ballColor);
			drawBody(x, y, cellSize, g);
			g.setColor(foodColor);
			drawEyes(cellSize, g);
			drawTail(bodyX.get(bodyX.size() - 1), bodyY.get(bodyY.size() - 1), cellSize, g);

			try {
				checkIsLost(leftX, upperY, cellSize);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		

		private void drawFood(int leftX, int upperY, int cellSize, Graphics g) {
			if ((x == xFood && y == yFood) || (xFood == 0 && yFood == 0)) {
				if (xFood == 0 && yFood == 0) {
					for (int i = 0; i < defaultLength; i++) {
						bodyX.add(x);
						bodyY.add(y);
					}
				} else {
					bodyX.add(x);
					bodyY.add(y);
					score++;
				}
				xFood = leftX + cellSize * rnd.nextInt(model.getCols());
				yFood = upperY + cellSize * rnd.nextInt(model.getRows());
				boolean isOnSnake = true;
				while(isOnSnake) {
					boolean sameCoordinate = false;
					for(int i = 0;i < bodyX.size();i++) {
						if(xFood == bodyX.get(i) && yFood == bodyY.get(i)) {
							xFood = leftX + cellSize * rnd.nextInt(model.getCols());
							yFood = upperY + cellSize * rnd.nextInt(model.getRows());
							 sameCoordinate = true;
						}
					}
					if(!sameCoordinate) {
						isOnSnake = false;
					}
				}
				
			}
			g.setColor(Color.GREEN);
			g.fillOval(xFood, yFood, cellSize, cellSize);

		}

		public void checkIsLost(int leftX, int upperY, int cellSize) throws NumberFormatException, IOException {
			for (int i = 3; i < bodyX.size(); i++) {
				if (x == bodyX.get(i) && y == bodyY.get(i)) {
					if (score > RecordsFile.getRecord()) {
						RecordsFile.assignNewRecord(score);
						mainFrame.resultPanel.setResultLabel("FATALITY");
					}else {
						mainFrame.resultPanel.setResultLabel("You lose");
					}
					init(leftX, upperY, cellSize);
					timer.stop();
					card.show(mainFrame.getMainPanel(), nextPanel);
					mainFrame.setFocusOnResultPanel();

				}
			}
		}
		private void drawEyes(int cellSize, Graphics g) {
			int eyeSize = cellSize / 3;
			int distanceX = cellSize / 2;
			int distanceX2 = cellSize / 3;

			if (model.getDir() == model.LEFT) {
				g.fillOval(x + distanceX2, y + eyeSize / 2, eyeSize, eyeSize);
				g.fillOval(x + distanceX2, y + cellSize - eyeSize - 5, eyeSize, eyeSize);
			} else if (model.getDir() == model.RIGHT) {
				g.fillOval(x + distanceX, y + eyeSize / 2, eyeSize, eyeSize);
				g.fillOval(x + distanceX, y + cellSize - eyeSize - 5, eyeSize, eyeSize);
			} else if (model.getDir() == model.UP) {
				g.fillOval(x + eyeSize, y + distanceX2, eyeSize, eyeSize);
				g.fillOval(x + (eyeSize * 2), y + distanceX2, eyeSize, eyeSize);
			} else if (model.getDir() == model.DOWN) {
				g.fillOval(x + eyeSize, y + distanceX, eyeSize, eyeSize);
				g.fillOval(x + (eyeSize * 2), y + distanceX, eyeSize, eyeSize);
			}

			

		}
		public void drawBody(int x, int y, int cellSize, Graphics g) {
			bodyX.set(0, x);
			bodyY.set(0, y);

			for (int i = bodyX.size() - 1; i > 0; i--) {

				bodyX.set(i, bodyX.get(i - 1));
				bodyY.set(i, bodyY.get(i - 1));

				g.fillOval(bodyX.get(i), bodyY.get(i), cellSize, cellSize);

			}

			g.fillOval(x, y, cellSize, cellSize);
		}

		public void drawModel(int leftX, int upperY, int cellSize, Graphics g) {
			for (int i = 0; i < model.getRows(); ++i) {
				for (int j = 0; j < model.getCols(); ++j) {
					int x = leftX + cellSize * j;
					int y = upperY + cellSize * i;
					g.setColor(backColor);
					g.fillRect(x, y, cellSize, cellSize);
					g.setColor(lineColor);
					g.drawRect(x, y, cellSize, cellSize);
				}
			}
		}

		public void drawTail(int lastBallX, int lastBallY, int cellSize, Graphics g) {
			int lastBallCoordinateX = bodyX.get(bodyX.size() - 1);
			int lastBallCoordinateY = bodyY.get(bodyY.size() - 1);

			if (lastBallCoordinateX > bodyX.get(bodyX.size() - 2)
					&& lastBallCoordinateY == bodyY.get(bodyX.size() - 2)) {
				tailX = new int[] { lastBallCoordinateX + cellSize * 2, lastBallCoordinateX + cellSize * 2,
						lastBallCoordinateX + cellSize };
				tailY = new int[] { lastBallCoordinateY, lastBallCoordinateY + cellSize,
						lastBallCoordinateY + cellSize / 2 };

				if (x == lastBallCoordinateX + cellSize && y == lastBallCoordinateY) {
					card.show(mainFrame.getMainPanel(), nextPanel);
					mainFrame.setFocusOnResultPanel();
				}
			} else if (lastBallCoordinateX < bodyX.get(bodyX.size() - 2)
					&& lastBallCoordinateY == bodyY.get(bodyX.size() - 2)) {
				tailX = new int[] { lastBallCoordinateX - cellSize, lastBallCoordinateX - cellSize,
						lastBallCoordinateX };
				tailY = new int[] { lastBallCoordinateY, lastBallCoordinateY + cellSize,
						lastBallCoordinateY + cellSize / 2 };

				if (x == lastBallCoordinateX - cellSize && y == lastBallCoordinateY) {
					card.show(mainFrame.getMainPanel(), nextPanel);
					mainFrame.setFocusOnResultPanel();
				}
			} else if (lastBallCoordinateX == bodyX.get(bodyX.size() - 2)
					&& lastBallCoordinateY < bodyY.get(bodyX.size() - 2)) {
				tailX = new int[] { lastBallCoordinateX, lastBallCoordinateX + cellSize,
						lastBallCoordinateX + cellSize / 2 };
				tailY = new int[] { lastBallCoordinateY - cellSize, lastBallCoordinateY - cellSize,
						lastBallCoordinateY };

				if (x == lastBallCoordinateX && y == lastBallCoordinateY - cellSize) {
					card.show(mainFrame.getMainPanel(), nextPanel);
					mainFrame.setFocusOnResultPanel();
				}
			} else if (lastBallCoordinateX == bodyX.get(bodyX.size() - 2)
					&& lastBallCoordinateY > bodyY.get(bodyX.size() - 2)) {
				tailX = new int[] { lastBallCoordinateX, lastBallCoordinateX + cellSize,
						lastBallCoordinateX + cellSize / 2 };
				tailY = new int[] { lastBallCoordinateY + cellSize * 2, lastBallCoordinateY + cellSize * 2,
						lastBallCoordinateY + cellSize };

				if (x == lastBallCoordinateX && y == lastBallCoordinateY + cellSize) {
					card.show(mainFrame.getMainPanel(), nextPanel);
					mainFrame.setFocusOnResultPanel();
				}
			}
			g.fillPolygon(tailX, tailY, 3);
		}

		public void init(int leftX, int upperY, int cellSize) {
			xFood = yFood = 0;
			score = 0;
			x = leftX + cellSize * model.getCol();
			y = upperY + cellSize * model.getRow();
			bodyX = new ArrayList<Integer>();
			bodyY = new ArrayList<Integer>();

		}
	}

	private class TimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			model.move();
			repaint();
		}

	}

	private class AnimationPanelKeyAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP && model.getDir() != model.DOWN) {
				model.setDir(Model.UP);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT && model.getDir() != model.LEFT) {
				model.setDir(Model.RIGHT);
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN && model.getDir() != model.UP) {
				model.setDir(Model.DOWN);
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT && model.getDir() != model.RIGHT) {
				model.setDir(Model.LEFT);
			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (!isStopped) {
					timer.stop();
					isStopped = true;
				} else {
					timer.start();
					isStopped = false;
				}
			}
		}

	}

	class KeyListenerEvent extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (KeyEvent.VK_ENTER == e.getKeyCode()) {

			}

		}

	}
}
