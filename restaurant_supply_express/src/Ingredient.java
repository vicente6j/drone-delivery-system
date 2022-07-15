public class Ingredient {

    private String barcode;
    private String name;
    private Integer weight;

    private Ingredient(IngredientBuilder builder) {
        this.barcode = builder.barcode;
        this.name = builder.name;
        this.weight = builder.weight;
    }

    /**
     * toString for ingredient
     */
    public String toString() {
        return String.format("barcode: %s, name: %s, unit_weight: %d", this.barcode, this.name, this.weight);
    }

    public String getBarcode() {
        return this.barcode;
    }

    public String getName() {
        return this.name;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public void setBar(String newBar) {
        this.barcode = newBar;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setWeight(Integer newWeight) {
        this.weight = newWeight;
    }

    public static class IngredientBuilder {
        private String barcode;
        private String name;
        private Integer weight;

        public IngredientBuilder(String barcode, String name, Integer weight) {
            this.barcode = barcode;
            this.name = name;
            this.weight = weight;
        }

        public Ingredient build() {
            Ingredient newIngredient = new Ingredient(this);
            return newIngredient;
        }
    }
}