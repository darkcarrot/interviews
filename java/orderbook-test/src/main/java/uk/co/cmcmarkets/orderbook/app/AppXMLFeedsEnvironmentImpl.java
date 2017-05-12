package uk.co.cmcmarkets.orderbook.app;

import uk.co.cmcmarkets.orderbook.iface.Action;
import uk.co.cmcmarkets.orderbook.iface.LogLevel;
import uk.co.cmcmarkets.orderbook.iface.Order;
import uk.co.cmcmarkets.orderbook.iface.OrderConsumer;


public class AppXMLFeedsEnvironmentImpl extends AppEnvironmentImpl {
    private final String xmlFileName;

    public AppXMLFeedsEnvironmentImpl(LogLevel logLevel, String xmlFileName) {
        super(logLevel);
        this.xmlFileName = xmlFileName;
    }

    /**
     * Sends a stream of orders to the {@link OrderConsumer}s.
     *
     * @throws Exception if there is an error.
     * @see #notifyOrder(Action, Order)
     */
    protected void feedOrders() throws Exception {
        new XMLOrderParser(this).parseDocument(xmlFileName);
    }
}
