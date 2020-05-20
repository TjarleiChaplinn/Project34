package Client.arduinoConnection;

import Client.arduinoConnection.ArduinoConnection;

import java.util.Calendar;

public class BonDataTransfer extends Thread {
	
	private boolean kill = false;
	
	private ArduinoConnection connection = null;
	
	public boolean permission = false;
	
	private int dataArrayIndex = 0;
	
	String[] dataArray = new String[12];
	
	public BonDataTransfer(ArduinoConnection connection, boolean permission) {
		for(String array : dataArray){
			array = "";
		}
		this.connection = connection;
		this.permission = permission;
	}

	public void setData(String adres, String plaats, String bedrag, String bericht, String transnr, String rekening, String devicenr) {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int timeHour = cal.get(Calendar.HOUR_OF_DAY);
		int timeMinute = cal.get(Calendar.MINUTE);

		String dayStr = String.valueOf(day);
		if(dayStr.length() == 1) {
			dayStr = "0" + dayStr;
		}
		String monthStr = String.valueOf(month);
		if(monthStr.length() == 1) {
			monthStr = "0" + monthStr;
		}
		String yearStr = String.valueOf(year);
		String timeHourStr = String.valueOf(timeHour);
		if(timeHourStr.length() == 1) {
			timeHourStr = "0" + timeHourStr;
		}
		String timeMinuteStr = String.valueOf(timeMinute);
		if(timeMinuteStr.length() == 1) {
			timeMinuteStr = "0" + timeMinuteStr;
		}

		dataArray[0] = adres;
		dataArray[1] = plaats;
		dataArray[2] = bedrag;
		dataArray[3] = bericht;
		dataArray[4] = transnr;
		dataArray[5] = rekening;
		dataArray[6] = devicenr;
		dataArray[7] = dayStr;
		dataArray[8] = monthStr;
		dataArray[9] = yearStr;
		dataArray[10] = timeHourStr;
		dataArray[11] = timeMinuteStr;
	}

	public void run() {
		while(!kill) {
			while(!permission || !connection.permission) { 
				if(kill) {return;}
				try {sleep(10);} catch (InterruptedException e) {}
			}
			
			connection.permission = false;
			
			connection.sendData("2");
			
			do {
				try {
					while (dataArrayIndex < dataArray.length) {
						connection.getData();
						connection.sendData(dataArray[dataArrayIndex++]);
					}
					permission = false;
					dataArrayIndex = 0;
					connection.permission = true;
				} catch (Exception e) {
					System.out.println("Data niet aangekomen.");
				}
			} while(permission);
		}
		
	}
	
	public void killThread() {
		kill = true;
		System.out.println("Bon Thread Closed");
	}
}
