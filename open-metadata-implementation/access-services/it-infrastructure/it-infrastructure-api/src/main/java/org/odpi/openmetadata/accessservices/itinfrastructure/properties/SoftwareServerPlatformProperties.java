/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public class SoftwareServerPlatformProperties extends AssetProperties
{
    private static final long    serialVersionUID = 1L;

    private String  platformType    = null;
    private String  platformVersion = null;
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
            platformType    = template.getSoftwareServerType();
            platformVersion = template.getSoftwareServerVersion();
            platformSource  = template.getSoftwareServerSource();
            platformUserId  = template.getSoftwareServerUserId();
        }
    }


    /**
     * Return the type description for the server's type.
     *
     * @return type description string
     */
    public String getSoftwareServerType()
    {
        return platformType;
    }


    /**
     * Set up the type description for the server's type.
     *
     * @param platformType type description string
     */
    public void setSoftwareServerType(String platformType)
    {
        this.platformType = platformType;
    }


    /**
     * Return the version of the server.
     *
     * @return version string
     */
    public String getSoftwareServerVersion()
    {
        return platformVersion;
    }


    /**
     * Set up the version of the server.
     *
     * @param platformVersion version string
     */
    public void setSoftwareServerVersion(String platformVersion)
    {
        this.platformVersion = platformVersion;
    }


    /**
     * Return the source (such as vendor or operator) of the server.
     *
     * @return string name
     */
    public String getSoftwareServerSource()
    {
        return platformSource;
    }


    /**
     * Set up the source (such as vendor or operator) of the server.
     *
     * @param platformSource string name
     */
    public void setSoftwareServerSource(String platformSource)
    {
        this.platformSource = platformSource;
    }


    /**
     * Return the user identity of the server.
     *
     * @return string user identity
     */
    public String getSoftwareServerUserId()
    {
        return platformUserId;
    }


    /**
     * Set up the user identity of the server.
     *
     * @param platformUserId string user identity
     */
    public void setSoftwareServerUserId(String platformUserId)
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
                       "platformType='" + platformType + '\'' +
                       ", platformVersion='" + platformVersion + '\'' +
                       ", platformSource='" + platformSource + '\'' +
                       ", platformUserId='" + platformUserId + '\'' +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", classifications=" + getClassifications() +
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
        SoftwareServerPlatformProperties that = (SoftwareServerPlatformProperties) objectToCompare;
        return Objects.equals(platformType, that.platformType) &&
                Objects.equals(platformVersion, that.platformVersion) &&
                Objects.equals(platformSource, that.platformSource) &&
                Objects.equals(platformUserId, that.platformUserId);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), platformType, platformVersion, platformSource, platformUserId);
    }
}
