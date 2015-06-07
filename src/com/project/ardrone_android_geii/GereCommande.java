package com.project.ardrone_android_geii;


public class GereCommande {

	public Menu_Activity ClasseMain;
	
	//Constant Command Strings
	public static final  String i5Pourcents = "1028443341";					
	public static final String i10Pourcents = "1036831949";
	public static final String i20Pourcents = "1045220557";
	public static final String i25Pourcents = "1048576000";
	public static final String i50Pourcents = "1056964608";
	public static final String i75Pourcents = "1061158912";
	public static final String i100Pourcents = "1065353216";
	public static final String iM5Pourcents = "-1119040307";
	public static final String iM10Pourcents = "-1110651699";
	public static final String iM20Pourcents = "-1102263091";
	public static final String iM25Pourcents = "-1098907648";
	public static final String iM50Pourcents = "-1090519040";
	public static final String iM75Pourcents = "-1086324736";
	public static final String iM100Pourcents = "-1082130432";

	String roll;				//Position Variables
	String pitch;
	String gaz;
	String yaw;
	String sData;

	public GereCommande(){									

		sData = "0,0,0,0";

	}

	/**

	 * Generate the parameters roll and pitch in function of the position of the joystick 1 on the axis x and y. 

	 * @param i 
	 *  Position of the joystick on the x axis.
	 * @param j
	 *  Position of the joystick on the y axis.

	 */
	public void GenerateCMDJoy1(double i, double j){				
		if(i >= -10 && i <= -8.75d){
			roll =iM100Pourcents;
		}
		else if(i > -8.75d && i <= -6.25d){
			roll =iM75Pourcents;
		}
		else if(i > -6.25d && i <= -3.75d){
			roll =iM50Pourcents;
		}
		else if(i > -3.75d && i <= -2.25d){
			roll =iM25Pourcents;
		}
		else if(i > -2.25d && i <= -1.5d){
			roll =iM20Pourcents;
		}
		else if(i > -1.5d && i <= -0.75d){
			roll =iM10Pourcents;
		}
		else if(i > -0.75d && i <-0.1){
			roll=iM5Pourcents;
		}
		else if(i > -0.1d && i <0.1){
			roll="0";
		}
		else if(i > 0.1 && i <= 0.75d){
			roll=i5Pourcents;
		}
		else if(i > 0.75d && i <= 1.5d){
			roll=i10Pourcents;
		}
		else if(i > 1.5d && i <= 2.25d){
			roll=i20Pourcents;
		}
		else if(i < 2.25d && i <= 3.75d){
			roll=i25Pourcents;
		}
		else if(i > 3.75d && i <= 6.25d){
			roll=i50Pourcents;
		}
		else if(i > 6.25d && i <= 8.75d){
			roll=i75Pourcents;
		}
		else if(i > 8.75 && i <= 10){
			roll=i100Pourcents;
		}
		else if(i==0){
			roll ="0";
		}

		if(j >= -10 && j <= -8.75d){
			pitch=iM100Pourcents;
		}
		else if(j > -8.75d && j <= -6.25d){
			pitch=iM75Pourcents;
		}
		else if(j > -6.25d && j <= -3.75d){
			pitch=iM50Pourcents;
		}
		else if(j > -3.75d && j <= -2.25d){
			pitch=iM25Pourcents;
		}
		else if(j > -2.25d && j <= -1.5d){
			pitch=iM20Pourcents;
		}
		else if(j > -1.5d && j <= -0.75d){
			pitch=iM10Pourcents;
		}
		else if(j > -0.75d && j <-0.1){
			pitch=iM5Pourcents;
		}
		else if(j > -0.1d && j <0.1){
			pitch="0";
		}
		else if(j > 0.1 && j <= 0.75d){
			pitch=i5Pourcents;
		}
		else if(j > 0.75d && j <= 1.5d){
			pitch =i10Pourcents;
		}
		else if(j > 1.5d && j <= 2.25d){
			pitch =i20Pourcents;
		}
		else if(j < 2.25d && j <= 3.75d){
			pitch =i25Pourcents;
		}
		else if(j > 3.75d && j <= 6.25d){
			pitch =i50Pourcents;
		}
		else if(j > 6.25d && j <= 8.75d){
			pitch =i75Pourcents;
		}
		else if(j > 8.75 && j <= 10){
			pitch =i100Pourcents;
		}
		else if(j==0){
			pitch ="0";
		}

		if ((i < 0.2) && (i > -0.2)) {
			roll = "0";
		}
		if ((j < 0.2) && (j > -0.2)) {
			pitch = "0";
		}
	}

	/**

	 * Generate the parameters yaw and gaz in function of the position of the joystick 2 on the axis x and y. 

	 * @param i 
	 *  Position of the joystick on the x axis.
	 * @param j
	 *  Position of the joystick on the y axis.

	 */
	public void GenerateCMDJoy2(double i, double j){			
		if(i >= -10 && i <= -8.75d){
			yaw =iM100Pourcents;
		}
		else if(i > -8.75d && i <= -6.25d){
			yaw =iM75Pourcents;
		}
		else if(i > -6.25d && i <= -3.75d){
			yaw =iM50Pourcents;
		}
		else if(i > -3.75d && i <= -2.25d){
			yaw =iM25Pourcents;
		}
		else if(i > -2.25d && i <= -1.5d){
			yaw =iM20Pourcents;
		}
		else if(i > -1.5d && i <= -0.75d){
			yaw =iM10Pourcents;
		}
		else if(i > -0.75d && i <-0.1){
			yaw=iM5Pourcents;
		}
		else if(i > -0.1d && i <0.1){
			yaw="0";
		}
		else if(i > 0.1 && i <= 0.75d){
			yaw=i5Pourcents;
		}
		else if(i > 0.75d && i <= 1.5d){
			yaw=i10Pourcents;
		}
		else if(i > 1.5d && i <= 2.25d){
			yaw=i20Pourcents;
		}
		else if(i < 2.25d && i <= 3.75d){
			yaw=i25Pourcents;
		}
		else if(i > 3.75d && i <= 6.25d){
			yaw=i50Pourcents;
		}
		else if(i > 6.25d && i <= 8.75d){
			yaw=i75Pourcents;
		}
		else if(i > 8.75 && i <= 10){
			yaw=i100Pourcents;
		}
		else if(i==0){
			yaw ="0";
		}

		if(j >= -10 && j <= -8.75d){
			gaz=i100Pourcents;
		}
		else if(j > -8.75d && j <= -6.25d){
			gaz=i75Pourcents;
		}
		else if(j > -6.25d && j <= -3.75d){
			gaz=i50Pourcents;
		}
		else if(j > -3.75d && j <= -2.25d){
			gaz=i25Pourcents;
		}
		else if(j > -2.25d && j <= -1.5d){
			gaz=i20Pourcents;
		}
		else if(j > -1.5d && j <= -0.75d){
			gaz=i10Pourcents;
		}
		else if(j > -0.75d && j <-0.1){
			gaz=iM5Pourcents;
		}
		else if(j > -0.1d && j <0.1){
			gaz="0";
		}
		else if(j > 0.1 && j <= 0.75d){
			gaz=i5Pourcents;
		}
		else if(j > 0.75d && j <= 1.5d){
			gaz =iM10Pourcents;
		}
		else if(j > 1.5d && j <= 2.25d){
			gaz =iM20Pourcents;
		}
		else if(j < 2.25d && j <= 3.75d){
			gaz =iM25Pourcents;
		}
		else if(j > 3.75d && j <= 6.25d){
			gaz =iM50Pourcents;
		}
		else if(j > 6.25d && j <= 8.75d){
			gaz =iM75Pourcents;
		}
		else if(j > 8.75 && j <= 10){
			gaz =iM100Pourcents;
		}
		else if(j==0){
			gaz ="0";
		}

		if ((i < 0.2) && (i > -0.2)) {
			yaw = "0";
		}
		if ((j < 0.2) && (j > -0.2)) {
			gaz = "0";
		}
	}
	
	/**

	 * Generate the command’s part containing the parameters roll pitch gaz and yaw as well as the boolean allowing the drone to move. 

	 * @param s 
	 *  String s containing a 0 or a 1, 0 activates the hover and 1 activates the pilotage
	 * @return 
	 *  String containing the second part of the AT command. Example : ",1,roll,pitch,gaz,yaw".

	 */
	public String GeneratePCMD(String s){					

		sData = ","+s+","+roll+","+pitch+","+gaz+","+yaw;		
		return sData;
	}
}













