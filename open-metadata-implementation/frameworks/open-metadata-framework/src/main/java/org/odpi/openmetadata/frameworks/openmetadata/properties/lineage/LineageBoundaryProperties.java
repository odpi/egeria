/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.lineage;

import com.fasterxml.jackson.annotation.*;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LineageBoundaryProperties describe the common properties for a lineage boundary relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = UltimateDestinationProperties.class, name = "UltimateDestinationProperties"),
                @JsonSubTypes.Type(value = UltimateSourceProperties.class, name = "UltimateSourceProperties"),
        })
public class LineageBoundaryProperties  extends LineageRelationshipProperties
{
    private Map<String, Integer> hops = null;


    /**
     * Default constructor
     */
    public LineageBoundaryProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public LineageBoundaryProperties(LineageBoundaryProperties template)
    {
        super(template);

        if (template != null)
        {
            hops = template.getHops();
        }
    }


    /**
     * Set up the details of how many hops the ultimate source/destination is.
     *
     * @param hops map from element qualified name to number of hops
     */
    public void setHops(Map<String, Integer> hops)
    {
        this.hops = hops;
    }


    /**
     * Returns the details of how many hops the ultimate source/destination is.
     *
     * @return map from element qualified name to number of hops
     */
    public Map<String, Integer> getHops()
    {
        return hops;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LineageBoundaryProperties{" +
                "hops='" + hops + '\'' +
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
        if (!super.equals(objectToCompare)) return false;
        LineageBoundaryProperties that = (LineageBoundaryProperties) objectToCompare;
        return Objects.equals(hops, that.hops);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), hops);
    }
}