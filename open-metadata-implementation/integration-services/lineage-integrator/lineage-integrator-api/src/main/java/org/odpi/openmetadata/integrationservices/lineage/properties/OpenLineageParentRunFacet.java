/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the content of an open lineage schema for a parent run facet as defined in JSON
 * spec https://openlineage.io/spec/facets/1-0-0/ParentRunFacet.json#/$defs/ParentRunFacet.
 * It is used internally in Egeria to pass this information to the Lineage Integrator OMIS's integration connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageParentRunFacet extends OpenLineageDataSetFacet
{
    private OpenLineageParentRunFacetRun run = null;
    private OpenLineageParentRunFacetJob job = null;


    /**
     * Default constructor
     */
    public OpenLineageParentRunFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/ParentRunFacet.json#/$defs/ParentRunFacet"));
    }


    /**
     * Return details of the parent process's run.
     *
     * @return run information
     */
    public OpenLineageParentRunFacetRun getRun()
    {
        return run;
    }


    /**
     * Set up details of the parent process's run.
     *
     * @param run run information
     */
    public void setRun(OpenLineageParentRunFacetRun run)
    {
        this.run = run;
    }


    /**
     * Return details of the parent process.
     *
     * @return job information
     */
    public OpenLineageParentRunFacetJob getJob()
    {
        return job;
    }


    /**
     * Set up details of the parent process.
     *
     * @param job job information
     */
    public void setJob(OpenLineageParentRunFacetJob job)
    {
        this.job = job;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageParentRunFacet{" +
                       "run=" + run +
                       ", job=" + job +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        OpenLineageParentRunFacet that = (OpenLineageParentRunFacet) objectToCompare;
        return Objects.equals(run, that.run) &&
                       Objects.equals(job, that.job);
    }


    /**
     * Return hash code basa``ed on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), run, job);
    }
}
