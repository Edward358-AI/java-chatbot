public class Utils {
    public static boolean contains(String el, String[] arr) {
        for (String x : arr) {
            if (x.equals(el)) {
                return true;
            }
        }

        return false;
    }
}
