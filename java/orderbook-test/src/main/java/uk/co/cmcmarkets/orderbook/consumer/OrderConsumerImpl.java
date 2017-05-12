package uk.co.cmcmarkets.orderbook.consumer;

import uk.co.cmcmarkets.orderbook.iface.*;
import uk.co.cmcmarkets.orderbook.model.OrderBook;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderConsumerImpl implements OrderConsumer {

    private final Map<String, OrderBook> orderBooks = new ConcurrentHashMap<>();
    private final Map<Long, Order> allOrders = new ConcurrentHashMap<>();
    private Log log;

    @Override
    public void startProcessing(Log log) {
        this.log = log;
        log.log(LogLevel.INFO, "Start processing orders");
    }

    @Override
    public void finishProcessing() {
        orderBooks.values().forEach(values -> log.log(LogLevel.INFO, String.format("\n%s\n", values.summary())));
    }

    @Override
    public void handleEvent(Action action, Order order) {
        final long orderId = order.getOrderId();
        final String symbol;
        final Order previousOrder;
        switch (action) {
            case ADD:
                symbol = order.getSymbol();
                orderBooks.putIfAbsent(symbol, new OrderBook(symbol));
                allOrders.put(orderId, order);
                orderBooks.get(symbol).processTrade(action, order);
                break;
            case REMOVE:
                previousOrder = allOrders.get(orderId);
                symbol = previousOrder.getSymbol();
                orderBooks.get(symbol).processTrade(action, previousOrder);
                allOrders.remove(orderId);
                break;
            case EDIT:
                previousOrder = allOrders.get(orderId);
                symbol = previousOrder.getSymbol();
                orderBooks.get(symbol).processTrade(Action.REMOVE, previousOrder);
                final Order newOrder = new Order(order.getOrderId(), previousOrder.getSymbol(), previousOrder.isBuy(), order.getPrice(), order.getQuantity());
                orderBooks.get(symbol).processTrade(Action.ADD, newOrder);
                allOrders.put(orderId, newOrder);
                break;
            default:
                break;
        }
    }
}
