/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * IntegrationGroupSummaryResponse provides a container for transporting the status of an integration group.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationGroupSummaryResponse extends FFDCResponseBase
{
    @Serial
    private static final long serialVersionUID = 1L;

    private IntegrationGroupSummary integrationGroupSummary = null;


    /**
     * Default constructor
     */
    public IntegrationGroupSummaryResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationGroupSummaryResponse(IntegrationGroupSummaryResponse template)
    {
        if (template != null)
        {
            integrationGroupSummary = template.getIntegrationGroupSummary();
        }
    }


    /**
     * Return the summary for the integration group.
     *
     * @return summary for the integration group
     */
    public IntegrationGroupSummary getIntegrationGroupSummary()
    {
        return integrationGroupSummary;
    }


    /**
     * Set up the summary for the integration group.
     *
     * @param integrationGroupSummary summary for integration group
     */
    public void setIntegrationGroupSummary(IntegrationGroupSummary integrationGroupSummary)
    {
        this.integrationGroupSummary = integrationGroupSummary;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "IntegrationGroupSummaryResponse{" +
                "governanceEngineSummary=" + integrationGroupSummary +
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
        IntegrationGroupSummaryResponse that = (IntegrationGroupSummaryResponse) objectToCompare;
        return Objects.equals(integrationGroupSummary, that.integrationGroupSummary);
    }

    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(integrationGroupSummary);
    }
}
