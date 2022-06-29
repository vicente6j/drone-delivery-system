import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryService {
    private String name;
    private Integer revenue;
    private String location;
    private HashMap<Integer, Drone> drones;
    private ArrayList<Person> employees;
    private Person manager;
    private ArrayList<Pilot> pilots;

    public DeliveryService(String name, Integer revenue, String location) {
        this.name = name;
        this.revenue = revenue;
        this.location = location;
        this.drones = new HashMap<>();
        this.employees = new ArrayList<Person>();
        this.manager = null;
        this.pilots = new ArrayList<Pilot>();
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

    public ArrayList<Person> getEmployees() {
        return this.employees;
    }

    public HashMap<Integer, Drone> getAllDrones( ) {
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
            if (drone.getAllPayloads().size() > 0){
                for (Payload payload: drone.getAllPayloads()){
                    if (payload.getIngredientQuantity() > 0){
                        System.out.println(payload);
                    }
                }
            }
            if (drone.getAppointedPilot() != null){
                System.out.println("&> pilot: " + drone.getAppointedPilot().getUsername());
            }
            String result = "";
            if (drone.getSwarmDrones()!= null && drone.getSwarmDrones().size() > 0) {
                result = "&> drone is directing this swarm: [ drone tags | ";
                for (Drone swarm_D : drone.getSwarmDrones()) {
                            result += swarm_D.getInitTag() + " | ";
                }
                result = result.substring(0, result.length() - 2) + "]";
                System.out.println(result);
            }
        }
    }

    public static String getLocation(String name, ArrayList<DeliveryService> deliveryServicesList) {
        for (int i = 0; i < deliveryServicesList.size(); i++) {
            if (deliveryServicesList.get(i).getName().equals(name)) {
                return deliveryServicesList.get(i).getLocation();
            }
        }
        return "";
    }

    public static boolean exists(String name, ArrayList<DeliveryService> deliveryServices){
        for (int i = 0; i < deliveryServices.size(); i++){
            if (deliveryServices.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    //––––––––––phase 3 new methods

    public String hire_worker(Person p) {
        if (p instanceof Manager) {
            return "ERROR:employee_can't_be_hired";
        } else if(p instanceof Pilot && ((Pilot)p).getEmployedby() != null 
                && !((Pilot)p).getEmployedby().equals(this.getName())) {
            return "ERROR:person_piloting_can't_be_hired";
            //     else {
            //     p.setEmployed(true);
            //     p.getEmployedIn().add(this);
            //     this.employees.add(p);
            //     return "OK:new_employee_has_been_hired";
            //     }
        } else {
            p.getEmployedIn().add(this);
            this.employees.add(p);
            return "OK:new_employee_has_been_hired";
        }
    }


    public String fire_worker(Person p) {
        if (p.equals(manager)) {
            return "ERROR:employee_is_managing_a_service";
        } else if (p instanceof Pilot && ((Pilot)p).getEmployedby().equals(this.getName())) {
            return "ERROR:person_piloting_can't_be_fired";
        } else if (employees.contains(p)) {
            p.getEmployedIn().remove(this);
            this.employees.remove(p);
            return "OK:employee_has_been_fired";
        }
        return "ERROR: the_person_wasn't_hired";
    }

    public String appoint_manager(Person p) {
        if (employees.contains(p)) {
            if (!pilots.contains(p)) {
                if (p.getEmployedIn().size() <= 1) {
                    if (manager != null) {
                        for (Person employee : employees) {
                            if (employee.equals(manager)) {
                                employee.setManaging("");
                            }
                        }
                    }
                    manager = p;
                    p.setManaging(this.getName());
                    return "OK:employee_has_been_appointed_manager";
                } else {
                    return "ERROR:employee_is_working_at_other_companies";
                }
            } else {
                System.out.println("ERROR:employee_is_working_as_a_pilot");
            }
        }
        return "ERROR:employee_does_not_work_for_this_service";
    }

    public String train_pilot(Person p, String init_license, Integer init_experience) {
        if (this.works_for(p)) {
            if (!p.equals(manager)) {
                if (!(this.pilots_for(p))) {
                    if (manager != null) {
                        Pilot pilot = new Pilot(this.name, p.getUsername(), p.getFname(), 
                            p.getLname(), p.getDate(), p.getAddress(),p.getEmployedIn(), 
                            init_license, init_experience);
                        pilots.add(pilot);
                        return "OK:pilot_has_been_trained";
                    } else {
                        return "ERROR:the_service_doesn't_have_a_valid_manager";
                    }
                } else {
                    return "ERROR:employee_is_already_working_as_a_pilot";
                }
            } else {
                return "ERROR:employee_is_too_busy_managing";
            }
        }
        return "ERROR:employee_does_not_work_for_this_service";
    }

    public String appointPilot(Pilot pilot, Integer drone_tag) {
        Drone d = getDrone(drone_tag);
        if (d == null) {
            return "ERROR:drone_does_not_exist_at_this_delivery_service";
        }
        if (this.pilots_for(pilot)) {
            if (pilot.getLicenseID() != null) {
                if (pilot.getManaging().equals("")) {
                    if (pilot.getEmployedIn().size() <= 1) {
                        if (d.getAppointedPilot() != null) {
                            d.getAppointedPilot().subtractAppointedDrone(d);
                        }
                        pilot.addAppointedDrone(d);
                        d.setAppointedPilot(pilot);
                        return "OK:pilot_has_been_appointed_to_drone";
                    } else {
                        return "ERROR:employee_is_working_at_more_than_one_company";
                    }
                } else {
                    return "ERROR:employee_is_working_as_a_manager";
                }
            } else {
                return "ERROR:employee_does_not_have_valid_pilot_license";
            }
        } else {
            return "ERROR:employee_is_not_working_for_this_service";
        }
    }
    
    public String joinSwarm(Integer lead_drone_tag, Integer swarm_drone_tag) {
        Drone lead_drone = getDrone(lead_drone_tag);
        Drone swarm_drone = getDrone(swarm_drone_tag);
        if (lead_drone != null && swarm_drone != null) {
            if (lead_drone.getLocation().equals(swarm_drone.getLocation())) {
                if (lead_drone.getAppointedPilot() != null) {
                    swarm_drone.joinSwarm(lead_drone);
                    swarm_drone.eraseSwarmDrones();
                    lead_drone.getSwarmDrones().add(swarm_drone);
                    return "OK:swarm_drone_has_joined_lead_drone";
                } else {
                    return "ERROR:lead_drone_has_no_valid_pilot_or_it_joined_its_own_swarm";
                }
            } else {
                return "ERROR:drones_have_different_locations";
            }
        } else {
           return "ERROR:one_or_both_drones_do_not_exist_at_this_delivery_service";
        }
    }

    public String leaveSwarm(Integer swarm_drone_tag) {
        Drone swarm_drone = getDrone(swarm_drone_tag);
        if (swarm_drone != null) {
            Drone swarm_drone_leader = getDrone(swarm_drone.getLeader().getInitTag());
            if (swarm_drone_leader != null) {
                swarm_drone_leader.leaveSwarmDrones(swarm_drone);
                swarm_drone.leaveSwarm();
                return "OK:change_completed";
            } else {
                return "ERROR:drone_is_not_in_swarm";
            }
        } else {
            return "ERROR:drone_does_not_exist_at_this_delivery_service";
        }
    }

    private boolean works_for(Person p) {
        for (Person employee: employees) {
            if (employee.getUsername().equals(p.getUsername())) {
                return true;
            }
        }
        return false;
    }

    private boolean pilots_for(Person p) {
        for (Pilot pilot: pilots) {
            if (pilot.getUsername().equals(p.getUsername())) {
                return true;
            }
        }
        return false;
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
    public static DeliveryService getServiceByName(String service_name, ArrayList<DeliveryService> deliveryServicesList) {
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
        if (loadingDrone != null) {
            if (loadingDrone.getLocation().equals(this.getLocation())) {
                if (employees.size() > pilots.size() + 1) {
                    loadingDrone.addFuel(petrol);
                    return "OK:change_completed";
                } else {
                    return "ERROR:there_are_not_enough_employees";
                }
            } else {
                return "ERROR:drone_not_located_at_home_base";
            }
        } else {
            return "ERROR:drone_does_not_exist_at_this_delivery_service";
        }
    }

    public String loadIngredient(Ingredient i, Integer drone_tag, Integer quantity, Integer unit_price) {
        Drone loadingDrone = getDrone(drone_tag);
        if (loadingDrone != null) {
            if (loadingDrone.getLocation().equals(this.getLocation())) {
                if (loadingDrone.getRemainingCapacity() >= quantity) {
                    if (employees.size() > pilots.size() + 1) {
                        loadingDrone.updateCapacity(quantity, this.getName(), drone_tag, unit_price, i);
                        return "OK:change_completed";
                    } else {
                        return "ERROR:there_are_not_enough_employees";
                    }
                } else {
                    return "ERROR:drone_does_not_have_enough_space";
                }
            } else {
                return "ERROR:drone_not_located_at_home_base";
            }
        } else {
            return "ERROR:drone_does_not_exist_at_this_delivery_service";
        }
    }
    
    public String flyDrone(Integer drone_tag, String destination_name, ArrayList<Location> locationList) {
        Drone flyingDrone = getDrone(drone_tag);
        boolean validFlight = false;
        String str = null;
        /* Validates identity of leading drone and iterates over a new ArrayList<Drone> including leading drone and swarm
        ** to see if whole passes certain conditions. */
        if (flyingDrone != null) {
            if (flyingDrone.getAppointedPilot() != null) {
                ArrayList<Drone> coup = flyingDrone.getSwarmDrones();
                coup.add(flyingDrone);
                for (int i = 0; i < coup.size(); i++) {
                    int distanceTo = Location.calculateDistance(coup.get(i).getLocation(), destination_name, locationList);
                    int distanceBack = Location.calculateDistance(destination_name, coup.get(i).getLocation(), locationList);
                    if (coup.get(i).getRemainingFuel() >= distanceTo + distanceBack) {
                        if (Location.hasSpaceForAll(destination_name, locationList, coup.size())) {
                            validFlight = true;
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
        /* After validating all conditions first, iterates over coup and updates all drones.
        ** The reason for validating all the drones first is that if one of the drones in the middle of the
        ** coup does not pass the condition, you will still have updated all the prior drones fuel
        ** and destination, which you would have to revert.
        */
        if (validFlight) {
            ArrayList<Drone> coup = flyingDrone.getSwarmDrones();
            coup.add(flyingDrone);
            for (int i = 0; i < coup.size(); i++) {
                int distanceTo = Location.calculateDistance(coup.get(i).getLocation(), destination_name, locationList);
                coup.get(i).getAppointedPilot().addExperience(1);
                coup.get(i).subtractFuel(distanceTo);
                coup.get(i).updateLocation(destination_name);
                Location.increaseRemaining(coup.get(i).getLocation(), locationList);
                Location.decreaseRemaining(destination_name, locationList);
            }
            coup.remove(flyingDrone);
            str = "OK:change_complete";
        }
        return str;
    }

    // Check if delivery service manager is valid.
    private boolean hasValidManager() {
        return this.manager != null;
    }
}
