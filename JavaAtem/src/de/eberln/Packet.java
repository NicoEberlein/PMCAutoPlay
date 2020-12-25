package de.eberln;

public class Packet {
	
	private String header;
	
	private String type;
	private String uid;
	private String packageNr;
	
	private byte[] datagram;
	
	public Packet(String type, String uid, String packageNr) {
		
		if(type.equals("Hello")) {
			this.type = "1014";
		}else if(type.equals("ACK")){
			this.type = "800c";
		}
		
		this.uid = uid;
		this.packageNr = packageNr;
		
		createHeader();
		
	}
	
	private void createHeader() {
		
		header = type;
		header += uid;
		header += packageNr;
		
		byte[] b = hexStringToByteArray(header);
		
		if(b.length < 12) {
			byte[] fill = new byte[12-b.length];
			datagram = new byte[b.length + fill.length];
			
			System.arraycopy(b, 0, datagram, 0, b.length);
			System.arraycopy(fill, 0, datagram, b.length, fill.length);	
		}else {
			datagram = b;
		}
	}
	
	public byte[] getDatagram() {
		return datagram;
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
}
