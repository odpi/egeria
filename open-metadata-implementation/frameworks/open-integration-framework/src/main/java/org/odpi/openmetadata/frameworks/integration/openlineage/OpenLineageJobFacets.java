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
 * This class represents the map of job facets for a job.  The standard facets from the OpenLineage spec, and the custom facets
 * registered in the OpenLineage registry, are held in named properties.  Any other facets (custom facets, or standard facets not yet modelled) are held in the
 * additionalProperties map, keyed by their facet name, so that nothing is lost on a round trip through these beans.
 * The map key of each facet in the JSON is the facet's key from the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageJobFacets
{
    private OpenLineageDocumentationJobFacet      documentation = null;
    private OpenLineageSQLJobFacet                sql = null;
    private OpenLineageSourceCodeLocationJobFacet sourceCodeLocation = null;
    private OpenLineageSourceCodeJobFacet         sourceCode = null;
    private OpenLineageJobTypeJobFacet            jobType = null;
    private OpenLineageOwnershipJobFacet          ownership = null;
    private OpenLineageTagsJobFacet               tags = null;
    private OpenLineageLineageJobFacet            lineage = null;
    private OpenLineageGcpComposerJobFacet        gcpComposerJob = null;
    private OpenLineageGcpLineageJobFacet         gcpLineage = null;
    private Map<String, OpenLineageJobFacet>      additionalProperties = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageJobFacets()
    {
    }


    /**
     * Return the documentation facet.
     *
     * @return facet bean
     */
    public OpenLineageDocumentationJobFacet getDocumentation()
    {
        return documentation;
    }


    /**
     * Set up the documentation facet.
     *
     * @param documentation facet bean
     */
    public void setDocumentation(OpenLineageDocumentationJobFacet documentation)
    {
        this.documentation = documentation;
    }


    /**
     * Return the sql facet.
     *
     * @return facet bean
     */
    public OpenLineageSQLJobFacet getSql()
    {
        return sql;
    }


    /**
     * Set up the sql facet.
     *
     * @param sql facet bean
     */
    public void setSql(OpenLineageSQLJobFacet sql)
    {
        this.sql = sql;
    }


    /**
     * Return the sourceCodeLocation facet.
     *
     * @return facet bean
     */
    public OpenLineageSourceCodeLocationJobFacet getSourceCodeLocation()
    {
        return sourceCodeLocation;
    }


    /**
     * Set up the sourceCodeLocation facet.
     *
     * @param sourceCodeLocation facet bean
     */
    public void setSourceCodeLocation(OpenLineageSourceCodeLocationJobFacet sourceCodeLocation)
    {
        this.sourceCodeLocation = sourceCodeLocation;
    }


    /**
     * Return the sourceCode facet.
     *
     * @return facet bean
     */
    public OpenLineageSourceCodeJobFacet getSourceCode()
    {
        return sourceCode;
    }


    /**
     * Set up the sourceCode facet.
     *
     * @param sourceCode facet bean
     */
    public void setSourceCode(OpenLineageSourceCodeJobFacet sourceCode)
    {
        this.sourceCode = sourceCode;
    }


    /**
     * Return the jobType facet.
     *
     * @return facet bean
     */
    public OpenLineageJobTypeJobFacet getJobType()
    {
        return jobType;
    }


    /**
     * Set up the jobType facet.
     *
     * @param jobType facet bean
     */
    public void setJobType(OpenLineageJobTypeJobFacet jobType)
    {
        this.jobType = jobType;
    }


    /**
     * Return the ownership facet.
     *
     * @return facet bean
     */
    public OpenLineageOwnershipJobFacet getOwnership()
    {
        return ownership;
    }


    /**
     * Set up the ownership facet.
     *
     * @param ownership facet bean
     */
    public void setOwnership(OpenLineageOwnershipJobFacet ownership)
    {
        this.ownership = ownership;
    }


    /**
     * Return the tags facet.
     *
     * @return facet bean
     */
    public OpenLineageTagsJobFacet getTags()
    {
        return tags;
    }


    /**
     * Set up the tags facet.
     *
     * @param tags facet bean
     */
    public void setTags(OpenLineageTagsJobFacet tags)
    {
        this.tags = tags;
    }


    /**
     * Return the lineage facet.
     *
     * @return facet bean
     */
    public OpenLineageLineageJobFacet getLineage()
    {
        return lineage;
    }


    /**
     * Set up the lineage facet.
     *
     * @param lineage facet bean
     */
    public void setLineage(OpenLineageLineageJobFacet lineage)
    {
        this.lineage = lineage;
    }


    /**
     * Return the gcp_composer_job facet.
     *
     * @return facet bean
     */
    @JsonProperty("gcp_composer_job")
    public OpenLineageGcpComposerJobFacet getGcpComposerJob()
    {
        return gcpComposerJob;
    }


    /**
     * Set up the gcp_composer_job facet.
     *
     * @param gcpComposerJob facet bean
     */
    @JsonProperty("gcp_composer_job")
    public void setGcpComposerJob(OpenLineageGcpComposerJobFacet gcpComposerJob)
    {
        this.gcpComposerJob = gcpComposerJob;
    }


    /**
     * Return the gcp_lineage facet.
     *
     * @return facet bean
     */
    @JsonProperty("gcp_lineage")
    public OpenLineageGcpLineageJobFacet getGcpLineage()
    {
        return gcpLineage;
    }


    /**
     * Set up the gcp_lineage facet.
     *
     * @param gcpLineage facet bean
     */
    @JsonProperty("gcp_lineage")
    public void setGcpLineage(OpenLineageGcpLineageJobFacet gcpLineage)
    {
        this.gcpLineage = gcpLineage;
    }


    /**
     * Return any additional facets that are not modelled as named properties.  They are serialized as
     * top-level facet entries alongside the named facets.
     *
     * @return map of facet name to facet
     */
    @JsonAnyGetter
    public Map<String, OpenLineageJobFacet> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any additional facets that are not modelled as named properties.
     *
     * @param additionalProperties map of facet name to facet
     */
    public void setAdditionalProperties(Map<String, OpenLineageJobFacet> additionalProperties)
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
    public void setAdditionalProperty(String facetName, OpenLineageJobFacet facet)
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
        return "OpenLineageJobFacets{" +
                       "documentation=" + documentation +
                       ", sql=" + sql +
                       ", sourceCodeLocation=" + sourceCodeLocation +
                       ", sourceCode=" + sourceCode +
                       ", jobType=" + jobType +
                       ", ownership=" + ownership +
                       ", tags=" + tags +
                       ", lineage=" + lineage +
                       ", gcpComposerJob=" + gcpComposerJob +
                       ", gcpLineage=" + gcpLineage +
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
        OpenLineageJobFacets that = (OpenLineageJobFacets) objectToCompare;
        return Objects.equals(documentation, that.documentation) &&
                       Objects.equals(sql, that.sql) &&
                       Objects.equals(sourceCodeLocation, that.sourceCodeLocation) &&
                       Objects.equals(sourceCode, that.sourceCode) &&
                       Objects.equals(jobType, that.jobType) &&
                       Objects.equals(ownership, that.ownership) &&
                       Objects.equals(tags, that.tags) &&
                       Objects.equals(lineage, that.lineage) &&
                       Objects.equals(gcpComposerJob, that.gcpComposerJob) &&
                       Objects.equals(gcpLineage, that.gcpLineage) &&
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
        return Objects.hash(documentation, sql, sourceCodeLocation, sourceCode, jobType, ownership, tags, lineage, gcpComposerJob, gcpLineage, additionalProperties);
    }
}
