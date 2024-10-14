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
 * Description of a table.  These are the values that are returned from UC.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TableInfo extends TableProperties implements ElementBase
{
    private long   created_at = 0L;
    private String created_by = null;
    private long   updated_at = 0L;
    private String updated_by = null;
    private String table_id   = null;

    /**
     * Constructor
     */
    public TableInfo()
    {
    }


    /**
     * Return the time that the element was created.
     *
     * @return date/time as long
     */
    @Override
    public long getCreated_at()
    {
        return created_at;
    }


    /**
     * Set up the time that the element was created.
     *
     * @param created_at date/time as long
     */
    @Override
    public void setCreated_at(long created_at)
    {
        this.created_at = created_at;
    }


    /**
     * Return the time that the element was last updated.
     *
     * @return date/time as long
     */
    @Override
    public long getUpdated_at()
    {
        return updated_at;
    }


    /**
     * Set up the time that the element was last updated.
     *
     * @param updated_at  date/time as long
     */
    @Override
    public void setUpdated_at(long updated_at)
    {
        this.updated_at = updated_at;
    }


    /**
     * Return the userId that created the element.
     *
     * @return string name
     */
    @Override
    public String getCreated_by()
    {
        return created_by;
    }


    /**
     * Set up the userId that created the element.
     *
     * @param created_by string name
     */
    @Override
    public void setCreated_by(String created_by)
    {
        this.created_by = created_by;
    }


    /**
     * Return the element that last updated the element.
     *
     * @return string name
     */
    @Override
    public String getUpdated_by()
    {
        return updated_by;
    }


    /**
     * Set up the element that last updated the element.
     *
     * @param updated_by string name
     */
    @Override
    public void setUpdated_by(String updated_by)
    {
        this.updated_by = updated_by;
    }


    /**
     * Return the internal identifier of the table.
     *
     * @return string
     */
    public String getTable_id()
    {
        return table_id;
    }


    /**
     * Set up the internal identifier of the table.
     *
     * @param table_id string
     */
    public void setTable_id(String table_id)
    {
        this.table_id = table_id;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TableInfo{" +
                "created_at=" + created_at +
                ", created_by='" + created_by + '\'' +
                ", updated_at=" + updated_at +
                ", updated_by='" + updated_by + '\'' +
                ", table_id='" + table_id + '\'' +
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
        TableInfo that = (TableInfo) objectToCompare;
        return created_at == that.created_at &&
                Objects.equals(created_by, that.created_by) &&
                updated_at == that.updated_at &&
                Objects.equals(updated_by, that.updated_by) &&
                Objects.equals(table_id, that.table_id);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), created_at, created_by, updated_at, updated_by, table_id);
    }
}
