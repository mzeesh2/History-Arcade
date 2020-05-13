import java.io.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;


public class GameServer {
	
	private int portNum;
	private ServerSocket server;
	private ServerMessenger messenger;
	
	private GameServer(int portNumber) {
		try {
			portNum = portNumber;
			server = new ServerSocket(portNum);
			messenger = new ServerMessenger();
		}
		catch(IOException e) {
            System.out.println(e);			
		}
	}
	
	
	public static void main(String[] args) {
		System.out.print("Please input a valid port number to start server: ");
        Scanner in = new Scanner(System.in);  //Start server on requested port if possible
        GameServer gs = new GameServer(in.nextInt());  //Initialize socket
        System.out.println("Started server.");

    	try {
    		Socket client = gs.server.accept();  //Accept connection when one comes
            gs.messenger.addClient(client,"Player 1");
            System.out.println("Player 1 connected.");

            //Get information from client about game kind
            String playerCount = gs.messenger.getClientResponse("Player 1");
            String numRounds = gs.messenger.getClientResponse("Player 1");
            String warName = gs.messenger.getClientResponse("Player 1");

            //Handle case of multiple clients in multiplayer
            if(playerCount.equals("2")) {
				gs.messenger.messageClient("Player 1", "Waiting on other player to join...");
            	Socket client2 = gs.server.accept();
            	gs.messenger.addClient(client2,"Player 2");
                System.out.println("Player 2 connected.");
            }
            
            //Create instance of the game map selected
            StringBuilder sb = new StringBuilder("src/maps/" + warName  + ".json");
            String gamefile = sb.toString(); 
            Game theGame = new Game(gamefile, playerCount, numRounds, gs.messenger);
            theGame.play();
            
    		gs.messenger.closeAllConnections();  //Close sockets and scanner
    		gs.server.close();
    		in.close();
    	}
        catch(IOException e) {
        	System.out.println(e);        		
        }        	
	}
	
};
