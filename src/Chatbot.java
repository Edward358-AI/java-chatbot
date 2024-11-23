import java.util.*;

public class Chatbot {
  private Orders orders = new Orders();

  private Scanner sc = new Scanner(System.in);
  private final String[] STATES = {"welcome", "goodbye", "takeorder", "vieworder", ""};
  private String state = "welcome";
}
