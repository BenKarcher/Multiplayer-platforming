package me.ben.net;

//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Scanner;

//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextField;
//import javax.swing.ScrollPaneConstants;

public class Client{
	
//	private final static int WIDTH = 300;
//	private final static int HEIGHT = 200;
	private final static int TCP_Timeout = 3000;
	//private final static int UDP_Timeout = 1000;
	private final static int MAX_PACKET_SIZE = Server.getMaxPacketSize();
	
	private boolean running = false;
	private static String serverIP;
	private int clientIDNumber;
	private int UDPPort;
	
	private DatagramSocket UDPSocket;
	private static Socket TCPSocket;
	private static DataInputStream in;
	private static DataOutputStream out;
	
	public static void main(String[] args) {	
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a server IP address, localhost, or join to join an open server:");
		serverIP = sc.nextLine();
		sc.close();
		new Client();
	}
	
	public Client(){
//		JFrame cf = new JFrame();
//		JPanel cp = new JPanel();
//		cp.setPreferredSize(new Dimension(WIDTH, HEIGHT));
//		cp.setBackground(Color.LIGHT_GRAY);
//		JLabel l = new JLabel();
//		l.setText("Type in IP address or select server from list.");
//		cp.add(l, BorderLayout.NORTH);
//		JTextField tf = new JTextField();
//		tf.setEditable(true);
//		tf.setPreferredSize(new Dimension(WIDTH, 20));
//		cp.add(tf, BorderLayout.CENTER);
//		JScrollPane sp = new JScrollPane();
//		sp.setPreferredSize(new Dimension(WIDTH, 155));
//		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//		cp.add(sp, BorderLayout.SOUTH);
//		cf.add(cp);
//		cf.pack();
//		cf.setResizable(false);
//		cf.setTitle("Client");
//		cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		cf.setLocationRelativeTo(null);
//		cf.setVisible(true);
		
		if(serverIP.equals("join")){
			serverIP = getServerIP();
			System.out.print(serverIP);
		}
		
		try {
			System.out.println("Connecting to TCP server...");
			TCPSocket = new Socket(serverIP, Server.getTCPPort());
			TCPSocket.setSoTimeout(TCP_Timeout);
			System.out.println("Connected.");
			in = new DataInputStream(TCPSocket.getInputStream());
			out = new DataOutputStream(TCPSocket.getOutputStream());
			System.out.println("Opening UDP socket...");
			UDPSocket = new DatagramSocket();
			UDPPort = UDPSocket.getLocalPort();
			//UDPSocket.setSoTimeout(UDP_Timeout);
			System.out.println("Socket opened.");
		} catch (IOException e) {
			System.err.println("Error in establishing server connections.");
			e.printStackTrace();
		}
		running = true;
		run();
	}
	
	public void run(){	
		try {
			//receive and store assigned client ID number
			clientIDNumber = in.readInt();
			System.out.println("Assigned ID number: " + clientIDNumber);
			//send UDP port number to TCP server for storage and use by UDP server
			out.writeInt(UDPPort);
		} catch (IOException e) {
			System.err.println("Failure in communication with TCP server.");
			e.printStackTrace();
		}
		//turn server IP address into InetAddress
		InetAddress serverInetAddress = null;
		try {
			serverInetAddress = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e) {
			System.err.println("Inavalid server IP.");
			e.printStackTrace();
		}
		while(running){
			//4 allocated because an integer is 32 bits = 4 bytes
			ByteBuffer b = ByteBuffer.allocate(4);
			b.putInt(clientIDNumber);
			byte[] statePacketBuf = b.array();
			DatagramPacket statePacket = new DatagramPacket(statePacketBuf, statePacketBuf.length, serverInetAddress, Server.getUDPPort());
			byte[] inPacketBuf = new byte[MAX_PACKET_SIZE];
			DatagramPacket inPacket = new DatagramPacket(inPacketBuf, inPacketBuf.length);
			try {
				UDPSocket.send(statePacket);
				UDPSocket.receive(inPacket);
				byte[] bytesReceived = inPacket.getData();
				ByteBuffer inByteBuffer = ByteBuffer.allocate(inPacketBuf.length);
				inByteBuffer.put(bytesReceived);
				inByteBuffer.flip();
				System.out.println("Client ID Number: " + clientIDNumber);
				System.out.println("Packet: " + inByteBuffer.getInt());
			} catch (IOException e) {
				System.err.println("Error in sending packet");
				e.printStackTrace();
			}
		}
	}
	
	public String getServerIP(){
		String serverIP = null;
		try {
			URL url = new URL("http://webdev.ausdk12.org/students/2015/ben_k/simple%20platform/levelsave.txt");
			Scanner reader = new Scanner(url.openStream());
			while(reader.hasNextLine()){
				serverIP = reader.nextLine();
			}
		    reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return serverIP;
	}
}