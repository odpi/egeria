/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.InfrastructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ITInfrastructureProperties is a java bean used to create software servers, hosts and platforms.
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ITInfrastructureProperties{} " + super.toString();
    }
}
