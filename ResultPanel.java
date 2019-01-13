import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class ResultPanel extends JPanel {
	private MainFrame mainFrame;
	private CardLayout cardLayout;
	private String nextPanel;
	private JLabel instruction = new JLabel("Press Enter for restart", SwingConstants.CENTER);
	private static JLabel result = new JLabel( "" ,SwingConstants.CENTER);

	public ResultPanel(MainFrame mainFrame, CardLayout cardLayout, String nextPanel) throws IOException {

		
		this.mainFrame = mainFrame;
		this.cardLayout = cardLayout;
		this.nextPanel = nextPanel;
		setLayout(new BorderLayout());
		setFocusable(true);
		setBackground(Color.BLACK);
		instruction.setForeground(Color.GREEN);
		result.setForeground(Color.RED);
		result.setFont(new Font("Monotype Corsiva", 1, 100));

		add(instruction, BorderLayout.SOUTH);
		add(result, BorderLayout.CENTER);
		addKeyListener(new PanelKeyAdapter());
	}
    public static void setResultLabel(String label) {
    	result.setText(label);
    }
	private class PanelKeyAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				cardLayout.show(mainFrame.getMainPanel(), nextPanel);
				mainFrame.setFocusOnChooserOfSpeedPanel();
			}
		}

	}

}