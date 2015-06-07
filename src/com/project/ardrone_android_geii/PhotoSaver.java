package com.project.ardrone_android_geii;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import io.vov.vitamio.MediaPlayer;

/**

 * 

 * Class allowing to get a frame from the MediaPlayer which streams the video and save it
into the local storage of the phone.

 */
public class PhotoSaver {
	String filename;
	Date date;
	String finalname;
	String path1;
	Bitmap image;
	Calendar rightNow;
	MediaPlayer mMediaPlayer;
	Context context;
	String imgname;

	public PhotoSaver(Context c, MediaPlayer m){
		this.context = c ;
		this.mMediaPlayer = m ;
		rightNow = Calendar.getInstance();
		filename = rightNow.get(Calendar.DAY_OF_MONTH)+"_"+(rightNow.get(Calendar.MONTH)+1)+"_"+rightNow.get(Calendar.YEAR)+".jpeg";
	}

	/**

	 * 

	 * Get the current frame of the MediaPlayer and saves it in the local storage of the phone at 
the PNG format.

	 */
	public void record(){
		if(Environment.getExternalStorageState() != null){
			try{
				image = mMediaPlayer.getCurrentFrame();	
				File picture = getOutputMediaFile();
				FileOutputStream fos = new FileOutputStream(picture);			
				image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();
				Toast.makeText(context, "Photo sauvegardée :"+imgname , Toast.LENGTH_SHORT).show();
			}catch(FileNotFoundException e){
				Toast.makeText(context, "Echec file" , Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "Echec close" , Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(context, "Repertoire non disponible" , Toast.LENGTH_SHORT).show();
		}
	}

	
	/**

	 * 

	 * Initializes the File which will be used to save the frame. Called in the record() method.
	 * @return the File to be used.

	 */
	private  File getOutputMediaFile(){

		rightNow = Calendar.getInstance();
		finalname = "DronePicture_"+rightNow.get(Calendar.HOUR)+":"+rightNow.get(Calendar.MINUTE)+":"+rightNow.get(Calendar.SECOND)+"_"+filename;
		//Create a media file name
		File mediaFile;
		imgname = Environment.getExternalStorageDirectory()+ "/Pictures/"+ finalname;
		mediaFile = new File(imgname);  
		return mediaFile;
	} 
}
