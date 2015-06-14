package com.project.ardrone_android_geii;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

/**

 * 

 * Class handling data received from the Drone's sensors.
 
 */
public class SensorData_Receiver {					//DECLARATION
	
	private InetAddress ip_addr;
	private DatagramSocket socket;
	private byte[] nav_buff_start={0x01,0x00,0x00,0x00};
	private byte[] nav_buff = new byte[512];
	private byte[] at_buff = new byte[512];
	private DatagramPacket nav_packet;
	private DatagramPacket at_packet;
	
	public SensorData_Receiver(DatagramSocket udp_socket){
		
		this.socket = udp_socket;
		try {
			this.ip_addr = InetAddress.getByName("192.168.1.1");
			this.at_buff = "AT*CONFIG=2,\"general:navdata_demo\",\"TRUE\"\r".getBytes("ASCII");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.nav_packet = new DatagramPacket(nav_buff_start, nav_buff_start.length, ip_addr, 5554);
		this.at_packet = new DatagramPacket(at_buff, at_buff.length, ip_addr, 5556);
		
	}
	
	public void ActualiseSocket (DatagramSocket sock){
		
		this.socket = null;
		this.socket = sock;
		
	}
	
	public int InitNavData(){			
		
		int result = 0;
		try {
			this.socket.send(nav_packet);
			TimeUnit.MILLISECONDS.sleep(30);
			this.socket.send(at_packet);
			TimeUnit.MILLISECONDS.sleep(30);
			result = 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = 0;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = 0;
		}
		this.nav_packet.setData(nav_buff);
		this.nav_packet.setLength(nav_buff.length);
				
		return result;
	}
	
	public int ReceiveNavdata(){
		
		int iResult;
		try {
			this.socket.receive(nav_packet);
			iResult = 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			iResult = 0;
		}
		return iResult;
	}
	
	public int GetBattery(){
		
		int iBatt=0;
		iBatt = byteArrayToInt(nav_buff, 24);
		return iBatt;
		
	}
	
	public int GetPitch(){
		
		float fPitch = 0;
		int iPitch = 0;
		fPitch = byteArrayToFloat(nav_buff, 28);
		fPitch = (fPitch/86000)*100;
		iPitch = (int) fPitch;
		return iPitch;
	}
	
	public int GetYaw(){
		
		float fYaw = 0;
		int iYaw = 0;
		fYaw = byteArrayToFloat(nav_buff, 36);
		fYaw = (fYaw/86000)*100;
		iYaw = (int) fYaw;
		return iYaw;
	}
	
	public int GetRoll(){
		
		float fRoll = 0;
		int iRoll = 0;
		fRoll = byteArrayToFloat(nav_buff, 32);
		fRoll = (fRoll/86000)*100;
		iRoll = (int) fRoll;
		return iRoll;
	}

	public int GetSpeed(){
	
		float vx = 0;
		float vy = 0;
		float vz = 0;
		int iSpeed = 0;
		vx = byteArrayToFloat(nav_buff, 44);
		vy = byteArrayToFloat(nav_buff, 48);
		vz = byteArrayToFloat(nav_buff, 52);
		iSpeed = (int) Math.sqrt(vx*vx + vy*vy + vz*vz);
		return iSpeed;
	
	}
	
	public int GetAltitude(){
		
		int iAlt = 0;
		iAlt = (byteArrayToInt(nav_buff, 40))/1000;
		return iAlt;
	}
	
	public void NoSleep(){
		try {
			socket.send(at_packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public int byteArrayToInt(byte[] b, int offset){
		
	        int value = 0;
	        for(int i = 3; i >= 0; i--)
	        {
	            int shift = i * 8;
	            value += (b[i + offset] & 0x000000FF) << shift;
	        }
	        return value;
	    }
	    
    public float byteArrayToFloat(byte[] buffer, int offset){
			
	    	byte[] buff_tmp = new byte[4];
	    	int iBcl = 0;
	    	float fResult=0;
	    	
	    	for(iBcl=0; iBcl<4; iBcl++)
	        {
	            buff_tmp[iBcl]=buffer[iBcl+offset];
	        }
	    	fResult = ByteBuffer.wrap(buff_tmp).order(ByteOrder.LITTLE_ENDIAN).getFloat();
	    	return fResult;
	    	
	    }

}
