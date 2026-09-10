/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the dataQualityMetrics dataset facet.  It captures data quality metrics for the dataset as a whole.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-0/DataQualityMetricsDatasetFacet.json#/$defs/DataQualityMetricsDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataQualityMetricsDataSetFacet extends OpenLineageDataSetFacet
{
    private Long                                                    rowCount = null;
    private Long                                                    bytes = null;
    private Long                                                    fileCount = null;
    private String                                                  lastUpdated = null;
    private Map<String, OpenLineageDataQualityMetricsColumnMetrics> columnMetrics = null;


    /**
     * Default constructor
     */
    public OpenLineageDataQualityMetricsDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/DataQualityMetricsDatasetFacet.json#/$defs/DataQualityMetricsDatasetFacet"));
    }


    /**
     * Return the number of rows evaluated.
     *
     * @return long
     */
    public Long getRowCount()
    {
        return rowCount;
    }


    /**
     * Set up the number of rows evaluated.
     *
     * @param rowCount long
     */
    public void setRowCount(Long rowCount)
    {
        this.rowCount = rowCount;
    }


    /**
     * Return the size in bytes.
     *
     * @return long
     */
    public Long getBytes()
    {
        return bytes;
    }


    /**
     * Set up the size in bytes.
     *
     * @param bytes long
     */
    public void setBytes(Long bytes)
    {
        this.bytes = bytes;
    }


    /**
     * Return the number of files evaluated.
     *
     * @return long
     */
    public Long getFileCount()
    {
        return fileCount;
    }


    /**
     * Set up the number of files evaluated.
     *
     * @param fileCount long
     */
    public void setFileCount(Long fileCount)
    {
        this.fileCount = fileCount;
    }


    /**
     * Return the ISO-8601 timestamp of the last time the dataset was changed.
     *
     * @return string
     */
    public String getLastUpdated()
    {
        return lastUpdated;
    }


    /**
     * Set up the ISO-8601 timestamp of the last time the dataset was changed.
     *
     * @param lastUpdated string
     */
    public void setLastUpdated(String lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }


    /**
     * Return the metrics for each column.  The map key is the column name.
     *
     * @return map
     */
    public Map<String, OpenLineageDataQualityMetricsColumnMetrics> getColumnMetrics()
    {
        return columnMetrics;
    }


    /**
     * Set up the metrics for each column.  The map key is the column name.
     *
     * @param columnMetrics map
     */
    public void setColumnMetrics(Map<String, OpenLineageDataQualityMetricsColumnMetrics> columnMetrics)
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
        return "OpenLineageDataQualityMetricsDataSetFacet{" +
                       "rowCount=" + rowCount +
                       ", bytes=" + bytes +
                       ", fileCount=" + fileCount +
                       ", lastUpdated='" + lastUpdated + '\'' +
                       ", columnMetrics=" + columnMetrics +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", _deleted=" + get_deleted() +
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
        OpenLineageDataQualityMetricsDataSetFacet that = (OpenLineageDataQualityMetricsDataSetFacet) objectToCompare;
        return Objects.equals(rowCount, that.rowCount) &&
                       Objects.equals(bytes, that.bytes) &&
                       Objects.equals(fileCount, that.fileCount) &&
                       Objects.equals(lastUpdated, that.lastUpdated) &&
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
        return Objects.hash(super.hashCode(), rowCount, bytes, fileCount, lastUpdated, columnMetrics);
    }
}
