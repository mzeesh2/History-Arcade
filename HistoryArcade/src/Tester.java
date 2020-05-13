import java.util.*;
import java.io.*;

public class Tester {
	public static void main(String[] args) {
		//Construct the map pieces
		Location portugal = new Location(1,"Portugal");
		Location spain = new Location(2,"Spain");
		Location france = new Location(3,"France");
		Location uk = new Location(4,"United Kingdom");
		Location belgium = new Location(5,"Belgium");
		Location netherlands = new Location(6,"Netherlands");
		
		//Add the connections between map regions
		portugal.addAdacent(spain);
		
		spain.addAdacent(portugal);
		spain.addAdacent(france);
		
		france.addAdacent(spain);
		france.addAdacent(uk);
		france.addAdacent(belgium);
		
		uk.addAdacent(france);
		uk.addAdacent(netherlands);
		
		netherlands.addAdacent(uk);
		netherlands.addAdacent(belgium);
		
		belgium.addAdacent(france);
		belgium.addAdacent(netherlands);
		
		//Add some trivia
		Trivia example = new Trivia("What is the language of France?","French");
		Trivia example2 = new Trivia("What is another name for French bread?","Baguette");
		france.addTrivia(example);
		france.addTrivia(example2);
		
		//Create and place the player in the map
		Player p = new Player("Dan", "Blue", portugal);
		portugal.addCharacter(p);
		
		String input;
		Scanner in = new Scanner(System.in);
		Location curr;
		Trivia t;
		while(true) {
			//Output current state of game information
			curr = p.getCurrentLocation();
			System.out.println("You are currently in: " + curr.getName());
			System.out.println("These are the available places to move to: ");
			Set<String> adjacents = curr.namesOfAdjacents();
			for(String loc: adjacents)
				System.out.print("'" + loc + "', ");
			System.out.println("");  //Whitespace to improve output formatting

			//Get player input and check if quit
			System.out.println("Where do you want to go? ");
			input = in.nextLine().trim();
			if(input.equals("quit"))
				break;
			
			//Check if valid move
			if(curr.hasAdacent(input)) {
				curr.moveCharacter(p, input);
				curr = p.getCurrentLocation();
			}
			else
				System.out.println("The location '" + input + "' does not exist");
			
			//Player has entered the area so ask them a trivia question
			t = curr.getTrivia();
			System.out.println(t.getQuestion());
			System.out.println("What is your answer? ");
			input = in.nextLine().trim();
			System.out.println("That is... " + t.checkAnswer(input));
			
			System.out.println("");  //Whitespace to improve output formatting
		}
		
		in.close();
		System.out.println("Exiting the game...");
	}
}
