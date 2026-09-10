/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the root run and job of a nested hierarchy of runs.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageParentRunFacetRoot
{
    private OpenLineageParentRunFacetRun run = null;
    private OpenLineageParentRunFacetJob job = null;


    /**
     * Default constructor
     */
    public OpenLineageParentRunFacetRoot()
    {
    }


    /**
     * Return the root run of the hierarchy.
     *
     * @return bean
     */
    public OpenLineageParentRunFacetRun getRun()
    {
        return run;
    }


    /**
     * Set up the root run of the hierarchy.
     *
     * @param run bean
     */
    public void setRun(OpenLineageParentRunFacetRun run)
    {
        this.run = run;
    }


    /**
     * Return the root job of the hierarchy.
     *
     * @return bean
     */
    public OpenLineageParentRunFacetJob getJob()
    {
        return job;
    }


    /**
     * Set up the root job of the hierarchy.
     *
     * @param job bean
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
        return "OpenLineageParentRunFacetRoot{" +
                       "run=" + run +
                       ", job=" + job +
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
        OpenLineageParentRunFacetRoot that = (OpenLineageParentRunFacetRoot) objectToCompare;
        return Objects.equals(run, that.run) &&
                       Objects.equals(job, that.job);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(run, job);
    }
}
