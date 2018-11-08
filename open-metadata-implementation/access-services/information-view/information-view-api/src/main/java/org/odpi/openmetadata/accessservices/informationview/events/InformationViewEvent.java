/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
public class InformationViewEvent extends InformationViewBean{

    private TableContext tableContext = new TableContext();
    private ConnectionDetails connectionDetails;
    private List<DerivedColumnDetail> derivedColumns = new ArrayList<>();


    /**
     * Return the connection details
     *
     * @return properties of the connection
     */
    public ConnectionDetails getConnectionDetails() {
        return connectionDetails;
    }

    /**
     * Set up the connection details
     *
     * @param connectionDetails - properties of the connection
     */
    public void setConnectionDetails(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    /**
     * Return list of derived columns
     *
     * @return the list of the properties for each derived column
     */
    public List<DerivedColumnDetail> getDerivedColumns() {
        return derivedColumns;
    }

    /**
     * Set up the list of derived columns
     *
     * @param derivedColumns - list of properties for each derived columns
     */
    public void setDerivedColumns(List<DerivedColumnDetail> derivedColumns) {
        this.derivedColumns = derivedColumns;
    }

    public TableContext getTableContext() {
        return tableContext;
    }

    @Override
    public String toString() {
        return "InformationViewEvent{" +
                "tableContext=" + tableContext +
                ", connectionDetails=" + connectionDetails +
                ", derivedColumns=" + derivedColumns +
                '}';
    }
}
