/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * NewFileSystemProperties describes the root node of a file system.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewFileSystemRequestBody extends AssetOwnerOMASAPIRequestBody
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
        NewFileSystemRequestBody that = (NewFileSystemRequestBody) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getUniqueName(), that.getUniqueName()) &&
                       Objects.equals(getFileSystemType(), that.getFileSystemType()) &&
                       Objects.equals(getVersion(), that.getVersion()) &&
                       Objects.equals(getPatchLevel(), that.getPatchLevel()) &&
                       Objects.equals(getSource(), that.getSource()) &&
                       Objects.equals(getFormat(), that.getFormat()) &&
                       Objects.equals(getAdditionalProperties(), that.getAdditionalProperties()) &&
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
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getUniqueName(),
                            getFileSystemType(), getVersion(), getPatchLevel(), getSource(),
                            getFormat(), getAdditionalProperties(), getEncryption());
    }
}
