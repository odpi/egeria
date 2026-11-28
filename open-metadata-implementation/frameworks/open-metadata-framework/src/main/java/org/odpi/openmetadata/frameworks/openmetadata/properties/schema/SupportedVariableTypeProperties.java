/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SupportedVariableTypeProperties describes the link between an element (typically a process) and a schema type
 * describing the type of data value used to initialize/configure it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SupportedVariableTypeProperties extends LabeledRelationshipProperties
{
    private Integer position       = null;
    private Integer minCardinality = null;
    private Integer maxCardinality = null;


    /**
     * Default constructor
     */
    public SupportedVariableTypeProperties()
    {
        super();
        super.typeName = OpenMetadataType.SUPPORTED_VARIABLE_TYPE_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SupportedVariableTypeProperties(SupportedVariableTypeProperties template)
    {
        super(template);

        if (template != null)
        {
            position       = template.getPosition();
            minCardinality = template.getMinCardinality();
            maxCardinality = template.getMaxCardinality();
        }
    }


    /**
     * Return the position used to pass a data value of this type to the element.  Zero means no position set; the first position is 1.
     *
     * @return integer
     */
    public Integer getPosition()
    {
        return position;
    }


    /**
     * Set up the position used to pass a data value of this type to the element.  Zero means no position set; the first position is 1.
     *
     * @param position integer
     */
    public void setPosition(Integer position)
    {
        this.position = position;
    }


    /**
     * Return the minimum number of allowed instances.
     *
     * @return integer
     */
    public Integer getMinCardinality()
    {
        return minCardinality;
    }


    /**
     * Set up the minimum number of allowed instances.
     *
     * @param minCardinality integer
     */
    public void setMinCardinality(Integer minCardinality)
    {
        this.minCardinality = minCardinality;
    }


    /**
     * Return the maximum number of allowed instances.
     *
     * @return integer
     */
    public Integer getMaxCardinality()
    {
        return maxCardinality;
    }


    /**
     * Set up the maximum number of allowed instances.
     *
     * @param maxCardinality integer
     */
    public void setMaxCardinality(Integer maxCardinality)
    {
        this.maxCardinality = maxCardinality;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SupportedVariableTypeProperties{" +
                "position=" + position +
                ", minCardinality=" + minCardinality +
                ", maxCardinality=" + maxCardinality +
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
        SupportedVariableTypeProperties that = (SupportedVariableTypeProperties) objectToCompare;
        return Objects.equals(position, that.position) &&
                Objects.equals(minCardinality, that.minCardinality) &&
                Objects.equals(maxCardinality, that.maxCardinality);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), position, minCardinality, maxCardinality);
    }
}
