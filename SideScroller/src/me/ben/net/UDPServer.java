package me.ben.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class UDPServer implements Runnable{
	
	private static final int MAX_PACKET_SIZE = Server.getMaxPacketSize();
	
	private DatagramSocket socket;
	private boolean running = false;
	
	public UDPServer(){
		try {
			socket = new DatagramSocket(Server.getUDPPort());
		} catch (SocketException e) {
			System.err.println("Socket creation failed.");
			e.printStackTrace();
		}
		running = true;
	}
	
	@Override
	public void run(){
		while(running){
			byte[] inPacketBuf = new byte[MAX_PACKET_SIZE];
			DatagramPacket inPacket = new DatagramPacket(inPacketBuf, inPacketBuf.length);
			try {
				//System.out.println("Ready to receive packet.");
				socket.receive(inPacket);
				byte[] bytesReceived = inPacket.getData();
				ByteBuffer b = ByteBuffer.allocate(inPacketBuf.length);
				b.put(bytesReceived);
				b.flip();
				int senderClientID = b.getInt();
				boolean isValidID = false;
				for(ClientIdentifier ci : Server.getListOfClientInformation()){
					if(ci.getClientID() == senderClientID){
						isValidID = true;
						break;
					}
				}
				if(isValidID){
					for(ClientIdentifier ci : Server.getListOfClientInformation()){
						if(ci.getClientID() != senderClientID){
							b.clear();
							b.putInt(senderClientID);
							byte[] outPacketBuf = b.array();
							DatagramPacket outPacket = new DatagramPacket(outPacketBuf, outPacketBuf.length, ci.getClientInetAddress(), ci.getClientUDPPort());
							socket.send(outPacket);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}