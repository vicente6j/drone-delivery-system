public class Manager extends Worker {
    private DeliveryService service;

    public Manager(String username, String fname, String lname, String date, String address, DeliveryService service) {
        super(username, fname, lname, date, address);
        this.service = service;
    }

    public DeliveryService getService() {
        return this.service;
    }
}