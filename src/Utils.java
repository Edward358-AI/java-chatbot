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

    public static int genOrderNum() {
        return randint(1, 100);
    }

    public static boolean checkKeyword(String[] keywords, String input) {
        for (String keyword : keywords) {
            if (input.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }
}
