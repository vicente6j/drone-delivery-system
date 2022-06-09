public class Restaurant {
    private String name;
    private String location;
    private Integer spent = 0;

    public Restaurant(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public String getLocation() {
        return this.location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMoneySpent() {
        return this.spent;
    }

    public void setMoneySpent(Integer spent) {
        this.spent = spent;
    }
}