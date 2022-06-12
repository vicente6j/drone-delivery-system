import java.util.ArrayList;

public class DeliveryService {
    private String name;
    private Integer revenue;
    private String location;

    public DeliveryService(String name, Integer revenue, String location) {
        this.name = name;
        this.revenue = revenue;
        this.location = location;
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
    public static String getLocation(String name, ArrayList<DeliveryService> deliveryServicesList) {
        for (int i = 0; i < deliveryServicesList.size(); i++) {
            if (deliveryServicesList.get(i).getName().equals(name)) {
                return deliveryServicesList.get(i).getLocation();
            }
        }
        return "";
    }

    public static boolean nameUnique(String name, ArrayList<DeliveryService> deliveryServices){
        for (int i = 0; i < deliveryServices.size(); i++){
            if ( deliveryServices.get(i).getName().equals(name)){
                return false;
            }
        }
        return true;

    }
}