package de.eberln;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPReceiver extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[]buf = new byte[1024];

    public UDPReceiver(DatagramSocket socket) throws SocketException {
        this.socket = socket;
    }

    public void run(){
        running = true;

        while (running) {
            DatagramPacket packet
              = new DatagramPacket(buf, buf.length);
            try {
				socket.receive(packet);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            Atem.sendMessage(packet);
		}
    }
}