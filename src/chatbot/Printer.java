package chatbot;

import utility.Colors;

class Printer {
    public static void print() {
        System.out.println("");
    }

    public static void print(String x) {
        System.out.println(x);
    }

    public static void print(String x, String color) {
        System.out.println(color + x + Colors.RESET);
    }
}
