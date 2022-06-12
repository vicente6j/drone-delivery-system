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

    public void setLimit(Integer newSpaceLimit) {
        this.spaceLimit = newSpaceLimit;
    }

    //subtracts remaining when drone lands
    public void droneLand(Integer droneNumber) {
        if (this.remaining < droneNumber) {
            System.out.println("Not enough spaces.");
        } else {
            this.remaining = this.remaining - droneNumber;
        }
    }

    public static boolean isValidLocation(String name, ArrayList<Location> locationList) {
        boolean isValid = false;
        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).getName().equals(name)) {
                isValid = true;
            }
        }
        return isValid;
    }

    public static int calculateDistance(String arrival, String departure,ArrayList<Location> locationList ){
        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        int found  = 0;
        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).getName().equals(arrival)) {
                x1 = locationList.get(i).x_coor;
                y1 = locationList.get(i).y_coor;
                found++;
            }
            if (locationList.get(i).getName().equals(departure)) {
                x2 = locationList.get(i).x_coor;
                y2 = locationList.get(i).y_coor;
                found++;
            }
        }
        if (found == 2){
            return 1 + (int) Math.floor(Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
        } else{
            return -1;
        }

    }
    public static boolean isSpace(String name, ArrayList<Location> locationList) {
        boolean isValid = false;
        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).getName().equals(name)) {
                if (locationList.get(i).getSpaceLimit() > 0){
                    isValid = true;
                }
            }
        }
        return isValid;
    }


}