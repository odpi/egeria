/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The BoundedSchemaType object provides structural information for Arrays and Sets.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@Deprecated
public class BoundedSchemaType extends SchemaType
{
    private static final long     serialVersionUID = 1L;

    private   BoundedSchemaCategory boundedSchemaCategory = null;
    protected int                   maximumElements       = 0;
    protected SchemaType            elementType           = null;


    /**
     * Default constructor
     */
    @Deprecated
    public BoundedSchemaType()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template object to copy
     */
    @Deprecated
    public BoundedSchemaType(BoundedSchemaType template)
    {
        super(template);

        if (template != null)
        {
            boundedSchemaCategory = template.getBoundedSchemaCategory();
            maximumElements = template.getMaximumElements();
            elementType = template.getElementType();
        }
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return a copy of this schema as a SchemaElement
     */
    @Deprecated
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new BoundedSchemaType(this);
    }



    /**
     * Returns a clone of this object as the abstract SchemaType class.
     *
     * @return a copy of this schema as a SchemaType
     */
    @Deprecated
    @Override
    public SchemaType cloneSchemaType()
    {
        return new BoundedSchemaType(this);
    }


    /**
     * Return the type of the schema.
     *
     * @return BoundedSchemaCategory enum
     */
    @Deprecated
    public BoundedSchemaCategory getBoundedSchemaCategory() { return boundedSchemaCategory; }


    /**
     * Set up the type of schema.
     *
     * @param boundedSchemaCategory BoundedSchemaCategory enum
     */
    @Deprecated
    public void setBoundedSchemaCategory(BoundedSchemaCategory boundedSchemaCategory)
    {
        this.boundedSchemaCategory = boundedSchemaCategory;
    }


    /**
     * Return the maximum elements that can be stored in this schema.  This is set up by the caller.
     * Zero means not bounded.  For a STRUCT the max elements are the number of elements in
     * the structure and this value is ignored.
     *
     * @return int maximum number of elements
     */
    @Deprecated
    public int getMaximumElements()
    {
        return maximumElements;
    }


    /**
     * Set up the maximum elements that can be stored in this schema.  This is set up by the caller.
     * Zero means not bounded.  For a STRUCT the max elements are the number of elements in
     * the structure and this value is ignored.
     *
     * @param maximumElements int maximum number of elements
     */
    @Deprecated
    public void setMaximumElements(int maximumElements)
    {
        this.maximumElements = maximumElements;
    }


    /**
     * Return the type of the elements nested within this schema.
     *
     * @return schema type
     */
    @Deprecated
    public SchemaType getElementType()
    {
        return elementType;
    }


    /**
     * Set up the type of the elements nested within this schema.
     *
     * @param elementType schema type
     */
    @Deprecated
    public void setElementType(SchemaType elementType)
    {
        this.elementType = elementType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BoundedSchemaType{" +
                "boundedSchemaCategory=" + boundedSchemaCategory +
                ", maximumElements=" + maximumElements +
                ", elementType=" + elementType +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
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
        if (!(objectToCompare instanceof BoundedSchemaType))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        BoundedSchemaType schema = (BoundedSchemaType) objectToCompare;
        return getMaximumElements() == schema.getMaximumElements() &&
                getBoundedSchemaCategory() == schema.getBoundedSchemaCategory();
    }
}