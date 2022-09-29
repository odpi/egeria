/* SPDX-License-Identifier: Apache 2.0 */
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
 * This class represents the map of data quality quantiles in the open lineage standard spec
 * https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditionalQuantiles
{
    private Map<String, Double> additionalProperties = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditionalQuantiles()
    {
    }


    /**
     * Return a map of quantile measurements.
     *
     * @return The property key is the quantile. Examples: 0.1 0.25 0.5 0.75 1
     */
    public Map<String, Double> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up a map of quantile measurements.
     *
     * @param additionalProperties The property key is the quantile. Examples: 0.1 0.25 0.5 0.75 1
     */
    public void setAdditionalProperties(Map<String, Double> additionalProperties)
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
        return "OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditionalQuantiles{" +
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
        OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditionalQuantiles
                that = (OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditionalQuantiles) objectToCompare;
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
