public class Employment {
    private DeliveryService deliveryService;
    private User user;
    private String type;

    public Employment(DeliveryService deliveryService, User user, String type) {
        this.deliveryService = deliveryService;
        this.user = user;
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public User getUser() {
        return this.user;
    }
}