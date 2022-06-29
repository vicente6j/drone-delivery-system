import java.util.ArrayList;

public class Worker extends Person {
    public Worker(String username, String fname, String lname, String date, String address,
            ArrayList<DeliveryService> employedIn) {
        super(username, fname, lname, date, address, employedIn);
    }
}