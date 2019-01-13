import java.util.*;
import java.awt.*;
import java.io.IOException;

import javax.swing.*;

public class MainFrame extends JFrame {
	private JPanel main;
	private GamePanel gamePanel;
	private TitlePanel titlePanel;
	static ResultPanel resultPanel;
	private ChooserOfSpeedPanel speedChooserPanel;
	private CardLayout card;

	public MainFrame() {
		main = new JPanel();
		add(main);
		card = new CardLayout();
		main.setLayout(card);

		titlePanel = new TitlePanel(this, card, "Speed");
		speedChooserPanel = new ChooserOfSpeedPanel(this, card, "Game");
		try {
			gamePanel = new GamePanel(this, card, "Result");
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		try {
			resultPanel = new ResultPanel(this, card, "Speed");
		} catch (IOException e) {
			e.printStackTrace();
		}
		main.add(titlePanel,"Title");
		main.add(speedChooserPanel, "Speed");
		main.add(gamePanel, "Game");
		main.add(resultPanel, "Result");
		
		setFocusOnTitlePanel();
	}

	public Container getMainPanel() {
		return main;
	}
   
	public void setFocusOnTitlePanel() {
		titlePanel.requestFocus();

	}

	public void setFocusOnChooserOfSpeedPanel() {
		speedChooserPanel.requestFocus();

	}

	public void setFocusOnResultPanel() {
		resultPanel.requestFocus();

	}

	public void setFocusOnGamePanel() {
		gamePanel.requestFocus();

	}



	public static void main(String[] args) {
		MainFrame frame = new MainFrame();

		frame.setTitle("Change Panels");
		frame.setSize(700, 700);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
