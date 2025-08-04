/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class AttributeForSchemaProperties extends RelationshipBeanProperties
{
    private int position       = 0;
    private int minCardinality = 0;
    private int maxCardinality = 0;


    /**
     * Default constructor
     */
    public AttributeForSchemaProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public AttributeForSchemaProperties(AttributeForSchemaProperties template)
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AttributeForSchemaProperties{" +
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
        AttributeForSchemaProperties that = (AttributeForSchemaProperties) objectToCompare;
        return  minCardinality == that.minCardinality &&
                maxCardinality == that.maxCardinality &&
                position == that.position;
    }


    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), position, minCardinality, maxCardinality);
    }
}
