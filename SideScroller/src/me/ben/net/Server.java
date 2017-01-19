package me.ben.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Server{
	
	private final static int TCP_PORT = 7777;
	private final static int UDP_PORT = 20013;
	private final static int MAX_PACKET_SIZE = 256;
	private static Thread UDPServerThread;
	private static Thread TCPServerThread;
	private static volatile List<ClientIdentifier> listOfClientInformation = new ArrayList<ClientIdentifier>();
	
	public static void main(String[] args) {
		new Server();
	}
	
	public Server(){
		TCPServerThread = new Thread(new TCPServer(), "TCP server");
		TCPServerThread.start();
		UDPServerThread = new Thread(new UDPServer(), "UDP server");
		UDPServerThread.start();
		try {
		    // open a connection to the site
		    URL url = new URL("http://webdev.ausdk12.org/students/2015/ben_k/simple%20platform/savedata/form.php");
		    URLConnection con = url.openConnection();
		    // activate the output
		    con.setDoOutput(true);
		    PrintStream ps = new PrintStream(con.getOutputStream());
		    // send parameters to site (preface with data=)
		    ps.print("data=" + getPublicIP()+"    eclipse  ");
		    // we have to get the input stream in order to actually send the request
		    con.getInputStream();
		    // close the print stream
		    ps.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPublicIP() throws MalformedURLException{
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = null;
	    try {
	    	in = new BufferedReader(new InputStreamReader(
	    			whatismyip.openStream()));
	        String ip = in.readLine();
	        return ip;
	    } catch (IOException e) {
			e.printStackTrace();
		} finally {
	        if (in != null) {
	        	try {
	        		in.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
		return null;
	}
	
	public static List<ClientIdentifier> getListOfClientInformation(){
		return listOfClientInformation;
	}
	
	public static void addToListOfClientInformation(ClientIdentifier ci){
		listOfClientInformation.add(ci);
	}
	
	public static int getUDPPort(){
		return UDP_PORT;
	}
	
	public static int getTCPPort(){
		return TCP_PORT;
	}
	
	public static int getMaxPacketSize(){
		return MAX_PACKET_SIZE;
	}
}