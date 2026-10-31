/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.RelationshipDecoration;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Represents the relationship between a complex schema type and a nested schema attribute.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConceptBeadRelationshipEndProperties extends RelationshipBeanProperties
{
    private String                 attributeName  = null;
    private RelationshipDecoration decoration     = null;
    private Integer                position       = null;
    private Integer                minCardinality = null;
    private Integer                maxCardinality = null;
    private Boolean                uniqueValues   = null;
    private Boolean                orderedValues  = null;
    private Boolean                navigable      = null;


    /**
     * Default constructor
     */
    public ConceptBeadRelationshipEndProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONCEPT_BEAD_RELATIONSHIP_END_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public ConceptBeadRelationshipEndProperties(ConceptBeadRelationshipEndProperties template)
    {
        super(template);

        if (template != null)
        {
            attributeName  = template.getAttributeName();
            decoration     = template.getDecoration();
            position       = template.getPosition();
            minCardinality = template.getMinCardinality();
            maxCardinality = template.getMaxCardinality();
            uniqueValues   = template.getUniqueValues();
            orderedValues  = template.getOrderedValues();
            navigable      = template.getNavigable();
        }
    }


    /**
     * Return the position of this relationship.
     *
     * @return int position in schema - 0 means none or N/A
     */
    public Integer getPosition() { return position; }


    /**
     * Set up the position of this data field in the data structure.
     *
     * @param position int position in schema - 0 means first
     */
    public void setPosition(Integer position)
    {
        this.position = position;
    }


    /**
     * Return the name of the relationship.
     *
     * @return name
     */
    public String getAttributeName()
    {
        return attributeName;
    }


    /**
     * Set up the name for this relationship.
     *
     * @param attributeName name
     */
    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }


    /**
     * Return the description of this end of the relationship.
     *
     * @return string
     */
    public RelationshipDecoration getDecoration()
    {
        return decoration;
    }


    /**
     * Set up the description of this end of the relationship.
     *
     * @param decoration string
     */
    public void setDecoration(RelationshipDecoration decoration)
    {
        this.decoration = decoration;
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
     * Return the boolean flag that indicates whether the instances have unique values.
     *
     * @return boolean
     */
    public Boolean getUniqueValues()
    {
        return uniqueValues;
    }


    /**
     * Set up the boolean flag that indicates whether the instances have unique values.
     *
     * @param flag boolean
     */
    public void setUniqueValues(Boolean flag)
    {
        this.uniqueValues = flag;
    }


    /**
     * Return the boolean flag that indicates whether the instances have ordered values.
     *
     * @return boolean
     */
    public Boolean getOrderedValues() { return orderedValues; }


    /**
     * Set up the boolean flag that indicates whether the instances have ordered values.
     *
     * @param orderedValues boolean
     */
    public void setOrderedValues(Boolean orderedValues)
    {
        this.orderedValues = orderedValues;
    }


    /**
     * Set up whether the relationship is navigable through this end of the relationship
     *
     * @param navigable boolean
     */
    public void setNavigable(Boolean navigable)
    {
        this.navigable = navigable;
    }


    /**
     * Return whether the relationship is navigable through this end of the relationship.
     *
     * @return boolean
     */
    public Boolean getNavigable()
    {
        return navigable;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConceptBeadRelationshipEndProperties{" +
                "attributeName='" + attributeName + '\'' +
                ", decoration=" + decoration +
                ", position=" + position +
                ", minCardinality=" + minCardinality +
                ", maxCardinality=" + maxCardinality +
                ", uniqueValues=" + uniqueValues +
                ", orderedValues=" + orderedValues +
                ", navigable=" + navigable +
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
        ConceptBeadRelationshipEndProperties that = (ConceptBeadRelationshipEndProperties) objectToCompare;
        return Objects.equals(position, that.position) &&
                Objects.equals(minCardinality, that.minCardinality) &&
                Objects.equals(maxCardinality, that.maxCardinality) &&
                Objects.equals(orderedValues, that.orderedValues) &&
                Objects.equals(uniqueValues, that.uniqueValues) &&
                Objects.equals(navigable, that.navigable) &&
                Objects.equals(attributeName, that.attributeName) &&
                Objects.equals(decoration, that.decoration);
    }

    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), uniqueValues, orderedValues, position, attributeName, decoration, minCardinality, maxCardinality, navigable);
    }
}
