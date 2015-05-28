package com.project.ardrone_android_geii;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GereDroneOpenCV extends GereDrone  {

	
	
	GereThreadsOpenCV mThreadsCV;
	
	public GereDroneOpenCV(String CommandeStart, String Adresse, int iPort,
			JoyActivityOpenCV joyActivity) {
		sAdresse = Adresse;
		iPortDrone = iPort;
		CommandeGenere = CommandeStart;
		mJoy = joyActivity;
		

		mThreadsCV = new GereThreadsOpenCV(this);
		
		udp_socket = CreationSocketPacket(CommandeStart, iPort, Adresse); // CREATION DU SOCKET


		NavRec = new NavdataReceiver(udp_socket); //
		
		mThreadsCV.StartDonnee();
		
		
		mThreadsCV.StartWatchDog();
	}
	




	
	
	
	
	
	
	@Override
	public void decollage() { // BOUTTON DECOLLAGE, LANCE LE THREAD DE DECOLLAGE ET DE PILOTAGE
		 
		
    	//	RelanceSocketThreadDonnee();
								
			
			mThreadsCV.bWatchDog = false;
			iVal = 1;
			iSecondeU = 0;
			iSecondeD = 0;
			iMinuteU = 0;
			iMinuteD = 0;
			icSecondeU = 0;
			icSecondeD = 0;

			AttenteMs(50);
			mThreadsCV.Startdecollage();
	
			AttenteMs(50);
			mThreadsCV.StartPilotage();
			

			mScheduleTaskExecutor.scheduleAtFixedRate(mThreadsCV.mTaskChrono, 0, 10,TimeUnit.MILLISECONDS);
		

	}
	
	@Override
	public void atterissage() { // ATTERIT ET ARRETE LE THREAD DE PILOTAGE, LE REMPLACE PAR LE WATCHDOG
		 

    	mScheduleTaskExecutor.shutdownNow();
    	mScheduleTaskExecutor = Executors.newSingleThreadScheduledExecutor();
    	mThreadsCV.bWatchDog = true;
		
		AttenteMs(50);
	
		
		mThreadsCV.StartAtterissage();
		AttenteMs(50);
		mThreadsCV.StartWatchDog();
		
		
		

		

	

	}

}
