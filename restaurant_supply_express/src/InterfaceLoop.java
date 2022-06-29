import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.Date;

public class InterfaceLoop{

    public InterfaceLoop() {}

    public static ArrayList<Ingredient> ingredientList = new ArrayList<>();
    public static ArrayList<Location> locationList = new ArrayList<>();
    public static ArrayList<DeliveryService> deliveryServicesList = new ArrayList<>();
    public static ArrayList<Restaurant> restaurantList = new ArrayList<>();
    public static ArrayList<Person> personsList = new ArrayList<>();
    public static ArrayList<Pilot> pilotsList = new ArrayList<>();

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
        if (!Location.isValid(init_name, locationList)) {
            locationList.add(loc);
            System.out.println("OK:change_completed");
        } else {
            System.out.println("ERROR:location_already_exists");
        }
    }

    void displayLocations() {
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
            int distance = 1 + (int) Math.floor(Math.sqrt(Math.pow(arrivalLocation.getX() - departureLocation.getX(), 2) + Math.pow(arrivalLocation.getY() - departureLocation.getY(), 2)));
            System.out.println("OK:distance = " + distance);
        } else {
            System.out.println("ERROR: Locations are not valid.");
        }
    }

    void makeDeliveryService(String init_name, Integer init_revenue, String located_at) {
        if (!DeliveryService.exists(init_name, deliveryServicesList)) {
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
            String result = "name: " + currentDeliveryService.getName() 
                    + ", " + "revenue: $" + currentDeliveryService.getRevenue() + ", " 
                    + "location: " + currentDeliveryService.getLocation();
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
        DeliveryService service = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        if (service != null) {
            if (service.checkIfDroneExists(init_tag)) {
                System.out.println("ERROR:drone_identifier_already_exists");
            }
            for (Location location : locationList) {
                if (service.getLocation().equals(location.getName())) {
                    if (location.getRemaining() > 0) {
                        Drone newDrone = new Drone(service_name, init_tag, init_capacity, init_fuel, service.getLocation());
                        service.addDrone(newDrone);
                        location.setRemaining(location.getRemaining() - 1);
                        System.out.println("OK:change_completed");
                    } else {
                        System.out.println("ERROR:not_enough_space_to_create_new_drone");
                    }
                }
            }
        } else {
            System.out.println("ERROR:service_does_not_exist");
        }
    }

    void displayDrones(String service_name) {
        for (DeliveryService service : deliveryServicesList) {
            if (service.getName().equals(service_name)) {
                service.displayDrones();
            }
        }
        System.out.println("OK:display_completed");
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

    void purchaseIngredient(String restaurant_name, String service_name, Integer drone_tag, String barcode, Integer quantity) {
        for (DeliveryService service : deliveryServicesList) {
            if (service.getName().equals(service_name)) {
                Drone drone = service.getDrone(drone_tag);

                if (drone == null) {
                    System.out.println("ERROR:drone_does_not_exist");
                    break;
                }
                if(barcode.equals("")){
                    System.out.println("ERROR:ingredient_identifier_does_not_exist");
                    break;
                }

                if (!drone.getLocation().equals(Restaurant.getLocation(restaurant_name, restaurantList))) {
                    System.out.println("ERROR:drone_not_located_at_restaurant");
                    break;
                }

                boolean payloadFound = false;

                for (Payload currentPayload : drone.getAllPayloads()) {
                    if (currentPayload.getIngredientAssociated().getBarcode().equals(barcode)) {
                        payloadFound = true;
                        Payload payload = drone.getPayload(barcode);

                        if (Payload.validatePurchase(payload, quantity)) {
                            drone.conductSale(quantity, payload.getIngredientUnitPrice());
                            Restaurant.makePurchase(restaurant_name, quantity, payload.getIngredientUnitPrice(), restaurantList);
                            payload.postSaleUpdate(quantity, drone);
                            System.out.println("OK:change_completed");
                            break;
                        }
                    }
                }

                if (!payloadFound) {
                    System.out.println("ERROR:ingredient_identifier_does_not_exist");
                }
            }
        }
     }

     //–––––––––––––––––––––––phase 3 new methods


     void makePerson(String init_username, String init_fname, String init_lname, Integer init_year, Integer init_month, Integer init_date, String init_address) {
        boolean validUser = true;
        for (Person p : personsList) {
            if(p.getUsername().equals(init_username)) {
                System.out.println("ERROR:username_exists");
                validUser = false;
                break;
            }
        }
        if(validUser) {
            int index = 0;
            boolean added = false;
            Date date_format = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(init_year + "-" + init_month + "-" + init_date);
            Person per = new Person(init_username, init_fname, init_lname,formatter.format(date_format), init_address, new ArrayList<DeliveryService>());
            while (index < personsList.size()) {
                String a = personsList.get(index).getUsername();
                String b = per.getUsername();
                if (a.compareTo(b) > 0) {
                    personsList.add(index, per);
                    added = true;
                    break;
                }
                index++;
            }
            if (!added) {
                personsList.add(personsList.size(), per);
            }
            System.out.println("OK:person_created");
        }
     }

     void displayPersons() {
        for(Person p: personsList) {
            System.out.println(p);
        }
        System.out.println("OK:display_completed");
     }

     void hireWorker(String service_name, String user_name) {
        DeliveryService ds = null;
        Person p = null;
        for(DeliveryService d: deliveryServicesList) {
            if(d.getName().equals(service_name)) {
                ds = d;
                break;
            }
        }
        for(Person per: personsList) {
            if(per.getUsername().equals(user_name)) {
                p = per;
                break;
            }
        }
        if (ds!= null & p !=null){
            String result = ds.hire_worker(p);
            System.out.println(result);
            } else {
                System.out.println("ERROR: the_values_introduced_are_not_valid");
            }
     }


     void fireWorker(String service_name, String user_name) { 
        DeliveryService ds = null;
        Person p = null;
        for(DeliveryService d: deliveryServicesList) {
            if(d.getName().equals(service_name)) {
                ds = d;
                break;
            }
        }
        for(Person per: personsList) {
            if(per.getUsername().equals(user_name)) {
                p = per;
                break;
            }
        }
        if (ds!= null & p !=null){
        String result = ds.fire_worker(p);
        System.out.println(result);
        } else {
            System.out.println("ERROR: the_values_introduced_are_not_valid");
        }
     }
     void appointManager(String service_name, String user_name) {
        DeliveryService ds = null;
        Person p = null;
        for(DeliveryService d: deliveryServicesList) {
            if(d.getName().equals(service_name)) {
                ds = d;
                break;
            }
        }
        for(Person per: personsList) {
            if(per.getUsername().equals(user_name)) {
                p = per;
                break;
            }
        }
        if (ds!= null & p !=null){
        String result = ds.appoint_manager(p);
        System.out.println(result);
        } else {
            System.out.println("ERROR: the_values_introduced_are_not_valid");
        }
      }

      void trainPilot(String service_name, String user_name, String init_license, Integer init_experience) {
        DeliveryService ds = null;
        Person p = null;
        for(DeliveryService d: deliveryServicesList) {
            if(d.getName().equals(service_name)) {
                ds = d;
                break;
            }
        }
        for(Person per: personsList) {
            if(per.getUsername().equals(user_name)) {
                p = per;
                break;
            }
        }
        if (ds!= null & p !=null){
        String result = ds.train_pilot(p, init_license, init_experience);
        if (result.equals("OK:pilot_has_been_trained")){
            Pilot pilot = new Pilot(ds.getName(), p.getUsername(), p.getFname(), p.getLname(), p.getDate(), p.getAddress(),p.getEmployedIn(), init_license, init_experience);
            personsList.remove(p);
            personsList.add((Person)pilot);
            pilotsList.add(pilot);
        }
        System.out.println(result);
        } else {
            System.out.println("ERROR: the_values_introduced_are_not_valid");
        }
       }

    void appointPilot(String service_name, String user_name, Integer drone_tag) {
        DeliveryService ds = DeliveryService.getServiceByName(service_name, deliveryServicesList);
        Pilot p = Pilot.getPilotByName(user_name, pilotsList);
        if (ds != null && p != null) {
            String result = ds.appointPilot(p, drone_tag);
            System.out.println(result); 
        } else {
            System.out.println("ERROR:the_values_introduced_are_not_valid");
        }
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


     //–––––––––––––––––––––––commandLoop() 

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

                } else if (tokens[0].equals("make_person")) {
                    makePerson(tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]), tokens[7]);

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
