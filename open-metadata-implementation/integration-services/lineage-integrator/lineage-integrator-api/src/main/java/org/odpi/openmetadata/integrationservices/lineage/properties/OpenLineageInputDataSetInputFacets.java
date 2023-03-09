/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the map of input facets in the open lineage standard spec
 * https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageInputDataSetInputFacets
{
    private OpenLineageDataQualityAssertionsInputDataSetFacet dataQualityAssertions = null;
    private OpenLineageDataQualityMetricsInputDataSetFacet    dataQualityMetrics    = null;
    private Map<String, OpenLineageInputDataSetInputFacet>    additionalProperties  = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageInputDataSetInputFacets()
    {
    }


    /**
     * Return data quality assertions facet.
     *
     * @return facet
     */
    public OpenLineageDataQualityAssertionsInputDataSetFacet getDataQualityAssertions()
    {
        return dataQualityAssertions;
    }


    /**
     * Set up the data quality assertions facet.
     *
     * @param dataQualityAssertions facet
     */
    public void setDataQualityAssertions(OpenLineageDataQualityAssertionsInputDataSetFacet dataQualityAssertions)
    {
        this.dataQualityAssertions = dataQualityAssertions;
    }


    /**
     * Return the data quality metrics facet.
     *
     * @return facet
     */
    public OpenLineageDataQualityMetricsInputDataSetFacet getDataQualityMetrics()
    {
        return dataQualityMetrics;
    }


    /**
     * Set up the data quality metrics facet.
     *
     * @param dataQualityMetrics facet
     */
    public void setDataQualityMetrics(OpenLineageDataQualityMetricsInputDataSetFacet dataQualityMetrics)
    {
        this.dataQualityMetrics = dataQualityMetrics;
    }


    /**
     * Return a map of additional input facets.  The name is the identifier of the facet type and the object is the facet itself.
     *
     * @return input facet map (map from string to object)
     */
    public Map<String, OpenLineageInputDataSetInputFacet> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up a map of additional input facets.  The name is the identifier of the facet type and the object is the facet itself.
     *
     * @param additionalProperties input facet map (map from string to object)
     */
    public void setAdditionalProperties(Map<String, OpenLineageInputDataSetInputFacet> additionalProperties)
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
        return "OpenLineageInputDataSetInputFacets{" +
                       "dataQualityAssertions=" + dataQualityAssertions +
                       ", dataQualityMetrics=" + dataQualityMetrics +
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
        return Objects.hash(dataQualityAssertions, dataQualityMetrics, additionalProperties);
    }
}
