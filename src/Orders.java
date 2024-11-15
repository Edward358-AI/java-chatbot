import java.util.*;

public class Orders {
    private ArrayList<Order> orders = new ArrayList<Order>();
    
    public Object[] viewOrder(int orderNumber) {
        for (Order order : orders) {
            if (order.getNumber() == orderNumber) {
                return order.toString();
            }
        }
        return null;
    }

    public void saveOrder(int orderNumber, String[] items) {
        int orderNum = orderNumber;
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getNumber() == orderNum) {
                orderNum = Utils.genOrderNum();
                i = 0;
            }
        }
        
        orders.add(new Order(orderNum, items));
    }

    public void deleteOrder(int orderNumber) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getNumber() == orderNum) {
                orders.remove(i);
                break;
            }
        }
    }

    public String toString() {
        String string = "";
        
        for (int i = 0; i < orders.size(); i++) {
            string += orders.get(i).toString() + " ";
        }

        return string;
    }
}
