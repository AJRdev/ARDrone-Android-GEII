package com.example.projet_er4_v1;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

import java.io.IOException;

import android.util.Log;




public class GereVideo implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener {
	MediaPlayer mMediaPlayer;
	JoyActivityVideo joyActivity;
	private String TAG = "Fcking NOTIF";



	public GereVideo(JoyActivityVideo joy){			
		joyActivity = joy;


	}

	void playVideo(){


		mMediaPlayer = new MediaPlayer(joyActivity);
		
		try {
			mMediaPlayer.setDataSource(joyActivity.path);
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
		
		
		mMediaPlayer.setDisplay(joyActivity.holder);
		mMediaPlayer.prepareAsync();
		mMediaPlayer.setOnBufferingUpdateListener(this);
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setOnVideoSizeChangedListener(this);	

	}

	void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	void startVideoPlayback() {
		joyActivity.holder.setFixedSize(joyActivity.metrics.widthPixels, joyActivity.metrics.heightPixels);
		mMediaPlayer.start();
	}
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		startVideoPlayback();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		if(mp.getCurrentFrame() != null){

			Log.v(TAG , "LA FRAME EST LA" );

		}
		else{
			Log.v(TAG, "LA FRAME EST PAS PAS LA" );
		}
		
	}


}





