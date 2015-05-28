package com.project.ardrone_android_geii;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GereManette
{
	
	public GereManette()
	{
		
		
	}
	
	
	public boolean GetKeyState( KeyEvent keyEvent ) {
		
		boolean bState = false;
		
		if(keyEvent.getAction() == keyEvent.ACTION_DOWN)
		{
			bState = true;
		}
		else if(keyEvent.getAction() == keyEvent.ACTION_UP)
		{
			bState = false;
		}
		
		return bState;
		
	}	
	
	 public int GetKey( KeyEvent keyEvent ) {
	    	
	    	int iKey = 0;
	    	
	    		
		    		if(keyEvent.getKeyCode() == keyEvent.KEYCODE_BUTTON_A)
			   		{
		    			iKey = 1;
			   			
			   		}
			   		
			   		if(keyEvent.getKeyCode() == keyEvent.KEYCODE_BUTTON_B)
			   		{
			   			iKey = 2;
			   			
			   		}
			   		
			   		if(keyEvent.getKeyCode() == keyEvent.KEYCODE_BUTTON_X)
			   		{
			   			iKey = 3;
			   			
			   		}
			   		
			   		if(keyEvent.getKeyCode() == keyEvent.KEYCODE_BUTTON_Y)
			   		{
			   			iKey = 4;
			   			
			   		}
			   		
			   		if(keyEvent.getKeyCode() == keyEvent.KEYCODE_BUTTON_START)
			   		{
			   			iKey = 5;
			   			
			   		}
			   		
			   		if(keyEvent.getKeyCode() == keyEvent.KEYCODE_BUTTON_SELECT)
			   		{
			   			iKey = 6;
			   			
			   		}
			   		
			   		if(keyEvent.getKeyCode() == keyEvent.KEYCODE_BUTTON_R1)
			   		{
			   			iKey = 7;
			   			
			   		}
			   		
			   		if(keyEvent.getKeyCode() == keyEvent.KEYCODE_BUTTON_L1)
			   		{
			   			iKey = 8;
			   			
			   		}
			   		
			   		if(keyEvent.getKeyCode() == keyEvent.KEYCODE_BUTTON_R2)
			   		{
			   			
			   			iKey = 9;
			   		}
			   		
			   		if(keyEvent.getKeyCode() == keyEvent.KEYCODE_BUTTON_L2)
			   		{
			   			
			   			iKey = 10;
			   		}
	    		
		   		
	        return iKey;
	   
	    }
	 
	 // RETOURNE LES COORDONNEES DU JOYSTICK1 SUR SON AXE X
	 public double GetJ1PosX(MotionEvent motionEvent) {
     	
		 double dX = 0;
		 float fX;
		 
			if ((motionEvent.getSource() & InputDevice.SOURCE_JOYSTICK) ==
	                InputDevice.SOURCE_JOYSTICK &&
	                motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
			
				fX = motionEvent.getAxisValue(MotionEvent.AXIS_X);
				fX = fX * 10;
				dX = (double)fX;
 	        
			} 
     
		return dX;
 
	 }
	 
	 // RETOURNE LES COORDONNEES DU JOYSTICK1 SUR SON AXE Y
	 public double GetJ1PosY(MotionEvent motionEvent) {
     	
		 double dY = 0;
		 float fY;
		 
			if ((motionEvent.getSource() & InputDevice.SOURCE_JOYSTICK) ==
	                InputDevice.SOURCE_JOYSTICK &&
	                motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
			
				fY = motionEvent.getAxisValue(MotionEvent.AXIS_Y);
				fY = fY * 10;
				dY = (double)fY;
 	        
			} 
     
		return dY;
 
	 }
	 
	// RETOURNE LES COORDONNEES DU JOYSTICK1 SUR SON AXE X
		 public double GetJ2PosX(MotionEvent motionEvent) {
	     	
			 double dX = 0;
			 float fX;
			 
				if ((motionEvent.getSource() & InputDevice.SOURCE_JOYSTICK) ==
		                InputDevice.SOURCE_JOYSTICK &&
		                motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				
					fX = motionEvent.getAxisValue(MotionEvent.AXIS_Z);
					fX = fX * 10;
					dX = (double)fX;
	 	        
				} 
	     
			return dX;
	 
		 }
		 
		 // RETOURNE LES COORDONNEES DU JOYSTICK1 SUR SON AXE Y
		 public double GetJ2PosY(MotionEvent motionEvent) {
	     	
			 double dY = 0;
			 float fY;
			 
				if ((motionEvent.getSource() & InputDevice.SOURCE_JOYSTICK) ==
		                InputDevice.SOURCE_JOYSTICK &&
		                motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				
					fY = motionEvent.getAxisValue(MotionEvent.AXIS_RZ);
					fY = fY * 10;
					dY = (double)fY;
	 	        
				} 
	     
			return dY;
	 
		 }
		 
		// RETOURNE LES COORDONNEES DU JOYSTICK1 SUR SON AXE Y
				 public int GetDPad(MotionEvent motionEvent) {
			     	
					 int iPad = 0;
					 float fX;
					 float fY;
					 int iX;
					 int iY;
					 
						if ((motionEvent.getSource() & InputDevice.SOURCE_JOYSTICK) ==
				                InputDevice.SOURCE_JOYSTICK &&
				                motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
						
							fX = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_X);
		    				fY = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_Y);
		    				
		    				iX = (int)fX * 100;
		    				iY = (int)fY * 100;
		    				
			    				if(iX == 100)
			    				{
			    					iPad = 2;
			    				}
			    				else if(iX == -100)
			    				{
			    					iPad = 4;
			    				}
			    				
			    				if(iY == 100)
			    				{
			    					iPad = 3;
			    				}
			    				else if(iY == -100)
			    				{
			    					iPad = 1;
			    				}
		    				}
		    			
					return iPad;
			 
				 }

	    
	
}