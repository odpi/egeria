/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.PartOfRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Represents the relationship between a concept bead and one of its attributes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConceptBeadAttributeLinkProperties extends PartOfRelationshipProperties
{
    private boolean uniqueValues  = false;
    private boolean orderedValues = false;


    /**
     * Default constructor
     */
    public ConceptBeadAttributeLinkProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONCEPT_BEAD_ATTRIBUTE_LINK_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public ConceptBeadAttributeLinkProperties(ConceptBeadAttributeLinkProperties template)
    {
        super(template);

        if (template != null)
        {
            uniqueValues  = template.getUniqueValues();
            orderedValues = template.getOrderedValues();
        }
    }


    /**
     * Return the boolean flag that indicates whether the instances have unique values.
     *
     * @return boolean
     */
    public boolean getUniqueValues()
    {
        return uniqueValues;
    }


    /**
     * Set up the boolean flag that indicates whether the instances have unique values.
     *
     * @param flag boolean
     */
    public void setUniqueValues(boolean flag)
    {
        this.uniqueValues = flag;
    }


    /**
     * Return the boolean flag that indicates whether the instances have ordered values.
     *
     * @return boolean
     */
    public boolean getOrderedValues() { return orderedValues; }


    /**
     * Set up the boolean flag that indicates whether the instances have ordered values.
     *
     * @param orderedValues boolean
     */
    public void setOrderedValues(boolean orderedValues)
    {
        this.orderedValues = orderedValues;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConceptBeadAttributeLinkProperties{" +
                "uniqueValues=" + uniqueValues +
                ", orderedValues=" + orderedValues +
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
        ConceptBeadAttributeLinkProperties that = (ConceptBeadAttributeLinkProperties) objectToCompare;
        return orderedValues == that.orderedValues &&
                uniqueValues == that.uniqueValues ;
    }

    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), uniqueValues, orderedValues);
    }
}
