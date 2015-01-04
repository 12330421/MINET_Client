package com.minet;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.NumberFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginView {
	private JTextField userNameField;
	private JFrame loginFrame;
	
	private JPanel serverIPPanel;
	private JLabel serverIPLabel;
	private JTextField serverIPTextField;
	private JLabel serverIPStatusLabel;
	
	private JPanel buttonsPanel;
	private JButton loginButton;
	
	private JLabel connectStatusLabel;
	private JPanel userNamePanel;
	private JLabel userNameLabel;
	private JLabel userNameStatusLabel;
	
	private JPanel portPanel;
	private JFormattedTextField portTextField;
	private JLabel portStatusLabel;
	
	private JLabel label;
	private Boolean isLoginSuccess;
	
	public static void main(String[] args){
		LoginView loginView = new LoginView();
	}
	/**
	 * Create the application.
	 */
	public LoginView() {
		isLoginSuccess = true; 
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		loginFrame = new JFrame();
		Container loginContainer = loginFrame.getContentPane();
		loginContainer.setLayout(new GridLayout(4, 1));
		loginFrame.setSize(350, 170);
		loginFrame.setBackground(Color.WHITE);
		loginFrame.setTitle("登录");
		loginFrame.setVisible(true);
		
		serverIPPanel = new JPanel();
		portPanel = new JPanel();
		userNamePanel = new JPanel();
		buttonsPanel = new JPanel();
		
		loginContainer.add(serverIPPanel);
		loginContainer.add(portPanel);
		loginContainer.add(userNamePanel);
		loginContainer.add(buttonsPanel);
		
		serverIPPanel.setLayout(new GridLayout(1,3));
		serverIPLabel = new JLabel("服务器IP");
		serverIPPanel.add(serverIPLabel);
		serverIPTextField = new JTextField("localhost");
		serverIPPanel.add(serverIPTextField);
		serverIPStatusLabel = new JLabel("例如：127.0.0.1");
		serverIPPanel.add(serverIPStatusLabel);
		
		portPanel.setLayout(new GridLayout(1, 3));
		label = new JLabel("端口号");
		portPanel.add(label);
		portTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		
		portPanel.add(portTextField);
		portStatusLabel = new JLabel("1024~65535");
		portPanel.add(portStatusLabel);
		
		userNamePanel.setLayout(new GridLayout(1,3));
		userNameLabel = new JLabel("用户名");
		userNamePanel.add(userNameLabel);
		final JTextField userNameField = new JTextField(10);
		userNamePanel.add(userNameField);
		userNameStatusLabel = new JLabel("例如：John");
		userNamePanel.add(userNameStatusLabel);
		
		loginButton = new JButton("登录");
		buttonsPanel.add(loginButton);
		connectStatusLabel = new JLabel("<--点它登录");
		buttonsPanel.add(connectStatusLabel);
		
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Constants.serverName = serverIPTextField.getText();
				Constants.p2pListenPort = Integer.parseInt(portTextField.getValue().toString());
				System.out.println("ServerName: " + Constants.serverName);
				Constants.userName = userNameField.getText();
				Constants.executor.execute(new ClientThread("login"));
				
				connectStatusLabel.setText("正在连接");
				int i = 0;
				
				while (Constants.threadLoginFlag == 0) {
					System.out.println("I am waiting");
					i++;
					
					isLoginSuccess = true;
					
					 if (i>50000) {
						connectStatusLabel.setText("连接失败");
						isLoginSuccess = false;
						break;
					}
				}

				if (isLoginSuccess && Constants.threadLoginFlag != 0) {
					
					System.out.println("----"+Constants.p2pListenPort);
					Constants.executor.execute(new ClientThread("p2pListener"));
					loginFrame.setVisible(false);
					Constants.clientView = new ClientView();
				} else {
					loginFrame.repaint();
				}
				

			}
		});
		
		loginFrame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public String getUserName() {
		return userNameField.getText();
	}
}