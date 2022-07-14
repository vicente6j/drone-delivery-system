import java.util.HashSet;

public class User extends Person {
    private String license;
    private Integer experience;
    private HashSet<String> employers;

    public User(String username, String fname, String lname, String birthdate,
            String address) {
        super(username, fname, lname, birthdate, address);
        this.employers = new HashSet<>();
    }

    public User(String username, String fname, String lname, String birthdate,
            String address, HashSet<String> employers, String license, Integer experience) {
        super(username, fname, lname, birthdate, address);
        this.employers = employers;
        this.license = license;
        this.experience = experience;
    }

    public HashSet<String> getEmployers() {
        return this.employers;
    }

    public void setEmployers(HashSet<String> employers) {
        this.employers = employers;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicense() {
        return this.license;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getExperience() {
        return this.experience;
    }

    public void increaseExperience() {
        this.experience += 1;
    }
}
