package server.response;

/**
 * Created by Dark on 12/01/2017.
 */
public class SizeResponse extends BaseResponse {
    private final int largestSize;

    public SizeResponse(String symbol, int size) {
        super(symbol);
        this.largestSize = size;
    }

    public int getLargestSize() {
        return largestSize;
    }
}
