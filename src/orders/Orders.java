package orders;

import java.util.ArrayList;
import utility.Utils;

public class Orders {
    private ArrayList<Order> orders = new ArrayList<Order>();

    public boolean queueEmpty() {
        return orders.size() == 0;
    }

    public ViewOrder viewOrder(int orderNumber) {
        for (Order order : orders) {
            if (order.getNumber() == orderNumber) {
                return new ViewOrder(order);
            }
        }
        return null;
    }

    public ViewOrder saveOrder(ArrayList<String> items, ArrayList<Double> prices) {
        int orderNum = genOrderNum();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getNumber() == orderNum) {
                orderNum = genOrderNum();
                i = 0;
            }
        }
        Order order = new Order(orderNum, items, prices);
        orders.add(order);

        return new ViewOrder(order);
    }

    public void removeItem(int orderNum, String toRemove) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getNumber() == orderNum) {
                orders.get(i).removeItem(toRemove);
            }
        }
    }

    public void addItem(int orderNum, String toAdd) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getNumber() == orderNum) {
                orders.get(i).addItem(toAdd);
            }
        }
    }

    public void deleteOrder(int orderNumber) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getNumber() == orderNumber) {
                orders.remove(i);
                break;
            }
        }
    }

    public String asText() {
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
