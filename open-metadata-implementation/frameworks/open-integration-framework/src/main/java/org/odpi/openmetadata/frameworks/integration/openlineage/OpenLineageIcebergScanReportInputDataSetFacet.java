/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the icebergScanReport input dataset facet registered by Apache Iceberg.  It reports the snapshot, filter, projection and scan metrics of a read of an Iceberg table.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-1/IcebergScanReportInputDatasetFacet.json#/$defs/IcebergScanReportInputDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageIcebergScanReportInputDataSetFacet extends OpenLineageInputDataSetInputFacet
{
    private Long                                                 snapshotId = null;
    private String                                               filterDescription = null;
    private Long                                                 schemaId = null;
    private List<String>                                         projectedFieldNames = null;
    private OpenLineageIcebergScanReportInputDataSetFacetMetrics scanMetrics = null;
    private Map<String, Object>                                  metadata = null;


    /**
     * Default constructor
     */
    public OpenLineageIcebergScanReportInputDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-1/IcebergScanReportInputDatasetFacet.json#/$defs/IcebergScanReportInputDatasetFacet"));
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
     * Return the filter used to scan the Iceberg table.
     *
     * @return string
     */
    public String getFilterDescription()
    {
        return filterDescription;
    }


    /**
     * Set up the filter used to scan the Iceberg table.
     *
     * @param filterDescription string
     */
    public void setFilterDescription(String filterDescription)
    {
        this.filterDescription = filterDescription;
    }


    /**
     * Return the schema ID of the Iceberg table.
     *
     * @return long
     */
    public Long getSchemaId()
    {
        return schemaId;
    }


    /**
     * Set up the schema ID of the Iceberg table.
     *
     * @param schemaId long
     */
    public void setSchemaId(Long schemaId)
    {
        this.schemaId = schemaId;
    }


    /**
     * Return the names of the fields projected from the Iceberg table.
     *
     * @return list
     */
    public List<String> getProjectedFieldNames()
    {
        return projectedFieldNames;
    }


    /**
     * Set up the names of the fields projected from the Iceberg table.
     *
     * @param projectedFieldNames list
     */
    public void setProjectedFieldNames(List<String> projectedFieldNames)
    {
        this.projectedFieldNames = projectedFieldNames;
    }


    /**
     * Return the scan metrics.
     *
     * @return bean
     */
    public OpenLineageIcebergScanReportInputDataSetFacetMetrics getScanMetrics()
    {
        return scanMetrics;
    }


    /**
     * Set up the scan metrics.
     *
     * @param scanMetrics bean
     */
    public void setScanMetrics(OpenLineageIcebergScanReportInputDataSetFacetMetrics scanMetrics)
    {
        this.scanMetrics = scanMetrics;
    }


    /**
     * Return the additional metadata reported with the scan.
     *
     * @return map
     */
    public Map<String, Object> getMetadata()
    {
        return metadata;
    }


    /**
     * Set up the additional metadata reported with the scan.
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
        return "OpenLineageIcebergScanReportInputDataSetFacet{" +
                       "snapshotId=" + snapshotId +
                       ", filterDescription='" + filterDescription + '\'' +
                       ", schemaId=" + schemaId +
                       ", projectedFieldNames=" + projectedFieldNames +
                       ", scanMetrics=" + scanMetrics +
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
        OpenLineageIcebergScanReportInputDataSetFacet that = (OpenLineageIcebergScanReportInputDataSetFacet) objectToCompare;
        return Objects.equals(snapshotId, that.snapshotId) &&
                       Objects.equals(filterDescription, that.filterDescription) &&
                       Objects.equals(schemaId, that.schemaId) &&
                       Objects.equals(projectedFieldNames, that.projectedFieldNames) &&
                       Objects.equals(scanMetrics, that.scanMetrics) &&
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
        return Objects.hash(super.hashCode(), snapshotId, filterDescription, schemaId, projectedFieldNames, scanMetrics, metadata);
    }
}
