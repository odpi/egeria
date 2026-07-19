/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.fileclassifier;

import java.util.Date;

/**
 * FileClassification holds the classification information for a specific file
 */
public class FileClassification
{
    private final String  fileSystemName;
    private final String  fileName;
    private final String  canonicalPathName;
    private final String  fileExtension;
    private final String  fileAddress;
    private final Date    creationTime;
    private final Date    lastModifiedTime;
    private final Date    lastAccessedTime;
    private final boolean canRead;
    private final boolean canWrite;
    private final boolean canExecute;
    private final boolean isHidden;
    private final boolean isSymLink;
    private final String  fileType;
    private final String  deployedImplementationType;
    private final String  encoding;
    private final String  assetTypeName;
    private final long    fileSize;


    public FileClassification(String  fileSystemName,
                              String  fileName,
                              String  canonicalPathName,
                              String  fileExtension,
                              String  fileAddress,
                              Date    creationTime,
                              Date    lastModifiedTime,
                              Date    lastAccessedTime,
                              boolean canRead,
                              boolean canWrite,
                              boolean canExecute,
                              boolean isHidden,
                              boolean isSymLink,
                              String  fileType,
                              String  deployedImplementationType,
                              String  encoding,
                              String  assetTypeName,
                              long    fileSize)
    {
        this.fileSystemName             = fileSystemName;
        this.fileName                   = fileName;
        this.canonicalPathName          = canonicalPathName;
        this.fileExtension              = fileExtension;
        this.fileAddress                = fileAddress;
        this.creationTime               = creationTime;
        this.lastModifiedTime           = lastModifiedTime;
        this.lastAccessedTime           = lastAccessedTime;
        this.canRead                    = canRead;
        this.canWrite                   = canWrite;
        this.canExecute                 = canExecute;
        this.isHidden                   = isHidden;
        this.isSymLink                  = isSymLink;
        this.fileType                   = fileType;
        this.deployedImplementationType = deployedImplementationType;
        this.encoding                   = encoding;
        this.assetTypeName              = assetTypeName;
        this.fileSize                   = fileSize;
    }

    /**
     * Return the unique name of the file asset.  This is the combination of the asset type name, the file system name, and the canonical path name.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return assetTypeName + "::" + fileSystemName + ":" + canonicalPathName;
    }


    /**
     * Return the resource name of the file asset.  This is the combination of the file system name, and the canonical path name.
     *
     * @return string name
     */
    public String getResourceName()
    {
        return fileSystemName + ":" + canonicalPathName;
    }


    /**
     * Return the name of the filesystem where the file is located.
     *
     * @return string name
     */
    public String getFileSystemName()
    {
        return fileSystemName;
    }


    /**
     * Return the short file name of the file.
     *
     * @return string name
     */
    public String getFileName()
    {
        return fileName;
    }


    /**
     * Return the full pathname of the file.
     *
     * @return string name
     */
    public String getCanonicalPathName()
    {
        return canonicalPathName;
    }


    /**
     * Return the file extension of the file.
     *
     * @return letters after the "dot"
     */
    public String getFileExtension()
    {
        return fileExtension;
    }


    /**
     * Return the network address of the file.
     *
     * @return name of the file on the file system
     */
    public String getFileAddress()
    {
        return fileAddress;
    }


    /**
     * return the logical file type.
     *
     * @return string name
     */
    public String getFileType()
    {
        return fileType;
    }


    /**
     * Return the deployed implementation type.
     *
     * @return string name
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the asset type name.
     *
     * @return string name
     */
    public String getAssetTypeName()
    {
        return assetTypeName;
    }


    /**
     * Return the encoding of the file - may be null
     *
     * @return encoding standard
     */
    public String getEncoding()
    {
        return encoding;
    }


    /**
     * Return the time that the file was created.
     *
     * @return date/time
     */
    public Date getCreationTime()
    {
        return creationTime;
    }


    /**
     * Return the last time the file was modified.
     *
     * @return date/time
     */
    public Date getLastModifiedTime()
    {
        return lastModifiedTime;
    }


    /**
     * Return the last time the file was accessed.
     *
     * @return date/time
     */
    public Date getLastAccessedTime()
    {
        return lastAccessedTime;
    }


    /**
     * Is the file readable?
     *
     * @return boolean
     */
    public boolean isCanRead()
    {
        return canRead;
    }


    /**
     * Is the file writable?
     *
     * @return boolean
     */
    public boolean isCanWrite()
    {
        return canWrite;
    }


    /**
     * Is the file executable?
     *
     * @return boolean
     */
    public boolean isCanExecute()
    {
        return canExecute;
    }


    /**
     * Return whether this is a hidden file.
     *
     * @return boolean
     */
    public boolean isHidden()
    {
        return isHidden;
    }


    /**
     * Return whether is file/directory is a symbolic link.
     *
     * @return boolean
     */
    public boolean isSymLink()
    {
        return isSymLink;
    }


    /**
     * Return the size of the file in bytes.
     *
     * @return long
     */
    public long getFileSize()
    {
        return fileSize;
    }
}
