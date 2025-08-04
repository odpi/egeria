/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AuthorizationManagerProperties describe the properties for a capability deployed to a software server.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationManagerProperties extends SoftwareCapabilityProperties
{
    /**
     * Default constructor.
     */
    public AuthorizationManagerProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.AUTHORIZATION_MANAGER.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public AuthorizationManagerProperties(AuthorizationManagerProperties template)
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
        return "AuthorizationManagerProperties{" +
                "} " + super.toString();
    }
}
