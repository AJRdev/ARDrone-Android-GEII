package com.example.projet_er4_v1;	//PROGRAMME AVEC FONCTION


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.projet_er4_v1.JoyActivityVideo.*;

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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class ReglageActivity extends Activity {
	 
	
	private SeekBar SeekBarAngleMax;
	private SeekBar seekBarAltMax;
	private SeekBar seekBarAltMin;
	private SeekBar seekBarGazSpeed;
	private SeekBar seekBarYawSpeed;
	
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private TextView textView4;
	private TextView textView5;
    
   static public float fAngleMax;
   static public int iAltMax;
   static public int iAltMin;
   static public int iGazSpeed;
   static public float fYawSpeed;
   
   int progress1 = 0;
   int progress2 = 0;
   int progress3 = 0;
   int progress4 = 0;
   int progress5 = 0;
   
   boolean bReglage;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.reglage);
        
        bReglage = true;
    
     
        
        SeekBarAngleMax = (SeekBar) findViewById(R.id.seekBar1);
        seekBarAltMax = (SeekBar) findViewById(R.id.seekBar2);
        seekBarAltMin = (SeekBar) findViewById(R.id.seekBar3);
        seekBarGazSpeed = (SeekBar) findViewById(R.id.seekBar4);
        seekBarYawSpeed = (SeekBar) findViewById(R.id.seekBar5);
        
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        
        
         SeekBarAngleMax.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            	 
         @Override
         			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
        	 progress1 = progresValue;}
         @Override
         			public void onStartTrackingTouch(SeekBar seekBar) {}
         @Override
         			public void onStopTrackingTouch(SeekBar seekBar) {}});
            
            seekBarAltMax.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
          	
       @Override
       			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
	        	 		progress2 = progresValue;}
       @Override
       			public void onStartTrackingTouch(SeekBar seekBar) {}
       @Override
       			public void onStopTrackingTouch(SeekBar seekBar) {}});
            
            seekBarAltMin.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
          	
       @Override
       			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
	        	 		progress3 = progresValue;}
       @Override
       			public void onStartTrackingTouch(SeekBar seekBar) {}
       @Override
       			public void onStopTrackingTouch(SeekBar seekBar) {}});

            seekBarGazSpeed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            	 
         @Override
         			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
  	        	 		progress4 = progresValue;}
         @Override
         			public void onStartTrackingTouch(SeekBar seekBar) {}
         @Override
         			public void onStopTrackingTouch(SeekBar seekBar) {}});

            seekBarYawSpeed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
          	
       			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
	        	 		progress5 = progresValue;}
       @Override
       			public void onStartTrackingTouch(SeekBar seekBar) {}
       @Override
       			public void onStopTrackingTouch(SeekBar seekBar) {}});
            
          
            SeekBarAngleMax.setProgress(10);
            seekBarAltMax.setProgress(3000);
            seekBarAltMin.setProgress(50);
            seekBarGazSpeed.setProgress(50);
            seekBarYawSpeed.setProgress(48);
            
            new Thread(new TaskAffDonnee()).start();	
    }

    public void retour(View view){
    	
    	this.finish();
    	bReglage = false;
    }
    
    public void FctDefault (View view){
    	
    	
    	SeekBarAngleMax.setProgress(15);
        seekBarAltMax.setProgress(3000);
        seekBarAltMin.setProgress(100);
        seekBarGazSpeed.setProgress(50);
        seekBarYawSpeed.setProgress(50);
    	
    	
    }
    
    class TaskAffDonnee implements Runnable { // THREAD RECUPERATION ET AFFICHAGE DES DONNEE
		
		@Override
		public void run() {



			do {
				
				  fAngleMax = (float) ((SeekBarAngleMax.getProgress())*(2*Math.PI)/360);
				  iAltMin = seekBarAltMin.getProgress()*10;
				  iGazSpeed = (2000*(seekBarGazSpeed.getProgress())/100);
				  fYawSpeed = (float) (6.11*(seekBarYawSpeed.getProgress()+12)/100) ;
				  iAltMax = seekBarAltMax.getProgress()+500;
				   
				   if(iAltMax >=5000 ){
					  iAltMax = 10000;
					}
				
				runOnUiThread(new Thread(new Runnable() { // ON LES AFFICHE DANS  LE UI THREAD
							@Override
							public void run() {
					
					            textView1.setText(Integer.toString(SeekBarAngleMax.getProgress())+"° / " + Integer.toString(SeekBarAngleMax.getMax())+"°");
					            textView2.setText(Integer.toString(seekBarAltMax.getProgress()+500)+"mm / " + Integer.toString(seekBarAltMax.getMax()+500)+"mm");
					            textView3.setText(Integer.toString(seekBarAltMin.getProgress())+"cm / " + Integer.toString(seekBarAltMin.getMax())+"cm");
					            textView4.setText(Integer.toString(seekBarGazSpeed.getProgress()+10)+"% / " + Integer.toString(seekBarGazSpeed.getMax()+10)+"%");
					            textView5.setText(Integer.toString(seekBarYawSpeed.getProgress()+12)+"% / " + Integer.toString(seekBarYawSpeed.getMax()+12)+"%");
					            
							}}));
				
				
				try {
					TimeUnit.MILLISECONDS.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (bReglage == true);
		}

	}

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    

	



  
     
   
 }
    
    
   


