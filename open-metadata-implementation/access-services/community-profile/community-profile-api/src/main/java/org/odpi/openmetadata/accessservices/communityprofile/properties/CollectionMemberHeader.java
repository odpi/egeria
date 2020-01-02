/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionMemberHeader describes the common properties of a item in a favorite things list.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssetCollectionMember.class, name = "AssetCollectionMember"),
        @JsonSubTypes.Type(value = ProjectCollectionMember.class, name = "ProjectCollectionMember"),
        @JsonSubTypes.Type(value = CommunityCollectionMember.class, name = "CommunityCollectionMember")
})
public class CollectionMemberHeader extends ReferenceableHeader
{
    private static final long    serialVersionUID = 1L;

    private Date         dateAddedToCollection = null;
    private String       membershipRationale   = null;
    private WatchStatus  watchStatus           = null;


    /**
     * Default constructor
     */
    public CollectionMemberHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CollectionMemberHeader(CollectionMemberHeader template)
    {
        super(template);

        if (template != null)
        {
            this.dateAddedToCollection = template.getDateAddedToCollection();
            this.membershipRationale = template.getMembershipRationale();
            this.watchStatus = template.getWatchStatus();
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
     * Return whether this element is to generate notifications when it changes.
     *
     * @return watch status enum
     */
    public WatchStatus getWatchStatus()
    {
        return watchStatus;
    }


    /**
     * Set up whether this element is to generate notifications when it changes.
     *
     * @param watchStatus watch status enum
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
        return "CollectionMemberHeader{" +
                "dateAddedToCollection=" + dateAddedToCollection +
                ", membershipRationale='" + membershipRationale + '\'' +
                ", watchStatus=" + watchStatus +
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
        CollectionMemberHeader that = (CollectionMemberHeader) objectToCompare;
        return Objects.equals(getDateAddedToCollection(), that.getDateAddedToCollection()) &&
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
        return Objects.hash(super.hashCode(), getDateAddedToCollection(), getMembershipRationale(), getWatchStatus());
    }
}
