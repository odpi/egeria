/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the map of run facets for a run.  The standard facets from the OpenLineage spec, and the custom facets
 * registered in the OpenLineage registry, are held in named properties.  Any other facets (custom facets, or standard facets not yet modelled) are held in the
 * additionalProperties map, keyed by their facet name, so that nothing is lost on a round trip through these beans.
 * The map key of each facet in the JSON is the facet's key from the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageRunFacets
{
    private OpenLineageParentRunFacet               parent = null;
    private OpenLineageNominalTimeRunFacet          nominalTime = null;
    private OpenLineageEnvironmentVariablesRunFacet environmentVariables = null;
    private OpenLineageErrorMessageRunFacet         errorMessage = null;
    private OpenLineageExecutionParametersRunFacet  executionParameters = null;
    private OpenLineageExternalQueryRunFacet        externalQuery = null;
    private OpenLineageExtractionErrorRunFacet      extractionError = null;
    private OpenLineageJobDependenciesRunFacet      jobDependencies = null;
    private OpenLineageProcessingEngineRunFacet     processingEngine = null;
    private OpenLineageTagsRunFacet                 tags = null;
    private OpenLineageTestRunFacet                 test = null;
    private OpenLineageGcpComposerRunFacet          gcpComposerRun = null;
    private OpenLineageGcpDataprocRunFacet          gcpDataproc = null;
    private Map<String, OpenLineageRunFacet>        additionalProperties = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageRunFacets()
    {
    }


    /**
     * Return the parent facet.
     *
     * @return facet bean
     */
    public OpenLineageParentRunFacet getParent()
    {
        return parent;
    }


    /**
     * Set up the parent facet.
     *
     * @param parent facet bean
     */
    public void setParent(OpenLineageParentRunFacet parent)
    {
        this.parent = parent;
    }


    /**
     * Return the nominalTime facet.
     *
     * @return facet bean
     */
    public OpenLineageNominalTimeRunFacet getNominalTime()
    {
        return nominalTime;
    }


    /**
     * Set up the nominalTime facet.
     *
     * @param nominalTime facet bean
     */
    public void setNominalTime(OpenLineageNominalTimeRunFacet nominalTime)
    {
        this.nominalTime = nominalTime;
    }


    /**
     * Return the environmentVariables facet.
     *
     * @return facet bean
     */
    public OpenLineageEnvironmentVariablesRunFacet getEnvironmentVariables()
    {
        return environmentVariables;
    }


    /**
     * Set up the environmentVariables facet.
     *
     * @param environmentVariables facet bean
     */
    public void setEnvironmentVariables(OpenLineageEnvironmentVariablesRunFacet environmentVariables)
    {
        this.environmentVariables = environmentVariables;
    }


    /**
     * Return the errorMessage facet.
     *
     * @return facet bean
     */
    public OpenLineageErrorMessageRunFacet getErrorMessage()
    {
        return errorMessage;
    }


    /**
     * Set up the errorMessage facet.
     *
     * @param errorMessage facet bean
     */
    public void setErrorMessage(OpenLineageErrorMessageRunFacet errorMessage)
    {
        this.errorMessage = errorMessage;
    }


    /**
     * Return the executionParameters facet.
     *
     * @return facet bean
     */
    public OpenLineageExecutionParametersRunFacet getExecutionParameters()
    {
        return executionParameters;
    }


    /**
     * Set up the executionParameters facet.
     *
     * @param executionParameters facet bean
     */
    public void setExecutionParameters(OpenLineageExecutionParametersRunFacet executionParameters)
    {
        this.executionParameters = executionParameters;
    }


    /**
     * Return the externalQuery facet.
     *
     * @return facet bean
     */
    public OpenLineageExternalQueryRunFacet getExternalQuery()
    {
        return externalQuery;
    }


    /**
     * Set up the externalQuery facet.
     *
     * @param externalQuery facet bean
     */
    public void setExternalQuery(OpenLineageExternalQueryRunFacet externalQuery)
    {
        this.externalQuery = externalQuery;
    }


    /**
     * Return the extractionError facet.
     *
     * @return facet bean
     */
    public OpenLineageExtractionErrorRunFacet getExtractionError()
    {
        return extractionError;
    }


    /**
     * Set up the extractionError facet.
     *
     * @param extractionError facet bean
     */
    public void setExtractionError(OpenLineageExtractionErrorRunFacet extractionError)
    {
        this.extractionError = extractionError;
    }


    /**
     * Return the jobDependencies facet.
     *
     * @return facet bean
     */
    public OpenLineageJobDependenciesRunFacet getJobDependencies()
    {
        return jobDependencies;
    }


    /**
     * Set up the jobDependencies facet.
     *
     * @param jobDependencies facet bean
     */
    public void setJobDependencies(OpenLineageJobDependenciesRunFacet jobDependencies)
    {
        this.jobDependencies = jobDependencies;
    }


    /**
     * Return the processing_engine facet.
     *
     * @return facet bean
     */
    @JsonProperty("processing_engine")
    public OpenLineageProcessingEngineRunFacet getProcessingEngine()
    {
        return processingEngine;
    }


    /**
     * Set up the processing_engine facet.
     *
     * @param processingEngine facet bean
     */
    @JsonProperty("processing_engine")
    public void setProcessingEngine(OpenLineageProcessingEngineRunFacet processingEngine)
    {
        this.processingEngine = processingEngine;
    }


    /**
     * Return the tags facet.
     *
     * @return facet bean
     */
    public OpenLineageTagsRunFacet getTags()
    {
        return tags;
    }


    /**
     * Set up the tags facet.
     *
     * @param tags facet bean
     */
    public void setTags(OpenLineageTagsRunFacet tags)
    {
        this.tags = tags;
    }


    /**
     * Return the test facet.
     *
     * @return facet bean
     */
    public OpenLineageTestRunFacet getTest()
    {
        return test;
    }


    /**
     * Set up the test facet.
     *
     * @param test facet bean
     */
    public void setTest(OpenLineageTestRunFacet test)
    {
        this.test = test;
    }


    /**
     * Return the gcp_composer_run facet.
     *
     * @return facet bean
     */
    @JsonProperty("gcp_composer_run")
    public OpenLineageGcpComposerRunFacet getGcpComposerRun()
    {
        return gcpComposerRun;
    }


    /**
     * Set up the gcp_composer_run facet.
     *
     * @param gcpComposerRun facet bean
     */
    @JsonProperty("gcp_composer_run")
    public void setGcpComposerRun(OpenLineageGcpComposerRunFacet gcpComposerRun)
    {
        this.gcpComposerRun = gcpComposerRun;
    }


    /**
     * Return the gcp_dataproc facet.
     *
     * @return facet bean
     */
    @JsonProperty("gcp_dataproc")
    public OpenLineageGcpDataprocRunFacet getGcpDataproc()
    {
        return gcpDataproc;
    }


    /**
     * Set up the gcp_dataproc facet.
     *
     * @param gcpDataproc facet bean
     */
    @JsonProperty("gcp_dataproc")
    public void setGcpDataproc(OpenLineageGcpDataprocRunFacet gcpDataproc)
    {
        this.gcpDataproc = gcpDataproc;
    }


    /**
     * Return any additional facets that are not modelled as named properties.  They are serialized as
     * top-level facet entries alongside the named facets.
     *
     * @return map of facet name to facet
     */
    @JsonAnyGetter
    public Map<String, OpenLineageRunFacet> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any additional facets that are not modelled as named properties.
     *
     * @param additionalProperties map of facet name to facet
     */
    public void setAdditionalProperties(Map<String, OpenLineageRunFacet> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Add a facet that is not modelled as a named property.  Jackson calls this for each unrecognized facet
     * found in the JSON.
     *
     * @param facetName name (key) of the facet
     * @param facet facet bean
     */
    @JsonAnySetter
    public void setAdditionalProperty(String facetName, OpenLineageRunFacet facet)
    {
        if (additionalProperties == null)
        {
            additionalProperties = new LinkedHashMap<>();
        }
        additionalProperties.put(facetName, facet);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageRunFacets{" +
                       "parent=" + parent +
                       ", nominalTime=" + nominalTime +
                       ", environmentVariables=" + environmentVariables +
                       ", errorMessage=" + errorMessage +
                       ", executionParameters=" + executionParameters +
                       ", externalQuery=" + externalQuery +
                       ", extractionError=" + extractionError +
                       ", jobDependencies=" + jobDependencies +
                       ", processingEngine=" + processingEngine +
                       ", tags=" + tags +
                       ", test=" + test +
                       ", gcpComposerRun=" + gcpComposerRun +
                       ", gcpDataproc=" + gcpDataproc +
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
        OpenLineageRunFacets that = (OpenLineageRunFacets) objectToCompare;
        return Objects.equals(parent, that.parent) &&
                       Objects.equals(nominalTime, that.nominalTime) &&
                       Objects.equals(environmentVariables, that.environmentVariables) &&
                       Objects.equals(errorMessage, that.errorMessage) &&
                       Objects.equals(executionParameters, that.executionParameters) &&
                       Objects.equals(externalQuery, that.externalQuery) &&
                       Objects.equals(extractionError, that.extractionError) &&
                       Objects.equals(jobDependencies, that.jobDependencies) &&
                       Objects.equals(processingEngine, that.processingEngine) &&
                       Objects.equals(tags, that.tags) &&
                       Objects.equals(test, that.test) &&
                       Objects.equals(gcpComposerRun, that.gcpComposerRun) &&
                       Objects.equals(gcpDataproc, that.gcpDataproc) &&
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
        return Objects.hash(parent, nominalTime, environmentVariables, errorMessage, executionParameters, externalQuery, extractionError, jobDependencies, processingEngine, tags, test, gcpComposerRun, gcpDataproc, additionalProperties);
    }
}
