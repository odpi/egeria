/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.rest;

import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * AssetCollectionRequestBody defines the properties used to create an asset connector anchor object.
 * AssetCollectionMember collections are linked off of the individual's profile.
 */
public class AssetCollectionRequestBody extends CommunityProfileOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String              qualifiedName        = null;
    private String              displayName          = null;
    private String              description          = null;
    private String              collectionUse        = null;
    private CollectionOrder     collectionOrdering   = null;
    private Map<String, Object> additionalProperties = null;

    /**
     * Default Constructor
     */
    public AssetCollectionRequestBody()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public AssetCollectionRequestBody(AssetCollectionRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.qualifiedName = template.getQualifiedName();
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
            this.collectionUse = template.getCollectionUse();
            this.collectionOrdering = template.getCollectionOrdering();
            this.additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the unique name for this asset.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the unique name for this asset.
     *
     * @param qualifiedName string name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Return the display name for this asset (normally a shortened for of the qualified name).
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for this asset (normally a shortened for of the qualified name).
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
     * Return any additional properties associated with the asset.
     *
     * @return map of property names to property values
     */
    public Map<String, Object> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up any additional properties associated with the asset.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetCollectionRequestBody{" +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", collectionUse='" + collectionUse + '\'' +
                ", collectionOrdering=" + collectionOrdering +
                ", additionalProperties=" + additionalProperties +
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
        AssetCollectionRequestBody that = (AssetCollectionRequestBody) objectToCompare;
        return  Objects.equals(getQualifiedName(), that.getQualifiedName()) &&
                Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getCollectionUse(), that.getCollectionUse()) &&
                getCollectionOrdering() == that.getCollectionOrdering() &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getQualifiedName(), getDisplayName(), getDescription(),
                            getCollectionUse(),
                            getCollectionOrdering(), getAdditionalProperties());
    }
}
