package de.eberln;

public class Packet {
	
	private byte[] header;
	
	private byte[] head;
	private byte[] userID;
	private byte[] packageNr;
	
	public Packet(String type, String uid, String packageNr) {
		
		this.head = ByteArrayHandler.convertStringToByteArray(type);
		this.userID = ByteArrayHandler.convertStringToByteArray(uid);
		this.packageNr = ByteArrayHandler.convertStringToByteArray(packageNr);
		
		header = ByteArrayHandler.connectByteArrays(head, userID, this.packageNr);
	}
	
	public byte[] getHeader() {
		return ByteArrayHandler.formatDatagramPacketLength(header, 12);
	}
	
	public byte[] getDatagramPacket() {
		return getHeader();
	}
}
