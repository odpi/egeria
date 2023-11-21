/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionProcessProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ProcessStatus;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * NewGovernanceActionProcessRequestBody describes the request body used to create governance action process properties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewGovernanceActionProcessRequestBody implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private ProcessStatus                     processStatus = null;
    private GovernanceActionProcessProperties properties    = null;


    /**
     * Default constructor
     */
    public NewGovernanceActionProcessRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public NewGovernanceActionProcessRequestBody(NewGovernanceActionProcessRequestBody template)
    {
        if (template != null)
        {
            processStatus = template.getProcessStatus();
            properties = template.getProperties();
        }
    }


    /**
     * Return the initial status of the governance action status.
     *
     * @return status enum
     */
    public ProcessStatus getProcessStatus()
    {
        return processStatus;
    }


    /**
     * Set up the initial status of the governance action status.
     *
     * @param processStatus status enum
     */
    public void setProcessStatus(ProcessStatus processStatus)
    {
        this.processStatus = processStatus;
    }


    /**
     * Return the properties for the element.
     *
     * @return properties object
     */
    public GovernanceActionProcessProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the element.
     *
     * @param properties properties object
     */
    public void setProperties(GovernanceActionProcessProperties properties)
    {
        this.properties = properties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NewGovernanceActionProcessRequestBody{" +
                       "processStatus=" + processStatus +
                       ", properties=" + properties +
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
        NewGovernanceActionProcessRequestBody that = (NewGovernanceActionProcessRequestBody) objectToCompare;
        return processStatus == that.processStatus &&
                       Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), processStatus, properties);
    }
}
