import java.util.ArrayList;

public class Drone {
    private String service_name;
    private Integer init_tag;
    private Integer init_capacity;
    private Integer init_fuel;
    private Integer remaining_fuel;
    private Integer sales;

    private String location;

    public Drone(String service_name, Integer init_tag, Integer init_capacity, Integer init_fuel, String location) {
        this.service_name = service_name;
        this.init_tag = init_tag;
        this.init_capacity = init_capacity;
        this.init_fuel= init_fuel;
        this.sales = 0;
        this.remaining_fuel = init_fuel;
        this.location = location;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public Integer getInit_tag() {
        return init_tag;
    }

    public void setInit_tag(Integer init_tag) {
        this.init_tag = init_tag;
    }

    public Integer getInit_fuel() {
        return init_fuel;
    }

    public void setInit_fuel(Integer init_fuel) {
        this.init_fuel = init_fuel;
    }

    public Integer getInit_capacity() {
        return init_capacity;
    }

    public void setInit_capacity(Integer init_capacity) {
        this.init_capacity = init_capacity;
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

    public Integer getRemaining_fuel() {
        return remaining_fuel;
    }

    public void setRemaining_fuel(Integer remaining_fuel) {
        this.remaining_fuel = remaining_fuel;
    }

    public static boolean DroneUnique(Integer tag, String delivery, ArrayList<DeliveryService> deliveryServices, ArrayList<Drone> drones){
        for (int i = 0; i < deliveryServices.size(); i++){
            if (deliveryServices.get(i).getName().equals(delivery)){
                for (int j = 0; j < drones.size(); j++){
                    if (drones.get(j).getInit_tag().equals(tag)){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;

    }


    
}

