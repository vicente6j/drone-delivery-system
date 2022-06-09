import java.util.Scanner;
import java.util.TreeMap;

//additional imports
import java.util.ArrayList;

public class InterfaceLoop{

    public InterfaceLoop() { }

    //variables
    public static ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
    public static ArrayList<Object[]> locationList = new ArrayList<Object[]>();

    //sindy
    void makeIngredient(String init_barcode, String init_name, Integer init_weight) { 
        Object[] ingr = new Object[] {init_barcode, init_name, init_weight};
        Ingredient ingr = new Ingredient(init_barcode, init_name, init_weight);
        if(ingredientList.size() != 0) {
            int ok = 0;
            for (int i = 0; i < ingredientList.size(); i++) {
            String a = ingredientList.get(i).getName();
            String b = ingr.getName();
            if(a.compareTo(b) > 0) {
                ingredientList.add(i, ingr);
                ok = 1;
                break;
            }
            }
            if (ok == 0) {
                ingredientList.add(ingr);
            }
        } else {
            ingredientList.add(ingr);
        }
        // make_ingredient,saf_spc,saffron,4
        //make_ingredient,iku_sfd,ikura,9
        //make_ingredient,truf_fgs,truffles,6
        
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

    //person two
    void checkDistance(String departure_point, String arrival_point) { }

    void makeDeliveryService(String init_name, Integer init_revenue, String located_at) { }

    void displayServices() { }

    void makeRestaurant(String init_name, String located_at) { }

    void displayRestaurants() { }

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
