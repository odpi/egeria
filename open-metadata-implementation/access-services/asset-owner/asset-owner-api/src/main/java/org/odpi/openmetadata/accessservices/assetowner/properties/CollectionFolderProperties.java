/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
    private String              collectionOrderingProperty = null;
    private CollectionOrder     collectionOrdering         = null;

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
            this.collectionOrderingProperty = template.getCollectionOrderingProperty();
            this.collectionOrdering         = template.getCollectionOrdering();
        }
    }


    /**
     * Return the property to use to determine the order that member are returned.
     *
     * @return AssetCollectionOrder enum
     */
    public CollectionOrder getCollectionOrdering()
    {
        return collectionOrdering;
    }


    /**
     * Set up the property to use to determine the order that assets are returned.
     *
     * @param collectionOrdering AssetCollectionOrder enum
     */
    public void setCollectionOrdering(CollectionOrder collectionOrdering)
    {
        this.collectionOrdering = collectionOrdering;
    }


    /**
     * Return the name of the property to use if collectionOrdering is OTHER.
     *
     * @return property name
     */
    public String getCollectionOrderingProperty()
    {
        return collectionOrderingProperty;
    }


    /**
     * Set up the name of the property to use if collectionOrdering is OTHER.
     *
     * @param collectionOrderingProperty test
     */
    public void setCollectionOrderingProperty(String collectionOrderingProperty)
    {
        this.collectionOrderingProperty = collectionOrderingProperty;
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
                "collectionOrderingProperty='" + collectionOrderingProperty + '\'' +
                ", collectionOrdering=" + collectionOrdering +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
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
        return Objects.equals(collectionOrderingProperty, that.collectionOrderingProperty) && collectionOrdering == that.collectionOrdering;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), collectionOrderingProperty, collectionOrdering);
    }
}
