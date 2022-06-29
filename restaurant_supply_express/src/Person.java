import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Person {
    private String username;
    private String fname;
    private String lname;
    private String date;
    private String address;
    private String managing;
    private boolean isPilot;

    private ArrayList<DeliveryService> employedIn;

    public Person(String username, String fname, String lname, String date, String address,
            ArrayList<DeliveryService> employedIn) {
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.date = date;
        this.address = address;
        this.managing = "";
        this.isPilot = false;
        this.employedIn = new ArrayList<DeliveryService>(employedIn);
    }

    // toString
    public String toString() {
        if (employedIn.size() == 0) {
            return "userID: " + this.username + ", name: " + this.fname + " " + this.lname
                    + ", birth date: " + this.date + ", address: " + this.address;
        } else {
            if (managing.equals("")) {
                String result1 = "userID: " + this.username + ", name: " + this.fname + " " + this.lname
                        + ", birth date: " + this.date + ", address: " + this.address;
                String result2 = "employee is working at: " + "\n";
                for (DeliveryService ds : employedIn) {
                    result2 += "&> " + ds.getName() + "\n";
                }
                String result3 = "";
                String result4 = "";
                if (this instanceof Pilot) {
                    Pilot pilot = (Pilot) this;
                    result3 = "\n" + "user has a pilot's license (" + pilot.getLicenseID() + ") with "
                            + pilot.getExperience() + " successful flight(s)";
                    if (pilot.getControlledDrones().size() > 0) {
                        result4 = "\n" + "employee is flying these drones:  [ drone tags | ";
                        for (Drone d_controlled : pilot.getControlledDrones()) {
                            System.out.println(pilot.getControlledDrones());
                            result4 += d_controlled.getInitTag() + " | ";
                        }
                        result4 = result4.substring(0, result4.length() - 2);
                        result4 += "]";
                    }
                }
                return result1 + "\n" + result2.substring(0, result2.length() - 1) + result3 + result4;
            } else {
                String result1 = "userID: " + this.username + ", name: " + this.fname + " " + this.lname
                        + ", birth date: " + this.date + ", address: " + this.address;
                String result2 = "employee is managing: " + managing;
                return result1 + "\n" + result2.substring(0, result2.length());
            }
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

    public ArrayList<DeliveryService> getEmployedIn() {
        return this.employedIn;
    }

    // setters

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

    public String getManaging() {
        return managing;
    }

    public void setManaging(String managing) {
        this.managing = managing;
    }

    public boolean getPilot() {
        return isPilot;
    }

    public void setPilot(boolean isPilot) {
        this.isPilot = isPilot;
    }

}