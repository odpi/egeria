/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalservice.properties.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionMember describes a member of a collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionMember implements MetadataElement
{
    private ElementHeader                  elementHeader       = null;
    private CollectionMembershipProperties membershipProperties = null;
    private ElementStub                    member = null;


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
            this.membershipProperties = template.getMembershipProperties();
            this.member = template.getMember();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties from the membership relationship.
     *
     * @return membership properties
     */
    public CollectionMembershipProperties getMembershipProperties()
    {
        return membershipProperties;
    }


    /**
     * Set up the properties from the membership relationship.
     *
     * @param membershipProperties membership properties
     */
    public void setMembershipProperties(CollectionMembershipProperties membershipProperties)
    {
        this.membershipProperties = membershipProperties;
    }


    /**
     * Return the properties of the member.
     *
     * @return member properties
     */
    public ElementStub getMember()
    {
        return member;
    }


    /**
     * Set up the properties of the member.
     *
     * @param member  properties
     */
    public void setMember(ElementStub member)
    {
        this.member = member;
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
                       ", membershipProperties=" + membershipProperties +
                       ", member=" + member +
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
        if (! (objectToCompare instanceof CollectionMember that))
        {
            return false;
        }
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(membershipProperties, that.membershipProperties) &&
                       Objects.equals(member, that.member);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, membershipProperties, member);
    }
}
