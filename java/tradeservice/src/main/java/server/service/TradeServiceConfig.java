package server.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.trade.TradeCache;

/**
 * @author Zihao Chen
 */
@Configuration
public class TradeServiceConfig {

    @Bean
    public TradeCache provideTradeCache() {
        return new TradeCache();
    }
}
