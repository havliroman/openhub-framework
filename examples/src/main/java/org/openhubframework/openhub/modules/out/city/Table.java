/*
 * Copyright (c) 2016 BSC Praha, spol. s r.o.
 */

package org.openhubframework.openhub.modules.out.city;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:roman.havlicek@bsc-ideas.com">Roman Havlicek</a>
 */
public class Table {

    private String country;
    private String city;

    public String getCountry() {
        return country;
    }

    @XmlElement(name = "Country")
    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    @XmlElement(name = "City")
    public void setCity(String city) {
        this.city = city;
    }
}
