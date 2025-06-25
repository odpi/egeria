/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DigitalProductStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SolutionElementStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewSolutionElementRequestBody provides an optional structure used when creating solution elements
 * (solution blueprints, solution components and solution ports).
 * It provides an optional extension to set up the initial status.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewSolutionElementRequestBody extends NewElementRequestBody
{
    private SolutionElementStatus initialStatus = null;


    /**
     * Default constructor
     */
    public NewSolutionElementRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewSolutionElementRequestBody(NewSolutionElementRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.initialStatus = template.getInitialStatus();
        }
    }


    /**
     * Return the initial status of the solution element.
     *
     * @return instance status
     */
    public SolutionElementStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Set up the initial status of the solution element.
     *
     * @param initialStatus instance status
     */
    public void setInitialStatus(SolutionElementStatus initialStatus)
    {
        this.initialStatus = initialStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewSolutionElementRequestBody{" +
                ", initialStatus=" + initialStatus +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof NewSolutionElementRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return initialStatus == that.initialStatus;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), initialStatus);
    }
}
