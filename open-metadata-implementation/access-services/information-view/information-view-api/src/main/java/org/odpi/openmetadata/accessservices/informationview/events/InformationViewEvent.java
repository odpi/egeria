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
public class InformationViewEvent extends InformationViewHeader {

    private TableSource tableSource;
    private TableSource originalTableSource;
    private List<DerivedColumn> derivedColumns = new ArrayList<>();


    public TableSource getTableSource() {
        return tableSource;
    }

    public void setTableSource(TableSource tableSource) {
        this.tableSource = tableSource;
    }

    /**
     * Return list of derived columns
     *
     * @return the list of the properties for each derived column
     */
    public List<DerivedColumn> getDerivedColumns() {
        return derivedColumns;
    }

    /**
     * Set up the list of derived columns
     *
     * @param derivedColumns - list of properties for each derived columns
     */
    public void setDerivedColumns(List<DerivedColumn> derivedColumns) {
        this.derivedColumns = derivedColumns;
    }

    public TableSource getOriginalTableSource() {
        return originalTableSource;
    }

    public void setOriginalTableSource(TableSource originalTableSource) {
        this.originalTableSource = originalTableSource;
    }

    @Override
    public String toString() {
        return "InformationViewEvent{" +
                "tableSource=" + tableSource +
                ", originalTableSource=" + originalTableSource +
                ", derivedColumns=" + derivedColumns +
                '}';
    }
}
