/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.InfrastructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ITInfrastructureProperties is a java bean used to create software servers, hosts, networks, and platforms.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = HostProperties.class, name = "HostProperties"),
                @JsonSubTypes.Type(value = NetworkProperties.class, name = "NetworkProperties"),
                @JsonSubTypes.Type(value = SoftwareServerProperties.class, name = "SoftwareServerProperties"),
                @JsonSubTypes.Type(value = SoftwareServerPlatformProperties.class, name = "SoftwareServerPlatformProperties"),
        })
public class ITInfrastructureProperties extends InfrastructureProperties
{
    private String userId = null;

    /**
     * Default constructor
     */
    public ITInfrastructureProperties()
    {
        super();
        super.typeName = OpenMetadataType.IT_INFRASTRUCTURE.typeName;
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ITInfrastructureProperties(ITInfrastructureProperties template)
    {
        super(template);

        if (template != null)
        {
            userId = template.getUserId();
        }
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ITInfrastructureProperties(AssetProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.IT_INFRASTRUCTURE.typeName;
    }


    /**
     * Return the identifier of the user's account
     *
     * @return string
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     * Return the identifier of the user's account.
     *
     * @param userId string
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ITInfrastructureProperties{" +
                "userId='" + userId + '\'' +
                "} " + super.toString();
    }

    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ITInfrastructureProperties that = (ITInfrastructureProperties) objectToCompare;
        return Objects.equals(userId, that.userId);
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), userId);
    }

}
