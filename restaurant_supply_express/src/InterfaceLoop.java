import java.util.Scanner;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;

public class InterfaceLoop {

    public InterfaceLoop() {
    }

    public static HashMap<String, Ingredient> ingredients = new HashMap<>();
    public static HashMap<String, DeliveryService> services = new HashMap<>();
    public static HashMap<String, Location> locations = new HashMap<>();
    public static HashMap<String, Restaurant> restaurants = new HashMap<>();
    public static HashMap<String, Person> people = new HashMap<>();

    void makeIngredient(String init_barcode, String init_name, Integer init_weight) {
        if (init_weight < 0) {
            System.out.println("ERROR:negative_weight_not_allowed");
            return;
        }
        // Ingredient already exists
        if (ingredients.get(init_barcode) != null) {
            System.out.println("ERROR:ingredient_already_exists");
            return;
        }
        Ingredient newIngredient = new Ingredient.IngredientBuilder(init_barcode, init_name, init_weight).build();
        ingredients.put(init_barcode, newIngredient);
        System.out.println("OK:ingredient_created");
    }

    void displayIngredients() {
        for (Ingredient ingredient : ingredients.values()) {
            System.out.println(ingredient);
        }
        System.out.println("OK:display_completed");
    }

    void makeLocation(String init_name, Integer init_x_coord, Integer init_y_coord, Integer init_space_limit) {
        // Space limit can not be negative
        if (init_space_limit < 0) {
            System.out.println("ERROR:space_limit_cannot_be_negative");
            return;
        }
        // Location already exists
        if (locations.get(init_name) != null) {
            System.out.println("ERROR:location_already_exists");
            return;
        }
        Location newLocation = new Location.LocationBuilder(init_name, init_x_coord, init_y_coord)
                .spaceLimit(init_space_limit).remaining(init_space_limit).build();
        locations.put(init_name, newLocation);
        System.out.println("OK:location_created");
    }

    void displayLocations() {
        for (Location location : locations.values()) {
            System.out.println(location);
        }
        System.out.println("OK:display_completed");
    }

    void checkDistance(String departure_point, String arrival_point) {
        Location departureLocation = locations.get(departure_point);
        Location arrivalLocation = locations.get(arrival_point);
        if (departureLocation == null) {
            System.out.println("ERROR:departure_location_doesn't exist");
            return;
        }
        if (arrivalLocation == null) {
            System.out.println("ERROR:arrival_location_doesn't exist");
            return;
        }
        int distance = Location.calculateDistance(arrivalLocation, departureLocation);
        System.out.println("OK:distance = " + distance);
    }

    void makeDeliveryService(String init_name, Integer init_revenue, String located_at) {
        if (locations.get(located_at) == null) {
            System.out.println("ERROR:location_spot_doesn't_exist.");
            return;
        }
        // Cannot start with negative revenue
        if (init_revenue < 0) {
            System.out.println("ERROR:negative_revenue_not_allowed");
            return;
        }
        // Delivery service with name already exists
        if (services.get(init_name) != null) {
            System.out.println("ERROR:service_already_exists");
            return;
        }
        DeliveryService newDeliveryService = new DeliveryService.DeliveryServiceBuilder(init_name, init_revenue,
                located_at).build();
        services.put(init_name, newDeliveryService);
        System.out.println("OK:delivery_service_created");
    }

    void displayServices() {
        for (DeliveryService deliveryService : services.values()) {
            System.out.println(deliveryService);
        }
        System.out.println("OK:display_completed");
    }

    void makeRestaurant(String init_name, String located_at) {
        // Restaurant already exists
        if (restaurants.get(init_name) != null) {
            System.out.println("ERROR:restaurant_already_exists");
            return;
        }
        // Location doesn't exist
        if (locations.get(located_at) == null) {
            System.out.println("ERROR:location_spot_doesn't_exist.");
            return;
        }
        Restaurant newRestaurant = new Restaurant.RestaurantBuilder(init_name, located_at).build();
        restaurants.put(init_name, newRestaurant);
        System.out.println("OK:restaurant_created");
    }

    void displayRestaurants() {
        for (Restaurant restaurant : restaurants.values()) {
            System.out.println(restaurant);
        }
        System.out.println("OK:display_completed");
    }

    void makeDrone(String service_name, Integer init_tag, Integer init_capacity, Integer init_fuel) {
        DeliveryService deliveryService = services.get(service_name);
        if (deliveryService == null) {
            System.out.println("ERROR:the_service_does_not_exist");
            return;
        }
        deliveryService.makeDrone(init_tag, init_capacity, init_fuel, locations.get(deliveryService.getLocation()));
    }

    void displayDrones(String service_name) {
        DeliveryService deliveryService = services.get(service_name);
        deliveryService.displayDrones();
        System.out.println("OK:display_completed");
    }

    void displayAllDrones() {
        for (DeliveryService deliveryService : services.values()) {
            System.out.println("service name [" + deliveryService.getName() + "] drones:");
            deliveryService.displayDrones();
        }
    }

    void flyDrone(String service_name, Integer drone_tag, String destination_name) {
        DeliveryService deliveryService = services.get(service_name);
        if (deliveryService == null) {
            System.out.println("ERROR:delivery_service_does_not_exist");
            return;
        }
        if (locations.get(destination_name) == null) {
            System.out.println("ERROR:flight_destination_does_not_exist");
            return;
        }
        Drone drone = deliveryService.getDrone(drone_tag);
        if (drone == null) {
            System.out.println("ERROR:drone_identifier_does_not_exist");
            return;
        }
        drone.fly(locations.get(destination_name), deliveryService, locations);
    }

    void loadIngredient(String service_name, Integer drone_tag, String barcode, Integer quantity, Integer unit_price) {
        DeliveryService deliveryService = services.get(service_name);
        if (deliveryService == null) {
            System.out.println("ERROR:delivery_service_does_not_exist");
        }
        deliveryService.loadIngredient(drone_tag, barcode, quantity, unit_price, ingredients);
    }

    void loadFuel(String service_name, Integer drone_tag, Integer petrol) {
        if (petrol < 0) {
            System.out.println("ERROR:cannot_load_negative_fuel");
            return;
        }
        DeliveryService deliveryService = services.get(service_name);
        if (deliveryService == null) {
            System.out.println("ERROR:delivery_service_doesn't_exist");
        }
        deliveryService.loadFuel(drone_tag, petrol);
    }

    void purchaseIngredient(String restaurant_name, String service_name, Integer drone_tag, String barcode,
            Integer quantity) {
        Restaurant restaurant = restaurants.get(restaurant_name);
        if (restaurant == null) {
            System.out.println("ERROR:restaurant_identifier_does_not_exist");
            return;
        }
        restaurant.purchaseIngredients(service_name, drone_tag, barcode, quantity, services);
    }

    void makePerson(String init_username, String init_fname, String init_lname, Integer init_year, Integer init_month,
            Integer init_date, String init_address) {

        if (people.containsKey(init_username)) {
            System.out.println("ERROR:person_already_exists");
            return;
        }
        Date date_format = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(init_year + "-" + init_month + "-" + init_date);
        Person person = new Person(init_username, init_fname, init_lname, formatter.format(date_format), init_address);
        people.put(init_username, person);
        System.out.println("OK:person_created");
    }

    void displayPersons() {
        for (Person person : people.values()) {
            System.out.println(person);
        }
        System.out.println("OK:display_completed");
    }

    void hireWorker(String service_name, String user_name) {
        if (!services.containsKey(service_name)) {
            System.out.println("ERROR:the_service_does_not_exist");
            return;
        }
        if (!people.containsKey(user_name)) {
            System.out.println("ERROR:the_person_does_not_exist");
            return;
        }
        DeliveryService deliveryService = services.get(service_name);
        deliveryService.hire(people.get(user_name), people);
    }

    void fireWorker(String service_name, String user_name) {
        if (!services.containsKey(service_name)) {
            System.out.println("ERROR:the_service_does_not_exist");
            return;
        }
        if (!people.containsKey(user_name)) {
            System.out.println("ERROR:the_person_does_not_exist");
            return;
        }
        DeliveryService deliveryService = services.get(service_name);
        deliveryService.fire(people.get(user_name), people);
    }

    void appointManager(String service_name, String user_name) {
        if (!services.containsKey(service_name)) {
            System.out.println("ERROR:the_service_does_not_exist");
            return;
        }
        if (!people.containsKey(user_name)) {
            System.out.println("ERROR:the_person_does_not_exist");
            return;
        }
        DeliveryService deliveryService = services.get(service_name);
        deliveryService.appointManager(people.get(user_name), people);
    }

    void trainPilot(String service_name, String user_name, String init_license, Integer init_experience) {
        if (!services.containsKey(service_name)) {
            System.out.println("ERROR:the_service_does_not_exist");
            return;
        }
        if (!people.containsKey(user_name)) {
            System.out.println("ERROR:the_person_does_not_exist");
            return;
        }
        DeliveryService deliveryService = services.get(service_name);
        deliveryService.trainPilot(people.get(user_name), init_license, init_experience);
    }

    void appointPilot(String service_name, String user_name, Integer drone_tag) {
        if (!services.containsKey(service_name)) {
            System.out.println("ERROR:delivery_service_does_not_exist");
            return;
        }
        if (!people.containsKey(user_name)) {
            System.out.println("ERROR:the_person_does_not_exist");
            return;
        }
        DeliveryService deliveryService = services.get(service_name);
        deliveryService.appointPilot(people.get(user_name), people, drone_tag);
    }

    void joinSwarm(String service_name, Integer lead_drone_tag, Integer swarm_drone_tag) {
        DeliveryService deliveryService = services.get(service_name);
        if (deliveryService == null) {
            System.out.println("ERROR:delivery_service_does_not_exist");
            return;
        }
        Drone leaderDrone = deliveryService.getDrone(lead_drone_tag);
        if (leaderDrone == null) {
            System.out.println("ERROR:leader_drone_does_not_exist");
            return;
        }
        Drone swarmDrone = deliveryService.getDrone(swarm_drone_tag);
        if (swarmDrone == null) {
            System.out.println("ERROR:swarm_drone_does_not_exist");
            return;
        }
        swarmDrone.joinSwarm(leaderDrone, deliveryService, people);
    }

    void leaveSwarm(String service_name, Integer swarm_drone_tag) {
        DeliveryService deliveryService = services.get(service_name);
        if (deliveryService == null) {
            System.out.println("ERROR:delivery_service_doesn't_exist");
            return;
        }
        Drone swarmDrone = deliveryService.getDrone(swarm_drone_tag);
        if (swarmDrone == null) {
            System.out.println("ERROR:swarm_drone_doesn't_exist");
            return;
        }
        swarmDrone.leaveSwarm();
    }

    void collectRevenue(String service_name) {
        DeliveryService service = services.get(service_name);
        // Delivery service does not exist
        if (service == null) {
            System.out.println("ERROR:the_delivery_service_does_not_exist");
            return;
        }
        // Collect revenue
        service.collectRevenue();
    }

    // ?????????????????????????????????????????????????????????????????????commandLoop()

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
                    makeLocation(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]),
                            Integer.parseInt(tokens[4]));

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
                    makeDrone(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]),
                            Integer.parseInt(tokens[4]));

                } else if (tokens[0].equals("display_drones")) {
                    displayDrones(tokens[1]);

                } else if (tokens[0].equals("display_all_drones")) {
                    displayAllDrones();

                } else if (tokens[0].equals("fly_drone")) {
                    flyDrone(tokens[1], Integer.parseInt(tokens[2]), tokens[3]);

                } else if (tokens[0].equals("load_ingredient")) {
                    loadIngredient(tokens[1], Integer.parseInt(tokens[2]), tokens[3], Integer.parseInt(tokens[4]),
                            Integer.parseInt(tokens[5]));

                } else if (tokens[0].equals("load_fuel")) {
                    loadFuel(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));

                } else if (tokens[0].equals("purchase_ingredient")) {
                    purchaseIngredient(tokens[1], tokens[2], Integer.parseInt(tokens[3]), tokens[4],
                            Integer.parseInt(tokens[5]));

                } else if (tokens[0].equals("make_person")) {
                    makePerson(tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]),
                            Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]), tokens[7]);

                } else if (tokens[0].equals("display_persons")) {
                    displayPersons();

                } else if (tokens[0].equals("hire_worker")) {
                    hireWorker(tokens[1], tokens[2]);
                } else if (tokens[0].equals("fire_worker")) {
                    fireWorker(tokens[1], tokens[2]);
                } else if (tokens[0].equals("appoint_manager")) {
                    appointManager(tokens[1], tokens[2]);
                } else if (tokens[0].equals("train_pilot")) {
                    trainPilot(tokens[1], tokens[2], tokens[3],
                            Integer.parseInt(tokens[4]));
                } else if (tokens[0].equals("appoint_pilot")) {
                    appointPilot(tokens[1], tokens[2], Integer.parseInt(tokens[3]));
                } else if (tokens[0].equals("join_swarm")) {
                    joinSwarm(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
                } else if (tokens[0].equals("leave_swarm")) {
                    leaveSwarm(tokens[1], Integer.parseInt(tokens[2]));
                } else if (tokens[0].equals("collect_revenue")) {
                    collectRevenue(tokens[1]);
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