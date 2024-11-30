package data;

public class Food {
    public static final String[] ITEMS = {
            "Double-Double",
            "Cheeseburger",
            "Hamburger",
            "French Fries",
    };
    public static final double[] PRICES = {
            5.89,
            4.19,
            3.69,
            2.79
    };
    public static final double[] CALORIES = {
            610.0,
            430.0,
            360.0,
            360.0
    };
    
    public static double[] getInfo(String food) {
        for (int i = 0; i < ITEMS.length; i++) {
            if (ITEMS[i].toLowerCase().equals(food.toLowerCase())) {
                return new double[] { PRICES[i], CALORIES[i] };
            }
        }

        return null;
    }
}
