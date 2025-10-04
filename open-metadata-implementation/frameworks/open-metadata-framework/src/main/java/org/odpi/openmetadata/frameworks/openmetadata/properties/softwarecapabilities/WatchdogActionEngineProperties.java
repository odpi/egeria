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
 * WatchdogActionEngineProperties defines the properties of a connector that is a WatchdogActionEngine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class WatchdogActionEngineProperties extends GovernanceEngineProperties
{
    /**
     * Default constructor
     */
    public WatchdogActionEngineProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.WATCHDOG_ACTION_ENGINE.typeName);;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public WatchdogActionEngineProperties(GovernanceEngineProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public WatchdogActionEngineProperties(EngineProperties template)
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
        return "WatchdogActionEngineProperties{} " + super.toString();
    }
}
