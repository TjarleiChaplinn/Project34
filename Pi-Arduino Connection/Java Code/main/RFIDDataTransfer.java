package main;

public class RFIDDataTransfer extends Thread {

	private boolean kill = false;
	
	private ArduinoConnection connection = null;
	public boolean permission = false;
	
	public String scannedCard = "";
	
	public RFIDDataTransfer(ArduinoConnection connection, boolean permission) {
		this.permission = permission;
		this.connection = connection;
	}
	
	public void run() {
		while(!kill) {
			while(!permission || !connection.permission) {
				if(kill) {return;}
				try {sleep(10);} catch (InterruptedException e) {}
			}
			
			connection.permission = false;
			
				
			try {
				connection.sendData("3");
				scannedCard = connection.getData();
			} catch(Exception e) {
				System.out.println("Data niet aangekomen.");
			}
			
			connection.permission = true;
			permission = false;
		}
	}
	
	public void killThread() {
		kill = true;
		System.out.println("RFID Thread Closed");
	}
}
