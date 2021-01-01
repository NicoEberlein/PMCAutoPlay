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
	
	public static int[][] channels = new int[8][2];
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		socket = new DatagramSocket(9910);
		
		UDPReceiver udp = new UDPReceiver(socket);
		Thread.sleep(2000);
		udp.start();
		
		connectToAtem();
	
	}
	
	public static void startAtemListener() throws InterruptedException, IOException {
		
		socket = new DatagramSocket(9910);
		
		UDPReceiver udp = new UDPReceiver(socket);
		Thread.sleep(2000);
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
			byte[] currentPacket = null;
			int size = 0;
			
			do {
				
				String sizeString = ByteArrayHandler.convertByteToHexString(payload[0]) + ByteArrayHandler.convertByteToHexString(payload[1]);
				size = Integer.parseInt(sizeString, 16);
				
				typeOfContent = new String(Arrays.copyOfRange(payload, 4, 8), StandardCharsets.UTF_8);
				
				currentPacket = Arrays.copyOfRange(payload, 0, size);
				
				payload = Arrays.copyOfRange(payload, size, payload.length);
				
			}while(!typeOfContent.equalsIgnoreCase("TlIn") && size != 0);
			
			if(typeOfContent.equals("TlIn")) {
				parseTlInPacket(currentPacket);
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
	
	public static void parseTlInPacket(byte[] payload) {
		
		byte[] data = Arrays.copyOfRange(payload, 10, 18);
		int i=1;
		for(byte b : data) {
			if(Byte.toUnsignedInt(b) == 1) {
				System.out.println("Prg: " + i);
				channels[i-1][0] = 1;
			}else if(Byte.toUnsignedInt(b) == 2) {
				System.out.println("Prv: " + i);
				channels[i-1][1] = 1;
			}else if(Byte.toUnsignedInt(b) == 3) {
				System.out.println("Prg: " + i);
				System.out.println("Prv: " + i);
				channels[i-1][0] = 1;
				channels[i-1][1] = 1;
			}
			i++;
		}
	}
}
