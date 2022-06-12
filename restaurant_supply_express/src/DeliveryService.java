import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryService {
    private String name;
    private Integer revenue;
    private String location;
    private HashMap<Integer, Drone> drones;

    public DeliveryService(String name, Integer revenue, String location) {
        this.name = name;
        this.revenue = revenue;
        this.location = location;
        this.drones = new HashMap<>();
    }

    public String getName() {
        return this.name;
    }

    public Integer getRevenue() {
        return this.revenue;
    }

    public String getLocation() {
        return this.location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean checkIfDroneExists(Integer droneTag) {
        return this.drones.containsKey(droneTag);
    }

    public void addDrone(Drone drone) {
        this.drones.put(drone.getInitTag(), drone);
    }

    public Drone getDrone(Integer droneTag) {
        return this.drones.get(droneTag);
    }

    public void displayDrones() {
        for (Drone drone : this.drones.values()) {
            System.out.println("tag: " + drone.getInitTag() + ", " + "capacity:" + drone.getInitCapacity() + ", " + "remaining_cap: " + drone.getRemainingCapacity() + ", " + "fuel: " + drone.getRemainingFuel() + ", " + "sales: $" + drone.getSales() + ", "  + "location: " + drone.getLocation());
        }
    }

    public static String getLocation(String name, ArrayList<DeliveryService> deliveryServicesList) {
        for (int i = 0; i < deliveryServicesList.size(); i++) {
            if (deliveryServicesList.get(i).getName().equals(name)) {
                return deliveryServicesList.get(i).getLocation();
            }
        }
        return "";
    }

    public static boolean exists(String name, ArrayList<DeliveryService> deliveryServices){
        for (int i = 0; i < deliveryServices.size(); i++){
            if (deliveryServices.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;

    }
}