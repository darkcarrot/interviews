package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import server.response.BaseResponse;
import server.response.FlagResponse;
import server.response.PriceResponse;
import server.response.SizeResponse;
import server.trade.Trade;
import server.trade.TradeCache;

/**
 * @author Zihao Chen
 */
@RestController
@EnableAutoConfiguration
public class TradeService {

    @Autowired
    private TradeCache tradeCache;

    @RequestMapping(value = "/")
    public String index() {
        return "Hello World";
    }

    @RequestMapping(value = "/newTrade", method = RequestMethod.POST)
    public BaseResponse newTrade(@RequestBody Trade newTrade) {
        tradeCache.addNewTrade(newTrade);
        return new BaseResponse(newTrade.getSymbol());
    }

    @RequestMapping(value = "/largest", method = RequestMethod.GET)
    public BaseResponse getLargest(@RequestParam(value = "symbol") String symbol) {
        final int largest = tradeCache.getLargest(symbol);
        return new SizeResponse(symbol, largest);
    }

    @RequestMapping(value = "/average", method = RequestMethod.GET)
    public BaseResponse getAverage(@RequestParam(value = "symbol") String symbol) {
        final double average = tradeCache.getAverage(symbol);
        return new PriceResponse(symbol, average);
    }

    @RequestMapping(value = "/countFlag", method = RequestMethod.GET)
    public BaseResponse getFlagCount(@RequestParam(value = "symbol") String symbol,
                                     @RequestParam(value = "flag") String flag) {
        final int count = tradeCache.countFlag(symbol, flag);
        return new FlagResponse(symbol, flag, count);
    }


}
