/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Represents the relationship between a graph edge and vertex.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GraphEdgeLinkProperties extends RelationshipBeanProperties
{
    private String              linkTypeName         = null;
    private int                 relationshipEnd      = 0;
    private String              relationshipEndName  = null;
    private String              description          = null;
    private int                 minCardinality       = 0;
    private int                 maxCardinality       = 0;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public GraphEdgeLinkProperties()
    {
        super();
        super.typeName = OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public GraphEdgeLinkProperties(GraphEdgeLinkProperties template)
    {
        super(template);

        if (template != null)
        {
            linkTypeName = template.getLinkTypeName();
            relationshipEnd = template.getRelationshipEnd();
            relationshipEndName = template.getRelationshipEndName();
            description = template.getDescription();
            minCardinality  = template.getMinCardinality();
            maxCardinality = template.getMaxCardinality();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the name of the link.
     *
     * @return name
     */
    public String getLinkTypeName()
    {
        return linkTypeName;
    }


    /**
     * Set up the name of the link.
     *
     * @param linkTypeName name
     */
    public void setLinkTypeName(String linkTypeName)
    {
        this.linkTypeName = linkTypeName;
    }


    /**
     * Return the position of this data field in the data structure.
     *
     * @return int position in schema - 0 means first
     */
    public int getRelationshipEnd() { return relationshipEnd; }


    /**
     * Set up the position of this data field in the data structure.
     *
     * @param relationshipEnd int position in schema - 0 means first
     */
    public void setRelationshipEnd(int relationshipEnd)
    {
        this.relationshipEnd = relationshipEnd;
    }


    /**
     * Return the name of the relationship.
     *
     * @return name
     */
    public String getRelationshipEndName()
    {
        return relationshipEndName;
    }


    /**
     * Set up then name for this relationship
     *
     * @param relationshipEndName name
     */
    public void setRelationshipEndName(String relationshipEndName)
    {
        this.relationshipEndName = relationshipEndName;
    }


    /**
     * Return the description of this end of the vertex.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of this end of the vertex.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return this minimum number of instances allowed for this attribute.
     *
     * @return int
     */
    public int getMinCardinality()
    {
        return minCardinality;
    }


    /**
     * Set up the minimum number of instances allowed for this attribute.
     *
     * @param minCardinality int
     */
    public void setMinCardinality(int minCardinality)
    {
        this.minCardinality = minCardinality;
    }


    /**
     * Return the maximum number of instances allowed for this attribute.
     *
     * @return int (-1 means infinite)
     */
    public int getMaxCardinality()
    {
        return maxCardinality;
    }


    /**
     * Set up the maximum number of instances allowed for this attribute.
     *
     * @param maxCardinality int (-1 means infinite)
     */
    public void setMaxCardinality(int maxCardinality)
    {
        this.maxCardinality = maxCardinality;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GraphEdgeLinkProperties{" +
                "linkTypeName='" + linkTypeName + '\'' +
                ", relationshipEnd=" + relationshipEnd +
                ", relationshipEndName='" + relationshipEndName + '\'' +
                ", description='" + description + '\'' +
                ", minCardinality=" + minCardinality +
                ", maxCardinality=" + maxCardinality +
                ", additionalProperties=" + additionalProperties +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        GraphEdgeLinkProperties that = (GraphEdgeLinkProperties) objectToCompare;
        return relationshipEnd == that.relationshipEnd &&
                minCardinality == that.minCardinality &&
                maxCardinality == that.maxCardinality &&
                Objects.equals(linkTypeName, that.linkTypeName) &&
                Objects.equals(relationshipEndName, that.relationshipEndName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }

    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), linkTypeName, relationshipEnd, relationshipEndName, description, minCardinality, maxCardinality, additionalProperties);
    }
}
