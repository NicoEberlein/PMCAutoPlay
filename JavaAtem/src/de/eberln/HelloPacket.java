package de.eberln;

public class HelloPacket extends Packet{
	
	public HelloPacket(String type, String uid, String packageNr) {
		super(type, uid, packageNr);
	}
	
	public byte[] getDatagramPacket() {
		
		byte[] data = {(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
		
		return ByteArrayHandler.connectByteArrays(getHeader(), data);
		
	}
	
	
}
