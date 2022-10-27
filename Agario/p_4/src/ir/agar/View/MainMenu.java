package ir.agar.View;

import ir.agar.Game;
import ir.agar.Game;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {

	/**
	 * Create the panel.
	 */
	public MainMenu() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		Component horizontalGlue = Box.createHorizontalGlue();
		add(horizontalGlue);
		
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		Component verticalGlue = Box.createVerticalGlue();
		panel.add(verticalGlue);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 10));
		
		JButton btnNewServer = new JButton("New Server");
		panel_1.add(btnNewServer);
		btnNewServer.setSize(new Dimension(300, 0));

		btnNewServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				Game.openPanel("create");
			}
		});
		
		JButton btnFindServer = new JButton("Find Server");
		panel_1.add(btnFindServer);
		btnFindServer.setSize(new Dimension(300, 30));

		btnFindServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				Game.openPanel("find");
			}
		});
		
		JButton btnExit = new JButton("Exit");
		panel_1.add(btnExit);
		btnExit.setMinimumSize(new Dimension(300, 25));

		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				Game.getGameFrame().dispose();
			}
		});
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		panel.add(verticalGlue_1);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		add(horizontalGlue_1);

	}
}
