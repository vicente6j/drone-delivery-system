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
        } else if(p instanceof Pilot && ((Pilot)p).getEmployedby() != null && !((Pilot)p).getEmployedby().equals(this.getName())) {
            return "ERROR:person_piloting_can't_be_hired";
            //else {
            //     p.setEmployed(true);
            //     p.getEmployedIn().add(this);
            //     this.employees.add(p);
            //     return "OK:new_employee_has_been_hired";
            // }
        } else {
            p.getEmployedIn().add(this);
            this.employees.add(p);
            return "OK:new_employee_has_been_hired";
        }
    }


    public String fire_worker(Person p) {
        if(p.equals(manager)) {
            return "ERROR:employee_is_managing_a_service";
        } else if(p instanceof Pilot && ((Pilot)p).getEmployedby().equals(this.getName())) {
            return "ERROR:person_piloting_can't_be_fired";
        } else if (employees.contains(p)) {
            p.getEmployedIn().remove(this);
            this.employees.remove(p);
            return "OK:employee_has_been_fired";
        }
        return "ERROR: the_person_wasn't_hired";
    }

    public String appoint_manager(Person p) {
        if(employees.contains(p)) {
            if(!pilots.contains(p)){
                if (p.getEmployedIn().size() <= 1){
                    if (manager != null){
                        for (Person employee : employees){
                            if (employee.equals(manager)){
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
        System.out.println(employees.size());
        if(this.works_for(p)) {
            if(!p.equals(manager)){
                if (this.pilots_for(p)== false){
                    if (manager != null){
                        Pilot pilot = new Pilot(this.name, p.getUsername(), p.getFname(), p.getLname(), p.getDate(), p.getAddress(),p.getEmployedIn(), init_license, init_experience);
                        pilots.add(pilot);
                        System.out.println(pilots.size());
                        System.out.println(employees.size());
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
    private boolean works_for(Person p){
        for (Person employee: employees){
            if( employee.getUsername().equals(p.getUsername())){
                return true;
            }
        }
        return false;
    }

    private boolean pilots_for(Person p){
        for (Pilot pilot: pilots){
            if( pilot.getUsername().equals(p.getUsername())){
                return true;
            }
        }
        return false;
    }





}