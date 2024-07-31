/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueMembershipProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueMember describes a member of a valid value set.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueMember
{
    private ElementHeader                  relationshipHeader     = null;
    private ValidValueMembershipProperties relationshipProperties = null;
    private ValidValueElement              validValueElement      = null;


    /**
     * Default constructor
     */
    public ValidValueMember()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValueMember(ValidValueMember template)
    {
        if (template != null)
        {
            this.relationshipHeader = template.getRelationshipHeader();
            this.relationshipProperties = template.getRelationshipProperties();
            this.validValueElement = template.getValidValueElement();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    public ElementHeader getRelationshipHeader()
    {
        return relationshipHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param relationshipHeader element header object
     */
    public void setRelationshipHeader(ElementHeader relationshipHeader)
    {
        this.relationshipHeader = relationshipHeader;
    }


    /**
     * Return the properties of the relationship.
     *
     * @return properties
     */
    public ValidValueMembershipProperties getRelationshipProperties()
    {
        return relationshipProperties;
    }


    /**
     * Set up the properties of the relationship.
     *
     * @param relationshipProperties  properties
     */
    public void setRelationshipProperties(ValidValueMembershipProperties relationshipProperties)
    {
        this.relationshipProperties = relationshipProperties;
    }


    /**
     * Return the member element.
     *
     * @return element
     */
    public ValidValueElement getValidValueElement()
    {
        return validValueElement;
    }


    /**
     * Set up the member element.
     *
     * @param validValueElement element
     */
    public void setValidValueElement(ValidValueElement validValueElement)
    {
        this.validValueElement = validValueElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValueMember{" +
                       "relationshipHeader=" + relationshipHeader +
                       ", relationshipProperties=" + relationshipProperties +
                       ", memberElement=" + validValueElement +
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
        if (! (objectToCompare instanceof ValidValueMember that))
        {
            return false;
        }
        return Objects.equals(relationshipHeader, that.relationshipHeader) &&
                       Objects.equals(relationshipProperties, that.relationshipProperties) &&
                       Objects.equals(validValueElement, that.validValueElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(relationshipHeader, relationshipProperties, validValueElement);
    }
}
