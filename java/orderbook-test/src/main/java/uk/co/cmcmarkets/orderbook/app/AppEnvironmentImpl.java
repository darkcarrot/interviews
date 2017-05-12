package uk.co.cmcmarkets.orderbook.app;

import uk.co.cmcmarkets.orderbook.iface.*;
import uk.co.cmcmarkets.orderbook.iface.*;
import uk.co.cmcmarkets.orderbook.iface.*;

import java.util.LinkedHashSet;
import java.util.Set;


public abstract class AppEnvironmentImpl implements AppEnvironment {
    private final Set<OrderConsumer> consumers = new LinkedHashSet<OrderConsumer>();
    private final LogLevel logLevel;
    /**
     * Implementation of the {@link Log} facade which uses the standard out.
     */
    protected final Log log = new Log() {
        @Override
        public void log(LogLevel logLevel, String msg) {
            if (!isEnabled(logLevel)) {
                return;
            }
            System.out.println(logLevel + ": " + msg);
        }

        private boolean isEnabled(LogLevel logLevel) {
            return logLevel.compareTo(AppEnvironmentImpl.this.logLevel) >= 0;
        }
    };

    public AppEnvironmentImpl(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public void registerHandler(OrderConsumer handler) {
        consumers.add(handler);
    }

    @Override
    public final void run() {
        notifyStart();
        try {
            feedOrders();
        } catch (Exception e) {
            log.log(LogLevel.ERROR, e.getMessage());
        } finally {
            notifyFinish();
        }
    }

    /**
     * Sends a stream of orders to the {@link OrderConsumer}s.
     *
     * @throws Exception if there is an error.
     * @see #notifyOrder(Action, Order)
     */
    protected void feedOrders() throws Exception {}

    /**
     * Invokes {@link OrderConsumer#handleEvent(Action, Order)} for every registered consumer with
     * specified <code>action</code> and <code>order</code>.
     */
    protected final void notifyOrder(Action action, Order order) {
        for (OrderConsumer consumer : consumers) {
            consumer.handleEvent(action, order);
        }
    }

    private final void notifyStart() {
        for (OrderConsumer consumer : consumers) {
            consumer.startProcessing(log);
        }
    }

    private final void notifyFinish() {
        for (OrderConsumer consumer : consumers) {
            consumer.finishProcessing();
        }
    }
}
