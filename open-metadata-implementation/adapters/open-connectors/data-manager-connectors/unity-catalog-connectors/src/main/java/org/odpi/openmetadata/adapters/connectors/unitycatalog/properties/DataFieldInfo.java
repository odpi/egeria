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
 * Common properties of a data field.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataFieldInfo
{
    private String name               = null;
    private String comment            = null;
    private String type_text          = null;
    private String type_json          = null;
    private String type_name          = null;
    private int    type_precision     = 0;
    private int    type_scale         = 0;
    private String type_interval_type = null;
    private int    position           = 0;



    /**
     * Constructor
     */
    public DataFieldInfo()
    {
    }

    /**
     * Return the unique name of the element within its name space.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the unique name of the element within its name space.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return a comment describing the element within its name space.
     *
     * @return text
     */
    public String getComment()
    {
        return comment;
    }


    /**
     * Set up a comment describing the element within its name space.
     *
     * @param comment text
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }


    /**
     * Return the full data type specification as SQL/catalogString text.
     *
     * @return string
     */
    public String getType_text()
    {
        return type_text;
    }


    /**
     * Set up the full data type specification as SQL/catalogString text.
     *
     * @param type_text string
     */
    public void setType_text(String type_text)
    {
        this.type_text = type_text;
    }


    /**
     * Return the full data type specification, JSON-serialized.
     *
     * @return string
     */
    public String getType_json()
    {
        return type_json;
    }


    /**
     * Set up the Full data type specification, JSON-serialized.
     *
     * @param type_json string
     */
    public void setType_json(String type_json)
    {
        this.type_json = type_json;
    }


    /**
     * Return the column type name enum.
     *
     * @return enum
     */
    public String getType_name()
    {
        return type_name;
    }


    /**
     * Set up the column type name enum.
     *
     * @param type_name enum
     */
    public void setType_name(String type_name)
    {
        this.type_name = type_name;
    }


    /**
     * Return the digits of precision; required for DecimalTypes.
     *
     * @return int
     */
    public int getType_precision()
    {
        return type_precision;
    }


    /**
     * Set up the digits of precision; required for DecimalTypes.
     *
     * @param type_precision int
     */
    public void setType_precision(int type_precision)
    {
        this.type_precision = type_precision;
    }


    /**
     * Return the digits to right of decimal; Required for DecimalTypes.
     *
     * @return int
     */
    public int getType_scale()
    {
        return type_scale;
    }


    /**
     * Set up the digits to right of decimal; Required for DecimalTypes.
     *
     * @param type_scale int
     */
    public void setType_scale(int type_scale)
    {
        this.type_scale = type_scale;
    }


    /**
     * Return the format of interval type.
     *
     * @return string
     */
    public String getType_interval_type()
    {
        return type_interval_type;
    }


    /**
     * Set up the format of interval type.
     *
     * @param type_interval_type string
     */
    public void setType_interval_type(String type_interval_type)
    {
        this.type_interval_type = type_interval_type;
    }


    /**
     * Return the ordinal position of data field (starting at position 0).
     *
     * @return int
     */
    public int getPosition()
    {
        return position;
    }


    /**
     * Set up the ordinal position of column (starting at position 0).
     *
     * @param position int
     */
    public void setPosition(int position)
    {
        this.position = position;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataFieldInfo{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", type_text='" + type_text + '\'' +
                ", type_json='" + type_json + '\'' +
                ", type_name=" + type_name +
                ", typePrecision=" + type_precision +
                ", type_scale=" + type_scale +
                ", type_interval_type='" + type_interval_type + '\'' +
                ", position=" + position +
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
        DataFieldInfo that = (DataFieldInfo) objectToCompare;
        return type_precision == that.type_precision && type_scale == that.type_scale && position == that.position && Objects.equals(name, that.name) && Objects.equals(comment, that.comment) && Objects.equals(type_text, that.type_text) && Objects.equals(type_json, that.type_json) && type_name == that.type_name && Objects.equals(type_interval_type, that.type_interval_type);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, comment, type_text, type_json, type_name, type_precision, type_scale, type_interval_type, position);
    }
}
