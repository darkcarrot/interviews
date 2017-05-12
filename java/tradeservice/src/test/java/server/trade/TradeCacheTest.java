package server.trade;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * @author Zihao Chen
 */
public class TradeCacheTest {

    private TradeCache tradeCache;

    @Before
    public void setUp() throws Exception {
        tradeCache = new TradeCache();

        final List<Trade> trades = getTrades();
        trades.forEach(tradeCache::addNewTrade);
    }

    @Test
    public void getLargest() throws Exception {
        assertEquals(1000, tradeCache.getLargest("XYZ LN"));
        assertEquals(746, tradeCache.getLargest("123 LN"));
        assertEquals(0, tradeCache.getLargest("456 LN"));

    }

    @Test
    public void getAverage() throws Exception {
        assertEquals(200.005, tradeCache.getAverage("XYZ LN"), 5);
        assertEquals(98.60, tradeCache.getAverage("123 LN"), 5);
        assertEquals(0, tradeCache.getAverage("456 LN"), 5);
    }

    @Test
    public void countFlag() throws Exception {
        assertEquals(1, tradeCache.countFlag("XYZ LN", "A"));
        assertEquals(1, tradeCache.countFlag("XYZ LN", "B"));
        assertEquals(1, tradeCache.countFlag("XYZ LN", "Z"));
        assertEquals(0, tradeCache.countFlag("XYZ LN", "F"));

        assertEquals(1, tradeCache.countFlag("123 LN", "X"));
        assertEquals(0, tradeCache.countFlag("123 LN", "Y"));

        assertEquals(0, tradeCache.countFlag("456 LN", "O"));
    }

    @Test
    @Ignore
    public void updateSizeMap() throws Exception {
        //TODO
    }

    @Test
    @Ignore
    public void updatePriceMap() throws Exception {
        //TODO
    }

    @Test
    @Ignore
    public void updateSymbolFlagMap() throws Exception {
        //TODO
    }

    private List<Trade> getTrades() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final List<Trade> result = Lists.newArrayList();
        String csv;
        try {
            csv = IOUtils.toString(classLoader.getResourceAsStream("test.csv"));
        } catch (IOException e) {
            return result;
        }

        try {
            final CSVParser parse = CSVParser.parse(csv, CSVFormat.EXCEL.withHeader());
            parse.getRecords().forEach(record -> {
                final long timestamp = Long.valueOf(record.get(0));
                final String symbol = record.get(1);
                final double price = Double.valueOf(record.get(2));
                final int size = Integer.valueOf(record.get(3));
                final Set<String> flags = Sets.newHashSet(Arrays.asList(record.get(4).split("")));

                final Trade trade = createTrade(timestamp, symbol, price, size, flags);

                result.add(trade);
            });
        } catch (IOException e) {
            return result;
        }

        return result;
    }

    private Trade createTrade(long timestamp, String symbol, double price, int size, Set<String> flags) {
        final Trade trade = new Trade();
        trade.setTimestamp(timestamp);
        trade.setSymbol(symbol);
        trade.setPrice(price);
        trade.setSize(size);
        trade.setFlags(flags);
        return trade;
    }

}