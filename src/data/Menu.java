package data;

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
        Printer.print("Food items:");
        for (int i = 0; i<Food.ITEMS.length;i++) {
            Printer.print(Food.ITEMS[i] + ": $" + Food.PRICES[i]);
        }

        Printer.print("\nDrinks:");
        for (int i = 0; i<Drinks.ITEMS.length;i++) {
            Printer.print(Drinks.ITEMS[i] + ": $" + Drinks.PRICES[i]);
        }

        Printer.print("\nShakes:");
        for (int i = 0; i<Shakes.ITEMS.length;i++) {
            Printer.print(Shakes.ITEMS[i] + ": $" + Shakes.PRICES[i]);
        }

        Printer.print("\n(Not so) Secret Menu:");
        for (int i = 0; i<Secret.ITEMS.length;i++) {
            Printer.print(Secret.ITEMS[i] + ": $" + Secret.PRICES[i]);
        }
    }
}
