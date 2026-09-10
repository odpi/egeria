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
 * This class represents the icebergCommitReport output dataset facet registered by Apache Iceberg.  It reports the snapshot and commit metrics of a write to an Iceberg table.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-2/IcebergCommitReportOutputDatasetFacet.json#/$defs/IcebergCommitReportOutputDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageIcebergCommitReportOutputDataSetFacet extends OpenLineageOutputDataSetOutputFacet
{
    private Long                                                    snapshotId = null;
    private Long                                                    sequenceNumber = null;
    private String                                                  operation = null;
    private OpenLineageIcebergCommitReportOutputDataSetFacetMetrics commitMetrics = null;
    private Map<String, Object>                                     metadata = null;


    /**
     * Default constructor
     */
    public OpenLineageIcebergCommitReportOutputDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-2/IcebergCommitReportOutputDatasetFacet.json#/$defs/IcebergCommitReportOutputDatasetFacet"));
    }


    /**
     * Return the snapshot ID of the Iceberg table.
     *
     * @return long
     */
    public Long getSnapshotId()
    {
        return snapshotId;
    }


    /**
     * Set up the snapshot ID of the Iceberg table.
     *
     * @param snapshotId long
     */
    public void setSnapshotId(Long snapshotId)
    {
        this.snapshotId = snapshotId;
    }


    /**
     * Return the sequence number of the Iceberg table.
     *
     * @return long
     */
    public Long getSequenceNumber()
    {
        return sequenceNumber;
    }


    /**
     * Set up the sequence number of the Iceberg table.
     *
     * @param sequenceNumber long
     */
    public void setSequenceNumber(Long sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
    }


    /**
     * Return the operation that was performed on the Iceberg table, for example append, overwrite or delete.
     *
     * @return string
     */
    public String getOperation()
    {
        return operation;
    }


    /**
     * Set up the operation that was performed on the Iceberg table, for example append, overwrite or delete.
     *
     * @param operation string
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
    }


    /**
     * Return the commit metrics.
     *
     * @return bean
     */
    public OpenLineageIcebergCommitReportOutputDataSetFacetMetrics getCommitMetrics()
    {
        return commitMetrics;
    }


    /**
     * Set up the commit metrics.
     *
     * @param commitMetrics bean
     */
    public void setCommitMetrics(OpenLineageIcebergCommitReportOutputDataSetFacetMetrics commitMetrics)
    {
        this.commitMetrics = commitMetrics;
    }


    /**
     * Return the additional metadata reported with the commit.
     *
     * @return map
     */
    public Map<String, Object> getMetadata()
    {
        return metadata;
    }


    /**
     * Set up the additional metadata reported with the commit.
     *
     * @param metadata map
     */
    public void setMetadata(Map<String, Object> metadata)
    {
        this.metadata = metadata;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageIcebergCommitReportOutputDataSetFacet{" +
                       "snapshotId=" + snapshotId +
                       ", sequenceNumber=" + sequenceNumber +
                       ", operation='" + operation + '\'' +
                       ", commitMetrics=" + commitMetrics +
                       ", metadata=" + metadata +
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
        OpenLineageIcebergCommitReportOutputDataSetFacet that = (OpenLineageIcebergCommitReportOutputDataSetFacet) objectToCompare;
        return Objects.equals(snapshotId, that.snapshotId) &&
                       Objects.equals(sequenceNumber, that.sequenceNumber) &&
                       Objects.equals(operation, that.operation) &&
                       Objects.equals(commitMetrics, that.commitMetrics) &&
                       Objects.equals(metadata, that.metadata);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), snapshotId, sequenceNumber, operation, commitMetrics, metadata);
    }
}
