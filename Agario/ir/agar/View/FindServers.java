package ir.agar.View;

import ir.agar.Game;
import ir.agar.Controller.ClientController;
import ir.agar.Game;

import javax.swing.*;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FindServers extends JPanel {
	private JTextField txtIpfrom;
	private JTextField txtIpTo;
	private JTextField txtPort;
	private JList serverToConnect;
	private JButton btnConnect;

	private ArrayList<String> serversToConnect = new ArrayList<>();

	/**
	 * Create the panel.
	 */
	public FindServers() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		Component horizontalGlue = Box.createHorizontalGlue();
		add(horizontalGlue);

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		Component verticalGlue = Box.createVerticalGlue();
		panel.add(verticalGlue);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));

		JPanel panel_1 = new JPanel();
		panel_2.add(panel_1);
		panel_1.setMaximumSize(new Dimension(600, 600));
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] {40, 350};
		gbl_panel_1.rowHeights = new int[] {0, 40, 40, 40, 40, 130, 40};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panel_1.setLayout(gbl_panel_1);

		JLabel lblFindNewServer = new JLabel("** Find New Server **");
		lblFindNewServer.setFont(new Font("Dialog", Font.BOLD, 20));
		GridBagConstraints gbc_lblFindNewServer = new GridBagConstraints();
		gbc_lblFindNewServer.gridwidth = 2;
		gbc_lblFindNewServer.insets = new Insets(0, 0, 30, 0);
		gbc_lblFindNewServer.gridx = 0;
		gbc_lblFindNewServer.gridy = 0;
		panel_1.add(lblFindNewServer, gbc_lblFindNewServer);

		JLabel lblIpFrom = new JLabel("IP From:");
		GridBagConstraints gbc_lblIpFrom = new GridBagConstraints();
		gbc_lblIpFrom.insets = new Insets(0, 0, 5, 5);
		gbc_lblIpFrom.anchor = GridBagConstraints.EAST;
		gbc_lblIpFrom.gridx = 0;
		gbc_lblIpFrom.gridy = 1;
		panel_1.add(lblIpFrom, gbc_lblIpFrom);

		txtIpfrom = new JTextField();
		GridBagConstraints gbc_txtIpfrom = new GridBagConstraints();
		gbc_txtIpfrom.insets = new Insets(0, 0, 5, 0);
		gbc_txtIpfrom.fill = GridBagConstraints.BOTH;
		gbc_txtIpfrom.gridx = 1;
		gbc_txtIpfrom.gridy = 1;

		txtIpfrom.setText("127.0.0.1");
		panel_1.add(txtIpfrom, gbc_txtIpfrom);
		txtIpfrom.setColumns(10);

		JLabel lblTo = new JLabel("To:");
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTo.anchor = GridBagConstraints.EAST;
		gbc_lblTo.gridx = 0;
		gbc_lblTo.gridy = 2;
		panel_1.add(lblTo, gbc_lblTo);

		txtIpTo = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 2;
		panel_1.add(txtIpTo, gbc_textField);
		txtIpTo.setColumns(10);
		txtIpTo.setText("127.0.0.1");

		txtPort = new JTextField();
		GridBagConstraints gbc_txtPort = new GridBagConstraints();
		gbc_txtPort.insets = new Insets(0, 0, 5, 0);
		gbc_txtPort.fill = GridBagConstraints.BOTH;
		gbc_txtPort.gridx = 1;
		gbc_txtPort.gridy = 3;
		panel_1.add(txtPort, gbc_txtPort);
		txtPort.setColumns(10);

		txtPort.setText("8585");

		JLabel lblPort = new JLabel("Port:");
		GridBagConstraints gbc_lblPort = new GridBagConstraints();
		gbc_lblPort.anchor = GridBagConstraints.EAST;
		gbc_lblPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblPort.gridx = 0;
		gbc_lblPort.gridy = 3;
		panel_1.add(lblPort, gbc_lblPort);

		JButton btnFindServers = new JButton("Find Servers");
		GridBagConstraints gbc_btnFindServers = new GridBagConstraints();
		gbc_btnFindServers.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnFindServers.insets = new Insets(0, 0, 5, 0);
		gbc_btnFindServers.gridx = 1;
		gbc_btnFindServers.gridy = 4;
		panel_1.add(btnFindServers, gbc_btnFindServers);

		btnFindServers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				btnFindServers.setText("Finding ...");
				btnFindServers.setEnabled(false);

				ClientController.findServers(txtIpfrom.getText(), txtIpTo.getText(), Integer.parseInt(txtPort.getText()), new Runnable() {
					@Override
					public void run() {
						ArrayList<String[]> foundServers = ClientController.getFoundServers();

						for (String[] server: foundServers){
							serversToConnect.add(server[0] + ": *" + server[1] + "* (" + server[5] + ")");
						}

						serverToConnect.setListData(serversToConnect.toArray());
						btnFindServers.setText("Find Servers");
						btnFindServers.setEnabled(true);
					}
				});
			}
		});

		JLabel lblFoundServers = new JLabel("Found Servers:");
		GridBagConstraints gbc_lblFoundServers = new GridBagConstraints();
		gbc_lblFoundServers.insets = new Insets(0, 0, 5, 5);
		gbc_lblFoundServers.gridx = 0;
		gbc_lblFoundServers.gridy = 5;
		panel_1.add(lblFoundServers, gbc_lblFoundServers);


		JScrollPane scrollPane = new JScrollPane();



		serverToConnect = new JList();
		serverToConnect.setPreferredSize(new Dimension(250, 300));
		serverToConnect.setBorder(new LineBorder(Color.LIGHT_GRAY));
		serverToConnect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.insets = new Insets(0, 0, 5, 0);
		gbc_list.gridx = 1;
		gbc_list.gridy = 5;

		panel_1.add(scrollPane, gbc_list);


		scrollPane.setViewportView(serverToConnect);

		serverToConnect.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				if(serverToConnect.getSelectedIndex() == -1)
					btnConnect.setEnabled(false);
				else
					btnConnect.setEnabled(true);
			}
		});




		btnConnect = new JButton("Connect");
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConnect.gridx = 1;
		gbc_btnConnect.gridy = 6;
		panel_1.add(btnConnect, gbc_btnConnect);
		btnConnect.setEnabled(false);

		Component verticalGlue_1 = Box.createVerticalGlue();
		panel.add(verticalGlue_1);

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		add(horizontalGlue_1);

		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				int selectedIndex = serverToConnect.getSelectedIndex();
				if (selectedIndex != -1 || selectedIndex < ClientController.getFoundServers().size()) {
					ClientController.selectServer(ClientController.getFoundServers().get(selectedIndex));
					Game.getLoginOrRegister().updateInfo();
					Game.openPanel("lor");

				}
			}
		});

	}

}
