/*
 * Copyright (c) 2016 BSC Praha, spol. s r.o.
 */

package org.openhubframework.openhub.modules.out.city;

import static org.openhubframework.openhub.modules.out.city.CitiesProcessingRoute.PROCESSING_ROUTE_BEAN;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.openhubframework.openhub.api.route.AbstractBasicRoute;
import org.openhubframework.openhub.api.route.CamelConfiguration;
import org.openhubframework.openhub.modules.ExampleProperties;
import org.openhubframework.openhub.modules.in.city.CitiesDto;
import org.openhubframework.openhub.modules.out.city.converter.CitiesConverter;

/**
 * Route for processing the response with list of cities in {@link CitiesDto}.
 *
 * @author <a href="mailto:roman.havlicek@bsc-ideas.com">Roman Havlicek</a>
 */
@CamelConfiguration(value = PROCESSING_ROUTE_BEAN)
public class CitiesProcessingRoute extends AbstractBasicRoute {

    static final String PROCESSING_ROUTE_BEAN = "citiesProcessingRouteBean";
    private static final String FILE_NAME = "cities.txt";
    private static final String ORIGINAL_BODY_PROPERTY_NAME = "bodyBeforeTransformation";

    @Override
    protected void doConfigure() throws Exception {

        // Handling general exceptions
        onException(Exception.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "General error occurred")
                .log(LoggingLevel.DEBUG, "${exception.stacktrace}")
                .transform().method(CitiesConverter.class, "processGeneralError")
        ;

        from(ExampleProperties.CITIES_PROCESSING_DIRECT)
                .choice()
                    .when(header("fileDirectory").isNotNull())
                        .to(ExampleProperties.CITIES_TO_FILE_DIRECT)
                    .otherwise()
                        .to(ExampleProperties.CITIES_REST_RESPONSE_DIRECT)
                .end();

        from(ExampleProperties.CITIES_TO_FILE_DIRECT)
                .log(LoggingLevel.INFO, "saving to file: ${header.fileDirectory}" + FILE_NAME)
                .setProperty(ORIGINAL_BODY_PROPERTY_NAME, body())
                .transform().method(CitiesConverter.class, "convertCitiesDtoForFile")
                // dynamic file path is composed of directory passed in header + fixed file name
                .to("file://?fileName=${header.fileDirectory}" + FILE_NAME)
                // restores original body  from before conversion into file format
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(exchange.getProperty(ORIGINAL_BODY_PROPERTY_NAME));
                    }
                })
                .to(ExampleProperties.CITIES_REST_RESPONSE_DIRECT)
        ;

        from(ExampleProperties.CITIES_REST_RESPONSE_DIRECT)
                // marshalling CityDto into JSON object - doesn't bloody work very well
                // this marshals CityDto into JSON, but as an array of bytes (not a String)
                .log(LoggingLevel.INFO, "rest response: ${body}")
                .marshal().json(JsonLibrary.Jackson)
                .log(LoggingLevel.DEBUG, "after marshaling rest response: ${body}")
                .convertBodyTo(String.class)
        ;
    }
}
