import java.util.HashMap;

public class Ingredient {

    private String barcode;
    private String name;
    private Integer weight;

    public Ingredient(String initBarcode, String initName, Integer initWeight) {
        this.barcode = initBarcode;
        this.name = initName;
        this.weight = initWeight;
    }

    /**
     * Method to create a new ingredient
     * 
     * @param init_barcode String representing the ingredient barcode
     * @param init_name    String representing the ingredient name
     * @param init_weight  String representing the ingredient weight
     * @param ingredients  HashMap<String, Ingredient> representing the data
     *                     structure storing the ingredients
     */
    public static void create(String init_barcode, String init_name, Integer init_weight,
            HashMap<String, Ingredient> ingredients) {
        // Valid the ingredient creation
        if (!validateIngredient(init_barcode, init_name, init_weight, ingredients)) {
            return;
        }
        Ingredient newIngredient = new Ingredient(init_barcode, init_name, init_weight);
        ingredients.put(init_barcode, newIngredient);
        System.out.println("OK:ingredient_created");
    }

    /**
     * Helper method to validate the creation of a new ingredient
     * 
     * @param init_barcode String representing the ingredient barcode
     * @param init_name    String representing the ingredient name
     * @param init_weight  String representing the ingredient weight
     * @param ingredients  HashMap<String, Ingredient> representing the data
     *                     structure storing the ingredients
     * @return boolean representing if the ingredient creation is valid
     */
    private static boolean validateIngredient(String init_barcode, String init_name, Integer init_weight,
            HashMap<String, Ingredient> ingredients) {
        // Initial weight cannot be negative
        if (init_weight < 0) {
            System.out.println("ERROR:negative_weight_not_allowed");
            return false;
        }
        // Ingredient already exists
        if (ingredients.get(init_barcode) != null) {
            System.out.println("ERROR:ingredient_already_exists");
            return false;
        }
        return true;
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
}