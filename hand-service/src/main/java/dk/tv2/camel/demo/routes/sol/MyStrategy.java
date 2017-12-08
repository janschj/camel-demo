package dk.tv2.camel.demo.routes.sol;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * This is our own order aggregation strategy where we can control
 * how each splitted message should be combined. As we do not want to
 * loos any message we copy from the new to the old to preserve the
 * name lines as long we process them
 */
public class MyStrategy implements AggregationStrategy {
 
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // put name together in old exchange by adding the name from new exchange
 
        if (oldExchange == null) {
            // the first time we aggregate we only have the new exchange,
            // so we just return it
            String newLine = newExchange.getIn().getBody(String.class);
            System.out.println("Aggregate new ex: " + newLine);
           return newExchange;
        }
 
        String names = oldExchange.getIn().getBody(String.class);
        String newLine = newExchange.getIn().getBody(String.class);
 
        System.out.println("Aggregate old ex: " + names);
        System.out.println("Aggregate new ex: " + newLine);
 
        // put orders together separating by semi colon
        names = names + newLine;
        // put combined order back on old to preserve it
        oldExchange.getIn().setBody(names);
 
        System.out.println("Aggregatet: " + names);
       // return old as this is the one that has all the orders gathered until now
        return oldExchange;
    }
}
