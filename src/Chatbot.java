import java.util.*;

public class Chatbot {
    // this is just for reference
    // private final String[] STATES = {
    // "welcome",
    // "goodbye",
    // "takeOrder",
    // "askQuestion",
    // "location",
    // "nutrition",
    // "viewOrders",
    // "updateOrder",
    // "contactInfo"
    // };
    private Orders orders = new Orders();
    private Scanner sc = new Scanner(System.in);
    private final String[] QUIT = { "quit" };
    private final String[] YES = """
Yes
Yeah
Yea
Yup
Sure
Okay
Alright
Definitely
Absolutely
Fine
Of course
Why not
            """.toLowerCase().split("\n");
    private final String[] NO = """
No
Nope
Nah
Never
Not really
No way
Negative
Not at all
I'm good
I'm okay
Not today
Not right now
No thanks
            """.toLowerCase().split("\n");
    private String state = "welcome";

    private boolean keyword(String input, String[] keywords) {
        return keyword(input, keywords, false);
    }

    private boolean keyword(String input, String[] keywords, boolean checkAll) {
        boolean contained = false;
        if (checkAll) {
            contained = true;
            for (String keyword : keywords) {
                if (!input.contains(keyword)) {
                    contained = false;
                    break;
                }
            }
        } else {
            for (String keyword : keywords) {
                if (input.contains(keyword)) {
                    contained = true;
                    break;
                }
            }
        }

        return contained;
    }

    private void checkQuit(String input) {
        if (keyword(input, QUIT)) {
            state = "goodbye";
            parseState();
        }
    }

    private void readYesNo(String yesState, String noState, String customErrMsg) {
        String tempState = null;
        do {
            String resp = sc.nextLine().toLowerCase();
            if (keyword(resp, QUIT)) {
                tempState = "goodbye";
            } else {
                if (keyword(resp, YES) && !keyword(resp, NO)) {
                    tempState = yesState;
                } else if (!keyword(resp, YES) && keyword(resp, NO)) {
                    tempState = noState;
                } else {
                    System.out.println(Apologies.getRandom() + customErrMsg);
                    tempState = null;
                }
            }
        } while (tempState == null);
        state = tempState;
        parseState();
    }

    private void askQuestion() {
        System.out.println("Do you have any questions regarding location, nutrition, or contact info, or would you like to view the orders? Please choose one.");
        String[] location = {"location"};
        String[] nutrition = {"nutrition"};
        String[] contact = {"contact"};
        String[] vieworder1 = {"see", "view", "check", "look", "access", "review", "pull up", "track", "visit"};
        String[] vieworder2 = {"order"};
        String tempState = null;
        do {
            String resp = sc.nextLine().toLowerCase();
            boolean choseLocation = keyword(resp, location);
            boolean choseNutrition = keyword(resp, nutrition);
            boolean choseContact = keyword(resp, contact);
            boolean choseOrder = keyword(resp, vieworder1) && keyword(resp, vieworder2);
            boolean saidNo = keyword(resp, NO);
            if (keyword(resp, QUIT)) {
                tempState = "goodbye";
            } else {
                if (choseLocation && !choseNutrition && !choseOrder && !choseContact && !saidNo) {
                    tempState = "location";
                } else if (!choseLocation && choseNutrition && !choseOrder && !choseContact && !saidNo) {
                    tempState = "nutrition";
                } else if (!choseLocation && !choseNutrition && choseOrder && !choseContact && !saidNo) {
                    tempState = "viewOrders";
                } else if (!choseLocation && !choseNutrition && !choseOrder && choseContact && !saidNo) {
                    tempState = "contactInfo";
                }  else if (!choseLocation && !choseNutrition && !choseOrder && !choseContact && saidNo) {
                    tempState = "welcome";
                } else {
                    System.out.println(Apologies.getRandom() + " Would you like information regarding location, nutrition, or contact info? Or would you like to view the order queue?");
                    continue;
                }
            }
        } while (tempState == null);
        state = tempState;
        parseState();
    }

    public void initialize() {
        parseState();
    }

    private void parseState() {
        switch (state) {
            case "welcome":
                welcome();
                break;
            case "goodbye":
                goodbye();
                System.exit(0);
                break;
            case "takeOrder":
                takeOrder();
                break;
            case "askQuestion":
                askQuestion();
                break;
            case "location":
                location();
                break;
            case "nutrition":
                nutrition();
                break;
            case "viewOrders":
                viewOrders();
                break;
            case "updateOrder":
                updateOrder();
                break;
            case "contactInfo":
                contactInfo();
                break;
            default:
                break;
        }
    }

    private void welcome() {
        System.out.println(String.format(
                "%s I am In 'n Out's virtual assistant. If at any time you would like to stop chatting, just say \"quit\"anytime. Can I take your order today?",
                Greetings.getRandom()));
        readYesNo("takeOrder", "askQuestion", " Can I take your order today?");
    }

    private void goodbye() {
        System.out.println(String.format(
                "%s As the In n' Out chatbot, I don't necessarily deliver quality you can taste, but quality you can trust! Until next time!", Goodbyes.getRandom()));
    }

    private void takeOrder() {
        ArrayList<String> orderItems = new ArrayList<String>();
        ArrayList<Double> orderPrices = new ArrayList<Double>();
        
        boolean finished = false;
        System.out.println("Here is our menu:\n");
        Menu.printMenu();
        System.out.println();
        do {
            System.out.println("Please type out the name of an item you would like to add to your order. If you are finished type \"finished\".");
            String cItem = sc.nextLine().toLowerCase().trim();
            checkQuit(cItem);
            if (keyword(cItem, new String[]{"finished", "finish", "done", "end", "ready"})) {
                if (orderItems.size() == 0) {
                    System.out.print("You must enter at least one item. ");
                    continue;
                } else {
                    finished = true;
                }
            } else {
                if (Menu.getInfo(cItem) != null) {
                    orderItems.add(Utils.capitalize(cItem));
                    orderPrices.add(Menu.getInfo(cItem)[0]);
                    System.out.println(Utils.capitalize(cItem) + " added to your order.");
                } else {
                    System.out.println("Food item not found. Please check your spelling and try again. Thanks!");
                    continue;
                }
            }
        } while (!finished);
        viewOrder order = orders.saveOrder(orderItems, orderPrices);
        System.out.println("Here are your order details: \n");
        System.out.println(order.asText());
        state = "askQuestion";
        System.out.print("While you're waiting, ");
        parseState();
    }

    private void location() {
        boolean locFound = false;
        do {
            System.out.println("Regarding location, what state would you like to get information about? You can enter either the name or postal code (the two letter abbreviation).");
            String state = sc.nextLine().toLowerCase().trim();
            checkQuit(state);
            System.out.println("What city in that state would you like location information regarding?");
            String city = sc.nextLine().toLowerCase().trim();
            checkQuit(city);
            String[][] locations;
            if (state.length() == 2) {
                locations = Locations.getLocationsByPostal(city, state);
            } else {
                locations = Locations.getLocationsByState(city, state);
            }
            if (locations != null) {
                System.out.println("Here are the locations in the area that you specified.");
                for (String[] location : locations) {
                    System.out.println(location[2] + ", " + location[1] + ", " + location[3]);
                }
                locFound = true;
            } else {
                System.out.print(Apologies.getRandom()+" There were no locations found/the city and state were invalid. ");
            }
        } while (!locFound);
        state = "askQuestion";
        System.out.print("Hopefully you found what you were looking for. ");
        parseState();
    }

    private void nutrition() {
        boolean nutrFound = false;
        do {
            System.out.println("Regarding nutrition, what food item would you like to get information about? Type \"menu\" to see our menu.");
            String food = sc.nextLine().toLowerCase().trim();
            checkQuit(food);
            if (keyword(food, new String[]{"menu"})) {
                Menu.printMenu();
                continue;
            }
            double[] nutrInfo = Menu.getInfo(food);
            if (nutrInfo != null) {
                System.out.println(Utils.capitalize(food) + " has " + (int) nutrInfo[1] + " calories.");
                nutrFound = true;
            } else {
                System.out.print(Apologies.getRandom() + " ");
            }
        } while (!nutrFound);
        state = "askQuestion";
        System.out.print("Hopefully you found what you were looking for. ");
        parseState();
    }

    private void viewOrders() {
        System.out.println("Here is the list of current orders:\n\n" + orders.asText());
        System.out.println("Would you like to update any of these orders?");
        readYesNo("updateOrder", "askQuestion", " Would you like to update an order?");
    }

    private void updateOrder() {
        ArrayList<String> added = new ArrayList<String>();
        ArrayList<String> removed = new ArrayList<String>();
        int orderNum = 0;
        do {
            System.out.println("Please input the order number of the order you would like to update.");
            String s = sc.nextLine().trim();
            checkQuit(s);
            if (Utils.strIsInt(s) && orders.viewOrder(Integer.parseInt(s)) != null) {
                orderNum = Integer.parseInt(s);
            }
        } while (orderNum == 0);
        
        boolean finished = false;
        do {
            System.out.println("Please enter an item in you order you would like to remove. Type \"menu\" anytime to view the menu. When finished, type \"finished\"");
            String food = sc.nextLine().toLowerCase().trim();
            checkQuit(food);
            if (keyword(food, new String[]{"menu"})) {
                Menu.printMenu();
                continue;
            }
            if (keyword(food, new String[]{"finished", "finish", "done", "end", "ready"})) {
                if (removed.size() == 0) {
                    System.out.print("No items were removed from order.");
                    finished = true;
                } else {
                    finished = true;
                }
            } else {
                if (Menu.getInfo(food) != null && Arrays.asList(orders.viewOrder(orderNum).getItems()).contains(Utils.capitalize(food))) {
                    food = Utils.capitalize(food);
                    orders.removeItem(orderNum, food);
                    removed.add(food);
                    System.out.println(food + " was removed from order #" + orderNum + ".");
                    continue;
                } else {
                    System.out.println("Food item not found/not in order. Please check your spelling and try again. Thanks!");
                    continue;
                }
            }
        } while (!finished);

        System.out.println();
        finished = false;

        do {
            System.out.println("Please enter an item in you order you would like to add. Type \"menu\" anytime to view the menu. When finished, type \"finished\"");
            String food = sc.nextLine().toLowerCase().trim();
            checkQuit(food);
            if (keyword(food, new String[]{"menu"})) {
                Menu.printMenu();
                continue;
            }
            if (keyword(food, new String[]{"finished", "finish", "done", "end", "ready"})) {
                if (added.size() == 0) {
                    System.out.print("No items were added to order.");
                    finished = true;
                } else {
                    finished = true;
                }
            } else {
                if (Menu.getInfo(food) != null) {
                    food = Utils.capitalize(food);
                    orders.addItem(orderNum, food);
                    added.add(food);
                    System.out.println(food + " was added to order #" + orderNum + ".");
                    continue;
                } else {
                    System.out.println("Food item not found. Please check your spelling and try again. Thanks!");
                    continue;
                }
            }
        } while (!finished);
        viewOrder order = orders.viewOrder(orderNum);
        String isNew = (added.size() > 0 || removed.size() > 0) ? "new" : "";
        System.out.println("Here are your " + isNew + " order details: \n");
        System.out.println(order.asText());
        state = "askQuestion";
        System.out.print("Now that your order has been updated, ");
        parseState();
    }

    private void contactInfo() {
        System.out.println("If you have any questions, please go to https://in-n-out.com/contact if you have any particular questions, comments, and concerns.\nWe are also available by phone, you can dial an associate at 1-800-786-1000. Our office hours are:\nSunday to Thursday: 8am - 1am\nFriday to Saturday: 8am to 1:30am\nYou can also write directly to customer service, here is our mailbox:\nIn-N-Out Burgers Corporate Office\n4199 Campus Drive, 9th Floor\nIrvine, CA 92612\n\nNow that's out of the way, would you like me to take your order now, or no?");
        state = "askQuestion";
        System.out.print("Now that's out of the way, ");
        parseState();
    }

}

class Orders {
    private ArrayList<Order> orders = new ArrayList<Order>();

    viewOrder viewOrder(int orderNumber) {
        for (Order order : orders) {
            if (order.getNumber() == orderNumber) {
                return new viewOrder(order);
            }
        }
        return null;
    }

    viewOrder saveOrder(ArrayList<String> items, ArrayList<Double> prices) {
        int orderNum = genOrderNum();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getNumber() == orderNum) {
                orderNum = genOrderNum();
                i = 0;
            }
        }
        Order order = new Order(orderNum, items, prices);
        orders.add(order);
        return new viewOrder(order);
    }

    void removeItem(int orderNum, String toRemove) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getNumber() == orderNum) {
                orders.get(i).removeItem(toRemove);
            }
        }
    }

    void addItem(int orderNum, String toAdd) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getNumber() == orderNum) {
                orders.get(i).addItem(toAdd);
            }
        }
    }

    void deleteOrder(int orderNumber) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getNumber() == orderNumber) {
                orders.remove(i);
                break;
            }
        }
    }

    String asText() {
        String string = "";

        for (int i = 0; i < orders.size(); i++) {
            string += orders.get(i).asText() + ((i == orders.size() - 1) ? "" : "\n\n");
        }

        return string;
    }

    private int genOrderNum() {
        return Utils.randint(1, 100);
    }
}

class viewOrder {
    private final int orderNumber;
    private String[] items;
    private Double[] prices;

    viewOrder(int orderNumber, String[] items, Double[] prices) {
        this.orderNumber = orderNumber;
        this.items = items;
        this.prices = prices;
    }

    viewOrder(Order order) {
        this.orderNumber = order.getNumber();
        this.items = order.getItems();
        this.prices = order.getPrices();
    }

    int getNumber() {
        return orderNumber;
    }

    String[] getItems() {
        return items;
    }

    Double[] getPrices() {
        return prices;
    }

    String asText() {
        String string = "";

        string += "Order #" + String.valueOf(orderNumber) + ": \n";
        for (int i = 0; i < items.length; i++) {
            string += items[i] + " - $" + prices[i];
            string += (i == items.length - 1) ? "\n" : ", ";
        }
        string += "Total: $" + Utils.sum(prices);

        return string;
    }
    
}

class Order {
    private final int orderNumber;
    private ArrayList<String> items;
    private ArrayList<Double> prices;

    Order(int orderNumber, ArrayList<String> items, ArrayList<Double> prices) {
        this.orderNumber = orderNumber;
        this.items = items;
        this.prices = prices;
    }
    int getNumber() {
        return orderNumber;
    }

    String[] getItems() {
        return items.toArray(new String[0]);
    }

    Double[] getPrices() {
        return prices.toArray(new Double[0]);
    }

    void removeItem(String toRemove) {
        int index = items.indexOf(toRemove);
        if (index > -1) {
            items.remove(index);
            prices.remove(index);
        }
            
    }

    void addItem(String toAdd) {
        items.add(toAdd);
        prices.add(Menu.getInfo(toAdd)[0]);
    }

    String asText() {
        String string = "";

        string += "Order #" + String.valueOf(orderNumber) + ": \n";
        for (int i = 0; i < items.size(); i++) {
            string += items.get(i) + " - $" + prices.get(i);
            string += (i == items.size() - 1) ? "\n" : ", ";
        }
        string += "Total: $" + Utils.sum(prices.toArray(new Double[0]));

        return string;
    }
}

class Secret {
    static final String[] ITEMS = {
            "4X4",
            "3X3",
            "Double Meat",
            "Grilled Cheese",
            "Animal Fries"
    };
    static final double[] PRICES = {
            10.99,
            8.49,
            5.19,
            3.19,
            4.25
    };
    static final double[] CALORIES = {
            970.0,
            790.0,
            470.0,
            380.0,
            750.0
    };

    static double[] getInfo(String food) {
        for (int i = 0; i < ITEMS.length; i++) {
            if (ITEMS[i].toLowerCase().equals(food.toLowerCase())) {
                return new double[] { PRICES[i], CALORIES[i] };
            }
        }
        return null;
    }
}

class Greetings {
    static final String[] allGreetings = {
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

    static String getRandom() {
        int randomInt = Utils.randint(0, allGreetings.length-1);

        return allGreetings[randomInt];
    }
}

class Goodbyes {
    static final String[] allGoodbyes = {
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

    static String getRandom() {
        int randomInt = Utils.randint(0, allGoodbyes.length-1);

        return allGoodbyes[randomInt];
    }
}

class Menu {
    static String[] ITEMS = new String[Food.ITEMS.length + Drinks.ITEMS.length + Shakes.ITEMS.length + Secret.ITEMS.length];
    static double[] PRICES = new double[Food.ITEMS.length + Drinks.ITEMS.length + Shakes.ITEMS.length + Secret.ITEMS.length];
    static double[] CALORIES = new double[Food.ITEMS.length + Drinks.ITEMS.length + Shakes.ITEMS.length + Secret.ITEMS.length];
    static {
        for (int i = 0; i<Food.ITEMS.length;i++) {
            ITEMS[i] = Food.ITEMS[i];
            PRICES[i] = Food.PRICES[i];
            CALORIES[i] = Food.CALORIES[i];
        }
        for (int i = 0; i<Drinks.ITEMS.length;i++) {
            ITEMS[Food.ITEMS.length + i] = Drinks.ITEMS[i];
            PRICES[Food.ITEMS.length + i] = Drinks.PRICES[i];
            CALORIES[Food.ITEMS.length + i] = Drinks.CALORIES[i];
        }
        for (int i = 0; i<Shakes.ITEMS.length;i++) {
            ITEMS[Food.ITEMS.length + Drinks.ITEMS.length + i] = Shakes.ITEMS[i];
            PRICES[Food.ITEMS.length + Drinks.ITEMS.length + i] = Shakes.PRICES[i];
            CALORIES[Food.ITEMS.length + Drinks.ITEMS.length + i] = Shakes.CALORIES[i];
        }
        for (int i = 0; i<Secret.ITEMS.length;i++) {
            ITEMS[Food.ITEMS.length + Drinks.ITEMS.length + Shakes.ITEMS.length + i] = Secret.ITEMS[i];
            PRICES[Food.ITEMS.length + Drinks.ITEMS.length + Shakes.ITEMS.length + i] = Secret.PRICES[i];
            CALORIES[Food.ITEMS.length + Drinks.ITEMS.length + Shakes.ITEMS.length + i] = Secret.CALORIES[i];
        }
    }
    static double[] getInfo(String food) {
        for (int i = 0; i < ITEMS.length; i++) {
            if (ITEMS[i].toLowerCase().equals(food.toLowerCase())) {
                return new double[] { PRICES[i], CALORIES[i] };
            }
        }
        return null;
    }
    static void printMenu() {
        System.out.println("Food items:");
        for (int i = 0; i<Food.ITEMS.length;i++) {
            System.out.println(Food.ITEMS[i] + ": $" + Food.PRICES[i]);
        }
        System.out.println("\nDrinks:");
        for (int i = 0; i<Drinks.ITEMS.length;i++) {
            System.out.println(Drinks.ITEMS[i] + ": $" + Drinks.PRICES[i]);
        }
        System.out.println("\nShakes:");
        for (int i = 0; i<Shakes.ITEMS.length;i++) {
            System.out.println(Shakes.ITEMS[i] + ": $" + Shakes.PRICES[i]);
        }
        System.out.println("\n(Not so) Secret Menu:");
        for (int i = 0; i<Secret.ITEMS.length;i++) {
            System.out.println(Secret.ITEMS[i] + ": $" + Secret.PRICES[i]);
        }
    }
}

class Food {
    static final String[] ITEMS = {
            "Double-Double",
            "Cheeseburger",
            "Hamburger",
            "French Fries",
    };
    static final double[] PRICES = {
            5.89,
            4.19,
            3.69,
            2.79
    };
    static final double[] CALORIES = {
            610.0,
            430.0,
            360.0,
            360.0
    };

    static double[] getInfo(String food) {
        for (int i = 0; i < ITEMS.length; i++) {
            if (ITEMS[i].toLowerCase().equals(food.toLowerCase())) {
                return new double[] { PRICES[i], CALORIES[i] };
            }
        }
        return null;
    }
}

class Drinks {
    static final String[] ITEMS = {
            "Coca-Cola",
            "Diet Coke",
            "Cherry Coke",
            "7UP",
            "Dr. Pepper",
            "Root Beer",
            "Signature Pink Lemonade",
            "Lite Pink Lemonade",
            "Iced Tea",
            "Milk",
            "Coffee",
            "Hot Cocoa"
    };
    static final double[] PRICES = {
            2.19,
            2.19,
            2.19,
            2.19,
            2.19,
            2.19,
            2.19,
            2.19,
            2.19,
            1.39,
            1.79,
            2.49
    };
    static final double[] CALORIES = {
            130.0,
            0.0,
            140.0,
            130.0,
            130.0,
            150.0,
            150.0,
            5.0,
            0.0,
            150.0,
            0.0,
            160.0
    };

    static double[] getInfo(String food) {
        for (int i = 0; i < ITEMS.length; i++) {
            if (ITEMS[i].toLowerCase().equals(food.toLowerCase())) {
                return new double[] { PRICES[i], CALORIES[i] };
            }
        }
        return null;
    }
}

class Shakes {
    static final String[] ITEMS = {
            "Chocolate Shake",
            "Strawberry Shake",
            "Vanilla Shake"
    };
    static final double[] PRICES = {
            3.59,
            3.59,
            3.59
    };
    static final double[] CALORIES = {
            610.0,
            610.0,
            590.0
    };

    static double[] getInfo(String food) {
        for (int i = 0; i < ITEMS.length; i++) {
            if (ITEMS[i].toLowerCase().equals(food.toLowerCase())) {
                return new double[] { PRICES[i], CALORIES[i] };
            }
        }
        return null;
    }
}

class Apologies {
    static final String[] allGreetings = {
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

    static String getRandom() {
        int randomInt = Utils.randint(0, allGreetings.length-1);

        return allGreetings[randomInt];
    }
}

class Locations {
    static final String[] locations = """
              1 	Baldwin Park 	13850 Francisquito Ave. 	CA 	10/22/1948
              2 	Covina 	1371 Grand Ave. 	CA 	3/1/1951
              3 	La Verne 	2098 Foothill Blvd. 	CA 	9/18/1952
              4 	West Covina 	15610 San Bernardino 	CA 	7/3/1962
              5 	Pasadena 	2114 E. Foothill 	CA 	10/5/1959
              6 	Azusa 	324 S. Azusa Ave. 	CA 	8/1/1966
              7 	La Puente 	15259 E. Amar Rd. 	CA 	5/1/1968
              8 	Pomona 	1851 Indian Hill Blvd. 	CA 	4/1/1970
              9 	North Hollywood 	5864 Lankershim Blvd. 	CA 	5/10/1971
              10 	Panorama City 	13651 Roscoe Blvd. 	CA 	3/10/1972
              11 	Hacienda Heights 	14620 E. Gale Ave. 	CA 	7/24/1972
              12 	San Fernando 	11455 Laurel Canyon Blvd. 	CA 	6/2/1973
              13 	Temple City 	10601 E. Lower Azusa Rd. 	CA 	8/27/1973
              14 	Norwalk 	14330 Pioneer Blvd. 	CA 	2/18/1974
              15 	Diamond Bar 	21133 Golden Springs Dr. 	CA 	6/30/1974
              16 	Tujunga 	6225 Foothill Blvd. 	CA 	4/9/1975
              17 	Santa Ana 	815 N. Bristol St. 	CA 	9/28/1975
              18 	Woodland Hills 	19920 Ventura Blvd. 	CA 	2/8/1976
              19 	Garden Grove 	9032 Trask Ave. 	CA 	6/25/1978
              20 	Rosemead 	4242 N. Rosemead Blvd. 	CA 	12/21/1978
              21 	Ontario 	2235 Mountain Ave. 	CA 	10/4/1979
              22 	Fontana 	9855 Sierra Ave. 	CA 	12/27/1979
              23 	Rancho Cucamonga 	8955 Foothill Blvd. 	CA 	9/15/1980
              24 	Anaheim 	600 S. Brookhurst St. 	CA 	12/3/1980
              25 	Corona 	450 Auto Center Dr. 	CA	reopened 11/10/2011
              26 	San Bernardino 	795 W. 5th Street 	CA 	2/11/1982
              27 	Arcadia 	420 N. Santa Anita Ave. 	CA 	6/8/1982
              28 	San Juan Capistrano 	28782 Camino Capistrano 	CA 	8/24/1982
              29 	Hesperia 	13704 Main St. 	CA 	6/24/1983
              30 	Torrance 	730 W. Carson St. 	CA 	12/23/1983
              31 	San Bernardino 	1065 E. Harriman Pl. 	CA 	2/28/1984
              32 	Placentia 	825 W . Chapman Ave. 	CA 	5/25/1984
              33 	Pomona 	2505 Garey Ave. 	CA 	8/16/1984
              34 	Ontario 	1891 E. G St. 	CA 	10/19/1984
              35 	Northridge 	9858 Balboa Blvd. 	CA 	12/26/1984
              36 	La Habra 	2030 E. Lambert Rd. 	CA 	5/14/1985
              37 	Westminster 	6292 Westminster Blvd. 	CA 	6/29/1985
              38 	Costa Mesa 	594 W. 19th St. 	CA 	9/23/1985
              39 	Riverside 	6634 Clay St. 	CA 	11/29/1985
              40 	Lakewood 	5820 Bellflower Blvd. 	CA 	12/28/1985
              41 	Camarillo 	1316 Ventura Blvd. 	CA 	8/27/1986
              42 	Buena Park 	7926 Valley View St. 	CA 	9/25/1986
              43 	Lancaster 	2021 W . Avenue I 	CA 	11/26/1986
              44 	Moreno Valley 	23035 Hemlock Ave. 	CA 	1/7/1987
              45 	Upland 	837 Foothill Blvd. 	CA 	4/6/1987
              46 	Riverside 	7467 Indiana Ave. 	CA 	6/30/1987
              47 	Santa Fe Springs 	10525 Carmenita Rd. 	CA 	10/21/1987
              48 	Norco 	1810 Hamner Ave. 	CA 	3/5/1988
              49 	Pico Rivera 	9070 Whittier Blvd. 	CA 	5/13/1988
              50 	Thousand Palms 	72265 Varner Rd. 	CA 	6/17/1988
              51 	Indio 	82043 Hwy. 111 	CA 	6/17/1988
              52 	Hemet 	W. Florida Ave. 	CA 	9/9/1988
              53 	Newhall 	25220 N. The Old Road 	CA 	12/28/1988
              54 	Lake Elsinore 	331 Railroad Canyon Rd. 	CA 	5/18/1989
              55 	Los Angeles 	9245 W. Venice Blvd. 	CA 	9/6/1989
              56 	Barstow 	2821 Lenwood Rd. 	CA 	11/14/1989
              57 	Lemon Grove 	7160 Broadway 	CA 	1/16/1990
              58 	Newbury Park 	1550 Newbury Rd. 	CA 	2/28/1990
              59 	Orange 	3501 E. Chapman Ave. 	CA 	3/9/1990
              60 	Long Beach 	4600 Los Coyotes Diagonal 	CA 	6/6/1990
              61 	Vista 	2010 Hacienda Dr. 	CA 	8/30/1990
              62 	Temecula 	27700 Jefferson Ave. 	CA 	9/19/1990
              63 	Tustin 	3020 El Camino Real 	CA 	10/10/1990
              64 	West Covina 	2940 E. Garvey Ave. 	CA 	12/22/1990
              65 	Bakersfield 	2310 Panama Ln. 	CA 	4/24/1991
              66 	City of Industry 	17849 E. Colima Rd. 	CA 	6/18/1991
              67 	Irvine 	4115 Campus Dr. 	CA 	7/25/1991
              68 	San Diego 	9410 Mira Mesa Blvd. 	CA 	8/13/1991
              69 	Pacific Beach 	2910 Damon Ave. 	CA 	10/24/1991
              70 	National City 	500 Mile of Cars Way 	CA 	11/18/1991
              71 	San Diego/Kearny Mesa 	4375 Kearny Mesa Rd. 	CA 	12/18/1991
              72 	Hesperia 	17069 Bear Valley Rd. 	CA 	3/10/1992
              73 	El Cajon 	100 Fletcher Parkway 	CA 	3/31/1992
              74 	Huntington Park 	6000 Pacific Blvd. 	CA 	4/23/1992
              75 	Torrance 	24445 Crenshaw Blvd. 	CA 	7/30/1992
              76 	Ventura 	2070 Harbor Blvd. 	CA 	9/8/1992
              77 	Fullerton 	1180 S. Harbor Blvd. 	CA 	9/16/1992
              78 	Simi Valley 	2600 Stearns St. 	CA 	10/22/1992
              79 	Palmdale 	142 E. Palmdale Blvd. 	CA 	10/30/1992
              80 	Las Vegas 	2900 W. Sahara Ave. 	NV 	11/17/1992
              81 	Las Vegas 	51 N. Nellis Blvd. 	NV 	11/18/1992
              82 	Fresno 	5106 W. Shaw Ave. 	CA 	11/30/1992
              83 	Fresno 	4302 N. Blackstone Ave. 	CA 	12/18/1992
              84 	Rancho Cucamonga 	12599 Foothill Blvd. 	CA 	4/14/1993
              85 	Sherman Oaks 	4444 Van Nuys Blvd. 	CA 	7/15/1993
              86 	Las Vegas 	14888 Dean Martin Dr. 	NV 	7/28/1993
              87 	Carmel Mountain 	11880 Carmel Mountain Rd. 	CA 	8/2/1993
              88 	Las Vegas 	4705 S. Maryland Pkwy. 	NV 	8/31/1993
              89 	Carlsbad 	5950 Avenida Encinas 	CA 	10/5/1993
              90 	Modesto 	3900 Pelandale Ave. 	CA 	10/18/1993
              91 	Merced 	1579 Martin Luther King Jr. 	CA 	11/18/1993
              92 	Redondo Beach 	3801 Inglewood Ave. 	CA 	12/3/1993
              93 	Fresno 	2657 S. Second St. 	CA 	12/15/1993
              94 	Laguna Niguel 	27380 La Paz Rd. 	CA 	1/3/1994
              95 	Kettleman City 	33464 Bernard Dr. 	CA 	3/24/1994
              96 	Tracy 	575 Clover Rd. 	CA 	6/1/1994
              97 	Foothill Ranch 	26482 Towne Centre Dr. 	CA 	7/6/1994
              98 	Atascadero 	6000 San Anselmo Rd. 	CA 	7/22/1994
              99 	Salinas 	151 Kern St. 	CA 	9/21/1994
              100 	Gilroy 	641 Leavesley Rd. 	CA 	11/10/1994
              101 	Stockton 	2727 W . March Ln. 	CA 	11/30/1994
              102 	Hollywood 	7009 Sunset Blvd. 	CA 	12/15/1994
              103 	Vacaville 	170 Nut Tree Pkwy. 	CA 	1/4/1995
              104 	Las Vegas 	1960 Rock Springs Dr. 	NV 	2/28/1995
              105 	La Mirada 	14341 Firestone Blvd. 	CA 	5/31/1995
              106 	Visalia 	1933 S. Mooney Blvd. 	CA 	6/21/1995
              107 	Santa Clarita 	28368 Sand Canyon Rd. 	CA 	10/19/1995
              108 	Goleta 	4865 Calle Real 	CA 	11/14/1995
              109 	Auburn 	130 Grass Valley Hwy. 	CA 	11/30/1995
              110 	Burbank 	761 N. First St. 	CA 	12/14/1995
              111 	Los Angeles 	3640 Cahuenga Blvd. 	CA 	12/27/1995
              112 	Downey 	8767 Firestone Blvd. 	CA 	3/5/1996
              113 	Rohnert Park 	5145 Redwood Dr. 	CA 	3/27/1996
              114 	Long Beach 	6391 E. Pacific Coast Hwy. 	CA 	4/25/1996
              115 	Milpitas 	50 Ranch Dr. 	CA 	9/5/1996
              116 	Elk Grove 	9188 E. Stockton Blvd. 	CA 	11/14/1996
              117 	Los Angeles 	9149 S. Sepulveda Blvd. 	CA 	1/22/1997
              118 	San Ramon 	2270 San Ramon Valley Blvd. 	CA 	2/27/1997
              119 	Westwood 	922 Gayley Ave. 	CA 	4/1/1997
              120 	Pinole 	1417 Fitzgerald Dr. 	CA 	4/24/1997
              121 	Alhambra 	1210 N. Atlantic Blvd. 	CA 	5/14/1997
              122 	Rancho Cordova 	2475 Sunrise Blvd. 	CA 	6/3/1997
              123 	San Diego 	3102 Sports Arena Blvd. 	CA 	6/27/1997
              124 	Pleasanton 	6015 Johnson Dr. 	CA 	8/6/1997
              125 	Fairfield 	1364 Holiday Ln. 	CA 	9/9/1997
              126 	Napa 	820 W. Imola Ave. 	CA 	10/16/1997
              127 	Davis 	1020 Olive Dr. 	CA 	1/23/1998
              128 	Mission Valley 	2005 Camino Del Este 	CA 	5/21/1998
              129 	Mountain View 	1159 N. Rengstorff Ave. 	CA 	6/11/1998
              130 	Sunnyvale 	604 E. El Camino Real 	CA 	7/2/1998
              131 	Sacramento 	3501 Truxel Rd. 	CA 	8/13/1998
              132 	Roseville 	1803 Taylor Rd. 	CA 	10/15/1998
              133 	Santa Ana 	3361 S. Bristol St. 	CA 	11/11/1998
              134 	Van Nuys 	7930 Van Nuys Blvd. 	CA 	12/10/1998
              135 	Long Beach 	7691 Carson Street 	CA 	4/21/1999
              136 	Santa Maria 	1330 S. Bradley Rd. 	CA 	5/27/1999
              137 	Redding 	1275 Dana Drive 	CA 	6/25/1999
              138 	Anaheim Hills 	5646 E. La Palma Ave 	CA 	8/12/1999
              139 	Placerville 	3055 Forni Road 	CA 	9/30/1999
              140 	Marina Del Rey 	13425 Washington Blvd. 	CA 	10/28/1999
              141 	Mill Valley 	798 Redwood Hwy. 	CA 	12/16/1999
              142 	San Jose 	5611 Santa Teresa Blvd. 	CA 	1/26/2000
              143 	Lake Havasu 	81-101 London Bridge Rd. 	AZ 	5/3/2000
              144 	Ontario 	4310 E. Ontario Mills Parkway 	CA 	7/18/2000
              145 	Bakersfield 	5100 Stockdale Hwy. 	CA 	10/2/2000
              146 	Scottsdale 	7467 E. Frank Lloyd Wright Bl. 	AZ 	11/2/2000
              147 	Chico 	2050 Business Ln. 	CA 	11/16/2000
              148 	Avondale 	1525 Dysart Road 	AZ 	12/7/2000
              149 	Livermore 	1881 N Livermore Ave 	CA 	2/15/2001
              150 	Henderson 	1051 W . Sunset Road 	NV 	3/29/2001
              151 	San Pedro 	1090 N. Western Ave. 	CA 	6/14/2001
              152 	Mountain View 	53 W. El Camino Real 	CA 	7/12/2001
              153 	Chandler 	7050 W. Ray Road 	AZ 	8/23/2001
              154 	San Francisco 	333 Jefferson St. 	CA 	9/20/2001
              155 	Mesa 	1650 S. Stapley 	AZ 	10/25/2001
              156 	Union City 	32060 Union Landing Blvd. 	CA 	11/1/2001
              157 	Glendale 	310 N. Harvey Drive 	CA 	11/15/2001
              158 	San Jose 	2950 E. Capitol Expressway 	CA 	12/6/2001
              159 	Daly City 	260 Washington Street 	CA 	1/17/2002
              160 	Phoenix 	21001 N. Tatum Blvd. 	AZ 	2/14/2002
              161 	Turlock 	3071 Countryside Dr. 	CA 	3/14/2002
              162 	Oceanside 	4605 Frazee Rd 	CA 	4/4/2002
              163 	Oxnard 	381 W . Esplanade 	CA 	5/2/2002
              164 	Chandler 	2790 W. Chandler 	AZ 	6/6/2002
              165 	Las Vegas 	9240 S. Eastern Ave. 	NV 	7/10/2002
              166 	Inglewood 	3411 W. Century Blvd. 	CA 	8/6/2002
              167 	Chino 	3927 Grand Avenue 	CA 	9/5/2002
              168 	Pittsburg 	4550 Delta Gateway Blvd. 	CA 	10/8/2002
              169 	Peoria 	8285 W. Bell Rd. 	AZ 	1/16/2003
              170 	Laguna Hills 	24001 Avenida De La Carlota 	CA 	1/30/2003
              171 	Santa Clarita 	26401 Bouquet Canyon Rd. 	CA 	2/20/2003
              172 	Glendora 	1261 S. Lone Hill 	CA 	3/27/2003
              173 	Fresno 	8010 N. Blackstone Ave. 	CA 	4/30/2003
              174 	Glendale 	119 Brand Blvd. 	CA 	6/12/2003
              175 	Corona 	2305 Compton Ave. 	CA 	6/19/2003
              176 	Santa Clara 	3001 Mission College Blvd. 	CA 	8/7/2003
              177 	Phoenix 	19407 N. 27th Avenue 	AZ 	9/11/2003
              178 	Porter Ranch 	19901 Rinaldi St. 	CA 	10/30/2003
              179 	Moorpark 	856 New Los Angeles Ave 	CA 	11/20/2003
              180 	Huntington Beach 	18062 Beach Blvd. 	CA 	1/27/2004
              181 	Laughlin 	2058 Casino Dr. 	NV 	3/4/2004
              182 	Orange 	2585 N. Tustin Ave. 	CA 	4/21/2004
              183 	Roseville 	10309 Fairway Dr. 	CA 	6/10/2004
              184 	Prescott 	3040 Hwy. 69 	AZ 	6/29/2004
              185 	San Marcos 	583 Grand Ave. 	CA 	7/27/2004
              186 	Reno 	8215 S. Virginia 	NV 	9/23/2004
              187 	Carson Valley 	957 Topsy Lane 	NV 	10/14/2004
              188 	Sparks 	280 Pyramid Way 	NV 	11/18/2004
              189 	Brentwood 	5581 Lone Tree Way 	CA 	12/16/2004
              190 	Millbrae 	11 Rollins Rd. 	CA 	3/31/2005
              191 	Manteca 	1490 E. Yosemite Avenue 	CA 	4/12/2005
              192 	Paradise Valley 	12413 N. Tatum Blvd. 	AZ 	4/28/2005
              193 	Oakland 	8300 Oakport St. 	CA 	5/18/2005
              194 	Fremont 	43349 Pacific Commons Blvd. 	CA 	8/17/2005
              195 	Tempe 	920 E. Playa Del Norte 	AZ 	9/15/2005
              196 	Van Nuys 	7220 Balboa Blvd. 	CA 	10/5/2005
              197 	Lodi 	2625 W . Kettleman Ln. 	CA 	10/13/2005
              198 	Morgan Hill 	895 Cochrane Road 	CA 	11/30/2005
              199 	City of Industry 	21620 Valley Blvd. 	CA 	12/20/2005
              200 	Temecula 	30697 Temecula Pkwy. 	CA 	12/30/2005
              201 	Kingman 	1770 E. Beverly Ave. 	AZ 	6/15/2006
              202 	Petaluma 	1010 Lakeville St. 	CA 	6/29/2006
              203 	Yuma 	1940 E. 16th St. 	AZ 	11/16/2006
              204 	Las Vegas 	5690 Centennial Center Blvd. 	NV 	2/7/2007
              205 	Gilbert 	2449 S. Market St. 	AZ 	3/1/2007
              206 	Canoga Park 	6841 N. Topanga Canyon Blvd. 	CA 	4/10/2007
              207 	Tucson 	3711 E. Broadway Blvd. 	AZ 	4/24/2007
              208 	Northridge 	8830 Tampa Ave. 	CA 	8/9/2007
              209 	Lebec/Tejon Ranch 	5926 Dennis McCarthy Dr. 	CA 	8/30/2007
              210 	Marana 	8180 Cortaro Rd. 	AZ 	11/15/2007
              211 	El Centro 	2390 E. 4th St. 	CA 	12/13/2007
              212 	Murrieta 	39697 Avenida Acacias 	CA 	12/20/2007
              213 	Sacramento 	2900 Del Paso Rd. 	CA 	12/28/2007
              214 	Yuba City 	1375 Sunsweet Blvd. 	CA 	3/27/2008
              215 	Washington City 	832 W. Telegraph St. 	UT 	4/22/2008
              216 	Tustin 	2895 Park Ave. 	CA 	4/29/2008
              217 	Sacramento 	4600 Madison Ave. 	CA 	5/29/2008
              218 	Mesa 	1859 S. Signal Butte Rd. 	AZ 	6/12/2008
              219 	Chandler 	2910 E. Germann Rd. 	AZ 	7/15/2008
              220 	Clovis 	382 N. Clovis Ave. 	CA 	8/21/2008
              221 	Las Vegas 	3882 Blue Diamond Rd. 	NV 	8/28/2008
              222 	San Leandro 	15575 Hesperian Blvd. 	CA 	9/30/2008
              223 	Casa Grande 	873 N. Promenade Pkwy. 	AZ 	10/16/2008
              224 	Menifee 	30296 Haun Rd. 	CA 	10/28/2008
              225 	West Sacramento 	780 Ikea Court 	CA 	12/5/2008
              226 	Las Vegas 	9610 W. Tropicana Ave. 	NV 	12/18/2008
              227 	Chula Vista 	1725 Eastlake Pkwy. 	CA 	12/23/2008
              228 	Phoenix 	34850 N. Valley Pkwy. 	AZ 	1/20/2009
              229 	Folsom 	225 Placerville Rd. 	CA 	1/29/2009
              230 	Mesa 	1342 S. Alma School Rd. 	AZ 	4/30/2009
              231 	Woodland 	2011 Bronze Star Dr. 	CA 	5/7/2009
              232 	Phoenix 	9585 W. Camelback Rd. 	AZ 	6/11/2009
              233 	Victorville 	15290 Civic Drive Rd. 	CA 	9/24/2009
              234 	North Las Vega 	6880 N. 5th St. 	NV 	10/29/2009
              235 	Orem 	350 E. University Pkwy. 	UT 	11/19/2009
              236 	Draper 	12191 South Factory Outlet Dr. 	UT 	11/19/2009
              237 	West Jordan 	7785 Jordan Landing Blvd. 	UT 	12/1/2009
              238 	Tucson 	1979 E. Ajo Way 	AZ 	12/15/2009
              239 	American Fork 	601 West Main St. 	UT 	12/17/2009
              240 	San Jose 	550 Newhall Dr. 	CA 	1/14/2010
              241 	Oro Valley 	11545 N. Oracle Rd. 	AZ 	6/9/2010
              242 	Tucson 	7111 E. Broadway Blvd. 	AZ 	6/9/2010
              243 	North Las Vegas 	2765 E. Craig Rd. 	NV 	6/23/2010
              244 	Riverton 	12569 S. Crossing Dr. 	UT 	7/21/2010
              245 	West Valley City 	3715 S. Constitution Blvd. 	UT 	7/21/2010
              246 	Redwood City 	949 Veterans Blvd. 	CA 	8/26/2010
              247 	Escondido 	1260 W . Valley Parkway 	CA 	9/30/2010
              248 	Centerville 	475 N. 700 West 	UT 	10/14/2010
              249 	Santa Rosa 	2131 County Center Dr. 	CA 	10/28/2010
              250 	Arroyo Grande 	1170 W . Branch St. 	CA 	11/16/2010
              251 	Costa Mesa 	3211 Harbor Blvd. 	CA 	12/17/2010
              252 	Pheonix 	2770 W . Peoria Ave. 	AZ 	2/17/2011
              253 	Tucson 	4620 N Oracle Rd. 	AZ 	3/17/2011
              254 	Poway 	12890 Gregg Court 	CA 	4/21/2011
              255 	Allen 	190 E Stacy Rd. 	TX 	5/11/2011
              256 	Frisco 	2800 Preston Rd. 	TX 	5/11/2011
              257 	San Carlos 	445 Industrial Rd. 	CA 	6/16/2011
              258 	Dallas 	7940 N. Central Exwy. 	TX 	6/23/2011
              259 	Dallas 	7909 Lyndon B Johnson Fwy. 	TX 	8/11/2011
              260 	Fort Worth 	2900 W 7th St (at Currie St.) 	TX 	8/11/2011
              261 	Midvale 	7206 S. Union Park Ave 	UT 	8/25/2011
              262 	Arlington 	1075 W. I-20 	TX 	9/22/2011
              263 	Irving 	6501 N. Macarthur Blvd. 	TX 	9/29/2011
              264 	Rockwall 	1106 E. I-30 	TX 	10/20/2011
              265 	Signal Hill 	799 E. Spring St. 	CA 	10/27/2011
              266 	Dallas 	12909 Midway Rd. 	TX 	11/3/2011
              267 	Santa Nella 	28900 Henry Miller Rd. 	CA 	1/26/2012
              268 	Dallas 	15260 Dallas Pkwy. 	TX 	3/2/2012
              269 	Garland 	150 Town Center Blvd. 	TX 	3/1/2012
              270 	Torrance 	20150 Hawthorne Blvd. 	CA 	3/14/2012
              271 	Seal Beach 	12365 Seal Beach Blvd. 	CA 	3/15/2012
              272 	Highland 	28009 Greenspot Rd. 	CA 	3/29/2012
              273 	Hurst 	780 Airport Fwy. 	TX 	4/5/2012
              274 	Pleasant Hill 	570 Contra Costa Blvd. 	CA 	5/17/2012
              275 	Lancaster 	740 N. I-35 E. 	TX 	5/31/2012
              276 	Sacramento 	2001 Alta Arden Exwy. 	CA 	6/7/2012
              277 	Plano 	2740 N. Central Exwy. 	TX 	6/14/2012
              278 	Fort Worth 	5916 Quebec St. 	TX 	6/29/2012
              279 	Santee 	9414 Mission Gorge Rd. 	CA 	11/28/2012
              280 	Fort Worth 	4620 S. Hulen St. 	TX 	12/6/2012
              281 	Rancho Santa Margarita 	30121 Santa Margarita Pkwy. 	CA 	2/13/2013
              282 	Mansfield 	1221 N. Hwy. 287 	TX 	6/27/2013
              283 	Grapevine 	1303 William D. Tate Ave. 	TX 	7/11/2013
              284 	Riverdale 	4030 Riverdale Rd 	UT 	8/15/2013
              285 	Modesto 	1616 Sisk Rd. 	CA 	8/22/2013
              286 	Rialto 	108 E. Easton St. 	CA 	8/29/2013
              287 	South Gate 	4601 Firestone Blvd. 	CA 	10/31/2013
              288 	Novato 	216 Vintage Way 	CA 	11/14/2013
              289 	Round Rock 	4251 I-35 North 	TX 	12/3/2013
              290 	Austin 	4508 I-35 North 	TX 	12/12/2013
              291 	Hanford 	280 S. 12Th Ave. 	CA 	12/19/2013
              292 	Cabazon 	49188 Seminole Dr. 	CA 	1/15/2014
              293 	Queen Creek 	20545 E Rittenhouse 	AZ 	3/26/2014
              294 	Cedar Park 	901 E Whitestone Blvd. 	TX 	4/10/2014
              295 	Encinitas 	130 Calle Magdalena 	CA 	5/22/2014
              296 	Killeen 	2006 E. Central Texas Exwy. 	TX 	10/8/2014
              297 	Westlake Village 	30780 Russell Ranch Rd. 	CA 	11/05/2014
              298 	San Antonio 	10918 Culebra Rd. 	TX 	11/20/2014
              299 	The Colony 	5298 State Highway 121 	TX 	11/20/2014
              300 	Anahiem 	1168 State College Blvd. 	CA 	1/7/2015
              301 	Fort Worth 	2501 N. Tarrant Pkwy. 	TX 	2/6/2015
              302 	Windcrest 	8202 I-35 North 	TX 	4/23/2015
              303 	Alameda 	555 Willie Stargell Ave. 	CA 	5/14/2015
              304 	Medford 	1970 Carter Lake Hwy. 	OR 	9/9/2015
              305 	Cerritos 	10900 Alondra Blvd. 	CA 	 10/23/2015
              306 	Austin 	3701 S. Lamar Blvd. 	TX 	11/12/2015
              307 	Waco 	801 South 4th St. 	TX 	11/20/2015
              308 	Rocklin 	5490 Crossings Dr. 	CA 	12/3/2015
              309 	El Segundo 	600 N. Sepulveda Blvd. 	CA 	12/30/2015
              310 	Gardena 	1701 Artesia Blvd 	CA 	1/23/2016
              311 	La Quinta 	78611 Hwy. 111 	CA 	2/3/2016
              312 	Seaside 	1350 Del Monte Blvd 	CA 	2/22/2016
              313 	Oceanside 	936 N. Coast Highway 	CA 	4/7/2016
              314 	San Marcos 	1437 North Ih-35 	TX 	5/16/2016
              315 	Austin 	12431 N. I-35 	TX 	9/15/2016
              316 	Pflugerville 	1600 Town Center Dr. 	TX 	10/13/2016
              317 	Phoenix 	4840 N. 20th St. 	AZ 	11/3/2016
              318 	Yucaipa 	31465 Avenue E 	CA 	11/16/2016
              319 	Austin 	2700 Guadalupe St 	TX 	12/1/2016
              320 	Las Vegas 	3545 S. Las Vegas Blvd, Ste L24 	NV 	1/11/2017
              321 	Ukiah 	1351 N. State St 	CA 	1/25/2017
              322 	Denton 	2835 W. University Dr. 	TX 	2/9/2017
              323 	San Antonio 	22226 N. Us Hwy 281 	TX 	2/23/2017
              324 	Lincoln 	850 Groveland Lane 	CA 	5/25/2017
              325 	New Braunfels 	106 Fm 306 	TX 	6/15/2017
              326 	Daly City 	372 Gellert Blvd. 	CA 	7/27/2017
              327 	Sacramento 	8200 Delta Shores Circle 	CA 	8/24/2017
              328 	Grants Pass 	124 Ne Morgan Lane 	OR 	10/27/2017
              329 	Fort Worth 	3500 Highway 114 	TX 	2/4/2018
              330 	La Habra 	1761 W. Whittier Blvd. 	CA 	5/3/2018
              331 	Las Vegas 	10040 W. Sahara Ave. 	NV 	5/25/2018
              332 	Surprise 	14321 W. Bell Rd. 	AZ 	6/8/2018
              333 	Long Beach 	4041 Lakewood Blvd. 	CA 	6/28/2018
              334 	Las Vegas 	3804 East Flamingo Rd. 	NV 	10/18/2018
              335 	Rancho Mission Viejo 	30655 Gateway Pl. 	CA 	10/24/2018
              336 	Upland 	152 East 20th Street 	CA 	10/31/2018
              337 	Monterey Park 	5500 Market Place Dr. 	CA 	12/09/2018
              338 	Mesa 	3156 North Recker Road 	AZ 	12/17/2018
              339 	Vallejo 	720 Admiral Callaghan Ln. 	CA 	1/13/2019
              340 	Eastvale 	4950 Hamner Avenue 	CA 	2/8/2019
              341 	Dallas 	2525 W. Mockingbird Ln. 	TX 	2/22/2019
              342 	San Jose 	5590 Cottle Road 	CA 	5/2/2019
              343 	Azusa 	988 E. Alosta Ave. 	CA 	5/27/2019
              344 	Bellflower 	17325 Bellflower Blvd. 	CA 	8/23/2019
              345 	Stanton 	12975 Beach Blvd. 	CA 	9/26/2019
              346 	Lathrop 	6514 Golden Valley Parkway 	CA 	10/10/2019
              347 	Compton 	2200 S. Central Ave. 	CA 	10/24/2019
              348 	Redding 	4681 Churn Creek Rd. 	CA 	11/12/2019
              349 	Katy 	1010 Katy-fort Bend Rd. 	TX 	11/22/2019
              350 	Stafford 	12611 S. Kirkwood Rd. 	TX 	11/22/2019
              351 	Keizer 	6280 Keizer Station Blvd. 	OR 	12/12/2019
              352 	Las Vegas 	6530 S. Rainbow Blvd. 	NV 	3/18/2020
              353 	Riverside 	22410 Van Buren Blvd. 	CA 	4/6/2020
              354 	Eureka 	22616 Broadway Street 	CA 	7/15/2020
              355 	Beaumont 	21551 E. 2nd Street 	CA 	7/20/2020
              356 	Tucson 	1100 W. Irvington Rd. 	AZ 	8/20/2020
              357 	Houston 	7611 FM 1960 W 	TX 	9/4/2020
              358 	Spanish Fork 	795 East 800 North 	UT 	11/3/2020
              359 	Colorado Springs 	1840 Democracy Point 	CO 	11/20/2020
              360 	Aurora 	14150 E. Alameda Ave. 	CO 	11/20/2020
              361 	Tempe 	2405 W. Baseline Rd. 	AZ 	12/04/2020
              362 	Bakersfield 	7985 Rosedale Hwy. 	CA 	1/28/2021
              363 	Lone Tree 	9171 W. Westview Rd. 	CO 	2/22/2021
              364 	Henderson 	3551 St. Rose Pkwy. 	NV 	5/21/2021
              365 	Riverside 	3483 Van Buren Boulevard 	CA 	7/2/2021
              366 	Lakewood 	150 S. Wadsworth 	CO 	7/12/2021
              367 	Tulare 	1591 E. Prosperity Ave. 	CA 	8/19/2021
              368 	Visalia 	506 W. Riggin Ave. 	CA 	9/10/2021
              369 	Stockton 	5202 Pacific Ave. 	CA 	9/17/2021
              370 	Brea 	1130 E. Imperial Hwy. 	CA 	9/17/2021
              371 	Colorado Springs 	2895 New Center Point Road 	CO 	11/3/2021
              372 	Whittier 	11802 Whittier Blvd. 	CA 	12/3/2021
              373 	Yorba Linda 	18181 Imperial Hwy. 	CA 	12/14/2021
              374 	Rancho Mirage 	42560 Bob Hope Dr. 	CA 	1/7/2022
              375 	San Diego 	10370 Friars Rd. 	CA 	1/14/2022
              376 	Thornton 	111 E. 136th Ave. 	CO 	2/10/2022
              377 	Reno 	915 W. 5th Street 	NV 	2/24/2022
              378 	Castle Rock 	5470 Factory Shops Blvd. 	CO 	3/18/2022
              379 	Santa Ana 	1809 N. Tustin Ave. 	CA 	7/14/2022
              380 	Logan 	404 N. Main St. 	UT 	8/17/2022
              381 	Valencia 	27510 The Old Road 	CA 	10/14/2022
              382 	Roseburg 	2844 Nw Aviation Dr. 	OR 	11/10/2022
              383 	San Diego 	1093 Outer Rd. 	CA 	11/18/2022
              384 	The Woodlands 	1717 Lake Woodlands Drive 	TX 	12/1/2022
              385 	San Clemente 	115 Via Pico Plaza 	CA 	12/9/2022
              386 	Ceres 	2942 Service Road 	CA 	1/13/2023
              387 	Huntington Beach 	7902 Edinger Ave. 	CA 	1/26/2023
              388 	Elk Grove 	7620 Elk Grove Blvd. 	CA 	2/23/2023
              389 	Delano 	505 Woollomes Ave. 	CA 	3/10/2023
              390 	Denver 	4597 N. Central Park Blvd. 	CO 	4/28/2023
              391 	Chula Vista 	1810 Main Court 	CA 	5/10/2023
              392 	Santa Rosa 	2532 Santa Rosa Ave. 	CA 	7/13/2023
              393 	Las Vegas 	3397 W. Russell Rd. 	NV 	8/31/2023
              394 	Rialto 	1456 S Riverside Ave. 	CA 	9/7/2023
              395 	Loveland 	1450 Fall River Dr. 	CO 	11/10/2023
              396 	Cypress 	28320 Highway 290 	TX 	11/18/2023
              397 	Webster 	122 El Dorado Blvd. 	TX 	11/18/2023
              398 	Perris 	229 Old Nuevo Rd. 	CA 	11/30/2023
              399 	San Juan Capistrano 	31791 Del Obispo St. 	CA 	12/7/2023
              400 	Meridian 	3520 E. Fairview Ave. 	ID 	12/12/2023
              401 	Madera 	1830 W. Cleveland Ave 	CA 	1/5/2024
              402 	Flagstaff 	1860 South Milton Rd. 	AZ 	4/02/2024
              403 	Jurupa Valley 	4038 Pyrite St. 	CA 	05/31/2024
              404 	Orange 	250 The City Drive South 	CA 	07/26/2024
              405 	Redlands 	1301 W. Lugonia Ave 	CA 	08/01/2024
              406 	Layton 	1977 N. 1200 W. 	UT 	08/22/2024
              407 	Arvada 	7494 W. 52nd Ave. 	CO 	09/05/2024
              408 	Parker 	9558 Twenty Mile Rd. 	CO 	09/05/2024
            """.split("\n");

    static String[][] getLocationsByPostal(String city, String postal) {
        postal = postal.toUpperCase();
        boolean matched = false;
        ArrayList<String[]> matches = new ArrayList<String[]>();
        for (String location : locations) {
            if (location.toLowerCase().contains(city.toLowerCase()) && location.contains(postal)) {
                matches.add(location.split("\t"));
                matched = true;
            }
        }
        return matched ? matches.toArray(new String[0][0]) : null;
    }
    static String[][] getLocationsByState(String city, String state) {
        String[] states = """
Alaska: AK
Arizona: AZ
Arkansas: AR
California: CA
Colorado: CO
Connecticut: CT
Delaware: DE
District of Columbia: DC
Florida: FL
Georgia: GA
Hawaii: HI
Idaho: ID
Illinois: IL
Indiana: IN
Iowa: IA
Kansas: KS
Kentucky: KY
Louisiana: LA
Maine: ME
Maryland: MD
Massachusetts: MA
Michigan: MI
Minnesota: MN
Mississippi: MS
Missouri: MO
Montana: MT
Nebraska: NE
Nevada: NV
New Hampshire: NH
New Jersey: NJ
New Mexico: NM
New York: NY
North Carolina: NC
North Dakota: ND
Ohio: OH
Oklahoma: OK
Oregon: OR
Pennsylvania: PA
Rhode Island: RI
South Carolina: SC
South Dakota: SD
Tennessee: TN
Texas: TX
Utah: UT
Vermont: VT
Virginia: VA
Washington: WA
West Virginia: WV
Wisconsin: WI
Wyoming: WY
""".split("\n");
        for (String s : states) {
            if (s.toLowerCase().contains(state.toLowerCase())) {
                state = s.split(": ")[1];
                break;
            }
        }
        return getLocationsByPostal(city, state);
    }

}
