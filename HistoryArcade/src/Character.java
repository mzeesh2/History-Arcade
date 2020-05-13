import java.util.HashMap;
import java.util.Set;

public class Character {
	
	protected String name;
	protected String color;
	protected int points = 0;
	protected Location currentPlace;
	protected HashMap<String, Location> capturedLocations;
	
	private Character() {}
	
	public Character(String name, String color, Location place) {
		this.color = color;
		this.name = name;
		this.points = 0;
		this.currentPlace = place;
		this.capturedLocations = new HashMap<>();
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the amount of points a character has
	 */
	public int getPoints() {
		return points;
	}
	/**
	 * @param points the point amount to set
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the current location of character
	 */
	public Location getCurrentLocation() {
		return currentPlace;
	}
	/**
	 * @param place the currentPlace to set
	 */
	public void setCurrentLocation(Location place) {
		this.currentPlace = place;
	}
	
	/**
	 * @return a key set of all the captured location names
	 */
    public Set<String> capturedLocationNames() {
    	return capturedLocations.keySet();
    }
	/**
	 * @param place the location that has been captured by the character
	 */
	public void capture(Location place) {
		if(place.getOwner() != null && place.getOwner() != this) {  //Take location from other character
			place.getOwner().lostLocation(place);
		}
		
		if(place.getOwner() != this) {  //Only give points if the area captured is not already owned
			this.points = this.points + 1;
			place.setOwner(this);
			capturedLocations.put(place.getName(), place);
		}
	}
	/**
	 * @param place the location that has been lost
	 */
	public void lostLocation(Location place) {
		this.points = this.points - 1;
		capturedLocations.remove(place.getName());
	}
}
