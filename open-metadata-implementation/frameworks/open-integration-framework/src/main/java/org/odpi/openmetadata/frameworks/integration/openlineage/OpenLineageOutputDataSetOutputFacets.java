/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the map of output dataset facets for an output dataset.  The standard facets from the OpenLineage spec, and the custom facets
 * registered in the OpenLineage registry, are held in named properties.  Any other facets (custom facets, or standard facets not yet modelled) are held in the
 * additionalProperties map, keyed by their facet name, so that nothing is lost on a round trip through these beans.
 * The map key of each facet in the JSON is the facet's key from the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageOutputDataSetOutputFacets
{
    private OpenLineageOutputStatisticsOutputDataSetFacet    outputStatistics = null;
    private OpenLineageOutputSubsetOutputDataSetFacet        subset = null;
    private OpenLineageIcebergCommitReportOutputDataSetFacet icebergCommitReport = null;
    private Map<String, OpenLineageOutputDataSetOutputFacet> additionalProperties = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageOutputDataSetOutputFacets()
    {
    }


    /**
     * Return the outputStatistics facet.
     *
     * @return facet bean
     */
    public OpenLineageOutputStatisticsOutputDataSetFacet getOutputStatistics()
    {
        return outputStatistics;
    }


    /**
     * Set up the outputStatistics facet.
     *
     * @param outputStatistics facet bean
     */
    public void setOutputStatistics(OpenLineageOutputStatisticsOutputDataSetFacet outputStatistics)
    {
        this.outputStatistics = outputStatistics;
    }


    /**
     * Return the subset facet.
     *
     * @return facet bean
     */
    public OpenLineageOutputSubsetOutputDataSetFacet getSubset()
    {
        return subset;
    }


    /**
     * Set up the subset facet.
     *
     * @param subset facet bean
     */
    public void setSubset(OpenLineageOutputSubsetOutputDataSetFacet subset)
    {
        this.subset = subset;
    }


    /**
     * Return the icebergCommitReport facet.
     *
     * @return facet bean
     */
    public OpenLineageIcebergCommitReportOutputDataSetFacet getIcebergCommitReport()
    {
        return icebergCommitReport;
    }


    /**
     * Set up the icebergCommitReport facet.
     *
     * @param icebergCommitReport facet bean
     */
    public void setIcebergCommitReport(OpenLineageIcebergCommitReportOutputDataSetFacet icebergCommitReport)
    {
        this.icebergCommitReport = icebergCommitReport;
    }


    /**
     * Return any additional facets that are not modelled as named properties.  They are serialized as
     * top-level facet entries alongside the named facets.
     *
     * @return map of facet name to facet
     */
    @JsonAnyGetter
    public Map<String, OpenLineageOutputDataSetOutputFacet> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any additional facets that are not modelled as named properties.
     *
     * @param additionalProperties map of facet name to facet
     */
    public void setAdditionalProperties(Map<String, OpenLineageOutputDataSetOutputFacet> additionalProperties)
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
    public void setAdditionalProperty(String facetName, OpenLineageOutputDataSetOutputFacet facet)
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
        return "OpenLineageOutputDataSetOutputFacets{" +
                       "outputStatistics=" + outputStatistics +
                       ", subset=" + subset +
                       ", icebergCommitReport=" + icebergCommitReport +
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
        OpenLineageOutputDataSetOutputFacets that = (OpenLineageOutputDataSetOutputFacets) objectToCompare;
        return Objects.equals(outputStatistics, that.outputStatistics) &&
                       Objects.equals(subset, that.subset) &&
                       Objects.equals(icebergCommitReport, that.icebergCommitReport) &&
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
        return Objects.hash(outputStatistics, subset, icebergCommitReport, additionalProperties);
    }
}
