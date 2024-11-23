public class Secret {
    public static final String[] ITEMS = {
        "4X4",
        "3X3",
        "Double Meat", 
        "Grilled Cheese",
        "Animal Fries"
    };
    public static final double[] PRICES = {
        10.99,
        8.49,
        5.19,
        3.19,
        4.25
    };
    public static final double[] CALORIES = {
        970.0,
        790.0,
        470.0,
        750.0
    };
    public static double[] getInfo(String food) {
        for (int i = 0; i < ITEMS.length; i++) {
            if (ITEMS[i].toLowerCase().equals(food.toLowerCase())) {
                return new double[]{PRICES[i], CALORIES[i]};
            }
        }
        return null;
    }
}
