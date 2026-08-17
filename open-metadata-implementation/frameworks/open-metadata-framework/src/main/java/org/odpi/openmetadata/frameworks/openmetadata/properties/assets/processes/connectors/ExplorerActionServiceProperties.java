/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.DeployedSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExplorerActionServiceProperties defines the properties of a connector that is an ExplorerActionService.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExplorerActionServiceProperties extends GovernanceServiceProperties
{
    /**
     * Default constructor
     */
    public ExplorerActionServiceProperties()
    {
        super();
        super.typeName = OpenMetadataType.EXPLORER_ACTION_SERVICE.typeName;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public ExplorerActionServiceProperties(ExplorerActionServiceProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public ExplorerActionServiceProperties(DeployedConnectorProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.EXPLORER_ACTION_SERVICE.typeName;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public ExplorerActionServiceProperties(DeployedSoftwareComponentProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.EXPLORER_ACTION_SERVICE.typeName;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public ExplorerActionServiceProperties(GovernanceServiceProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.EXPLORER_ACTION_SERVICE.typeName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExplorerActionServiceProperties{} " + super.toString();
    }
}
