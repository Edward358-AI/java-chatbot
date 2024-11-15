public class Greetings {
    private static String[] allGreetings = {
            "Hello.",
            "Good day.",
            "Greetings.",
            "Hello there.",
            "How can I assist you today?",
            "Good to see you.",
            "Welcome.",
            "I’m here to help—how may I assist?",
            "Greetings, how may I be of service?",
            "Hello, how can I assist you today?",
            "Hi, what can I do for you?",
            "It’s a pleasure to connect with you.",
            "Good afternoon/morning.",
            "How may I assist?",
            "Hello, how may I help?"
    };

    public static String getRandom() {
        int randomInt = Utils.randint(0, allGreetings.length);
        return allGreetings[randomInt];
    }
}
