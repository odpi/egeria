/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.OrderBy;

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
    private String  name                = null;
    private String  description         = null;
    private String  collectionType      = null;
    private OrderBy collectionOrder     = null;
    private String  orderByPropertyName = null;


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
            this.collectionType    = template.getCollectionType();
            this.collectionOrder     = template.getCollectionOrder();
            this.orderByPropertyName = template.getOrderByPropertyName();
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
     * Return a descriptive name for the collection's type.
     *
     * @return string name
     */
    public String getCollectionType()
    {
        return collectionType;
    }


    /**
     * Set up a descriptive name for the collection's type.
     *
     * @param collectionType string name
     */
    public void setCollectionType(String collectionType)
    {
        this.collectionType = collectionType;
    }


    /**
     * Return the property to use to determine the order that assets are returned.
     *
     * @return OrderBy enum
     */
    public OrderBy getCollectionOrder()
    {
        return collectionOrder;
    }


    /**
     * Set up the property to use to determine the order that assets are returned.
     *
     * @param collectionOrder OrderBy enum
     */
    public void setCollectionOrder(OrderBy collectionOrder)
    {
        this.collectionOrder = collectionOrder;
    }


    /**
     * Return the property name for OrderBy.OTHER.
     *
     * @return string property name
     */
    public String getOrderByPropertyName()
    {
        return orderByPropertyName;
    }


    /**
     * Set up the property name for OrderBy.OTHER.
     *
     * @param orderByPropertyName string property name
     */
    public void setOrderByPropertyName(String orderByPropertyName)
    {
        this.orderByPropertyName = orderByPropertyName;
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
                       ", collectionType='" + collectionType + '\'' +
                       ", collectionOrdering=" + collectionOrder +
                       ", orderPropertyName='" + orderByPropertyName + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
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
        return getCollectionOrder() == that.getCollectionOrder() &&
                       Objects.equals(getName(), that.getName()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getCollectionType(), that.getCollectionType()) &&
                       Objects.equals(getOrderByPropertyName(), that.getOrderByPropertyName());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getName(), getDescription(), getCollectionType(), getCollectionOrder(), getOrderByPropertyName());
    }
}
