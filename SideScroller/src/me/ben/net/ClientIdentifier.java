package me.ben.net;

import java.net.InetAddress;

public class ClientIdentifier {
	private int clientID;
	private int clientUDPPort;
	private InetAddress clientInetAddress;
	
	public ClientIdentifier(int clientID, int clientUDPPort, InetAddress clientInetAddress){
		this.clientID = clientID;
		this.clientUDPPort = clientUDPPort;
		this.clientInetAddress = clientInetAddress;
	}
	
	public int getClientID(){
		return clientID;
	}
	
	public InetAddress getClientInetAddress(){
		return clientInetAddress;
	}
	
	public int getClientUDPPort(){
		return clientUDPPort;
	}
}