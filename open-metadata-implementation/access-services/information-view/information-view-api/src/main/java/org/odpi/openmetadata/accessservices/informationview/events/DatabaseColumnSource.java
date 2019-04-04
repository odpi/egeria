/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabaseColumnSource extends Source {

    private String name;

    private TableSource tableSource = new TableSource();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TableSource getTableSource() {
        return tableSource;
    }

    public void setTableSource(TableSource tableSource) {
        this.tableSource = tableSource;
    }


    @Override
    public Map<String, String> getAdditionalProperties() {
        return tableSource != null ? tableSource.getAdditionalProperties() : super.getAdditionalProperties();
    }


    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", tableSource=" + tableSource +
                '}';
    }

}
