import java.util.ArrayList;

public class Person{
    private String username;
    private String fname;
    private String lname;
    private String date;
    private String address;
    private boolean employed;
    private ArrayList<DeliveryService> employedIn;

    public Person(String username, String fname, String lname, Integer year, Integer month, Integer date, String address) {
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.date = year + "-" + month + "-" + date;
        this.address = address;
        this.employed = false;
        this.employedIn = new ArrayList<DeliveryService>();
    }

    //toString
    public String toString() {
        if(employedIn.size() == 0) {
            return "userID: " + this.username + ", name: " + this.fname + " " + this.lname
                + ", birth date: " + this.date + ", address: " + this.address;
        } else {
            String result1 = "userID: " + this.username + ", name: " + this.fname + " " + this.lname
                + ", birth date: " + this.date + ", address: " + this.address;
            String result2 = "employee is working at: " + "\n";
            for(DeliveryService ds: employedIn) {
                result2 += "&> " + ds.getName() + "\n";
            }
            return result1 + "\n" + result2.substring(0, result2.length()-1);
        }
        
    }

    //getters

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

    public boolean getEmployed() {
        return this.employed;
    }

    public ArrayList<DeliveryService> getEmployedIn() {
        return this.employedIn;
    }

    //setters

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

    public void setEmployed(boolean newEmployed) {
        this.employed = newEmployed;
    }


}