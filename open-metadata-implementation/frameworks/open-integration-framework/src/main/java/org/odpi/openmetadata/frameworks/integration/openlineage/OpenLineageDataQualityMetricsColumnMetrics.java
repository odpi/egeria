/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the data quality metrics for a single column of a dataset.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataQualityMetricsColumnMetrics
{
    private Long                nullCount = null;
    private Long                distinctCount = null;
    private Double              sum = null;
    private Double              count = null;
    private Double              min = null;
    private Double              max = null;
    private Map<String, Double> quantiles = null;


    /**
     * Default constructor
     */
    public OpenLineageDataQualityMetricsColumnMetrics()
    {
    }


    /**
     * Return the number of null values in this column for the rows evaluated.
     *
     * @return long
     */
    public Long getNullCount()
    {
        return nullCount;
    }


    /**
     * Set up the number of null values in this column for the rows evaluated.
     *
     * @param nullCount long
     */
    public void setNullCount(Long nullCount)
    {
        this.nullCount = nullCount;
    }


    /**
     * Return the number of distinct values in this column for the rows evaluated.
     *
     * @return long
     */
    public Long getDistinctCount()
    {
        return distinctCount;
    }


    /**
     * Set up the number of distinct values in this column for the rows evaluated.
     *
     * @param distinctCount long
     */
    public void setDistinctCount(Long distinctCount)
    {
        this.distinctCount = distinctCount;
    }


    /**
     * Return the total sum of values in this column for the rows evaluated.
     *
     * @return double
     */
    public Double getSum()
    {
        return sum;
    }


    /**
     * Set up the total sum of values in this column for the rows evaluated.
     *
     * @param sum double
     */
    public void setSum(Double sum)
    {
        this.sum = sum;
    }


    /**
     * Return the number of values in this column.
     *
     * @return double
     */
    public Double getCount()
    {
        return count;
    }


    /**
     * Set up the number of values in this column.
     *
     * @param count double
     */
    public void setCount(Double count)
    {
        this.count = count;
    }


    /**
     * Return the minimum value in this column.
     *
     * @return double
     */
    public Double getMin()
    {
        return min;
    }


    /**
     * Set up the minimum value in this column.
     *
     * @param min double
     */
    public void setMin(Double min)
    {
        this.min = min;
    }


    /**
     * Return the maximum value in this column.
     *
     * @return double
     */
    public Double getMax()
    {
        return max;
    }


    /**
     * Set up the maximum value in this column.
     *
     * @param max double
     */
    public void setMax(Double max)
    {
        this.max = max;
    }


    /**
     * Return the quantiles of the values in this column.  The map key is the quantile, for example 0.1, 0.25, 0.5, 0.75, 1, and the value is the value at that quantile.
     *
     * @return map
     */
    public Map<String, Double> getQuantiles()
    {
        return quantiles;
    }


    /**
     * Set up the quantiles of the values in this column.  The map key is the quantile, for example 0.1, 0.25, 0.5, 0.75, 1, and the value is the value at that quantile.
     *
     * @param quantiles map
     */
    public void setQuantiles(Map<String, Double> quantiles)
    {
        this.quantiles = quantiles;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageDataQualityMetricsColumnMetrics{" +
                       "nullCount=" + nullCount +
                       ", distinctCount=" + distinctCount +
                       ", sum=" + sum +
                       ", count=" + count +
                       ", min=" + min +
                       ", max=" + max +
                       ", quantiles=" + quantiles +
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
        OpenLineageDataQualityMetricsColumnMetrics that = (OpenLineageDataQualityMetricsColumnMetrics) objectToCompare;
        return Objects.equals(nullCount, that.nullCount) &&
                       Objects.equals(distinctCount, that.distinctCount) &&
                       Objects.equals(sum, that.sum) &&
                       Objects.equals(count, that.count) &&
                       Objects.equals(min, that.min) &&
                       Objects.equals(max, that.max) &&
                       Objects.equals(quantiles, that.quantiles);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(nullCount, distinctCount, sum, count, min, max, quantiles);
    }
}
