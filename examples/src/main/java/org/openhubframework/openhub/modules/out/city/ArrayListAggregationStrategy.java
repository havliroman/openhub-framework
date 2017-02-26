/*
 * Copyright (c) 2016 BSC Praha, spol. s r.o.
 */

package org.openhubframework.openhub.modules.out.city;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Aggregation of the incoming messages from the new {@link Exchange} into
 * {@link java.util.List} of messages without condition and then save into old {@link Exchange}.
 *
 * This class uses {@link ArrayList} as a default implementation of the {@link java.util.List} interface.
 *
 * @author <a href="mailto:roman.havlicek@bsc-ideas.com">Roman Havlicek</a>
 */
public class ArrayListAggregationStrategy implements AggregationStrategy {

    public ArrayListAggregationStrategy() {
        super();
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Message newIn = newExchange.getIn();
        // in message in new body is converted into string using ToStringTypeConverter
        String newBody = newIn.getBody(String.class);
        ArrayList<String> list;
        // first iteration contains only new exchange, old exchange is null
        if (oldExchange == null) {
            list = new ArrayList<>();
            list.add(newBody);
            newIn.setBody(list);
            return newExchange;
        // all other iterations adds message from new exchange into old exchange
        } else {
            Message in = oldExchange.getIn();
            list = in.getBody(ArrayList.class);
            list.add(newBody);
            return oldExchange;
        }
    }
}
