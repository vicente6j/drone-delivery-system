import java.util.ArrayList;

import javax.lang.model.element.Element;

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

    public Drone(String serviceName, Integer initTag, Integer initCapacity, Integer initFuel, String location) {
        this.serviceName = serviceName;
        this.initTag = initTag;
        this.initCapacity = initCapacity;
        this.initFuel= initFuel;
        this.sales = 0;
        this.remainingFuel = initFuel;
        this.remainingCapacity = initCapacity;
        this.location = location;
        this.payloads = new ArrayList<>();
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
        for(Payload payload: this.payloads){
            if (payload.getIngredientAssociated().getBar().equals(newPayload.getIngredientAssociated().getBar())) {
                found = true;
                if (payload.getIngredientUnitPrice() == newPayload.getIngredientUnitPrice()) {
                    payload.setIngredientQuantity(payload.getIngredientQuantity() + newPayload.getIngredientQuantity());
                    System.out.println("OK:change_completed");
                } else {
                    System.out.println("OK:the_ingredient_couldn_be_added_as_it_has_a_diffrent_price_than_the_original"); 
                }
            }
        }
        if (!found) {
            this.payloads.add(newPayload);
            System.out.println("OK:change_completed");
        }
    }

    public ArrayList<Payload> getAllPayload() {
        return this.payloads;
    }

    public Payload getPayload(String ingredientBarcode) {
        for (Payload payload : this.payloads) {
            if (payload.getIngredientAssociated().getBar().equals(ingredientBarcode)) {
                return payload;
            }
        }
        return null;
    }

    public static void conductSale(Drone drone, int quantity, int price) {
        int saleValue = quantity * price;
        drone.setSales(drone.getSales() + saleValue);
        drone.setInitCapacity(drone.getInitCapacity() + quantity);
    }

    public void removePayload(Payload payload) {
        this.payloads.remove(payload);
    }
}

