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
 * This class represents the map of input dataset facets for an input dataset.  The standard facets from the OpenLineage spec, and the custom facets
 * registered in the OpenLineage registry, are held in named properties.  Any other facets (custom facets, or standard facets not yet modelled) are held in the
 * additionalProperties map, keyed by their facet name, so that nothing is lost on a round trip through these beans.
 * The map key of each facet in the JSON is the facet's key from the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageInputDataSetInputFacets
{
    private OpenLineageDataQualityAssertionsInputDataSetFacet dataQualityAssertions = null;
    private OpenLineageDataQualityMetricsInputDataSetFacet    dataQualityMetrics = null;
    private OpenLineageInputStatisticsInputDataSetFacet       inputStatistics = null;
    private OpenLineageInputSubsetInputDataSetFacet           subset = null;
    private OpenLineageIcebergScanReportInputDataSetFacet     icebergScanReport = null;
    private Map<String, OpenLineageInputDataSetInputFacet>    additionalProperties = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageInputDataSetInputFacets()
    {
    }


    /**
     * Return the dataQualityAssertions facet.
     *
     * @return facet bean
     */
    public OpenLineageDataQualityAssertionsInputDataSetFacet getDataQualityAssertions()
    {
        return dataQualityAssertions;
    }


    /**
     * Set up the dataQualityAssertions facet.
     *
     * @param dataQualityAssertions facet bean
     */
    public void setDataQualityAssertions(OpenLineageDataQualityAssertionsInputDataSetFacet dataQualityAssertions)
    {
        this.dataQualityAssertions = dataQualityAssertions;
    }


    /**
     * Return the dataQualityMetrics facet.
     *
     * @return facet bean
     */
    public OpenLineageDataQualityMetricsInputDataSetFacet getDataQualityMetrics()
    {
        return dataQualityMetrics;
    }


    /**
     * Set up the dataQualityMetrics facet.
     *
     * @param dataQualityMetrics facet bean
     */
    public void setDataQualityMetrics(OpenLineageDataQualityMetricsInputDataSetFacet dataQualityMetrics)
    {
        this.dataQualityMetrics = dataQualityMetrics;
    }


    /**
     * Return the inputStatistics facet.
     *
     * @return facet bean
     */
    public OpenLineageInputStatisticsInputDataSetFacet getInputStatistics()
    {
        return inputStatistics;
    }


    /**
     * Set up the inputStatistics facet.
     *
     * @param inputStatistics facet bean
     */
    public void setInputStatistics(OpenLineageInputStatisticsInputDataSetFacet inputStatistics)
    {
        this.inputStatistics = inputStatistics;
    }


    /**
     * Return the subset facet.
     *
     * @return facet bean
     */
    public OpenLineageInputSubsetInputDataSetFacet getSubset()
    {
        return subset;
    }


    /**
     * Set up the subset facet.
     *
     * @param subset facet bean
     */
    public void setSubset(OpenLineageInputSubsetInputDataSetFacet subset)
    {
        this.subset = subset;
    }


    /**
     * Return the icebergScanReport facet.
     *
     * @return facet bean
     */
    public OpenLineageIcebergScanReportInputDataSetFacet getIcebergScanReport()
    {
        return icebergScanReport;
    }


    /**
     * Set up the icebergScanReport facet.
     *
     * @param icebergScanReport facet bean
     */
    public void setIcebergScanReport(OpenLineageIcebergScanReportInputDataSetFacet icebergScanReport)
    {
        this.icebergScanReport = icebergScanReport;
    }


    /**
     * Return any additional facets that are not modelled as named properties.  They are serialized as
     * top-level facet entries alongside the named facets.
     *
     * @return map of facet name to facet
     */
    @JsonAnyGetter
    public Map<String, OpenLineageInputDataSetInputFacet> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any additional facets that are not modelled as named properties.
     *
     * @param additionalProperties map of facet name to facet
     */
    public void setAdditionalProperties(Map<String, OpenLineageInputDataSetInputFacet> additionalProperties)
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
    public void setAdditionalProperty(String facetName, OpenLineageInputDataSetInputFacet facet)
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
        return "OpenLineageInputDataSetInputFacets{" +
                       "dataQualityAssertions=" + dataQualityAssertions +
                       ", dataQualityMetrics=" + dataQualityMetrics +
                       ", inputStatistics=" + inputStatistics +
                       ", subset=" + subset +
                       ", icebergScanReport=" + icebergScanReport +
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
        OpenLineageInputDataSetInputFacets that = (OpenLineageInputDataSetInputFacets) objectToCompare;
        return Objects.equals(dataQualityAssertions, that.dataQualityAssertions) &&
                       Objects.equals(dataQualityMetrics, that.dataQualityMetrics) &&
                       Objects.equals(inputStatistics, that.inputStatistics) &&
                       Objects.equals(subset, that.subset) &&
                       Objects.equals(icebergScanReport, that.icebergScanReport) &&
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
        return Objects.hash(dataQualityAssertions, dataQualityMetrics, inputStatistics, subset, icebergScanReport, additionalProperties);
    }
}
