import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Person {
    private String username;
    private String fname;
    private String lname;
    private String date;
    private String address;

    public Person(String username, String fname, String lname, String date, String address) {
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.date = date;
        this.address = address;
    }

    public static void create(String init_username, String init_fname, String init_lname, Integer init_year,
            Integer init_month,
            Integer init_date, String init_address, HashMap<String, Person> persons) {
        if (getPersonByUsername(init_username, persons) != null) {
            System.out.println("ERROR:username_exists");
            return;
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(init_year + "-" + init_month + "-" + init_date);
        Person newPerson = new Person(init_username, init_fname, init_lname, formatter.format(date), init_address);
        persons.put(init_username, newPerson);
        System.out.println("OK:person_created");
    }

    public static Person getPersonByUsername(String username, HashMap<String, Person> persons) {
        return persons.get(username);
    }

    public static void displayAll(HashMap<String, Person> persons) {
        for (Person person : persons.values()) {
            System.out.println(person);
        }
        System.out.println("OK:display_completed");
    }

    public String toString() {
        if (this instanceof Pilot) {
            return "";
        } else if (this instanceof Manager) {
            return "";
        } else if (this instanceof Worker) {
            return "";
        } else {
            return "userID: " + this.username + ", name: " + this.fname + " " + this.lname
                    + ", birth date: " + this.date + ", address: " + this.address;
        }
    }

    // getters

    public String getUsername() {
        return this.username;
    }

    public String getFname() {
        return this.fname;
    }

    public String getLname() {
        return this.lname;
    }

    public String getDate() {
        return this.date;
    }

    public String getAddress() {
        return this.address;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public void setFname(String newFname) {
        this.fname = newFname;
    }

    public void setLname(String newLname) {
        this.lname = newLname;
    }

    public void setDate(String newDate) {
        this.date = newDate;
    }

    public void setAddress(String newAddress) {
        this.address = newAddress;
    }
}