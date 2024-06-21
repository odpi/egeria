/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Describes a table that is dependent on an SQL object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TableDependency
{
    private String table_full_name = null;


    /**
     * Constructor
     */
    public TableDependency()
    {
    }


    /**
     * Return the full name of the dependent table, in the form of __catalog_name__.__schema_name__.__table_name__.
     *
     * @return tableFullName
     */
    public String getTable_full_name()
    {
        return table_full_name;
    }


    /**
     * Set up the full name of the dependent table, in the form of __catalog_name__.__schema_name__.__table_name__.
     *
     * @param table_full_name tableFullName
     */
    public void setTable_full_name(String table_full_name)
    {
        this.table_full_name = table_full_name;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TableDependency{" +
                "table_full_name='" + table_full_name + '\'' +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        TableDependency that = (TableDependency) objectToCompare;
        return Objects.equals(table_full_name, that.table_full_name);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(table_full_name);
    }
}
