/*
 * Copyright (c) 2016 BSC Praha, spol. s r.o.
 */

package org.openhubframework.openhub.modules.out.city;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:roman.havlicek@bsc-ideas.com">Roman Havlicek</a>
 */
@XmlRootElement(name = "NewDataSet")
public class NewDataSet {

    private List<Table> tables;

    public List<Table> getTables() {
        return tables;
    }

    @XmlElement(name = "Table")
    public void setTables(List<Table> tables) {
        this.tables = tables;
    }
}
