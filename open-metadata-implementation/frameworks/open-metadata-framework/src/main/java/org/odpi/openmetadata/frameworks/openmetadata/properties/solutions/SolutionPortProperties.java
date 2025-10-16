/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.solutions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SolutionPortDirection;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.DesignModelElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SolutionPortProperties is a class for representing a generic port for a solution component.  
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionPortProperties extends DesignModelElementProperties
{
    private SolutionPortDirection direction         = null;


    /**
     * Default constructor
     */
    public SolutionPortProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SOLUTION_PORT.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SolutionPortProperties(SolutionPortProperties template)
    {
        super(template);

        if (template != null)
        {
            this.direction = template.getDirection();
        }
    }


    /**
     * Return the direction of data flow of the port.
     *
     * @return portType enum
     */
    public SolutionPortDirection getDirection()
    {
        return direction;
    }


    /**
     * Set up the direction of data flow of the port.
     *
     * @param direction portType enum
     */
    public void setDirection(SolutionPortDirection direction)
    {
        this.direction = direction;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SolutionPortProperties{" +
                "solutionPortDirection=" + direction +
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
        SolutionPortProperties that = (SolutionPortProperties) objectToCompare;
        return direction == that.direction;
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), direction);
    }
}
