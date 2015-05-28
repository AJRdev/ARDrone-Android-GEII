package com.project.ardrone_android_geii;



import android.util.Log;


public class GereThreadsOpenCV extends GereThreads{

	private String CommandeGenere;

	public GereThreadsOpenCV(GereDroneOpenCV Drone) {
		super(Drone);
		Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : Constructor");
	}

	@Override
	public void StartPilotage() {

		tTaskPilotage = new Thread(new TaskPilotage());
		tTaskPilotage.start();
	}


	class TaskPilotage implements Runnable { 

		// THREAD DE PILOTAGE, LANCE DES L'APPUI SUR LE BOUTON DECOLLAGE
		
		//Controlling the DRONE 
		//AT*PCMD =[Sequence number ],[Flag bit-field],[Roll],[Pitch],[Gaz],[Yaw];
		@Override
		public void run() {			

			Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : TaskPilotage OpenCV Tracking");

			do {
				if(JoyActivityOpenCV.bOpenCV){

				
					Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : TaskPilotage mVideoH :"+JoyActivityOpenCV.mVideoH);
					Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : TaskPilotage Pos Rect y :"+JoyActivityOpenCV.mRectangle.y);

					if(JoyActivityOpenCV.mRectangle.x>(JoyActivityOpenCV.mVideoW/2)+(JoyActivityOpenCV.mVideoW/10)){
						if(JoyActivityOpenCV.mRectangle.y>(JoyActivityOpenCV.mVideoH/2)+(JoyActivityOpenCV.mVideoH/10)){

							CommandeGenere = "AT*PCMD="+mDroneCV.iVal+",1,"+0+","+GereCommande.iM5Pourcents+","+GereCommande.iM50Pourcents+","+GereCommande.i25Pourcents;
							Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : Je descends !");
						}
						else if(JoyActivityOpenCV.mRectangle.y<(JoyActivityOpenCV.mVideoH/2)-(JoyActivityOpenCV.mVideoH/8)){


							CommandeGenere = "AT*PCMD="+mDroneCV.iVal+",1,"+0+","+GereCommande.iM5Pourcents+","+GereCommande.i25Pourcents+","+GereCommande.i25Pourcents;	
							Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : Je monte !");
						}else{
							CommandeGenere = "AT*PCMD="+mDroneCV.iVal+",1,"+0+","+GereCommande.iM5Pourcents+","+0+","+GereCommande.i25Pourcents;
						}

					}
					else if(JoyActivityOpenCV.mRectangle.x<(JoyActivityOpenCV.mVideoW/2)-(JoyActivityOpenCV.mVideoW/10)){


						if(JoyActivityOpenCV.mRectangle.y>(JoyActivityOpenCV.mVideoH/2)+(JoyActivityOpenCV.mVideoH/10)){

							CommandeGenere = "AT*PCMD="+mDroneCV.iVal+",1,"+0+","+GereCommande.iM5Pourcents+","+GereCommande.iM50Pourcents+","+GereCommande.iM25Pourcents;
							Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : Je descends !");
						}
						else if(JoyActivityOpenCV.mRectangle.y<(JoyActivityOpenCV.mVideoH/2)-(JoyActivityOpenCV.mVideoH/8)){


							CommandeGenere = "AT*PCMD="+mDroneCV.iVal+",1,"+0+","+GereCommande.iM5Pourcents+","+GereCommande.i25Pourcents+","+GereCommande.iM25Pourcents;	
							Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : Je monte !");
						}else{
							CommandeGenere = "AT*PCMD="+mDroneCV.iVal+",1,"+0+","+GereCommande.iM5Pourcents+","+0+","+GereCommande.iM25Pourcents;
						}
					}
					else{
						CommandeGenere = "AT*PCMD="+mDroneCV.iVal+",1,"+0+","+0+","+0+",0";
					}	

					mDroneCV.IncrementSeq();
					mDroneCV.EnvoiTrameUDP(CommandeGenere);						
					mDroneCV.AttenteMs(50);
				}else{
					Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : TaskPilotage run bOpenCV");
					//INCREMENT LE NUMERO DE SEQUENCE
					mDroneCV.IncrementSeq();	
					//ASSEMBLAGE DE LA COMMANDE
					mDroneCV.CommandeGenere = "AT*PCMD=" + mDroneCV.iVal + mDroneCV.mJoy.GenereCommande(); 
					//ENVOI DE LA COMMANDE
					mDroneCV.EnvoiTrameUDP(mDroneCV.CommandeGenere);
					//ATTENTE DE 50MS POUR LA FLUIDITE DES MOUVEMENTS
					mDroneCV.AttenteMs(50);
				}
			} while (bWatchDog == false); // PASSE A FALSE LORS DE L'APPUI SUR ATERRISSAGE

			mDroneCV.iVal = 0;

		}

		

		private String DroneControl(String Roll,String Pitch,String Gaz,String Yaw){

			String Command = "AT*PCMD="+mDroneCV.iVal+",1,"+Roll+","+Pitch+","+Gaz+","+Yaw;

			return Command;

		}
		private void PilotageOpenCV(int iWidth_margin,int iHeight_margin){

			

				Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : TaskPilotage run bOpenCV");
				Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : TaskPilotage mVideoH :"+JoyActivityOpenCV.mVideoH);
				Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : TaskPilotage Pos Rect y :"+JoyActivityOpenCV.mRectangle.y);

				if(JoyActivityOpenCV.mRectangle.x>(JoyActivityOpenCV.mVideoW/2)+(JoyActivityOpenCV.mVideoW/10)){
					if(JoyActivityOpenCV.mRectangle.y>(JoyActivityOpenCV.mVideoH/2)+(JoyActivityOpenCV.mVideoH/10)){

						Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : Je descends !");
						CommandeGenere = DroneControl("0",GereCommande.iM5Pourcents,GereCommande.iM50Pourcents,GereCommande.i25Pourcents);


					}
					else if(JoyActivityOpenCV.mRectangle.y<(JoyActivityOpenCV.mVideoH/2)-(JoyActivityOpenCV.mVideoH/8)){

						Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : Je monte !");
						CommandeGenere = DroneControl("0",GereCommande.iM5Pourcents,GereCommande.i25Pourcents,GereCommande.i25Pourcents);

					}else{
						CommandeGenere = DroneControl("0",GereCommande.iM5Pourcents,"0",GereCommande.i25Pourcents);

					}

				}
				else if(JoyActivityOpenCV.mRectangle.x<(JoyActivityOpenCV.mVideoW/2)-(JoyActivityOpenCV.mVideoW/10)){
					if(JoyActivityOpenCV.mRectangle.y>(JoyActivityOpenCV.mVideoH/2)+(JoyActivityOpenCV.mVideoH/10)){

						Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : Je descends !");
						CommandeGenere = DroneControl("0",GereCommande.iM5Pourcents,GereCommande.iM50Pourcents,GereCommande.iM25Pourcents);


					}
					else if(JoyActivityOpenCV.mRectangle.y<(JoyActivityOpenCV.mVideoH/2)-(JoyActivityOpenCV.mVideoH/8)){


						CommandeGenere = DroneControl("0",GereCommande.iM5Pourcents,GereCommande.i25Pourcents,GereCommande.iM25Pourcents);

						Log.v(JoyActivityOpenCV.TAG, "GereThreadsOpenCV : Je monte !");
					}else{
						CommandeGenere = DroneControl("0",GereCommande.iM5Pourcents,"0",GereCommande.iM25Pourcents);
					}
				}
				else{
					CommandeGenere = DroneControl("0","0","0","0");
				}

			
		}
	}
	public void StartDonnee() {

		tTaskDonnee = new Thread(new TaskDonnee());
		tTaskDonnee.start();

	}

	public void StartReglage() {
		tTaskReglage = new Thread(new TaskReglage());
		tTaskReglage.start();

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

			mDroneCV.IncrementSeq();


			mDroneCV.AttenteMs(50);
			mDroneCV.EnvoiTrameUDP("AT*FTRIM=1\rAT*REF=2,290718208"); // TRIM + DECOLLAGE



			mDroneCV.iVal = 3;

			mDroneCV.AttenteMs(50);

		}

	}

	class TaskEmergency implements Runnable { // THREAD D'ATTERISSAGE D'URGENCE
		@Override
		public void run() {

			mDroneCV.IncrementSeq();
			mDroneCV.EnvoiTrameUDP("AT*REF=" + mDroneCV.iVal + ",290717952"); // Arret / d'urgence des moteurs

			mDroneCV.AttenteMs(50);

		}
	}

	class TaskAtterissage implements Runnable { // THREAD D'ATTERISSAGE
		@Override
		public void run() {
			mDroneCV.IncrementSeq();
			mDroneCV.EnvoiTrameUDP("AT*REF=" + mDroneCV.iVal + ",290717696"); // ATTERISSAGE

			mDroneCV.AttenteMs(50);

		}
	}

	class TaskWatchDog implements Runnable { // THREAD WATCHDOG
		@Override
		public void run() {


			do {
				mDroneCV.IncrementSeq();

				mDroneCV.EnvoiTrameUDP("AT*PCMD=" + mDroneCV.iVal + ",0,0,0,0,0"); 			// RESET WATCHDOG pour eviter le TimeOut


				mDroneCV.AttenteMs(500);
			} while (bWatchDog == true);

		}
	}



	public class TaskDonnee implements Runnable { // THREAD RECUPERATION ET AFFICHAGE DES DONNEE

		@Override
		public void run() {

			mDroneCV.NavRec.InitNavData();

			do {

				mDroneCV.NavRec.ReceiveNavdata();
				mDroneCV.NavRec.NoSleep();

				GereDrone.iBatterie = mDroneCV.NavRec.GetBattery();
				GereDrone.iAltitude = mDroneCV.NavRec.GetAltitude();			
				GereDrone.iYaw = mDroneCV.NavRec.GetYaw();
				GereDrone.iPitch = mDroneCV.NavRec.GetPitch();
				GereDrone.iRoll = mDroneCV.NavRec.GetRoll();
				GereDrone.iSpeed = mDroneCV.NavRec.GetSpeed();
				mDroneCV.AttenteMs(30);


			} while (isDonnee == true);
		}

	}

	class TaskReglage implements Runnable { // THREAD DE REGLAGE
		@Override
		public void run() {

			mDroneCV.AttenteMs(50);
			mDroneCV.IncrementSeq();
			mDroneCV.EnvoiTrameUDP("AT*PCMD=" + mDroneCV.iVal + ",1,0,0,0,0");

			mDroneCV.AttenteMs(50);
			mDroneCV.IncrementSeq();
			mDroneCV.EnvoiTrameUDP("AT*CONFIG="+mDroneCV.iVal+",\"control:euler_angle_max\",\""+ReglageActivity.fAngleMax+"\"");

			mDroneCV.AttenteMs(50);
			mDroneCV.IncrementSeq();
			mDroneCV.EnvoiTrameUDP("AT*CONFIG="+mDroneCV.iVal+",\"control:altitude_max\",\""+ReglageActivity.iAltMax+"\"");

			mDroneCV.AttenteMs(50);
			mDroneCV.IncrementSeq();
			mDroneCV.EnvoiTrameUDP("AT*CONFIG="+mDroneCV.iVal+",\"control:control_vz_max\",\""+ReglageActivity.iGazSpeed+"\"");

			mDroneCV.AttenteMs(50);
			mDroneCV.IncrementSeq();
			mDroneCV.EnvoiTrameUDP("AT*CONFIG="+mDroneCV.iVal+",\"control:control_yaw\",\""+ReglageActivity.fYawSpeed+"\"");

			mDroneCV.AttenteMs(50);
			mDroneCV.IncrementSeq();

		}
	}

	class TaskChrono implements Runnable { // Task chronomètre
		@Override
		public void run() {

			if (mDroneCV.icSecondeU != 9) {
				mDroneCV.icSecondeU = mDroneCV.icSecondeU + 1;
			}

			else {
				mDroneCV.icSecondeU = 0;
				mDroneCV.icSecondeD = mDroneCV.icSecondeD + 1;
			}

			if (mDroneCV.iSecondeU != 10) {

				if ((mDroneCV.icSecondeU == 9) && (mDroneCV.icSecondeD == 9)) {

					mDroneCV.icSecondeU = 0;
					mDroneCV.icSecondeD = 0;
					mDroneCV.iSecondeU = mDroneCV.iSecondeU + 1;
				}
			} else {
				mDroneCV.iSecondeU = 0;
				mDroneCV.iSecondeD = mDroneCV.iSecondeD + 1;
			}

			if (mDroneCV.iMinuteU != 10) {

				if ((mDroneCV.iSecondeU == 0) && (mDroneCV.iSecondeD == 6)) {

					mDroneCV.iSecondeU = 0;
					mDroneCV.iSecondeD = 0;
					mDroneCV.iMinuteU = mDroneCV.iMinuteU + 1;
				}
			}

			else {
				mDroneCV.iMinuteU = 0;
				mDroneCV.iMinuteD = mDroneCV.iMinuteD + 1;

			}

			mDroneCV.sTemps = Integer.toString(mDroneCV.iMinuteD) + Integer.toString(mDroneCV.iMinuteU)
					+ ":" + mDroneCV.iSecondeD + mDroneCV.iSecondeU + ":"
					+ Integer.toString(mDroneCV.icSecondeD)
					+ Integer.toString(mDroneCV.icSecondeU);

			mDroneCV.mJoy.runOnUiThread(new Thread(new Runnable() {

				@Override
				public void run() {
					mDroneCV.mJoy.textView13.setText(mDroneCV.sTemps);              // affiche le chronomètre
				}

			}));
		}

	}

}
