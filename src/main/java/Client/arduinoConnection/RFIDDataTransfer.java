package Client.arduinoConnection;

import Client.App;
import Client.arduinoConnection.ArduinoConnection;
import Client.idleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class RFIDDataTransfer extends Thread {

	private boolean kill = false;
	
	private ArduinoConnection connection = null;
	public boolean permission = false;
	
	public String scannedCard = "";
	
	public RFIDDataTransfer(ArduinoConnection connection, boolean permission) {
		this.permission = permission;
		this.connection = connection;
	}

	public String getScannedCard(){return scannedCard;}

	
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
	}
}
