/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationServiceSummary;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * IntegrationServiceSummaryResponse provides a container for transporting the status of each of the integration services running in an
 * integration daemon.
 */
public class IntegrationServiceSummaryResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    private List<IntegrationServiceSummary> integrationServiceSummaries = null;


    /**
     * Default constructor
     */
    public IntegrationServiceSummaryResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationServiceSummaryResponse(IntegrationServiceSummaryResponse template)
    {
        if (template != null)
        {
            integrationServiceSummaries = template.getIntegrationServiceSummaries();
        }
    }


    /**
     * Return the summary of each integration service running in the integration daemon.
     *
     * @return list of summaries
     */
    public List<IntegrationServiceSummary> getIntegrationServiceSummaries()
    {
        if (integrationServiceSummaries == null)
        {
            return null;
        }
        else if (integrationServiceSummaries.isEmpty())
        {
            return null;
        }

        return integrationServiceSummaries;
    }


    /**
     * Set up the summary of each integration service running in the integration daemon.
     *
     * @param integrationServiceSummaries list of summaries
     */
    public void setIntegrationServiceSummaries(List<IntegrationServiceSummary> integrationServiceSummaries)
    {
        this.integrationServiceSummaries = integrationServiceSummaries;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "IntegrationServiceSummaryResponse{" +
                "integrationServiceSummaries=" + integrationServiceSummaries +
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
        IntegrationServiceSummaryResponse that = (IntegrationServiceSummaryResponse) objectToCompare;
        return Objects.equals(integrationServiceSummaries, that.integrationServiceSummaries);
    }


    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), integrationServiceSummaries);
    }
}
