package main;

public class KeypadDataTransfer extends Thread {
	
	private boolean kill = false;
	
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
			
			connection.permission = false;
			
			do {
				try {
					connection.sendData("1");
					String temp = connection.getData();
					switch(temp) {
					case "1":
						System.out.println(1);
						break;
					case "2":
						System.out.println(2);
						break;
					case "3":
						System.out.println(3);
						break;
					case "4":
						System.out.println(4);
						break;
					case "5":
						System.out.println(5);
						break;
					case "6":
						System.out.println(6);
						break;
					case "7":
						System.out.println(7);
						break;
					case "8":
						System.out.println(8);
						break;
					case "9":
						System.out.println(9);
						break;
					case "0":
						System.out.println(0);
						break;
					case "A":
						System.out.println("A");
						break;
					case "B":
						System.out.println("B");
						break;
					case "C":
						System.out.println("C");
						break;
					case "D":
						System.out.println("D");
						break;
					case "*":
						System.out.println("*");
						break;
					case "#":
						System.out.println("#");
						permission = false;
						break;
					}
				} catch (Exception e) {
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
