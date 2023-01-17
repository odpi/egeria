/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionMember describes a member of a collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionMember implements MetadataElement, Serializable
{
    private static final long    serialVersionUID = 1L;

    private ElementHeader elementHeader = null;

    private Date   dateAddedToCollection = null;
    private String membershipRationale   = null;
    private ReferenceableProperties properties = null;


    /**
     * Default constructor
     */
    public CollectionMember()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CollectionMember(CollectionMember template)
    {
        if (template != null)
        {
            this.elementHeader = template.getElementHeader();
            this.dateAddedToCollection = template.getDateAddedToCollection();
            this.membershipRationale = template.getMembershipRationale();
            this.properties = template.getProperties();
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
     * Return the properties of the element.
     *
     * @return properties
     */
    public ReferenceableProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the element.
     *
     * @param properties  properties
     */
    public void setProperties(ReferenceableProperties properties)
    {
        this.properties = properties;
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
                       "elementHeader=" + elementHeader +
                       ", dateAddedToCollection=" + dateAddedToCollection +
                       ", membershipRationale='" + membershipRationale + '\'' +
                       ", properties=" + properties +
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
        if (! (objectToCompare instanceof CollectionMember))
        {
            return false;
        }

        CollectionMember that = (CollectionMember) objectToCompare;

        if (elementHeader != null ? ! elementHeader.equals(that.elementHeader) : that.elementHeader != null)
        {
            return false;
        }
        if (dateAddedToCollection != null ? ! dateAddedToCollection.equals(that.dateAddedToCollection) : that.dateAddedToCollection != null)
        {
            return false;
        }
        if (membershipRationale != null ? ! membershipRationale.equals(that.membershipRationale) : that.membershipRationale != null)
        {
            return false;
        }
        return properties != null ? properties.equals(that.properties) : that.properties == null;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        int result = elementHeader != null ? elementHeader.hashCode() : 0;
        result = 31 * result + (dateAddedToCollection != null ? dateAddedToCollection.hashCode() : 0);
        result = 31 * result + (membershipRationale != null ? membershipRationale.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }
}
