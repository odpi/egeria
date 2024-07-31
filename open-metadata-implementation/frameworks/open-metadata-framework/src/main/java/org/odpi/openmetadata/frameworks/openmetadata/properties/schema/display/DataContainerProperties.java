/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.display;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaElementProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaAttributeProperties represents a data field that is part of a complex schema type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContainerProperties extends SchemaElementProperties
{
    private int               elementPosition       = 0;
    private int               minCardinality        = 0;
    private int               maxCardinality        = 0;



    /**
     * Default constructor
     */
    public DataContainerProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public DataContainerProperties(DataContainerProperties template)
    {
        super(template);

        if (template != null)
        {
            elementPosition       = template.getElementPosition();
            minCardinality        = template.getMinCardinality();
            maxCardinality        = template.getMaxCardinality();
        }
    }


    /**
     * Return the position of this schema attribute in its parent schema.
     *
     * @return int position in schema - 0 means first
     */
    public int getElementPosition() { return elementPosition; }


    /**
     * Set up the position of this schema attribute in its parent schema.
     *
     * @param elementPosition int position in schema - 0 means first
     */
    public void setElementPosition(int elementPosition)
    {
        this.elementPosition = elementPosition;
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
        return "DataContainerProperties{" +
                       "elementPosition=" + elementPosition +
                       ", minCardinality=" + minCardinality +
                       ", maxCardinality=" + maxCardinality +
                       ", isDeprecated=" + getIsDeprecated() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        DataContainerProperties that = (DataContainerProperties) objectToCompare;
        return elementPosition == that.elementPosition &&
                       minCardinality == that.minCardinality &&
                       maxCardinality == that.maxCardinality;
    }


    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementPosition, minCardinality, maxCardinality);
    }
}
