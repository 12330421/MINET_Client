package com.minet;

import java.awt.EventQueue;

public class ClientTest {

	public static void main(String[] args)  {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView window = new LoginView();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
