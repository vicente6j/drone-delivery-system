import java.util.HashMap;

public class Drone {

    private String serviceName;
    private Integer tag;
    private Integer initCapacity;
    private Integer remainingCapacity;
    private Integer initFuel;
    private Integer remainingFuel;
    private Integer sales = 0;
    private String location;
    private PilotEmployee appointedPilotEmployee;
    private Drone leader;
    private HashMap<String, Payload> payloads;
    private HashMap<Integer, Drone> swarm;

    public Drone(String serviceName, Integer tag, Integer initCapacity,
            Integer initFuel, String location) {
        this.serviceName = serviceName;
        this.tag = tag;
        this.initCapacity = initCapacity;
        this.initFuel = initFuel;
        this.remainingFuel = initFuel;
        this.remainingCapacity = initCapacity;
        this.location = location;
        this.payloads = new HashMap<>();
        this.swarm = new HashMap<>();
    }

    /**
     * Method to create a new drone.
     * 
     * @param service_name  String representing the delivery service name
     * @param init_tag      Integer representing the drone's tag
     * @param init_capacity Integer representing the drone initial capacity
     * @param init_fuel     Integer representing the drone initial fuel
     * @param services      HashMap<String, DeliveryService> representing the data
     *                      structure that stores services
     * @param locations     HashMap<String, Location> representing the data
     *                      structure that stores locations
     */
    public static void create(String service_name, Integer init_tag, Integer init_capacity, Integer init_fuel,
            HashMap<String, DeliveryService> services, HashMap<String, Location> locations) {
        if (!validateDrone(service_name, init_capacity, init_fuel, services, locations)) {
            return;
        }
        DeliveryService deliveryService = services.get(service_name);
        Location location = locations.get(deliveryService.getLocation());
        Drone newDrone = new Drone(service_name, init_tag, init_capacity, init_fuel,
                location.getName());
        deliveryService.addDrone(newDrone);
        location.decreaseRemainingSpace();
        System.out.println("OK:drone_created");
    }

    /**
     * Method to validate a new drone.
     * 
     * @param service_name  String representing the delivery service name
     * @param init_capacity Integer representing the drone initial capacity
     * @param init_fuel     Integer representing the drone initial fuel
     * @param services      HashMap<String, DeliveryService> representing the data
     *                      structure that stores services
     * @param locations     HashMap<String, Location> representing the data
     *                      structure that stores locations
     */
    private static boolean validateDrone(String service_name, Integer init_capacity, Integer init_fuel,
            HashMap<String, DeliveryService> services, HashMap<String, Location> locations) {
        DeliveryService deliveryService = services.get(service_name);
        // Delivery service doesn't exist
        if (deliveryService == null) {
            System.out.println("ERROR:service_does_not_exist");
            return false;
        }
        // Location is out of space
        if (locations.get(deliveryService.getLocation()).getRemaining() == 0) {
            System.out.println("ERROR:location_does_not_have_space");
            return false;
        }
        // Drone cannot have negative capacity
        if (init_capacity < 0) {
            System.out.println("ERROR:negative_capacity_not_allowed");
            return false;
        }
        // Drone can not have negative fuel
        if (init_fuel < 0) {
            System.out.println("ERROR:negative_fuel_not_allowed");
            return false;
        }
        return true;
    }

    /**
     * Method to fly a drone to a destination
     * 
     * @param destination     Location representing the destination to fly the drone
     *                        to
     * @param deliveryService Delivery service representing the service who owns the
     *                        drone
     * @param locations       HashMap<String, Location> representing the data
     *                        structure that stores locations
     */
    public void fly(Location destination, DeliveryService deliveryService, HashMap<String, Location> locations) {
        if (!validateFly(destination, deliveryService, locations)) {
            return;
        }
        boolean isInSwarm = this.leader != null ? true : false;
        boolean isSwarmLeader = this.getSwarm().size() > 0;

        if (isInSwarm || isSwarmLeader) {
            Drone swarmLeader = this.leader == null ? this : this.getLeader();

            for (Drone drone : swarmLeader.getSwarm().values()) {
                drone.fly(destination, locations.get(drone.getLocation()));
            }
            User appointedPilot = ((User) swarmLeader.getAppointedPilotEmployee());
            appointedPilot.increaseExperience();
            System.out.println("OK:swarm_flew");
        } else {
            this.fly(destination, locations.get(this.location));
            User appointedPilot = ((User) this.appointedPilotEmployee);
            appointedPilot.increaseExperience();
            System.out.println("OK:drone_flew");
        }
    }

    /**
     * Method to fly a drone to a location
     * 
     * @param destination     Location representing the destination location
     * @param currentLocation Location representing the current drone location
     */
    private void fly(Location destination, Location currentLocation) {
        int distanceTo = Location.calculateDistance(destination,
                currentLocation);
        this.remainingFuel -= distanceTo;
        this.setLocation(destination.getName());
        currentLocation.increaseRemainingSpace();
        destination.decreaseRemainingSpace();
    }

    /**
     * Method to validate whether a drone or swarm can fly
     * 
     * @param destination     Location representing the destination location
     * @param deliveryService DeliveryService representing the delivery service
     *                        which owns the drones
     * @param locations       HashMap<String, Location> representing the data
     *                        strucure which stores the locations
     * @return boolean representing whether the drone can fly or not
     */
    private boolean validateFly(Location destination, DeliveryService deliveryService,
            HashMap<String, Location> locations) {
        boolean isInSwarm = this.leader != null ? true : false;
        boolean isSwarmLeader = this.getSwarm().size() > 0;
        if (isInSwarm || isSwarmLeader) {
            Drone swarmLeader = this.leader == null ? this : this.getLeader();
            // Swarm leader's pilot doesn't have a valid license
            if (swarmLeader.getAppointedPilotEmployee().getLicense() == null) {
                System.out.println("ERROR:swarm_leader_pilot_doesn't_have_valid_license");
                return false;
            }
            int swarmSize = swarmLeader.getSwarm().size();
            // Not enough space at location for swarm
            if (destination.getRemaining() < swarmSize + 1) {
                System.out.println("ERROR:not_enough_space_to_maneuver_the_swarm_to_that_location");
                return false;
            }
            for (Drone drone : swarmLeader.getSwarm().values()) {
                int distanceTo = Location.calculateDistance(destination,
                        locations.get(drone.getLocation()));
                int distanceBack = Location.calculateDistance(locations.get(deliveryService.getLocation()),
                        destination);
                if (drone.getRemainingFuel() < distanceTo + distanceBack) {
                    System.out.println(
                            "ERROR:a_drone_in_the_swarm_doesn't_have_enough_fuel_to_go_to_destination_and_back_to_homebase");
                    return false;
                }
            }
        } else if (!isInSwarm && !isSwarmLeader) {
            // Drone doesn't have an appointed pilot
            if (this.getAppointedPilotEmployee() == null) {
                System.out.println("ERROR:drone_doesn't_have_a_pilot");
                return false;
            }
            // Drone pilot doesn't have a valid license
            if (this.getAppointedPilotEmployee().getLicense() == null) {
                System.out.println("ERROR:drone_pilot_doesn't_have_a_valid_license");
                return false;
            }
            // Not enough space for drone at destination
            if (destination.getRemaining() == 0) {
                System.out.println("ERROR:destination_location_doesn't_have_enough_space");
                return false;
            }
            int distanceTo = Location.calculateDistance(destination,
                    locations.get(this.location));
            int distanceBack = Location.calculateDistance(locations.get(deliveryService.getLocation()), destination);
            // Not enough fuel to go to destination and back
            if (this.remainingFuel < distanceTo + distanceBack) {
                System.out.println("ERROR:not_enough_fuel_to_go_to_destination_and_back");
                return false;
            }
        }
        return true;
    }

    /**
     * Method to add a new payload containing an ingredient to a drone.
     * 
     * @param newPayload Payload representing the payload to add to the drone
     */
    public void addPayload(Payload newPayload) {
        String ingredientBarcode = newPayload.getIngredientAssociated().getBarcode();
        Payload existingPayload = this.payloads.get(ingredientBarcode);
        if (existingPayload == null) {
            this.payloads.put(ingredientBarcode, newPayload);
        } else {
            if (existingPayload.getIngredientUnitPrice() != newPayload.getIngredientUnitPrice()) {
                System.out.println("ERROR:cannot_add_same_ingredient_with_different_price");
                return;
            } else {
                existingPayload.setIngredientQuantity(
                        existingPayload.getIngredientQuantity() + newPayload.getIngredientQuantity());
            }
        }
    }

    /**
     * Method to increase sales of drone after restuarant purchases ingredient
     * 
     * @param quantity Integer representing the quantity sold
     * @param price    Integer representing the price per unit
     */
    public void conductSale(int quantity, int price) {
        int saleValue = quantity * price;
        this.setSales(this.getSales() + saleValue);
        this.setRemainingCapacity(this.getRemainingCapacity() + quantity);
    }

    /**
     * Method to add a drone to a swarm.
     * 
     * @param leaderDrone     Drone representing the leader swarm of the drone
     * @param deliveryService DeliveryService representing the service that owns the
     *                        drones
     * @param people          HashMap<String, Person> representing the datastructure
     *                        that stors people
     */
    public void joinSwarm(Drone leaderDrone, DeliveryService deliveryService, HashMap<String, Person> people) {
        if (!this.location.equals(leaderDrone.getLocation())) {
            System.out.println("ERROR:leader_drone_and_swarm_drone_at_different_locations");
            return;
        }
        // Cannot join a drone's swarm if it is not the leader of the swarm
        if (leaderDrone.getLeader() != null) {
            System.out.println("ERROR:cannot_join_the_swarm_since_the_drone_is_following_a_leader");
        }
        if (this.swarm.size() > 0) {
            System.out.println("ERROR:swarm_leader_cannot_join_a_different_swarm");
            return;
        }
        if (this.appointedPilotEmployee != null) {
            // Remove the drone from pilot's control
            this.appointedPilotEmployee.renounceDrone(this.tag, people, deliveryService.getEmployments());
            // Remove appointed pilot employee
            this.appointedPilotEmployee = null;
        }
        // Set drone leader
        this.leader = leaderDrone;
        // Add the drone to the leader's swarm
        leaderDrone.addSwarmDrone(this);
        System.out.println("OK:drone_joined_swarm");
    }

    /**
     * Method for a drone to leave a swarm
     */
    public void leaveSwarm() {
        // Cannot leave the swarm if it is the leader
        if (this.swarm.size() > 0) {
            System.out.println("ERROR:leader_cannot_leave_swarm");
            return;
        }
        if (this.getLeader() == null) {
            System.out.println("ERROR:drone_is_not_in_a_swarm");
            return;
        }
        // Set appointed pilot employee to the swarm leader's appointed pilot employee
        PilotEmployee swarmLeaderPilotEmployee = this.getLeader().getAppointedPilotEmployee();
        this.setAppointedPilotEmployee(swarmLeaderPilotEmployee);
        // Remove the swarm drone from the leader's swarm
        this.leader.removeSwarmDrone(this);
        // Remove the leader from the swarm drone
        this.leader = null;
        swarmLeaderPilotEmployee.takeDrone(this);
        System.out.println("OK:drone_left_swarm");
    }

    /**
     * toString for a drone
     */
    public String toString() {
        String pilot = "";
        String swarm = "";
        if (this.appointedPilotEmployee != null) {
            pilot = String.format("\n&> pilot: %s", this.appointedPilotEmployee.getUsername());
        }
        if (this.swarm.size() > 0) {
            String drones = "";
            for (Drone drone : this.swarm.values()) {
                drones += String.format("| %d ", drone.getTag());
            }
            swarm = String.format("\ndrone is directing this swarm: [ drone tags %s]", drones);
        }
        return String.format("tag: %d, capacity: %d, remaining_cap: %d, fuel: %d, sales: $%d, location: %s", this.tag,
                this.initCapacity, this.remainingCapacity, this.remainingFuel, this.sales, this.location) + pilot
                + swarm;

    }

    /**
     * Getter method for the swarm
     * 
     * @return HashMap<Integer, Drone> representing the data structure that stores
     *         swarm drones
     */
    private HashMap<Integer, Drone> getSwarm() {
        return this.swarm;
    }

    /**
     * Getter method for the appointed pilot employee
     * 
     * @return Pilot Employee representing the appointed pilot
     */
    public PilotEmployee getAppointedPilotEmployee() {
        return this.appointedPilotEmployee;
    }

    /**
     * Setter method for the appointed pilot employee
     * 
     * @param pilotEmployee Pilot Employee representing the pilot employee
     */
    public void setAppointedPilotEmployee(PilotEmployee pilotEmployee) {
        this.appointedPilotEmployee = pilotEmployee;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getInitFuel() {
        return initFuel;
    }

    public void setInitFuel(Integer initFuel) {
        this.initFuel = initFuel;
    }

    public Integer getInitCapacity() {
        return initCapacity;
    }

    public void setInitCapacity(Integer initCapacity) {
        this.initCapacity = initCapacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRemainingFuel() {
        return remainingFuel;
    }

    public void setRemainingFuel(Integer remainingFuel) {
        this.remainingFuel = remainingFuel;
    }

    public Integer getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(Integer remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    /**
     * Getter method to get all the payloads from a drone
     * 
     * @return HashMap<String, Payload> representing the data structure that stores
     *         payloads
     */
    public HashMap<String, Payload> getPayloads() {
        return this.payloads;
    }

    /**
     * Get the payload with a certain ingredient barcode
     * 
     * @param ingredientBarcode
     * @return
     */
    public Payload getPayload(String ingredientBarcode) {
        return this.payloads.get(ingredientBarcode);
    }

    /**
     * Method to remove a payload from a drone if there is not more ingredient
     * quantity
     * 
     * @param payload Payload representing the payload to remove
     */
    public void removePayload(Payload payload) {
        this.payloads.remove(payload.getIngredientAssociated().getBarcode());
    }

    /**
     * Method to add fuel to a drone
     * 
     * @param petrol Integer representing the amount of petrol to add
     */
    public void addFuel(Integer petrol) {
        this.setRemainingFuel(this.getRemainingFuel() + petrol);
    }

    /**
     * Method to add a drone into a leader drone's swarm
     * 
     * @param drone Drone representing the swarm drone
     */
    public void addSwarmDrone(Drone drone) {
        this.swarm.put(drone.getTag(), drone);
    }

    /**
     * Method to remove a drone from a leader's swarm
     * 
     * @param drone Drone representing the swarm drone
     */
    public void removeSwarmDrone(Drone drone) {
        this.swarm.remove(drone.getTag());
    }

    /**
     * Method to get the swarm leader
     * 
     * @return Drone representing the swarm leader
     */
    public Drone getLeader() {
        return this.leader;
    }

    public void setLeader(Drone drone) {
        this.leader = drone;
    }
}