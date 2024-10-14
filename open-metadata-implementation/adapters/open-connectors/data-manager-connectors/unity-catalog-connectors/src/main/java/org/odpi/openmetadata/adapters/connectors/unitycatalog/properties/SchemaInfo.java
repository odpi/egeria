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
 * Description of a schema.  These are the values that are returned from UC.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaInfo extends SchemaProperties implements ElementBase
{
    private long   created_at = 0L;
    private String created_by = null;
    private long   updated_at = 0L;
    private String updated_by = null;
    private String schema_id  = null;
    private String full_name  = null;

    /**
     * Constructor
     */
    public SchemaInfo()
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
     * Return the internal identifier of the schema.
     *
     * @return string
     */
    public String getSchema_id()
    {
        return schema_id;
    }


    /**
     * Set up the internal identifier of the schema.
     *
     * @param schema_id string
     */
    public void setSchema_id(String schema_id)
    {
        this.schema_id = schema_id;
    }


    /**
     * Return the fully-qualified name of the schema.
     *
     * @return name
     */
    public String getFull_name()
    {
        return full_name;
    }


    /**
     * Set upi the fully-qualified name of the schema.
     *
     * @param full_name name
     */
    public void setFull_name(String full_name)
    {
        this.full_name = full_name;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaInfo{" +
                "created_at=" + created_at +
                ", created_by='" + created_by + '\'' +
                ", updated_at=" + updated_at +
                ", updated_by='" + updated_by + '\'' +
                ", schema_id='" + schema_id + '\'' +
                ", full_name='" + full_name + '\'' +
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
        SchemaInfo that = (SchemaInfo) objectToCompare;
        return created_at == that.created_at &&
                Objects.equals(created_by, that.created_by) &&
                updated_at == that.updated_at &&
                Objects.equals(updated_by, that.updated_by) &&
                Objects.equals(schema_id, that.schema_id) &&
                Objects.equals(full_name, that.full_name);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), created_at, created_by, updated_at, updated_by, schema_id, full_name);
    }
}
