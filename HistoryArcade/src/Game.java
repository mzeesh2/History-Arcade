import java.io.*;
import java.util.*;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

public class Game {
	
	private ArrayList<Character> charList = new ArrayList<>();
	private ServerMessenger messenger;
	private int numberOfRounds;
	
	/**
	 * 
	 * @param mapFileName the file name of the file to construct a game map from
	 * @param playerCount the number of players in the game
	 * @param messenger the ServerMessenger object that will be used to relay information between server and clients
	 */
	public Game(String mapFileName, String playerCount, String numRounds, ServerMessenger messenger) {	
		try {
			this.messenger = messenger;
			try {
				this.numberOfRounds = Integer.parseInt(numRounds);
			}
			catch(NumberFormatException e) {
				messenger.messageAllClients("An Error has occurred while constructing Game!");
				messenger.messageAllClients("QUIT");  //Tell all clients to disconnect
				messenger.closeAllConnections();  //Close all sockets on server side
				e.printStackTrace();				
			}
			
	        //Parse map file specified 
	        Object obj = new JSONParser().parse(new FileReader(mapFileName));  //Parsing the JSON to an object
	        JSONObject jo = (JSONObject) obj;  //Type casting obj to JSONObject
	        
	        JSONObject jobj;
	        JSONArray ja = (JSONArray) jo.get("Locations");  //Get the locations information
	        Iterator<JSONObject> iter = ja.iterator();  //Creating iterator to retrieve information
	        while (iter.hasNext())  //Creating Locations
	        { 
	            jobj = iter.next();
	            Long id = (Long)jobj.get("ID");  //Needs conversion because JSON parser only reads numbers as Long objects
	            new Location(id.intValue(), (String)jobj.get("Region"));
	        }

	        ja = (JSONArray) jo.get("Connections");  //Get the Connections information
	        iter = ja.iterator();  //Update iterator
	        while (iter.hasNext())  //Creating connections between Locations
	        { 
	            jobj = iter.next();
	            Long fromID = (Long)jobj.get("fromRegionID");
	            Location from = Location.getLocationByID(fromID.intValue());
	            Long toID = (Long)jobj.get("toRegionID");
	            Location to = Location.getLocationByID(toID.intValue());
	            from.addAdacent(to);
	        }
	        
	        ja = (JSONArray) jo.get("Trivia");  //Get the Connections information
	        iter = ja.iterator();  //Update iterator
	        while (iter.hasNext())  //Creating trivia for Locations
	        { 
	            jobj = iter.next();
	            Trivia t = new Trivia((String)jobj.get("Question"), (String)jobj.get("Answer"));
	            Long regionID = (Long)jobj.get("RegionID");
	            Location l = Location.getLocationByID(regionID.intValue());
	            l.addTrivia(t);
	        }   
		}
		catch(ParseException | IOException e) {  //If error occurs while game is being constructed
			messenger.messageAllClients("An Error has occurred while constructing Game!");
			messenger.messageAllClients("QUIT");  //Tell all clients to disconnect
			messenger.closeAllConnections();  //Close all sockets on server side
			e.printStackTrace();
		}
		
		//Create and place the player in a random region of the map
		Location region = Location.getRandomLocation();
		Player playerOne = new Player("Player 1", "Blue", region);
		region.addCharacter(playerOne);  //Create a function to drop player into a random region
		this.charList.add(playerOne);
		
		region = Location.getRandomLocation();
		if(playerCount.equals("1")) {  //Create NPC with random difficulty setting 
	        Random rand = new Random();  // Generate a random number
			int difficulty = Math.abs(rand.nextInt()) % 4;  //25% chance of being each difficulty
			System.out.println("Difficulty is: " + difficulty);
			switch(difficulty) {  //Inform player of randomly chosen difficulty
				case 0:
					messenger.messageAllClients("NPC difficulty set to easy");
					break;
				case 1:
					messenger.messageAllClients("NPC difficulty set to medium");
					break;
				case 2:
					messenger.messageAllClients("NPC difficulty set to hard");
					break;
				case 3:
					messenger.messageAllClients("NPC difficulty set to expert");
					break;
				default:
					messenger.messageAllClients("Invalid NPC difficulty while constructing Game!");
					messenger.messageAllClients("QUIT");  //Tell all clients to disconnect
					messenger.closeAllConnections();  //Close all sockets on server side
					throw new Error("Invalid NPC difficulty");
			}
			NPC npc = new NPC("Glados","Red", region, difficulty);
			region.addCharacter(npc);  //Create a function to drop player into a random region
			this.charList.add(npc);
		}
		else {
			Player playerTwo = new Player("Player 2", "Red", region);
			region.addCharacter(playerTwo);  //Create a function to drop player into a random region
			this.charList.add(playerTwo);
		}
	}

	/**
	 * Runs the game
	 */
	public void play() {	
		String input;
		Location curr;
		Trivia t;
		Character currChar;
		int currentRound = 1;
		
		Loop:
		while(currentRound <= numberOfRounds) {
			Iterator<Character> iter = charList.iterator();  //Iterate over characters in game
			messenger.messageAllClients("Starting Round " + currentRound);
			while(iter.hasNext()) {
				currChar = iter.next();  //Get the next character entity
				
				//Output current state of game information
				curr = currChar.getCurrentLocation();
				messenger.messageAllClients(currChar.getName() + ", You are currently in: " + curr.getName());
				messenger.messageAllClients("POSITION " + currChar.getName() + " " + curr.getName());  //Tell clients to update map icons
				messenger.messageAllClients("These are the available places to move to: ");
				
				Set<String> adjacents = curr.namesOfAdjacents();
				String locList = "";
				for(String loc: adjacents)
					locList = locList + "'" + loc + "', ";
				messenger.messageAllClients(locList);  
				messenger.messageAllClients("");  //Whitespace to improve output formatting
	
				//Get player input and check if quit
				messenger.messageAllClients(currChar.getName() + ", where do you want to go?");
				if(currChar instanceof Player) {  //Player's turn
					messenger.messageClient(currChar.getName(), "INPUT");
					input = messenger.getClientResponse(currChar.getName());
					if(input.equalsIgnoreCase("QUIT")) {
						//SEND QUIT MESSAGE TO CLIENT TO END COMMUNICATION
						messenger.messageAllClients("Exiting the game...");
						messenger.messageAllClients("QUIT");  //This will be a break client out of communication condition
						break Loop;
					}
					
					//Check if valid move
					if(curr.hasAdacent(input)) {
						curr.moveCharacter(currChar, input);
						curr = currChar.getCurrentLocation();
						messenger.messageAllClients(currChar.getName() + " has entered " + curr.getName());
						messenger.messageAllClients("POSITION " + currChar.getName() + " " + curr.getName());  //Tell clients to update map icons
					}
					else {
						messenger.messageAllClients("The location '" + input + "' does not exist");
						messenger.messageAllClients(currChar.getName() + " has moved nowhere");
					}
				}
				else {  //NPC's turn
					input = ((NPC) currChar).chooseRandomAdjacentLocation();
					curr.moveCharacter(currChar, input);
					curr = currChar.getCurrentLocation();
					messenger.messageAllClients(currChar.getName() + " has entered " + curr.getName());
					messenger.messageAllClients("POSITION " + currChar.getName() + " " + curr.getName());  //Tell clients to update map icons
				}
				
				//Character has entered the area so ask them a trivia question
				t = curr.getTrivia();
				messenger.messageAllClients(t.getQuestion());
				messenger.messageAllClients("What is your answer? ");
				if(currChar instanceof Player) {  //Player's turn
					messenger.messageClient(currChar.getName(), "INPUT");
					input = messenger.getClientResponse(currChar.getName());

					if(input.equalsIgnoreCase("QUIT")) {
						//SEND QUIT MESSAGE TO CLIENT TO END COMMUNICATION
						messenger.messageAllClients("Exiting the game...");
						messenger.messageAllClients("QUIT");  //This will be a break client out of communication condition
						break Loop;
					}
				}
				else {  //NPC's turn
					input = ((NPC)currChar).generateQuestionResponse(t);
				}
				
				//Update score on character and inform user of result
				boolean result = t.checkAnswer(input);
				if(result) {  //If correctly answered, capture territory if not already owned
					if(curr.getOwner() != currChar)  //Log if territory is owned or not
						System.out.println(curr.getName() + " is not owned by " + currChar.getName() + " yet");
					else
						System.out.println(curr.getName() + " is owned by " + currChar.getName());
					
					System.out.println(currChar.getName() + "'s points before: " + currChar.getPoints());
					currChar.capture(curr);  //Annex territory if not owned and add point, character losing territory loses location and a point
					System.out.println(currChar.getName() + "'s points after: " + currChar.getPoints());
				}
				messenger.messageAllClients("That is... " + result);
				messenger.messageAllClients("");  //Whitespace to improve output formatting
				
				//Output captured locations of each player, just testing if it records this information correctly
				for(Character c: charList) {
					messenger.messageAllClients(c.getName() + " has captured: ");
					String capturedList = "";
					for(String loc: c.capturedLocationNames()) {
						capturedList = capturedList + "'" + loc + "', "; 
					}
					messenger.messageAllClients(capturedList);
				}
				messenger.messageAllClients("");  //Whitespace to improve output formatting
				
				//Update Score Board on clients
				StringBuilder scoreSB = new StringBuilder("SCORES");
				for(Character c: charList) {  //
					scoreSB.append(" " + Integer.toString(c.getPoints()));
				}
				messenger.messageAllClients(scoreSB.toString());
				
			}
			
			currentRound = currentRound + 1;  //Increment round counter
			iter = charList.iterator();  //Restart the iterator at front of list
		}

		messenger.messageAllClients("Game Over, check score board to see who won!");
		messenger.messageAllClients("QUIT");  //This will be a break client out of communication condition
		
	}
}
