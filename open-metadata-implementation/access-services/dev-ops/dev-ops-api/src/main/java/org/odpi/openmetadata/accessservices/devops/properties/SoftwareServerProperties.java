/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.devops.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SoftwareServerProperties is a representation of the properties for a software server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SoftwareServerProperties extends ITInfrastructureProperties
{
    private static final String deployedImplementationTypeProperty = "deployedImplementationType";
    private static final String versionProperty                    = "serverVersion";
    private static final String sourceProperty                     = "source";
    private static final String userIdProperty                     = "userId";

    private String  softwareServerType    = null;
    private String  softwareServerVersion = null;
    private String  softwareServerSource  = null;
    private String  softwareServerUserId  = null;


    /**
     * Default constructor
     */
    public SoftwareServerProperties()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SoftwareServerProperties(SoftwareServerProperties template)
    {
        super(template);

        if (template != null)
        {
            softwareServerType    = template.getSoftwareServerType();
            softwareServerVersion = template.getSoftwareServerVersion();
            softwareServerSource  = template.getSoftwareServerSource();
            softwareServerUserId  = template.getSoftwareServerUserId();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SoftwareServerProperties(AssetProperties template)
    {
        super(template);

        if (template != null)
        {
            Map<String, Object> assetExtendedProperties = template.getExtendedProperties();

            if (assetExtendedProperties != null)
            {
                if (assetExtendedProperties.get(deployedImplementationTypeProperty) != null)
                {
                    softwareServerType = assetExtendedProperties.get(deployedImplementationTypeProperty).toString();
                    assetExtendedProperties.remove(deployedImplementationTypeProperty);
                }

                if (assetExtendedProperties.get(versionProperty) != null)
                {
                    softwareServerVersion = assetExtendedProperties.get(versionProperty).toString();
                    assetExtendedProperties.remove(versionProperty);
                }

                if (assetExtendedProperties.get(sourceProperty) != null)
                {
                    softwareServerSource = assetExtendedProperties.get(sourceProperty).toString();
                    assetExtendedProperties.remove(sourceProperty);
                }

                if (assetExtendedProperties.get(userIdProperty) != null)
                {
                    softwareServerUserId = assetExtendedProperties.get(userIdProperty).toString();
                    assetExtendedProperties.remove(userIdProperty);
                }

                super.setExtendedProperties(assetExtendedProperties);
            }
        }
    }


    /**
     * Convert this object into an AssetProperties object.  This involves packing the additional properties introduced at this level
     * into the extended properties.
     *
     * @return asset properties
     */
    public AssetProperties cloneToAsset()
    {
        return this.cloneToAsset("SoftwareServer");
    }


    /**
     * Convert this object into an AssetProperties object.  This involves packing the properties introduced at this level
     * into the extended properties.
     *
     * @param subTypeName subtype name
     * @return asset properties
     */
    @Override
    public AssetProperties cloneToAsset(String subTypeName)
    {
        AssetProperties assetProperties = super.cloneToAsset(subTypeName);

        Map<String, Object> assetExtendedProperties = assetProperties.getExtendedProperties();

        if (assetExtendedProperties == null)
        {
            assetExtendedProperties = new HashMap<>();
        }

        if (softwareServerType != null)
        {
            assetExtendedProperties.put(deployedImplementationTypeProperty, softwareServerType);
        }
        if (softwareServerVersion != null)
        {
            assetExtendedProperties.put(versionProperty, softwareServerVersion);
        }
        if (softwareServerSource  != null)
        {
            assetExtendedProperties.put(sourceProperty, softwareServerSource);
        }
        if (softwareServerUserId  != null)
        {
            assetExtendedProperties.put(userIdProperty, softwareServerUserId);
        }

        if (! assetExtendedProperties.isEmpty())
        {
            assetProperties.setExtendedProperties(assetExtendedProperties);
        }

        return assetProperties;
    }


    /**
     * Return the type description for the server's type.
     *
     * @return type description string
     */
    public String getSoftwareServerType()
    {
        return softwareServerType;
    }


    /**
     * Set up the type description for the server's type.
     *
     * @param softwareServerType type description string
     */
    public void setSoftwareServerType(String softwareServerType)
    {
        this.softwareServerType = softwareServerType;
    }


    /**
     * Return the version of the server.
     *
     * @return version string
     */
    public String getSoftwareServerVersion()
    {
        return softwareServerVersion;
    }


    /**
     * Set up the version of the server.
     *
     * @param softwareServerVersion version string
     */
    public void setSoftwareServerVersion(String softwareServerVersion)
    {
        this.softwareServerVersion = softwareServerVersion;
    }


    /**
     * Return the source (such as vendor or operator) of the server.
     *
     * @return string name
     */
    public String getSoftwareServerSource()
    {
        return softwareServerSource;
    }


    /**
     * Set up the source (such as vendor or operator) of the server.
     *
     * @param softwareServerSource string name
     */
    public void setSoftwareServerSource(String softwareServerSource)
    {
        this.softwareServerSource = softwareServerSource;
    }


    /**
     * Return the user identity of the server.
     *
     * @return string user identity
     */
    public String getSoftwareServerUserId()
    {
        return softwareServerUserId;
    }


    /**
     * Set up the user identity of the server.
     *
     * @param softwareServerUserId string user identity
     */
    public void setSoftwareServerUserId(String softwareServerUserId)
    {
        this.softwareServerUserId = softwareServerUserId;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SoftwareServerProperties{" +
                       "softwareServerType='" + softwareServerType + '\'' +
                       ", softwareServerVersion='" + softwareServerVersion + '\'' +
                       ", softwareServerSource='" + softwareServerSource + '\'' +
                       ", softwareServerUserId='" + softwareServerUserId + '\'' +
                       ", displayName='" + getName() + '\'' +
                       ", description='" + getDescription() + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SoftwareServerProperties that = (SoftwareServerProperties) objectToCompare;
        return Objects.equals(softwareServerType, that.softwareServerType) &&
                Objects.equals(softwareServerVersion, that.softwareServerVersion) &&
                Objects.equals(softwareServerSource, that.softwareServerSource) &&
                Objects.equals(softwareServerUserId, that.softwareServerUserId);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), softwareServerType, softwareServerVersion, softwareServerSource, softwareServerUserId);
    }
}
