import java.util.Scanner;
import java.util.ArrayList;

public class InterfaceLoop{

    public InterfaceLoop() { }

    public static ArrayList<Ingredient> ingredientList = new ArrayList<>();
    public static ArrayList<Location> locationList = new ArrayList<>();
    public static ArrayList<DeliveryService> deliveryServicesList = new ArrayList<>();
    public static ArrayList<Restaurant> restaurantList = new ArrayList<>();

    void makeIngredient(String init_barcode, String init_name, Integer init_weight) { 
        Ingredient newIngredient = new Ingredient(init_barcode, init_name, init_weight);

        int index = 0;
        boolean added = false;
        if(Ingredient.exists(init_barcode, ingredientList) == null){
            while (index < ingredientList.size()) {
                String a = ingredientList.get(index).getName();
                String b = newIngredient.getName();
                if (a.compareTo(b) > 0) {
                    ingredientList.add(index, newIngredient);
                    added = true;
                    break;
                }
                index++;
            }
            if (!added) {
                ingredientList.add(ingredientList.size(), newIngredient);
            }
            System.out.println("OK:change_completed");
        } else {
            System.out.println("ERROR:ingredient_already_exists");
        }
        // make_ingredient,saf_spc,saffron,4
        // make_ingredient,iku_sfd,ikura,9
        // make_ingredient,truf_fgs,truffles,6
        
    }

    void displayIngredients() { 
        String result = "";
        for (int i = 0; i < ingredientList.size(); i++) {
            result = "barcode: " + ingredientList.get(i).getBarcode() 
                    + ", name: " + ingredientList.get(i).getName()
                    + ", unit_weight: " + ingredientList.get(i).getWeight();
            System.out.println(result);
        }
        System.out.println("OK:display_completed");
    }

    void makeLocation(String init_name, Integer init_x_coord, Integer init_y_coord, Integer init_space_limit) { 
        Location loc = new Location(init_name, init_x_coord, init_y_coord, init_space_limit);
        boolean found = false;
        for (int i = 0; i < locationList.size(); i++){
            if(locationList.get(i).getName().equals(init_name)){
                found = true;
            }
        }
        if(found == false){
            locationList.add(loc);
            System.out.println("OK:change_completed");
        } else{
            System.out.println("ERROR:location_already_exists");
        }
    }

    void displayLocations() { 
        String result = "";
        for (int i = 0; i < locationList.size(); i++) {
            result = "name: " + locationList.get(i).getName() 
                    + ", (x,y): " + "(" + locationList.get(i).getX() + "," + locationList.get(i).getY() + ")"
                    + ", space: " + "[" + locationList.get(i).getRemaining() + " / " + locationList.get(i).getSpaceLimit() + "]"
                    + " remaining";
            System.out.println(result);
        }
        System.out.println("OK:display_completed");
    }

    
    void checkDistance(String departure_point, String arrival_point) {
        Location departureLocation = null;
        Location arrivalLocation = null;
        
        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).getName().equals(departure_point)) {
                departureLocation = locationList.get(i);
            } else if (locationList.get(i).getName().equals(arrival_point)) {
                arrivalLocation = locationList.get(i);
            }
        }
        
        if (departureLocation != null && arrivalLocation != null) {
            int distance = 1 + (int) Math.floor(Math.sqrt(Math.pow(arrivalLocation.getX() - departureLocation.getX(), 2) + Math.pow(arrivalLocation.getY() - departureLocation.getY(), 2)));
            System.out.println("OK:distance = " + distance);
        } else {
            System.out.println("ERROR: Locations are not valid.");
        }
    }

    void makeDeliveryService(String init_name, Integer init_revenue, String located_at) {
        if (!DeliveryService.exists(init_name, deliveryServicesList)){
            if (!Location.isValid(located_at, locationList)) {
                System.out.println("ERROR: The delivery service location is invalid.");
            } else {
                DeliveryService newDeliveryService = new DeliveryService(init_name, init_revenue, located_at);
                deliveryServicesList.add(newDeliveryService);
                System.out.println("OK:change_completed");
            }
        } else {
            System.out.println("ERROR:service_identifier_already_exist");
        }
    }

    void displayServices() {
        for (int i = 0; i < deliveryServicesList.size(); i++) {
            DeliveryService currentDeliveryService = deliveryServicesList.get(i);
            String result = "name: " + currentDeliveryService.getName() + ", " + "revenue: $" + currentDeliveryService.getRevenue() + ", " + "location: " + currentDeliveryService.getLocation();
            System.out.println(result);
        }
        System.out.println("OK:display_completed");
    }

    void makeRestaurant(String init_name, String located_at) { 
         if (Restaurant.nameUnique(init_name, restaurantList)){
            if (!Location.isValid(located_at, locationList)) {
                System.out.println("ERROR: The restaurant location is invalid.");
            } else {
                Restaurant newRestaurant = new Restaurant(init_name, located_at);
                restaurantList.add(newRestaurant);
            }
            System.out.println("OK:change_completed");
         } else {
            System.out.println("ERROR:restaurant_already_exist");
         }
    }

    void displayRestaurants() {
        for (int i = 0; i < restaurantList.size(); i++) {
            Restaurant currentRestaurant = restaurantList.get(i);
            String result = "name: " + currentRestaurant.getName() + ", " + "money_spent: $" + currentRestaurant.getMoneySpent() + ", " + "location: " + currentRestaurant.getLocation();
            System.out.println(result);
        }
        System.out.println("OK:display_completed");
    }


    void makeDrone(String serviceName, Integer init_tag, Integer init_capacity, Integer init_fuel) {
        for (DeliveryService service : this.deliveryServicesList) {
            if (service.getName().equals(serviceName)) {
                if (service.checkIfDroneExists(init_tag)) {
                    System.out.println("ERROR:drone_identifier_already_exists");
                    break;
                }

                for (Location location : this.locationList) {
                    if (service.getLocation().equals(location.getName())) {
                        if (location.getRemaining() > 0) {
                            Drone newDrone = new Drone(serviceName, init_tag, init_capacity, init_fuel, service.getLocation());
                            service.addDrone(newDrone);
                            location.setRemaining(location.getRemaining() - 1);
                            System.out.println("OK:change_completed");
                        } else {
                            System.out.println("ERROR:not_enough_space_to_create_new_drone");
                        }
                        break;
                    }
                }
            }
        }
    }

    void displayDrones(String serviceName) {
        for (DeliveryService service : this.deliveryServicesList) {
            if (service.getName().equals(serviceName)) {
                service.displayDrones();
            }
        }
        System.out.println("OK:display_completed");
     }

    void displayAllDrones() {
        for (DeliveryService deliveryService : deliveryServicesList){
            if (deliveryService.getAllDrones().size() > 0){
                System.out.println("service name [" + deliveryService.getName() + "] drones:");
                deliveryService.displayDrones();
            }
        }
     }

    void flyDrone(String serviceName, Integer droneTag, String destination_name) {
        for (DeliveryService service : this.deliveryServicesList) {
            if (service.getName().equals(serviceName)) {
                Drone drone = service.getDrone(droneTag);

                if (Location.isValid(destination_name, locationList)) {
                    int distanceToDestination = Location.calculateDistance(drone.getLocation(), destination_name, locationList);
                    int distanceToHomeBaseFromDestination = Location.calculateDistance(destination_name, service.getLocation(), locationList);

                    if (drone.getRemainingFuel() >= distanceToDestination + distanceToHomeBaseFromDestination) {
                        if (Location.hasSpace(destination_name, locationList)) {
                            drone.setRemainingFuel(drone.getRemainingFuel() - distanceToDestination);
                            Location.increaseRemaining(drone.getLocation(), locationList);
                            drone.setLocation(destination_name);
                            Location.decreaseRemaining(destination_name, locationList);
                            System.out.println("OK:change_completed");
                        } else  {
                            System.out.println("ERROR:not_enough_space_for_the_drone");
                        }
                    } else if (drone.getRemainingFuel() >= distanceToDestination) {
                        System.out.println("ERROR:not_enough_fuel_to_reach_home_base_from_the_destination");
                    } else if (drone.getRemainingFuel() < distanceToDestination) {
                        System.out.println("ERROR:not_enough_fuel_to_reach_the_destination");
                    }
                } else {
                    System.out.println("ERROR:flight_destination_does_not_exist");
                }
            }
        }
     }

    void loadIngredient(String serviceName, Integer droneTag, String barcode, Integer quantity, Integer unitPrice) {
        if (Ingredient.exists(barcode, ingredientList) != null) {
            Ingredient ingredientPayload = Ingredient.exists(barcode, ingredientList);
            for (DeliveryService service : this.deliveryServicesList) {
                if (service.getName().equals(serviceName)) {
                    Drone drone = service.getDrone(droneTag);

                    if (drone == null) {
                        System.out.println("ERROR:drone_does_not_exist");
                        break;
                    }
                    if (drone.getLocation().equals(DeliveryService.getLocation(drone.getServiceName(), deliveryServicesList))) {
                        if (drone.getRemainingCapacity() >= quantity) {
                            drone.setRemainingCapacity(drone.getRemainingCapacity() - quantity);
                            Payload newPayload = new Payload(serviceName, droneTag, quantity, unitPrice, ingredientPayload);
                            drone.addPayload(newPayload);//here have to check if we already have it
                        } else{
                            System.out.println("ERROR:drone_does_not_have_enough_space");
                        }
                    } else {
                        System.out.println("ERROR:drone_not_located_at_home_base");
                    }
                }
            }
        } else {
            System.out.println("ERROR:ingredient_identifier_does_not_exist");
        }
     }

    void loadFuel(String serviceName, Integer droneTag, Integer petrol) {
        for (DeliveryService service : this.deliveryServicesList) {
            if (service.getName().equals(serviceName)) {
                Drone drone = service.getDrone(droneTag);

                if (drone == null) {
                    System.out.println("ERROR:drone_does_not_exist");
                    break;
                }

                if (drone.getLocation().equals(DeliveryService.getLocation(drone.getServiceName(), deliveryServicesList))) {
                    if (drone.getRemainingFuel() < drone.getInitFuel()) {
                        if (drone.getInitFuel() - drone.getRemainingFuel() >= petrol)  {
                            drone.setRemainingFuel(drone.getRemainingFuel() + petrol);
                        } else {
                            drone.setRemainingFuel(drone.getInitFuel());
                        }
                        System.out.println("OK:change_completed");
                    }
                } else {
                    System.out.println("ERROR:drone_not_located_at_home_base");
                }
            }
        }
    }

    void purchaseIngredient(String restaurantName, String serviceName, Integer droneTag, String barcode, Integer quantity) {
        for (DeliveryService service : this.deliveryServicesList) {
            if (service.getName().equals(serviceName)) {
                Drone drone = service.getDrone(droneTag);

                if (drone == null) {
                    System.out.println("ERROR:drone_does_not_exist");
                    break;
                }

                if (!drone.getLocation().equals(Restaurant.getLocation(restaurantName, restaurantList))) {
                    System.out.println("ERROR:drone_not_located_at_restaurant");
                    break;
                }

                boolean payloadFound = false;

                for (Payload currentPayload : drone.getAllPayloads()) {
                    if (currentPayload.getIngredientAssociated().getBarcode().equals(barcode)) {
                        payloadFound = true;
                        Payload payload = drone.getPayload(barcode);

                        if (Payload.validatePurchase(payload, quantity)) {
                            drone.conductSale(payload.getIngredientQuantity(), payload.getIngredientUnitPrice());
                            Restaurant.makePurchase(restaurantName, payload.getIngredientQuantity(), payload.getIngredientUnitPrice(), restaurantList);
                            payload.postSaleUpdate(quantity, drone);
                            System.out.println("OK:change_completed");
                        }
                    }
                }

                if (!payloadFound) {
                    System.out.println("ERROR:drone_doesn't_have that_ingredient");
                }
            }
        }
     }

    public void commandLoop() {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        String[] tokens;
        final String DELIMITER = ",";

        while (true) {
            try {
                // Determine the next command and echo it to the monitor for testing purposes
                wholeInputLine = commandLineInput.nextLine();
                tokens = wholeInputLine.split(DELIMITER);
                System.out.println("> " + wholeInputLine);

                if (tokens[0].indexOf("//") == 0) {
                    // deliberate empty body to recognize and skip over comments

                } else if (tokens[0].equals("make_ingredient")) {
                    makeIngredient(tokens[1], tokens[2], Integer.parseInt(tokens[3]));

                } else if (tokens[0].equals("display_ingredients")) {
                    displayIngredients();

                } else if (tokens[0].equals("make_location")) {
                    makeLocation(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));

                } else if (tokens[0].equals("display_locations")) {
                    displayLocations();

                } else if (tokens[0].equals("check_distance")) {
                    checkDistance(tokens[1], tokens[2]);

                } else if (tokens[0].equals("make_service")) {
                    makeDeliveryService(tokens[1], Integer.parseInt(tokens[2]), tokens[3]);

                } else if (tokens[0].equals("display_services")) {
                    displayServices();

                } else if (tokens[0].equals("make_restaurant")) {
                    makeRestaurant(tokens[1], tokens[2]);

                } else if (tokens[0].equals("display_restaurants")) {
                    displayRestaurants();

                } else if (tokens[0].equals("make_drone")) {
                    makeDrone(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));

                } else if (tokens[0].equals("display_drones")) {
                    displayDrones(tokens[1]);

                } else if (tokens[0].equals("display_all_drones")) {
                    displayAllDrones();

                } else if (tokens[0].equals("fly_drone")) {
                    flyDrone(tokens[1], Integer.parseInt(tokens[2]), tokens[3]);

                } else if (tokens[0].equals("load_ingredient")) {
                    loadIngredient(tokens[1], Integer.parseInt(tokens[2]), tokens[3], Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]));

                } else if (tokens[0].equals("load_fuel")) {
                    loadFuel(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));

                } else if (tokens[0].equals("purchase_ingredient")) {
                    purchaseIngredient(tokens[1], tokens[2], Integer.parseInt(tokens[3]), tokens[4], Integer.parseInt(tokens[5]));

                } else if (tokens[0].equals("stop")) {
                    System.out.println("stop acknowledged");
                    break;

                } else {
                    System.out.println("command " + tokens[0] + " NOT acknowledged");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println();
            }
        }

        System.out.println("simulation terminated");
        commandLineInput.close();
    }

    void displayMessage(String status, String text_output) {
        System.out.println(status.toUpperCase() + ":" + text_output.toLowerCase());
    }
}
