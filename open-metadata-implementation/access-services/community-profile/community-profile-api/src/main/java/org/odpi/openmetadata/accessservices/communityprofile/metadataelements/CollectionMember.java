/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionMembershipProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ReferenceableProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

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
    private ElementHeader                  relationshipHeader     = null;
    private CollectionMembershipProperties relationshipProperties = null;

    private ElementHeader                  elementHeader    = null;

    private ReferenceableProperties        properties = null;


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
            relationshipHeader = template.getRelationshipHeader();
            relationshipProperties = template.getRelationshipProperties();
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
        }
    }

    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    public ElementHeader getRelationshipHeader()
    {
        return relationshipHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param relationshipHeader element header object
     */
    public void setRelationshipHeader(ElementHeader relationshipHeader)
    {
        this.relationshipHeader = relationshipHeader;
    }


    /**
     * Return details of the relationship
     *
     * @return relationship properties
     */
    public CollectionMembershipProperties getRelationshipProperties()
    {
        return relationshipProperties;
    }


    /**
     * Set up relationship properties
     *
     * @param relationshipProperties relationship properties
     */
    public void setRelationshipProperties(CollectionMembershipProperties relationshipProperties)
    {
        this.relationshipProperties = relationshipProperties;
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
                "relationshipHeader=" + relationshipHeader +
                ", relationshipProperties=" + relationshipProperties +
                ", elementHeader=" + elementHeader +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        CollectionMember that = (CollectionMember) objectToCompare;
        return Objects.equals(relationshipHeader, that.relationshipHeader) &&
                Objects.equals(relationshipProperties, that.relationshipProperties) &&
                Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(properties, that.properties);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(relationshipHeader, relationshipProperties, elementHeader, properties);
    }
}
