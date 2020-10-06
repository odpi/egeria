/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;


import com.fasterxml.jackson.annotation.*;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * FileSystemProperties describes the root node of a file system.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class FileSystemProperties extends SoftwareServerCapabilityProperties
{
    private static final long    serialVersionUID = 1L;

    private String              fileSystemType = null;
    private String              version = null;
    private String              patchLevel = null;
    private String              source = null;
    private String              format = null;
    private String              encryption = null;


    /**
     * Default constructor
     */
    public FileSystemProperties()
    {
        super();
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
            fileSystemType = template.getFileSystemType();
            version = template.getVersion();
            patchLevel = template.getPatchLevel();
            source = template.getSource();
            format = template.getFormat();
            encryption = template.getEncryption();
        }
    }


    /**
     * Return the type of the file system.
     *
     * @return string name
     */
    public String getFileSystemType()
    {
        return fileSystemType;
    }


    /**
     * Set up the type of the file system.
     *
     * @param type string name
     */
    public void setFileSystemType(String type)
    {
        this.fileSystemType = type;
    }


    /**
     * Return the version number of the file system.
     *
     * @return string version identifier
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the version number of the file system.
     *
     * @param version string version identifier
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the patch level of the file system.
     *
     * @return string version identifier
     */
    public String getPatchLevel()
    {
        return patchLevel;
    }


    /**
     * Set up the patch level of the file system.
     *
     * @param patchLevel string version identifier
     */
    public void setPatchLevel(String patchLevel)
    {
        this.patchLevel = patchLevel;
    }


    /**
     * Return the source of the file system.
     *
     * @return string name
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the source of the file system.
     *
     * @param source string name
     */
    public void setSource(String source)
    {
        this.source = source;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "FileSystemProperties{" +
                "fileSystemType='" + fileSystemType + '\'' +
                ", version='" + version + '\'' +
                ", patchLevel='" + patchLevel + '\'' +
                ", source='" + source + '\'' +
                ", format='" + format + '\'' +
                ", encryption='" + encryption + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        FileSystemProperties that = (FileSystemProperties) objectToCompare;
        return Objects.equals(getFileSystemType(), that.getFileSystemType()) &&
                       Objects.equals(getVersion(), that.getVersion()) &&
                       Objects.equals(getPatchLevel(), that.getPatchLevel()) &&
                       Objects.equals(getSource(), that.getSource()) &&
                       Objects.equals(getFormat(), that.getFormat()) &&
                       Objects.equals(getEncryption(), that.getEncryption());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getFileSystemType(), getVersion(), getPatchLevel(), getSource(),
                            getFormat(),
                            getEncryption());
    }
}
