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
 * This class represents the content of an open lineage documentation data set facet as defined in JSON
 * spec https://openlineage.io/spec/facets/1-0-0/OutputStatisticsOutputDatasetFacet.json#/$defs/OutputStatisticsOutputDatasetFacet.
 * It is used internally in Egeria to pass this information to the integration daemon's integration connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageOutputStatisticsOutputDataSetFacet extends OpenLineageOutputDataSetOutputFacet
{
    private long rowCount = 0;
    private long size = 0;


    /**
     * Default constructor
     */
    public OpenLineageOutputStatisticsOutputDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/OutputStatisticsOutputDatasetFacet.json#/$defs/OutputStatisticsOutputDatasetFacet"));
    }


    /**
     * Return the number of row written to the data set.
     *
     * @return count
     */
    public long getRowCount()
    {
        return rowCount;
    }


    /**
     * Set up the number of row written to the data set.
     *
     * @param rowCount count
     */
    public void setRowCount(long rowCount)
    {
        this.rowCount = rowCount;
    }


    /**
     * Return the number of bytes written to the data set.
     *
     * @return number of bytes
     */
    public long getSize()
    {
        return size;
    }


    /**
     * Set up the number of bytes written to the data set.
     *
     * @param size number of bytes
     */
    public void setSize(long size)
    {
        this.size = size;
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
        return rowCount == that.rowCount &&
                       size == that.size;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), rowCount, size);
    }
}
