/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the outputStatistics output dataset facet.  It captures the volume of data written to the dataset by the run.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-2/OutputStatisticsOutputDatasetFacet.json#/$defs/OutputStatisticsOutputDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageOutputStatisticsOutputDataSetFacet extends OpenLineageOutputDataSetOutputFacet
{
    private Long rowCount = null;
    private Long size = null;
    private Long fileCount = null;


    /**
     * Default constructor
     */
    public OpenLineageOutputStatisticsOutputDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-2/OutputStatisticsOutputDatasetFacet.json#/$defs/OutputStatisticsOutputDatasetFacet"));
    }


    /**
     * Return the number of rows written to the dataset.
     *
     * @return long
     */
    public Long getRowCount()
    {
        return rowCount;
    }


    /**
     * Set up the number of rows written to the dataset.
     *
     * @param rowCount long
     */
    public void setRowCount(Long rowCount)
    {
        this.rowCount = rowCount;
    }


    /**
     * Return the size in bytes written to the dataset.
     *
     * @return long
     */
    public Long getSize()
    {
        return size;
    }


    /**
     * Set up the size in bytes written to the dataset.
     *
     * @param size long
     */
    public void setSize(Long size)
    {
        this.size = size;
    }


    /**
     * Return the number of files written to the dataset.
     *
     * @return long
     */
    public Long getFileCount()
    {
        return fileCount;
    }


    /**
     * Set up the number of files written to the dataset.
     *
     * @param fileCount long
     */
    public void setFileCount(Long fileCount)
    {
        this.fileCount = fileCount;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageOutputStatisticsOutputDataSetFacet{" +
                       "rowCount=" + rowCount +
                       ", size=" + size +
                       ", fileCount=" + fileCount +
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
        OpenLineageOutputStatisticsOutputDataSetFacet that = (OpenLineageOutputStatisticsOutputDataSetFacet) objectToCompare;
        return Objects.equals(rowCount, that.rowCount) &&
                       Objects.equals(size, that.size) &&
                       Objects.equals(fileCount, that.fileCount);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), rowCount, size, fileCount);
    }
}
