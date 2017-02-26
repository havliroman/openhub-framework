/*
 * Copyright (c) 2016 BSC Praha, spol. s r.o.
 */

package org.openhubframework.openhub.modules.out.city.converter;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.JAXB;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.openhubframework.openhub.modules.in.city.CitiesDto;
import org.openhubframework.openhub.modules.in.city.ErrorDto;
import org.openhubframework.openhub.modules.out.cities.model.GetCitiesByCountry;
import org.openhubframework.openhub.modules.out.cities.model.GetCitiesByCountryResponse;
import org.openhubframework.openhub.modules.out.city.NewDataSet;
import org.openhubframework.openhub.modules.out.city.Table;
import org.springframework.stereotype.Component;

/**
 * Converter for SOAP webservice request/response.
 *
 * @author <a href="mailto:roman.havlicek@bsc-ideas.com">Roman Havlicek</a>
 */
@Component
public final class CitiesConverter {

    /**
     * Regex pattern for one-word unicode text.
     */
    private static final Pattern ONE_WORD_TEXT = Pattern.compile("^\\p{L}+$");

    private CitiesConverter() {
        // cannot instantiate
    }

    /**
     * Creates SOAP webservice request {@link GetCitiesByCountry} with {@code countryName} as a single parameter.
     *
     * @param countryName country for which the cities will be looked up
     * @return            {@link GetCitiesByCountry} request for the SOAP webservice
     */
    public static GetCitiesByCountry convertToGetCitiesByCountry(@Header("country") final String countryName) {
        GetCitiesByCountry request = new GetCitiesByCountry();
        request.setCountryName(countryName);
        return request;
    }

    /**
     * Converts response {@link GetCitiesByCountryResponse} from SOAP webservice into {@link CitiesDto} containing
     * list of single-word cities.
     *
     * @param response {@link GetCitiesByCountryResponse} response from SOAP webservice
     * @return  {@link CitiesDto} with list of one-word cities
     */
    public static CitiesDto convertToCitiesDto(@Body final GetCitiesByCountryResponse response) {

        // converting string with cities into POJO
        final String citiesInXml = response.getGetCitiesByCountryResult();
        final NewDataSet newDataSet = JAXB.unmarshal(new StringReader(citiesInXml), NewDataSet.class);

        // add one-word cities into result list
        List<String> cities = new ArrayList<>();
        for (Table table : newDataSet.getTables()) {
            if (isOneWordCity(table.getCity())) {
                cities.add(table.getCity());
            }
        }

        return new CitiesDto(cities);
    }

    /**
     * Converts list of cities in {@link CitiesDto} into String of cities separated by comma.
     *
     * @param citiesDto {@link CitiesDto} with list of cities to be converted
     * @return  {@link String} with cities separated by comma
     */
    public static String convertCitiesDtoForFile(@Body final CitiesDto citiesDto) {
        StringBuilder sb = new StringBuilder();
        boolean empty = true;
        for (String city : citiesDto.getCities()) {
            if (empty) {
                sb.append(city);
                empty = false;
            } else {
                sb.append(", ");
                sb.append(city);
            }
        }

        return sb.toString();
    }

    public static ErrorDto processNoConnectionError(@Body final Exception exception) {
        return createErrorDto("Connection error. Please check your connection.");
    }

    public static ErrorDto processGeneralError(@Body final Exception exception) {
        return createErrorDto("Something bad has happened. Sorry for the inconvenience. Don't do drugs!");
    }

    private static ErrorDto createErrorDto(final String message) {
        final ErrorDto result = new ErrorDto();
        result.setErrorMessage(message);
        return result;
    }


    /**
     * Checks if given {@code city} is one-word only.
     *
     * @param city name of the city to check
     * @return     {@code true} if city name is a one-word only, {@code false} otherwise
     */
    private static boolean isOneWordCity(final String city) {
        return ONE_WORD_TEXT.matcher(city).matches();
    }
}
