public class Payload {
    private Integer ingredientQuantity;
    private Integer ingredientUnitPrice;
    private Ingredient ingredientAssociated;

    public Payload(Integer ingredientQuantity, Integer ingredientUnitPrice,
            Ingredient ingredient) {
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientUnitPrice = ingredientUnitPrice;
        this.ingredientAssociated = ingredient;
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

    public Ingredient getIngredientAssociated() {
        return ingredientAssociated;
    }

    public void setIngredientAssociated(Ingredient ingredientAssociated) {
        this.ingredientAssociated = ingredientAssociated;
    }

    /**
     * Method to decrease payload ingredient quantity after a sale to restaurant.
     * 
     * @param quantity Integer reopresenting the ingredient quantity purchased
     * @param drone    Drone representing the drone who did the sale
     */
    public void postSaleUpdate(int quantity, Drone drone) {
        this.setIngredientQuantity(this.getIngredientQuantity() - quantity);
        if (this.getIngredientQuantity() == 0) {
            drone.removePayload(this);
        }
    }

    /**
     * toString for payload
     */
    public String toString() {
        return String.format("&> barcode: %s, item_name: %s, total_quantity: %d, unit_cost: %d, total_weight: %d",
                this.ingredientAssociated.getBarcode(), this.ingredientAssociated.getName(), this.ingredientQuantity,
                this.ingredientUnitPrice, this.ingredientAssociated.getWeight() * this.ingredientQuantity);
    }
}