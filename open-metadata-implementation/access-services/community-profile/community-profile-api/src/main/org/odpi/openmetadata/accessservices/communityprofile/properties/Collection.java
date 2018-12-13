/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Collection defines a collection of assets that an individual is working with.  The asset collections
 * are linked off of the individual's profile.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Collection extends ReferenceableHeader
{
    private String          displayName        = null;
    private String          description        = null;
    private String          collectionUse      = null;
    private CollectionOrder collectionOrdering = null;

    /**
     * Default Constructor
     */
    public Collection()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public Collection(Collection template)
    {
        super(template);

        if (template != null)
        {
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
            this.collectionUse = template.getCollectionUse();
            this.collectionOrdering = template.getCollectionOrdering();
        }
    }


    /**
     * Return the display name for this asset (normally a shortened form of the qualified name).
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for this asset (normally a shortened form of the qualified name).
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description for this asset.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description for this asset.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the description of how the collection is used by the actor.
     *
     * @return text
     */
    public String getCollectionUse()
    {
        return collectionUse;
    }


    /**
     * Set up the description of how the collection is used by the actor.
     *
     * @param collectionUse test
     */
    public void setCollectionUse(String collectionUse)
    {
        this.collectionUse = collectionUse;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "Collection{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", collectionUse='" + collectionUse + '\'' +
                ", collectionOrdering=" + collectionOrdering +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", classifications=" + getClassifications() +
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
        Collection that = (Collection) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getCollectionUse(), that.getCollectionUse()) &&
                getCollectionOrdering() == that.getCollectionOrdering();
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getCollectionUse(),
                            getCollectionOrdering());
    }
}
