package com.example.projet_er4_v1;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;

public class JoyActivityVideo extends Activity implements SurfaceHolder.Callback{


	GereDrone Drone;			// DECLARATIONS
	GereCommande Commande;
	GereManette Manette;
	JoystickView joy1;
	JoystickView joy2;
	GereVideo mVideo;


	public static String sData;
	public String CommandeGenere = "";
	public String path = "tcp://192.168.1.1:5555/";

	boolean bBouton1 = false;     
	boolean bBouton2 = false;
	Boolean bReglage = false;
	boolean bAppui = false;
	public static boolean bBaseDonnee = false;


	int iTouche;
	int iPad;

	double dJ1X = 0,dJ1Y = 0, dJ2X = 0,dJ2Y = 0;



	TextView textView1;
	TextView textView2;
	TextView textView3;
	TextView textView4;
	TextView textView5;
	TextView textView6;
	TextView textView13;



	Byte buffer;

	JoystickMovedListener listener1;
	JoystickMovedListener listener2;
	DisplayMetrics metrics;
	SurfaceHolder holder;

	private SurfaceView mPreview;



	public final int iBatterieMin = 10;
	public final int iPort = 5556;
	public final String AdresseDrone = "192.168.1.1";
	public final String CommandeDepart = "COMMANDE_INUTILE";




	public JoyActivityVideo() {

		Drone = new GereDrone(CommandeDepart, AdresseDrone, iPort, this);		// INSTANTIATION D'UN DRONE
		Commande = new GereCommande();
		Manette = new GereManette();


		Commande.roll = "0";
		Commande.pitch = "0";
		Commande.yaw = "0";
		Commande.gaz = "0";


		listener1 = new JoystickMovedListener() {

			@Override
			public void OnMoved(double x, double y) {
				// TODO Auto-generated method stub

			}

			@Override
			public void OnReleased() {
				// TODO Auto-generated method stub
				Commande.roll = "0";
				Commande.pitch = "0";

			}
		};


		listener2 = new JoystickMovedListener() {

			@Override
			public void OnMoved(double x, double y) {
				// TODO Auto-generated method stub
			}

			@Override
			public void OnReleased() {
				// TODO Auto-generated method stub
				Commande.gaz = "0";
				Commande.yaw = "0";

			}

		};
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate (savedInstanceState);

		if (!LibsChecker.checkVitamioLibs(this))
			return;


		if (Menu_Activity.bVideo == true){
			setContentView(R.layout.joy_video);
		}
		else {
			setContentView(R.layout.joy_main);
		}



		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ActionBar actionBar = getActionBar();
		actionBar.hide();


		if (Menu_Activity.bVideo == true){

			mVideo = new GereVideo (this);
			mPreview = (SurfaceView) findViewById(R.id.surface);
			holder = mPreview.getHolder();
			holder.addCallback(this);
			holder.setFormat(PixelFormat.RGBA_8888);
			metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);




		}



		joy1 = (JoystickView) findViewById(R.id.joystickView01); // Création des joystick et association avec leurs listener																
		joy1.setOnJostickMovedListener(listener1);

		joy2 = (JoystickView) findViewById(R.id.joystickView02);
		joy2.setOnJostickMovedListener(listener2);


		textView1 = (TextView) findViewById(R.id.textView1); 	//INSTANCIATION TEXTVIEWS
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		textView4 = (TextView) findViewById(R.id.textView4);
		textView5 = (TextView) findViewById(R.id.textView5);
		textView6 = (TextView) findViewById(R.id.textView6);
		textView13 = (TextView) findViewById(R.id.textView13);




	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	protected void onPause() {
		
		
		if (Menu_Activity.bVideo){
			mVideo.releaseMediaPlayer();

		}
		this.finish();

		super.onPause();


		
	}


	@Override
	protected void onResume(){


		if (Menu_Activity.bVideo == true){
			mVideo.playVideo();
		}

		new Thread(new TaskAffDonnee()).start();

		if (bReglage == true){

			Drone.mThreads.StartReglage();
			Toast.makeText(getApplicationContext(), "Réglages envoyés",Toast.LENGTH_SHORT).show();
			bReglage = false;
		}
		super.onResume();

	}

	@Override
	protected void onDestroy() {

		if (Menu_Activity.bVideo == true){
			mVideo.releaseMediaPlayer();
		}
		this.finish();

		super.onDestroy();		
	}

	public void decollage(View view) { // BOUTTON DECOLLAGE, LANCE LE THREAD DE DECOLLAGE ET DE PILOTAGE

		if(Drone.mThreads.bWatchDog == true) {

			Commande.GeneratePCMD("0");								 
			bBaseDonnee = true;

			if (Drone.iBatterie >=0) { // ON NE DECOLLE QUE SI LA BATTERIE EST  SUFFISAMENT CHARGE

				Drone.decollage();
				Toast.makeText(getApplicationContext(),	"Début de l'enregistrement", Toast.LENGTH_SHORT).show();


			} else { 											// SINON ON AFFICHE UN TOAST D'AVERTISSEMENT

				Toast.makeText(getApplicationContext(), "Recharger la batterie",Toast.LENGTH_SHORT).show();

			}
		}
		else {

			Toast.makeText(getApplicationContext(), "vous etes déja en vol !",Toast.LENGTH_SHORT).show();

		}

	}

	public void reglage(View view){

		Intent myIntent = new Intent(JoyActivityVideo.this,ReglageActivity.class); 
		startActivityForResult(myIntent,53);
		bReglage = true;

	}

	public void atterissage(View view) { // ATTERIT ET ARRETE LE THREAD DE PILOTAGE, LE REMPLACE PAR LE WATCHDOG

		if(Drone.mThreads.bWatchDog == false) {
			Drone.atterissage();
			bBaseDonnee = false;
			Toast.makeText(getApplicationContext(), "Fin de l'enregistrement",Toast.LENGTH_SHORT).show();
		}
		else {

			Toast.makeText(getApplicationContext(), "Le drone n'est pas en vol",Toast.LENGTH_SHORT).show();

		}

	}

	public void emergency(View view) {				// boutton atterissage d'urgence

		Drone.emergency();
		bBaseDonnee = false;
		Toast.makeText(getApplicationContext(), "Atterissage d'urgence",Toast.LENGTH_SHORT).show();

	}

	public void CapturePhoto(View v){
		try {
			new PhotoSaver(this,mVideo.mMediaPlayer).record();
		}
		catch (Exception e) {

			Toast.makeText(getApplicationContext(), "erreur photo",Toast.LENGTH_SHORT).show();
		}

	}

	class TaskAffDonnee implements Runnable { // THREAD RECUPERATION ET AFFICHAGE DES DONNEE

		@Override
		public void run() {
			do{
				Drone.AttenteMs(30);
				runOnUiThread(new Thread(new Runnable() { // ON LES AFFICHE DANS  LE UI THREAD


					@Override
					public void run() {

						textView2.setText(Integer.toString(GereDrone.iPitch));
						textView1.setText(Integer.toString(GereDrone.iAltitude));
						textView3.setText(Integer.toString(GereDrone.iBatterie));
						textView4.setText(Integer.toString(GereDrone.iYaw));
						textView5.setText(Integer.toString(GereDrone.iRoll));
						textView6.setText(Integer.toString(GereDrone.iSpeed));



					}

				}));

			} while (Drone.mThreads.isDonnee == true);
		}


	}



	public String GenereCommande(){


		if ((dJ1X < 0.5) && (dJ1X>-0.5) && (dJ1Y< 0.5)&& (dJ1Y>-0.5)){  


			if ((joy1.dX > 0.5) || (joy1.dX<-0.5) || (joy1.dY> 0.5) || (joy1.dY<-0.5)) {
				Commande.GenerateCMDJoy1(joy1.dX, joy1.dY);
			}
			else if (bBouton1 == false){

				Commande.GenerateCMDJoy1(0, 0);
			}
			else {

			}
		}
		else {
			Commande.GenerateCMDJoy1(dJ1X, dJ1Y);
		}


		if ((dJ2X < 0.5) && (dJ2X>-0.5) && (dJ2Y< 0.5)&& (dJ2Y>-0.5)){

			if ((joy2.dX > 0.5) || (joy2.dX<-0.5) || (joy2.dY> 0.5) || (joy2.dY<-0.5)) {

				Commande.GenerateCMDJoy2(joy2.dX, joy2.dY);
			}
			else if (bBouton2 == false){

				Commande.GenerateCMDJoy2(0, 0);
			}
			else {

			}

		}
		else {

			Commande.GenerateCMDJoy2(dJ2X, dJ2Y);


		}
		sData = Commande.GeneratePCMD("1");
		return sData;

	}










	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}





	//						****GESTION DE LA MANETTE****

	@Override
	public boolean dispatchKeyEvent(KeyEvent keyEvent) {

		// BOOLEEN QUI RECUPERE L'INFORMATION: "UTILISATEUR A APPUIE SUR UNE TOUCHE OU L'A RELACHE"
		// TRUE = TOUCHE APPUIEE  |  FALSE = TOUCHE RELACHEE
		bAppui = Manette.GetKeyState(keyEvent);
		iTouche = Manette.GetKey(keyEvent);



		if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) { 

			if (Menu_Activity.bVideo == true){
				mVideo.releaseMediaPlayer();
			}


			super.onBackPressed();
		}
		else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_HOME) {    


			if (Menu_Activity.bVideo == true){
				mVideo.releaseMediaPlayer();
			}

		}



		// SI L'UTILISATEUR APPUIE SUR UNE TOUCHE
		if(bAppui)
		{
			bBouton2 = true;
			switch(iTouche)
			{
			case 1:			// APPUI A


				Commande.gaz = Commande.iM50Pourcents;


				break;

			case 2:			// APPUI B
				Commande.yaw = Commande.i50Pourcents;
				break;

			case 3:			// APPUI X
				Commande.yaw = Commande.iM50Pourcents;
				break;

			case 4:			// APPUI Y
				Commande.gaz = Commande.i50Pourcents;
				break;

			case 5:		// APPUI START
				this.emergency(null);
				break;

			case 6:		// APPUI SELECT
				this.CapturePhoto(null);
				break;

			case 7:			// APPUI R1

				this.decollage(null);

				break;

			case 8:			// APPUI L1

				this.atterissage(null);

				break;

			case 9:			// APPUI R2

				Commande.gaz = Commande.i50Pourcents;
				break;

			case 10:			// APPUI L2
				Commande.gaz = Commande.iM50Pourcents;
				break;

			}

		}

		// SI L'UTILISATEUR RELACHE LA TOUCHE
		else
		{
			bBouton2 = false;
			switch(iTouche)
			{
			case 1:			// RELACHE A
				Commande.gaz = "0";
				break;

			case 2:			// RELACHE B
				Commande.yaw = "0";
				break;

			case 3:			// RELACHE X
				Commande.yaw = "0";
				break;

			case 4:			// RELACHE Y
				Commande.gaz = "0";
				break;

			case 5:		// RELACHE START

				break;

			case 6:		// RELACHE SELECT

				break;

			case 7:			// RELACHE R1

				break;

			case 8:			// RELACHE L1

				break;

			case 9:			// RELACHE R2

				Commande.gaz = "0";
				break;

			case 10:			// RELACHE L2
				Commande.gaz = "0";
				break;
			}




		}





		return true;
	}
	// LISTENER POUR LES JOYSTICKS ET LE PAVE DIRECTIONNEL

	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent motionEvent){		// FONCTION S'APPELLANT pour le pad et les joy

		// RECUPERATION DE LA DIRECTION PRESSEE
		iPad = Manette.GetDPad(motionEvent);

		switch(iPad)
		{
		case 1:		// APPUI HAUT
			Commande.pitch = Commande.iM50Pourcents;

			bBouton1 = true;
			break;

		case 2:			// APPUI DROITE		
			Commande.roll = Commande.i50Pourcents;	
			bBouton1 = true;
			break;

		case 3:		// APPUI BAS			
			Commande.pitch = Commande.i50Pourcents;

			bBouton1 = true;
			break;

		case 4:		// APPUI GAUCHE		
			Commande.roll = Commande.iM50Pourcents;	
			bBouton1 = true;
			break;

		default :		// RELACHEMENT
			Commande.pitch = "0";
			Commande.roll = "0";
			bBouton1 = false;
			break;

		}

		// RECUPERE LES COORDONNEES DES 2 JOYSTICKS


		// JOYSTICK 1
		dJ1X = Manette.GetJ1PosX(motionEvent);
		dJ1Y = Manette.GetJ1PosY(motionEvent);
		// JOYSTICK 2
		dJ2X = Manette.GetJ2PosX(motionEvent);
		dJ2Y = Manette.GetJ2PosY(motionEvent);

		if ((dJ1X < 0.5) && (dJ1X>-0.5) && (dJ1Y< 0.5)&& (dJ1Y>-0.5)){ 

			dJ1X = 0;

		}
		if ((dJ2X < 0.5) && (dJ2X>-0.5) && (dJ2Y< 0.5)&& (dJ2Y>-0.5)){

			dJ2X = 0;

		}

		return true;
	}

}

