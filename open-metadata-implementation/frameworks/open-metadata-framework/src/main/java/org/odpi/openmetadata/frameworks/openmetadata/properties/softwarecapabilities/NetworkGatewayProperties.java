/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NetworkGatewayProperties is a class for representing a network gateway.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NetworkGatewayProperties extends SoftwareCapabilityProperties
{
    /**
     * Default constructor
     */
    public NetworkGatewayProperties()
    {
        super();
        super.typeName = OpenMetadataType.NETWORK_GATEWAY.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public NetworkGatewayProperties(NetworkGatewayProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public NetworkGatewayProperties(SoftwareCapabilityProperties template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "NetworkGatewayProperties{} " + super.toString();
    }
}
