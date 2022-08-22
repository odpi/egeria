/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.RelationshipProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * RelationshipElement contains the properties and header for a relationship retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipElement implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader          relationshipHeader     = null;
    private RelationshipProperties relationshipProperties = null;
    private ElementHeader          end1GUID = null;
    private ElementHeader          end2GUID = null;

    /**
     * Default constructor
     */
    public RelationshipElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelationshipElement(RelationshipElement template)
    {
        if (template != null)
        {
            relationshipHeader = template.getRelationshipHeader();
            relationshipProperties = template.getRelationshipProperties();
            end1GUID = template.getEnd1GUID();
            end2GUID = template.getEnd2GUID();
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
    public RelationshipProperties getRelationshipProperties()
    {
        return relationshipProperties;
    }


    /**
     * Set up relationship properties
     *
     * @param relationshipProperties relationship properties
     */
    public void setRelationshipProperties(RelationshipProperties relationshipProperties)
    {
        this.relationshipProperties = relationshipProperties;
    }


    /**
     * Return the element header associated with end 1 of the relationship.
     *
     * @return element header object
     */
    public ElementHeader getEnd1GUID()
    {
        return end1GUID;
    }


    /**
     * Set up the element header associated with end 1 of the relationship.
     *
     * @param end1GUID element header object
     */
    public void setEnd1GUID(ElementHeader end1GUID)
    {
        this.end1GUID = end1GUID;
    }



    /**
     * Return the element header associated with end 2 of the relationship.
     *
     * @return element header object
     */
    public ElementHeader getEnd2GUID()
    {
        return end2GUID;
    }


    /**
     * Set up the element header associated with end 2 of the relationship.
     *
     * @param end2GUID element header object
     */
    public void setEnd2GUID(ElementHeader end2GUID)
    {
        this.end2GUID = end2GUID;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RelationshipElement{" +
                       "relationshipHeader=" + relationshipHeader +
                       ", relationshipProperties=" + relationshipProperties +
                       ", end1GUID=" + end1GUID +
                       ", end2GUID=" + end2GUID +
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
        RelationshipElement that = (RelationshipElement) objectToCompare;
        return Objects.equals(getRelationshipHeader(), that.getRelationshipHeader()) &&
                       Objects.equals(getRelationshipProperties(), that.getRelationshipProperties()) &&
                       Objects.equals(getEnd1GUID(), that.getEnd1GUID()) &&
                       Objects.equals(getEnd2GUID(), that.getEnd2GUID());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relationshipHeader, relationshipProperties, end1GUID, end2GUID);
    }
}
