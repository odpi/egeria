/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Description of a table. Maps to CreateTable
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TableProperties extends StoredDataProperties
{
    private TableType        table_type         = null;
    private DataSourceFormat data_source_format = null;
    private List<ColumnInfo> columns            = null;

    /**
     * Constructor
     */
    public TableProperties()
    {
    }

    public TableType getTable_type()
    {
        return table_type;
    }

    public void setTable_type(TableType table_type)
    {
        this.table_type = table_type;
    }

    public DataSourceFormat getData_source_format()
    {
        return data_source_format;
    }

    public void setData_source_format(DataSourceFormat data_source_format)
    {
        this.data_source_format = data_source_format;
    }

    public List<ColumnInfo> getColumns()
    {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns)
    {
        this.columns = columns;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TableProperties{" +
                "table_type=" + table_type +
                ", data_source_format=" + data_source_format +
                ", columns=" + columns +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare)) return false;
        TableProperties that = (TableProperties) objectToCompare;
        return table_type == that.table_type && data_source_format == that.data_source_format && Objects.equals(columns, that.columns);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), table_type, data_source_format, columns);
    }
}
