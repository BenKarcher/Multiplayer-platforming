package me.ben.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class WorkerRunnable implements Runnable{
	
	private int maxClientIDNumber = 999999;
	private DataOutputStream out;
	private DataInputStream in;
	private int clientIDNumber;
	private InetAddress clientInetAddress;
	
	public WorkerRunnable(Socket socket){
		//socket given as parameter never stored in a field
		System.out.println("WorkerRunnable being instantiated for ip adress " + socket.getInetAddress());
		try {
			clientInetAddress = socket.getInetAddress();
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Instantiation done.");
	}

	@Override
	public void run() {
		Random r = new Random();
		boolean isValidID;
		//pick a random client ID number not already in use
		do{
			isValidID = true;
			clientIDNumber = r.nextInt(maxClientIDNumber + 1);
			for(ClientIdentifier ci : Server.getListOfClientInformation()){
				if(ci.getClientID() == clientIDNumber){
					isValidID = false;
					break;
				}
			}
		}while(!isValidID);

		try {
			//send the generated client ID number to the client
			out.writeInt(clientIDNumber);
			out.flush();
			//receive the UDP port of the client
			int UDPPort = in.readInt();
			System.out.println("Client UDP port: " + UDPPort);
			//store client ID and UDP port in server
			Server.addToListOfClientInformation(new ClientIdentifier(clientIDNumber, UDPPort, clientInetAddress));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}