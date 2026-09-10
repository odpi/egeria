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
 * This class represents the commit metrics reported by Apache Iceberg for a write to a table.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageIcebergCommitReportOutputDataSetFacetMetrics
{
    private Double totalDuration = null;
    private Double attempts = null;
    private Double addedDataFiles = null;
    private Double removedDataFiles = null;
    private Double totalDataFiles = null;
    private Double addedDeleteFiles = null;
    private Double addedEqualityDeleteFiles = null;
    private Double addedPositionalDeleteFiles = null;
    private Double addedDVs = null;
    private Double removedDeleteFiles = null;
    private Double removedEqualityDeleteFiles = null;
    private Double removedPositionalDeleteFiles = null;
    private Double removedDVs = null;
    private Double totalDeleteFiles = null;
    private Double addedRecords = null;
    private Double removedRecords = null;
    private Double totalRecords = null;
    private Double addedFilesSizeInBytes = null;
    private Double removedFilesSizeInBytes = null;
    private Double totalFilesSizeInBytes = null;
    private Double addedPositionalDeletes = null;
    private Double removedPositionalDeletes = null;
    private Double totalPositionalDeletes = null;
    private Double addedEqualityDeletes = null;
    private Double removedEqualityDeletes = null;
    private Double totalEqualityDeletes = null;


    /**
     * Default constructor
     */
    public OpenLineageIcebergCommitReportOutputDataSetFacetMetrics()
    {
    }


    /**
     * Return the total duration of the commit in milliseconds.
     *
     * @return double
     */
    public Double getTotalDuration()
    {
        return totalDuration;
    }


    /**
     * Set up the total duration of the commit in milliseconds.
     *
     * @param totalDuration double
     */
    public void setTotalDuration(Double totalDuration)
    {
        this.totalDuration = totalDuration;
    }


    /**
     * Return the number of commit attempts.
     *
     * @return double
     */
    public Double getAttempts()
    {
        return attempts;
    }


    /**
     * Set up the number of commit attempts.
     *
     * @param attempts double
     */
    public void setAttempts(Double attempts)
    {
        this.attempts = attempts;
    }


    /**
     * Return the number of data files added.
     *
     * @return double
     */
    public Double getAddedDataFiles()
    {
        return addedDataFiles;
    }


    /**
     * Set up the number of data files added.
     *
     * @param addedDataFiles double
     */
    public void setAddedDataFiles(Double addedDataFiles)
    {
        this.addedDataFiles = addedDataFiles;
    }


    /**
     * Return the number of data files removed.
     *
     * @return double
     */
    public Double getRemovedDataFiles()
    {
        return removedDataFiles;
    }


    /**
     * Set up the number of data files removed.
     *
     * @param removedDataFiles double
     */
    public void setRemovedDataFiles(Double removedDataFiles)
    {
        this.removedDataFiles = removedDataFiles;
    }


    /**
     * Return the total number of data files after the commit.
     *
     * @return double
     */
    public Double getTotalDataFiles()
    {
        return totalDataFiles;
    }


    /**
     * Set up the total number of data files after the commit.
     *
     * @param totalDataFiles double
     */
    public void setTotalDataFiles(Double totalDataFiles)
    {
        this.totalDataFiles = totalDataFiles;
    }


    /**
     * Return the number of delete files added.
     *
     * @return double
     */
    public Double getAddedDeleteFiles()
    {
        return addedDeleteFiles;
    }


    /**
     * Set up the number of delete files added.
     *
     * @param addedDeleteFiles double
     */
    public void setAddedDeleteFiles(Double addedDeleteFiles)
    {
        this.addedDeleteFiles = addedDeleteFiles;
    }


    /**
     * Return the number of equality delete files added.
     *
     * @return double
     */
    public Double getAddedEqualityDeleteFiles()
    {
        return addedEqualityDeleteFiles;
    }


    /**
     * Set up the number of equality delete files added.
     *
     * @param addedEqualityDeleteFiles double
     */
    public void setAddedEqualityDeleteFiles(Double addedEqualityDeleteFiles)
    {
        this.addedEqualityDeleteFiles = addedEqualityDeleteFiles;
    }


    /**
     * Return the number of positional delete files added.
     *
     * @return double
     */
    public Double getAddedPositionalDeleteFiles()
    {
        return addedPositionalDeleteFiles;
    }


    /**
     * Set up the number of positional delete files added.
     *
     * @param addedPositionalDeleteFiles double
     */
    public void setAddedPositionalDeleteFiles(Double addedPositionalDeleteFiles)
    {
        this.addedPositionalDeleteFiles = addedPositionalDeleteFiles;
    }


    /**
     * Return the number of deletion vectors added.
     *
     * @return double
     */
    public Double getAddedDVs()
    {
        return addedDVs;
    }


    /**
     * Set up the number of deletion vectors added.
     *
     * @param addedDVs double
     */
    public void setAddedDVs(Double addedDVs)
    {
        this.addedDVs = addedDVs;
    }


    /**
     * Return the number of delete files removed.
     *
     * @return double
     */
    public Double getRemovedDeleteFiles()
    {
        return removedDeleteFiles;
    }


    /**
     * Set up the number of delete files removed.
     *
     * @param removedDeleteFiles double
     */
    public void setRemovedDeleteFiles(Double removedDeleteFiles)
    {
        this.removedDeleteFiles = removedDeleteFiles;
    }


    /**
     * Return the number of equality delete files removed.
     *
     * @return double
     */
    public Double getRemovedEqualityDeleteFiles()
    {
        return removedEqualityDeleteFiles;
    }


    /**
     * Set up the number of equality delete files removed.
     *
     * @param removedEqualityDeleteFiles double
     */
    public void setRemovedEqualityDeleteFiles(Double removedEqualityDeleteFiles)
    {
        this.removedEqualityDeleteFiles = removedEqualityDeleteFiles;
    }


    /**
     * Return the number of positional delete files removed.
     *
     * @return double
     */
    public Double getRemovedPositionalDeleteFiles()
    {
        return removedPositionalDeleteFiles;
    }


    /**
     * Set up the number of positional delete files removed.
     *
     * @param removedPositionalDeleteFiles double
     */
    public void setRemovedPositionalDeleteFiles(Double removedPositionalDeleteFiles)
    {
        this.removedPositionalDeleteFiles = removedPositionalDeleteFiles;
    }


    /**
     * Return the number of deletion vectors removed.
     *
     * @return double
     */
    public Double getRemovedDVs()
    {
        return removedDVs;
    }


    /**
     * Set up the number of deletion vectors removed.
     *
     * @param removedDVs double
     */
    public void setRemovedDVs(Double removedDVs)
    {
        this.removedDVs = removedDVs;
    }


    /**
     * Return the total number of delete files after the commit.
     *
     * @return double
     */
    public Double getTotalDeleteFiles()
    {
        return totalDeleteFiles;
    }


    /**
     * Set up the total number of delete files after the commit.
     *
     * @param totalDeleteFiles double
     */
    public void setTotalDeleteFiles(Double totalDeleteFiles)
    {
        this.totalDeleteFiles = totalDeleteFiles;
    }


    /**
     * Return the number of records added.
     *
     * @return double
     */
    public Double getAddedRecords()
    {
        return addedRecords;
    }


    /**
     * Set up the number of records added.
     *
     * @param addedRecords double
     */
    public void setAddedRecords(Double addedRecords)
    {
        this.addedRecords = addedRecords;
    }


    /**
     * Return the number of records removed.
     *
     * @return double
     */
    public Double getRemovedRecords()
    {
        return removedRecords;
    }


    /**
     * Set up the number of records removed.
     *
     * @param removedRecords double
     */
    public void setRemovedRecords(Double removedRecords)
    {
        this.removedRecords = removedRecords;
    }


    /**
     * Return the total number of records after the commit.
     *
     * @return double
     */
    public Double getTotalRecords()
    {
        return totalRecords;
    }


    /**
     * Set up the total number of records after the commit.
     *
     * @param totalRecords double
     */
    public void setTotalRecords(Double totalRecords)
    {
        this.totalRecords = totalRecords;
    }


    /**
     * Return the size in bytes of the files added.
     *
     * @return double
     */
    public Double getAddedFilesSizeInBytes()
    {
        return addedFilesSizeInBytes;
    }


    /**
     * Set up the size in bytes of the files added.
     *
     * @param addedFilesSizeInBytes double
     */
    public void setAddedFilesSizeInBytes(Double addedFilesSizeInBytes)
    {
        this.addedFilesSizeInBytes = addedFilesSizeInBytes;
    }


    /**
     * Return the size in bytes of the files removed.
     *
     * @return double
     */
    public Double getRemovedFilesSizeInBytes()
    {
        return removedFilesSizeInBytes;
    }


    /**
     * Set up the size in bytes of the files removed.
     *
     * @param removedFilesSizeInBytes double
     */
    public void setRemovedFilesSizeInBytes(Double removedFilesSizeInBytes)
    {
        this.removedFilesSizeInBytes = removedFilesSizeInBytes;
    }


    /**
     * Return the total size in bytes of the files after the commit.
     *
     * @return double
     */
    public Double getTotalFilesSizeInBytes()
    {
        return totalFilesSizeInBytes;
    }


    /**
     * Set up the total size in bytes of the files after the commit.
     *
     * @param totalFilesSizeInBytes double
     */
    public void setTotalFilesSizeInBytes(Double totalFilesSizeInBytes)
    {
        this.totalFilesSizeInBytes = totalFilesSizeInBytes;
    }


    /**
     * Return the number of positional deletes added.
     *
     * @return double
     */
    public Double getAddedPositionalDeletes()
    {
        return addedPositionalDeletes;
    }


    /**
     * Set up the number of positional deletes added.
     *
     * @param addedPositionalDeletes double
     */
    public void setAddedPositionalDeletes(Double addedPositionalDeletes)
    {
        this.addedPositionalDeletes = addedPositionalDeletes;
    }


    /**
     * Return the number of positional deletes removed.
     *
     * @return double
     */
    public Double getRemovedPositionalDeletes()
    {
        return removedPositionalDeletes;
    }


    /**
     * Set up the number of positional deletes removed.
     *
     * @param removedPositionalDeletes double
     */
    public void setRemovedPositionalDeletes(Double removedPositionalDeletes)
    {
        this.removedPositionalDeletes = removedPositionalDeletes;
    }


    /**
     * Return the total number of positional deletes after the commit.
     *
     * @return double
     */
    public Double getTotalPositionalDeletes()
    {
        return totalPositionalDeletes;
    }


    /**
     * Set up the total number of positional deletes after the commit.
     *
     * @param totalPositionalDeletes double
     */
    public void setTotalPositionalDeletes(Double totalPositionalDeletes)
    {
        this.totalPositionalDeletes = totalPositionalDeletes;
    }


    /**
     * Return the number of equality deletes added.
     *
     * @return double
     */
    public Double getAddedEqualityDeletes()
    {
        return addedEqualityDeletes;
    }


    /**
     * Set up the number of equality deletes added.
     *
     * @param addedEqualityDeletes double
     */
    public void setAddedEqualityDeletes(Double addedEqualityDeletes)
    {
        this.addedEqualityDeletes = addedEqualityDeletes;
    }


    /**
     * Return the number of equality deletes removed.
     *
     * @return double
     */
    public Double getRemovedEqualityDeletes()
    {
        return removedEqualityDeletes;
    }


    /**
     * Set up the number of equality deletes removed.
     *
     * @param removedEqualityDeletes double
     */
    public void setRemovedEqualityDeletes(Double removedEqualityDeletes)
    {
        this.removedEqualityDeletes = removedEqualityDeletes;
    }


    /**
     * Return the total number of equality deletes after the commit.
     *
     * @return double
     */
    public Double getTotalEqualityDeletes()
    {
        return totalEqualityDeletes;
    }


    /**
     * Set up the total number of equality deletes after the commit.
     *
     * @param totalEqualityDeletes double
     */
    public void setTotalEqualityDeletes(Double totalEqualityDeletes)
    {
        this.totalEqualityDeletes = totalEqualityDeletes;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageIcebergCommitReportOutputDataSetFacetMetrics{" +
                       "totalDuration=" + totalDuration +
                       ", attempts=" + attempts +
                       ", addedDataFiles=" + addedDataFiles +
                       ", removedDataFiles=" + removedDataFiles +
                       ", totalDataFiles=" + totalDataFiles +
                       ", addedDeleteFiles=" + addedDeleteFiles +
                       ", addedEqualityDeleteFiles=" + addedEqualityDeleteFiles +
                       ", addedPositionalDeleteFiles=" + addedPositionalDeleteFiles +
                       ", addedDVs=" + addedDVs +
                       ", removedDeleteFiles=" + removedDeleteFiles +
                       ", removedEqualityDeleteFiles=" + removedEqualityDeleteFiles +
                       ", removedPositionalDeleteFiles=" + removedPositionalDeleteFiles +
                       ", removedDVs=" + removedDVs +
                       ", totalDeleteFiles=" + totalDeleteFiles +
                       ", addedRecords=" + addedRecords +
                       ", removedRecords=" + removedRecords +
                       ", totalRecords=" + totalRecords +
                       ", addedFilesSizeInBytes=" + addedFilesSizeInBytes +
                       ", removedFilesSizeInBytes=" + removedFilesSizeInBytes +
                       ", totalFilesSizeInBytes=" + totalFilesSizeInBytes +
                       ", addedPositionalDeletes=" + addedPositionalDeletes +
                       ", removedPositionalDeletes=" + removedPositionalDeletes +
                       ", totalPositionalDeletes=" + totalPositionalDeletes +
                       ", addedEqualityDeletes=" + addedEqualityDeletes +
                       ", removedEqualityDeletes=" + removedEqualityDeletes +
                       ", totalEqualityDeletes=" + totalEqualityDeletes +
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
        OpenLineageIcebergCommitReportOutputDataSetFacetMetrics that = (OpenLineageIcebergCommitReportOutputDataSetFacetMetrics) objectToCompare;
        return Objects.equals(totalDuration, that.totalDuration) &&
                       Objects.equals(attempts, that.attempts) &&
                       Objects.equals(addedDataFiles, that.addedDataFiles) &&
                       Objects.equals(removedDataFiles, that.removedDataFiles) &&
                       Objects.equals(totalDataFiles, that.totalDataFiles) &&
                       Objects.equals(addedDeleteFiles, that.addedDeleteFiles) &&
                       Objects.equals(addedEqualityDeleteFiles, that.addedEqualityDeleteFiles) &&
                       Objects.equals(addedPositionalDeleteFiles, that.addedPositionalDeleteFiles) &&
                       Objects.equals(addedDVs, that.addedDVs) &&
                       Objects.equals(removedDeleteFiles, that.removedDeleteFiles) &&
                       Objects.equals(removedEqualityDeleteFiles, that.removedEqualityDeleteFiles) &&
                       Objects.equals(removedPositionalDeleteFiles, that.removedPositionalDeleteFiles) &&
                       Objects.equals(removedDVs, that.removedDVs) &&
                       Objects.equals(totalDeleteFiles, that.totalDeleteFiles) &&
                       Objects.equals(addedRecords, that.addedRecords) &&
                       Objects.equals(removedRecords, that.removedRecords) &&
                       Objects.equals(totalRecords, that.totalRecords) &&
                       Objects.equals(addedFilesSizeInBytes, that.addedFilesSizeInBytes) &&
                       Objects.equals(removedFilesSizeInBytes, that.removedFilesSizeInBytes) &&
                       Objects.equals(totalFilesSizeInBytes, that.totalFilesSizeInBytes) &&
                       Objects.equals(addedPositionalDeletes, that.addedPositionalDeletes) &&
                       Objects.equals(removedPositionalDeletes, that.removedPositionalDeletes) &&
                       Objects.equals(totalPositionalDeletes, that.totalPositionalDeletes) &&
                       Objects.equals(addedEqualityDeletes, that.addedEqualityDeletes) &&
                       Objects.equals(removedEqualityDeletes, that.removedEqualityDeletes) &&
                       Objects.equals(totalEqualityDeletes, that.totalEqualityDeletes);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(totalDuration, attempts, addedDataFiles, removedDataFiles, totalDataFiles, addedDeleteFiles, addedEqualityDeleteFiles, addedPositionalDeleteFiles, addedDVs, removedDeleteFiles, removedEqualityDeleteFiles, removedPositionalDeleteFiles, removedDVs, totalDeleteFiles, addedRecords, removedRecords, totalRecords, addedFilesSizeInBytes, removedFilesSizeInBytes, totalFilesSizeInBytes, addedPositionalDeletes, removedPositionalDeletes, totalPositionalDeletes, addedEqualityDeletes, removedEqualityDeletes, totalEqualityDeletes);
    }
}
