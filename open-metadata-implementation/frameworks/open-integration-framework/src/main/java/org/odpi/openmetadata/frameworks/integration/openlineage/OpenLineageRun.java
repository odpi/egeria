/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the content of an open lineage run event as defined in JSON
 * spec https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.  It is used internally in Egeria to pass this information
 * to the integration daemon's integration connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageRun
{
    private UUID                 runId;
    private OpenLineageRunFacets facets;
    private Map<String, Object>  additionalProperties = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageRun()
    {
    }


    /**
     * Return the unique identifier of the job run.
     *
     * @return uuid
     */
    public UUID getRunId()
    {
        return runId;
    }


    /**
     * Set up the unique identifier of the job run.
     *
     * @param runId UUID value
     */
    public void setRunId(UUID runId)
    {
        this.runId = runId;
    }


    /**
     * Return the map of facets describing the run of the job.
     *
     * @return run facets object
     */
    public OpenLineageRunFacets getFacets()
    {
        return facets;
    }


    /**
     * Set up the map of facets describing the run of the job.
     *
     * @param facets run facets object
     */
    public void setFacets(OpenLineageRunFacets facets)
    {
        this.facets = facets;
    }


    /**
     * Return a map of additional custom facets.  The name is the identifier of the facet type and the object is the facet itself.
     *
     * @return custom facet map (map from string to object)
     */
    public Map<String, Object> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up a map of additional custom facets.  The name is the identifier of the facet type and the object is the facet itself.
     *
     * @param additionalProperties custom facet map (map from string to object)
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageRun{" +
                       "runId=" + runId +
                       ", facets=" + facets +
                       ", additionalProperties=" + additionalProperties +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        OpenLineageRun that = (OpenLineageRun) objectToCompare;
        return Objects.equals(runId, that.runId) &&
                       Objects.equals(facets, that.facets) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(runId, facets, additionalProperties);
    }
}
