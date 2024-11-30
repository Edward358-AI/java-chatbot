package responses;

import utility.Utils;

public class Greetings {
    private static final String[] allGreetings = {
            "Hello!",
            "Good day!",
            "Greetings!",
            "Hello there!",
            "Good to see you!",
            "Welcome!",
            "It’s a pleasure to connect with you!",
            "Nice to see you here today!",
            "Hey there!",
            "Good to have you here!",
            "Hi, let me know what's on your mind!",
            "Hello, hope your day’s going well so far!",
            "Hi, let's get started!",
            "Hi, how's it going?",
            "Hey, glad to see you!"
    };

    public static String getRandom() {
        int randomInt = Utils.randint(0, allGreetings.length-1);

        return allGreetings[randomInt];
    }
}
