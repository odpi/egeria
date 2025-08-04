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
 * RelatedBy contains the properties and header for a relationship retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedBy
{
    private ElementHeader              relationshipHeader     = null;
    private RelationshipBeanProperties relationshipProperties = null;

    /**
     * Default constructor
     */
    public RelatedBy()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelatedBy(RelatedBy template)
    {
        if (template != null)
        {
            relationshipHeader = template.getRelationshipHeader();
            relationshipProperties = template.getRelationshipProperties();
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RelatedBy{" +
                       "relationshipHeader=" + relationshipHeader +
                       ", relationshipProperties=" + relationshipProperties +
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
        RelatedBy that = (RelatedBy) objectToCompare;
        return Objects.equals(getRelationshipHeader(), that.getRelationshipHeader()) &&
                       Objects.equals(getRelationshipProperties(), that.getRelationshipProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relationshipHeader, relationshipProperties);
    }
}
