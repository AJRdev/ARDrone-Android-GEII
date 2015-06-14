package com.project.ardrone_android_geii;	//PROGRAMME AVEC FONCTION


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import com.example.projet_er4_v1.R;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**

 * 

 * Class managing the Main Menu UI of the Application.
 
 */
public class Menu_UI extends Activity {




	private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{
				//Log.i(TAG_OpenCV, "OpenCV loaded successfully");

			} break;
			default:
			{
				super.onManagerConnected(status);
			} break;
			}
		}
	};


	public SQLiteDatabase MaBase;
	public static boolean bVideo = false;
	private static int  iTime = 0;
	private ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
	private PeriodicTask1 Task1 = new PeriodicTask1();
	private int iMode = 2;
	




	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);

		new Thread(new TaskCreate()).start();

		scheduleTaskExecutor.scheduleAtFixedRate(Task1, 0, 1000, TimeUnit.MILLISECONDS);			// LANCE LE THREAD DE LA BASE

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if(id == R.id.videomode){
			if(this.bVideo == false){
				this.bVideo = true;			
			}
			iMode=1;
			Toast.makeText(getApplicationContext(), "Video enabled", Toast.LENGTH_SHORT).show();
			return true;

		}else if(id == R.id.cvmode){

			iMode=0;
			Toast.makeText(getApplicationContext(), "OpenCV enabled", Toast.LENGTH_SHORT).show();

			return true;

		}else if(id == R.id.novideo_mode){
			if(this.bVideo == true){
				this.bVideo = false;

			}
			iMode=2;
			Toast.makeText(getApplicationContext(), "Video disabled", Toast.LENGTH_SHORT).show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private static Handler handlerCreation = new Handler()
	{ 

		public void handleMessage(Message msg1) {
			super.handleMessage(msg1); 

		}
	};

	private static Handler handlerVisualisation = new Handler()		//GESTION DE LA VISUALISATION DES DONNEES
	{ 

		public void handleMessage(Message msg3) {
			super.handleMessage(msg3); 


		}
	};

	private static Handler handlerReinitialisation = new Handler()
	{ 

		public void handleMessage(Message msg4) {
			super.handleMessage(msg4); 

		}
	};

	public void pilotage(View view){  						// BOUTON PILOTAGE


		new Thread(new TaskReinit()).start();

		if(iMode==1 || iMode==2){
			Toast.makeText(getApplicationContext(), "Base réinitialisée", Toast.LENGTH_SHORT).show();
			Intent myIntent = new Intent(Menu_UI.this,Drone_UI.class); 
			startActivityForResult(myIntent,50);

		}else{

			Toast.makeText(getApplicationContext(), "Base réinitialisée", Toast.LENGTH_SHORT).show();
			Intent myIntent = new Intent(Menu_UI.this,DroneUI_OpenCV.class); 

			startActivityForResult(myIntent,50);

		}





	}

	public void visualisation (View view){								// LANCE LE THREAD DE VISUALISATION

		new Thread(new TaskVisu()).start();
	}

	public void reinitialisation(View view){							// REINITIALISE LES DONNEE DE LA BASE

		new Thread(new TaskReinit()).start();

		Toast.makeText(getApplicationContext(), "Base réinitialisée", Toast.LENGTH_LONG).show();
	}

	public void showMessage(String title,String message)				// MONTRE LA FENETRE AVEC LES DONNEE
	{
		Builder builder=new Builder(this);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.show();
	}

	class TaskCreate implements Runnable { 						// CREATION DE LA BASE
		@Override

		public void run() {

			handlerCreation.post(new Runnable() {
				@Override
				public void run() {

					MaBase = openOrCreateDatabase("BoiteNoire", Context.MODE_PRIVATE, null);
					MaBase.execSQL("CREATE TABLE IF NOT EXISTS BoiteNoir(Time INTEGER PRIMARY KEY, Speed INTEGER, " +
							"Battery INTEGER, Altitude INTEGER, Yaw INTEGER, Pitch INTEGER, Roll INTEGER)");

				}
			});


		}
	}

	class TaskVisu implements Runnable {					// THREAD DE VISUALISATION
		@Override

		public void run() {

			handlerVisualisation.post(new Runnable() {
				@Override
				public void run() {

					Cursor c= MaBase.rawQuery("SELECT * FROM BoiteNoir", null);

					if(c.getCount()==0)
					{
						showMessage("Boite Noire vide", "Aucune donnée");
						return;
					}
					StringBuffer buffer=new StringBuffer();
					while(c.moveToNext())
					{
						buffer.append("Time: "+c.getString(0)+" s\n");
						buffer.append("Speed: "+c.getString(1)+"\n");
						buffer.append("Battery: "+c.getString(2)+" %\n");
						buffer.append("Altitude: "+c.getString(3)+" m\n");
						buffer.append("Yaw: "+c.getString(4)+" %\n");
						buffer.append("Pitch: "+c.getString(5)+" %\n");
						buffer.append("Roll: "+c.getString(6)+" %\n");
						buffer.append("\n");

					}
					showMessage("Données de la Boite Noire", buffer.toString());
				}
			});
		}
	}

	class TaskReinit implements Runnable {						//GESTION DE LA REINITIALISATION DES DONNEES 		 	
		@Override

		public void run() {

			handlerReinitialisation.post(new Runnable() {
				@Override
				public void run() {

					MaBase.delete("BoiteNoir", "1", null);
					iTime = 1;
				}
			});
		}
	}

	class PeriodicTask1 implements Runnable {		// REMPLI LA BASE DE DONNEE TOUTE LES SECONDES
		public void run() {     



			if(Drone_UI.bBaseDonnee == true){

				//SEULEMENT SI LE DRONE EST EN VOL

				MaBase.execSQL("INSERT INTO BoiteNoir (Time, Speed, Battery, Altitude, Yaw, Pitch, Roll) " +			//COMMANDE SQL
						"VALUES (" + iTime + ", " + DroneManager.iSpeed + ", " + DroneManager.iBatterie + ", " + DroneManager.iAltitude +
						", " + DroneManager.iYaw + ", " + DroneManager.iPitch + ", " + DroneManager.iRoll + ")");

				iTime++;
			}
			else{											//SEULEMENT SI LE DRONE A ATTERIT
				iTime = 0;
			}
		}
	}
}





