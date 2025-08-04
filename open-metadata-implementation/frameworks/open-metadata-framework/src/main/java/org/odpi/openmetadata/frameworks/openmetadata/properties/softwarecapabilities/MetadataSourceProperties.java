/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MetadataSourceProperties describe the properties for a capability that is supplying external metadata source
 * information to the open metadata ecosystem.  This entity is stored as a SoftwareCapability and
 * its GUID and qualifiedName is passed as the externalSourceGUID and externalSourceName on the profile
 * management requests.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataSourceProperties extends SoftwareCapabilityProperties
{
    /**
     * Default constructor.
     */
    public MetadataSourceProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SOFTWARE_CAPABILITY.typeName);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MetadataSourceProperties{" +
                "} " + super.toString();
    }
}
