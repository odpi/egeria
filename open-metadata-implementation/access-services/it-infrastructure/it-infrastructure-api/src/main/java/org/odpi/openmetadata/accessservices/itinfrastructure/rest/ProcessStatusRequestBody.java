/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ProcessStatus;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ProcessStatusRequestBody describes the request body used to update a process's status.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProcessStatusRequestBody implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private ProcessStatus processStatus = null;


    /**
     * Default constructor
     */
    public ProcessStatusRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ProcessStatusRequestBody(ProcessStatusRequestBody template)
    {
        if (template != null)
        {
            processStatus = template.getProcessStatus();
        }
    }


    /**
     * Return the status for the element.
     *
     * @return enum object
     */
    public ProcessStatus getProcessStatus()
    {
        return processStatus;
    }


    /**
     * Set up the status for the element.
     *
     * @param processStatus enum object
     */
    public void setProcessStatus(ProcessStatus processStatus)
    {
        this.processStatus = processStatus;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProcessStatusRequestBody{" +
                       "processStatus=" + processStatus +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        ProcessStatusRequestBody that = (ProcessStatusRequestBody) objectToCompare;
        return Objects.equals(getProcessStatus(), that.getProcessStatus());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), processStatus);
    }
}
