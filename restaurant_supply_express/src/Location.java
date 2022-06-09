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
        this.remaining = this.remaining - droneNumber;
    }
}