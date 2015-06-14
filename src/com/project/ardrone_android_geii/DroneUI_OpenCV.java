package com.project.ardrone_android_geii;

import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.example.projet_er4_v1.R;

import io.vov.vitamio.LibsChecker;
import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**

 * 

 * Class derived from Drone_UI managing the Drone Piloting Activity UI with OpenCV features enabled.
 
 */
public class DroneUI_OpenCV extends Drone_UI {

	public static final String TAG = "MediaPlayerDemo";
	static final String TAG_OpenCV = "OPENCV DEBUG";


	private SurfaceTexture mSurfaceTexture;
	private Surface mSurface;


	private VideoManager_OpenCV mVideoOpenCV;


	private ColorDetector_OpenCV mDetector;
	private Mat mSpectrum;
	private Scalar mBlobColorRgba;
	private Scalar mBlobColorHsv;
	private Size SPECTRUM_SIZE;
	private Scalar CONTOUR_COLOR;
	public Mat mRgba;

	private int mVideoWidth;

	public boolean mIsColorSelected;

	public Bitmap video;
	public int iPercentBuff;
	public Mat modified;

	public static int mVideoH;
	public static Rect mRectangle;
	private TextureView mTextureView;
	public TextView mtV_video;

	DroneManager_OpenCV DroneCV;

	static boolean bOpenCV= false;
	static int iCoorRec;
	public static int mVideoW;


	public DroneUI_OpenCV(){

		DroneCV = new DroneManager_OpenCV(CommandeDepart, AdresseDrone, iPort, this);
		Commande = new JoystickCommandManager();
		Manette = new PhysicalJoystickManager();


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



		setContentView(R.layout.joy_opencv);


		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ActionBar actionBar = getActionBar();
		actionBar.hide();


		mVideoOpenCV = new VideoManager_OpenCV(this);


		mTextureView = (TextureView) findViewById(R.id.texture_view1);   	
		mTextureView.setSurfaceTextureListener(new CanvasListener());
		mTextureView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub


				if(video != null){

					if(iPercentBuff < 10){
						int cols = mRgba.cols();
						Log.v(TAG_OpenCV, "ON METHOD TOUCH START");	


						int rows = mRgba.rows();

						Log.v(TAG_OpenCV, "ON METHOD TOUCH START 2");	

						//float xOffset = ((float)cols/(float)mVideoWidth);
						//float yOffset = ((float)rows/(float)mVideoHeight);

						Log.v(TAG_OpenCV, "ON METHOD TOUCH START 3");	
						//Log.i(TAG_OpenCV, "Method Ontouch : Offset x and y (" + xOffset + ", " + yOffset + ")");
						Log.i(TAG_OpenCV, "Method Ontouch : Touch image coordinates before Offset: (" + (int)event.getX() + ", " + (int)event.getY() + ")");

						//int x = (int) (event.getX() * xOffset); //MISE A L'ECHELLE POUR LES COORDONNES
						//int y = (int) (event.getY() * yOffset); //D'APPUI

						int x = (int) (event.getX() ); //MISE A L'ECHELLE POUR LES COORDONNES
						int y = (int) (event.getY() );
						
						Log.i(TAG_OpenCV, "Method Ontouch : Cols and Rows: (" + cols + ", " + rows + ")");
						Log.i(TAG_OpenCV, "Method Ontouch : Touch image coordinates after Offset: (" + x + ", " + y + ")");

						if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;

						org.opencv.core.Rect touchedRect = new org.opencv.core.Rect();

						touchedRect.x = (x>4) ? x-4 : 0;
						touchedRect.y = (y>4) ? y-4 : 0;

						touchedRect.width = (x+4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
						touchedRect.height = (y+4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;

						Mat touchedRegionRgba = mRgba.submat(touchedRect);
						Bitmap test = Bitmap.createBitmap(touchedRegionRgba.cols(), touchedRegionRgba.rows(), Bitmap.Config.ARGB_8888);
						Utils.matToBitmap(touchedRegionRgba, test);



						Mat touchedRegionHsv = new Mat();
						Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL); 

						// Calculate average color of touched region
						mBlobColorHsv = Core.sumElems(touchedRegionHsv);
						int pointCount = touchedRect.width*touchedRect.height;
						for (int i = 0; i < mBlobColorHsv.val.length; i++)
							mBlobColorHsv.val[i] /= pointCount;

						mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);

						Log.i(TAG_OpenCV, "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
								", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");

						mDetector.setHsvColor(mBlobColorHsv);

						Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE);

						mIsColorSelected = true;



						touchedRegionRgba.release();
						touchedRegionHsv.release();

						Log.v(TAG_OpenCV, "METHOD ON TOUCH apres traitement");
					}


				}
				return false;
			}
		});


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
		mtV_video = (TextView) findViewById(R.id.tCV);




	}


	private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
		Mat pointMatRgba = new Mat();
		Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
		Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);
		Log.v(TAG_OpenCV, "FUNCTION SCALARHsv");
		return new Scalar(pointMatRgba.get(0, 0));
	}



	private class OpenCVAsyncTask extends AsyncTask {


		@Override
		protected Object doInBackground(Object... params) {

			Canvas a = mTextureView.lockCanvas(null);




			modified = this.getMatFromFrame();
			Bitmap bVBitmap = Bitmap.createBitmap(modified.cols(), modified.rows(), video.getConfig());
			Utils.matToBitmap(modified, bVBitmap);

			a.drawBitmap(bVBitmap, 0, 0, null);
			mTextureView.unlockCanvasAndPost(a);
			return null;
		}

		private Mat getMatFromFrame(){

			mDetector.process(mRgba);
			List<MatOfPoint> contours = mDetector.getContours();

			if(contours.size() != 0){
				mRectangle = Imgproc.boundingRect(contours.get(0));
			}
			//Log.e(TAG_OpenCV, "Contour Rec x and y : " + mRectangle.x + "," + mRectangle.y);

			Log.e(TAG_OpenCV, "Contours count: " + contours.size());
			Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);



			return mRgba;

		}

	}

	@Override
	public void decollage(View view) { // BOUTTON DECOLLAGE, LANCE LE THREAD DE DECOLLAGE ET DE PILOTAGE

		if(DroneCV.mThreadsCV.bWatchDog == true) {

			Commande.GeneratePCMD("0");								 
			bBaseDonnee = true;

			if (DroneCV.iBatterie >=0) { // ON NE DECOLLE QUE SI LA BATTERIE EST  SUFFISAMENT CHARGE

				DroneCV.decollage();
				Toast.makeText(getApplicationContext(),	"Début de l'enregistrement", Toast.LENGTH_SHORT).show();


			} else { 											// SINON ON AFFICHE UN TOAST D'AVERTISSEMENT

				Toast.makeText(getApplicationContext(), "Recharger la batterie",Toast.LENGTH_SHORT).show();

			}
		}
		else {

			Toast.makeText(getApplicationContext(), "vous etes déja en vol !",Toast.LENGTH_SHORT).show();

		}

	}

	@Override
	public void atterissage(View view) { // ATTERIT ET ARRETE LE THREAD DE PILOTAGE, LE REMPLACE PAR LE WATCHDOG

		if(DroneCV.mThreadsCV.bWatchDog == false) {
			DroneCV.atterissage();
			bBaseDonnee = false;
			Toast.makeText(getApplicationContext(), "Fin de l'enregistrement",Toast.LENGTH_SHORT).show();
		}
		else {

			Toast.makeText(getApplicationContext(), "Le drone n'est pas en vol",Toast.LENGTH_SHORT).show();

		}

	}

	public void OpenCV_Track(View v){
		if(bOpenCV){
			bOpenCV =false;

		}else{
			bOpenCV=true;
		}

	}

	@Override
	protected void onPause() {

		super.onPause();
		mVideoOpenCV.releaseMediaPlayer();

		if(mRgba != null && mSpectrum != null){
			mRgba.release();
			mSpectrum.release();
		}





	}

	@Override
	protected void onResume(){


		super.onResume();

		mDetector = new ColorDetector_OpenCV();
		mSpectrum = new Mat();
		mBlobColorRgba = new Scalar(255);
		mBlobColorHsv = new Scalar(255);
		SPECTRUM_SIZE = new Size(200, 64);
		CONTOUR_COLOR = new Scalar(255,0,0,255);

		mVideoOpenCV.playVideo();



		new Thread(new TaskAffDonnee()).start();

		if (bReglage == true){

			DroneCV.mThreadsCV.StartReglage();
			Toast.makeText(getApplicationContext(), "Réglages envoyés",Toast.LENGTH_SHORT).show();
			bReglage = false;
		}


	}

	@Override
	protected void onDestroy() {

		super.onDestroy();	
		mVideoOpenCV.releaseMediaPlayer();

		if(mRgba != null && mSpectrum != null){
			mRgba.release();
			mSpectrum.release();
		}




	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent keyEvent) {

		// BOOLEEN QUI RECUPERE L'INFORMATION: "UTILISATEUR A APPUIE SUR UNE TOUCHE OU L'A RELACHE"
		// TRUE = TOUCHE APPUIEE  |  FALSE = TOUCHE RELACHEE
		bAppui = Manette.GetKeyState(keyEvent);
		iTouche = Manette.GetKey(keyEvent);



		if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) { 


			mVideoOpenCV.releaseMediaPlayer();



			super.onBackPressed();
		}
		else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_HOME) {    



			mVideoOpenCV.releaseMediaPlayer();


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

	private class CanvasListener implements SurfaceTextureListener {




		private int mVideoHeight;

		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture surface,
				int width, int height) {
			mVideoWidth = width;
			mVideoHeight = height;
			Log.d(TAG, "ANDRE onSurfaceTextureAvailable");
			Log.v(TAG, "SurfaceHeight and SurfaceWidth: (" + height + ", " + width + ")");
			mSurfaceTexture = surface;
			mSurface = new Surface(mSurfaceTexture);





		}

		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
			Log.d(TAG, "onSurfaceTextureDestroyed");

			return true;
		}

		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture surface,
				int width, int height) {
			Log.i(TAG, "onSurfaceTextureSizeChanged");
		}

		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture surface) {
			Log.i(TAG, "onSurfaceTextureUpdated");
		}

	}

	class TaskAffDonnee implements Runnable { // THREAD RECUPERATION ET AFFICHAGE DES DONNEE

		@Override
		public void run() {
			do{
				DroneCV.AttenteMs(30);
				runOnUiThread(new Thread(new Runnable() { // ON LES AFFICHE DANS  LE UI THREAD


					@Override
					public void run() {

						textView2.setText(Integer.toString(DroneManager_OpenCV.iPitch));
						textView1.setText(Integer.toString(DroneManager_OpenCV.iAltitude));
						textView3.setText(Integer.toString(DroneManager_OpenCV.iBatterie));
						textView4.setText(Integer.toString(DroneManager_OpenCV.iYaw));
						textView5.setText(Integer.toString(DroneManager_OpenCV.iRoll));
						textView6.setText(Integer.toString(DroneManager_OpenCV.iSpeed));



					}

				}));

			} while (DroneCV.mThreadsCV.isDonnee == true);
		}
	}

	public void StartAsyncTask() {
		new OpenCVAsyncTask().execute();

	}

}
