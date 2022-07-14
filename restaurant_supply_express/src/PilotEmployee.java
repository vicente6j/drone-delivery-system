import java.util.HashMap;

public class PilotEmployee extends User {
    private HashMap<Integer, Drone> controllingDrones;

    /**
     * Copy constructor that copys data from a user to a pilot
     * 
     * @param user User
     */
    public PilotEmployee(User user) {
        super(user.getUsername(), user.getFname(), user.getLname(), user.getBirthDate(), user.getAddress());
        this.controllingDrones = new HashMap<>();
        this.setEmployers(user.getEmployers());
        this.setLicense(user.getLicense());
        this.setExperience(user.getExperience());
    }

    /**
     * Method for a pilot to take control of a drone.
     * 
     * @param droneTag      Integer representing the tag of the drone to control
     * @param serviceDrones HashMap<Integer, Drone> data structure representing the
     *                      drones belonging to the service
     * @param people        HashMap<String, Person> data structure representing the
     *                      people in the system
     */
    public void takeDrone(Integer droneTag, HashMap<Integer, Drone> serviceDrones, HashMap<String, Person> people) {
        // Pilot is already controlling the drone
        if (this.controllingDrones.containsKey(droneTag)) {
            System.out.println("ERROR:pilot_already_controlling_drone");
            return;
        }
        // Get the drone to control
        Drone drone = serviceDrones.get(droneTag);
        // Remove the drone from the current appointed pilot
        PilotEmployee currentAppointedPilotEmployee = drone.getAppointedPilotEmployee();
        if (currentAppointedPilotEmployee != null) {
            currentAppointedPilotEmployee.renounceDrone(drone.getTag(), people);
        }
        // Remove drone from swarm if it was part of one
        if (drone.getLeader() != null) {
            Drone swarmLeader = drone.getLeader();
            swarmLeader.removeSwarmDrone(drone);
            drone.setLeader(null);
        }
        // Set the drone to the new appointed pilot
        drone.setAppointedPilotEmployee(this);
        this.controllingDrones.put(droneTag, drone);
        System.out.println("OK:employee_has_been_appointed_pilot");
    }

    /**
     * Method for a pilot to take control of a drone after it leaves a swarm.
     * 
     * @param drone Drone representing the drone to take
     */
    public void takeDrone(Drone drone) {
        this.controllingDrones.put(drone.getTag(), drone);
    }

    /**
     * Helper method to renounce the drone from a pilot
     * 
     * @param droneTag Integer representing the tag of the drone to renounce
     * @param people   HashMap<String, Person> data structure representing the
     *                 people in the system
     */
    public void renounceDrone(Integer droneTag, HashMap<String, Person> people) {
        this.controllingDrones.remove(droneTag);
        // Turn the pilot back into a worker if no more drones in their control, but
        // keep license and experience
        if (this.controllingDrones.size() == 0) {
            people.put(this.getUsername(),
                    new WorkerEmployee(this, this.getEmployers(), this.getLicense(), this.getExperience()));
        }
    }

    public HashMap<Integer, Drone> getDrones() {
        return this.controllingDrones;
    }

    /**
     * toString method for a pilot employee
     */
    public String toString() {
        // String containing the service where the pilot works
        String employers = "";
        if (this.getEmployers().size() > 0) {
            for (String deliveryService : this.getEmployers()) {
                employers += String.format("&> %s\n", deliveryService);
            }
        }

        // String containing the license
        String license = "";
        if (this.getLicense() != null) {
            license = String.format("user has a pilot's license (%s) with %d successful flight(s)\n",
                    this.getLicense(), this.getExperience());
        }

        // String containing the drones in the pilot's control
        String drones = "";
        for (Drone drone : this.controllingDrones.values()) {
            drones += String.format("| %d ", drone.getTag());
        }
        return super.toString() + "\n" + employers + license
                + String.format("employee is flying these drones: [ drone tags %s]", drones);
    }
}
