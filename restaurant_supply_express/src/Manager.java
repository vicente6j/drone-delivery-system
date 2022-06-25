import java.util.ArrayList;

public class Manager extends Person {
    public Manager(String username, String fname, String lname, String date, String address,ArrayList<DeliveryService> employedIn) {
        super(username, fname, lname, date, address, employedIn);
    }
}