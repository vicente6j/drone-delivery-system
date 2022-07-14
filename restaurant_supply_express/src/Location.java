import java.util.HashMap;

public class Location {

    private String name;
    private Integer x_coord;
    private Integer y_coord;
    private Integer spaceLimit;
    private Integer remaining;

    public Location(String init_name, Integer init_x_coord, Integer init_y_coord, Integer init_space_limit) {
        this.name = init_name;
        this.x_coord = init_x_coord;
        this.y_coord = init_y_coord;
        this.spaceLimit = init_space_limit;
        this.remaining = init_space_limit;
    }

    /**
     * Method to create a new location.
     * 
     * @param name       String representing the location name
     * @param x_coord    Integer representing the location x coordinate
     * @param y_coord    Integer representing the location y coordinate
     * @param spaceLimit Integer representing the location space limit
     * @param locations  HashMap<String, Location> representing the data strucute
     *                   that stores locations
     */
    public static void create(String name, Integer x_coord, Integer y_coord, Integer spaceLimit,
            HashMap<String, Location> locations) {
        if (!validateLocation(name, spaceLimit, locations)) {
            return;
        }
        Location newLocation = new Location(name, x_coord, y_coord, spaceLimit);
        locations.put(name, newLocation);
        System.out.println("OK:location_created");
    }

    /**
     * Helper method to validate the creation of a new location
     * 
     * @param name       String representing the name of the location
     * @param spaceLimit Integer representing the space limit of the location
     * @param locations  HashMap<String, Location> representing the data strucute
     *                   that stores locations
     * @return boolean representing if the new location can be created
     */
    private static boolean validateLocation(String name, Integer spaceLimit, HashMap<String, Location> locations) {
        // Space limit can not be negative
        if (spaceLimit < 0) {
            System.out.println("ERROR:space_limit_cannot_be_negative");
            return false;
        }
        // Location already exists
        if (locations.get(name) != null) {
            System.out.println("ERROR:location_already_exists");
            return false;
        }
        return true;
    }

    /**
     * toString method for a location
     */
    public String toString() {
        return String.format("name: %s, (x,y): (%d, %d), space: [%d / %d] remaining", this.name, this.x_coord,
                this.y_coord, this.remaining, this.spaceLimit);
    }

    public String getName() {
        return this.name;
    }

    public Integer getXCoord() {
        return this.x_coord;
    }

    public Integer getYCoord() {
        return this.y_coord;
    }

    public Integer getSpaceLimit() {
        return this.spaceLimit;
    }

    public Integer getRemaining() {
        return this.remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Method to calculate the distance between two locations
     * 
     * @param arrival   Location representing the arrival location
     * @param departure Location representing the departure location
     * @return int representing the distance
     */
    public static int calculateDistance(Location arrival, Location departure) {
        int arrival_x = arrival.getXCoord();
        int arrival_y = arrival.getYCoord();
        int departure_x = departure.getXCoord();
        int departure_y = departure.getYCoord();

        if (arrival == departure) {
            return 0;
        }
        return 1 + (int) Math
                .floor(Math.sqrt(Math.pow(arrival_x - departure_x, 2) + Math.pow(arrival_y - departure_y, 2)));
    }

    /**
     * Method to increase remaining space when a drone leaves a location
     */
    public void increaseRemainingSpace() {
        this.remaining += 1;
    }

    /**
     * Method to decrease remaining space when a drone arrives at a location
     */
    public void decreaseRemainingSpace() {
        this.remaining -= 1;
    }
}