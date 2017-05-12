package server.response;

/**
 * @author Zihao Chen
 */
public class BaseResponse {

    private final String symbol;

    public BaseResponse(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
