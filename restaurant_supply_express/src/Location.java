public class Location {

    private String name;
    private Integer x_coord;
    private Integer y_coord;
    private Integer spaceLimit;
    private Integer remaining;

    private Location(LocationBuilder builder) {
        this.name = builder.name;
        this.x_coord = builder.x_coord;
        this.y_coord = builder.y_coord;
        this.spaceLimit = builder.spaceLimit;
        this.remaining = builder.spaceLimit;
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

    public static class LocationBuilder {
        private String name;
        private int x_coord;
        private int y_coord;
        private int spaceLimit;
        private int remaining;

        public LocationBuilder(String name, int x_coord, int y_coord) {
            this.name = name;
            this.x_coord = x_coord;
            this.y_coord = y_coord;
        }

        public LocationBuilder spaceLimit(int spaceLimit) {
            this.spaceLimit = spaceLimit;
            return this;
        }

        public LocationBuilder remaining(int remaining) {
            this.remaining = remaining;
            return this;
        }

        public Location build() {
            Location newLocation = new Location(this);
            return newLocation;
        }
    }
}