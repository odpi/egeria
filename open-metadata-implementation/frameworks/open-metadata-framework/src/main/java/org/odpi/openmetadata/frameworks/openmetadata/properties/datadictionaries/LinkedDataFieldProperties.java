/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CoverageCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Represents the relationship between two data fields.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LinkedDataFieldProperties extends RelationshipBeanProperties
{
    private String              relationshipTypeName = null;
    private Integer             relationshipEnd      = null;
    private Integer             minCardinality       = null;
    private Integer             maxCardinality       = null;
    private String              displayName          = null;
    private String              description          = null;
    private Map<String, String> additionalProperties = null;

    /**
     * Default constructor
     */
    public LinkedDataFieldProperties()
    {
        super();
        super.typeName = OpenMetadataType.LINKED_DATA_FIELD_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public LinkedDataFieldProperties(LinkedDataFieldProperties template)
    {
        super(template);

        if (template != null)
        {
            relationshipTypeName = template.getRelationshipTypeName();
            relationshipEnd      = template.getRelationshipEnd();
            minCardinality       = template.getMinCardinality();
            maxCardinality       = template.getMaxCardinality();
            displayName          = template.getDisplayName();
            description          = template.getDescription();
            additionalProperties = template.getAdditionalProperties();
        }
    }

    public String getRelationshipTypeName()
    {
        return relationshipTypeName;
    }

    public void setRelationshipTypeName(String relationshipTypeName)
    {
        this.relationshipTypeName = relationshipTypeName;
    }

    /**
     * Return the position of this data field in the data structure.
     *
     * @return int position in schema - 0 means none or not applicable
     */
    public Integer getRelationshipEnd() { return relationshipEnd; }


    /**
     * Set up the position of this data field in the data structure.
     *
     * @param relationshipEnd int position in schema - 0 means none or not applicable
     */
    public void setRelationshipEnd(Integer relationshipEnd)
    {
        this.relationshipEnd = relationshipEnd;
    }


    /**
     * Return this minimum number of instances allowed for this attribute.
     *
     * @return int
     */
    public Integer getMinCardinality()
    {
        return minCardinality;
    }


    /**
     * Set up the minimum number of instances allowed for this attribute.
     *
     * @param minCardinality int
     */
    public void setMinCardinality(Integer minCardinality)
    {
        this.minCardinality = minCardinality;
    }


    /**
     * Return the maximum number of instances allowed for this attribute.
     *
     * @return int (-1 means infinite)
     */
    public Integer getMaxCardinality()
    {
        return maxCardinality;
    }


    /**
     * Set up the maximum number of instances allowed for this attribute.
     *
     * @param maxCardinality int (-1 means infinite)
     */
    public void setMaxCardinality(Integer maxCardinality)
    {
        this.maxCardinality = maxCardinality;
    }



    /**
     * Returns the stored display name property for the element.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the element.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the stored description property for the element.
     * If no description is provided then null is returned.
     *
     * @return  String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property for the element.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
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
        return "LinkedDataFieldProperties{" +
                "relationshipTypeName='" + relationshipTypeName + '\'' +
                ", relationshipEnd=" + relationshipEnd +
                ", minCardinality=" + minCardinality +
                ", maxCardinality=" + maxCardinality +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        LinkedDataFieldProperties that = (LinkedDataFieldProperties) objectToCompare;
        return Objects.equals(relationshipEnd, that.relationshipEnd) &&
                Objects.equals(minCardinality, that.minCardinality) &&
                Objects.equals(maxCardinality, that.maxCardinality) &&
                Objects.equals(relationshipTypeName, that.relationshipTypeName) &&
                Objects.equals(displayName, that.displayName) &&
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
        return Objects.hash(super.hashCode(), relationshipTypeName, relationshipEnd, minCardinality, maxCardinality, displayName, description, additionalProperties);
    }
}
