package utility;

public class Printer {
    public static void println() {
        System.out.println("");
    }

    public static void print() {
        System.out.print("");
    }

    public static void println(String x) {
        System.out.println(x);
    }

    public static void print(String x) {
        System.out.print(x);
    }

    public static void print(String x, String color) {
        System.out.print(color + x + Colors.RESET);
    }

    public static void println(String x, String color) {
        System.out.println(color + x + Colors.RESET);
    }
}
