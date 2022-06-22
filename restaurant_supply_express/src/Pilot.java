import java.util.ArrayList;

public class Pilot extends Person {
    private ArrayList<DeliveryService> piloting_for;
    private boolean licensed;

    public Pilot(String username, String fname, String lname, Integer year, Integer month, Integer date, String address) {
        super(username, fname, lname, year, month, date, address);
        this.licensed = false;
        this.piloting_for = new ArrayList<DeliveryService>();
    }

    public boolean pilotingFor(DeliveryService ds) {
        for(DeliveryService d: piloting_for) {
            if(d.getName().equals(ds.getName())) {
                return true;
            }
        }
        return false;
    }
}