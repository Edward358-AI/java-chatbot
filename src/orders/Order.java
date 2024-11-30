package orders;

import java.util.ArrayList;
import utility.Utils;
import data.Menu;

public class Order {
    private final int orderNumber;
    private ArrayList<String> items;
    private ArrayList<Double> prices;

    public Order(int orderNumber, ArrayList<String> items, ArrayList<Double> prices) {
        this.orderNumber = orderNumber;
        this.items = items;
        this.prices = prices;
    }

    public int getNumber() {
        return orderNumber;
    }

    public String[] getItems() {
        return items.toArray(new String[0]);
    }

    public Double[] getPrices() {
        return prices.toArray(new Double[0]);
    }

    public void removeItem(String toRemove) {
        int index = items.indexOf(toRemove);
        if (index > -1) {
            items.remove(index);
            prices.remove(index);
        }
            
    }

    public void addItem(String toAdd) {
        items.add(toAdd);
        prices.add(Menu.getInfo(toAdd)[0]);
    }

    public String asText() {
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
