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

        string += "order #" + String.valueOf(orderNumber) + ": ";
        for (int i = 0; i < items.length; i++) {
            string += "item #" + String.valueOf(i) + ": " + items[i];
        }
        
        return string;
    }
}
