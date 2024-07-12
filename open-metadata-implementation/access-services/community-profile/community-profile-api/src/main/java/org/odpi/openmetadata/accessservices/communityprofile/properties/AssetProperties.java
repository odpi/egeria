/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetProperties describes an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetProperties extends ReferenceableProperties
{
    private String name                       = null;
    private String versionIdentifier          = null;
    private String description                = null;
    private String deployedImplementationType = null;
    private Date   dateAssetCreated           = null;
    private Date   dateAssetLastUpdated       = null;


    /**
     * Default constructor
     */
    public AssetProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetProperties(AssetProperties template)
    {
        super(template);

        if (template != null)
        {
            this.name = template.getName();
            this.versionIdentifier = template.getVersionIdentifier();
            this.description = template.getDescription();
            this.deployedImplementationType = template.getDeployedImplementationType();
            this.dateAssetCreated = template.getDateAssetCreated();
            this.dateAssetLastUpdated = template.getDateAssetLastUpdated();
        }
    }


    /**
     * Return the name of the resource.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the resource.
     *
     * @param name string name
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
     * Return the description of the asset.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the asset.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }



    /**
     * Retrieve the name of the technology used for this data asset.
     *
     * @return string name
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Set up the name of the technology used for this data asset.
     *
     * @param deployedImplementationType string name
     */
    public void setDeployedImplementationType(String deployedImplementationType)
    {
        this.deployedImplementationType = deployedImplementationType;
    }


    /**
     * Return the date that the asset was created.
     *
     * @return date
     */
    public Date getDateAssetCreated()
    {
        if (dateAssetCreated == null)
        {
            return null;
        }
        else
        {
            return new Date(dateAssetCreated.getTime());
        }
    }


    /**
     * Set up the date that the asset was created.
     *
     * @param dateAssetCreated date
     */
    public void setDateAssetCreated(Date dateAssetCreated)
    {
        this.dateAssetCreated = dateAssetCreated;
    }


    /**
     * Return the date that the asset was last updated.
     *
     * @return date
     */
    public Date getDateAssetLastUpdated()
    {
        if (dateAssetLastUpdated == null)
        {
            return null;
        }
        else
        {
            return new Date(dateAssetLastUpdated.getTime());
        }
    }


    /**
     * Set up the date that the asset was last updated.
     *
     * @param dateAssetLastUpdated date
     */
    public void setDateAssetLastUpdated(Date dateAssetLastUpdated)
    {
        this.dateAssetLastUpdated = dateAssetLastUpdated;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetProperties{" +
                "name='" + name + '\'' +
                ", versionIdentifier='" + versionIdentifier + '\'' +
                ", description='" + description + '\'' +
                ", deployedImplementationType='" + deployedImplementationType + '\'' +
                ", dateAssetCreated=" + dateAssetCreated +
                ", dateAssetLastUpdated=" + dateAssetLastUpdated +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        AssetProperties that = (AssetProperties) objectToCompare;
        return Objects.equals(name, that.name) && Objects.equals(versionIdentifier, that.versionIdentifier) && Objects.equals(description, that.description) && Objects.equals(deployedImplementationType, that.deployedImplementationType) && Objects.equals(dateAssetCreated, that.dateAssetCreated) && Objects.equals(dateAssetLastUpdated, that.dateAssetLastUpdated);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), name, versionIdentifier, description, deployedImplementationType, dateAssetCreated, dateAssetLastUpdated);
    }
}
