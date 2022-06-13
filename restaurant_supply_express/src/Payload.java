import java.util.ArrayList;

public class Payload {
    private Integer droneTag;
    private Integer ingredientQuantity;
    private Integer ingredientUnitPrice;
    private Ingredient ingredientAssociated;

    public Payload(String serviceName, Integer droneTag, Integer ingredientQuantity, Integer ingredientUnitPrice, Ingredient ingredient) {
        this.droneTag = droneTag;
        //this.ingredientBarcode = ingredientBarcode;
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientUnitPrice = ingredientUnitPrice;
        this.ingredientAssociated = ingredient;
        //this.ingredientName = ingredientName;
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


    public Integer getDroneTag() {
        return droneTag;
    }

    public void setDroneTag(Integer drone_tag) {
        this.droneTag = droneTag;
    }

    public Ingredient getIngredientAssociated() {
        return ingredientAssociated;
    }

    public void setIngredientAssociated(Ingredient ingredientAssociated) {
        this.ingredientAssociated = ingredientAssociated;
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
