import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {

	private Socket client;
	private PrintWriter outgoing;
	private BufferedReader incoming;
	
	public GameClient(String ip, int port) {
		try {
	        client = new Socket(ip, port);
	        incoming = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        outgoing = new PrintWriter(client.getOutputStream(), true);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getIncomingMsg() {
		try {
			String msg = incoming.readLine();
			System.out.println("GameClient incoming: " + msg);
			return msg;
		}
		catch(IOException e) {
			return "GameClient: Msg Error";
		}
	}

	public void sendOutgoingMsg(String msg) {
		System.out.println("GameClient outgoing: " + msg);
		outgoing.println(msg);
	}	
	
	public void closeClient() {
		try {
			client.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	

}
