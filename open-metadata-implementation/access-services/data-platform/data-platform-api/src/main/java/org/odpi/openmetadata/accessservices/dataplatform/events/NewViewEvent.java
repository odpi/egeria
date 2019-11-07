/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DerivedColumn;
import org.odpi.openmetadata.accessservices.dataplatform.properties.TableSource;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The NewViewEvent will create a new View from a data platform as an external source.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class")
public class NewViewEvent extends DataPlatformEventHeader {

    private TableSource tableSource;
    private TableSource originalTableSource;
    private List<DerivedColumn> derivedColumns = new ArrayList<>();


    /**
     * Gets table source.
     *
     * @return the table source
     */
    public TableSource getTableSource() {
        return tableSource;
    }

    /**
     * Sets table source.
     *
     * @param tableSource the table source
     */
    public void setTableSource(TableSource tableSource) {
        this.tableSource = tableSource;
    }

    /**
     * Gets derived columns.
     *
     * @return the derived columns
     */
    public List<DerivedColumn> getDerivedColumns() {
        return derivedColumns;
    }

    /**
     * Sets derived columns.
     *
     * @param derivedColumns the derived columns
     */
    public void setDerivedColumns(List<DerivedColumn> derivedColumns) {
        this.derivedColumns = derivedColumns;
    }

    /**
     * Gets original table source.
     *
     * @return the original table source
     */
    public TableSource getOriginalTableSource() {
        return originalTableSource;
    }

    /**
     * Sets original table source.
     *
     * @param originalTableSource the original table source
     */
    public void setOriginalTableSource(TableSource originalTableSource) {
        this.originalTableSource = originalTableSource;
    }

    @Override
    public String toString() {
        return "NewViewEvent{" +
                "tableSource=" + tableSource +
                ", originalTableSource=" + originalTableSource +
                ", derivedColumns=" + derivedColumns +
                '}';
    }
}