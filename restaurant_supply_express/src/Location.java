import java.util.ArrayList;

public class Location {

    private String name;
    private Integer x_coor;
    private Integer y_coor;
    private Integer spaceLimit;
    private Integer remaining;

    public Location(String initName, Integer initx, Integer inity, Integer initSpaceLimit) {
        this.name = initName;
        this.x_coor = initx;
        this.y_coor = inity;
        this.spaceLimit = initSpaceLimit;
        this.remaining = initSpaceLimit;
    }

    public String getName() {
        return this.name;
    }

    public Integer getX() {
        return this.x_coor;
    }

    public Integer getY() {
        return this.y_coor;
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

    public void setX_Coor(Integer newX) {
        this.x_coor = newX;
    }

    public void setY_Coor(Integer newY) {
        this.y_coor = newY;
    }

    public static boolean isValid(String name, ArrayList<Location> locationList) {
        boolean isValid = false;
        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).getName().equals(name)) {
                isValid = true;
            }
        }
        return isValid;
    }

    public static int calculateDistance(String arrival, String departure, ArrayList<Location> locationList ){
        int arrival_x = 0;
        int departure_x = 0;
        int arrival_y = 0;
        int departure_y = 0;
        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).getName().equals(arrival)) {
                arrival_x = locationList.get(i).x_coor;
                arrival_y = locationList.get(i).y_coor;
            }
            if (locationList.get(i).getName().equals(departure)) {
                departure_x = locationList.get(i).x_coor;
                departure_y = locationList.get(i).y_coor;
            }
        }
        if (arrival.equals(departure)) {
            return 0;
        }
        return 1 + (int) Math.floor(Math.sqrt(Math.pow(arrival_x - departure_x, 2) + Math.pow(arrival_y - departure_y, 2)));
    }

    public static boolean hasSpace(String name, ArrayList<Location> locationList) {
        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).getName().equals(name)) {
                return locationList.get(i).getRemaining() > 0;
            }
        }
        return false;
    }

    public static boolean hasSpaceForAll(String name, ArrayList<Location> locationList, Integer numDrones) {
        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).getName().equals(name)) {
                return locationList.get(i).getRemaining() > numDrones;
            }
        }
        return false;
    }

    public static void increaseRemaining(String locationName, ArrayList<Location> locationsList) {
        for (Location location : locationsList) {
            if (location.getName().equals(locationName)) {
                location.setRemaining(location.getRemaining() + 1);
            }
        }
    }

    public static void decreaseRemaining(String locationName, ArrayList<Location> locationsList) {
        for (Location location : locationsList) {
            if (location.getName().equals(locationName)) {
                location.setRemaining(location.getRemaining() - 1);
            }
        }
    }

    public String toString() {
        return "name: " + this.getName() + ", (x,y): " + "(" + this.getX() + "," + this.getY() + ")" + ", space: " + "[" + this.getRemaining() + " / " + this.getSpaceLimit() + "]" + " remaining";
    }
}
