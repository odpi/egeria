/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * IntegrationGroupSummariesResponse provides a container for transporting the status of a collection of integration groups.
 */
public class IntegrationGroupSummariesResponse extends FFDCResponseBase
{
    @Serial
    private static final long serialVersionUID = 1L;

    private List<IntegrationGroupSummary> integrationGroupSummaries = null;


    /**
     * Default constructor
     */
    public IntegrationGroupSummariesResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationGroupSummariesResponse(IntegrationGroupSummariesResponse template)
    {
        if (template != null)
        {
            integrationGroupSummaries = template.getIntegrationGroupSummaries();
        }
    }


    /**
     * Return the summary of each integration group assigned to the integration daemon.
     *
     * @return list of summaries
     */
    public List<IntegrationGroupSummary> getIntegrationGroupSummaries()
    {
        if (integrationGroupSummaries == null)
        {
            return null;
        }
        else if (integrationGroupSummaries.isEmpty())
        {
            return null;
        }

        return integrationGroupSummaries;
    }


    /**
     * Set up the summary of each integration group assigned to the integration daemon.
     *
     * @param integrationGroupSummaries list of summaries
     */
    public void setIntegrationGroupSummaries(List<IntegrationGroupSummary> integrationGroupSummaries)
    {
        this.integrationGroupSummaries = integrationGroupSummaries;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "IntegrationGroupSummariesResponse{" +
                "integrationGroupSummaries=" + integrationGroupSummaries +
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
        IntegrationGroupSummariesResponse that = (IntegrationGroupSummariesResponse) objectToCompare;
        return Objects.equals(integrationGroupSummaries, that.integrationGroupSummaries);
    }

    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(integrationGroupSummaries);
    }
}
