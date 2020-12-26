package de.eberln;

public class ByteArrayHandler {

	//Konvertiert einen 2 oder 4 langen String zu einem zwei langen Byte Array. Sollte der String nur zwei lang sein wird die erste Stelle des Byte Arrays mit 0x00 aufgefüllt.
	
	public static byte[] convertStringToByteArray(String s) {
		
		byte[] result = new byte[2];
		
		if(s.length() == 2) {
			
			result[0] = (byte) 0x00;
			result[1] = new Integer(Integer.decode("0x" + s)).byteValue();
			return result;
			
		}else if(s.length() == 4) {
			
			char[] charS = s.toCharArray();
			result[0] = new Integer(Integer.decode("0x" + charS[0] + charS[1])).byteValue();
			result[1] = new Integer(Integer.decode("0x" + charS[2] + charS[3])).byteValue();
			return result;
			
		}else {
			byte[] b = {(byte) 0x00, (byte) 0x00};
			return b;
		}
		
	}
	
	//Verbindet 2,3 oder 4 Byte Arrays
	
	public static byte[] connectByteArrays(byte[] first, byte[] second, byte[] third, byte[] fourth) {
		byte[] connectedByteArray = new byte[first.length + second.length + third.length + fourth.length];
		
		System.arraycopy(first, 0, connectedByteArray, 0, first.length);
		System.arraycopy(second, 0, connectedByteArray, first.length, second.length);
		System.arraycopy(third, 0, connectedByteArray, first.length+second.length, third.length);
		System.arraycopy(fourth, 0, connectedByteArray, first.length+second.length+third.length, fourth.length);
		return connectedByteArray;
	}
	
	public static byte[] connectByteArrays(byte[] first, byte[] second, byte[] third) {
		byte[] connectedByteArray = new byte[first.length + second.length + third.length];
		
		System.arraycopy(first, 0, connectedByteArray, 0, first.length);
		System.arraycopy(second, 0, connectedByteArray, first.length, second.length);
		System.arraycopy(third, 0, connectedByteArray, first.length+second.length, third.length);
		
		return connectedByteArray;
	}
	
	public static byte[] connectByteArrays(byte[] first, byte[] second) {
		byte[] connectedByteArray = new byte[first.length + second.length];
		
		System.arraycopy(first, 0, connectedByteArray, 0, first.length);
		System.arraycopy(second, 0, connectedByteArray, first.length, second.length);
		
		return connectedByteArray;
	}

	
	//Stellt sicher, dass das Outputarray immer die Länge length hat, sollte es kürzer sein. Es werden entsprechend viele Bytes 0x00 hinzugefügt
	
	public static byte[] formatDatagramPacketLength(byte[] oldHeader, int length) {
		
		if(oldHeader.length < length) {
			
			byte[] fillArray = new byte[length-oldHeader.length];
			
			for(int i=0;i<fillArray.length;i++) {
				fillArray[i] = (byte) 0x00;
			}
			
			byte[] newHeader = connectByteArrays(oldHeader, fillArray);
			return newHeader;
			
			
		}else{
			return oldHeader;
		}
	}
	
	public static byte[] removeHeader(byte[] datagram) {
		byte[] datagramWithoutHeader = new byte[datagram.length-12];
		
		for(int i=0;i<datagramWithoutHeader.length;i++) {
			datagramWithoutHeader[i] = datagram[i+12];
		}
		
		return datagramWithoutHeader;
		
	}
	
	public static String convertByteToHexString(byte b) {
		return String.format("%02X", b);
	}
	
}
