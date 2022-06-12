import java.util.Scanner;
import java.util.ArrayList;

public class InterfaceLoop{

    public InterfaceLoop() { }

    public static ArrayList<Ingredient> ingredientList = new ArrayList<>();
    public static ArrayList<Location> locationList = new ArrayList<>();
    public static ArrayList<DeliveryService> deliveryServicesList = new ArrayList<>();
    public static ArrayList<Restaurant> restaurantList = new ArrayList<>();
    public static ArrayList<Drone> dronesList = new ArrayList<>();
    public static ArrayList<Payload> payloads = new ArrayList<>();

    void makeIngredient(String init_barcode, String init_name, Integer init_weight) { 
        Ingredient newIngredient = new Ingredient(init_barcode, init_name, init_weight);

        int index = 0;
        boolean added = false;
        if(Ingredient.barcodeUnique(init_barcode, ingredientList)){
            while (index < ingredientList.size()) {
                String a = ingredientList.get(index).getName();
                String b = newIngredient.getName();
                if(a.compareTo(b) > 0) {
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
            result = "barcode: " + ingredientList.get(i).getBar() 
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
                    + ", space: " + "[" + locationList.get(i).getSpaceLimit() + " / " + locationList.get(i).getRemaining() + "]"
                    + " remaining";
            System.out.println(result);
        }
        System.out.println("OK:display_completed");
    }

    
    void checkDistance(String departure_point, String arrival_point) {
        // Ensure that the departure_point and arrival_point parameters refer to valid Locations
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
        if (DeliveryService.nameUnique(init_name, deliveryServicesList)){
            if (!Location.isValidLocation(located_at, locationList)) {
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
            if (!Location.isValidLocation(located_at, locationList)) {
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

    //All drones are created at the same location as their owning delivery service's home base.
    // A drone can only be created if there's available space at that location
    void makeDrone(String service_name, Integer init_tag, Integer init_capacity, Integer init_fuel) {
        if (Drone.DroneUnique(init_tag, service_name, deliveryServicesList, dronesList)){
            for (int i = 0; i < deliveryServicesList.size(); i ++){
                boolean added = false;
                for (int j = 0; j < locationList.size(); j++){
                    if (locationList.get(j).getName().equals(deliveryServicesList.get(i).getLocation()) && locationList.get(j).getRemaining() > 0 ){
                        Drone newDrone = new Drone(service_name, init_tag, init_capacity, init_fuel, locationList.get(j).getName());
                        dronesList.add(newDrone);
                        locationList.get(j).setLimit(locationList.get(j).getSpaceLimit() - 1);
                        added = true;
                        System.out.println("OK:change_completed");
                    }
                }
                if (added == false){
                    System.out.println("ERROR:not_enough_space_to_create_new_drone");
                }
            }
        } else {
            System.out.println("ERROR:drone_identifier_already_exists");
        }
    }

    void displayDrones(String service_name) {
        ArrayList<Drone> sorted_dronesList = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < dronesList.size(); i++) {
            Drone currentDrone = dronesList.get(i);
            if (currentDrone.getService_name().equals(service_name)) {
                    sorted_dronesList.add(index, currentDrone);
                }
            }
        for (int i = 0; i < sorted_dronesList.size() - 1; i++) {
            for(int j = i+1; j < sorted_dronesList.size(); j++){
                if (sorted_dronesList.get(i).getInit_tag() > sorted_dronesList.get(j).getInit_tag()){
                    Drone aux = sorted_dronesList.get(i);
                    sorted_dronesList.set(i, sorted_dronesList.get(j));
                    sorted_dronesList.set(j, aux);
                }
            }
        }
        for (int i = 0; i < sorted_dronesList.size(); i++) {
            String result = "tag: " + sorted_dronesList.get(i).getInit_tag() + ", " + "remaining_cap: " + sorted_dronesList.get(i).getInit_capacity() + ", " + "fuel: " + sorted_dronesList.get(i).getInit_fuel() + ", " + "sales: $" + sorted_dronesList.get(i).getSales() + ", "  + "location: " + sorted_dronesList.get(i).getLocation();
            System.out.println(result);
        }
        //if nothing??
        System.out.println("OK:display_completed");
     }

    void displayAllDrones() { }

    void flyDrone(String service_name, Integer drone_tag, String destination_name) {
        //Remember that a delivery service never wants to leave a drone stranded without enough fuel to return to home base.
        //Therefore, before a drone moves from its current location, it must have enough fuel to reach the intended destination AND must also have enough fuel to get from the destination back to its home base.
        //Also, it must ensure that there is enough space at the intended destination.
        for (int i = 0; i < dronesList.size(); i++){
            if (dronesList.get(i).getService_name().equals(service_name) && dronesList.get(i).getInit_tag().equals(drone_tag)){
                if (Location.isValidLocation(destination_name, locationList)){
                    int destination = Location.calculateDistance(dronesList.get(i).getLocation(), destination_name, locationList);
                    if (destination * 2 <= dronesList.get(i).getInit_fuel()){
                        if (Location.isSpace(destination_name, locationList)){
                            System.out.println("OK:change_completed");
                            dronesList.get(i).setRemaining_fuel(dronesList.get(i).getRemaining_fuel() - destination * 2);
                            //adjust spa e remaining
                        } else{
                            System.out.println("ERROR:not_enough_space_for_the_drone");
                        }
                    } else if (destination <= dronesList.get(i).getInit_fuel() && destination * 2 > dronesList.get(i).getInit_fuel()){
                        System.out.println("ERROR:not_enough_fuel_to_reach_home_base_from_the_destination");
                    } else if (destination > dronesList.get(i).getInit_fuel()) {
                        System.out.println("ERROR:not_enough_fuel_to_reach_the_destination");
                    }
                } else {
                    System.out.println("ERROR:flight_destination_does_not_exist");

                }
            }
        }
     }

    void loadIngredient(String service_name, Integer drone_tag, String barcode, Integer quantity, Integer unit_price) {
        //A number of packages (i.e., quantity) can be loaded onto that drone if - and only if - the drone is located at its service's home base,
        //and the drone has enough free slots to hold the new packages.
        if (Ingredient.barcodeUnique(barcode, ingredientList) == false){
            for (int i = 0 ; i < dronesList.size(); i++){
                if (dronesList.get(i).getService_name().equals(service_name) && dronesList.get(i).getInit_tag().equals(drone_tag)){
                    if (dronesList.get(i).getLocation().equals(DeliveryService.getLocation(dronesList.get(i).getService_name(), deliveryServicesList))){
                        if(dronesList.get(i).getInit_capacity() >= quantity){
                            dronesList.get(i).setInit_capacity(dronesList.get(i).getInit_capacity()-quantity);
                            Payload load = new Payload(service_name, drone_tag, barcode, quantity, unit_price);
                            payloads.add(load);
                            System.out.println("OK:change_completed");
                        } else{
                            System.out.println("ERROR:drone_does_not_have_enough_space");
                        }
                    } else{
                        System.out.println("ERROR:drone_not_located_at_home_base");
                    }
                }
            }
        } else {
            System.out.println("ERROR:ingredient_identifier_does_not_exist");
        }
     }

    void loadFuel(String service_name, Integer drone_tag, Integer petrol) {
        //A drone can be refueled if - and only if - the drone is located at its service's home base.
        for (int i = 0 ; i < dronesList.size(); i++){
            if (dronesList.get(i).getService_name().equals(service_name) && dronesList.get(i).getInit_tag().equals(drone_tag)){
                if (dronesList.get(i).getLocation().equals(DeliveryService.getLocation(dronesList.get(i).getService_name(), deliveryServicesList))){
                    if(dronesList.get(i).getRemaining_fuel() < dronesList.get(i).getInit_fuel()){
                        if (dronesList.get(i).getInit_fuel() - dronesList.get(i).getRemaining_fuel() >= petrol) {
                            dronesList.get(i).setRemaining_fuel(dronesList.get(i).getRemaining_fuel() + petrol); 
                        } else {
                            dronesList.get(i).setRemaining_fuel(dronesList.get(i).getInit_fuel()); 
                        }
                    }
                }
            }
        }

    }

    void purchaseIngredient(String restaurant_name, String service_name, Integer drone_tag, String barcode, Integer quantity) {
        //A restaurant can only purchase ingredients from a drone when the drone is at the restaurant's location.
        //Also, the drone must be carrying enough of the ingredient (i.e., quantity) being requested - otherwise, the order must fail/be refused in its entirety (i.e., no "partial orders").
        if (Ingredient.barcodeUnique(barcode, ingredientList) == false){
            for (int i  = 0; i < dronesList.size(); i++){
                if (dronesList.get(i).getInit_tag().equals(drone_tag) && dronesList.get(i).getService_name().equals(service_name)) {
                    if(dronesList.get(i).getLocation().equals(Restaurant.getLocation(restaurant_name, restaurantList))){
                        Payload load = Payload.getPayload(barcode, service_name, drone_tag, payloads);
                        if(load != null){
                            if(load.getQuantity() > quantity){
                                dronesList.get(i).setSales(dronesList.get(i).getSales() +  Payload.getPrice(barcode, service_name, drone_tag, payloads, quantity));
                                dronesList.get(i).setInit_capacity(dronesList.get(i).getInit_capacity() + quantity);
                                Payload.updatePayload(barcode, service_name, drone_tag, payloads, quantity);
                                System.out.println("OK:change_completed");
                            }
                        } else{
                            System.out.println("ERROR:drone_doesn't_have that_ingredient");
                        }
                    } else{
                        System.out.println("ERROR:drone_not_located_at_restaurant");
                    }
                }
            }
        } else {
            System.out.println("ERROR:ingredient_identifier_does_not_exist");
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
