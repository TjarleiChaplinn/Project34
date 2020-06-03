package Client.arduinoConnection;

import Client.App;
import Client.arduinoConnection.ArduinoConnection;
import Client.lib.ApiConnector;
import Client.loginController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Arrays;

import static java.lang.Integer.valueOf;

public class KeypadDataTransfer extends Thread {

	public String keypadInput = "";
	public String pincode = "";

	private boolean kill = false;

	public boolean pin = true;
	public boolean hasInput = false;

	String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

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
					char tempChar = temp.charAt(0);
					if(App.scene == "login") {
						boolean onlyNumbers = Arrays.stream(numbers).anyMatch(temp::equals);
						if(pin && onlyNumbers){
							pincode += temp;
							App.loginController.setPinText();
							if(pincode.length() == 4){
								pin = false;
							}
						}
						else if(tempChar == '*'){
							pin = true;
							pincode = "";
							App.loginController.setPinText();
						}
						else if(tempChar == 'A'){
							String password = pincode;
							try {
								App.apiConnector = new ApiConnector("1", password, false);
								if (App.apiConnector.verifyPin("1", password)==true) {
									App.balance = App.apiConnector.getBalance("1", password);
									permission = false;
									App.gotoMainScene();
								}
								else{
									App.loginController.melding.setText(App.apiConnector.getMessage());
								}
							} catch (IOException e) {
								e.printStackTrace();
								System.err.println("API connection error: " + e.getMessage());
							}
						}
					}
					else if(App.scene == "main"){
						if(tempChar == 'B'){
							if (valueOf(App.balance) >= 70) {
								if (App.aantalVijftig >= 1 && App.aantalTien >= 2) {
									App.setNul();
									App.totaalbedrag = 70;
									App.nvijftig = 1;
									App.ntien = 2;
									App.gotoEndScene();
								}
								else{
									App.gotoWarningScene("Te weinig geld in automaat");
								}
							}
							else{
								App.gotoWarningScene("Te weinig balans");
							}
						}
						else if(tempChar == 'A'){
							Platform.runLater(new Runnable(){
								@Override
								public void run(){
									App.mainController.gotoBiljetScene();
								}
							});
						}
						permission = false;
					}
					else if(App.scene == "biljet"){
						if(tempChar == 'A'){
							App.biljetController.addFive();
						}
						else if(tempChar == 'B'){
							App.biljetController.addTen();
						}
						else if(tempChar == 'C'){
							App.biljetController.addFifty();
						}
						else if(tempChar == '3'){
							App.biljetController.remFive();
						}
						else if(tempChar == '6'){
							App.biljetController.remTen();
						}
						else if(tempChar == '9'){
							App.biljetController.remFifty();
						}
						else if(tempChar == 'D'){
							if (valueOf(App.balance) >= App.totaalbedrag) {
								App.gotoEndScene();
							}
							else{
								App.gotoWarningScene("Te weinig balans");
							}
							permission = false;
						}
						else if(tempChar == '*'){
							App.gotoMainScene();
							permission = false;
						}
					}
					else if(App.scene == "warning"){
						if(tempChar == '*'){
							App.gotoMainScene();
							permission = false;
						}
					}
					else if(App.scene == "end"){
						if(tempChar == 'A'){
							App.endController.doTransaction();
						}
						else if(tempChar == 'B'){
							App.endController.switchPrint();
						}
						else if(tempChar == '*'){
							App.gotoMainScene();
							permission = false;
						}
					}

					if(tempChar == '#') {
						Client.App.rfid.scannedCard = "";
						pincode = "";
						keypadInput = "";
						hasInput = false;
						permission = false;
						App.gotoIdle();
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
		System.out.println(currentThread().getName() + " Closed");
	}
	
	public void killThread() {
		kill = true;
	}
	
}
