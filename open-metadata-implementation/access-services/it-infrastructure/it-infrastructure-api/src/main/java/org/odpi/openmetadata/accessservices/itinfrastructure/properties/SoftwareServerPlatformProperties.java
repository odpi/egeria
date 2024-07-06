/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SoftwareServerPlatformProperties is a representation of the properties for a software server platform.
 * This is the process that is visible to the operating system or container.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SoftwareServerPlatformProperties extends ITInfrastructureProperties
{
    private static final String versionProperty = OpenMetadataProperty.PLATFORM_VERSION.name;
    private static final String sourceProperty  = OpenMetadataProperty.SOURCE.name;
    private static final String userIdProperty  = OpenMetadataProperty.USER_ID.name;

    private String platformVersion            = null;
    private String  platformSource  = null;
    private String  platformUserId  = null;


    /**
     * Default constructor
     */
    public SoftwareServerPlatformProperties()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SoftwareServerPlatformProperties(SoftwareServerPlatformProperties template)
    {
        super(template);

        if (template != null)
        {
            platformVersion = template.getSoftwareServerPlatformVersion();
            platformSource  = template.getSoftwareServerPlatformSource();
            platformUserId  = template.getSoftwareServerPlatformUserId();
        }
    }



    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SoftwareServerPlatformProperties(AssetProperties template)
    {
        super(template);

        if (template != null)
        {
            Map<String, Object> assetExtendedProperties = template.getExtendedProperties();

            if (assetExtendedProperties != null)
            {
                if (assetExtendedProperties.get(versionProperty) != null)
                {
                    platformVersion = assetExtendedProperties.get(versionProperty).toString();
                    assetExtendedProperties.remove(versionProperty);
                }

                if (assetExtendedProperties.get(sourceProperty) != null)
                {
                    platformSource = assetExtendedProperties.get(sourceProperty).toString();
                    assetExtendedProperties.remove(sourceProperty);
                }

                if (assetExtendedProperties.get(userIdProperty) != null)
                {
                    platformUserId = assetExtendedProperties.get(userIdProperty).toString();
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
        return this.cloneToAsset(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);
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

        if (platformVersion != null)
        {
            assetExtendedProperties.put(versionProperty, platformVersion);
        }
        if (platformSource  != null)
        {
            assetExtendedProperties.put(sourceProperty, platformSource);
        }
        if (platformUserId  != null)
        {
            assetExtendedProperties.put(userIdProperty, platformUserId);
        }

        if (! assetExtendedProperties.isEmpty())
        {
            assetProperties.setExtendedProperties(assetExtendedProperties);
        }

        return assetProperties;
    }


    /**
     * Return the version of the platform.
     *
     * @return version string
     */
    public String getSoftwareServerPlatformVersion()
    {
        return platformVersion;
    }


    /**
     * Set up the version of the platform.
     *
     * @param platformVersion version string
     */
    public void setSoftwareServerPlatformVersion(String platformVersion)
    {
        this.platformVersion = platformVersion;
    }


    /**
     * Return the source (such as vendor or operator) of the platform.
     *
     * @return string name
     */
    public String getSoftwareServerPlatformSource()
    {
        return platformSource;
    }


    /**
     * Set up the source (such as vendor or operator) of the platform.
     *
     * @param platformSource string name
     */
    public void setSoftwareServerPlatformSource(String platformSource)
    {
        this.platformSource = platformSource;
    }


    /**
     * Return the user identity of the platform.
     *
     * @return string user identity
     */
    public String getSoftwareServerPlatformUserId()
    {
        return platformUserId;
    }


    /**
     * Set up the user identity of the platform.
     *
     * @param platformUserId string user identity
     */
    public void setSoftwareServerPlatformUserId(String platformUserId)
    {
        this.platformUserId = platformUserId;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SoftwareServerPlatformProperties{" +
                "platformVersion='" + platformVersion + '\'' +
                ", platformSource='" + platformSource + '\'' +
                ", platformUserId='" + platformUserId + '\'' +
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
        SoftwareServerPlatformProperties that = (SoftwareServerPlatformProperties) objectToCompare;
        return Objects.equals(platformVersion, that.platformVersion) &&
                Objects.equals(platformSource, that.platformSource) &&
                Objects.equals(platformUserId, that.platformUserId);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), platformVersion, platformSource, platformUserId);
    }
}
