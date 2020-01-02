/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetCollectionMember describes an asset that is a member of an individual's my-assets collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetCollectionMember extends CollectionMemberHeader
{
    private static final long    serialVersionUID = 1L;

    private String              owner                = null;
    private OwnerType           ownerType            = null;
    private List<String>        zoneMembership       = null;
    private String              lastChange           = null;
    private Date                dateAssetCreated     = null;
    private Date                dateAssetLastUpdated = null;
    private Map<String, Object> extendedProperties   = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public AssetCollectionMember()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetCollectionMember(AssetCollectionMember template)
    {
        super(template);

        if (template != null)
        {
            this.owner = template.getOwner();
            this.ownerType = template.getOwnerType();
            this.zoneMembership = template.getZoneMembership();
            this.lastChange = template.getLastChange();
            this.dateAssetCreated = template.getDateAssetCreated();
            this.dateAssetLastUpdated = template.getDateAssetLastUpdated();
            this.extendedProperties = template.getExtendedProperties();
            this.additionalProperties = template.getAdditionalProperties();
        }
    }



    /**
     * Return the owner for this asset.  This is the user id of the person or engine that is responsible for
     * managing this asset.
     *
     * @return string id
     */
    public String getOwner()
    {
        return owner;
    }


    /**
     * Set up the owner for this asset.  This is the user id of the person or engine that is responsible for
     * managing this asset.
     *
     * @param owner string id
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
    }


    /**
     * Return the type of owner identifier used in the owner field (default is userId).
     *
     * @return OwnerType enum
     */
    public OwnerType getOwnerType()
    {
        return ownerType;
    }


    /**
     * Set up the type of owner identifier used in the owner field (default is userId).
     *
     * @param ownerType OwnerType enum
     */
    public void setOwnerType(OwnerType ownerType)
    {
        this.ownerType = ownerType;
    }


    /**
     * Return the names of the zones that this asset is a member of.
     *
     * @return list of zone names
     */
    public List<String> getZoneMembership()
    {
        if (zoneMembership == null)
        {
            return null;
        }
        else if (zoneMembership.isEmpty())
        {
            return null;
        }
        else
        {
            return zoneMembership;
        }
    }


    /**
     * Set up the names of the zones that this asset is a member of.
     *
     * @param zoneMembership list of zone names
     */
    public void setZoneMembership(List<String> zoneMembership)
    {
        this.zoneMembership = zoneMembership;
    }


    /**
     * Return the description of the last change to the asset.  This is free form text.
     *
     * @return string description
     */
    public String getLastChange()
    {
        return lastChange;
    }


    /**
     * Set up the description of the last change to the asset.  This is free form text.
     *
     * @param lastChange string description
     */
    public void setLastChange(String lastChange)
    {
        this.lastChange = lastChange;
    }


    /**
     * Return the date that the asset was created.
     *
     * @return date
     */
    public Date getDateAssetCreated()
    {
        if (dateAssetCreated == null)
        {
            return null;
        }
        else
        {
            return new Date(dateAssetCreated.getTime());
        }
    }


    /**
     * Set up the date that the asset was created.
     *
     * @param dateAssetCreated date
     */
    public void setDateAssetCreated(Date dateAssetCreated)
    {
        this.dateAssetCreated = dateAssetCreated;
    }


    /**
     * Return the date that the asset was last updated.
     *
     * @return date
     */
    public Date getDateAssetLastUpdated()
    {
        if (dateAssetLastUpdated == null)
        {
            return null;
        }
        else
        {
            return new Date(dateAssetLastUpdated.getTime());
        }
    }


    /**
     * Set up the date that the asset was last updated.
     *
     * @param dateAssetLastUpdated date
     */
    public void setDateAssetLastUpdated(Date dateAssetLastUpdated)
    {
        this.dateAssetLastUpdated = dateAssetLastUpdated;
    }


    /**
     * Return any properties associated with the subclass of this element.
     *
     * @return map of property names to property values
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setExtendedProperties(Map<String, Object> additionalProperties)
    {
        this.extendedProperties = additionalProperties;
    }


    /**
     * Return any additional properties associated with the element.
     *
     * @return map of property names to property values
     */
    public Map<String, String> getAdditionalProperties()
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
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
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
        return "AssetCollectionMember{" +
                "owner='" + owner + '\'' +
                ", ownerType='" + ownerType + '\'' +
                ", zoneMembership=" + zoneMembership +
                ", lastChange='" + lastChange + '\'' +
                ", dateAssetCreated=" + dateAssetCreated +
                ", dateAssetLastUpdated=" + dateAssetLastUpdated +
                ", extendedProperties=" + extendedProperties +
                ", additionalProperties=" + additionalProperties +
                ", dateAddedToCollection=" + getDateAddedToCollection() +
                ", membershipRationale='" + getMembershipRationale() + '\'' +
                ", watchStatus=" + getWatchStatus() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", classifications=" + getClassifications() +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
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
        AssetCollectionMember that = (AssetCollectionMember) objectToCompare;
        return Objects.equals(getOwner(), that.getOwner()) &&
                Objects.equals(getOwnerType(), that.getOwnerType()) &&
                Objects.equals(getZoneMembership(), that.getZoneMembership()) &&
                Objects.equals(getLastChange(), that.getLastChange()) &&
                Objects.equals(getDateAssetCreated(), that.getDateAssetCreated()) &&
                Objects.equals(getDateAssetLastUpdated(), that.getDateAssetLastUpdated()) &&
                Objects.equals(getExtendedProperties(), that.getExtendedProperties()) &&
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
        return Objects.hash(super.hashCode(), getOwner(), getOwnerType(), getZoneMembership(), getLastChange(), getDateAssetCreated(),
                            getDateAssetLastUpdated(), getExtendedProperties(), getAdditionalProperties());
    }
}
