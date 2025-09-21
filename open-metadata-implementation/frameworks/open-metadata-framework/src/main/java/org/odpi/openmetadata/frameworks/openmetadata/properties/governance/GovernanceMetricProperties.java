/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceMetricProperties stores information about the way activity associated with a governance definition is to be measured.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceMetricProperties extends GovernanceControlProperties
{
    private String measurement      = null;
    private String target           = null;


    /**
     * Default constructor
     */
    public GovernanceMetricProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GOVERNANCE_METRIC.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public GovernanceMetricProperties(GovernanceMetricProperties template)
    {
        super (template);

        if (template != null)
        {
            /*
             * Copy the values from the supplied template.
             */
            measurement      = template.getMeasurement();
            target           = template.getTarget();
        }
    }


    /**
     * Return the description of the measurements needed to support this metric.
     *
     * @return String measurement description
     */
    public String getMeasurement() { return measurement; }


    /**
     * Set up the description of the measurements needed to support this metric.
     *
     * @param measurement String measurement description
     */
    public void setMeasurement(String measurement)
    {
        this.measurement = measurement;
    }


    /**
     * Return the description of the targets that the organization is aiming to achieve.
     *
     * @return String target description.
     */
    public String getTarget() { return target; }


    /**
     * Set up the description of the targets that the organization is aiming to achieve.
     *
     * @param target String target description
     */
    public void setTarget(String target)
    {
        this.target = target;
    }




    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GovernanceMetricProperties{" +
                "measurement='" + measurement + '\'' +
                ", target='" + target + '\'' +
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
        GovernanceMetricProperties that = (GovernanceMetricProperties) objectToCompare;
        return Objects.equals(measurement, that.measurement) &&
                       Objects.equals(target, that.target);
    }


    /**
     * Uses the guid to create a hashcode.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), measurement, target);
    }
}