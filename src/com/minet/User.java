package com.minet;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User {

	private String id;
	private String name;
	private Socket socket;
	private String IP;
	private int port;
	private boolean isOnline;
	private BufferedReader inputReader;
	private PrintWriter outputWriter;

	public User(String id, String name, String IP, int port) {
		this.id = id;
		this.name = name;
		this.IP = IP;
		this.port = port;
		this.socket = null;
		this.inputReader = null;
		this.outputWriter = null;
	}

	public User(String id, String name, String IP, int port, Socket socket) {
		this.id = id;
		this.name = name;
		this.IP = IP;
		this.port = port;
		this.socket = socket;
		this.inputReader = null;
		this.outputWriter = null;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIP() {
		return this.IP;
	}

	public int getPort() {
		return this.port;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setInputReader(BufferedReader br) {
		this.inputReader = br;
	}

	public void setOutputWriter(PrintWriter pw) {
		this.outputWriter = pw;
	}

	public BufferedReader getInputReader() {
		return this.inputReader;
	}

	public PrintWriter getOutputWriter() {
		return this.outputWriter;
	}
}