package uk.co.cmcmarkets.orderbook.model;

import uk.co.cmcmarkets.orderbook.iface.Action;
import uk.co.cmcmarkets.orderbook.iface.Order;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

/**
 * @author Z
 */
public class OrderBook {

    private Map<Integer, BookInfo> bidMap = new ConcurrentSkipListMap<>(Collections.reverseOrder());
    private Map<Integer, BookInfo> askMap = new ConcurrentSkipListMap<>();
    private String symbol;

    public OrderBook(String symbol) {
        this.symbol = symbol;
    }

    public void processTrade(Action action, Order order) {
        assert order.getSymbol().equals(symbol);

        final int price = order.getPrice();
        final Map<Integer, BookInfo> map = order.isBuy() ? bidMap : askMap;
        map.putIfAbsent(price, new BookInfo());
        map.get(price).process(action, order);

    }

    public String summary() {
        return String.format("%s\nAsk Price\tAsk Size\tOrder Count\n%s\nBid Price\tBid Size\tOrder Count\n%s", symbol, mapSummary(askMap), mapSummary(bidMap));
    }

    private String mapSummary(Map<Integer, BookInfo> map) {
        return String.join("\n",
                map.entrySet().stream()
                        .map(entry -> String.format("%s\t%s", entry.getKey(), entry.getValue().summary()))
                        .filter(line -> !line.contains("0\t0"))
                        .collect(Collectors.toList()));
    }
}
