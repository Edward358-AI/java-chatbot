public class Apologies {
    private static String[] allGreetings = {
            "Apologies, but I didn’t quite understand.",
            "Could you please clarify that?",
            "I’m afraid I didn’t catch that.",
            "Could you rephrase your question, please?",
            "I’m not entirely certain how to respond to that.",
            "Would you mind elaborating?",
            "I’m sorry, but I need more information to assist you.",
            "That’s beyond my current knowledge.",
            "Could you provide more details?",
            "I may need more context to understand fully.",
            "My apologies; I don’t have an answer to that yet.",
            "Could you ask that in a different way?",
            "I'm here to assist, though I need a bit more clarity.",
            "I apologize—I didn’t quite follow.",
            "I’m sorry, but I require more information to assist you effectively."
    };

    public static String getRandom() {
        int randomInt = Utils.randint(0, allGreetings.length);
        return allGreetings[randomInt];
    }
}
