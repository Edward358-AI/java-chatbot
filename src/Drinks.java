public class Drinks {
    public static final String[] ITEMS = {
        "Coca-Cola",
        "Diet Coke", 
        "Cherry Coke", 
        "7UP", 
        "Dr. Pepper", 
        "Root Beer", 
        "Signature Pink Lemonade", 
        "Lite Pink Lemonade", 
        "Iced Tea", 
        "Milk", 
        "Coffee", 
        "Hot Cocoa"
    };
    public static final double[] PRICES = {
        2.19, 
        2.19, 
        2.19, 
        2.19, 
        2.19, 
        2.19, 
        2.19, 
        2.19, 
        2.19, 
        1.39, 
        1.79, 
        2.49
    };
    public static final double[] CALORIES = {
        130.0,
        0.0,
        140.0,
        130.0,
        130.0,
        150.0,
        150.0,
        5.0,
        0.0,
        150.0,
        0.0,
        160.0
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
