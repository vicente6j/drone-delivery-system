import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class InterfaceLoop {

    public InterfaceLoop() {
    }

    public static ArrayList<Ingredient> ingredientList = new ArrayList<>();
    public static ArrayList<Location> locationList = new ArrayList<>();
    public static ArrayList<DeliveryService> deliveryServicesList = new ArrayList<>();
    public static ArrayList<Restaurant> restaurantList = new ArrayList<>();
    public static HashMap<String, Person> persons = new HashMap<>();
    public static HashMap<String, Worker> workers = new HashMap<>();

    void makeIngredient(String init_barcode, String init_name, Integer init_weight) {
        Ingredient newIngredient = new Ingredient(init_barcode, init_name, init_weight);

        int index = 0;
        boolean added = false;
        if (Ingredient.exists(init_barcode, ingredientList) == null) {
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
    }

    void displayIngredients() {
        for (int i = 0; i < ingredientList.size(); i++) {
            System.out.println(ingredientList.get(i));
        }
        System.out.println("OK:display_completed");
    }

    void makeLocation(String init_name, Integer init_x_coord, Integer init_y_coord, Integer init_space_limit) {
        Location loc = new Location(init_name, init_x_coord, init_y_coord, init_space_limit);
        boolean found = false;
        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).getName().equals(init_name)) {
                found = true;
            }
        }
        if (found == false) {
            locationList.add(loc);
            System.out.println("OK:change_completed");
        } else {
            System.out.println("ERROR:location_already_exists");
        }
    }

    void displayLocations() {
        String result = "";
        for (int i = 0; i < locationList.size(); i++) {
            System.out.println(locationList.get(i));
        }
        System.out.println("OK:display_completed");
    }

    void checkDistance(String departure_point, String arrival_point) {
        Location departureLocation = null;
        Location arrivalLocation = null;

        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).getName().equals(departure_point)) {
                departureLocation = locationList.get(i);
            }
            if (locationList.get(i).getName().equals(arrival_point)) {
                arrivalLocation = locationList.get(i);
            }
        }

        if (departureLocation != null && arrivalLocation != null) {
            int distance = 1 + (int) Math.floor(Math.sqrt(Math.pow(arrivalLocation.getX() - departureLocation.getX(), 2)
                    + Math.pow(arrivalLocation.getY() - departureLocation.getY(), 2)));
            System.out.println("OK:distance = " + distance);
        } else {
            System.out.println("ERROR: Locations are not valid.");
        }
    }

    void makeDeliveryService(String init_name, Integer init_revenue, String located_at) {
        DeliveryService deliveryService = DeliveryService.getServiceByName(init_name, deliveryServicesList);

        if (deliveryService != null) {
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
            String result = "name: " + currentDeliveryService.getName() + ", " + "revenue: $"
                    + currentDeliveryService.getRevenue() + ", " + "location: " + currentDeliveryService.getLocation();
            System.out.println(result);
        }
        System.out.println("OK:display_completed");
    }

    void makeRestaurant(String init_name, String located_at) {
        if (Restaurant.nameUnique(init_name, restaurantList)) {
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
            System.out.println(restaurantList.get(i));
        }
        System.out.println("OK:display_completed");
    }

    void makeDrone(String service_name, Integer init_tag, Integer init_capacity, Integer init_fuel) {
        DeliveryService deliveryService = DeliveryService.getServiceByName(service_name, deliveryServicesList);

        if (deliveryService != null) {
            if (deliveryService.checkIfDroneExists(init_tag)) {
                System.out.println("ERROR:drone_identifier_already_exists");
            } else {
                for (Location location : locationList) {
                    if (deliveryService.getLocation().equals(location.getName())) {
                        if (location.getRemaining() > 0) {
                            Drone newDrone = new Drone(service_name, init_tag, init_capacity, init_fuel,
                                    deliveryService.getLocation());
                            deliveryService.addDrone(newDrone);
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

    void displayDrones(String service_name) {
        DeliveryService deliveryService = DeliveryService.getServiceByName(service_name, deliveryServicesList);

        if (deliveryService != null) {
            deliveryService.displayDrones();
        }
    }

    void displayAllDrones() {
        for (DeliveryService deliveryService : deliveryServicesList) {
            if (deliveryService.getAllDrones().size() > 0) {
                System.out.println("service name [" + deliveryService.getName() + "] drones:");
                deliveryService.displayDrones();
            }
        }
    }

    void flyDrone(String service_name, Integer drone_tag, String destination_name) {
        DeliveryService ds = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        boolean destinationValid = Location.isValid(destination_name, locationList);
        if (ds != null) {
            if (destinationValid) {
                String result = ds.flyDrone(drone_tag, destination_name, locationList);
                System.out.println(result);
            } else {
                System.out.println("ERROR:flight_destination_does_not_exist");
            }
        } else {
            System.out.println("service_does_not_exist");
        }
    }

    void loadIngredient(String service_name, Integer drone_tag, String barcode, Integer quantity, Integer unit_price) {
        Ingredient i = Ingredient.exists(barcode, ingredientList);
        DeliveryService ds = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        if (i != null && ds != null) {
            String result = ds.loadIngredient(i, drone_tag, quantity, unit_price);
            System.out.println(result);
        } else {
            System.out.println("ERROR:ingredient_or_service_does_not_exist");
        }
    }

    void loadFuel(String service_name, Integer drone_tag, Integer petrol) {
        DeliveryService ds = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        if (ds != null) {
            String result = ds.loadFuel(drone_tag, petrol);
            System.out.println(result);
        } else {
            System.out.println("ERROR:service_does_not_exist");
        }
    }

    void purchaseIngredient(String restaurant_name, String service_name, Integer drone_tag, String barcode,
            Integer quantity) {
        DeliveryService deliveryService = DeliveryService.getServiceByName(service_name, deliveryServicesList);

        if (deliveryService != null) {
            Drone drone = deliveryService.getDrone(drone_tag);

            if (drone == null) {
                System.out.println("ERROR:drone_does_not_exist");
                return;
            }
            if (barcode.equals("")) {
                System.out.println("ERROR:ingredient_identifier_does_not_exist");
                return;
            }

            if (!drone.getLocation().equals(Restaurant.getLocation(restaurant_name, restaurantList))) {
                System.out.println("ERROR:drone_not_located_at_restaurant");
                return;
            }

            boolean payloadFound = false;

            for (Payload currentPayload : drone.getAllPayloads()) {
                if (currentPayload.getIngredientAssociated().getBarcode().equals(barcode)) {
                    payloadFound = true;
                    Payload payload = drone.getPayload(barcode);

                    if (Payload.validatePurchase(payload, quantity)) {
                        drone.conductSale(quantity, payload.getIngredientUnitPrice());
                        Restaurant.makePurchase(restaurant_name, quantity, payload.getIngredientUnitPrice(),
                                restaurantList);
                        payload.postSaleUpdate(quantity, drone);
                        System.out.println("OK:change_completed");
                        return;
                    }
                }
            }

            if (!payloadFound) {
                System.out.println("ERROR:ingredient_identifier_does_not_exist");
            }
        }
    }

    // –––––––––––––––––––––––phase 3 new methods

    void makePerson(String init_username, String init_fname, String init_lname, Integer init_year, Integer init_month,
            Integer init_date, String init_address) {
        Person.create(init_username, init_fname, init_lname, init_year, init_month, init_date, init_address,
                persons);
    }

    void displayPersons() {
        Person.displayAll(persons);
    }

    void hireWorker(String service_name, String user_name) {
        DeliveryService deliveryService = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        Person person = Person.getPersonByUsername(user_name, persons);

        if (deliveryService == null) {
            System.out.println("ERROR:the_delivery_service_does_not_exist.");
        }

        if (person == null) {
            System.out.println("ERROR:the_person_does_not_exist.");
        }

        Worker worker = workers.get(user_name);
        if (worker == null) {
            Worker newWorker = new Worker(person.getUsername(), person.getFname(), person.getLname(), person.getDate(),
                    person.getAddress());
            workers.put(user_name, newWorker);
        }

        deliveryService.hire(worker);
    }

    void fireWorker(String service_name, String user_name) {
        DeliveryService deliveryService = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        Person person = Person.getPersonByUsername(user_name, persons);

        if (person == null) {
            System.out.println("ERROR:the_person_does_not_exist.");
        }

        Worker worker = workers.get(user_name);

        if (worker == null) {
            System.out.println("ERROR:the_worker_does_not_exist.");
        }

        deliveryService.fire_worker(worker);
    }

    void appointManager(String service_name, String user_name) {
        DeliveryService deliveryService = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        Person person = Person.getPersonByUsername(user_name, persons);

        if (deliveryService == null) {
            System.out.println("ERROR:the_delivery_service_does_not_exist.");
        }

        if (person == null) {
            System.out.println("ERROR:the_person_does_not_exist.");
        }

        Worker worker = workers.get(user_name);

        if (worker == null) {
            System.out.println("ERROR:the_person_is_not_a_worker.");
        }

        deliveryService.appoint_manager(worker, deliveryService, workers);
    }

    void trainPilot(String service_name, String user_name, String init_license, Integer init_experience) {
        DeliveryService deliveryService = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        Person person = Person.getPersonByUsername(user_name, persons);

        if (deliveryService == null) {
            System.out.println("ERROR:the_delivery_service_does_not_exist.");
        }

        if (person == null) {
            System.out.println("ERROR:the_person_does_not_exist.");
        }

        Worker worker = workers.get(user_name);

        if (worker == null) {
            System.out.println("ERROR:the_person_is_not_a_worker.");
        }

        deliveryService.train_pilot(worker, init_license, init_experience, workers);
    }

    void appointPilot(String service_name, String user_name, Integer drone_tag) {
        DeliveryService deliveryService = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        Worker worker = workers.get(user_name);

        if (worker == null) {
            System.out.println("ERROR:the_worker_does_not_exist");
        }

        deliveryService.appointPilot(worker, deliveryService, drone_tag, workers);
    }

    void joinSwarm(String service_name, Integer lead_drone_tag, Integer swarm_drone_tag) {
        DeliveryService ds = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        if (ds != null) {
            String result = ds.joinSwarm(lead_drone_tag, swarm_drone_tag);
            System.out.println(result);
        } else {
            System.out.println("ERROR:the_values_introduced_are_not_valid");
        }
    }

    void leaveSwarm(String service_name, Integer swarm_drone_tag) {
        DeliveryService ds = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        if (ds != null) {
            String result = ds.leaveSwarm(swarm_drone_tag);
            System.out.println(result);
        } else {
            System.out.println("ERROR:the_values_introduced_are_not_valid");
        }
    }

    void collectRevenue(String service_name) {
        DeliveryService service = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        // Delivery service does not exist
        if (service == null) {
            System.out.println("ERROR:the_delivery_service_does_not_exist");
        } else {
            // Collect revenue
            service.collectRevenue();
        }
    }

    // –––––––––––––––––––––––commandLoop()

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
