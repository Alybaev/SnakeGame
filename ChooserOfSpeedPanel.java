import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;

import javax.swing.*;

public class ChooserOfSpeedPanel extends JPanel {
	private MainFrame mainFrame;
	private CardLayout cardLayout;
	private String nextPanel;
	static Counter counter;
	private Font font;
	private int level;
	private JLabel labelChoose = new JLabel("CHOOSE SPEED",SwingConstants.CENTER);
	private JLabel labelInstruct = new JLabel("Arrows: Up-Down, Enter:continue", SwingConstants.CENTER);

	public ChooserOfSpeedPanel(MainFrame mainFrame, CardLayout cardLayout, String nextPanel) {
		setLayout(new BorderLayout());
		this.mainFrame = mainFrame;
		this.cardLayout = cardLayout;
		this.nextPanel = nextPanel;
		counter = new Counter();
		font = new Font(Font.SERIF, Font.BOLD, 144);
		
		labelChoose.setFont((new Font("Monotype Corsiva",1,50)));
		labelChoose.setForeground(Color.RED);
		labelInstruct.setForeground(Color.CYAN);
		
		setLayout(new BorderLayout());
		setBackground(Color.black);
		setFocusable(true);
		add(labelChoose,BorderLayout.NORTH);
		add(labelInstruct,BorderLayout.SOUTH);
       
		
		addKeyListener(new PanelKeyAdapter());
		addKeyListener(new ChooserKeyAdapter(counter));



	}

	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		String msg = Integer.toString(counter.getValue());

		FontRenderContext fontContext = g2.getFontRenderContext();
		Rectangle2D rect = font.getStringBounds(msg, fontContext);
		int x = getWidth() / 2 - rect.getBounds().width / 2;
		int y = getHeight() / 2 - rect.getBounds().height / 2 - (int) rect.getY();

		g2.setFont(font);
		g2.setColor(Color.RED);
		g2.drawString(msg, x, y);
	}

	class ChooserKeyAdapter extends KeyAdapter {
		private Counter counter;

		public ChooserKeyAdapter(Counter counter) {
			this.counter = counter;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				counter.increase();
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				counter.decrease();
				repaint();
			}
		}

	}
	private class PanelKeyAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {


				cardLayout.show(mainFrame.getMainPanel(), nextPanel);
				mainFrame.setFocusOnGamePanel();
			}
		}

	}

}

class Counter {
	public static final int MIN_VALUE = 1;
	public static final int MAX_VALUE = 10;

	private static int value = (MIN_VALUE + MAX_VALUE) / 2;

	void increase() {
		if (value < MAX_VALUE) {
			++value;
		}
	}

	void decrease() {
		if (value > MIN_VALUE) {
			--value;
		}
	}

	static int getValue() {
		return value;
	}
}