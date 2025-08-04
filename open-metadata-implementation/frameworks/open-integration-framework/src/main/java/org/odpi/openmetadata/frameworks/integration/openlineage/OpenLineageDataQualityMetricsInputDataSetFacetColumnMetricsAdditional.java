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
 * This class represents the map of data quality quartiles in the open lineage standard spec
 * https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditional
{
    private long                                                                           nullCount;
    private long                                                                           distinctCount;
    private double                                                                         sum;
    private double                                                                         count;
    private double                                                                         min;
    private double                                                                         max;
    private OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditionalQuantiles quantiles;


    /**
     * Default constructor
     */
    public OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditional()
    {
    }


    /**
     * Return the number of null values in this column for the rows evaluated.
     *
     * @return long
     */
    public long getNullCount()
    {
        return nullCount;
    }


    /**
     * Set up the number of null values in this column for the rows evaluated.
     *
     * @param nullCount long
     */
    public void setNullCount(long nullCount)
    {
        this.nullCount = nullCount;
    }


    /**
     * Return the number of distinct values in this column for the rows evaluated.
     *
     * @return long
     */
    public long getDistinctCount()
    {
        return distinctCount;
    }


    /**
     * Set up the number of distinct values in this column for the rows evaluated.
     *
     * @param distinctCount long
     */
    public void setDistinctCount(long distinctCount)
    {
        this.distinctCount = distinctCount;
    }


    /**
     * Return the total sum of values in this column for the rows evaluated.
     *
     * @return double
     */
    public double getSum()
    {
        return sum;
    }


    /**
     * Set up the total sum of values in this column for the rows evaluated.
     *
     * @param sum double
     */
    public void setSum(double sum)
    {
        this.sum = sum;
    }


    /**
     * Return the number of values in this column.
     *
     * @return double
     */
    public double getCount()
    {
        return count;
    }


    /**
     * Set up the number of values in this column.
     *
     * @param count double
     */
    public void setCount(double count)
    {
        this.count = count;
    }


    /**
     * Return the minimum value in this column.
     *
     * @return double
     */
    public double getMin()
    {
        return min;
    }


    /**
     * Set up the minimum value in this column.
     *
     * @param min double
     */
    public void setMin(double min)
    {
        this.min = min;
    }


    /**
     * Return the maximum value in this column.
     *
     * @return double
     */
    public double getMax()
    {
        return max;
    }


    /**
     * Set up the maximum value in this column.
     *
     * @param max double
     */
    public void setMax(double max)
    {
        this.max = max;
    }


    /**
     * Return the quantile measurements.
     *
     * @return quantiles
     */
    public OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditionalQuantiles getQuantiles()
    {
        return quantiles;
    }


    /**
     * Set up the quantile measurements.
     *
     * @param quantiles quantiles
     */
    public void setQuantiles(OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditionalQuantiles quantiles)
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
        return "OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditional{" +
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
        OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditional that =
                (OpenLineageDataQualityMetricsInputDataSetFacetColumnMetricsAdditional) objectToCompare;
        return nullCount == that.nullCount &&
                       distinctCount == that.distinctCount &&
                       Double.compare(that.sum, sum) == 0 &&
                       Double.compare(that.count, count) == 0 &&
                       Double.compare(that.min, min) == 0 &&
                       Double.compare(that.max, max) == 0 &&
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
