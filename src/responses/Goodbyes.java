package responses;

import utility.Utils;

public class Goodbyes {
    private static final String[] allGoodbyes = {
            "Goodbye.",
            "Take care.",
            "Farewell.",
            "Have a great day.",
            "Until we meet again.",
            "Wishing you well.",
            "Thank you for stopping by.",
            "I look forward to our next interaction.",
            "Please take care.",
            "I’ll be here whenever you need.",
            "All the best.",
            "Have a pleasant day ahead.",
            "Thank you and goodbye.",
            "Stay safe.",
            "Goodbye, and take care."
    };

    public static String getRandom() {
        int randomInt = Utils.randint(0, allGoodbyes.length-1);

        return allGoodbyes[randomInt];
    }
}
