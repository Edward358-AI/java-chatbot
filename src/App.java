import java.util.*;

public class App {
    public static Orders orders = new Orders();

    public static Scanner sc = new Scanner(System.in);
    public static String strInput = "";
    public static int intInput = "";

    public static void main(String args[]) {
        String greeting = Greetings.getRandom() + " I am In-n-Out's chatbot. Can I take your order? (yes/no)";
        System.out.println(greeting);

        strInput = sc.nextLine();
        if (contains({"y", "yes"}, strInput.toLowerCase())) {
            // Take order
        }
        
        sc.close()
    }

    public static boolean contains(String[] arr, String el) {
        for (x : arr) {
            if (x == el) {
                return true;
            }
        }

        return false;
    }
}
