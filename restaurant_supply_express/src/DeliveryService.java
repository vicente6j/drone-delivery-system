import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryService {
    private String name;
    private Integer revenue;
    private String location;
    private HashMap<Integer, Drone> drones;
    private HashMap<String, Worker> employees;
    private Integer numOfPilots = 0;
    private Worker manager;

    public DeliveryService(String name, Integer revenue, String location) {
        this.name = name;
        this.revenue = revenue;
        this.location = location;
        this.drones = new HashMap<>();
        this.employees = new HashMap<>();
        this.manager = null;
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

    public HashMap<Integer, Drone> getAllDrones() {
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

    public boolean checkIfDroneExists(Integer droneTag) {
        return this.drones.containsKey(droneTag);
    }

    public void addDrone(Drone drone) {
        this.drones.put(drone.getInitTag(), drone);
    }

    public Drone getDrone(Integer droneTag) {
        return this.drones.get(droneTag);
    }

    public void displayDrones() {
        for (Drone drone : this.drones.values()) {
            System.out.println(drone);
            if (drone.getAllPayloads().size() > 0) {
                for (Payload payload : drone.getAllPayloads()) {
                    if (payload.getIngredientQuantity() > 0) {
                        System.out.println(payload);
                    }
                }
            }
            if (drone.getAppointedPilot() != null) {
                System.out.println("&> pilot: " + drone.getAppointedPilot().getUsername());
            }
            String result = "";
            if (drone.getSwarmDrones() != null && drone.getSwarmDrones().size() > 0) {
                result = "&> drone is directing this swarm: [ drone tags | ";
                for (Drone swarm_D : drone.getSwarmDrones()) {
                    result += swarm_D.getInitTag() + " | ";
                }
                result = result.substring(0, result.length() - 2) + "]";
                System.out.println(result);
            }
        }
        System.out.println("OK:display_completed");
    }

    public static String getLocation(String name, ArrayList<DeliveryService> deliveryServicesList) {
        for (int i = 0; i < deliveryServicesList.size(); i++) {
            if (deliveryServicesList.get(i).getName().equals(name)) {
                return deliveryServicesList.get(i).getLocation();
            }
        }
        return "";
    }

    // ––––––––––phase 3 new methods
    public void hire(Worker worker) {
        // Check if employee is instance of Manager or Pilot
        if (validateWorkerHire(worker)) {
            this.employees.put(worker.getUsername(), worker);
            worker.getHired(this);
            System.out.println("OK:new_employee_has_been_hired");
        }
    }

    private boolean validateWorkerHire(Worker worker) {
        if (worker instanceof Manager) {
            System.out.println("ERROR:cannot_hire_a_manager");
            return false;
        } else if (worker instanceof Pilot) {
            System.out.println("ERROR:cannot_hire_a_pilot");
            return false;
        }
        return true;
    }

    public void fire_worker(Worker worker) {
        if (!this.employees.containsKey(worker.getUsername())) {
            System.out.println("ERROR:this_is_not_your_employee");
            return;
        }
        if (worker instanceof Manager) {
            System.out.println("ERROR:cannot_fire_a_manager");
            return;
        }
        if (worker instanceof Pilot && (((Pilot) worker).getControlledDrones().size() > 0)) {
            System.out.println("ERROR:cannot_fire_a_pilot_controlling_drones");
            return;
        }
        this.employees.remove(worker.getUsername());
        worker.getFired(this);
        System.out.println("ERROR:employee_has_been_fired");
    }

    public void appoint_manager(Worker worker, DeliveryService deliveryService, HashMap<String, Worker> workers) {
        boolean isValidManager = this.validateManagerAppointment(worker);

        Worker manager = new Manager(worker.getUsername(), worker.getFname(), worker.getLname(), worker.getDate(),
                worker.getAddress(), deliveryService);
        workers.replace(worker.getUsername(), manager);

        if (isValidManager) {
            this.manager = manager;
            System.out.println("OK:person_has_been_appointed_manager");
        }
    }

    private boolean validateManagerAppointment(Worker worker) {
        String workerUsername = worker.getUsername();
        if (!this.employees.containsKey(workerUsername)) {
            System.out.println("ERROR:the_employee_must_be_an_employee");
            return false;
        }

        if (worker instanceof Pilot) {
            System.out.println("ERROR:the_manager_cannot_be_a_pilot");
            return false;
        }

        if (worker.getWorksForCount() > 1) {
            System.out.println("ERROR:the_manager_cannot_work_for_other_services");
            return false;
        }
        return true;
    }

    private boolean validatePilotTrain(Worker worker, HashMap<String, Worker> workers) {
        if (!this.employees.containsKey(worker.getUsername())) {
            System.out.println("ERROR:the_employee_must_be_an_employee");
            return false;
        }

        if (worker instanceof Manager) {
            System.out.println("ERROR:the_pilot_cannot_be_a_manager");
            return false;
        }

        if (worker.getWorksForCount() > 1) {
            System.out.println("ERROR:the_pilot_cannot_work_for_other_services");
            return false;
        }

        if (worker instanceof Pilot) {
            System.out.println("ERROR:already_a_pilot");
            return false;
        }

        return true;
    }

    public void train_pilot(Worker worker, String init_license, Integer init_experience,
            HashMap<String, Worker> workers) {

        if (this.manager == null) {
            System.out.println("ERROR:service_does_not_have_valid_manager");
            return;
        }

        if (validatePilotTrain(worker, workers)) {
            worker.getTrained(init_license, init_experience);
        }
    }

    public void appointPilot(Worker worker, DeliveryService deliveryService, Integer drone_tag,
            HashMap<String, Worker> workers) {
        Drone drone = this.drones.get(drone_tag);
        if (drone == null) {
            System.out.println("ERROR:drone_does_not_exist_at_this_delivery_service");
        }

        if (!this.employees.containsKey(worker.getUsername())) {
            System.out.println("ERROR:pilot_does_not_work_for_service");
            return;
        }

        if (worker.getLicenseID() == null) {
            System.out.println("ERROR:worker_does_not_have_valid_pilot_license");
            return;
        }

        if (drone.getAppointedPilot() != null) {
            ((Pilot) drone.getAppointedPilot()).subtractAppointedDrone(drone);
        }

        Worker pilot = new Pilot(worker.getUsername(), worker.getFname(),
                worker.getLname(), worker.getDate(),
                worker.getAddress(), worker.getLicenseID(), worker.getExperience());
        workers.replace(worker.getUsername(), pilot);

        ((Pilot) pilot).addAppointedDrone(drone);

        System.out.println("OK:pilot_has_been_appointed_to_drone");
    }

    public String joinSwarm(Integer lead_drone_tag, Integer swarm_drone_tag) {
        Drone lead_drone = getDrone(lead_drone_tag);
        Drone swarm_drone = getDrone(swarm_drone_tag);
        String str = null;
        if (lead_drone != null && swarm_drone != null) {
            if (lead_drone.getLocation().equals(swarm_drone.getLocation())) {
                if (lead_drone.getAppointedPilot() != null) {
                    swarm_drone.joinSwarm(lead_drone);
                    swarm_drone.eraseSwarmDrones();
                    lead_drone.getSwarmDrones().add(swarm_drone);
                    str = "OK:swarm_drone_has_joined_lead_drone";
                } else {
                    str = "ERROR:lead_drone_has_no_pilot";
                }
            } else {
                str = "ERROR:drones_have_different_locations";
            }
        } else {
            str = "ERROR:one_or_both_drones_do_not_exist_at_this_delivery_service";
        }
        return str;
    }

    public String leaveSwarm(Integer swarm_drone_tag) {
        Drone swarm_drone = getDrone(swarm_drone_tag);
        String str = null;
        if (swarm_drone != null) {
            Drone swarm_drone_leader = getDrone(swarm_drone.getLeader().getInitTag());
            swarm_drone_leader.leaveSwarmDrones(swarm_drone);
            swarm_drone.leaveSwarm();
            str = "OK:change_completed";
        } else {
            str = "ERROR:drone_does_not_exist_at_this_delivery_service";
        }
        return str;
    }

    // Transfer sales from delivery service drones to delivery service revenue
    public void collectRevenue() {
        // Check if delivery service has a valid manager.
        if (!this.hasValidManager()) {
            System.out.println("ERROR:the_delivery_service_does_not_have_a_valid_manager");
        } else {
            int revenue = 0;
            // Loop through delivery service drones
            for (Drone drone : this.drones.values()) {
                // Add each drone sales to revenue
                revenue += drone.getSales();
                // Reset drone sales to 0
                drone.resetSales();
            }
            // Update service revenue
            this.revenue = revenue;
        }
    }

    // Public static method to get delivery service by name.
    // Returns reference to delivery service if found, else null.
    public static DeliveryService getServiceByName(String service_name,
            ArrayList<DeliveryService> deliveryServicesList) {
        DeliveryService deliveryService = null;
        for (DeliveryService service : deliveryServicesList) {
            if (service.getName().equals(service_name)) {
                deliveryService = service;
                break;
            }
        }
        return deliveryService;
    }

    public String loadFuel(Integer drone_tag, Integer petrol) {
        Drone loadingDrone = getDrone(drone_tag);
        String str = null;
        if (loadingDrone != null) {
            if (loadingDrone.getLocation().equals(this.getLocation())) {
                if (employees.size() > this.numOfPilots + 1) {
                    loadingDrone.setRemainingFuel(loadingDrone.getRemainingFuel() + petrol);
                    str = "OK:change_completed";
                } else {
                    str = "ERROR:there_are_not_enough_employees";
                }
            } else {
                str = "ERROR:drone_not_located_at_home_base";
            }
        } else {
            str = "ERROR:drone_does_not_exist_at_this_delivery_service";
        }
        return str;
    }

    public String loadIngredient(Ingredient i, Integer drone_tag, Integer quantity, Integer unit_price) {
        Drone loadingDrone = getDrone(drone_tag);
        String str = null;
        if (loadingDrone != null) {
            if (loadingDrone.getLocation().equals(this.getLocation())) {
                if (loadingDrone.getRemainingCapacity() >= quantity) {
                    if (employees.size() > this.numOfPilots + 1) {
                        loadingDrone.setRemainingCapacity(loadingDrone.getRemainingCapacity() - quantity);
                        Payload newPayload = new Payload(this.getName(), drone_tag, quantity, unit_price, i);
                        loadingDrone.addPayload(newPayload);
                        str = "OK:change_completed";
                    } else {
                        str = "ERROR:there_are_not_enough_employees";
                    }
                } else {
                    str = "ERROR:drone_does_not_have_enough_space";
                }
            } else {
                str = "ERROR:drone_not_located_at_home_base";
            }
        } else {
            str = "ERROR:drone_does_not_exist_at_this_delivery_service";
        }
        return str;
    }

    public String flyDrone(Integer drone_tag, String destination_name, ArrayList<Location> locationList) {
        Drone flyingDrone = getDrone(drone_tag);
        String str = null;
        boolean validFlight = false;
        /*
         * Validates identity of leading drone and iterates over a new ArrayList<Drone>
         * including leading drone and swarm
         ** to see if whole coup passes conditions
         */
        if (flyingDrone != null) {
            if (flyingDrone.getAppointedPilot() != null) {
                ArrayList<Drone> coup = flyingDrone.getSwarmDrones();
                coup.add(flyingDrone);
                for (int i = 0; i < coup.size(); i++) {
                    int distanceTo = Location.calculateDistance(coup.get(i).getLocation(), destination_name,
                            locationList);
                    int distanceBack = Location.calculateDistance(destination_name, coup.get(i).getLocation(),
                            locationList);
                    if (coup.get(i).getRemainingFuel() >= distanceTo + distanceBack) {
                        if (Location.hasSpaceForAll(destination_name, locationList, coup.size())) {
                            validFlight = true;
                            str = "OK:change_complete";
                        } else {
                            str = "ERROR:not_enough_space_to_maneuver_the_swarm_to_that_location";
                        }
                    } else if (coup.get(i).getRemainingFuel() >= distanceTo) {
                        str = "ERROR:not_enough_fuel_to_reach_home_base_from_the_destination";
                    } else if (coup.get(i).getRemainingFuel() < distanceTo) {
                        str = "ERROR:not_enough_fuel_to_reach_the_destination";
                    }
                }
                coup.remove(flyingDrone);
            } else {
                str = "ERROR:drone_is_a_swarm_drone";
            }
        } else {
            str = "ERROR:drone_does_not_exist_at_this_delivery_service";
        }
        // After validating all conditions first, iterates over coup and updates all
        // drones
        if (validFlight) {
            ArrayList<Drone> coup = flyingDrone.getSwarmDrones();
            coup.add(flyingDrone);
            for (int i = 0; i < coup.size(); i++) {
                int distanceTo = Location.calculateDistance(coup.get(i).getLocation(), destination_name, locationList);
                coup.get(i).setRemainingFuel(coup.get(i).getRemainingFuel() - distanceTo);
                Location.increaseRemaining(coup.get(i).getLocation(), locationList);
                coup.get(i).setLocation(destination_name);
                Location.decreaseRemaining(destination_name, locationList);
            }
            coup.remove(flyingDrone);
        }
        return str;
    }

    // Check if delivery service manager is valid.
    private boolean hasValidManager() {
        return this.manager != null;
    }
}
