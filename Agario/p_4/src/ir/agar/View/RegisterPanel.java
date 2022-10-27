package ir.agar.View;

import ir.agar.Game;
import ir.agar.Controller.ClientController;
import ir.agar.Game;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class RegisterPanel extends JPanel {
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JTextField txtName;
    private Color chosenCirclesColor = Color.GREEN;
    private byte[] circlesImage = null;

	/**
	 * Create the panel.
	 */
	public RegisterPanel() {


		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Component verticalGlue = Box.createVerticalGlue();
		add(verticalGlue);

		JPanel panel_1 = new JPanel();
		add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		Component horizontalGlue = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue);

		JPanel panel = new JPanel();
		panel_1.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.rowHeights = new int[] {0, 40, 40, 40, 40, 40, 40};
		gbl_panel.columnWidths = new int[] {150, 200};
		gbl_panel.columnWeights = new double[]{0.0, 0.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(gbl_panel);

		JLabel lblRegisterOnServer = new JLabel("** Register On ServerController **");
		lblRegisterOnServer.setFont(new Font("Dialog", Font.BOLD, 20));
		GridBagConstraints gbc_lblRegisterOnServer = new GridBagConstraints();
		gbc_lblRegisterOnServer.gridwidth = 2;
		gbc_lblRegisterOnServer.insets = new Insets(0, 0, 20, 5);
		gbc_lblRegisterOnServer.gridx = 0;
		gbc_lblRegisterOnServer.gridy = 0;
		panel.add(lblRegisterOnServer, gbc_lblRegisterOnServer);

		JLabel lblUsername = new JLabel("Username:");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.fill = GridBagConstraints.BOTH;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 1;
		panel.add(lblUsername, gbc_lblUsername);

		txtUsername = new JTextField();
		txtUsername.setMaximumSize(new Dimension(50000, 30));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		panel.add(txtUsername, gbc_textField);
		txtUsername.setColumns(10);

		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.fill = GridBagConstraints.BOTH;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 2;
		panel.add(lblPassword, gbc_lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setMaximumSize(new Dimension(50000, 30));
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.BOTH;
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		panel.add(txtPassword, gbc_textField_1);
		txtPassword.setColumns(10);

		JLabel lblImage = new JLabel("Image");
		GridBagConstraints gbc_lblImage = new GridBagConstraints();
		gbc_lblImage.fill = GridBagConstraints.BOTH;
		gbc_lblImage.insets = new Insets(0, 0, 5, 5);
		gbc_lblImage.gridx = 0;
		gbc_lblImage.gridy = 3;
		panel.add(lblImage, gbc_lblImage);



		JButton btnSelectImage = new JButton("Select Image...");
		btnSelectImage.setMaximumSize(new Dimension(50000, 30));
		GridBagConstraints gbc_btnSelectImage = new GridBagConstraints();
		gbc_btnSelectImage.fill = GridBagConstraints.BOTH;
		gbc_btnSelectImage.insets = new Insets(0, 0, 5, 0);
		gbc_btnSelectImage.gridx = 1;
		gbc_btnSelectImage.gridy = 3;
		panel.add(btnSelectImage, gbc_btnSelectImage);

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images", "jpg", "gif", "PNG");
        chooser.setFileFilter(filter);
        btnSelectImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int returnVal = chooser.showOpenDialog(Game.getGameFrame());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    try (FileInputStream fileInputStream = new FileInputStream(selectedFile);) {

                        try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
                            byte[] buffer = new byte[0xFFFF];

                            for (int len; (len = fileInputStream.read(buffer)) != -1; )
                                os.write(buffer, 0, len);

                            os.flush();

                            circlesImage = os.toByteArray();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        JLabel lblNameOnCircle = new JLabel("Name on Circle:");
		GridBagConstraints gbc_lblNameOnCircle = new GridBagConstraints();
		gbc_lblNameOnCircle.fill = GridBagConstraints.BOTH;
		gbc_lblNameOnCircle.insets = new Insets(0, 0, 5, 5);
		gbc_lblNameOnCircle.gridx = 0;
		gbc_lblNameOnCircle.gridy = 4;
		panel.add(lblNameOnCircle, gbc_lblNameOnCircle);

		txtName = new JTextField();
		txtName.setMaximumSize(new Dimension(50000, 30));
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.fill = GridBagConstraints.BOTH;
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 4;
		panel.add(txtName, gbc_textField_2);
		txtName.setColumns(10);

		JLabel lblCirclesColor = new JLabel("Circles Color:");
		GridBagConstraints gbc_lblCirclesColor = new GridBagConstraints();
		gbc_lblCirclesColor.fill = GridBagConstraints.BOTH;
		gbc_lblCirclesColor.insets = new Insets(0, 0, 5, 5);
		gbc_lblCirclesColor.gridx = 0;
		gbc_lblCirclesColor.gridy = 5;
		panel.add(lblCirclesColor, gbc_lblCirclesColor);

		JButton btnSelectColor = new JButton("Select Color ...");
		btnSelectColor.setMaximumSize(new Dimension(50000, 30));
		GridBagConstraints gbc_btnSelectColor = new GridBagConstraints();
		gbc_btnSelectColor.fill = GridBagConstraints.BOTH;
		gbc_btnSelectColor.insets = new Insets(0, 0, 5, 0);
		gbc_btnSelectColor.gridx = 1;
		gbc_btnSelectColor.gridy = 5;
		panel.add(btnSelectColor, gbc_btnSelectColor);

        JColorChooser jColorChooser = new JColorChooser();
        JDialog colorChooserDialog = JColorChooser.createDialog(Game.getGameFrame(), "Circles Color", true,
                jColorChooser, null, null);

        btnSelectColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                colorChooserDialog.setVisible(true);
                chosenCirclesColor = jColorChooser.getColor();
            }
        });

		JButton btnRegister = new JButton("Register");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridwidth = 2;
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 6;
		panel.add(btnRegister, gbc_btnNewButton);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String name = txtName.getText();
                new Thread(new Runnable() {
					@Override
					public void run() {
						ClientController cc = ClientController.getCurrentInstance();
						cc.connectServerTemp();
						boolean register = cc.register(username, password, name, chosenCirclesColor, circlesImage);
						cc.disconnect();

						if (register){
							cc.connectServer();
							Game.start();
						}
					}
				}).start();

            }
        });

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue_1);

		Component verticalGlue_1 = Box.createVerticalGlue();
		add(verticalGlue_1);

	}
}
