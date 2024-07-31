/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

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
    private static final String versionProperty = OpenMetadataProperty.SERVER_VERSION.name;
    private static final String sourceProperty  = OpenMetadataProperty.SOURCE.name;
    private static final String userIdProperty  = OpenMetadataProperty.USER_ID.name;

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
        AssetProperties assetProperties = super.cloneToAsset("SoftwareServer");

        Map<String, Object> extendedProperties = assetProperties.getExtendedProperties();

        if (extendedProperties == null)
        {
            extendedProperties = new HashMap<>();
        }

        if (softwareServerVersion != null)
        {
            extendedProperties.put(versionProperty, softwareServerVersion);
        }

        if (softwareServerSource != null)
        {
            extendedProperties.put(sourceProperty, softwareServerSource);
        }

        if (softwareServerUserId != null)
        {
            extendedProperties.put(userIdProperty, softwareServerUserId);
        }


        if (! extendedProperties.isEmpty())
        {
            assetProperties.setExtendedProperties(extendedProperties);
        }

        return assetProperties;
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

        assetProperties.setExtendedProperties(assetExtendedProperties);

        return assetProperties;
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
                "softwareServerVersion='" + softwareServerVersion + '\'' +
                ", softwareServerSource='" + softwareServerSource + '\'' +
                ", softwareServerUserId='" + softwareServerUserId + '\'' +
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
        SoftwareServerProperties that = (SoftwareServerProperties) objectToCompare;
        return Objects.equals(softwareServerVersion, that.softwareServerVersion) &&
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
        return Objects.hash(super.hashCode(), softwareServerVersion, softwareServerSource, softwareServerUserId);
    }
}
