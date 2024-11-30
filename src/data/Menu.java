package data;

import utility.Colors;
import utility.Printer;

public class Menu {
    private static String[] ITEMS = new String[Food.ITEMS.length + Drinks.ITEMS.length + Shakes.ITEMS.length + Secret.ITEMS.length];
    private static double[] PRICES = new double[Food.ITEMS.length + Drinks.ITEMS.length + Shakes.ITEMS.length + Secret.ITEMS.length];
    private static double[] CALORIES = new double[Food.ITEMS.length + Drinks.ITEMS.length + Shakes.ITEMS.length + Secret.ITEMS.length];
    static {
        for (int i = 0; i < Food.ITEMS.length; i++) {
            ITEMS[i] = Food.ITEMS[i];
            PRICES[i] = Food.PRICES[i];
            CALORIES[i] = Food.CALORIES[i];
        }
        for (int i = 0; i<  Drinks.ITEMS.length; i++) {
            ITEMS[Food.ITEMS.length + i] = Drinks.ITEMS[i];
            PRICES[Food.ITEMS.length + i] = Drinks.PRICES[i];
            CALORIES[Food.ITEMS.length + i] = Drinks.CALORIES[i];
        }
        for (int i = 0; i < Shakes.ITEMS.length; i++) {
            ITEMS[Food.ITEMS.length + Drinks.ITEMS.length + i] = Shakes.ITEMS[i];
            PRICES[Food.ITEMS.length + Drinks.ITEMS.length + i] = Shakes.PRICES[i];
            CALORIES[Food.ITEMS.length + Drinks.ITEMS.length + i] = Shakes.CALORIES[i];
        }
        for (int i = 0; i < Secret.ITEMS.length; i++) {
            ITEMS[Food.ITEMS.length + Drinks.ITEMS.length + Shakes.ITEMS.length + i] = Secret.ITEMS[i];
            PRICES[Food.ITEMS.length + Drinks.ITEMS.length + Shakes.ITEMS.length + i] = Secret.PRICES[i];
            CALORIES[Food.ITEMS.length + Drinks.ITEMS.length + Shakes.ITEMS.length + i] = Secret.CALORIES[i];
        }
    }

    public static double[] getInfo(String food) {
        for (int i = 0; i < ITEMS.length; i++) {
            if (ITEMS[i].toLowerCase().equals(food.toLowerCase())) {
                return new double[] { PRICES[i], CALORIES[i] };
            }
        }
        return null;
    }

    public static void printMenu() {
        Printer.print("Food items:", Colors.CYAN_FG);
        for (int i = 0; i < Food.ITEMS.length; i++) {
            Printer.print(Food.ITEMS[i] + ": $" + Colors.GREEN_FG + Food.PRICES[i]);
        }

        Printer.print("\nDrinks:", Colors.CYAN_FG);
        for (int i = 0; i < Drinks.ITEMS.length; i++) {
            Printer.print(Drinks.ITEMS[i] + ": $" + Colors.GREEN_FG + Drinks.PRICES[i]);
        }

        Printer.print("\nShakes:", Colors.CYAN_FG);
        for (int i = 0; i < Shakes.ITEMS.length; i++) {
            Printer.print(Shakes.ITEMS[i] + ": $" + Colors.GREEN_FG + Shakes.PRICES[i]);
        }

        Printer.print("\n(Not so) Secret Menu:", Colors.CYAN_FG);
        for (int i = 0; i < Secret.ITEMS.length; i++) {
            Printer.print(Secret.ITEMS[i] + ": $" + Colors.GREEN_FG + Secret.PRICES[i]);
        }
    }
}
