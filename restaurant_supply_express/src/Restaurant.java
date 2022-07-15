import java.util.*;

public class Restaurant {
    private String name;
    private String location;
    private Double spent = 0.0;
    private Map<DeliveryService, String> memberships;

    public Restaurant(String name, String location) {
        this.name = name;
        this.location = location;
        this.memberships = new HashMap<>(); //change
    }

    /**
     * Method to create a new restaurant
     * 
     * @param name        String representing the name of the restaurant
     * @param location    String representing the location of the restaurant
     * @param restaurants HashMap<String, Restaurant> representing the data strucute
     *                    that stores the restaurants
     * @param locations   HashMap<String, Location> representing the data structure
     *                    that stores the locations
     */
    public static void create(String name, String location, HashMap<String, Restaurant> restaurants,
            HashMap<String, Location> locations) {
        if (!validateRestaurant(name, location, restaurants, locations)) {
            return;
        }
        Restaurant newRestaurant = new Restaurant(name, location);
        restaurants.put(name, newRestaurant);
        System.out.println("OK:restaurant_created");
    }

    /**
     * Helper method to validate the creation of a new restaurant
     * 
     * @param name        String representing the name of the restaurant
     * @param location    String representing the location of the restaurant
     * @param restaurants HashMap<String, Restaurant> representing the data strucute
     *                    that stores the restaurants
     * @param locations   HashMap<String, Location> representing the data structure
     *                    that stores the locations
     */
    private static boolean validateRestaurant(String name, String location, HashMap<String, Restaurant> restaurants,
            HashMap<String, Location> locations) {
        // Restaurant already exists
        if (restaurants.get(name) != null) {
            System.out.println("ERROR:restaurant_already_exists");
            return false;
        }
        // Location doesn't exist
        if (locations.get(location) == null) {
            System.out.println("ERROR:location_spot_doesn't_exist.");
            return false;
        }
        return true;
    }

    /**
     * toString for the restaurant
     */
    public String toString() {
        String basicInfo = String.format("name: %s, money_spent: $%.2f, location: %s", this.name, this.spent, this.location);
        if(!(memberships.isEmpty())) {
            String membership = "";
            for (Map.Entry<DeliveryService, String> ms: memberships.entrySet()) {
                membership += ms.getKey().getName()+ ": " + (String)ms.getValue() + " member\n";
            }
            return basicInfo +"\n& "+membership.substring(0, membership.length()-1);
        }
        else {
            return basicInfo;
        }
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

    public Double getMoneySpent() {
        return this.spent;
    }

    public void setMoneySpent(Double spent) {
        this.spent = spent;
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
        if (deliveryService.getMembership(this) == null) { //changes 
            if(quantity * payload.getIngredientUnitPrice() >= 5) {
                this.makePurchase(deliveryService.priceAfterDiscount(this, quantity, payload.getIngredientUnitPrice()));
                drone.conductSale(deliveryService.priceAfterDiscount(this, quantity, payload.getIngredientUnitPrice()), quantity);
                payload.postSaleUpdate(quantity, drone);
                deliveryService.updateMembership(this, quantity, payload.getIngredientUnitPrice());
                this.memberships.put(deliveryService, deliveryService.getMembership(this));
                System.out.println("OK:ingredients_sold");
            } else {
                System.out.println("ERROR:first_time_customer_minumum_amount(5)_not_reached");
            }
        } else {
            this.makePurchase(deliveryService.priceAfterDiscount(this, quantity, payload.getIngredientUnitPrice()));
            drone.conductSale(deliveryService.priceAfterDiscount(this, quantity, payload.getIngredientUnitPrice()), quantity);
            payload.postSaleUpdate(quantity, drone);
            deliveryService.updateMembership(this, quantity, payload.getIngredientUnitPrice());
            this.memberships.put(deliveryService, deliveryService.getMembership(this));
            System.out.println("OK:ingredients_sold");
        }
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
    public void makePurchase(Double finalPrice) {
        this.spent += finalPrice;
    }

}