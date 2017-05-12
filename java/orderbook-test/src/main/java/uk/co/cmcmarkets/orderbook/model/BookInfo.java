package uk.co.cmcmarkets.orderbook.model;

import uk.co.cmcmarkets.orderbook.iface.Action;
import uk.co.cmcmarkets.orderbook.iface.Order;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Z
 */
public class BookInfo {

    private Map<Long, Integer> orders = new ConcurrentHashMap<>();

    public void process(Action action, Order order) {
        final long orderId = order.getOrderId();
        switch (action) {
            case ADD:
                orders.put(orderId, order.getQuantity());
                break;
            case REMOVE:
                orders.remove(orderId);
                break;
            default:
                break;
        }
    }

    public String summary() {
        return String.format("%s\t%s", orders.values().stream().mapToInt(Integer::intValue).sum(), orders.keySet().size());
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "orders=" + orders +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookInfo bookInfo = (BookInfo) o;
        return Objects.equals(orders, bookInfo.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders);
    }
}
