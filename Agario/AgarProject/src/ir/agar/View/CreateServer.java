package ir.agar.View;

import ir.agar.Controller.ServerController;
import ir.agar.Controller.ServerController;
import ir.agar.Game;

import javax.swing.*;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateServer extends JPanel {
	private JTextField mapWidth;
	private JTextField mapHeight;
	private JTextField serverNameField;
	private JTextField serverPortField;
    private JButton createServerButton;

	/**
	 * Create the panel.
	 */
	public CreateServer() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Component verticalGlue = Box.createVerticalGlue();
		add(verticalGlue);

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		Component horizontalGlue = Box.createHorizontalGlue();
		panel.add(horizontalGlue);

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{100, 200};
		gbl_panel_2.rowHeights = new int[]{40, 40, 40, 40, 40, 40, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);

		JLabel label = new JLabel("** Create New Server **");
		label.setFont(new Font("Dialog", Font.BOLD, 20));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.gridwidth = 2;
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel_2.add(label, gbc_label);

		JLabel label_1 = new JLabel("Server Name:");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 1;
		panel_2.add(label_1, gbc_label_1);

		serverNameField = new JTextField("Agar Server");
		serverNameField.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		panel_2.add(serverNameField, gbc_textField);

		JLabel label_2 = new JLabel("Map Width:");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 2;
		panel_2.add(label_2, gbc_label_2);

		mapWidth = new JTextField("1000");
		mapWidth.setColumns(10);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.BOTH;
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		panel_2.add(mapWidth, gbc_textField_1);

		JLabel label_3 = new JLabel("Map Height:");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.EAST;
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 3;
		panel_2.add(label_3, gbc_label_3);

		mapHeight = new JTextField("600");
		mapHeight.setColumns(10);
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.fill = GridBagConstraints.BOTH;
		gbc_textField_2.anchor = GridBagConstraints.NORTH;
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 3;
		panel_2.add(mapHeight, gbc_textField_2);

		JLabel label_4 = new JLabel("Port:");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.anchor = GridBagConstraints.EAST;
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 4;
		panel_2.add(label_4, gbc_label_4);

        serverPortField = new JTextField("8585");
        GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.BOTH;
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 4;
		panel_2.add(serverPortField, gbc_comboBox);




        createServerButton = new JButton("Create Server");
        GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.fill = GridBagConstraints.BOTH;
		gbc_button.gridwidth = 2;
		gbc_button.gridx = 0;
		gbc_button.gridy = 5;
		panel_2.add(createServerButton, gbc_button);

        createServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
				if (ServerController.getCurrentInstance() == null) {
					createServerButton.setText("Attach class file");
					ServerController serverController = ServerController.create(serverNameField.getText(), serverPortField.getText(), mapWidth.getText(), mapHeight.getText());
					serverController.init();
				}else{
                    AttachDialog attachDialog = new AttachDialog();
                    attachDialog.setTitle("Attach file");
                    attachDialog.setVisible(true);
                }
            }
        });

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		panel.add(horizontalGlue_1);

		Component verticalGlue_1 = Box.createVerticalGlue();
		add(verticalGlue_1);

	}

    public JButton getCreateServerButton() {
        return createServerButton;
    }
}
