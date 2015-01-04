package com.minet;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constants {
	public static String serverName = "127.0.0.1";
	public static int port = 4700;
	public static int threadLoginFlag = 0;
	public static Socket mainSocket;
	public static ServerSocket p2pListenServerSocket;
	public static Socket groupTalkSocket;
	public static String userName;
	public static ArrayList<P2pChatView> p2pChatViewList;
	public static ArrayList<User> onlineUserList;
	public static ExecutorService executor = Executors.newCachedThreadPool();

	public static Boolean isSent;
	public static ClientView clientView;
	public static String myID;
	public static String tempID;
	public static Socket tempp2pSocket;
	public static P2pChatView tempView;
	public static String message;
	public static int p2pListenPort;

	public static String getMyID() {
		return myID;
	}

	public static String getTempID() {
		return tempID;
	}

	public static Socket getTempp2pSocket() {
		return tempp2pSocket;
	}

	public static String getMessage() {
		return message;
	}

	public static P2pChatView getP2pChatViewById(String ID) {
		for (P2pChatView i : p2pChatViewList) {
			if (i.getP2pViewID().indexOf(ID) != -1) {
				return i;
			}
		}
		return null;
	}

	public static void addP2pChatView(P2pChatView pcv) {
		p2pChatViewList.add(pcv);
	}

	public static String getUserName() {
		return userName;
	}

}
