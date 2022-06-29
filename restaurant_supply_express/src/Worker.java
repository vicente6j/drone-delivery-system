import java.util.HashMap;

public class Worker extends Person {
    private String licenseID;
    private Integer experience;
    private HashMap<String, DeliveryService> employedBy;
    private Integer worksForCount = 1;

    public Worker(String username, String fname, String lname, String date, String address) {
        super(username, fname, lname, date, address);
        this.employedBy = new HashMap<>();
        this.licenseID = null;
    }

    public Integer getWorksForCount() {
        return this.worksForCount;
    }

    public void getHired(DeliveryService deliveryService) {
        this.employedBy.put(deliveryService.getName(), deliveryService);
    }

    public void getFired(DeliveryService deliveryService) {
        this.employedBy.remove(deliveryService.getName());
    }

    public String getLicenseID() {
        return this.licenseID;
    }

    public Integer getExperience() {
        return this.experience;
    }

    public void getTrained(String licenseID, Integer experience) {
        this.licenseID = licenseID;
        this.experience = experience;
    }
}