/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceEngineSummaryResponse provides a container for transporting the status of each of the governance
 * engines.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceEngineSummaryResponse extends FFDCResponseBase
{
    private GovernanceEngineSummary governanceEngineSummary = null;


    /**
     * Default constructor
     */
    public GovernanceEngineSummaryResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceEngineSummaryResponse(GovernanceEngineSummaryResponse template)
    {
        if (template != null)
        {
            governanceEngineSummary = template.getGovernanceEngineSummary();
        }
    }


    /**
     * Return the summary for the governance engine.
     *
     * @return summary for governance engine
     */
    public GovernanceEngineSummary getGovernanceEngineSummary()
    {
        return governanceEngineSummary;
    }


    /**
     * Set up the summary for the governance engine.
     *
     * @param governanceEngineSummary summary for governance engine
     */
    public void setGovernanceEngineSummary(GovernanceEngineSummary governanceEngineSummary)
    {
        this.governanceEngineSummary = governanceEngineSummary;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "GovernanceEngineSummaryResponse{" +
                "governanceEngineSummary=" + governanceEngineSummary +
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
        GovernanceEngineSummaryResponse that = (GovernanceEngineSummaryResponse) objectToCompare;
        return Objects.equals(governanceEngineSummary, that.governanceEngineSummary);
    }

    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(governanceEngineSummary);
    }
}
