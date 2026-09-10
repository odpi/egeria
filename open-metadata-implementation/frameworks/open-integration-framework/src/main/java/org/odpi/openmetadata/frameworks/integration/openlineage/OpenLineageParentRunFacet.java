/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the parent run facet.  It links a run to the parent run (and optionally the root run) that spawned it.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-2-0/ParentRunFacet.json#/$defs/ParentRunFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageParentRunFacet extends OpenLineageRunFacet
{
    private OpenLineageParentRunFacetRun  run = null;
    private OpenLineageParentRunFacetJob  job = null;
    private OpenLineageParentRunFacetRoot root = null;


    /**
     * Default constructor
     */
    public OpenLineageParentRunFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-2-0/ParentRunFacet.json#/$defs/ParentRunFacet"));
    }


    /**
     * Return the parent run.
     *
     * @return bean
     */
    public OpenLineageParentRunFacetRun getRun()
    {
        return run;
    }


    /**
     * Set up the parent run.
     *
     * @param run bean
     */
    public void setRun(OpenLineageParentRunFacetRun run)
    {
        this.run = run;
    }


    /**
     * Return the parent job.
     *
     * @return bean
     */
    public OpenLineageParentRunFacetJob getJob()
    {
        return job;
    }


    /**
     * Set up the parent job.
     *
     * @param job bean
     */
    public void setJob(OpenLineageParentRunFacetJob job)
    {
        this.job = job;
    }


    /**
     * Return the root run and job of the hierarchy.
     *
     * @return bean
     */
    public OpenLineageParentRunFacetRoot getRoot()
    {
        return root;
    }


    /**
     * Set up the root run and job of the hierarchy.
     *
     * @param root bean
     */
    public void setRoot(OpenLineageParentRunFacetRoot root)
    {
        this.root = root;
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
                       ", root=" + root +
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
                       Objects.equals(job, that.job) &&
                       Objects.equals(root, that.root);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), run, job, root);
    }
}
