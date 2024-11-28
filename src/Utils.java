public class Utils {
    public static boolean contains(String el, String[] arr) {
        for (String x : arr) {
            if (x.equals(el)) {
                return true;
            }
        }

        return false;
    }

    public static int randint(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static int sum(int[] s) {
        int r = 0;
        for (int i : s) r += i;
        return r;
    }

    public static int sum(Integer[] s) {
        int r = 0;
        for (int i : s) r += i;
        return r;
    }

    public static double sum(Double[] s) {
        double r = 0.0;
        for (double i : s) r += i;
        return Math.round(r * 100.0) / 100.0;
    }

    public static double sum(double[] s) {
        double r = 0.0;
        for (double i : s) r += i;
        return Math.round(r * 100.0) / 100.0;
    }
}
