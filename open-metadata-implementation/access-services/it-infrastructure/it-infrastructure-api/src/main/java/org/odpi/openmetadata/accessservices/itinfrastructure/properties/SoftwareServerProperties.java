/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SoftwareServerProperties is a representation of the properties for a software server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DatabaseServerProperties.class, name = "DatabaseServerProperties"),
        })
public class SoftwareServerProperties extends AssetProperties
{
    private static final long    serialVersionUID = 1L;

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
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", owner='" + getOwner() + '\'' +
                ", ownerCategory=" + getOwnerCategory() +
                ", zoneMembership=" + getZoneMembership() +
                ", origin=" + getOrigin() +
                ", latestChange='" + getLatestChange() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", classifications=" + getClassifications() +
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
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), softwareServerType, softwareServerVersion, softwareServerSource, softwareServerUserId);
    }
}
