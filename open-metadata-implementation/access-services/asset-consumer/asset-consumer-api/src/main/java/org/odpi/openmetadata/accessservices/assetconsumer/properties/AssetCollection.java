/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.properties;


import java.util.*;

/**
 * AssetCollection defines a collection of assets that an individual is working with.  The asset collections
 * are linked off of the individual's profile.
 */
public class AssetCollection extends AssetConsumerElementHeader
{
    private String               guid                 = null;
    private String               typeName             = null;
    private String               qualifiedName        = null;
    private String               displayName          = null;
    private String               description          = null;
    private String               collectionUse        = null;
    private AssetCollectionOrder collectionOrdering   = null;
    private Map<String, Object>  additionalProperties = null;
    private List<Classification> classifications      = null;

    /**
     * Default Constructor
     */
    public AssetCollection()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public AssetCollection(AssetCollection template)
    {
        super(template);

        if (template != null)
        {
            this.guid = template.getGUID();
            this.typeName = template.getTypeName();
            this.qualifiedName = template.getQualifiedName();
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
            this.collectionUse = template.getCollectionUse();
            this.collectionOrdering = template.getCollectionOrdering();
            this.additionalProperties = template.getAdditionalProperties();
            this.classifications = template.getClassifications();
        }
    }


    /**
     * Return the unique identifier for this asset.
     *
     * @return string guid
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique identifier for this asset.
     *
     * @param guid string guid for this asset
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the name for this Asset's typeName.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the name for this Asset's typeName.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
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
    public AssetCollectionOrder getCollectionOrdering()
    {
        return collectionOrdering;
    }


    /**
     * Set up the property to use to determine the order that assets are returned.
     *
     * @param collectionOrdering AssetCollectionOrder enum
     */
    public void setCollectionOrdering(AssetCollectionOrder collectionOrdering)
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
     * Return the list of active classifications for this asset.
     *
     * @return list of classification objects
     */
    public List<Classification> getClassifications()
    {
        if (classifications == null)
        {
            return null;
        }
        else if (classifications.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(classifications);
        }
    }


    /**
     * Set up the list of active classifications for this asset.
     *
     * @param classifications list of classification objects
     */
    public void setClassifications(List<Classification> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetCollection{" +
                "guid='" + guid + '\'' +
                ", typeName='" + typeName + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", collectionUse='" + collectionUse + '\'' +
                ", collectionOrdering=" + collectionOrdering +
                ", additionalProperties=" + additionalProperties +
                ", classifications=" + classifications +
                ", GUID='" + getGUID() + '\'' +
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
        AssetCollection that = (AssetCollection) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(getTypeName(), that.getTypeName()) &&
                Objects.equals(getQualifiedName(), that.getQualifiedName()) &&
                Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getCollectionUse(), that.getCollectionUse()) &&
                getCollectionOrdering() == that.getCollectionOrdering() &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties()) &&
                Objects.equals(getClassifications(), that.getClassifications());
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid, getTypeName(), getQualifiedName(), getDisplayName(), getDescription(),
                            getCollectionUse(),
                            getCollectionOrdering(), getAdditionalProperties(), getClassifications());
    }
}
