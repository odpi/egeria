/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CoverageCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.MemberDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.NestedDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.AttributeForSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.NestedSchemaAttributeProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Represents the relationship between a data structure type element and a data field within that structure.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AttributeForSchemaProperties.class, name = "AttributeForSchemaProperties"),
                @JsonSubTypes.Type(value = MemberDataFieldProperties.class, name = "MemberDataFieldProperties"),
                @JsonSubTypes.Type(value = NestedDataFieldProperties.class, name = "NestedDataFieldProperties"),
                @JsonSubTypes.Type(value = NestedSchemaAttributeProperties.class, name = "NestedSchemaAttributeProperties"),
        })
public class PartOfRelationshipProperties extends RelationshipBeanProperties
{
    private int              position         = 0;
    private int              minCardinality   = 0;
    private int              maxCardinality   = 0;
    private CoverageCategory coverageCategory = null;


    /**
     * Default constructor
     */
    public PartOfRelationshipProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public PartOfRelationshipProperties(PartOfRelationshipProperties template)
    {
        super(template);

        if (template != null)
        {
            position         = template.getPosition();
            minCardinality   = template.getMinCardinality();
            maxCardinality   = template.getMaxCardinality();
            coverageCategory = template.getCoverageCategory();
        }
    }


    /**
     * Return the position of this data field in the data structure.
     *
     * @return int position in schema - 0 means first
     */
    public int getPosition() { return position; }


    /**
     * Set up the position of this data field in the data structure.
     *
     * @param position int position in schema - 0 means first
     */
    public void setPosition(int position)
    {
        this.position = position;
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
     * Return the coverage of the attributes in this structure.
     *
     * @return enum
     */
    public CoverageCategory getCoverageCategory()
    {
        return coverageCategory;
    }


    /**
     * Set up the coverage of the attributes in this structure.
     *
     * @param coverageCategory enum
     */
    public void setCoverageCategory(CoverageCategory coverageCategory)
    {
        this.coverageCategory = coverageCategory;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "PartOfRelationshipProperties{" +
                "position=" + position +
                ", minCardinality=" + minCardinality +
                ", maxCardinality=" + maxCardinality +
                ", coverageCategory=" + coverageCategory +
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
        PartOfRelationshipProperties that = (PartOfRelationshipProperties) objectToCompare;
        return position == that.position && minCardinality == that.minCardinality && maxCardinality == that.maxCardinality && coverageCategory == that.coverageCategory;
    }

    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), position, minCardinality, maxCardinality, coverageCategory);
    }
}
