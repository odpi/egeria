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
 * MetadataRelationship describes a relationship between two metadata elements.
 * These relationships are directional (end1->end1) - although it is possible to navigate the relationships
 * in either direction.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetadataRelationshipSummary
{
    private ElementHeader              relationshipHeader     = null;
    private RelationshipBeanProperties relationshipProperties = null;
    private ElementStub                end1                   = null;
    private ElementStub                end2                   = null;

    /**
     * Default constructor
     */
    public MetadataRelationshipSummary()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template template to copy
     */
    public MetadataRelationshipSummary(MetadataRelationshipSummary template)
    {
        if (template != null)
        {
            relationshipHeader     = template.getRelationshipHeader();
            relationshipProperties = template.getRelationshipProperties();
            end1                   = template.getEnd1();
            end2 = template.getEnd2();
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
     * Set up the  properties for the element.
     * If no stored properties are present then null is returned.
     *
     * @param relationshipProperties  properties for the classification
     */
    public void setRelationshipProperties(RelationshipBeanProperties relationshipProperties)
    {
        this.relationshipProperties = relationshipProperties;
    }


    /**
     * Return a collection of the properties for the element.
     * If no stored properties are present then null is returned.
     *
     * @return properties map
     */
    public RelationshipBeanProperties getRelationshipProperties()
    {
        return relationshipProperties;
    }


    /**
     * Return the summary of the metadata element at end 1 of the relationship.
     *
     * @return element stub
     */
    public ElementStub getEnd1()
    {
        return end1;
    }


    /**
     * Set up the summary of the metadata element at end 1 of the relationship.
     *
     * @param end1 element stub
     */
    public void setEnd1(ElementStub end1)
    {
        this.end1 = end1;
    }


    /**
     * Return the summary of the metadata element at end 2 of the relationship.
     *
     * @return element stub
     */
    public ElementStub getEnd2()
    {
        return end2;
    }


    /**
     * Set up the summary of the metadata element at end 2 of the relationship.
     *
     * @param end2 element stub
     */
    public void setEnd2(ElementStub end2)
    {
        this.end2 = end2;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MetadataRelationshipSummary{" +
                "relationshipHeader=" + relationshipHeader +
                ", relationshipProperties=" + relationshipProperties +
                ", end1=" + end1 +
                ", end2=" + end2 +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        MetadataRelationshipSummary that = (MetadataRelationshipSummary) objectToCompare;
        return Objects.equals(relationshipHeader, that.relationshipHeader) &&
                Objects.equals(relationshipProperties, that.relationshipProperties) &&
                Objects.equals(end1, that.end1) &&
                Objects.equals(end2, that.end2);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(relationshipHeader, relationshipProperties, end1, end2);
    }
}
