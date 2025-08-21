/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opensurvey.measurements;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FileDirectoryMeasurement describes the measurements that are typically captured when a file system is surveyed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FileDirectoryMeasurement
{
    private String directoryName                   = null;
    private long   fileCount                       = 0L;
    private double totalFileSize                   = 0D;
    private long   subDirectoryCount               = 0L;
    private long   readableFileCount               = 0L;
    private long   writeableFileCount              = 0L;
    private long   executableFileCount             = 0L;
    private long   symLinkFileCount                = 0L;
    private long   hiddenFileCount                 = 0L;
    private long   fileNameCount                   = 0L;
    private long   fileExtensionCount              = 0L;
    private long   fileTypeCount                   = 0L;
    private long   assetTypeCount                  = 0L;
    private long   deployedImplementationTypeCount = 0L;
    private long   unclassifiedFileCount           = 0L;
    private long   inaccessibleFileCount           = 0L;
    private Date   lastFileCreationTime            = null;
    private Date   lastFileModificationTime        = null;
    private Date   lastFileAccessedTime            = null;


    /**
     * Default Constructor
     */
    public FileDirectoryMeasurement()
    {
    }


    public String getDirectoryName()
    {
        return directoryName;
    }

    public void setDirectoryName(String directoryName)
    {
        this.directoryName = directoryName;
    }

    public long getFileCount()
    {
        return fileCount;
    }

    public void setFileCount(long fileCount)
    {
        this.fileCount = fileCount;
    }

    public double getTotalFileSize()
    {
        return totalFileSize;
    }

    public void setTotalFileSize(double totalFileSize)
    {
        this.totalFileSize = totalFileSize;
    }

    public long getSubDirectoryCount()
    {
        return subDirectoryCount;
    }

    public void setSubDirectoryCount(long subDirectoryCount)
    {
        this.subDirectoryCount = subDirectoryCount;
    }

    public long getReadableFileCount()
    {
        return readableFileCount;
    }

    public void setReadableFileCount(long readableFileCount)
    {
        this.readableFileCount = readableFileCount;
    }

    public long getWriteableFileCount()
    {
        return writeableFileCount;
    }

    public void setWriteableFileCount(long writeableFileCount)
    {
        this.writeableFileCount = writeableFileCount;
    }

    public long getExecutableFileCount()
    {
        return executableFileCount;
    }

    public void setExecutableFileCount(long executableFileCount)
    {
        this.executableFileCount = executableFileCount;
    }

    public long getSymLinkFileCount()
    {
        return symLinkFileCount;
    }

    public void setSymLinkFileCount(long symLinkFileCount)
    {
        this.symLinkFileCount = symLinkFileCount;
    }

    public long getHiddenFileCount()
    {
        return hiddenFileCount;
    }

    public void setHiddenFileCount(long hiddenFileCount)
    {
        this.hiddenFileCount = hiddenFileCount;
    }

    public long getFileNameCount()
    {
        return fileNameCount;
    }

    public void setFileNameCount(long fileNameCount)
    {
        this.fileNameCount = fileNameCount;
    }

    public long getFileExtensionCount()
    {
        return fileExtensionCount;
    }

    public void setFileExtensionCount(long fileExtensionCount)
    {
        this.fileExtensionCount = fileExtensionCount;
    }

    public long getFileTypeCount()
    {
        return fileTypeCount;
    }

    public void setFileTypeCount(long fileTypeCount)
    {
        this.fileTypeCount = fileTypeCount;
    }

    public long getAssetTypeCount()
    {
        return assetTypeCount;
    }

    public void setAssetTypeCount(long assetTypeCount)
    {
        this.assetTypeCount = assetTypeCount;
    }

    public long getDeployedImplementationTypeCount()
    {
        return deployedImplementationTypeCount;
    }

    public void setDeployedImplementationTypeCount(long deployedImplementationTypeCount)
    {
        this.deployedImplementationTypeCount = deployedImplementationTypeCount;
    }

    public long getUnclassifiedFileCount()
    {
        return unclassifiedFileCount;
    }

    public void setUnclassifiedFileCount(long unclassifiedFileCount)
    {
        this.unclassifiedFileCount = unclassifiedFileCount;
    }

    public long getInaccessibleFileCount()
    {
        return inaccessibleFileCount;
    }

    public void setInaccessibleFileCount(long inaccessibleFileCount)
    {
        this.inaccessibleFileCount = inaccessibleFileCount;
    }

    public Date getLastFileCreationTime()
    {
        return lastFileCreationTime;
    }

    public void setLastFileCreationTime(Date lastFileCreationTime)
    {
        this.lastFileCreationTime = lastFileCreationTime;
    }

    public Date getLastFileModificationTime()
    {
        return lastFileModificationTime;
    }

    public void setLastFileModificationTime(Date lastFileModificationTime)
    {
        this.lastFileModificationTime = lastFileModificationTime;
    }

    public Date getLastFileAccessedTime()
    {
        return lastFileAccessedTime;
    }

    public void setLastFileAccessedTime(Date lastFileAccessedTime)
    {
        this.lastFileAccessedTime = lastFileAccessedTime;
    }

    @Override
    public String toString()
    {
        return "FileDirectoryMeasurement{" +
                "directoryName='" + directoryName + '\'' +
                ", fileCount=" + fileCount +
                ", totalFileSize=" + totalFileSize +
                ", subDirectoryCount=" + subDirectoryCount +
                ", readableFileCount=" + readableFileCount +
                ", writeableFileCount=" + writeableFileCount +
                ", executableFileCount=" + executableFileCount +
                ", symLinkFileCount=" + symLinkFileCount +
                ", hiddenFileCount=" + hiddenFileCount +
                ", fileNameCount=" + fileNameCount +
                ", fileExtensionCount=" + fileExtensionCount +
                ", fileTypeCount=" + fileTypeCount +
                ", assetTypeCount=" + assetTypeCount +
                ", deployedImplementationTypeCount=" + deployedImplementationTypeCount +
                ", unclassifiedFileCount=" + unclassifiedFileCount +
                ", inaccessibleFileCount=" + inaccessibleFileCount +
                ", lastFileCreationTime=" + lastFileCreationTime +
                ", lastFileModificationTime=" + lastFileModificationTime +
                ", lastFileAccessedTime=" + lastFileAccessedTime +
                '}';
    }
}
