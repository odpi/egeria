/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * HostProperties is a class for representing a host.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = HostClusterProperties.class, name = "HostClusterProperties"),
                @JsonSubTypes.Type(value = BareMetalComputerProperties.class, name = "BareMetalComputerProperties"),
                @JsonSubTypes.Type(value = VirtualMachineProperties.class, name = "VirtualMachineProperties"),
                @JsonSubTypes.Type(value = VirtualContainerProperties.class, name = "VirtualContainerProperties"),
        })
public class HostProperties extends ITInfrastructureProperties
{
    /**
     * Default constructor
     */
    public HostProperties()
    {
        super();
        super.typeName = OpenMetadataType.HOST.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public HostProperties(HostProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public HostProperties(AssetProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.HOST.typeName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "HostProperties{} " + super.toString();
    }
}
