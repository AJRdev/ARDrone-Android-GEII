package com.project.ardrone_android_geii;

import io.vov.vitamio.MediaPlayer;

import java.io.IOException;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import android.os.AsyncTask;
import android.util.Log;

/**

 * 

 * Class derived from VideoManager managing the video stream coming from the Drone with OpenCv features enabled.
 
 */
public class VideoManager_OpenCV extends VideoManager {

	DroneUI_OpenCV joyOpenCV;
	

	public VideoManager_OpenCV(DroneUI_OpenCV joy) {
		super(joy);
		joyOpenCV=joy;
		// TODO Auto-generated constructor stub
	}

	@Override
	void startVideoPlayback() {

		mMediaPlayer.start();
	}

	@Override
	void playVideo(){

		try {
			
			
		if(mMediaPlayer == null){

		mMediaPlayer = new MediaPlayer(joyOpenCV);
		}
		
			
			
			mMediaPlayer.setDataSource(joyOpenCV.path);
			mMediaPlayer.prepareAsync();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);	
			
			
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub


		if(joyOpenCV.mRgba != null){
			Log.v(joyOpenCV.TAG_OpenCV, "onBufferingUpdate Rows and Cols :(" + joyOpenCV.mRgba.rows() + ", " + joyOpenCV.mRgba.cols() + ")");
		}
		joyOpenCV.iPercentBuff = percent;
		joyOpenCV.mtV_video.setText("Synchro :" + percent);
		Log.v(joyOpenCV.TAG_OpenCV, "Buffer Percent :"+ percent );
		joyOpenCV.video = mMediaPlayer.getCurrentFrame();


		if(mp.getCurrentFrame() != null){

			Log.v(joyOpenCV.TAG_OpenCV, "The Frame is here !" );

		}
		if(joyOpenCV.video == null){

			Log.v(joyOpenCV.TAG_OpenCV, "The Frame is NULL !" );

		}



		if(joyOpenCV.video != null && !joyOpenCV.video.isRecycled() && joyOpenCV.iPercentBuff<10 ){



			Log.v(joyOpenCV.TAG_OpenCV, "Surface Touched :"+ joyOpenCV.mIsColorSelected );
			

			if(joyOpenCV.mRgba == null){
				joyOpenCV.mRgba = new Mat(joyOpenCV.video.getHeight(), joyOpenCV.video.getWidth(), CvType.CV_8UC4);
				DroneUI_OpenCV.mVideoH = joyOpenCV.video.getHeight();
				DroneUI_OpenCV.mVideoW = joyOpenCV.video.getWidth();
			}
			if(joyOpenCV.modified == null){
				joyOpenCV.modified = new Mat();
			}

			Log.v(joyOpenCV.TAG_OpenCV, "Run Start Rows and Cols :(" + joyOpenCV.mRgba.rows() + ", " + joyOpenCV.mRgba.cols() + ")");

			Utils.bitmapToMat(joyOpenCV.video, joyOpenCV.mRgba);

			if (joyOpenCV.mIsColorSelected) {

				joyOpenCV.StartAsyncTask();


			}



		}




	}


}
