public class Order {
    private final int orderNumber;
    private String[] items;
    
    public Order(int x, String[] y) {
        orderNumber = x;
        items = y;
    }
    
    public int getNumber() {
        return orderNumber;
    }

    public String[] getItems() {
        return items;
    }
}
