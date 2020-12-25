package de.eberln;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Atem {

	static DatagramSocket socket;
	
	private static String CMD_HELLO = "1014";
	
	private static boolean isInitialized;
	
	public static void main(String[] args) throws IOException {
		
		isInitialized = false;
		
		socket = new DatagramSocket(9910);
		
		UDPReceiver udp = new UDPReceiver(socket);
		udp.start();
		
		connectToAtem();
	
	}
	
	
	public static void connectToAtem() throws IOException {
		
		Packet p = new HelloPacket("1014", "1337", "00");
		DatagramPacket dpacket = new DatagramPacket(p.getDatagramPacket(), p.getDatagramPacket().length, InetAddress.getByName("192.168.10.240"), 9910);
		socket.send(dpacket);
	}
	
	
	
	public static void handleSocketData(DatagramPacket packet) throws IOException {
		System.out.println("Neues Paket");
		
		String[] socketData = parseCommandHeader(packet.getData());
		
		if(socketData[0].equals(CMD_HELLO)) {
			Packet p = new Packet("800c", "1337", "00");
			DatagramPacket dpacket = new DatagramPacket(p.getDatagramPacket(), p.getDatagramPacket().length, InetAddress.getByName("192.168.10.240"), 9910);
			socket.send(dpacket);
		}
		
	}
	
	
	
	public static String[] parseCommandHeader(byte[] packet) {
		
		byte[] header = new byte[12];
		for(int i=0;i<12;i++) {
			header[i] = packet[i];
		}
		
		String head = Integer.toHexString((int) header[0]) + Integer.toHexString((int) header[1]);
		String uid = Integer.toHexString((int) header[2]) + Integer.toHexString((int) header[2]);
		String packageNr = Integer.toHexString((int) header[4]) + Integer.toHexString((int) header[5]);
		
		String[] result = {head, uid, packageNr};
		
		return result;
	}
}
