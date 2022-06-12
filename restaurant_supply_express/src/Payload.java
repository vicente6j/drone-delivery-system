import java.util.ArrayList;

public class Payload {
    private Integer droneTag;
    private String ingredientBarcode;
    private Integer ingredientQuantity;
    private Integer ingredientUnitPrice;

    public Payload(String serviceName, Integer droneTag, String ingredientBarcode, Integer ingredientQuantity, Integer ingredientUnitPrice) {
        this.droneTag = droneTag;
        this.ingredientBarcode = ingredientBarcode;
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientUnitPrice = ingredientUnitPrice;
    }

    public Integer getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(Integer ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public Integer getIngredientUnitPrice() {
        return ingredientUnitPrice;
    }

    public void setIngredientPrice(Integer ingredientUnitPrice) {
        this.ingredientUnitPrice = ingredientUnitPrice;
    }

    public String getIngredientBarcode() {
        return ingredientBarcode;
    }

    public void setIngredientBarcode(String barcode) {
        this.ingredientBarcode = ingredientBarcode;
    }

    public Integer getDroneTag() {
        return droneTag;
    }

    public void setDroneTag(Integer drone_tag) {
        this.droneTag = droneTag;
    }

    public static boolean validatePurchase(Payload payload, int quantityDemanded) {
        if (payload == null) {
            System.out.println("ERROR:drone_doesn't_have that_ingredient");
            return false;
        }
        if (payload.getIngredientQuantity() < quantityDemanded) {
            System.out.println("ERROR:drone_does_not_have_enough_ingredient");
            return false;
        }
        return true;
    }

    public static void postSaleUpdate(Payload payload, int quantity) {
        payload.setIngredientQuantity(payload.getIngredientQuantity() - quantity);
    }
}
