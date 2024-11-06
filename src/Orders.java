import java.util.*;

public class Orders {
    private ArrayList<Order> orders = new ArrayList<Order>();
    
    public Object[] viewOrder(int orderNumber) {
        for (Order order : orders) {
            if (order.getNumber() == orderNumber) {
                return order.getItems();
            }
        }
        return null;
    }

    public void saveOrder(Order order) {
        orders.add(order);
    }

    public void deleteOrder(int orderNumber) {
        orders.remove(orderNumber);
    }

    public String toString() {
        String string = "";
        
        for (int i = 0; i < orders.size(); i++) {
            string += orders.get(i).toString() + " ";
        }

        return string;
    }
}
