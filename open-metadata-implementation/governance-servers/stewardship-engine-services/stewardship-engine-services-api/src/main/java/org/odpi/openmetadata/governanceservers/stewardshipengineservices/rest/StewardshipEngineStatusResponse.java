/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.stewardshipengineservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.stewardshipengineservices.properties.StewardshipEngineSummary;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * StewardshipEngineStatusResponse provides a container for transporting the status of each of the stewardship
 * engines.
 */
public class StewardshipEngineStatusResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    private List<StewardshipEngineSummary>  stewardshipEngineSummaries = null;


    /**
     * Default constructor
     */
    public StewardshipEngineStatusResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public StewardshipEngineStatusResponse(StewardshipEngineStatusResponse template)
    {
        if (template != null)
        {
            stewardshipEngineSummaries = template.getStewardshipEngineSummaries();
        }
    }


    /**
     * Return the summary of each stewardship engine assigned to the stewardship server.
     *
     * @return list of summaries
     */
    public List<StewardshipEngineSummary> getStewardshipEngineSummaries()
    {
        if (stewardshipEngineSummaries == null)
        {
            return null;
        }
        else if (stewardshipEngineSummaries.isEmpty())
        {
            return null;
        }

        return stewardshipEngineSummaries;
    }


    /**
     * Set up the list of summaries for the stewardship engine.
     *
     * @param stewardshipEngineSummaries list of summaries
     */
    public void setStewardshipEngineSummaries(List<StewardshipEngineSummary> stewardshipEngineSummaries)
    {
        this.stewardshipEngineSummaries = stewardshipEngineSummaries;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "StewardshipEngineStatusResponse{" +
                "stewardshipEngineSummaries=" + stewardshipEngineSummaries +
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
        StewardshipEngineStatusResponse that = (StewardshipEngineStatusResponse) objectToCompare;
        return Objects.equals(stewardshipEngineSummaries, that.stewardshipEngineSummaries);
    }

    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(stewardshipEngineSummaries);
    }
}
