/*
 * Copyright (c) 2016 BSC Praha, spol. s r.o.
 */

package org.openhubframework.openhub.modules.out.city;

import static org.openhubframework.openhub.api.common.jaxb.JaxbDataFormatHelper.jaxb;
import static org.openhubframework.openhub.modules.out.city.CitiesSoapWsRoute.SOAP_ROUTE_BEAN;

import java.net.NoRouteToHostException;
import java.net.UnknownHostException;

import org.apache.camel.LoggingLevel;
import org.openhubframework.openhub.api.route.AbstractBasicRoute;
import org.openhubframework.openhub.api.route.CamelConfiguration;
import org.openhubframework.openhub.modules.ExampleProperties;
import org.openhubframework.openhub.modules.out.cities.model.GetCitiesByCountry;
import org.openhubframework.openhub.modules.out.cities.model.GetCitiesByCountryResponse;
import org.openhubframework.openhub.modules.out.city.converter.CitiesConverter;

/**
 * Route definition for fetching and converting list of cities from SOAP web service.
 *
 * @author <a href="mailto:roman.havlicek@bsc-ideas.com">Roman Havlicek</a>
 */
@CamelConfiguration(value = SOAP_ROUTE_BEAN)
public class CitiesSoapWsRoute extends AbstractBasicRoute {

    static final String SOAP_ROUTE_BEAN = "citiesSoapWsRouteBean";

    @Override
    protected void doConfigure() throws Exception {

        // Handling connection related exceptions
        onException(UnknownHostException.class, NoRouteToHostException.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "SOAP webservice unreachable, probably due to connection loss")
                .log(LoggingLevel.DEBUG, "${exception.stacktrace}")
                .transform().method(CitiesConverter.class, "processNoConnectionError")
        ;

        // Handling general exceptions
        onException(Exception.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "General error occurred")
                .log(LoggingLevel.DEBUG, "${exception.stacktrace}")
                .transform().method(CitiesConverter.class, "processGeneralError")
        ;


        from(ExampleProperties.CITIES_REST_DIRECT)
                .log(LoggingLevel.INFO, "received request for country ${header.country} and to save into folder ${header.fileDirectory}")
                // transforming to GetCitiesByCountry request for SOAP webservice
                .transform().method(CitiesConverter.class, "convertToGetCitiesByCountry")
                .marshal(jaxb(GetCitiesByCountry.class))
                .log(LoggingLevel.DEBUG, "SOAP request: ${body}")
                // calling webservice
                .to("spring-ws:http://www.webservicex.com/globalweather.asmx?soapAction=http://www.webserviceX.NET/GetCitiesByCountry")
                .log(LoggingLevel.DEBUG, "SOAP response: ${body}")
                .unmarshal(jaxb(GetCitiesByCountryResponse.class))
                // transforming SOAP webservice response into DTO object with list of one-word cities
                .transform().method(CitiesConverter.class, "convertToCitiesDto")
                .log(LoggingLevel.INFO, "found cities: ${body}")
                .to(ExampleProperties.CITIES_PROCESSING_DIRECT)

        ;
    }
}
