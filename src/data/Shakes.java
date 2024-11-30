package data;

public class Shakes {
    public static final String[] ITEMS = {
            "Chocolate Shake",
            "Strawberry Shake",
            "Vanilla Shake"
    };
    public static final double[] PRICES = {
            3.59,
            3.59,
            3.59
    };
    public static final double[] CALORIES = {
            610.0,
            610.0,
            590.0
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
