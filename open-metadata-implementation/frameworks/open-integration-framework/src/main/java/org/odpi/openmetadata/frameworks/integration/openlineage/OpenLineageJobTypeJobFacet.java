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
 * This class represents the jobType job facet.  It classifies the job by processing type, integration and job type.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/2-0-4/JobTypeJobFacet.json#/$defs/JobTypeJobFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageJobTypeJobFacet extends OpenLineageJobFacet
{
    private String                                    processingType = null;
    private String                                    integration = null;
    private String                                    jobType = null;
    private OpenLineageJobTypeJobFacetEmissionPattern emissionPattern = null;


    /**
     * Default constructor
     */
    public OpenLineageJobTypeJobFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/2-0-4/JobTypeJobFacet.json#/$defs/JobTypeJobFacet"));
    }


    /**
     * Return the job processing type: BATCH, STREAMING or NONE.
     *
     * @return string
     */
    public String getProcessingType()
    {
        return processingType;
    }


    /**
     * Set up the job processing type: BATCH, STREAMING or NONE.
     *
     * @param processingType string
     */
    public void setProcessingType(String processingType)
    {
        this.processingType = processingType;
    }


    /**
     * Return the OpenLineage integration type of this job, for example SPARK, DBT, AIRFLOW or FLINK.
     *
     * @return string
     */
    public String getIntegration()
    {
        return integration;
    }


    /**
     * Set up the OpenLineage integration type of this job, for example SPARK, DBT, AIRFLOW or FLINK.
     *
     * @param integration string
     */
    public void setIntegration(String integration)
    {
        this.integration = integration;
    }


    /**
     * Return the run type, for example QUERY, COMMAND, DAG, TASK, JOB or MODEL.  This is an integration-specific field.
     *
     * @return string
     */
    public String getJobType()
    {
        return jobType;
    }


    /**
     * Set up the run type, for example QUERY, COMMAND, DAG, TASK, JOB or MODEL.  This is an integration-specific field.
     *
     * @param jobType string
     */
    public void setJobType(String jobType)
    {
        this.jobType = jobType;
    }


    /**
     * Return the how and what the job emits in its events.
     *
     * @return bean
     */
    public OpenLineageJobTypeJobFacetEmissionPattern getEmissionPattern()
    {
        return emissionPattern;
    }


    /**
     * Set up the how and what the job emits in its events.
     *
     * @param emissionPattern bean
     */
    public void setEmissionPattern(OpenLineageJobTypeJobFacetEmissionPattern emissionPattern)
    {
        this.emissionPattern = emissionPattern;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageJobTypeJobFacet{" +
                       "processingType='" + processingType + '\'' +
                       ", integration='" + integration + '\'' +
                       ", jobType='" + jobType + '\'' +
                       ", emissionPattern=" + emissionPattern +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", _deleted=" + get_deleted() +
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
        OpenLineageJobTypeJobFacet that = (OpenLineageJobTypeJobFacet) objectToCompare;
        return Objects.equals(processingType, that.processingType) &&
                       Objects.equals(integration, that.integration) &&
                       Objects.equals(jobType, that.jobType) &&
                       Objects.equals(emissionPattern, that.emissionPattern);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), processingType, integration, jobType, emissionPattern);
    }
}
