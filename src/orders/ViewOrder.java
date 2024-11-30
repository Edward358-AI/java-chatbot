package orders;

import utility.Utils;
import utility.Colors;

public class ViewOrder {
    private int orderNumber;
    private String[] items;
    private Double[] prices;

    public ViewOrder(int orderNumber, String[] items, Double[] prices) {
        this.orderNumber = orderNumber;
        this.items = items;
        this.prices = prices;
    }

    public ViewOrder(Order order) {
        this.orderNumber = order.getNumber();
        this.items = order.getItems();
        this.prices = order.getPrices();
    }

    public int getNumber() {
        return orderNumber;
    }

    public String[] getItems() {
        return items;
    }

    public Double[] getPrices() {
        return prices;
    }

    public String asText() {
        String string = "";

        string += "Order #" + String.valueOf(orderNumber) + ": \n";
        for (int i = 0; i < items.length; i++) {
            string += items[i] + " - " + Colors.GREEN_FG + "$" + prices[i] + Colors.RESET;
            string += (i == items.length - 1) ? "\n" : ", ";
        }
        string += "Total: " + Colors.GREEN_FG + "$" + Utils.sum(prices) + Colors.RESET;

        return string;
    }
    
}
