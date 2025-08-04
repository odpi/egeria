/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the map of data quality quartiles in the open lineage standard spec
 * https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataQualityMetricsInputDataSetFacetColumnMetrics
{
    private Map<String, OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditional> additionalProperties = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageDataQualityMetricsInputDataSetFacetColumnMetrics()
    {
    }


    /**
     * Return a map of additional column based data quality metrics facets.
     *
     * @return map from column name to metrics
     */
    public Map<String, OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditional> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up a map of additional column based data quality metrics facets.
     *
     * @param additionalProperties map from column name to metrics
     */
    public void setAdditionalProperties(Map<String, OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditional> additionalProperties)
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
        return "OpenLineageDataQualityMetricsInputDataSetFacetColumnMetrics{" +
                       "additionalProperties=" + additionalProperties +
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
        OpenLineageDataQualityMetricsInputDataSetFacetColumnMetrics
                that = (OpenLineageDataQualityMetricsInputDataSetFacetColumnMetrics) objectToCompare;
        return Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(additionalProperties);
    }
}
