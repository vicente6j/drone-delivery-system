import java.util.Scanner;
import java.util.TreeMap;

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
        // make_ingredient,saf_spc,saffron,4
        // make_ingredient,iku_sfd,ikura,9
        // make_ingredient,truf_fgs,truffles,6
        
        System.out.println("OK:change_completed");
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
        locationList.add(loc);
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
        if (!Location.isValidLocation(located_at, locationList)) {
            System.out.println("ERROR: The delivery service location is invalid.");
        } else {
            DeliveryService newDeliveryService = new DeliveryService(init_name, init_revenue, located_at);
            deliveryServicesList.add(newDeliveryService);
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
         if (!Location.isValidLocation(located_at, locationList)) {
            System.out.println("ERROR: The restaurant location is invalid.");
        } else {
            Restaurant newRestaurant = new Restaurant(init_name, located_at);
            restaurantList.add(newRestaurant);
        }
        System.out.println("OK:change_completed");
    }

    void displayRestaurants() {
        for (int i = 0; i < restaurantList.size(); i++) {
            Restaurant currentRestaurant = restaurantList.get(i);
            String result = "name: " + currentRestaurant.getName() + ", " + "money_spent: $" + currentRestaurant.getMoneySpent() + ", " + "location: " + currentRestaurant.getLocation();
            System.out.println(result);
        }
        System.out.println("OK:display_completed");
    }

    void makeDrone(String service_name, Integer init_tag, Integer init_capacity, Integer init_fuel) { }

    void displayDrones(String service_name) { }

    void displayAllDrones() { }

    void flyDrone(String service_name, Integer drone_tag, String destination_name) { }

    void loadIngredient(String service_name, Integer drone_tag, String barcode, Integer quantity, Integer unit_price) { }

    void loadFuel(String service_name, Integer drone_tag, Integer petrol) { }

    void purchaseIngredient(String restaurant_name, String service_name, Integer drone_tag, String barcode, Integer quantity) { }

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
