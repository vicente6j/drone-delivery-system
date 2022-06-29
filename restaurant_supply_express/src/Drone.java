import java.util.ArrayList;

public class Drone {

    private String serviceName;
    private Integer initTag;
    private Integer initCapacity;
    private Integer remainingCapacity;

    private Integer initFuel;
    private Integer remainingFuel;
    private Integer sales;
    private ArrayList<Payload> payloads;
    private String location;

    private Pilot appointedPilot;
    private Drone leader;
    private ArrayList<Drone> swarmDrones;

    public Drone(String serviceName, Integer initTag, Integer initCapacity, Integer initFuel, String location) {
        this.serviceName = serviceName;
        this.initTag = initTag;
        this.initCapacity = initCapacity;
        this.initFuel = initFuel;
        this.sales = 0;
        this.remainingFuel = initFuel;
        this.remainingCapacity = initCapacity;
        this.location = location;
        this.payloads = new ArrayList<>();
        this.appointedPilot = null;
        this.leader = null;
        this.swarmDrones = new ArrayList<>();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getInitTag() {
        return initTag;
    }

    public void setInitTag(Integer initTag) {
        this.initTag = initTag;
    }

    public Integer getInitFuel() {
        return initFuel;
    }

    public void setInitFuel(Integer initFuel) {
        this.initFuel = initFuel;
    }

    public Integer getInitCapacity() {
        return initCapacity;
    }

    public void setInitCapacity(Integer initCapacity) {
        this.initCapacity = initCapacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getRemainingFuel() {
        return remainingFuel;
    }

    public void setRemainingFuel(Integer remainingFuel) {
        this.remainingFuel = remainingFuel;
    }

    public Integer getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(Integer remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    public void addPayload(Payload newPayload) {
        boolean found = false;
        for (Payload payload : this.payloads) {
            if (payload.getIngredientAssociated().getBarcode()
                    .equals(newPayload.getIngredientAssociated().getBarcode())) {
                found = true;
                if (payload.getIngredientUnitPrice() == newPayload.getIngredientUnitPrice()) {
                    payload.setIngredientQuantity(payload.getIngredientQuantity() + newPayload.getIngredientQuantity());
                    System.out.println("OK:change_completed");
                } else {
                    System.out
                            .println("OK:the_ingredient_couldn_be_added_as_it_has_a_diffrent_price_than_the_original");
                }
            }
        }
        if (!found) {
            this.payloads.add(newPayload);
            System.out.println("OK:change_completed");
        }
    }

    public ArrayList<Payload> getAllPayloads() {
        return this.payloads;
    }

    public Payload getPayload(String ingredientBarcode) {
        for (Payload payload : this.payloads) {
            if (payload.getIngredientAssociated().getBarcode().equals(ingredientBarcode)) {
                return payload;
            }
        }
        return null;
    }

    public void conductSale(int quantity, int price) {
        int saleValue = quantity * price;
        this.setSales(this.getSales() + saleValue);
        this.setRemainingCapacity(this.getRemainingCapacity() + quantity);
    }

    public void removePayload(Payload payload) {
        this.payloads.remove(payload);
    }

    public void setAppointedPilot(Pilot p) {
        this.appointedPilot = p;
    }

    public Pilot getAppointedPilot() {
        return appointedPilot;
    }

    public void setLeader(Drone d) {
        this.leader = d;
    }

    public Drone getLeader() {
        return leader;
    }

    public void joinSwarm(Drone d) {
        this.setAppointedPilot(null);
        this.setLeader(d);
    }

    public void leaveSwarm() {
        this.setLeader(null);
    }

    public String toString() {
        return "tag: " + this.getInitTag() + ", " + "capacity: " + this.getInitCapacity() + ", " + "remaining_cap: "
                + this.getRemainingCapacity() + ", " + "fuel: " + this.getRemainingFuel() + ", " + "sales: $"
                + this.getSales() + ", " + "location: " + this.getLocation();
    }

    // Reset drone sales to 0 after delivery service collects revenue
    public void resetSales() {
        this.sales = 0;
    }

    public ArrayList<Drone> getSwarmDrones() {
        return swarmDrones;
    }

    public void eraseSwarmDrones() {
        swarmDrones = null;
    }

    public void leaveSwarmDrones(Drone leavingDrone) {
        swarmDrones.remove(leavingDrone);
    }

    public void addFuel(Integer petrol) {
        this.setRemainingFuel(this.getRemainingFuel() + petrol);
    }

    public void subtractFuel(Integer petrol) {
        this.setRemainingFuel(this.getRemainingFuel() - petrol);
    }

    public void updateCapacity(Integer quantity, String name, Integer drone_tag, Integer unit_price, Ingredient i) {
        this.setRemainingCapacity(this.getRemainingCapacity - quantity);
        Payload newPayload = new Payload(name, drone_tag, quantity, unit_price, i);
        this.addPayload(newPayload);
    }

    public void updateLocation(String destination) {
        this.setLocation(destination);
    }
}
