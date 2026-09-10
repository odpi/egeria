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
 * This class represents the scan metrics reported by Apache Iceberg for a read of a table.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageIcebergScanReportInputDataSetFacetMetrics
{
    private Double totalPlanningDuration = null;
    private Double resultDataFiles = null;
    private Double resultDeleteFiles = null;
    private Double totalDataManifests = null;
    private Double totalDeleteManifests = null;
    private Double scannedDataManifests = null;
    private Double skippedDataManifests = null;
    private Double totalFileSizeInBytes = null;
    private Double totalDeleteFileSizeInBytes = null;
    private Double skippedDataFiles = null;
    private Double skippedDeleteFiles = null;
    private Double scannedDeleteManifests = null;
    private Double skippedDeleteManifests = null;
    private Double indexedDeleteFiles = null;
    private Double equalityDeleteFiles = null;
    private Double positionalDeleteFiles = null;


    /**
     * Default constructor
     */
    public OpenLineageIcebergScanReportInputDataSetFacetMetrics()
    {
    }


    /**
     * Return the total planning duration in milliseconds.
     *
     * @return double
     */
    public Double getTotalPlanningDuration()
    {
        return totalPlanningDuration;
    }


    /**
     * Set up the total planning duration in milliseconds.
     *
     * @param totalPlanningDuration double
     */
    public void setTotalPlanningDuration(Double totalPlanningDuration)
    {
        this.totalPlanningDuration = totalPlanningDuration;
    }


    /**
     * Return the number of data files in the scan result.
     *
     * @return double
     */
    public Double getResultDataFiles()
    {
        return resultDataFiles;
    }


    /**
     * Set up the number of data files in the scan result.
     *
     * @param resultDataFiles double
     */
    public void setResultDataFiles(Double resultDataFiles)
    {
        this.resultDataFiles = resultDataFiles;
    }


    /**
     * Return the number of delete files in the scan result.
     *
     * @return double
     */
    public Double getResultDeleteFiles()
    {
        return resultDeleteFiles;
    }


    /**
     * Set up the number of delete files in the scan result.
     *
     * @param resultDeleteFiles double
     */
    public void setResultDeleteFiles(Double resultDeleteFiles)
    {
        this.resultDeleteFiles = resultDeleteFiles;
    }


    /**
     * Return the total number of data manifests.
     *
     * @return double
     */
    public Double getTotalDataManifests()
    {
        return totalDataManifests;
    }


    /**
     * Set up the total number of data manifests.
     *
     * @param totalDataManifests double
     */
    public void setTotalDataManifests(Double totalDataManifests)
    {
        this.totalDataManifests = totalDataManifests;
    }


    /**
     * Return the total number of delete manifests.
     *
     * @return double
     */
    public Double getTotalDeleteManifests()
    {
        return totalDeleteManifests;
    }


    /**
     * Set up the total number of delete manifests.
     *
     * @param totalDeleteManifests double
     */
    public void setTotalDeleteManifests(Double totalDeleteManifests)
    {
        this.totalDeleteManifests = totalDeleteManifests;
    }


    /**
     * Return the number of data manifests scanned.
     *
     * @return double
     */
    public Double getScannedDataManifests()
    {
        return scannedDataManifests;
    }


    /**
     * Set up the number of data manifests scanned.
     *
     * @param scannedDataManifests double
     */
    public void setScannedDataManifests(Double scannedDataManifests)
    {
        this.scannedDataManifests = scannedDataManifests;
    }


    /**
     * Return the number of data manifests skipped.
     *
     * @return double
     */
    public Double getSkippedDataManifests()
    {
        return skippedDataManifests;
    }


    /**
     * Set up the number of data manifests skipped.
     *
     * @param skippedDataManifests double
     */
    public void setSkippedDataManifests(Double skippedDataManifests)
    {
        this.skippedDataManifests = skippedDataManifests;
    }


    /**
     * Return the total size in bytes of the data files scanned.
     *
     * @return double
     */
    public Double getTotalFileSizeInBytes()
    {
        return totalFileSizeInBytes;
    }


    /**
     * Set up the total size in bytes of the data files scanned.
     *
     * @param totalFileSizeInBytes double
     */
    public void setTotalFileSizeInBytes(Double totalFileSizeInBytes)
    {
        this.totalFileSizeInBytes = totalFileSizeInBytes;
    }


    /**
     * Return the total size in bytes of the delete files scanned.
     *
     * @return double
     */
    public Double getTotalDeleteFileSizeInBytes()
    {
        return totalDeleteFileSizeInBytes;
    }


    /**
     * Set up the total size in bytes of the delete files scanned.
     *
     * @param totalDeleteFileSizeInBytes double
     */
    public void setTotalDeleteFileSizeInBytes(Double totalDeleteFileSizeInBytes)
    {
        this.totalDeleteFileSizeInBytes = totalDeleteFileSizeInBytes;
    }


    /**
     * Return the number of data files skipped.
     *
     * @return double
     */
    public Double getSkippedDataFiles()
    {
        return skippedDataFiles;
    }


    /**
     * Set up the number of data files skipped.
     *
     * @param skippedDataFiles double
     */
    public void setSkippedDataFiles(Double skippedDataFiles)
    {
        this.skippedDataFiles = skippedDataFiles;
    }


    /**
     * Return the number of delete files skipped.
     *
     * @return double
     */
    public Double getSkippedDeleteFiles()
    {
        return skippedDeleteFiles;
    }


    /**
     * Set up the number of delete files skipped.
     *
     * @param skippedDeleteFiles double
     */
    public void setSkippedDeleteFiles(Double skippedDeleteFiles)
    {
        this.skippedDeleteFiles = skippedDeleteFiles;
    }


    /**
     * Return the number of delete manifests scanned.
     *
     * @return double
     */
    public Double getScannedDeleteManifests()
    {
        return scannedDeleteManifests;
    }


    /**
     * Set up the number of delete manifests scanned.
     *
     * @param scannedDeleteManifests double
     */
    public void setScannedDeleteManifests(Double scannedDeleteManifests)
    {
        this.scannedDeleteManifests = scannedDeleteManifests;
    }


    /**
     * Return the number of delete manifests skipped.
     *
     * @return double
     */
    public Double getSkippedDeleteManifests()
    {
        return skippedDeleteManifests;
    }


    /**
     * Set up the number of delete manifests skipped.
     *
     * @param skippedDeleteManifests double
     */
    public void setSkippedDeleteManifests(Double skippedDeleteManifests)
    {
        this.skippedDeleteManifests = skippedDeleteManifests;
    }


    /**
     * Return the number of indexed delete files.
     *
     * @return double
     */
    public Double getIndexedDeleteFiles()
    {
        return indexedDeleteFiles;
    }


    /**
     * Set up the number of indexed delete files.
     *
     * @param indexedDeleteFiles double
     */
    public void setIndexedDeleteFiles(Double indexedDeleteFiles)
    {
        this.indexedDeleteFiles = indexedDeleteFiles;
    }


    /**
     * Return the number of equality delete files.
     *
     * @return double
     */
    public Double getEqualityDeleteFiles()
    {
        return equalityDeleteFiles;
    }


    /**
     * Set up the number of equality delete files.
     *
     * @param equalityDeleteFiles double
     */
    public void setEqualityDeleteFiles(Double equalityDeleteFiles)
    {
        this.equalityDeleteFiles = equalityDeleteFiles;
    }


    /**
     * Return the number of positional delete files.
     *
     * @return double
     */
    public Double getPositionalDeleteFiles()
    {
        return positionalDeleteFiles;
    }


    /**
     * Set up the number of positional delete files.
     *
     * @param positionalDeleteFiles double
     */
    public void setPositionalDeleteFiles(Double positionalDeleteFiles)
    {
        this.positionalDeleteFiles = positionalDeleteFiles;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageIcebergScanReportInputDataSetFacetMetrics{" +
                       "totalPlanningDuration=" + totalPlanningDuration +
                       ", resultDataFiles=" + resultDataFiles +
                       ", resultDeleteFiles=" + resultDeleteFiles +
                       ", totalDataManifests=" + totalDataManifests +
                       ", totalDeleteManifests=" + totalDeleteManifests +
                       ", scannedDataManifests=" + scannedDataManifests +
                       ", skippedDataManifests=" + skippedDataManifests +
                       ", totalFileSizeInBytes=" + totalFileSizeInBytes +
                       ", totalDeleteFileSizeInBytes=" + totalDeleteFileSizeInBytes +
                       ", skippedDataFiles=" + skippedDataFiles +
                       ", skippedDeleteFiles=" + skippedDeleteFiles +
                       ", scannedDeleteManifests=" + scannedDeleteManifests +
                       ", skippedDeleteManifests=" + skippedDeleteManifests +
                       ", indexedDeleteFiles=" + indexedDeleteFiles +
                       ", equalityDeleteFiles=" + equalityDeleteFiles +
                       ", positionalDeleteFiles=" + positionalDeleteFiles +
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
        OpenLineageIcebergScanReportInputDataSetFacetMetrics that = (OpenLineageIcebergScanReportInputDataSetFacetMetrics) objectToCompare;
        return Objects.equals(totalPlanningDuration, that.totalPlanningDuration) &&
                       Objects.equals(resultDataFiles, that.resultDataFiles) &&
                       Objects.equals(resultDeleteFiles, that.resultDeleteFiles) &&
                       Objects.equals(totalDataManifests, that.totalDataManifests) &&
                       Objects.equals(totalDeleteManifests, that.totalDeleteManifests) &&
                       Objects.equals(scannedDataManifests, that.scannedDataManifests) &&
                       Objects.equals(skippedDataManifests, that.skippedDataManifests) &&
                       Objects.equals(totalFileSizeInBytes, that.totalFileSizeInBytes) &&
                       Objects.equals(totalDeleteFileSizeInBytes, that.totalDeleteFileSizeInBytes) &&
                       Objects.equals(skippedDataFiles, that.skippedDataFiles) &&
                       Objects.equals(skippedDeleteFiles, that.skippedDeleteFiles) &&
                       Objects.equals(scannedDeleteManifests, that.scannedDeleteManifests) &&
                       Objects.equals(skippedDeleteManifests, that.skippedDeleteManifests) &&
                       Objects.equals(indexedDeleteFiles, that.indexedDeleteFiles) &&
                       Objects.equals(equalityDeleteFiles, that.equalityDeleteFiles) &&
                       Objects.equals(positionalDeleteFiles, that.positionalDeleteFiles);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(totalPlanningDuration, resultDataFiles, resultDeleteFiles, totalDataManifests, totalDeleteManifests, scannedDataManifests, skippedDataManifests, totalFileSizeInBytes, totalDeleteFileSizeInBytes, skippedDataFiles, skippedDeleteFiles, scannedDeleteManifests, skippedDeleteManifests, indexedDeleteFiles, equalityDeleteFiles, positionalDeleteFiles);
    }
}
