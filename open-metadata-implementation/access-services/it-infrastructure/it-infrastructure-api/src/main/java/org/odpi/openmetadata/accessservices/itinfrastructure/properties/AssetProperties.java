/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.properties;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetProperties is a java bean used to create assets associated with the IT landscape.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ITInfrastructureProperties.class, name = "ITInfrastructureProperties"),
                @JsonSubTypes.Type(value = DataAssetProperties.class, name = "DataAssetProperties"),
                @JsonSubTypes.Type(value = ProcessProperties.class, name = "ProcessProperties"),
        })
public class AssetProperties extends ConfigurationItemProperties
{
    private static final long     serialVersionUID = 1L;

    private String name = null;
    private String versionIdentifier = null;
    private String displayName = null;
    private String description = null;


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
            name                         = template.getName();
            versionIdentifier            = template.getVersionIdentifier();
            displayName                  = template.getDisplayName();
            description                  = template.getDescription();
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
            return displayName;
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
     * If no display name is available then name is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        if (displayName == null)
        {
            return name;
        }

        return displayName;
    }



    /**
     * Set up the stored display name property for the asset.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the stored description property for the asset.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property associated with the asset.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
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
                       ", versionIdentifier='" + versionIdentifier + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof AssetProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        AssetProperties that = (AssetProperties) objectToCompare;

        if (name != null ? ! name.equals(that.name) : that.name != null)
        {
            return false;
        }
        if (versionIdentifier != null ? ! versionIdentifier.equals(that.versionIdentifier) : that.versionIdentifier != null)
        {
            return false;
        }
        if (displayName != null ? ! displayName.equals(that.displayName) : that.displayName != null)
        {
            return false;
        }
        return description != null ? description.equals(that.description) : that.description == null;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (versionIdentifier != null ? versionIdentifier.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
