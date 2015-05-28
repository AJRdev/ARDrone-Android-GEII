package com.example.projet_er4_v1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;




public class GereDrone {
	
	
	ScheduledExecutorService mScheduleTaskExecutor = Executors.newScheduledThreadPool(2);
	
	public DatagramPacket udp_packet;
	public DatagramSocket udp_socket;
	public InetAddress adresse;
	public JoyActivityVideo mJoy;
	
	
	GereThreads mThreads;
	NavdataReceiver NavRec;
	
	//public DatagramSocket sauv_Socket;
	//boolean bSocketClose = true;
	
	public byte[] buffer;


	
	String CommandeGenere = "";
	String sAdresse;
	String sTemps;
	
	
	static int iPitch = 0;
	static int iAltitude = 0;
	static int iBatterie = 0;
	static int iYaw = 0;
	static int iRoll = 0;
	static int iSpeed = 0;
	
	int iVal = 0;
	int iPortDrone;

	int iSecondeU = 0;
	int iSecondeD = 0;
	int iMinuteU = 0;
	int iMinuteD = 0;
	int icSecondeU = 0;
	int icSecondeD = 0;
	
	public GereDrone(){
		
	}
	
	public GereDrone(String CommandeStart, String Adresse, int iPort, JoyActivityVideo joyActivity){			// CONSTRUCTEUR RECOI l'objet de classe MainActivity en parametre
		
		sAdresse = Adresse;
		iPortDrone = iPort;
		CommandeGenere = CommandeStart;
		mJoy = joyActivity;
		

		mThreads = new GereThreads(this);
		
		udp_socket = CreationSocketPacket(CommandeStart, iPort, Adresse); // CREATION DU SOCKET


		NavRec = new NavdataReceiver(udp_socket); //
		
		mThreads.StartDonnee();
		
		
		mThreads.StartWatchDog();

		
	}
	
	   public DatagramSocket CreationSocketPacket(String ComandeUDP, int port, String host){
	    	buffer = new byte[512];
	    	 
				try {											// TRY CREATION SOCKET
					udp_socket = new DatagramSocket();
					
				
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					
				}	
		

	    		
				try {													// INSERTION TRAME
					buffer = ComandeUDP.concat("\r").getBytes("ASCII");

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
							
				try {												// INSERTION ADRESSE
					adresse = InetAddress.getByName(host);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			udp_packet = new DatagramPacket(buffer, buffer.length, adresse, port);
			return udp_socket;
							
	    }
	   
	    public void EnvoiTrameUDP(String ComandeUDP){	
	    	
	    	
	    	try {																// INSERTION TRAME
				buffer = ComandeUDP.concat("\r").getBytes("ASCII");

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
	    	udp_packet.setData(buffer);							// Changement de la commande du packet
	    	
				try {
				udp_socket.send(udp_packet);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
	    }
	    
	    public void decollage() { // BOUTTON DECOLLAGE, LANCE LE THREAD DE DECOLLAGE ET DE PILOTAGE
										 
			
	    	//	RelanceSocketThreadDonnee();
									
				
				mThreads.bWatchDog = false;
				iVal = 1;
				iSecondeU = 0;
				iSecondeD = 0;
				iMinuteU = 0;
				iMinuteD = 0;
				icSecondeU = 0;
				icSecondeD = 0;

				AttenteMs(50);
				mThreads.Startdecollage();
		
				AttenteMs(50);
				mThreads.StartPilotage();
				

				mScheduleTaskExecutor.scheduleAtFixedRate(mThreads.mTaskChrono, 0, 10,TimeUnit.MILLISECONDS);
			

		}
	    
	    public void atterissage() { // ATTERIT ET ARRETE LE THREAD DE PILOTAGE, LE REMPLACE PAR LE WATCHDOG
			 

	    	mScheduleTaskExecutor.shutdownNow();
	    	mScheduleTaskExecutor = Executors.newSingleThreadScheduledExecutor();
	    	mThreads.bWatchDog = true;
			
			AttenteMs(50);
		
			
			mThreads.StartAtterissage();
			AttenteMs(50);
			mThreads.StartWatchDog();
			
			
			
	
			

		

		}

		public void emergency() {				// boutton atterissage d'urgence

			mScheduleTaskExecutor.shutdownNow();
			mScheduleTaskExecutor = Executors.newSingleThreadScheduledExecutor();

			mThreads.bWatchDog = true;
			
			mThreads.StartEmergency();
		
			AttenteMs(50);

			

		}
		
	    
	    public void AttenteMs(int iMSeconde) { // FONCTION UTILE D'ATTENTE NON BLOQUANTE
			

			try {
				TimeUnit.MILLISECONDS.sleep(iMSeconde);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		public void IncrementSeq() { // INCREMENTE LA VARIABLE DE SEQUENCE
			if (iVal >= 1000) {
				iVal = 1;
			} else {
				
				iVal++;

			}

		}

	
}
	
	
	


