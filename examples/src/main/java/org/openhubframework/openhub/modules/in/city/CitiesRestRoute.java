/*
 * Copyright (c) 2016 BSC Praha, spol. s r.o.
 */

package org.openhubframework.openhub.modules.in.city;

import static org.openhubframework.openhub.modules.in.city.CitiesRestRoute.REST_ROUTE_BEAN;

import org.apache.camel.LoggingLevel;
import org.openhubframework.openhub.api.route.AbstractBasicRoute;
import org.openhubframework.openhub.api.route.CamelConfiguration;
import org.openhubframework.openhub.modules.ExampleProperties;
import org.openhubframework.openhub.modules.out.city.converter.CitiesConverter;

/**
 * Route definition for transferring cities between SOAP WS and REST endpoint.
 *
 * @author <a href="mailto:roman.havlicek@bsc-ideas.com">Roman Havlicek</a>
 */
@CamelConfiguration(value = REST_ROUTE_BEAN)
public class CitiesRestRoute extends AbstractBasicRoute {

    static final String REST_ROUTE_BEAN = "citiesRestRouteBean";

    @Override
    protected void doConfigure() throws Exception {

        // Handling general exceptions
        onException(Exception.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "General error occurred")
                .log(LoggingLevel.DEBUG, "${exception.stacktrace}")
                .transform().method(CitiesConverter.class, "processGeneralError")
        ;

        rest("/{country}")
                .get()
                .consumes("application/json")
                .produces("application/json")
                .to(ExampleProperties.CITIES_REST_DIRECT)
        ;
    }




}
