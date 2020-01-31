/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.discoveryengineservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.properties.DiscoveryEngineSummary;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * DiscoveryEngineStatusResponse provides a container for transporting the status of each of the discovery
 * engines.
 */
public class DiscoveryEngineStatusResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    private List<DiscoveryEngineSummary>  discoveryEngineSummaries = null;


    /**
     * Default constructor
     */
    public DiscoveryEngineStatusResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryEngineStatusResponse(DiscoveryEngineStatusResponse template)
    {
        if (template != null)
        {
            discoveryEngineSummaries = template.getDiscoveryEngineSummaries();
        }
    }


    /**
     * Return the summary of each discovery engine assigned to the discovery server.
     *
     * @return list of summaries
     */
    public List<DiscoveryEngineSummary> getDiscoveryEngineSummaries()
    {
        if (discoveryEngineSummaries == null)
        {
            return null;
        }
        else if (discoveryEngineSummaries.isEmpty())
        {
            return null;
        }

        return discoveryEngineSummaries;
    }


    /**
     * Set up the list of summaries for the discovery engine.
     *
     * @param discoveryEngineSummaries list of summaries
     */
    public void setDiscoveryEngineSummaries(List<DiscoveryEngineSummary> discoveryEngineSummaries)
    {
        this.discoveryEngineSummaries = discoveryEngineSummaries;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "DiscoveryEngineStatusResponse{" +
                "discoveryEngineSummaries=" + discoveryEngineSummaries +
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
        DiscoveryEngineStatusResponse that = (DiscoveryEngineStatusResponse) objectToCompare;
        return Objects.equals(discoveryEngineSummaries, that.discoveryEngineSummaries);
    }

    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(discoveryEngineSummaries);
    }
}
