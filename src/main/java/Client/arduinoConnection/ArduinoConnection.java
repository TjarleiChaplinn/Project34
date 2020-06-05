package Client.arduinoConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import com.fazecast.jSerialComm.SerialPort;

public class ArduinoConnection {

	public SerialPort ports = null;
	public InputStream in = null;
	public PrintWriter out = null;
	public boolean permission = true;
	
	
	public ArduinoConnection() {
		try {
			ports = SerialPort.getCommPorts()[0];
			in = ports.getInputStream();
			out = new PrintWriter(ports.getOutputStream(), true);
		} catch (Exception e) {
			System.out.println("Er zijn geen aparaten aangesloten.");
			return;
		}
	}
	
	public synchronized String getData() throws Exception {
		try {
			double minutes = 0.25;
			int waitTime = (int)(60 * minutes * 1000);
			long prevTime = 0;
			long currentTime = 0;
			if(Client.App.keypad.permission){
				prevTime = System.currentTimeMillis();
				currentTime = System.currentTimeMillis();
			}

			while(in.available() == 0) {
				if(Client.App.keypad.permission) {
					if (currentTime - prevTime >= waitTime) {
						break;
					}
					currentTime = System.currentTimeMillis();
				}
			}
			
			String word = "";
			if(currentTime - prevTime < waitTime) {
				while (in.available() > 0) {
					word += (char) in.read();
				}
			}
			else{
				word = "#";
			}

			System.out.println("Data received: " + word);
			in.close();
			return word;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Er is iets foutgegaan.");
			return "";
		}
	}
	
	public synchronized void sendData(String temp) {
		try {
			while(in.available() > 0) {wait();}
		} catch (IOException | InterruptedException e) {}
		System.out.println("Data send: " + temp);
		out.write(temp);
		out.flush();
	}
}
