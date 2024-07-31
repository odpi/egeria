/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewFileAssetRequestBody carries the parameters for creating a new file asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
                      @JsonSubTypes.Type(value = NewCSVFileAssetRequestBody.class, name = "NewCSVFileAssetRequestBody")

              })
public class NewFileAssetRequestBody
{
    private String name              = null;
    private String resourceName      = null;
    private String versionIdentifier = null;
    private String description       = null;
    private String fullPath          = null;


    /**
     * Default constructor
     */
    public NewFileAssetRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewFileAssetRequestBody(NewFileAssetRequestBody template)
    {
        if (template != null)
        {
            name         = template.getName();
            resourceName = template.getResourceName();
            description  = template.getDescription();
            fullPath = template.getFullPath();
        }
    }


    /**
     * Return the name of the resource that this asset represents.
     *
     * @return string resource name
     */
    public String getName()
    {
        if (name == null)
        {
            return resourceName;
        }

        return name;
    }


    /**
     * Set up the name of the resource that this asset represents.
     *
     * @param name string resource name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Set up the version identifier of the resource.
     *
     * @return string version name
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Set up the version identifier of the resource.
     *
     * @param versionIdentifier string version name
     */
    public void setVersionIdentifier(String versionIdentifier)
    {
        this.versionIdentifier = versionIdentifier;
    }


    /**
     * Returns the stored display name property for the asset.
     * If no resource name is available then name is returned.
     *
     * @return String name
     */
    public String getResourceName()
    {
        if (resourceName == null)
        {
            return name;
        }

        return resourceName;
    }


    /**
     * Set up the stored display name property for the asset.
     *
     * @param resourceName String name
     */
    public void setResourceName(String resourceName)
    {
        this.resourceName = resourceName;
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
    public String getFullPath()
    {
        return fullPath;
    }


    /**
     * Set up the full path of the file - this should be unique.
     *
     * @param fullPath string name
     */
    public void setFullPath(String fullPath)
    {
        this.fullPath = fullPath;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NewFileAssetRequestBody{" +
                       "name='" + name + '\'' +
                       ", versionIdentifier='" + versionIdentifier + '\'' +
                       ", description='" + description + '\'' +
                       ", fullPath='" + fullPath + '\'' +
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
        NewFileAssetRequestBody that = (NewFileAssetRequestBody) objectToCompare;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getFullPath(), that.getFullPath());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getName(), getDescription(), getFullPath());
    }
}
