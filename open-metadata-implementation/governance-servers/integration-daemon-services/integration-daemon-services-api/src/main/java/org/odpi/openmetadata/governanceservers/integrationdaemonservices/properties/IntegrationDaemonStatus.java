/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationDaemonStatus provides a container for transporting the status of each of the integration services and integration groups running in an
 * integration daemon.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationDaemonStatus implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private List<IntegrationServiceSummary> integrationServiceSummaries = null;
    private List<IntegrationGroupSummary>   integrationGroupSummaries = null;


    /**
     * Default constructor
     */
    public IntegrationDaemonStatus()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationDaemonStatus(IntegrationDaemonStatus template)
    {
        if (template != null)
        {
            integrationServiceSummaries = template.getIntegrationServiceSummaries();
            integrationGroupSummaries = template.getIntegrationGroupSummaries();
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
     * Set up of each integration service running in the integration daemon.
     *
     * @param integrationServiceSummaries list of summaries
     */
    public void setIntegrationServiceSummaries(List<IntegrationServiceSummary> integrationServiceSummaries)
    {
        this.integrationServiceSummaries = integrationServiceSummaries;
    }


    /**
     * Return the summary of each integration group running in the integration daemon.
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
     * Set up of each integration group running in the integration daemon.
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
        return "IntegrationDaemonStatus{" +
                "integrationServiceSummaries=" + integrationServiceSummaries +
                ", integrationGroupSummaries=" + integrationGroupSummaries +
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
        IntegrationDaemonStatus that = (IntegrationDaemonStatus) objectToCompare;
        return Objects.equals(integrationServiceSummaries, that.integrationServiceSummaries) &&
                Objects.equals(integrationGroupSummaries, that.integrationGroupSummaries);
    }


    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), integrationServiceSummaries, getIntegrationGroupSummaries());
    }
}
