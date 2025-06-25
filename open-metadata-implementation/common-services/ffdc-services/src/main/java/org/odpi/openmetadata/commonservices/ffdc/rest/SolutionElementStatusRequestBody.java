/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SolutionElementStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SolutionElementStatusRequestBody provides a new status for the solution element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionElementStatusRequestBody extends MetadataSourceRequestBody
{
    private SolutionElementStatus status = null;


    /**
     * Default constructor
     */
    public SolutionElementStatusRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionElementStatusRequestBody(SolutionElementStatusRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.status = template.getStatus();
        }
    }


    /**
     * Return the new status of the solution element.
     *
     * @return instance status
     */
    public SolutionElementStatus getStatus()
    {
        return status;
    }


    /**
     * Set up the new status of the solution element.
     *
     * @param status instance status
     */
    public void setStatus(SolutionElementStatus status)
    {
        this.status = status;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "SolutionElementStatusRequestBody{" +
                "status=" + status +
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
        if (! (objectToCompare instanceof SolutionElementStatusRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return  status == that.status;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), status);
    }
}
