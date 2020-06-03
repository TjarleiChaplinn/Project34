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
			while(in.available() == 0) {}
			
			String word = "";
			
			while(in.available() > 0) {
				word += (char)in.read();
			}
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
		
		out.write(temp);
		out.flush();
	}
}
