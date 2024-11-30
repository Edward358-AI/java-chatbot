package chatbot;

import java.util.*;

import orders.Order;
import utility.*;
import data.*;
import responses.*;

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
    private final String[] QUIT = { "quit" , "q"};
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

    private boolean keyword(String s, String[] keywords, boolean checkAll) {
        boolean contained = false;
        if (checkAll) {
            contained = true;
            for (String keyword : keywords) {
                if (!s.contains(keyword)) {
                    contained = false;
                    break;
                }
            }
        } else {
            for (String keyword : keywords) {
                if (s.contains(keyword)) {
                    contained = true;
                    break;
                }
            }
        }

        return contained;
    }

    private boolean keyword(String input, String[] keywords) {
        return keyword(input, keywords, false);
    }

    private boolean keyContains(String input, String[] keywords) {
        return keyContains(input, keywords, false);
    }
    
    private boolean keyContains(String s, String[] keywords, boolean checkAll) {
        boolean contained = false;
        s = s.trim();
        List<String> input = Arrays.asList(s.split(" "));
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
        if (keyContains(input, QUIT)) {
            state = "goodbye";
            parseState();
        }
    }

    private void readYesNo(String yesState, String noState, String customErrMsg) {
        String tempState = null;
        do {
            String resp = sc.nextLine().toLowerCase();
            if (keyContains(resp, QUIT)) {
                tempState = "goodbye";
            } else {
                if (keyword(resp, YES) && !keyword(resp, NO)) {
                    tempState = yesState;
                } else if (!keyword(resp, YES) && keyword(resp, NO)) {
                    tempState = noState;
                } else {
                    Printer.print(Apologies.getRandom() + customErrMsg);
                    tempState = null;
                }
            }
        } while (tempState == null);
        state = tempState;
        parseState();
    }

    private void askQuestion() {
        Printer.print("Do you have any questions regarding location, nutrition, or contact info, or would you like to view the orders? Please choose one.");
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
            if (keyContains(resp, QUIT)) {
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
                    Printer.print(Apologies.getRandom() + " Would you like information regarding location, nutrition, or contact info? Or would you like to view the order queue?");
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
        Printer.print(String.format(
                "%s I am In 'n' Out's virtual assistant. If at any time you would like to stop chatting, just say \"quit\" or \"q\" anytime. Can I take your order today?",
                Greetings.getRandom()));
        readYesNo("takeOrder", "askQuestion", " Can I take your order today?");
    }

    private void goodbye() {
        Printer.print(String.format(
                "%s As the In 'n' Out chatbot, I don't necessarily deliver quality you can taste, but quality you can trust! Until next time!", Goodbyes.getRandom()));
    }

    private void takeOrder() {
        ArrayList<String> orderItems = new ArrayList<String>();
        ArrayList<Double> orderPrices = new ArrayList<Double>();
        
        boolean finished = false;
        Printer.print("Here is our menu:\n");
        Menu.printMenu();
        Printer.print();
        do {
            Printer.print("Please type out the name of an item you would like to add to your order. If you are finished type \"finished\".");
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
                    Printer.print(Utils.capitalize(cItem) + " added to your order.");
                } else {
                    Printer.print("Food item not found. Please check your spelling and try again. Thanks!");
                    continue;
                }
            }
        } while (!finished);
        viewOrder order = orders.saveOrder(orderItems, orderPrices);
        Printer.print("Here are your order details: \n");
        Printer.print(order.asText());
        state = "askQuestion";
        System.out.print("While you're waiting, ");
        parseState();
    }

    private void location() {
        boolean locFound = false;
        do {
            Printer.print("Regarding location, what state would you like to get information about? You can enter either the name or postal code (the two letter abbreviation).");
            String state = sc.nextLine().toLowerCase().trim();
            checkQuit(state);
            Printer.print("What city in that state would you like location information regarding?");
            String city = sc.nextLine().toLowerCase().trim();
            checkQuit(city);
            String[][] locations;
            if (state.length() == 2) {
                locations = Locations.getLocationsByPostal(city, state);
            } else {
                locations = Locations.getLocationsByState(city, state);
            }
            if (locations != null) {
                Printer.print("Here are the locations in the area that you specified.");
                for (String[] location : locations) {
                    Printer.print(location[2] + ", " + location[1] + ", " + location[3]);
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
            Printer.print("Regarding nutrition, what food item would you like to get information about? Type \"menu\" or \"m\" to see our menu.");
            String food = sc.nextLine().toLowerCase().trim();
            checkQuit(food);
            if (keyContains(food, new String[]{"menu", "m"})) {
                Menu.printMenu();
                continue;
            }
            double[] nutrInfo = Menu.getInfo(food);
            if (nutrInfo != null) {
                Printer.print(Utils.capitalize(food) + " has " + (int) nutrInfo[1] + " calories.");
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
        if (orders.queueEmpty()) {
            Printer.print("There are no orders to display.");
            state = "askQuestion";
            parseState();
        } else {
            Printer.print("Here is the list of current orders:\n\n" + orders.asText());
            Printer.print("Would you like to update any of these orders?");
            readYesNo("updateOrder", "askQuestion", " Would you like to update an order?");
        }
    }

    private void updateOrder() {
        ArrayList<String> added = new ArrayList<String>();
        ArrayList<String> removed = new ArrayList<String>();
        int orderNum = 0;
        do {
            Printer.print("Please input the order number of the order you would like to update.");
            String s = sc.nextLine().trim();
            checkQuit(s);
            if (Utils.strIsInt(s) && orders.viewOrder(Integer.parseInt(s)) != null) {
                orderNum = Integer.parseInt(s);
            }
        } while (orderNum == 0);
        
        boolean finished = false;
        do {
            Printer.print("Please enter an item in you order you would like to remove. Type \"menu\" or \"m\" anytime to view the menu. When finished, type \"finished\"");
            String food = sc.nextLine().toLowerCase().trim();
            checkQuit(food);
            if (keyContains(food, new String[]{"menu", "m"})) {
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
                    Printer.print(food + " was removed from order #" + orderNum + ".");
                    continue;
                } else {
                    Printer.print("Food item not found/not in order. Please check your spelling and try again. Thanks!");
                    continue;
                }
            }
        } while (!finished);

        Printer.print();
        finished = false;

        do {
            Printer.print("Please enter an item in you order you would like to add. Type \"menu\" or \"m\" anytime to view the menu. When finished, type \"finished\"");
            String food = sc.nextLine().toLowerCase().trim();
            checkQuit(food);
            if (keyContains(food, new String[]{"menu", "m"})) {
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
                    Printer.print(food + " was added to order #" + orderNum + ".");
                    continue;
                } else {
                    Printer.print("Food item not found. Please check your spelling and try again. Thanks!");
                    continue;
                }
            }
        } while (!finished);
        viewOrder order = orders.viewOrder(orderNum);
        String isNew = (added.size() > 0 || removed.size() > 0) ? "new" : "";
        Printer.print("Here are your " + isNew + " order details: \n");
        Printer.print(order.asText());
        state = "askQuestion";
        System.out.print("Now that your order has been updated, ");
        parseState();
    }

    private void contactInfo() {
        Printer.print("If you have any questions, please go to https://in-n-out.com/contact if you have any particular questions, comments, and concerns.\nWe are also available by phone, you can dial an associate at 1-800-786-1000. Our office hours are:\nSunday to Thursday: 8am - 1am\nFriday to Saturday: 8am to 1:30am\nYou can also write directly to customer service, here is our mailbox:\nIn-N-Out Burgers Corporate Office\n4199 Campus Drive, 9th Floor\nIrvine, CA 92612\n\nNow that's out of the way, would you like me to take your order now, or no?");
        state = "askQuestion";
        System.out.print("Now that's out of the way, ");
        parseState();
    }

}

class Orders {
    private ArrayList<Order> orders = new ArrayList<Order>();

    boolean queueEmpty() {
        return orders.size() == 0;
    }

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
