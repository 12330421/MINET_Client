package com.minet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginView {
	private JTextField userNameField;
	private JFrame loginFrame;
	private JPanel buttonsPanel;
	private JButton loginButton;
	private JButton cancelButton;
	private JPanel userNamePanel;
	private JLabel userNameLabel;
	private JPanel portPanel;
	private JTextField portTextField;
	private JLabel label;

	public static void main(String[] args){
		LoginView loginView = new LoginView();
	}
	/**
	 * Create the application.
	 */
	public LoginView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		loginFrame = new JFrame();
		Container loginContainer = loginFrame.getContentPane();
		loginContainer.setLayout(new GridLayout(3, 1));
		loginFrame.setVisible(true);
		loginFrame.setSize(220, 170);
		loginFrame.setBackground(Color.WHITE);
		loginFrame.setTitle("登录");

		portPanel = new JPanel();
		portPanel.setLayout(new GridLayout(1, 2));
		label = new JLabel("端口号");
		portPanel.add(label);
		portTextField = new JTextField();
		portPanel.add(portTextField);
		portTextField.setColumns(10);
		
		userNamePanel = new JPanel();
		userNamePanel.setLayout(new GridLayout(1,2));
		userNameLabel = new JLabel("用户名");
		userNamePanel.add(userNameLabel);
		final JTextField userNameField = new JTextField();
		userNamePanel.add(userNameField);
		userNameField.setColumns(10);
		
		buttonsPanel = new JPanel();
		loginButton = new JButton("登录");
		buttonsPanel.add(loginButton);
		cancelButton = new JButton("注冊");
		buttonsPanel.add(cancelButton);
		
		loginContainer.add(portPanel);
		loginContainer.add(userNamePanel);
		loginContainer.add(buttonsPanel);
		
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Constants.userName = userNameField.getText();
				Constants.executor.execute(new ClientThread("login"));
				Constants.executor.execute(new ClientThread("p2pListener"));
				Constants.p2pListenPort = Integer.parseInt(portTextField.getText());
				
				while (Constants.threadLoginFlag == 0) {
					System.out.println("I am waiting");
				}

				loginFrame.setVisible(false);

				Constants.clientView = new ClientView();

			}
		});
	}

	public String getUserName() {
		return userNameField.getText();
	}
}