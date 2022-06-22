import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryService {
    private String name;
    private Integer revenue;
    private String location;
    private HashMap<Integer, Drone> drones;
    private ArrayList<Person> employees;

    public DeliveryService(String name, Integer revenue, String location) {
        this.name = name;
        this.revenue = revenue;
        this.location = location;
        this.drones = new HashMap<>();
        this.employees = new ArrayList<Person>();
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
        if(p instanceof Manager) {
            return "ERROR:employee_can't_be_hired";
        } else if(p instanceof Pilot) {
            if(((Pilot)p).pilotingFor(this)) {
                return "ERROR:person_piloting_can't_be_hired";
            } else {
                p.setEmployed(true);
                p.getEmployedIn().add(this);
                this.employees.add(p);
                return "OK:new_employee_has_been_hired";
            }
        } else {
            p.setEmployed(true);
            p.getEmployedIn().add(this);
            this.employees.add(p);
            return "OK:new_employee_has_been_hired";
        }
    }





}