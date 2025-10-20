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
 * ReportingEngineProperties describes the Reporting engine capability of a server
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportingEngineProperties extends SoftwareCapabilityProperties
{
    /**
     * Default constructor
     */
    public ReportingEngineProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.REPORTING_ENGINE.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ReportingEngineProperties(SoftwareCapabilityProperties template)
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
        return "ReportingEngineProperties{} " + super.toString();
    }
}
