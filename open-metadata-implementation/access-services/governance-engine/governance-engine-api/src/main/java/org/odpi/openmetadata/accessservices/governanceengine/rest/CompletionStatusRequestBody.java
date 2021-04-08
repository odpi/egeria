/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CompletionStatusRequestBody provides a structure for passing the properties to record that a governance service finished a governance action.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CompletionStatusRequestBody implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private CompletionStatus      status            = null;
    private Map<String, String>   requestParameters = null;
    private List<String>          outputGuards      = null;
    private List<NewActionTarget> newActionTargets  = null;


    /**
     * Default constructor
     */
    public CompletionStatusRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CompletionStatusRequestBody(CompletionStatusRequestBody template)
    {
        if (template != null)
        {
            status = template.getStatus();
            requestParameters = template.getRequestParameters();
            outputGuards = template.getOutputGuards();
            newActionTargets = template.getNewActionTargets();
        }
    }


    /**
     * Return the completion status supplied by the governance service when it completed.
     *
     * @return completion status enum
     */
    public CompletionStatus getStatus()
    {
        return status;
    }


    /**
     * Set up the completion status.
     *
     * @param status completion status enum
     */
    public void setStatus(CompletionStatus status)
    {
        this.status = status;
    }


    /**
     * Return the parameters to pass onto the governance service.
     *
     * @return map of properties
     */
    public Map<String, String> getRequestParameters()
    {
        if (requestParameters == null)
        {
            return null;
        }

        if (requestParameters.isEmpty())
        {
            return null;
        }

        return requestParameters;
    }


    /**
     * Set up the parameters to pass onto the governance service.
     *
     * @param requestParameters map of properties
     */
    public void setRequestParameters(Map<String, String> requestParameters)
    {
        this.requestParameters = requestParameters;
    }


    /**
     * Return the list of guards supplied by the governance service when it completed.
     *
     * @return list of string guids
     */
    public List<String> getOutputGuards()
    {
        if (outputGuards == null)
        {
            return null;
        }

        if (outputGuards.isEmpty())
        {
            return null;
        }

        return outputGuards;
    }


    /**
     * Set up the list of guards supplied by the governance service when it completed.
     *
     * @param outputGuards list of string guids
     */
    public void setOutputGuards(List<String> outputGuards)
    {
        this.outputGuards = outputGuards;
    }


    /**
     * Return the metadata elements that the next governance action should target.
     *
     * @return list of names to string guids
     */
    public List<NewActionTarget> getNewActionTargets()
    {
        if (newActionTargets == null)
        {
            return null;
        }

        if (newActionTargets.isEmpty())
        {
            return null;
        }

        return newActionTargets;
    }


    /**
     * Set up the list of metadata elements that the next governance action should target.
     *
     * @param newActionTargets list of names to string guids
     */
    public void setNewActionTargets(List<NewActionTarget> newActionTargets)
    {
        this.newActionTargets = newActionTargets;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "CompletionStatusRequestBody{" +
                       "status=" + status +
                       ", requestParameters=" + requestParameters +
                       ", outputGuards=" + outputGuards +
                       ", newActionTargets=" + newActionTargets +
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
        CompletionStatusRequestBody that = (CompletionStatusRequestBody) objectToCompare;
        return status == that.status &&
                       Objects.equals(outputGuards, that.outputGuards) &&
                       Objects.equals(newActionTargets, that.newActionTargets);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(status, outputGuards, newActionTargets);
    }
}
