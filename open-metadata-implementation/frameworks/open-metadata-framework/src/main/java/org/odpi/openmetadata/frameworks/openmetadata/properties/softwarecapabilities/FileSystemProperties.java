/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * FileSystemProperties describes the root node of a file system.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FileSystemProperties extends ResourceManagerProperties
{
    private String format              = null;
    private String encryption          = null;
    private String canonicalMountPoint = null;
    private String localMountPoint     = null;


    /**
     * Default constructor
     */
    public FileSystemProperties()
    {
        super();
        super.typeName = OpenMetadataType.FILE_SYSTEM.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public FileSystemProperties(FileSystemProperties template)
    {
        super(template);

        if (template != null)
        {
            format = template.getFormat();
            encryption = template.getEncryption();
            canonicalMountPoint = template.getCanonicalMountPoint();
            localMountPoint = template.getLocalMountPoint();
        }
    }


    /**
     * Return the format of the file system.
     *
     * @return string name
     */
    public String getFormat()
    {
        return format;
    }


    /**
     * Set up the format of the file system.
     *
     * @param format string name
     */
    public void setFormat(String format)
    {
        this.format = format;
    }


    /**
     * Return the type of encryption used on the file system (or null is unencrypted).
     *
     * @return encryption type
     */
    public String getEncryption()
    {
        return encryption;
    }


    /**
     * Set up the type of encryption used on the file system (or null is unencrypted).
     *
     * @param encryption encryption type
     */
    public void setEncryption(String encryption)
    {
        this.encryption = encryption;
    }


    /**
     * Return the root of the path name that has been used in the metadata elements for files in this file system.
     *
     * @return string
     */
    public String getCanonicalMountPoint()
    {
        return canonicalMountPoint;
    }


    /**
     * Set up the root of the path name that has been used in the metadata elements for files in this file system.
     *
     * @param canonicalMountPoint string
     */
    public void setCanonicalMountPoint(String canonicalMountPoint)
    {
        this.canonicalMountPoint = canonicalMountPoint;
    }


    /**
     * Return the root of the path name to substitute when accessing files.  Basically remove the top of the
     * file's path name that matches the canonical mount point and replace it with this value.
     *
     * @return string
     */
    public String getLocalMountPoint()
    {
        return localMountPoint;
    }


    /**
     * Set up the part of the local file path name that needs to be replaced by the path name in the
     * canonical mount point.
     *
     * @param localMountPoint string
     */
    public void setLocalMountPoint(String localMountPoint)
    {
        this.localMountPoint = localMountPoint;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "FileSystemProperties{" +
                "format='" + format + '\'' +
                ", encryption='" + encryption + '\'' +
                ", canonicalMountPoint='" + canonicalMountPoint + '\'' +
                ", localMountPoint='" + localMountPoint + '\'' +
                "} " + super.toString();
    }



    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        FileSystemProperties that = (FileSystemProperties) objectToCompare;
        return Objects.equals(format, that.format) &&
                Objects.equals(encryption, that.encryption) &&
                Objects.equals(canonicalMountPoint, that.canonicalMountPoint) &&
                Objects.equals(localMountPoint, that.localMountPoint);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), format, encryption, canonicalMountPoint, localMountPoint);
    }
}
