package de.eberln;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

public class Atem {

	static DatagramSocket socket;
	
	private static String CMD_HELLO = "1014";
	private static String CMD_ACK = "880C";
	
	private static String uid = "1337";
	
	private static boolean isInitialized;
	
	public static void main(String[] args) throws IOException {
		
		isInitialized = false;
		
		socket = new DatagramSocket(9910);
		
		UDPReceiver udp = new UDPReceiver(socket);
		udp.start();
		
		connectToAtem();
	
	}
	
	
	public static void connectToAtem() throws IOException {
		
		Packet p = new HelloPacket("1014", uid, "00");
		DatagramPacket dpacket = new DatagramPacket(p.getDatagramPacket(), p.getDatagramPacket().length, InetAddress.getByName("192.168.10.240"), 9910);
		socket.send(dpacket);
		
	}
	
	public static void handleSocketData(DatagramPacket packet) throws IOException {
		
		String[] socketData = parseCommandHeader(packet.getData());
		byte[] data = packet.getData();
		
		if(socketData[0].equals(CMD_HELLO)) {
			
			Packet p = new Packet("800C", uid, "00");
			DatagramPacket dpacket = new DatagramPacket(p.getDatagramPacket(), p.getDatagramPacket().length, InetAddress.getByName("192.168.10.240"), 9910);
			socket.send(dpacket);
		
		}else if(socketData[0].equals(CMD_ACK)) {
		
			uid = socketData[1];
			
			Packet p = new Packet("800C", uid, getCurrentPackageNumber(data)[0] + getCurrentPackageNumber(data)[1]);
			DatagramPacket dpacket = new DatagramPacket(p.getDatagramPacket(), p.getDatagramPacket().length, InetAddress.getByName("192.168.10.240"), 9910);
			socket.send(dpacket);
			
		}else if(socketData[0].equals("0930") || socketData[0].equals("0894") || socketData[0].equals("0D8C")) {
			String typeOfContent = null;
			byte[] payload = ByteArrayHandler.removeHeader(data);
			
			do {
				System.out.println(payload[0] + " " + payload[1]);
				int size = (int) (new Byte(payload[0] + "" + payload[1]) & 0xFF);
				typeOfContent = new String(Arrays.copyOfRange(payload, 4, 7), StandardCharsets.UTF_8);
				payload = Arrays.copyOfRange(payload, size, payload.length);
			}while(!typeOfContent.equals("TlIn") || (int) payload[0] + (int) payload[1] == 0);
			
			if(typeOfContent.equals("TlIn")) {
				System.out.println("TlIn gefunden!");
			}
		}
	}
	
	
	
	public static String[] parseCommandHeader(byte[] packet) {
		
		byte[] header = new byte[12];
		for(int i=0;i<12;i++) {
			header[i] = packet[i];
		}

		String hex = DatatypeConverter.printHexBinary(header);
		String[] hexArray = hex.split("");
		String[] headerArray = new String[hexArray.length/2];
		
		int j = 0;
		
		for(int i=0;i<hexArray.length;i+=4) {
			headerArray[j] = hexArray[i] + hexArray[i+1] + hexArray[i+2] + hexArray[i+3];
			j++;
		}
		
		return headerArray;
	}
	
	public static String[] getCurrentPackageNumber(byte[] packet) {
		
		byte[] number = {packet[10], packet[11]};
		String hex = DatatypeConverter.printHexBinary(number);
		String[] hexArray = hex.split("");
		String[] numberArray = new String[hexArray.length/2];
		
		int j = 0;
		
		for(int i=0;i<hexArray.length;i+=2) {
			numberArray[j] = hexArray[i] + hexArray[i+1];
			j++;
		}
		
		return numberArray;
	}
}
