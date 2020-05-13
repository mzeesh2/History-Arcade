import java.io.*;
import java.util.*;

public class Location {
	private int placeId;  //Used to quickly identify a location
    private String placeName;  //Name of location
    private Character owner;  //Character that has most recently captured this location
    private ArrayList<Character> charactersHere;  //Characters in this location
    private ArrayList<Trivia> challenges;  //Challenge question list
    private HashMap<String, Location> adjacentLocations = new HashMap<>();  //All locations accessible from a location
    
    private static HashMap<Integer, Location> allLocations = new HashMap<>();  //All locations in game map
    
    public Location(int id, String name) {
    	placeId = id;
    	placeName = name;
    	owner = null;
    	charactersHere = new ArrayList<>();
    	challenges = new ArrayList<>();
    	adjacentLocations = new HashMap<>();
    	allLocations.put(id, this);
    }
    
	/**
	 * @return this location's name as String
	 */
    public String getName() {
    	return placeName;
    }
    
    /**
     * @param newOwner the character that has captured this location
     */
    public void setOwner(Character newOwner) {
    	owner = newOwner;
    }
    /**
     * @return owner the character that currently owns this location
     */
    public Character getOwner() {
    	return owner;
    }
    
	/**
	 * @return a random location in the game
	 */
    public static Location getRandomLocation() {
    	ArrayList<Location> locationsList = new ArrayList<>(allLocations.values());
        Random rand = new Random();  // Generate a random number
        int randomNumber = rand.nextInt(locationsList.size());
        return locationsList.get(randomNumber);
    }
    
	/**
	 * @param the id of the location wanting to be retrieved
	 * @return a specific location in the game
	 */
    public static Location getLocationByID(int id) {
        return allLocations.get(id);
    }
    
    
	/**
	 * @param l the location to add to the HashMap of adjacent locations
	 */
    public void addAdacent(Location l) {
    	adjacentLocations.put(l.getName(), l);
    }
	/**
	 * @return whether the given name is the name of an adjacent location
	 */
    public boolean hasAdacent(String s) {
    	return adjacentLocations.containsKey(s);
    }
	/**
	 * @return a set of all the keys in the adjacent location HashMap
	 */
    public Set<String> namesOfAdjacents() {
    	return adjacentLocations.keySet();
    }

	/**
	 * @return a random Trivia from this location
	 */
    public Trivia getTrivia() {
    	if(challenges.size() == 0)
    		return new Trivia("Placeholder Question, Answer is 'A'.","A");
	    Random rand = new Random();
	    int randomNumber = rand.nextInt(challenges.size());
	    return challenges.get(randomNumber);
    }
	/**
	 * @param t the Trivia to add to this Location
	 */
    public void addTrivia(Trivia t) {
    	challenges.add(t);
    }

	/**
	 * @param c the character to add to the ArrayList of players in this location
	 */
    public void addCharacter(Character c) {
    	charactersHere.add(c);
    }
	/**
	 * @param c the character to remove from the ArrayList of players in this location
	 */
    public void removeCharacter(Character c) {
    	charactersHere.remove(c);
    }
	/**
	 * @param c the character to move from this location to the other location
	 * @param name the name of where to move the player to
	 */
    public void moveCharacter(Character c, String name) {
    	charactersHere.remove(c);
    	Location l = adjacentLocations.get(name);
    	l.addCharacter(c);
    	c.setCurrentLocation(l);
    }
}
