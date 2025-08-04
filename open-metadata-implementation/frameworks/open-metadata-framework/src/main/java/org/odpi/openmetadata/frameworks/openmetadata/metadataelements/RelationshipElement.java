/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipElement contains the properties and header for a relationship retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipElement
{
    private ElementHeader              relationshipHeader     = null;
    private RelationshipBeanProperties relationshipProperties = null;
    private ElementStub                end1                   = null;
    private ElementStub            end2                   = null;

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
            end1 = template.getEnd1();
            end2 = template.getEnd2();
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
    public RelationshipBeanProperties getRelationshipProperties()
    {
        return relationshipProperties;
    }


    /**
     * Set up relationship properties
     *
     * @param relationshipProperties relationship properties
     */
    public void setRelationshipProperties(RelationshipBeanProperties relationshipProperties)
    {
        this.relationshipProperties = relationshipProperties;
    }


    /**
     * Return the element header associated with end 1 of the relationship.
     *
     * @return element header object
     */
    public ElementStub getEnd1()
    {
        return end1;
    }


    /**
     * Set up the element header associated with end 1 of the relationship.
     *
     * @param end1 element header object
     */
    public void setEnd1(ElementStub end1)
    {
        this.end1 = end1;
    }



    /**
     * Return the element header associated with end 2 of the relationship.
     *
     * @return element header object
     */
    public ElementStub getEnd2()
    {
        return end2;
    }


    /**
     * Set up the element header associated with end 2 of the relationship.
     *
     * @param end2 element header object
     */
    public void setEnd2(ElementStub end2)
    {
        this.end2 = end2;
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
                       ", end1GUID=" + end1 +
                       ", end2GUID=" + end2 +
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
                       Objects.equals(getEnd1(), that.getEnd1()) &&
                       Objects.equals(getEnd2(), that.getEnd2());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relationshipHeader, relationshipProperties, end1, end2);
    }
}
