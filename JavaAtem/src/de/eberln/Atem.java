package de.eberln;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Atem {

	static DatagramSocket socket;
	
	public static void main(String[] args) throws SocketException {
		
		/*socket = new DatagramSocket(9910);
		
		try {
			connectAtem();
			new UDPReceiver(socket).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//Packet p = new Packet("Hello", "1337", "0000");
		Packet p = new Packet("ACK", "1473", "13");
		
		
		byte[] datagram = p.getDatagram();
		
		for(int i=0;i<datagram.length;i++) {
			System.out.println(datagram[i]);
		}
		
	}
	
	/*public static void connectAtem() throws UnknownHostException, IOException {
		
		

		InetAddress ia = InetAddress.getByName("192.168.10.240");
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length, ia, 9910);
		socket.send(packet);

	}*/
}
