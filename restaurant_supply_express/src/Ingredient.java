public class Ingredient {

    private String barcode;
    private String name;
    private Integer weight;

    public Ingredient(String initBarcode, String initName, Integer initWeight) {
        this.barcode = initBarcode;
        this.name = initName;
        this.weight = initWeight;
    }

    public String getBar() {
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