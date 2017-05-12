package server.trade;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Zihao Chen
 */
public class TradeCache {

    private final Map<String, Integer> sizeMap = Maps.newHashMap();
    private final Map<String, Average> priceMap = Maps.newHashMap();
    private final Map<String, Integer> symbolFlagMap = Maps.newHashMap();

    private final ReentrantReadWriteLock sizeMapLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock priceMapLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock symbolFlagMapLock = new ReentrantReadWriteLock();

    public void addNewTrade(Trade trade) {
        updateSizeMap(trade);
        updatePriceMap(trade);
        updateSymbolFlagMap(trade);
    }

    public int getLargest(String symbol) {
        sizeMapLock.readLock().lock();
        try {
            return sizeMap.getOrDefault(symbol, 0);
        } finally {
            sizeMapLock.readLock().unlock();
        }
    }

    public double getAverage(String symbol) {
        priceMapLock.readLock().lock();
        try {
            final Average average = priceMap.getOrDefault(symbol, new Average());
            return average.getAverage();
        } finally {
            priceMapLock.readLock().unlock();
        }
    }

    public int countFlag(String symbol, String flag) {
        final String key = makeKey(symbol, flag);

        symbolFlagMapLock.readLock().lock();
        try {
            return symbolFlagMap.getOrDefault(key, 0);
        } finally {
            symbolFlagMapLock.readLock().unlock();
        }
    }

    @VisibleForTesting
    protected void updateSizeMap(Trade trade) {
        final String symbol = trade.getSymbol();
        final int incoming = trade.getSize();

        sizeMapLock.writeLock().lock();
        try {
            final Integer current = sizeMap.getOrDefault(symbol, 0);
            if (incoming > current) {
                sizeMap.put(symbol, incoming);
            }
        } finally {
            sizeMapLock.writeLock().unlock();
        }
    }

    @VisibleForTesting
    protected void updatePriceMap(Trade trade) {
        final String symbol = trade.getSymbol();
        final double price = trade.getPrice();

        priceMapLock.writeLock().lock();
        try {
            final Average current = priceMap.getOrDefault(symbol, new Average());
            current.updateTotal(price);
            priceMap.put(symbol, current);
        } finally {
            priceMapLock.writeLock().unlock();
        }
    }

    @VisibleForTesting
    protected void updateSymbolFlagMap(Trade trade) {
        final String symbol = trade.getSymbol();
        final Set<String> flags = trade.getFlags();

        flags.forEach(flag -> {
            final String key = makeKey(symbol, flag);
            symbolFlagMapLock.writeLock().lock();
            try {
                int count = symbolFlagMap.getOrDefault(key, 0);
                count = count + 1;
                symbolFlagMap.put(key, count);
            } finally {
                symbolFlagMapLock.writeLock().unlock();
            }
        });
    }

    private String makeKey(String symbol, String flag) {
        return String.format("%s-%s", symbol, flag);
    }

    private static class Average {
        private double total = 0.0;
        private int count = 0;

        Average() {
            this(0.0, 0);
        }

        Average(double total, int count) {
            this.total = total;
            this.count = count;
        }

        void updateTotal(double price) {
            this.total += price;
            this.count++;
        }

        double getAverage() {
            if (count == 0) {
                return 0.0;
            }
            return total / count;
        }
    }
}
