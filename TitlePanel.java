import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class TitlePanel extends JPanel {
	private MainFrame mainFrame;
	private CardLayout cardLayout;
	private String nextPanel;
	private JLabel snakeLabel = new JLabel("SUPER SNAKE",SwingConstants.CENTER);
	private JLabel enterCom = new JLabel("Please press ENTER",SwingConstants.CENTER);

	TitlePanel(MainFrame mainFrame, CardLayout cardLayout, String nextPanel) {
		setLayout(new BorderLayout());
		snakeLabel.setFont(new Font("Monotype Corsiva",1,40));
		snakeLabel.setForeground(Color.RED);
		
		enterCom.setForeground(Color.CYAN);
		
		add(snakeLabel,BorderLayout.NORTH);
		add(enterCom,BorderLayout.SOUTH);
		
		this.mainFrame = mainFrame;
		this.cardLayout = cardLayout;
		this.nextPanel = nextPanel;
		setFocusable(true);
		addKeyListener(new PanelKeyEvent());
		setBackground(Color.BLACK);

	}

	class PanelKeyEvent extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (KeyEvent.VK_ENTER == e.getKeyCode()) {
				cardLayout.show(mainFrame.getMainPanel(), nextPanel);
				mainFrame.setFocusOnChooserOfSpeedPanel();
			}

		}

	}
}
