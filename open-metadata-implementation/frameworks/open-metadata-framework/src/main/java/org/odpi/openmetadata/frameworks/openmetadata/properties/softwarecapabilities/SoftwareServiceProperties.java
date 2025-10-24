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
 * SoftwareServiceProperties describes a collection of APIs to offer services to other systems.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SoftwareServiceProperties extends SoftwareCapabilityProperties
{
    /**
     * Default constructor
     */
    public SoftwareServiceProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SOFTWARE_SERVICE.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SoftwareServiceProperties(SoftwareServiceProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SoftwareServiceProperties(SoftwareCapabilityProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SoftwareServiceProperties{} " + super.toString();
    }
}
