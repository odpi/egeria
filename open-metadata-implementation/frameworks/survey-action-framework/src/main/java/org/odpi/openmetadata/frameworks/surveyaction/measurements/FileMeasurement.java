/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.measurements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * File measurement records the measurements taken from a file.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FileMeasurement
{
    private String  fileName                   = null;
    private String  pathName                   = null;
    private String  fileExtension              = null;
    private String  fileType                   = null;
    private String  deployedImplementationType = null;
    private String  encoding                   = null;
    private String  assetTypeName              = null;
    private boolean canRead                    = false;
    private boolean canWrite                   = false;
    private boolean canExecute                 = false;
    private boolean symLink                    = false;
    private boolean hidden                     = false;
    private Date    creationTime               = null;
    private Date    lastModifiedTime           = null;
    private Date    lastAccessedTime           = null;
    private long    fileSize                   = 0L;
    private long    recordCount                = 0L;

    /**
     * Default Constructor
     */
    public FileMeasurement()
    {
    }


    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getPathName()
    {
        return pathName;
    }

    public void setPathName(String pathName)
    {
        this.pathName = pathName;
    }

    public String getFileExtension()
    {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension)
    {
        this.fileExtension = fileExtension;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }

    public void setDeployedImplementationType(String deployedImplementationType)
    {
        this.deployedImplementationType = deployedImplementationType;
    }

    public String getEncoding()
    {
        return encoding;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    public String getAssetTypeName()
    {
        return assetTypeName;
    }

    public void setAssetTypeName(String assetTypeName)
    {
        this.assetTypeName = assetTypeName;
    }

    public boolean getCanRead()
    {
        return canRead;
    }

    public void setCanRead(boolean canRead)
    {
        this.canRead = canRead;
    }

    public boolean getCanWrite()
    {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite)
    {
        this.canWrite = canWrite;
    }

    public boolean getCanExecute()
    {
        return canExecute;
    }

    public void setCanExecute(boolean canExecute)
    {
        this.canExecute = canExecute;
    }

    public boolean getSymLink()
    {
        return symLink;
    }

    public void setSymLink(boolean symLink)
    {
        this.symLink = symLink;
    }

    public boolean getHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public Date getCreationTime()
    {
        return creationTime;
    }

    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }

    public Date getLastModifiedTime()
    {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime)
    {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Date getLastAccessedTime()
    {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(Date lastAccessedTime)
    {
        this.lastAccessedTime = lastAccessedTime;
    }

    public long getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(long fileSize)
    {
        this.fileSize = fileSize;
    }

    public long getRecordCount()
    {
        return recordCount;
    }

    public void setRecordCount(long recordCount)
    {
        this.recordCount = recordCount;
    }


    @Override
    public String toString()
    {
        return "FileMeasurement{" +
                "fileName='" + fileName + '\'' +
                ", pathName='" + pathName + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", fileType='" + fileType + '\'' +
                ", deployedImplementationType='" + deployedImplementationType + '\'' +
                ", encoding='" + encoding + '\'' +
                ", assetTypeName='" + assetTypeName + '\'' +
                ", canRead=" + canRead +
                ", canWrite=" + canWrite +
                ", canExecute=" + canExecute +
                ", isSymLink=" + symLink +
                ", isHidden=" + hidden +
                ", creationTime=" + creationTime +
                ", lastModifiedTime=" + lastModifiedTime +
                ", lastAccessedTime=" + lastAccessedTime +
                ", fileSize=" + fileSize +
                ", recordCount=" + recordCount +
                '}';
    }
}
