/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

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
public abstract class CollectionMemberHeader implements MetadataElement, Serializable
{
    private static final long    serialVersionUID = 1L;

    private ElementHeader        elementHeader = null;

    private Date         dateAddedToCollection = null;
    private String       membershipRationale   = null;


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
        if (template != null)
        {
            this.elementHeader = template.getElementHeader();
            this.dateAddedToCollection = template.getDateAddedToCollection();
            this.membershipRationale = template.getMembershipRationale();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
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
        return "CollectionMemberHeader{" +
                       "elementHeader=" + elementHeader +
                       ", dateAddedToCollection=" + dateAddedToCollection +
                       ", membershipRationale='" + membershipRationale + '\'' +
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
        CollectionMemberHeader that = (CollectionMemberHeader) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(dateAddedToCollection, that.dateAddedToCollection) &&
                       Objects.equals(membershipRationale, that.membershipRationale);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getElementHeader(), getDateAddedToCollection(), getMembershipRationale());
    }
}
