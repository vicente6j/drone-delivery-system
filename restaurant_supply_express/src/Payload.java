import java.util.ArrayList;

public class Payload {

    private String service;

    private Integer drone_tag;

    private String barcode;
    
    private Integer quantity;

    private Integer price;



    public Payload(String service, Integer drone_tag, String barcode, Integer quantity, Integer price) {
        this.service = service;
        this.drone_tag = drone_tag;
        this.barcode = barcode;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getDrone_tag() {
        return drone_tag;
    }

    public void setDrone_tag(Integer drone_tag) {
        this.drone_tag = drone_tag;
    }

    public static Payload getPayload (String bracode, String service_name, Integer drone_tag, ArrayList<Payload>payloads){
        for (int i = 0; i < payloads.size(); i++){
            if(payloads.get(i).getService().equals(service_name) && payloads.get(i).getDrone_tag().equals(drone_tag) && payloads.get(i).getBarcode().equals(bracode)){
                return payloads.get(i);
            }
        }
        return null;
    }

    public static void updatePayload (String barcode, String service_name, Integer drone_tag, ArrayList<Payload>payloads, Integer quantity){
        for (int i = 0; i < payloads.size(); i++){
            if(payloads.get(i).getService().equals(service_name) && payloads.get(i).getDrone_tag().equals(drone_tag) && payloads.get(i).getBarcode().equals(barcode)){
                payloads.get(i).setQuantity(payloads.get(i).getQuantity() - quantity);
            }
        }
    }

    public static Integer getPrice (String barcode, String service_name, Integer drone_tag, ArrayList<Payload>payloads, Integer quantity){
        for (int i = 0; i < payloads.size(); i++){
            if(payloads.get(i).getService().equals(service_name) && payloads.get(i).getDrone_tag().equals(drone_tag) && payloads.get(i).getBarcode().equals(barcode)){
                 return payloads.get(i).getPrice() * quantity;
            }
        }
        return 0;
    }

    
}
