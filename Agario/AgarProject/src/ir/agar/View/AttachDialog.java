package ir.agar.View;

import ir.agar.Game;
import ir.agar.Model.ServerEngine;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class AttachDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtName;
    private File file;
    private byte[] imageByteArray;

    /**
     * Create the dialog.
     */
    public AttachDialog() {
        setUndecorated(false);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        GridBagLayout gbl_contentPanel = new GridBagLayout();
        gbl_contentPanel.columnWidths = new int[]{0, 0, 0};
        gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
        contentPanel.setLayout(gbl_contentPanel);
        {
            JLabel lblAttachClassFile = new JLabel("Attach Class File");
            lblAttachClassFile.setHorizontalAlignment(SwingConstants.CENTER);
            lblAttachClassFile.setHorizontalTextPosition(SwingConstants.CENTER);
            lblAttachClassFile.setFont(new Font("Dialog", Font.BOLD, 18));
            GridBagConstraints gbc_lblAttachClassFile = new GridBagConstraints();
            gbc_lblAttachClassFile.anchor = GridBagConstraints.NORTH;
            gbc_lblAttachClassFile.gridwidth = 2;
            gbc_lblAttachClassFile.fill = GridBagConstraints.HORIZONTAL;
            gbc_lblAttachClassFile.insets = new Insets(0, 0, 5, 5);
            gbc_lblAttachClassFile.gridx = 0;
            gbc_lblAttachClassFile.gridy = 0;
            contentPanel.add(lblAttachClassFile, gbc_lblAttachClassFile);
        }
        {
            JLabel lblClassName = new JLabel("Class name:");
            GridBagConstraints gbc_lblClassName = new GridBagConstraints();
            gbc_lblClassName.insets = new Insets(0, 0, 5, 5);
            gbc_lblClassName.gridx = 0;
            gbc_lblClassName.gridy = 2;
            contentPanel.add(lblClassName, gbc_lblClassName);
        }
        {
            txtName = new JTextField();
            GridBagConstraints gbc_txtName = new GridBagConstraints();
            gbc_txtName.insets = new Insets(0, 0, 5, 0);
            gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
            gbc_txtName.gridx = 1;
            gbc_txtName.gridy = 2;
            contentPanel.add(txtName, gbc_txtName);
            txtName.setColumns(10);
        }
        {
            JLabel lblFile = new JLabel("File:");
            GridBagConstraints gbc_lblFile = new GridBagConstraints();
            gbc_lblFile.insets = new Insets(0, 0, 5, 5);
            gbc_lblFile.gridx = 0;
            gbc_lblFile.gridy = 3;
            contentPanel.add(lblFile, gbc_lblFile);
        }
        {
            JButton btnSelect = new JButton("Select ...");
            GridBagConstraints gbc_btnSelect = new GridBagConstraints();
            gbc_btnSelect.fill = GridBagConstraints.HORIZONTAL;
            gbc_btnSelect.gridx = 1;
            gbc_btnSelect.gridy = 3;
            contentPanel.add(btnSelect, gbc_btnSelect);

            btnSelect.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("ClassFile", "class");
                    chooser.setFileFilter(filter);

                    int returnVal = chooser.showOpenDialog(Game.getGameFrame());
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        file = chooser.getSelectedFile();
                    }
                }
            });
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String className = txtName.getText().trim();

                        if (file == null || className.length() == 0)
                            return;

                        try (FileInputStream fileInputStream = new FileInputStream(file); ByteArrayOutputStream os = new ByteArrayOutputStream();) {
                            byte[] buffer = new byte[0xFFFF];
                            for (int len; (len = fileInputStream.read(buffer)) != -1; )
                                os.write(buffer, 0, len);

                            os.flush();
                            imageByteArray = os.toByteArray();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        ServerEngine.getCurrentInstance().addPowerType(className, imageByteArray);
                        AttachDialog.this.setVisible(false);

                    }
                });
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        setVisible(false);
                    }
                });
            }
        }


    }

}
