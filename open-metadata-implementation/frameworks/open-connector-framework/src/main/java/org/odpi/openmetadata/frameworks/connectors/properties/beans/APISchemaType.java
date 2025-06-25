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
 * APISchemaType describes the header of an API Schema.  The operations may be numerous and so they
 * are passed in a separate iterator.  The operation count helps to set up the iterator.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class APISchemaType extends SchemaType
{
    protected  int operationCount = 0;

    /**
     * Default constructor used by subclasses
     */
    public APISchemaType()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public APISchemaType(APISchemaType template)
    {
        super(template);

        if (template != null)
        {
            operationCount = template.getOperationCount();
        }
    }


    /**
     * Return the count of attributes in this schema type.
     *
     * @return String data type name
     */
    public int getOperationCount() { return operationCount; }


    /**
     * Set up the count of attributes in this schema type
     *
     * @param operationCount data type name
     */
    public void setOperationCount(int operationCount)
    {
        this.operationCount = operationCount;
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return PrimitiveSchemaType object
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new APISchemaType(this);
    }


    /**
     * Returns a clone of this object as the abstract SchemaType class.
     *
     * @return PrimitiveSchemaType object
     */
    @Override
    public SchemaType cloneSchemaType()
    {
        return new APISchemaType(this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "APISchemaType{" +
                "operationCount='" + operationCount + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", versionNumber='" + getVersionIdentifier() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", classifications=" + getClassifications() +
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
        APISchemaType that = (APISchemaType) objectToCompare;
        return getOperationCount() == that.getOperationCount();
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getOperationCount());
    }
}