/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.properties;

import java.util.Date;
import java.util.Objects;

/**
 * AssetCollectionMember describes an asset that is a member of an asset collection.  It combines information
 * from the Asset Entity with the Collection Membership relationship.
 */
public class AssetCollectionMember extends Asset
{
    Date        dateAssetCreated      = null;
    Date        dateAssetLastUpdated  = null;
    Date        dateAddedToCollection = null;
    String      membershipRationale   = null;
    WatchStatus watchStatus           = null;


    /**
     * Default constructor
     */
    public AssetCollectionMember()
    {
        super();
    }


    /**
     * Copy/clone constructor
     */
    public AssetCollectionMember(AssetCollectionMember   template)
    {
        super(template);

        if (template != null)
        {
            this.dateAssetCreated = template.getDateAssetCreated();
            this.dateAssetLastUpdated = template.getDateAssetLastUpdated();
            this.dateAddedToCollection = template.getDateAddedToCollection();
            this.membershipRationale = template.getMembershipRationale();
            this.watchStatus = template.getWatchStatus();
        }
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
     * Return the date that the asset was added to this collection.
     *
     * @return date
     */
    public Date getDateAddedToCollection()
    {
        if (dateAddedToCollection == null)
        {
            return null;
        }
        else
        {
            return new Date(dateAddedToCollection.getTime());
        }
    }


    /**
     * Set up the date that the asset was added to this collection.
     *
     * @param dateAddedToCollection date
     */
    public void setDateAddedToCollection(Date dateAddedToCollection)
    {
        this.dateAddedToCollection = dateAddedToCollection;
    }


    /**
     * Return the rationale or role of the asset in this collection.
     *
     * @return text
     */
    public String getMembershipRationale()
    {
        return membershipRationale;
    }


    /**
     * Set up the rationale or role of the asset in this collection.
     *
     * @param membershipRationale text
     */
    public void setMembershipRationale(String membershipRationale)
    {
        this.membershipRationale = membershipRationale;
    }


    /**
     * Return whether changes to this asset should be sent as notifications to this actor.
     *
     * @return WatchStatus enum
     */
    public WatchStatus getWatchStatus()
    {
        return watchStatus;
    }


    /**
     * Set up whether changes to this asset should be sent as notifications to this actor.
     *
     * @param watchStatus WatchStatus enum
     */
    public void setWatchStatus(WatchStatus watchStatus)
    {
        this.watchStatus = watchStatus;
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
                "dateAssetCreated=" + dateAssetCreated +
                ", dateAssetLastUpdated=" + dateAssetLastUpdated +
                ", dateAddedToCollection=" + dateAddedToCollection +
                ", membershipRationale='" + membershipRationale + '\'' +
                ", watchStatus=" + watchStatus +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", owner='" + getOwner() + '\'' +
                ", zoneMembership=" + getZoneMembership() +
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
        AssetCollectionMember that = (AssetCollectionMember) objectToCompare;
        return Objects.equals(getDateAssetCreated(), that.getDateAssetCreated()) &&
                Objects.equals(getDateAssetLastUpdated(), that.getDateAssetLastUpdated()) &&
                Objects.equals(getDateAddedToCollection(), that.getDateAddedToCollection()) &&
                Objects.equals(getMembershipRationale(), that.getMembershipRationale()) &&
                getWatchStatus() == that.getWatchStatus();
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDateAssetCreated(), getDateAssetLastUpdated(),
                            getDateAddedToCollection(),
                            getMembershipRationale(), getWatchStatus());
    }

}
