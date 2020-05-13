import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMessenger {
	private ArrayList<Communicator> clients;
	private ArrayList<Socket> sockets;
	
	public ServerMessenger() {
		clients = new ArrayList<>();
		sockets = new ArrayList<>();
	}
	
	/**
	 * @param soc the socket to construct a communicator object from
	 * @param n the name of the client
	 */
	public void addClient(Socket soc, String n) {
		Communicator c = new Communicator(soc, n);
		clients.add(c);
		sockets.add(soc);
	}
	
	/**
	 * @param name the name of the target client
	 * @return the Communicator object for that client or null if not found
	 */
	private Communicator getClientComm(String name) {
		for(Communicator c: clients) {
			if(c.clientName.equals(name))
				return c;
		}
		return null;  //May need something to handle this later
	}
	
	/**
	 * 
	 * @param name the name of the client to be messaged
	 * @param msg the message to be sent to the requested client
	 */
	public void messageClient(String name, String msg) {
		Communicator target = getClientComm(name);
		if(target != null) {
			target.sendOutgoingMsg(msg);
		}
	}

	/**
	 * @param msg the message to send to all clients
	 */
	public void messageAllClients(String msg) {
		for(Communicator c: clients)
			c.sendOutgoingMsg(msg);
	}
	
	/**
	 * @param name the name of the client to get a message from
	 * @return the message received from the requested client
	 */
	public String getClientResponse(String name) {
		Communicator target = getClientComm(name);
		if(target != null) {
			return target.getIncomingMsg();
		}
		return "Error: did not find client requested.";
	}
	
	/**
	 * Closes all sockets
	 */
	public void closeAllConnections() {
		try {
			for(Socket s: sockets) 
				s.close();
		}
	    catch(IOException e) {
	    	System.out.println(e);        		
	    }    
	}
	
	class Communicator {
		private String clientName;
		private PrintWriter outgoing;
		private BufferedReader incoming;
		
		public Communicator(Socket client, String name) {
			try {
				clientName = name;
		        outgoing = new PrintWriter(client.getOutputStream(),true);  //Send information to the client
		        incoming = new BufferedReader(new InputStreamReader(client.getInputStream()) );
			}
		    catch(IOException e) {
		    	System.out.println(e);        		
		    }        	
		}

		
		public String getIncomingMsg() {
			try {
				String msg = incoming.readLine().trim();
				System.out.println("ServerMessenger incoming: " + msg);
				return msg;
			}
			catch(IOException e) {
				return "ServerMessenger: Msg Error";
			}
		}

		public void sendOutgoingMsg(String msg) {
			System.out.println("ServerMessenger outgoing: " + msg);
			outgoing.println(msg);
		}
		
	}
}

