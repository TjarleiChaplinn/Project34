package Client.arduinoConnection;

import Client.App;
import Client.arduinoConnection.ArduinoConnection;
import javafx.scene.Scene;
import javafx.stage.Window;

import java.util.Arrays;

public class KeypadDataTransfer extends Thread {

	public String keypadInput = "";
	public String pincode = "";

	private boolean kill = false;

	public boolean pin = true;
	public boolean hasInput = false;

	String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
	String[] symbols = {"*", "#"};

	private ArduinoConnection connection = null;
	public boolean permission = false;
	
	public KeypadDataTransfer(ArduinoConnection connection, boolean permission) {
		this.permission = permission;
		this.connection = connection;
	}
	
	public void run() {
		while(!kill) {
			while(!permission || !connection.permission) {
				if(kill) {return;}
				try {sleep(10);} catch (InterruptedException e) {}
			}

			System.out.println("Keypad Started");

			connection.permission = false;
			
			do {
				try {
					connection.sendData("1");
					String temp = connection.getData();
					hasInput = true;
					boolean onlyNumbers = Arrays.stream(numbers).anyMatch(temp::equals);
					if(pin && onlyNumbers){
						Client.loginController.addKey();
						pincode += temp;
					}
					else {
						char tempChar = temp.charAt(0);
						if(tempChar == '#') {
							System.out.println("Goto idle");
							if(Client.loginController.currentThread().isAlive()) {
								Client.loginController.runThread = false;
							}
							Client.App.rfid.scannedCard = "";
							pincode = "";
							keypadInput = "";
							hasInput = false;
							permission = false;
							connection.permission = true;
							App.gotoIdle();
							return;
						}
						else if(tempChar == '*'){
							for(int i = 0; i < pincode.length(); i++){
								Client.loginController.removeKey();
							}
							pincode = "";
						}
						else if(tempChar == 'A'){

						}
					}
					sleep(10);
					keypadInput = temp;
					System.out.println("Pincode : " + pincode);
					System.out.println("keypadInput : " + keypadInput);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Data niet aangekomen.");
				}
			} while(permission);
			
			connection.permission = true;
		}
	}
	
	public void killThread() {
		kill = true;
		System.out.println("Keypad Thread Closed");
	}
	
}
