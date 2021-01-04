/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * GovernanceEngineSummariesResponse provides a container for transporting the status of a collection of governance engines.
 */
public class GovernanceEngineSummariesResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    private List<GovernanceEngineSummary> governanceEngineSummaries = null;


    /**
     * Default constructor
     */
    public GovernanceEngineSummariesResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceEngineSummariesResponse(GovernanceEngineSummariesResponse template)
    {
        if (template != null)
        {
            governanceEngineSummaries = template.getGovernanceEngineSummaries();
        }
    }


    /**
     * Return the summary of each governance engine assigned to the governance server.
     *
     * @return list of summaries
     */
    public List<GovernanceEngineSummary> getGovernanceEngineSummaries()
    {
        if (governanceEngineSummaries == null)
        {
            return null;
        }
        else if (governanceEngineSummaries.isEmpty())
        {
            return null;
        }

        return governanceEngineSummaries;
    }


    /**
     * Set up the list of summaries for the governance engine.
     *
     * @param governanceEngineSummaries list of summaries
     */
    public void setGovernanceEngineSummaries(List<GovernanceEngineSummary> governanceEngineSummaries)
    {
        this.governanceEngineSummaries = governanceEngineSummaries;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "GovernanceEngineSummariesResponse{" +
                "governanceEngineSummaries=" + governanceEngineSummaries +
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
        GovernanceEngineSummariesResponse that = (GovernanceEngineSummariesResponse) objectToCompare;
        return Objects.equals(governanceEngineSummaries, that.governanceEngineSummaries);
    }

    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(governanceEngineSummaries);
    }
}
