import java.util.ArrayList;

public class Pilot extends Person {

    private String licenseID;
    private String employedby;
    private Integer experience;
    private ArrayList<Drone> controlledDrones;

    public Pilot(String service_name, String user_name,String fname, String lname, String date, String address,ArrayList<DeliveryService> employedIn, String init_license, Integer init_experience) {
        super(user_name, fname, lname,date, address,employedIn);
        this.licenseID = init_license;
        this.employedby = service_name;
        this.experience = init_experience;
        controlledDrones = new ArrayList<Drone>();
    }

    public String getLicenseID() {
        return licenseID;
    }

    public void setLicenseID(String licenseID) {
        this.licenseID = licenseID;
    }

    // public boolean isLicensed() {
    //     return licensed;
    // }

    // public void setLicensed(boolean licensed) {
    //     this.licensed = licensed;
    // }

    public String getEmployedby() {
        return employedby;
    }

    public void setEmployedby(String employedby) {
        this.employedby = employedby;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public void addAppointedDrone(Drone d) {
        controlledDrones.add(d);
    }

    public void subtractAppointedDrone(Drone d) {
        controlledDrones.pop(d);
    }

    public ArrayList<Drone> getControlledDrones() {
        return controlledDrones;
    }
}