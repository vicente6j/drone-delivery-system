
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Person {
    private String username;
    private String fname;
    private String lname;
    private String birthdate;
    private String address;

    public Person(String username, String fname, String lname, String birthdate, String address) {
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.birthdate = birthdate;
        this.address = address;
    }

    /**
     * Creates a new person in the system.
     * 
     * @param username String representing the person's username
     * @param fname    String representing the person's first name
     * @param lname    String representing the person's last name
     * @param year     String representing the person's year born
     * @param month    String representing the person's month born
     * @param date     String representing the person's date born
     * @param address  String representing the person's address
     * @param people   HashMap of the people in the system.
     */
    public static void create(String username, String fname, String lname, Integer year, Integer month,
            Integer date, String address, HashMap<String, Person> people) {
        Date date_format = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(year + "-" + month + "-" + date);
        Person person = new Person(username, fname, lname, formatter.format(date_format), address);
        people.put(username, person);
        System.out.println("OK:person_created");
    }

    /**
     * toString method for a person
     */
    public String toString() {
        return String.format("userId: %s, name: %s, birth date: %s. address: %s", this.username,
                this.fname + " " + this.lname, this.birthdate, this.address);
    }

    public String getUsername() {
        return this.username;
    }

    public String getFname() {
        return this.fname;
    }

    public String getLname() {
        return this.lname;
    }

    public String getBirthDate() {
        return this.birthdate;
    }

    public String getAddress() {
        return this.address;
    }
}