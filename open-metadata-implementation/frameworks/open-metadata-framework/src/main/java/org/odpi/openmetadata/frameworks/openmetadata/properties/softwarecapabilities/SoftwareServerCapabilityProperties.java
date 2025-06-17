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
 * SoftwareCapabilityProperties describes a function implemented in software that is supported by an instance of IT Infrastructure.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SoftwareServerCapabilityProperties extends SoftwareCapabilityProperties
{
    /**
     * Default constructor.
     */
    public SoftwareServerCapabilityProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SoftwareServerCapabilityProperties(SoftwareServerCapabilityProperties template)
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
        return "SoftwareServerCapabilityProperties{} " + super.toString();
    }
}
