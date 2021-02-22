/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;

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

    private CompletionStatus    status               = null;
    private Map<String, String> requestProperties    = null;
    private List<String>        outputGuards         = null;
    private List<String>        newActionTargetGUIDs = null;


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
            requestProperties = template.getRequestProperties();
            outputGuards = template.getOutputGuards();
            newActionTargetGUIDs = template.getNewActionTargetGUIDs();
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
    public Map<String, String> getRequestProperties()
    {
        if (requestProperties == null)
        {
            return null;
        }

        if (requestProperties.isEmpty())
        {
            return null;
        }

        return requestProperties;
    }


    /**
     * Set up the parameters to pass onto the governance service.
     *
     * @param requestProperties map of properties
     */
    public void setRequestProperties(Map<String, String> requestProperties)
    {
        this.requestProperties = requestProperties;
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
     * Return the list of metadata elements that the next governance action should target.
     *
     * @return list of string guids
     */
    public List<String> getNewActionTargetGUIDs()
    {
        if (newActionTargetGUIDs == null)
        {
            return null;
        }

        if (newActionTargetGUIDs.isEmpty())
        {
            return null;
        }

        return newActionTargetGUIDs;
    }


    /**
     * Set up the list of metadata elements that the next governance action should target.
     *
     * @param newActionTargetGUIDs list of string guids
     */
    public void setNewActionTargetGUIDs(List<String> newActionTargetGUIDs)
    {
        this.newActionTargetGUIDs = newActionTargetGUIDs;
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
                       ", requestProperties=" + requestProperties +
                       ", outputGuards=" + outputGuards +
                       ", newActionTargetGUIDs=" + newActionTargetGUIDs +
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
                       Objects.equals(newActionTargetGUIDs, that.newActionTargetGUIDs);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(status, outputGuards, newActionTargetGUIDs);
    }
}
