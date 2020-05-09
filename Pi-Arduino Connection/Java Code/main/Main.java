package main;

import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		
		ArduinoConnection connection = new ArduinoConnection();
		BonDataTransfer bon = new BonDataTransfer(connection, "Fruitlaan 28", "Rijswijk", "185", "U heeft geld opgenomen", "965", "NL56INGB94", "8562", false);
		KeypadDataTransfer keypad = new KeypadDataTransfer(connection, false);
		
		try{
			connection.ports.openPort();
			bon.start();
			keypad.start();
		} catch(NullPointerException e) {
			System.out.println("Ports couldn't be opened.");
			return;
		}
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Keypad Aan");
		keypad.permission = true;
		
		while(keypad.permission) {try {
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		
		keypad.permission = false;
		System.out.println("Keypad Uit");
		
		System.out.println("Bonprinter Aan");
		bon.permission = true;
		
		while(bon.permission) {try {
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		
		System.out.println("Bonprinter Uit");
		
		try{
			bon.killThread();
			keypad.killThread();
			while(bon.isAlive() == true || keypad.isAlive() == true) {
				TimeUnit.MILLISECONDS.sleep(1);
			}
			connection.ports.closePort();
		} catch(NullPointerException | InterruptedException e) {
			System.out.println("Ports couldn't be closed.");
			return;
		}
	}
}
