import java.util.HashMap;

public class Restaurant {
    private String name;
    private String location;
    private Integer spent = 0;

    private Restaurant(RestaurantBuilder builder) {
        this.name = builder.name;
        this.location = builder.location;
    }

    /**
     * Method to purchase ingredients from a drone.
     * 
     * @param service_name String representing the delivery service who controls the
     *                     drone
     * @param drone_tag    Integer representing the drone tag
     * @param barcode      String representing the ingredient barcode
     * @param quantity     Integer representing the ingredient quantity
     * @param services     HashMap<String, DeliveryService> representing the data
     *                     structure which stores the services
     */
    public void purchaseIngredients(String service_name, Integer drone_tag, String barcode, Integer quantity,
            HashMap<String, DeliveryService> services) {
        DeliveryService deliveryService = services.get(service_name);
        if (!validatePurchaseIngredients(service_name, drone_tag, barcode, quantity, deliveryService)) {
            return;
        }
        Drone drone = deliveryService.getDrone(drone_tag);
        Payload payload = drone.getPayload(barcode);
        if (payload == null) {
            System.out.println("ERROR:drone_doesn't_have_ingredient");
            return;
        }
        if (quantity > payload.getIngredientQuantity()) {
            System.out.println("ERROR:not_enough_ingredient");
            return;
        }
        this.makePurchase(quantity, payload.getIngredientUnitPrice());
        drone.conductSale(quantity, payload.getIngredientUnitPrice());
        payload.postSaleUpdate(quantity, drone);
        System.out.println("OK:ingredients_sold");
    }

    /**
     * Helper method to validate whether the ingredient desired can be bought.
     * 
     * @param service_name    String representing the delivery service who controls
     *                        the drone
     * @param drone_tag       Integer representing the drone tag
     * @param barcode         String representing the ingredient barcode
     * @param quantity        Integer representing the ingredient quantity
     * @param deliveryService HashMap<String, DeliveryService> representing the data
     *                        structure which stores the services
     * @return boolean representing if the ingredient can be bought
     */
    private boolean validatePurchaseIngredients(String service_name, Integer drone_tag, String barcode,
            Integer quantity,
            DeliveryService deliveryService) {
        // Delivery service doesn't exist
        if (deliveryService == null) {
            System.out.println("ERROR:service_does_not_exist");
            return false;
        }
        Drone drone = deliveryService.getDrone(drone_tag);
        // Drone doesn't exist
        if (drone == null) {
            System.out.println("ERROR:drone_does_not_exist");
            return false;
        }
        // Purchase quantity cannot be negative
        if (quantity < 0) {
            System.out.println("ERROR:cannot_buy_negative_quantity");
            return false;
        }
        // Barcode is empty
        if (barcode.equals("")) {
            System.out.println("ERROR:empty_barcode");
            return false;
        }
        if (!drone.getLocation().equals(this.location)) {
            System.out.println("ERROR:drone_not_located_at_restaurant");
            return false;
        }
        return true;
    }

    /**
     * Method to have a restaurant purchase ingredients
     * 
     * @param quantityProduct  Integer representing the quantity of the product to
     *                         buy
     * @param unitPriceProduct Integer representing the unit price of the product
     */
    public void makePurchase(Integer quantityProduct, Integer unitPriceProduct) {
        this.spent += quantityProduct * unitPriceProduct;
    }

    /**
     * toString for the restaurant
     */
    public String toString() {
        return String.format("name: %s, money_spent: $%d, location: %s", this.name, this.spent, this.location);
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

    public static class RestaurantBuilder {
        private String name;
        private String location;

        public RestaurantBuilder(String name, String location) {
            this.name = name;
            this.location = location;
        }

        public Restaurant build() {
            Restaurant restaurant = new Restaurant(this);
            return restaurant;
        }
    }
}