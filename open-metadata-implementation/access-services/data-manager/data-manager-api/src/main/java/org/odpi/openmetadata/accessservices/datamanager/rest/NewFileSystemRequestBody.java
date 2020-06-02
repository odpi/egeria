/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * FileSystemProperties describes the root node of a file system.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewFileSystemRequestBody extends DataManagerOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String              displayName          = null;
    private String              description          = null;
    private String              uniqueName           = null;
    private String              fileSystemType       = null;
    private String              version              = null;
    private String              patchLevel           = null;
    private String              source               = null;
    private String              format               = null;
    private String              encryption           = null;
    private Map<String, String> additionalProperties = null;
    private String              typeName             = null;
    private Map<String, Object> extendedProperties   = null;

    /**
     * Default constructor
     */
    public NewFileSystemRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public NewFileSystemRequestBody(NewFileSystemRequestBody template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            description = template.getDescription();
            uniqueName = template.getUniqueName();
            fileSystemType = template.getFileSystemType();
            version = template.getVersion();
            patchLevel = template.getPatchLevel();
            source = template.getSource();
            format = template.getFormat();
            encryption = template.getEncryption();
            typeName = template.getTypeName();
            extendedProperties = template.getExtendedProperties();
        }
    }


    /**
     * Return the display name of the file
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name of the file.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of the file.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the file.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the full path of the file - this should be unique.
     *
     * @return string name
     */
    public String getUniqueName()
    {
        return uniqueName;
    }


    /**
     * Set up the full path of the file - this should be unique.
     *
     * @param uniqueName string name
     */
    public void setUniqueName(String uniqueName)
    {
        this.uniqueName = uniqueName;
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
     * @param type stirng name
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
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }




    /**
     * Return the subtype name for this asset - null means the type is DataFile.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the subtype name for this asset - null means the type is DataFile.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return any properties from a subclass of DataFile.
     *
     * @return map of properties
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }

        return new HashMap<>(extendedProperties);
    }


    /**
     * Set up any properties from a subclass of DataFile.
     *
     * @param extendedProperties map of properties
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NewFileSystemRequestBody{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", uniqueName='" + uniqueName + '\'' +
                ", fileSystemType='" + fileSystemType + '\'' +
                ", version='" + version + '\'' +
                ", patchLevel='" + patchLevel + '\'' +
                ", source='" + source + '\'' +
                ", format='" + format + '\'' +
                ", encryption='" + encryption + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", typeName='" + typeName + '\'' +
                ", extendedProperties=" + extendedProperties +
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
        NewFileSystemRequestBody that = (NewFileSystemRequestBody) objectToCompare;
        return Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(uniqueName, that.uniqueName) &&
                Objects.equals(fileSystemType, that.fileSystemType) &&
                Objects.equals(version, that.version) &&
                Objects.equals(patchLevel, that.patchLevel) &&
                Objects.equals(source, that.source) &&
                Objects.equals(format, that.format) &&
                Objects.equals(encryption, that.encryption) &&
                Objects.equals(additionalProperties, that.additionalProperties) &&
                Objects.equals(typeName, that.typeName) &&
                Objects.equals(extendedProperties, that.extendedProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(displayName, description, uniqueName, fileSystemType, version, patchLevel, source, format, encryption, additionalProperties, typeName, extendedProperties);
    }
}
