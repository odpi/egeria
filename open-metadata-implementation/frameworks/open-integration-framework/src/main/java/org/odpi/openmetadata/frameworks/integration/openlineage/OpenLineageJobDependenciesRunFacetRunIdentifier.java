/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the identity of a run referenced by the jobDependencies run facet.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageJobDependenciesRunFacetRunIdentifier
{
    private UUID runId = null;


    /**
     * Default constructor
     */
    public OpenLineageJobDependenciesRunFacetRunIdentifier()
    {
    }


    /**
     * Return the globally unique ID of the run.
     *
     * @return uuid
     */
    public UUID getRunId()
    {
        return runId;
    }


    /**
     * Set up the globally unique ID of the run.
     *
     * @param runId uuid
     */
    public void setRunId(UUID runId)
    {
        this.runId = runId;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageJobDependenciesRunFacetRunIdentifier{" +
                       "runId=" + runId +
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
        OpenLineageJobDependenciesRunFacetRunIdentifier that = (OpenLineageJobDependenciesRunFacetRunIdentifier) objectToCompare;
        return Objects.equals(runId, that.runId);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(runId);
    }
}
