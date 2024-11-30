package utility;

public class Printer {
    public static void print() {
        System.out.print("");
    }

    public static void print(String x) {
        System.out.print(Colors.YELLOW_FG + x + Colors.RESET);
    }

    public static void print(String x, String color) {
        System.out.print(color + x + Colors.RESET);
    }

    public static void println() {
        System.out.println("");
    }

    public static void println(String x) {
        System.out.println(Colors.YELLOW_FG + x + Colors.RESET);
    }

    public static void println(String x, String color) {
        System.out.println(color + x + Colors.RESET);
    }
}
