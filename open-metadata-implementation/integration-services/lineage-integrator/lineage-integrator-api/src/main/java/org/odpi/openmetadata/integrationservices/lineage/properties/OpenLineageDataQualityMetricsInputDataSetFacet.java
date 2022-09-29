/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the data quality metrics facet in the open lineage standard spec
 * https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataQualityMetricsInputDataSetFacet extends OpenLineageInputDataSetInputFacet
{
    private long                                                        rowCount = 0;
    private long                                                        bytes = 0;
    private OpenLineageDataQualityMetricsInputDataSetFacetColumnMetrics columnMetrics = null;


    /**
     * Default constructor
     */
    public OpenLineageDataQualityMetricsInputDataSetFacet()
    {
    }


    public long getRowCount()
    {
        return rowCount;
    }


    public void setRowCount(long rowCount)
    {
        this.rowCount = rowCount;
    }


    public long getBytes()
    {
        return bytes;
    }


    public void setBytes(long bytes)
    {
        this.bytes = bytes;
    }


    /**
     * Return the list of column metrics for the data set.
     *
     * @return list of column metrics
     */
    public OpenLineageDataQualityMetricsInputDataSetFacetColumnMetrics getColumnMetrics()
    {
        return columnMetrics;
    }


    /**
     * Set up the list of column metrics for the data set.
     *
     * @param columnMetrics list of column metrics
     */
    public void setColumnMetrics(OpenLineageDataQualityMetricsInputDataSetFacetColumnMetrics columnMetrics)
    {
        this.columnMetrics = columnMetrics;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageDataQualityMetricsInputDataSetFacet{" +
                       "rowCount=" + rowCount +
                       ", bytes=" + bytes +
                       ", columnMetrics=" + columnMetrics +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
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
        OpenLineageDataQualityMetricsInputDataSetFacet that = (OpenLineageDataQualityMetricsInputDataSetFacet) objectToCompare;
        return rowCount == that.rowCount &&
                       bytes == that.bytes &&
                       Objects.equals(columnMetrics, that.columnMetrics);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), rowCount, bytes, columnMetrics);
    }
}
