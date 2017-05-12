package server.response;

/**
 * @author Zihao Chen
 */
public class FlagResponse extends BaseResponse {

    private final String flag;
    private final int count;

    public FlagResponse(String symbol, String flag, int count) {
        super(symbol);
        this.flag = flag;
        this.count = count;
    }

    public String getFlag() {
        return flag;
    }

    public int getCount() {
        return count;
    }
}
