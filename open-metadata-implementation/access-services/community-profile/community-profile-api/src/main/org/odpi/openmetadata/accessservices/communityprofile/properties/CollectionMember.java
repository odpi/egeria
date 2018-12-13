/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionMember describes an asset that is a member of an asset collection.  It combines information
 * from the Asset Entity with the Collection Membership relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionMember extends Asset
{
    private Date         dateAddedToCollection = null;
    private String       membershipRationale   = null;


    /**
     * Default constructor
     */
    public CollectionMember()
    {
        super();
    }


    /**
     * Copy/clone constructor
     */
    public CollectionMember(CollectionMember template)
    {
        super(template);

        if (template != null)
        {
            this.dateAddedToCollection = template.getDateAddedToCollection();
            this.membershipRationale = template.getMembershipRationale();
        }
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CollectionMember{" +
                "dateAddedToCollection=" + dateAddedToCollection +
                ", membershipRationale='" + membershipRationale + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", owner='" + getOwner() + '\'' +
                ", zoneMembership=" + getZoneMembership() +
                ", lastChange='" + getLastChange() + '\'' +
                ", dateAssetCreated=" + getDateAssetCreated() +
                ", dateAssetLastUpdated=" + getDateAssetLastUpdated() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
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
        CollectionMember that = (CollectionMember) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getOwner(), that.getOwner()) &&
                Objects.equals(getZoneMembership(), that.getZoneMembership()) &&
                Objects.equals(getLastChange(), that.getLastChange()) &&
                Objects.equals(getDateAssetCreated(), that.getDateAssetCreated()) &&
                Objects.equals(getDateAssetLastUpdated(), that.getDateAssetLastUpdated()) &&
                Objects.equals(getDateAddedToCollection(), that.getDateAddedToCollection()) &&
                Objects.equals(getMembershipRationale(), that.getMembershipRationale());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getOwner(), getZoneMembership(),
                            getLastChange(), getDateAssetCreated(), getDateAssetLastUpdated(),
                            getDateAddedToCollection(),
                            getMembershipRationale());
    }
}
