package server.trade;

import com.google.common.base.Objects;

import java.util.Set;

/**
 * A POJO for server.trade
 *
 * @author Zihao Chen
 */

public class Trade {

    private String Symbol;
    private double price;
    private int size;
    private Set<String> flags;
    private long timestamp;

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Set<String> getFlags() {
        return flags;
    }

    public void setFlags(Set<String> flags) {
        this.flags = flags;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Double.compare(trade.price, price) == 0 &&
                size == trade.size &&
                timestamp == trade.timestamp &&
                Objects.equal(Symbol, trade.Symbol) &&
                Objects.equal(flags, trade.flags);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Symbol, price, size, flags, timestamp);
    }
}
