package server.service;


import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import server.trade.Trade;

import java.net.URI;

/**
 * @author Zihao Chen
 */
public class TradeServiceTest {

    private RestTemplate rt;

    @Before
    public void setUp() {
        rt = new RestTemplate();
        rt.getMessageConverters().add(new StringHttpMessageConverter());
    }

    @Test
    @Ignore
    public void testSend() {

        final Trade trade = new Trade();
        trade.setTimestamp(1481107485791L);
        trade.setSymbol("XYZ LN");
        trade.setPrice(2010.01);
        trade.setSize(9128);
        trade.setFlags(Sets.newHashSet("Z", "A", "B"));

        rt.postForObject(URI.create("http://localhost:8080/newTrade"), trade, Trade.class);
    }

}