/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ComplexSchemaType describes a schema with multiple attributes.  Notice it does not contain the attributes,
 * just a count of them.  This is because a complex schema type may have literally thousands of attributes
 * and so the attribute contents are retrieved separated through calls that support paging.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ComplexSchemaType extends SchemaType
{
    protected int    attributeCount = 0;


    /**
     * Default constructor used by subclasses
     */
    public ComplexSchemaType()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public ComplexSchemaType(ComplexSchemaType template)
    {
        super(template);

        if (template != null)
        {
            attributeCount = template.getAttributeCount();
        }
    }


    /**
     * Return the count of attributes in this schema type.
     *
     * @return String data type name
     */
    public int getAttributeCount() { return attributeCount; }


    /**
     * Set up the count of attributes in this schema type
     *
     * @param attributeCount data type name
     */
    public void setAttributeCount(int attributeCount)
    {
        this.attributeCount = attributeCount;
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return ComplexSchemaType object
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new ComplexSchemaType(this);
    }


    /**
     * Returns a clone of this object as the abstract SchemaType class.
     *
     * @return ComplexSchemaType object
     */
    @Override
    public SchemaType cloneSchemaType()
    {
        return new ComplexSchemaType(this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ComplexSchemaType{" +
                "attributeCount=" + attributeCount +
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
        ComplexSchemaType that = (ComplexSchemaType) objectToCompare;
        return getAttributeCount() == that.getAttributeCount();
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getAttributeCount());
    }
}