/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ComplexSchemaTypeProperties describes a schema with multiple attributes.  Notice it does not contain the attributes,
 * just a count of them.  This is because a complex schema type may have literally thousands of attributes
 * and so the attribute contents are retrieved separated through calls that support paging.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
                      @JsonSubTypes.Type(value = DocumentSchemaAttributeProperties.class, name = "DocumentSchemaAttributeProperties"),
                      @JsonSubTypes.Type(value = StructSchemaTypeProperties.class, name = "StructSchemaTypeProperties"),
                      @JsonSubTypes.Type(value = TabularSchemaTypeProperties.class, name = "TabularSchemaTypeProperties"),
})
public class ComplexSchemaTypeProperties extends SchemaTypeProperties
{
    private static final long     serialVersionUID = 1L;

    private int    attributeCount = 0;


    /**
     * Default constructor used by subclasses
     */
    public ComplexSchemaTypeProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public ComplexSchemaTypeProperties(ComplexSchemaTypeProperties template)
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ComplexSchemaType{" +
                "attributeCount='" + attributeCount + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
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
        ComplexSchemaTypeProperties that = (ComplexSchemaTypeProperties) objectToCompare;
        return attributeCount == that.attributeCount;
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), attributeCount);
    }
}