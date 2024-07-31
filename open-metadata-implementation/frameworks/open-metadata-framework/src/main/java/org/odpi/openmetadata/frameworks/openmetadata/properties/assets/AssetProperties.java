/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.SupplementaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.APIProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.ITInfrastructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetProperties holds asset properties that are used for displaying details of
 * an asset in summary lists or hover text.  It includes the following properties:
 * <ul>
 *     <li>type - metadata type information for the asset</li>
 *     <li>guid - globally unique identifier for the asset</li>
 *     <li>url - external link for the asset</li>
 *     <li>qualifiedName - The official (unique) name for the asset. This is often defined by the IT systems
 *     management organization and should be used (when available) on audit logs and error messages.
 *     (qualifiedName from Referenceable - model 0010)</li>
 *     <li>displayName - A consumable name for the asset.  Often a shortened form of the assetQualifiedName
 *     for use on user interfaces and messages.   The assetDisplayName should only be used for audit logs and error
 *     messages if the assetQualifiedName is not set. (Sourced from attribute name within Asset - model 0010)</li>
 *     <li>shortDescription - short description about the asset.
 *     (Sourced from assetSummary within ConnectionsToAsset - model 0205)</li>
 *     <li>description - full description of the asset.
 *     (Sourced from attribute description within Asset - model 0010)</li>
  *     <li>classifications - list of all classifications assigned to the asset</li>
 *     <li>extendedProperties - list of properties assigned to the asset from the Asset subclasses</li>
 *     <li>additionalProperties - list of properties assigned to the asset as additional properties</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = APIProperties.class, name = "APIProperties"),
        @JsonSubTypes.Type(value = DataAssetProperties.class, name = "DataAssetProperties"),
        @JsonSubTypes.Type(value = ProcessProperties.class, name = "ProcessProperties"),
        @JsonSubTypes.Type(value = ITInfrastructureProperties.class, name = "ITInfrastructureProperties"),
})
public class AssetProperties extends SupplementaryProperties
{
    private String name                       = null;
    private String resourceName               = null;
    private String versionIdentifier          = null;
    private String resourceDescription        = null;
    private String deployedImplementationType = null;


    /**
     * Default constructor
     */
    public AssetProperties()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public AssetProperties(AssetProperties template)
    {
        super(template);

        if (template != null)
        {
            name                       = template.getName();
            versionIdentifier          = template.getVersionIdentifier();
            resourceDescription        = template.getResourceDescription();
            deployedImplementationType = template.getDeployedImplementationType();
        }
    }


    /**
     * Convert this object into an AssetProperties object.  This involves packing the properties introduced at this level
     * into the extended properties.
     *
     * @param subTypeName subtype name
     * @return asset properties
     */
    public AssetProperties cloneToAsset(String subTypeName)
    {
        AssetProperties clone = new AssetProperties(this);

        if (clone.getTypeName() == null)
        {
            clone.setTypeName(subTypeName);
        }

        return clone;
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


    public String getResourceName()
    {
        return resourceName;
    }

    public void setResourceName(String resourceName)
    {
        this.resourceName = resourceName;
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
     * Returns the stored description property for the asset.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getResourceDescription()
    {
        return resourceDescription;
    }


    /**
     * Set up the stored description property associated with the asset.
     *
     * @param resourceDescription String text
     */
    public void setResourceDescription(String resourceDescription)
    {
        this.resourceDescription = resourceDescription;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetProperties{" +
                "name='" + name + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", versionIdentifier='" + versionIdentifier + '\'' +
                ", description='" + resourceDescription + '\'' +
                ", deployedImplementationType='" + deployedImplementationType + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        AssetProperties that = (AssetProperties) objectToCompare;
        return Objects.equals(name, that.name) &&
                Objects.equals(versionIdentifier, that.versionIdentifier) &&
                Objects.equals(resourceName, that.resourceName) &&
                Objects.equals(resourceDescription, that.deployedImplementationType) &&
                Objects.equals(resourceDescription, that.resourceDescription);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), name, resourceName, versionIdentifier, resourceDescription, deployedImplementationType);
    }
}