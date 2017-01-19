package me.ben.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable{
	
	final static int TCP_SERVER_PORT = Server.getTCPPort();
	static boolean running = false;
	static ServerSocket serverSocket;
	static Socket socket;
	
	public TCPServer(){
		running = true;
		try {
			serverSocket = new ServerSocket(TCP_SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(running){
			socket = null;
			try {
				socket = serverSocket.accept();
				new Thread(new WorkerRunnable(socket)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}