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
 * EnumSchemaType describes a schema element that has a a fixed set of values.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EnumSchemaType extends SimpleSchemaType
{
    private static final long     serialVersionUID = 1L;

    private String    validValueSetGUID = null;

    /**
     * Default constructor used by subclasses
     */
    public EnumSchemaType()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public EnumSchemaType(EnumSchemaType template)
    {
        super(template);

        if (template != null)
        {
            validValueSetGUID = template.getValidValueSetGUID();
        }
    }


    /**
     * Return the unique identifier of the valid value set that lists the valid values of this enumeration.
     *
     * @return string guid
     */
    public String getValidValueSetGUID()
    {
        return validValueSetGUID;
    }


    /**
     * Set up the unique identifier of the valid value set that lists the valid values of this enumeration.
     *
     * @param validValueSetGUID string guid
     */
    public void setValidValueSetGUID(String validValueSetGUID)
    {
        this.validValueSetGUID = validValueSetGUID;
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return SchemaElement
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new EnumSchemaType(this);
    }


    /**
     * Returns a clone of this object as the abstract SchemaType class.
     *
     * @return PrimitiveSchemaType object
     */
    @Override
    public SchemaType cloneSchemaType()
    {
        return new EnumSchemaType(this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EnumSchemaType{" +
                "validValueSetGUID='" + validValueSetGUID + '\'' +
                ", dataType='" + getDataType() + '\'' +
                ", defaultValue='" + getDefaultValue() + '\'' +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", namespace='" + getNamespace() + '\'' +
                ", deprecated=" + getIsDeprecated() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", classifications=" + getClassifications() +
                ", extendedProperties=" + getExtendedProperties() +
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
        EnumSchemaType that = (EnumSchemaType) objectToCompare;
        return Objects.equals(getValidValueSetGUID(), that.getValidValueSetGUID());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getValidValueSetGUID());
    }
}