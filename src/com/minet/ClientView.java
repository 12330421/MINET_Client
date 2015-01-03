package com.minet;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClientView {

	private JFrame clientFrame = null;
	private static JTextArea receiveMsgTextArea;
	private static JTextArea sendMsgTextArea;
	private Vector<String> onlineUser;
	private JList<String> onlineUserJList;
	private JScrollPane onlineUserListScrollPane;
	MouseListener mouseListener;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientView window = new ClientView();
					window.clientFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientView() {
		initialize();
		Constants.p2pChatViewList = new ArrayList<>();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		clientFrame = new JFrame();
		clientFrame.setTitle(Constants.getUserName());
		clientFrame.setSize(600, 600);
		clientFrame.setResizable(false);
		clientFrame.getContentPane().setLayout(null);

		JScrollPane receiveMsgScrollPane = new JScrollPane();
		receiveMsgScrollPane.setBounds(10, 22, 348, 298);
		clientFrame.getContentPane().add(receiveMsgScrollPane);

		receiveMsgTextArea = new JTextArea("消息:\n");
		receiveMsgTextArea.setEditable(false);
		receiveMsgScrollPane.setViewportView(receiveMsgTextArea);
		receiveMsgTextArea.setLineWrap(true);

		JScrollPane sendScrollPane = new JScrollPane();
		sendScrollPane.setBounds(10, 364, 348, 83);
		clientFrame.getContentPane().add(sendScrollPane);

		sendMsgTextArea = new JTextArea();
		sendScrollPane.setViewportView(sendMsgTextArea);
		sendMsgTextArea.setLineWrap(true);

		JLabel label = new JLabel("在线用户");
		label.setBounds(380, 22, 104, 13);
		clientFrame.getContentPane().add(label);

		onlineUserListScrollPane = new JScrollPane();
		onlineUserListScrollPane.setBounds(380, 37, 104, 283);
		clientFrame.getContentPane().add(onlineUserListScrollPane);
		

		JButton sendMsgButton = new JButton("发送");
		sendMsgButton.setBounds(380, 364, 93, 63);
		clientFrame.getContentPane().add(sendMsgButton);

		clientFrame.setVisible(true);

		sendMsgButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Constants.executor.execute(new ClientThread("broadcast"));
			}
		});

		mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// int index =
					// onlineUserJList.locationToIndex(e.getPoint());
					//System.out.println(onlineUserJList.getSelectedValue());
					String info = onlineUserJList.getSelectedValue();
					String a[] = info.split(":");
					for (User u:Constants.onlineUserList) {
						if (u.getId() == a[1] && u.getSocket() != null)
							return;
					}
					Constants.tempID = a[1];
					Constants.executor.execute(new ClientThread("p2pChat"));
					P2pChatView p2pChatView = new P2pChatView(a[0]);
					Constants.p2pChatViewList.add(p2pChatView);
				}
			}
		};
		updateUserList();

	}
	
	public void updateUserList(){
		this.onlineUser = new Vector<>();
		for (int i = 0; i < Constants.onlineUserList.size(); i++) {
			User user = Constants.onlineUserList.get(i);
			onlineUser.add(Constants.onlineUserList.get(i).getName() + ":"
					+ Constants.onlineUserList.get(i).getId());

		}
	    this.onlineUserJList = new JList<String>(onlineUser);
		this.onlineUserListScrollPane.setViewportView(onlineUserJList);
		onlineUserJList.addMouseListener(mouseListener);
	}

	public static void addMessage(String str) {
		receiveMsgTextArea.setText(receiveMsgTextArea.getText() + "\n" + str);
	}

	public static String getMessage() {
		String temp = sendMsgTextArea.getText();
		sendMsgTextArea.setText("");
		return temp;
	}

}
