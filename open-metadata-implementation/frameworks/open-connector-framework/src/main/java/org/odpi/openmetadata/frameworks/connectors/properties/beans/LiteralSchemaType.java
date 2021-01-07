/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LiteralSchemaType describes a schema element that has a fixed value type.  This class stores the value it represents.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LiteralSchemaType extends SchemaType
{
    private static final long     serialVersionUID = 1L;

    protected  String dataType   = null;
    protected  String fixedValue = null;

    /**
     * Default constructor used by subclasses
     */
    public LiteralSchemaType()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public LiteralSchemaType(LiteralSchemaType template)
    {
        super(template);

        if (template != null)
        {
            dataType   = template.getDataType();
            fixedValue = template.getFixedValue();
        }
    }



    /**
     * Return the data type for this element.  Null means unknown data type.
     *
     * @return String data type name
     */
    public String getDataType() { return dataType; }


    /**
     * Set up the data type for this element.  Null means unknown data type.
     *
     * @param dataType data type name
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return the fixed value for the element.  Null means fixed value is null.
     *
     * @return String containing fixed value
     */
    public String getFixedValue() { return fixedValue; }


    /**
     * Set up the fixed value for the element.  Null means fixed value is null.
     *
     * @param fixedValue String containing fixed value
     */
    public void setFixedValue(String fixedValue)
    {
        this.fixedValue = fixedValue;
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return SchemaElement
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new LiteralSchemaType(this);
    }



    /**
     * Returns a clone of this object as the abstract SchemaType class.
     *
     * @return LiteralSchemaType object
     */
    @Override
    public SchemaType cloneSchemaType()
    {
        return new LiteralSchemaType(this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LiteralSchemaType{" +
                "dataType='" + dataType + '\'' +
                ", fixedValue='" + fixedValue + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", author='" + author + '\'' +
                ", usage='" + usage + '\'' +
                ", encodingStandard='" + encodingStandard + '\'' +
                ", namespace='" + namespace + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", meanings=" + meanings +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
                ", extendedProperties=" + extendedProperties +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        LiteralSchemaType that = (LiteralSchemaType) objectToCompare;
        return Objects.equals(getDataType(), that.getDataType()) &&
                       Objects.equals(getFixedValue(), that.getFixedValue());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDataType(), getFixedValue());
    }
}