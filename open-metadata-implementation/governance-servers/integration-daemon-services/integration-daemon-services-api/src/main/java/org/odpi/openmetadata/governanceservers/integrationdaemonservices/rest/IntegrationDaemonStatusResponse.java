/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationDaemonStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationServiceSummary;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * IntegrationDaemonStatusResponse provides a container for transporting the status of each of the integration services running in an
 * integration daemon.
 */
public class IntegrationDaemonStatusResponse extends FFDCResponseBase
{
    @Serial
    private static final long serialVersionUID = 1L;

    private IntegrationDaemonStatus integrationDaemonStatus = null;


    /**
     * Default constructor
     */
    public IntegrationDaemonStatusResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationDaemonStatusResponse(IntegrationDaemonStatusResponse template)
    {
        if (template != null)
        {
            integrationDaemonStatus = template.getIntegrationDaemonStatus();
        }
    }


    /**
     * Return the summary of the services and groups running in the integration daemon.
     *
     * @return list of summaries
     */
    public IntegrationDaemonStatus getIntegrationDaemonStatus()
    {
        return integrationDaemonStatus;
    }


    /**
     * Set up the summary of the services and groups running in the integration daemon.
     *
     * @param integrationDaemonStatus list of summaries
     */
    public void setIntegrationDaemonStatus(IntegrationDaemonStatus integrationDaemonStatus)
    {
        this.integrationDaemonStatus = integrationDaemonStatus;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "IntegrationDaemonStatusResponse{" +
                "integrationServiceSummaries=" + integrationDaemonStatus +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }


    /**
     * Compare objects
     *
     * @param objectToCompare object
     * @return boolean
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
        IntegrationDaemonStatusResponse that = (IntegrationDaemonStatusResponse) objectToCompare;
        return Objects.equals(integrationDaemonStatus, that.integrationDaemonStatus);
    }


    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), integrationDaemonStatus);
    }
}
