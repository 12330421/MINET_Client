package com.minet;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class P2pChatView {

	private JFrame p2pFrame;
	private JTextArea receiveMsgTextArea;
	private JTextArea sendMsgTextArea;

	/**
	 * Create the application.
	 */
	public P2pChatView(String title) {
		initialize(title);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String title) {

		p2pFrame = new JFrame();
		p2pFrame.setTitle(title);
		p2pFrame.setSize(500, 500);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, 0.0,
				Double.MIN_VALUE };
		p2pFrame.getContentPane().setLayout(gridBagLayout);

		JScrollPane receiveMsgScrollPane = new JScrollPane();
		GridBagConstraints gbc_receiveMsgScrollPane = new GridBagConstraints();
		gbc_receiveMsgScrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_receiveMsgScrollPane.fill = GridBagConstraints.BOTH;
		gbc_receiveMsgScrollPane.gridx = 0;
		gbc_receiveMsgScrollPane.gridy = 0;
		p2pFrame.getContentPane().add(receiveMsgScrollPane,
				gbc_receiveMsgScrollPane);

		receiveMsgTextArea = new JTextArea();
		receiveMsgTextArea.setRows(4);
		receiveMsgTextArea.setEditable(false);
		receiveMsgScrollPane.setViewportView(receiveMsgTextArea);

		JPanel userInfoPanel = new JPanel();
		receiveMsgScrollPane.setColumnHeaderView(userInfoPanel);

		JLabel userNameLabel = new JLabel(title);
		userInfoPanel.add(userNameLabel);

		JScrollPane sendMsgScrollPane = new JScrollPane();
		GridBagConstraints gbc_sendMsgScrollPane = new GridBagConstraints();
		gbc_sendMsgScrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_sendMsgScrollPane.fill = GridBagConstraints.BOTH;
		gbc_sendMsgScrollPane.gridx = 0;
		gbc_sendMsgScrollPane.gridy = 2;
		p2pFrame.getContentPane().add(sendMsgScrollPane, gbc_sendMsgScrollPane);

		sendMsgTextArea = new JTextArea();
		sendMsgScrollPane.setViewportView(sendMsgTextArea);

		JButton sendButton = new JButton("发送");
		GridBagConstraints gbc_sendButton = new GridBagConstraints();
		gbc_sendButton.gridx = 0;
		gbc_sendButton.gridy = 3;
		p2pFrame.getContentPane().add(sendButton, gbc_sendButton);
		p2pFrame.setVisible(true);
		
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub 
				{
					Constants.tempID = getP2pViewID();
					System.out.println("view id="+getP2pViewID());
					Constants.message = getMessage();
					Constants.executor.execute(new ClientThread("p2pSendMeg"));
					addMessage(Constants.userName + " : " + Constants.message);
				}
			}
		});
		
	}
	
	public String getMessage() {
		String temp = sendMsgTextArea.getText();
		sendMsgTextArea.setText("");
		return temp;
	}
	
	public void addMessage(String str) {
		receiveMsgTextArea.setText(receiveMsgTextArea.getText() + "\n" +  str);
	}

	public String getP2pViewID() {
		return p2pFrame.getTitle();
	}
}
