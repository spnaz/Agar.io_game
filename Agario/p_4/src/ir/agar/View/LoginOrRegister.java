package ir.agar.View;

import ir.agar.Controller.ClientController;
import ir.agar.Game;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginOrRegister extends JPanel {
	private JTextField txtUsername;
	private JPasswordField pwdPassword;
    private JLabel lblServerName;
    private JLabel lblIPAddress;
    private JLabel lblClients;


	/**
	 * Create the panel.
	 */
	public LoginOrRegister() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Component verticalGlue_1 = Box.createVerticalGlue();
		add(verticalGlue_1);

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		Component horizontalGlue = Box.createHorizontalGlue();
		panel.add(horizontalGlue);

		JPanel panel_1 = new JPanel();
		panel_1.setMaximumSize(new Dimension(600, 600));
		panel.add(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] {50, 250};
		gbl_panel_1.rowHeights = new int[] {40, 40, 40, 40, 40, 40, 40, 40};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panel_1.setLayout(gbl_panel_1);

		JLabel lblLoginToServer = new JLabel("Login to Server");
		lblLoginToServer.setFont(new Font("Dialog", Font.BOLD, 20));
		GridBagConstraints gbc_lblLoginToServer = new GridBagConstraints();
		gbc_lblLoginToServer.insets = new Insets(0, 0, 5, 0);
		gbc_lblLoginToServer.gridwidth = 2;
		gbc_lblLoginToServer.gridx = 0;
		gbc_lblLoginToServer.gridy = 0;
		panel_1.add(lblLoginToServer, gbc_lblLoginToServer);

		JLabel lblServerName = new JLabel("Server Name:");
		GridBagConstraints gbc_lblServerName = new GridBagConstraints();
		gbc_lblServerName.insets = new Insets(0, 0, 5, 5);
		gbc_lblServerName.gridx = 0;
		gbc_lblServerName.gridy = 1;
		panel_1.add(lblServerName, gbc_lblServerName);


        this.lblServerName = new JLabel("Localhost");
        GridBagConstraints gbc_lblLocalhost = new GridBagConstraints();
		gbc_lblLocalhost.insets = new Insets(0, 0, 5, 0);
		gbc_lblLocalhost.gridx = 1;
		gbc_lblLocalhost.gridy = 1;
		panel_1.add(this.lblServerName, gbc_lblLocalhost);

		JLabel lblServerAddress = new JLabel("Server Address:");
		GridBagConstraints gbc_lblServerAddress = new GridBagConstraints();
		gbc_lblServerAddress.anchor = GridBagConstraints.EAST;
		gbc_lblServerAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblServerAddress.gridx = 0;
		gbc_lblServerAddress.gridy = 2;
		panel_1.add(lblServerAddress, gbc_lblServerAddress);


        lblIPAddress = new JLabel("192.168.1.1");
        GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 1;
		gbc_label.gridy = 2;
		panel_1.add(lblIPAddress, gbc_label);

		JLabel lblClients = new JLabel("Clients:");
		GridBagConstraints gbc_lblClients = new GridBagConstraints();
		gbc_lblClients.anchor = GridBagConstraints.EAST;
		gbc_lblClients.insets = new Insets(0, 0, 5, 5);
		gbc_lblClients.gridx = 0;
		gbc_lblClients.gridy = 3;
		panel_1.add(lblClients, gbc_lblClients);


        this.lblClients = new JLabel("0");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridx = 1;
		gbc_label_1.gridy = 3;
		panel_1.add(this.lblClients, gbc_label_1);

		JLabel lblUsername = new JLabel("Username:");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 5;
		panel_1.add(lblUsername, gbc_lblUsername);

		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 6;
		panel_1.add(lblPassword, gbc_lblPassword);

		txtUsername = new JTextField();
		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsername.fill = GridBagConstraints.BOTH;
		gbc_txtUsername.gridx = 1;
		gbc_txtUsername.gridy = 5;
		panel_1.add(txtUsername, gbc_txtUsername);
		txtUsername.setColumns(10);

		pwdPassword = new JPasswordField();
		GridBagConstraints gbc_pwdPassword = new GridBagConstraints();
		gbc_pwdPassword.insets = new Insets(0, 0, 5, 0);
		gbc_pwdPassword.fill = GridBagConstraints.BOTH;
		gbc_pwdPassword.gridx = 1;
		gbc_pwdPassword.gridy = 6;
		panel_1.add(pwdPassword, gbc_pwdPassword);

		JButton btnLogin = new JButton("Login");
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.insets = new Insets(0, 0, 0, 5);
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 7;
		panel_1.add(btnLogin, gbc_btnLogin);

		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String user = txtUsername.getText();
                String pass = new String(pwdPassword.getPassword());

                new Thread(new Runnable() {
					@Override
					public void run() {
                        ClientController cc = ClientController.getCurrentInstance();

                        if (cc.checkLogin(user, pass)){
							cc.setUserAndPass(user, pass);
							cc.connectServer();
							Game.start();
                        }

                    }
				}).start();
			}
		});

		JButton btnRegister = new JButton("Register");
		GridBagConstraints gbc_btnRegister = new GridBagConstraints();
		gbc_btnRegister.gridx = 1;
		gbc_btnRegister.gridy = 7;
		panel_1.add(btnRegister, gbc_btnRegister);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Game.openPanel("register");
            }
        });

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		panel.add(horizontalGlue_1);

		Component verticalGlue = Box.createVerticalGlue();
		add(verticalGlue);

	}

	public void updateInfo(){
        ClientController currentInstance = ClientController.getCurrentInstance();
        lblServerName.setText(currentInstance.getServerName());
        lblIPAddress.setText(currentInstance.getIp() + ":" + currentInstance.getPort());
        lblClients.setText(currentInstance.getClients()+"");
        revalidate();
        repaint();
    }

}
