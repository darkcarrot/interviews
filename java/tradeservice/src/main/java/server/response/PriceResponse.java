package server.response;

/**
 * Created by Dark on 12/01/2017.
 */
public class PriceResponse extends BaseResponse {
    private final double averagePrice;

    public PriceResponse(String symbol, double average) {
        super(symbol);
        this.averagePrice = average;
    }

    public double getAveragePrice() {
        return averagePrice;
    }
}
