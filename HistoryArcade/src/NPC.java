import java.util.*;

public class NPC extends Character {
	
	private int difficulty;
	
	public NPC(String name, String color, Location place, int diffLevel) {
		super(name, color, place);
		if(diffLevel < 0 || diffLevel > 3)  //Check for bounds inclusive (0->3)
			difficulty = 0;
		else  //If scaling value is acceptable
			difficulty = diffLevel;
	}

	/**
	 * @return the name of a random location adjacent to the current location
	 */
	public String chooseRandomAdjacentLocation() {
		String[] locNames = new String[currentPlace.namesOfAdjacents().size()];
		currentPlace.namesOfAdjacents().toArray(locNames);
        Random rand = new Random();  // Generate a random number
        int randomNumber = rand.nextInt(currentPlace.namesOfAdjacents().size());
		return locNames[randomNumber];
	}
	
	/**
	 * @param t the trivia being asked
	 * @return the name of a random location adjacent to the current location
	 */
	public String generateQuestionResponse(Trivia t) {
        Random rand = new Random();  // Generate a random number
        //Success rate based on difficulty 0 -> 12.5%, 1 -> 25%, 2 -> 50%, 3 -> 100%
        if(Math.abs(rand.nextInt()) % (8 / (Math.pow(2, difficulty))) == 0) {  //Chance of correctly answering, based on difficulty scaling
        	return t.getAnswer();
        }
		return "I don't know.";
	}
	
}
