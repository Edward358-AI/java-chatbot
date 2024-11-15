public class Order {
    private final int orderNumber;
    private String[] items;
    
    public Order(int orderNumber, String[] items) {
        this.orderNumber = orderNumber;
        this.items = items;
    }
    
    public int getNumber() {
        return orderNumber;
    }

    public String[] getItems() {
        return items;
    }

    public String toString() {
        String string = "";

        string += "Order #" + String.valueOf(orderNumber) + ": \n";
        for (int i = 0; i < items.length; i++) {
            string += "Item #" + String.valueOf(i) + ": " + items[i];
            string += (i == items.length - 1) ? "" : ", ";
        }
        
        return string;
    }
}
