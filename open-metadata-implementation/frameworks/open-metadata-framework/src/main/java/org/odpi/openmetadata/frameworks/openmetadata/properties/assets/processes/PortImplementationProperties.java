/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PortImplementationProperties describes a concrete port
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PortImplementationProperties extends PortProperties
{
    /**
     * Default constructor
     */
    public PortImplementationProperties()
    {
        super();
        super.typeName = OpenMetadataType.PORT_IMPLEMENTATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public PortImplementationProperties(PortImplementationProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public PortImplementationProperties(PortProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.PORT_IMPLEMENTATION.typeName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "PortImplementationProperties{} " + super.toString();
    }
}
