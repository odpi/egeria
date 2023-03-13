/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionProperties describes the core properties of a collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionProperties extends ReferenceableProperties
{
    private static final long    serialVersionUID = 1L;

    private String          name               = null;
    private String          description        = null;
    private CollectionOrder collectionOrdering = null;
    private String          orderPropertyName  = null;


    /**
     * Default constructor
     */
    public CollectionProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CollectionProperties(CollectionProperties template)
    {
        super(template);

        if (template != null)
        {
            this.name = template.getName();
            this.description = template.getDescription();
            this.collectionOrdering = template.getCollectionOrdering();
            this.orderPropertyName = template.getOrderPropertyName();
        }
    }


    /**
     * Return the name of the collection.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the collection.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the collection.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the collection.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the property to use to determine the order that assets are returned.
     *
     * @return CollectionOrder enum
     */
    public CollectionOrder getCollectionOrdering()
    {
        return collectionOrdering;
    }


    /**
     * Set up the property to use to determine the order that assets are returned.
     *
     * @param collectionOrdering CollectionOrder enum
     */
    public void setCollectionOrdering(CollectionOrder collectionOrdering)
    {
        this.collectionOrdering = collectionOrdering;
    }


    /**
     * Return the property name for CollectionOrder.OTHER.
     *
     * @return string property name
     */
    public String getOrderPropertyName()
    {
        return orderPropertyName;
    }


    /**
     * Set up the property name for CollectionOrder.OTHER.
     *
     * @param orderPropertyName string property name
     */
    public void setOrderPropertyName(String orderPropertyName)
    {
        this.orderPropertyName = orderPropertyName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CollectionProperties{" +
                       "name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", collectionOrdering=" + collectionOrdering +
                       ", orderPropertyName='" + orderPropertyName + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        CollectionProperties that = (CollectionProperties) objectToCompare;
        return getCollectionOrdering() == that.getCollectionOrdering() &&
                       Objects.equals(getName(), that.getName()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getOrderPropertyName(), that.getOrderPropertyName());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getName(), getDescription(), getCollectionOrdering(), getOrderPropertyName());
    }
}
