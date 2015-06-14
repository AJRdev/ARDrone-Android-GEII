package com.project.ardrone_android_geii;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



/**

 * 

 * Class managing the different threads of the application.
 
 */
public class ThreadsManager {
	
	TaskChrono mTaskChrono;
	Thread tTaskDonnee;
	Thread tTaskReglage;
	Thread tTaskDecollage;
	Thread tTaskPilotage;
	Thread tTaskAtterissage;
	Thread tTaskWatchDog;
	Thread tTaskEmergency;
	
	DroneManager mDrone;
	DroneManager_OpenCV mDroneCV;
	
	Boolean isDonnee = true;	
	Boolean bWatchDog = true;
	
	public ThreadsManager(DroneManager_OpenCV Drone){			// CONSTRUCTEUR RECOI l'objet de classe JoyActivityVideo en parametre
		
		mDroneCV = Drone;
		mTaskChrono = new TaskChrono();
		
	}
	
	
	public ThreadsManager(DroneManager Drone){			// CONSTRUCTEUR RECOI l'objet de classe JoyActivityVideo en parametre
		
		mDrone = Drone;
		mTaskChrono = new TaskChrono();
		
	}
	
	public void StartDonnee() {
		
		tTaskDonnee = new Thread(new TaskDonnee());
		tTaskDonnee.start();
		
	}
	
	public void StartReglage() {
		tTaskReglage = new Thread(new TaskReglage());
		tTaskReglage.start();
			
		}
	
	
	public void StartPilotage() {
		
		tTaskPilotage = new Thread(new TaskPilotage());
		tTaskPilotage.start();
	}
	
	
	public void StartEmergency() {
		
		tTaskEmergency = new Thread(new TaskEmergency());
		tTaskEmergency.start();
	}
	
	
	public void StartAtterissage() {
		tTaskAtterissage = new Thread(new TaskAtterissage());
		tTaskAtterissage.start();
		
	}
	
	public void Startdecollage() {
		
		tTaskDecollage = new Thread(new TaskDecollage());
		tTaskDecollage.start();
	}
	
	public void StartWatchDog() {
		
		tTaskWatchDog = new Thread(new TaskWatchDog());
		tTaskWatchDog.start();
	}




	class TaskDecollage implements Runnable { // THREAD DE DECOLLAGE
		@Override
		public void run() {

			mDrone.IncrementSeq();
		//	mDrone.EnvoiTrameUDP("AT*REF=1,290717952");


			mDrone.AttenteMs(50);
			mDrone.EnvoiTrameUDP("AT*FTRIM=1\rAT*REF=2,290718208"); // TRIM + DECOLLAGE

																	

			mDrone.iVal = 3;

			mDrone.AttenteMs(50);

		}
				
	}

	class TaskEmergency implements Runnable { // THREAD D'ATTERISSAGE D'URGENCE
		@Override
		public void run() {

			mDrone.IncrementSeq();
			mDrone.EnvoiTrameUDP("AT*REF=" + mDrone.iVal + ",290717952"); // Arret / d'urgence des moteurs
															
			mDrone.AttenteMs(50);

		}
	}

	class TaskAtterissage implements Runnable { // THREAD D'ATTERISSAGE
		@Override
		public void run() {
			mDrone.IncrementSeq();
			mDrone.EnvoiTrameUDP("AT*REF=" + mDrone.iVal + ",290717696"); // ATTERISSAGE

			mDrone.AttenteMs(50);

		}
	}

	class TaskWatchDog implements Runnable { // THREAD WATCHDOG
		@Override
		public void run() {
		

			do {
				mDrone.IncrementSeq();

				mDrone.EnvoiTrameUDP("AT*PCMD=" + mDrone.iVal + ",0,0,0,0,0"); 			// RESET WATCHDOG pour eviter le TimeOut
																	

				mDrone.AttenteMs(500);
			} while (bWatchDog == true);

		}
	}

	class TaskPilotage implements Runnable { // THREAD DE PILOTAGE, LANCE DES L'APPUI SUR LE BOUTON DECOLLAGE
		@Override
		public void run() {			
			do {	
				Log.v(DroneUI_OpenCV.TAG, "GereThreadsNormal: TaskPilotage run ");
				//INCREMENT LE NUMERO DE SEQUENCE
				mDrone.IncrementSeq();	
				//ASSEMBLAGE DE LA COMMANDE
				mDrone.CommandeGenere = "AT*PCMD=" + mDrone.iVal + mDrone.mJoy.GenereCommande(); 
				//ENVOI DE LA COMMANDE
				mDrone.EnvoiTrameUDP(mDrone.CommandeGenere);
				//ATTENTE DE 50MS POUR LA FLUIDITE DES MOUVEMENTS
				mDrone.AttenteMs(50);
			} while (bWatchDog == false); // PASSE A FALSE LORS DE L'APPUI SUR ATERRISSAGE
			
			mDrone.iVal = 0;
		}
	}
	
	public class TaskDonnee implements Runnable { // THREAD RECUPERATION ET AFFICHAGE DES DONNEE
									
		@Override
		public void run() {
			
			mDrone.NavRec.InitNavData();

			do {

				mDrone.NavRec.ReceiveNavdata();
				mDrone.NavRec.NoSleep();

				DroneManager.iBatterie = mDrone.NavRec.GetBattery();
				DroneManager.iAltitude = mDrone.NavRec.GetAltitude();			
				DroneManager.iYaw = mDrone.NavRec.GetYaw();
				DroneManager.iPitch = mDrone.NavRec.GetPitch();
				DroneManager.iRoll = mDrone.NavRec.GetRoll();
				DroneManager.iSpeed = mDrone.NavRec.GetSpeed();
				mDrone.AttenteMs(30);

												    
			} while (isDonnee == true);
		}

	}

	class TaskReglage implements Runnable { // THREAD DE REGLAGE
		@Override
		public void run() {
			
			mDrone.AttenteMs(50);
			mDrone.IncrementSeq();
			mDrone.EnvoiTrameUDP("AT*PCMD=" + mDrone.iVal + ",1,0,0,0,0");
			
			mDrone.AttenteMs(50);
			mDrone.IncrementSeq();
			mDrone.EnvoiTrameUDP("AT*CONFIG="+mDrone.iVal+",\"control:euler_angle_max\",\""+SettingsManager.fAngleMax+"\"");
				
			mDrone.AttenteMs(50);
			mDrone.IncrementSeq();
			mDrone.EnvoiTrameUDP("AT*CONFIG="+mDrone.iVal+",\"control:altitude_max\",\""+SettingsManager.iAltMax+"\"");
				
			/*mDrone.AttenteMs(50);
			mDrone.IncrementSeq();
			mDrone.EnvoiTrameUDP("AT*CONFIG="+mDrone.iVal+",\"control:altitude_min\",\""+ReglageActivity.iAltMin+"\"");*/
				
			mDrone.AttenteMs(50);
			mDrone.IncrementSeq();
			mDrone.EnvoiTrameUDP("AT*CONFIG="+mDrone.iVal+",\"control:control_vz_max\",\""+SettingsManager.iGazSpeed+"\"");
				
			mDrone.AttenteMs(50);
			mDrone.IncrementSeq();
			mDrone.EnvoiTrameUDP("AT*CONFIG="+mDrone.iVal+",\"control:control_yaw\",\""+SettingsManager.fYawSpeed+"\"");
				
			mDrone.AttenteMs(50);
			mDrone.IncrementSeq();
				
		}
	}
	
	class TaskChrono implements Runnable { // Task chronomètre
		@Override
		public void run() {

			if (mDrone.icSecondeU != 9) {
				mDrone.icSecondeU = mDrone.icSecondeU + 1;
			}

			else {
				mDrone.icSecondeU = 0;
				mDrone.icSecondeD = mDrone.icSecondeD + 1;
			}

			if (mDrone.iSecondeU != 10) {

				if ((mDrone.icSecondeU == 9) && (mDrone.icSecondeD == 9)) {

					mDrone.icSecondeU = 0;
					mDrone.icSecondeD = 0;
					mDrone.iSecondeU = mDrone.iSecondeU + 1;
				}
			} else {
				mDrone.iSecondeU = 0;
				mDrone.iSecondeD = mDrone.iSecondeD + 1;
			}

			if (mDrone.iMinuteU != 10) {

				if ((mDrone.iSecondeU == 0) && (mDrone.iSecondeD == 6)) {

					mDrone.iSecondeU = 0;
					mDrone.iSecondeD = 0;
					mDrone.iMinuteU = mDrone.iMinuteU + 1;
				}
			}

			else {
				mDrone.iMinuteU = 0;
				mDrone.iMinuteD = mDrone.iMinuteD + 1;

			}

			mDrone.sTemps = Integer.toString(mDrone.iMinuteD) + Integer.toString(mDrone.iMinuteU)
					+ ":" + mDrone.iSecondeD + mDrone.iSecondeU + ":"
					+ Integer.toString(mDrone.icSecondeD)
					+ Integer.toString(mDrone.icSecondeU);

			mDrone.mJoy.runOnUiThread(new Thread(new Runnable() {
				
				@Override
				public void run() {
					mDrone.mJoy.textView13.setText(mDrone.sTemps);              // affiche le chronomètre
				}

			}));
		}

	}

	    
}
	
	
	


