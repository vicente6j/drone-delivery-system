import java.util.ArrayList;

public class Restaurant {
    private String name;
    private String location;
    private Integer spent = 0;

    public Restaurant(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public String getLocation() {
        return this.location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMoneySpent() {
        return this.spent;
    }

    public void setMoneySpent(Integer spent) {
        this.spent = spent;
    }

    public static String getLocation(String name, ArrayList<Restaurant> restaurantList) {
        for (int i = 0; i < restaurantList.size(); i++) {
            if (restaurantList.get(i).getName().equals(name)) {
                return restaurantList.get(i).getLocation();
            }
        }
        return "";
    }

    public static void makePurchase(String name, Integer quantityProduct, Integer unitPriceProduct, ArrayList<Restaurant> restaurantList) {
        for (int i = 0; i < restaurantList.size(); i++) {
            if (restaurantList.get(i).getName().equals(name)) {
                restaurantList.get(i).setMoneySpent(restaurantList.get(i).getMoneySpent()+ quantityProduct * unitPriceProduct);
            }
        }
    }


    public static boolean nameUnique(String name, ArrayList<Restaurant> restaurants){
        for (int i = 0; i < restaurants.size(); i++){
            if ( restaurants.get(i).getName().equals(name)){
                return false;
            }
        }
        return true;

    }
}