/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.collections;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.OrderBy;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionFolderProperties defines the properties used to create a Folder classification for a collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionFolderProperties extends CollectionProperties
{
    private String  orderByPropertyName = null;
    private OrderBy collectionOrder     = null;

    /**
     * Default Constructor
     */
    public CollectionFolderProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public CollectionFolderProperties(CollectionFolderProperties template)
    {
        super(template);

        if (template != null)
        {
            this.orderByPropertyName = template.getOrderByPropertyName();
            this.collectionOrder     = template.getCollectionOrder();
        }
    }


    /**
     * Return the property to use to determine the order that member are returned.
     *
     * @return AssetCollectionOrder enum
     */
    public OrderBy getCollectionOrder()
    {
        return collectionOrder;
    }


    /**
     * Set up the property to use to determine the order that assets are returned.
     *
     * @param collectionOrder AssetCollectionOrder enum
     */
    public void setCollectionOrder(OrderBy collectionOrder)
    {
        this.collectionOrder = collectionOrder;
    }


    /**
     * Return the name of the property to use if collectionOrdering is OTHER.
     *
     * @return property name
     */
    public String getOrderByPropertyName()
    {
        return orderByPropertyName;
    }


    /**
     * Set up the name of the property to use if collectionOrdering is OTHER.
     *
     * @param orderByPropertyName test
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
        return "CollectionFolderProperties{" +
                       "collectionOrderingProperty='" + orderByPropertyName + '\'' +
                       ", collectionOrdering=" + collectionOrder +
                       ", name='" + getName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", collectionType='" + getCollectionType() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        CollectionFolderProperties that = (CollectionFolderProperties) objectToCompare;
        return Objects.equals(orderByPropertyName, that.orderByPropertyName) && collectionOrder == that.collectionOrder;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), orderByPropertyName, collectionOrder);
    }
}
