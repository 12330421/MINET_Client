package com.minet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jdk.internal.org.objectweb.asm.tree.IntInsnNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientThread extends Thread {
	private ProtocolHelper helper;
	private String action;

	public ClientThread(String action) {
		this.action = action;
		helper = new ProtocolHelper();
	}

	@Override
	public void run() {
		handle();
	}

	public void handle() {
		try {
			if (action == "login") {
				System.out.println(Constants.port);
				Constants.mainSocket = new Socket(Constants.serverName,
						Constants.port);
				System.out.println("123");
				login();
			} else if (action == "p2pListener") {
				p2pListener();
			} else if (action == "broadcast") {
				Constants.groupTalkSocket = new Socket(Constants.serverName,
						Constants.port);
				broadcast();
			} else if (action == "p2pChat") {
				p2pChat();
			} else if (action == "p2pSetUp") {
				p2pSetUp();
			} else if (action == "p2pSendMeg") {
				p2pSendMeg();
			} else if (action == "logout") {
				logout();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void p2pSetUp() throws IOException {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(
				Constants.tempp2pSocket.getInputStream()));
		PrintWriter outputWriter = new PrintWriter(
				Constants.tempp2pSocket.getOutputStream());
		Socket tempSocket = Constants.tempp2pSocket;
		String temp;
		while ((temp = inputReader.readLine()) == null)
			;
		System.out.println(temp);
		helper.setInStr(temp);
		String ID = helper.getData().get("ID").toString();
		User user = getUserByID(ID);
		String Name = user.getName();
		if (user.getSocket() != null) {
			System.out.println("you have already p2p chat with this user");
			Constants.tempp2pSocket.close();
			return;
		}

		if (user == null) {
			System.out.println("no such a user");
			Constants.tempp2pSocket.close();
			return;
		}

		user.setSocket(Constants.tempp2pSocket);
		user.setOutputWriter(outputWriter);
		user.setInputReader(inputReader);

		outputWriter.println(helper.generateResponse());
		outputWriter.flush();

		P2pChatView p2pChatView = new P2pChatView(ID);
		Constants.p2pChatViewList.add(p2pChatView);
		try {
			while (!tempSocket.isClosed()) {
				while ((temp = inputReader.readLine()) == null)
					;
				if (temp.indexOf("session terminated")!= -1) {
					Constants.getP2pChatViewById(ID).closeP2pChatView();
					return;
				}
				Constants.getP2pChatViewById(ID).addMessage(Name + " : " + temp);
			}	
		} catch (Exception e) {
			//Constants.getP2pChatViewById(ID).closeP2pChatView();
		}
		
		
	}

	private User getUserByID(String id) {
		for (int i = 0; i < Constants.onlineUserList.size(); ++i) {
			User user = Constants.onlineUserList.get(i);
			if (user.getId().equals(id))
				return user;
		}
		return null;
	}

	private void p2pListener() throws IOException {

		ServerSocket server = new ServerSocket(Constants.p2pListenPort);
		Constants.p2pListenServerSocket = server;
		while (!server.isClosed()) {
			Constants.tempp2pSocket = server.accept(); 
			Constants.executor.execute(new ClientThread("p2pSetUp"));
		}
	}

	private void p2pSendMeg() {
		User user = getUserByID(Constants.tempID);
		PrintWriter outputWriter = user.getOutputWriter();
		outputWriter.println(Constants.message);
		outputWriter.flush();
		Constants.isSent = true;
	}

	private void p2pChat() throws IOException {
		User user = getUserByID(Constants.tempID);
		String targetId = Constants.tempID;
		String targetName = user.getName();
		if (getUserByID(targetId).getSocket() != null) {
			System.out.println("you have already p2p chat with this user");
			return;
		}
		System.out.println("p2pchat to " + user.getIP() + ":" + user.getPort());
		Socket p2pSocket = new Socket(user.getIP(), user.getPort());
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(
				p2pSocket.getInputStream()));
		PrintWriter outputWriter = new PrintWriter(p2pSocket.getOutputStream());

		helper.setAction("p2pChat");
		Map<String, Object> requestData = new HashMap<String, Object>();
		requestData.put("Name", Constants.getUserName());
		requestData.put("ID", Constants.getMyID());// my own ID
		helper.setRequestData(requestData);
		outputWriter.println(helper.generateOutStr());
		outputWriter.flush();
		String temp;
		while ((temp = inputReader.readLine()) == null)
			;
		helper.setInStr(temp);

		user.setSocket(p2pSocket);
		user.setOutputWriter(outputWriter);
		user.setInputReader(inputReader);
        try {
        	while (!p2pSocket.isClosed()) {
    			while ((temp = inputReader.readLine()) == null)
    				;
				if (temp.indexOf("###session terminated")!= -1) {
					Constants.getP2pChatViewById(targetId).closeP2pChatView();
					return;
				}
    			Constants.getP2pChatViewById(targetId).addMessage(
    					targetName + " : " + temp);
    		}	
		} catch (Exception e) {
			//Constants.getP2pChatViewById(targetId).closeP2pChatView();
		}
		
		
	}

	private void chatWithServer() throws IOException {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(
				Constants.groupTalkSocket.getInputStream()));
		PrintWriter outputWriter = new PrintWriter(
				Constants.groupTalkSocket.getOutputStream());
		outputWriter.println(helper.generateOutStr());
		outputWriter.flush();
		String temp;
		while ((temp = inputReader.readLine()) == null)
			;
		helper.setInStr(temp);
		System.out.println("message from Server: " + temp);
		outputWriter.close();
		inputReader.close();
		Constants.groupTalkSocket.close();
	}

	private void login() throws IOException {
		helper.setAction("login");
		// System.out.println("send message to server: login" );

		Map<String, Object> requestData = new HashMap<String, Object>();
		requestData.put("Name", Constants.getUserName());
		requestData.put("p2pListenPort",
				Integer.toString(Constants.p2pListenPort));
		helper.setRequestData(requestData);

		BufferedReader inputReader = new BufferedReader(new InputStreamReader(
				Constants.mainSocket.getInputStream()));
		PrintWriter outputWriter = new PrintWriter(
				Constants.mainSocket.getOutputStream());
		outputWriter.println(helper.generateOutStr());
		outputWriter.flush();
		String temp;
		while ((temp = inputReader.readLine()) == null)
			;
		helper.setInStr(temp);

		Constants.myID = helper.getData().get("ID").toString();
		System.out.println("message from Server: " + temp);
		System.out.println("login status:" + helper.getPara("Status"));
		Constants.onlineUserList = new ArrayList<>();
		ArrayList<User> tempList = Json2UserList((JSONArray) helper.getData()
				.get("UserList"));

		for (int i = 0; i < tempList.size(); ++i)
			Constants.onlineUserList.add(tempList.get(i));

		System.out.println("current user list:\n");
		for (int i = 0; i < Constants.onlineUserList.size(); ++i) {
			System.out.println(Constants.onlineUserList.get(i).getId() + "--"
					+ Constants.onlineUserList.get(i).getName() + "\n");

		}
		Constants.threadLoginFlag++;

		while (Constants.mainSocket.isConnected()) {
			while ((temp = inputReader.readLine()) == null)
				;
			if (temp.startsWith("From:Server|")) {
				ProtocolHelper tempHelper = new ProtocolHelper();
				tempHelper.setInStr(temp);
				String tempAction = tempHelper.getPara("Action");
				if (tempAction.indexOf("LoginNotice") != -1) {
					Map tempData = tempHelper.getData();
					Constants.onlineUserList
							.add(new User(tempData.get("id").toString(),
									tempData.get("name").toString(), tempData
											.get("ip").toString(), Integer
											.parseInt(tempData.get("port")
													.toString())));
					/*System.out.println("A user have logined, id="
							+ tempData.get("id").toString() + "ip="
							+ tempData.get("ip"));*/
				} else if (tempAction.indexOf("LogoutNotice") != -1) {
					String id = tempHelper.getData().get("id").toString();

					for (int i = 0; i < Constants.onlineUserList.size(); ++i) {
						User tempUser = Constants.onlineUserList.get(i);

						if (tempUser.getId().indexOf(id) != -1) {
							System.out.println("A user have logouted: id=" + id
									+ "name=" + tempUser.getName());
							Socket tempSocket = tempUser.getSocket();
							if (tempSocket != null)
								Constants.getP2pChatViewById(id).closeP2pChatView();
							
							Constants.onlineUserList.remove(i);
							break;
						}
					}

				}
				System.out.println(" UserList:****");
				for (User i : Constants.onlineUserList)
					System.out.println(i.getName());
				Constants.clientView.updateUserList();
			} else {
				System.out.println(temp);
				ClientView.addMessage(temp);
			}
		}
	}

	private static ArrayList<User> Json2UserList(JSONArray jsonArray)
			throws JSONException {
		ArrayList<User> list = new ArrayList<User>();
		for (int i = 0; i < jsonArray.length(); ++i) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			User user = new User(jsonObject.getString("id"),
					jsonObject.getString("name"),
					jsonObject.getString("ipaddress"),
					jsonObject.getInt("p2pListenPort"));
			list.add(user);
		}
		return list;
	}

	private void logout() throws IOException {
		helper.setAction("logout");
		// System.out.println("send message to server: login" );

		Map<String, Object> requestData = new HashMap<String, Object>();
		requestData.put("name", Constants.getUserName());
		requestData.put("id", Constants.myID);
		helper.setRequestData(requestData);
		Socket tempSocket = new Socket(Constants.serverName, Constants.port);
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(
				tempSocket.getInputStream()));
		PrintWriter outputWriter = new PrintWriter(tempSocket.getOutputStream());
		System.out.println("sent logout message");
		outputWriter.println(helper.generateOutStr());
		outputWriter.flush();
		String temp;
		while ((temp = inputReader.readLine()) == null)
			;
		System.out.println("server logout ack ----" + temp);
		inputReader.close();
		outputWriter.close();
		tempSocket.close();
		Constants.mainSocket.close();
		Constants.p2pListenServerSocket.close();
	}

	private void broadcast() throws IOException {
		helper.setAction("broadcast");
		Map<String, Object> requestData = new HashMap<String, Object>();
		requestData.put("Name", Constants.getUserName());
		requestData.put("BroadcastStr", ClientView.getMessage());
		helper.setRequestData(requestData);
		chatWithServer();
	}
}