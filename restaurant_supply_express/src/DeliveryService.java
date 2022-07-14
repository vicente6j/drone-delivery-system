import java.util.HashMap;

public class DeliveryService {
    private String name;
    private Integer revenue;
    private String location;
    private HashMap<String, Employment> employments;
    private HashMap<Integer, Drone> drones;

    public DeliveryService(String name, Integer revenue, String location) {
        this.name = name;
        this.revenue = revenue;
        this.location = location;
        this.employments = new HashMap<>();
        this.drones = new HashMap<>();
    }

    /**
     * Hire method for a delivery service.
     * 
     * @param person Person representing the person in the system to hire.
     * @param people HashMap<String, Person> representing the datastruture that
     *               stores all the people in the system.
     */
    public void hire(Person person, HashMap<String, Person> people) {
        // Validate if the person can be hired
        if (!validateHire(person)) {
            return;
        }
        if (person instanceof WorkerEmployee) { // Person is an existing worker employee
            hire((User) person);
        } else { // Person is not an existing worker employee
            User worker = new WorkerEmployee(person);
            people.put(worker.getUsername(), worker);
            hire(worker);
        }
        System.out.println("OK:new_employee_has_been_hired");
    }

    /**
     * Helper method for hire which creates the worker-service employment
     * relationship.
     * 
     * @param worker User representing the worker to hire
     */
    private void hire(User worker) {
        // Create a new worker employment relationship between the service and worker.
        Employment employment = new Employment(this, worker, "worker");
        this.employments.put(worker.getUsername(), employment);
        // Add the service name to the worker's employers
        worker.getEmployers().add(this.name);
    }

    /**
     * Helper method to validate whether a person can be hired.
     * 
     * @param person Person representing the person to be hired.
     * @return boolean representing if the person can be hired.
     */
    private boolean validateHire(Person person) {
        // Cannot hire a pilot employee.
        if (person instanceof PilotEmployee) {
            System.out.println("ERROR:employee_too_busy_piloting_drones");
            return false;
        }
        // Cannot hire a manager.
        if (person instanceof WorkerEmployee && ((WorkerEmployee) person).getIsManager()) {
            System.out.println("ERROR:too_busy_managing_a_service");
            return false;
        }
        return true;
    }

    /**
     * Fire method for a delivery service
     * 
     * @param person Person representing the person to be fired.
     * @param people HashMap<String, Person> representing the datastruture that
     *               stores all the people in the system.
     */
    public void fire(Person person, HashMap<String, Person> people) {
        // Validate if the person can be fired
        if (!validateFire(person)) {
            return;
        }
        // Remove the service-employee relationship
        this.employments.remove(person.getUsername());
        ((User) person).getEmployers().remove(this.name);
        System.out.println("OK:employee_has_been_fired");
    }

    /**
     * Helper method to validate whether a person can be fired.
     * 
     * @param person Person representing the person to be fired
     * @return boolean representing whether the person can be fired
     */
    private boolean validateFire(Person person) {
        // Cannot fire someone who doesn't work for the service
        if (!this.employments.containsKey(person.getUsername())) {
            System.out.println("ERROR:employee_does_not_work_for_this_service");
            return false;
        }
        // Cannot fire a person if they're a manager
        if (person instanceof WorkerEmployee && ((WorkerEmployee) person).getIsManager()) {
            System.out.println("ERROR:employee_is_managing_a_service");
            return false;
        }
        // Cannot fire a pilot employee
        if (person instanceof PilotEmployee) {
            System.out.println("ERORR:employee_is_piloting_drones");
            return false;
        }
        return true;
    }

    /**
     * Method to appoint a worker employee to be the manager.
     * 
     * @param person Person representing the person to appoint to manager
     * @param people HashMap<String, Person> representing the datastruture that
     *               stores all the people in the system.
     */
    public void appointManager(Person person, HashMap<String, Person> people) {
        // Validate whether a person can be appointed to manager.
        if (!validateAppointManager(person)) {
            return;
        }
        // Demote the old manager to a regular worker
        User currentManager = this.getManager();
        if (currentManager != null) {
            ((WorkerEmployee) currentManager).setIsManager(false);
            this.employments.get(person.getUsername()).setType("worker");
        }
        // Promote the worker to the new manager
        ((WorkerEmployee) person).setIsManager(true);
        this.employments.get(person.getUsername()).setType("manager");
        System.out.println("OK:employee_has_been_appointed_manager");
    }

    /**
     * Helper method to validate whether a person can be appointed manager.
     * 
     * @param person Person representing the person to appoint to manager
     * @return boolean representing if the person can be apppointed manager
     */
    private boolean validateAppointManager(Person person) {
        // Cannot appoint a person who doesn't work for the service
        if (!this.employments.containsKey(person.getUsername())) {
            System.out.println("ERROR:employee_does_not_work_for_this_service");
            return false;
        }
        // Cannot appoint an employee who works for more than 1 service
        if (person instanceof User && ((User) person).getEmployers().size() > 1) {
            System.out.println("ERROR:employee_is_working_at_other_companies");
            return false;
        }
        // Cannot appoint a pilot employee
        if (person instanceof PilotEmployee) {
            System.out.println("ERROR:employee_too_busy_piloting_drones");
            return false;
        }
        return true;
    }

    /**
     * Method to a train a worker and get pilot credentials.
     * 
     * @param person     Person representing the person to train
     * @param license    String representing the pilot license
     * @param experience Integer representing the initial piloting experience
     */
    public void trainPilot(Person person, String license, Integer experience) {
        // Validate whether a person can be trained to be a pilot
        if (!validateTrainPilot(person, experience)) {
            return;
        }
        User employee = (User) person;
        // Set the license and experience of the worker employee
        (employee).setLicense(license);
        (employee).setExperience(experience);
        System.out.println("OK:pilot_has_been_trained");
    }

    private boolean validateTrainPilot(Person person, Integer experience) {
        // Cannot start with negative experience
        if (experience < 0) {
            System.out.println("ERROR:cannot_have_negative_experience");
            return false;
        }
        // Cannot train a person who doesn't work for the service
        if (!this.employments.containsKey(person.getUsername())) {
            System.out.println("ERROR:employee_does_not_work_for_this_service");
            return false;
        }
        // Cannot appoint a pilot if the service doesn't have a valid manager
        if (this.getManager() == null) {
            System.out.println("ERROR:no_valid_manager");
            return false;
        }
        // Cannot train a manager
        if (person instanceof WorkerEmployee && ((WorkerEmployee) person).getIsManager()) {
            System.out.println("ERROR:employee_is_too_busy_managing");
            return false;
        }
        return true;
    }

    /**
     * Method to appoint a worker employee to a pilot.
     * 
     * @param person   Person representing the person to appoint to a pilot
     * @param people   HashMap<String, Person> representing the datastruture that
     *                 stores all the people in the system.
     * @param droneTag Integer representing the drone tag of the drone to control
     */
    public void appointPilot(Person person, HashMap<String, Person> people, Integer droneTag) {
        // Validate if a person can be appointed to be a pilot
        if (!validateAppointPilot(person)) {
            return;
        }
        // Check if the drone exists in the service
        if (!this.drones.containsKey(droneTag)) {
            System.out.println("ERROR:drone_does_not_exist_in_service");
            return;
        }
        // Turn the worker employee into a pilot
        if (!(person instanceof PilotEmployee)) {
            people.put(person.getUsername(), new PilotEmployee(((User) person)));
        }
        // Have the pilot take control of the drone
        ((PilotEmployee) people.get(person.getUsername())).takeDrone(droneTag, this.drones, people);
        System.out.println("OK:employee_has_been_appointed_pilot");
    }

    /**
     * Helper method to validate if a person can be appointed to be a pilot.
     * 
     * @param person Person representing the person to appoint to pilot
     * @return boolean representing if the person can be appointed pilot
     */
    private boolean validateAppointPilot(Person person) {
        // Cannot appoint a pilot who doesn't work for the service
        if (!this.employments.containsKey(person.getUsername())) {
            System.out.println("ERROR:employee_does_not_work_for_this_service");
            return false;
        }
        // Cannot appoint a manager to be a pilot
        if (person instanceof WorkerEmployee && ((WorkerEmployee) person).getIsManager()) {
            System.out.println("ERROR:employee_is_too_busy_managing");
            return false;
        }
        User employee = ((User) person);
        // Cannot appoint a pilot if they are working at more than 1 service
        if ((employee).getEmployers().size() > 1) {
            System.out.println("ERROR:employee_is_working_at_other_companies");
            return false;
        }
        // Cannot appoint a pilot if they don't have a valid license
        if ((employee).getLicense() == null) {
            System.out.println("ERROR:no_valid_license");
            return false;
        }
        return true;
    }

    /**
     * Method to collect revenue from the sales of the drones.
     */
    public void collectRevenue() {
        if (this.getManager() == null) { // Cannot collect revenue if there is no valid manager
            System.out.println("ERROR:the_delivery_service_does_not_have_a_valid_manager");
        } else {
            int revenue = 0;
            // Collect the sales from all the service's drones
            for (Drone drone : this.drones.values()) {
                revenue += drone.getSales();
                drone.setSales(0);
            }
            this.revenue = revenue;
        }
    }

    /**
     * Method to get the manager of the delivery service
     * 
     * @return User representing the manager of the delivery service
     */
    private User getManager() {
        for (Employment employment : this.employments.values()) {
            if (employment.getType().equals("manager")) {
                return employment.getUser();
            }
        }
        return null;
    }

    /**
     * Method to create a delivery service.
     * 
     * @param name     String representing the name of the delivery service
     * @param revenue  Integer revenue representing the amount of initial revenue
     * @param location String representing the location
     * @param services HashMap<String, DeliveryService> representing the data
     *                 structure that stores delivery services
     * @param services HashMap<String, Location> representing the data
     *                 structure that stores locations
     */
    public static void create(String name, Integer revenue, String location,
            HashMap<String, DeliveryService> services, HashMap<String, Location> locations) {
        if (!validateDeliveryService(name, revenue, location, services, locations)) {
            return;
        }
        DeliveryService newDeliveryService = new DeliveryService(name, revenue, location);
        services.put(newDeliveryService.getName(), newDeliveryService);
        System.out.println("OK:delivery_service_created");
    }

    /**
     * Helper method to validate a delivery service creation
     * 
     * @param name     String representing the name of the delivery service
     * @param revenue  Integer represneting the amount of initial revenue
     * @param services HashMap<String, DeliveryService> representing the data
     *                 structure that stores delivery services
     * @param services HashMap<String, Location> representing the data
     *                 structure that stores locations
     * @return boolean representing whether the new delivery service can be created
     */
    private static boolean validateDeliveryService(String name, Integer revenue, String location,
            HashMap<String, DeliveryService> services, HashMap<String, Location> locations) {
        if (locations.get(location) == null) {
            System.out.println("ERROR:location_spot_doesn't_exist.");
        }
        // Cannot start with negative revenue
        if (revenue < 0) {
            System.out.println("ERROR:negative_revenue_not_allowed");
            return false;
        }
        // Delivery service with name already exists
        if (services.get(name) != null) {
            System.out.println("ERROR:service_already_exists");
            return false;
        }
        return true;
    }

    /**
     * Method to display delivery service drones
     */
    public void displayDrones() {
        for (Drone drone : this.drones.values()) {
            System.out.println(drone);
            for (Payload payload : drone.getPayloads().values()) {
                System.out.println(payload);
            }
        }
    }

    /**
     * Method to laod fuel onto a drone.
     * 
     * @param drone_tag Integer representing the drone tag
     * @param petrol    Integer representing the amount of petrol to add
     */
    public void loadFuel(Integer drone_tag, Integer petrol) {
        Drone drone = this.drones.get(drone_tag);
        if (drone == null) {
            System.out.println("ERROR:drone_doesn't_exist_in_service");
            return;
        }
        if (!drone.getLocation().equals(this.location)) {
            System.out.println("ERROR:drone_not_located_at_home_base");
            return;
        }
        // Delivery service needs a worker employee in order to load drones
        boolean hasWorkerEmployee = false;
        for (Employment employment : this.employments.values()) {
            if (employment.getType().equals("worker")) {
                hasWorkerEmployee = true;
            }
        }
        if (!hasWorkerEmployee) {
            System.out.println("ERROR:delivery_service_doesn't_have_workers_to_load_fuel");
            return;
        }
        drone.addFuel(petrol);
        System.out.println("OK:fuel_added");
    }

    /**
     * Method to load an ingredient onto a drone
     * 
     * @param drone_tag   Integer representing the drone tag
     * @param barcode     String representing the ingredient barcode
     * @param quantity    Integer representing the ingredient quantity
     * @param unit_price  Integer representing the ingredient unit price
     * @param ingredients HashMap<String, Ingredient> representing the data
     *                    structure that stores the ingredients
     */
    public void loadIngredient(Integer drone_tag, String barcode, Integer quantity,
            Integer unit_price, HashMap<String, Ingredient> ingredients) {
        Drone drone = this.drones.get(drone_tag);
        // Drone doesn't exist
        if (drone == null) {
            System.out.println("ERROR:delivery_service_doesn't_have_drone");
            return;
        }
        // Drone is not at the home base
        if (!drone.getLocation().equals(this.location)) {
            System.out.println("ERROR:drone_not_located_at_home_base");
            return;
        }
        // Drone doesn't have enought capacity left
        if (drone.getRemainingCapacity() < quantity) {
            System.out.println("ERROR:drone_does_not_have_enough_space");
            return;
        }
        // Unit price cannot be negative
        if (unit_price < 0) {
            System.out.println("ERROR:negative_unit_price_not_allowed");
            return;
        }
        // Delivery service needs a worker employee in order to load drones
        boolean hasWorkerEmployee = false;
        for (Employment employment : this.employments.values()) {
            if (employment.getType().equals("worker")) {
                hasWorkerEmployee = true;
            }
        }
        if (!hasWorkerEmployee) {
            System.out.println("ERROR:delivery_service_doesn't_have_workers_to_load_drone");
            return;
        }
        drone.setRemainingCapacity(drone.getRemainingCapacity() - quantity);
        Payload newPayload = new Payload(quantity, unit_price, ingredients.get(barcode));
        drone.addPayload(newPayload);
        System.out.println("OK:ingredient_loaded");
    }

    /**
     * toString method for delivery service.
     */
    public String toString() {
        return String.format("name: %s, revenue: $%d, location: %s", this.name, this.revenue, this.location);
    }

    /**
     * Method to add a drone to a delivery service
     * 
     * @param drone Drone representing the drone to add
     */
    public void addDrone(Drone drone) {
        this.drones.put(drone.getTag(), drone);
    }

    public String getName() {
        return this.name;
    }

    public Integer getRevenue() {
        return this.revenue;
    }

    public String getLocation() {
        return this.location;
    }

    public HashMap<Integer, Drone> getDrones() {
        return this.drones;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter method to get a drone with a given drone tag
     * 
     * @param droneTag Integer representing the drone tag
     * @return Drone representing the drone found
     */
    public Drone getDrone(Integer droneTag) {
        return this.drones.get(droneTag);
    }
}