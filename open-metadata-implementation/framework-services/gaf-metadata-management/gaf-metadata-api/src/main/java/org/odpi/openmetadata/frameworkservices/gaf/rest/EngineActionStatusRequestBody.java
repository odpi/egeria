/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EngineActionStatusRequestBody provides a structure for passing a new EngineActionStatus.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EngineActionStatusRequestBody
{
    private EngineActionStatus status = null;



    /**
     * Default constructor
     */
    public EngineActionStatusRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EngineActionStatusRequestBody(EngineActionStatusRequestBody template)
    {
        if (template != null)
        {
            status = template.getStatus();
        }
    }


    /**
     * Return the status of the governance service.
     *
     * @return status enum
     */
    public EngineActionStatus getStatus()
    {
        return status;
    }


    /**
     * Set up the status of the governance service.
     *
     * @param status  status enum
     */
    public void setStatus(EngineActionStatus status)
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
        return "EngineActionStatusRequestBody{" +
                       "status=" + status +
                       '}';
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        EngineActionStatusRequestBody that = (EngineActionStatusRequestBody) objectToCompare;
        return status == that.status;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(status);
    }
}
