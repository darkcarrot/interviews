package uk.co.cmcmarkets.orderbook.app;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import uk.co.cmcmarkets.orderbook.iface.Action;
import uk.co.cmcmarkets.orderbook.iface.Order;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 */
public class XMLOrderParser extends DefaultHandler {

    private final AppXMLFeedsEnvironmentImpl environment;

    public XMLOrderParser(AppXMLFeedsEnvironmentImpl environment) {
        this.environment = environment;
    }

    public void parseDocument(String fileName) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory.newInstance().newSAXParser().parse(fileName, this);
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        Order order = null;
        Action action = null;
        if (qName.equals("add")) {
            action = Action.ADD;
            order = new Order(
                    Long.valueOf(attributes.getValue("order-id")),
                    attributes.getValue("symbol"),
                    attributes.getValue("type").equals("buy"),
                    Integer.valueOf(attributes.getValue("price")),
                    Integer.valueOf(attributes.getValue("quantity"))
            );
        } else if (qName.equals("edit")) {
            action = Action.EDIT;
            order = new Order(
                    Long.valueOf(attributes.getValue("order-id")),
                    null,
                    true,
                    Integer.valueOf(attributes.getValue("price")),
                    Integer.valueOf(attributes.getValue("quantity"))
            );
        } else if (qName.equals("remove")) {
            action = Action.REMOVE;
            order = new Order(
                    Long.valueOf(attributes.getValue("order-id")),
                    null,
                    true,
                    -1,
                    -1
            );
        } else {
            if (!qName.equals("commands"))
                throw new IllegalArgumentException("Non supported action: " + qName);
        }
        if (null != order)
            environment.notifyOrder(action, order);
    }
}
