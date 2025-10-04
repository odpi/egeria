/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.DeployedSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.DeployedConnectorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.GovernanceActionServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.RepositoryGovernanceServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.SurveyActionServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceEngineProperties defines the properties of a governance engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GovernanceActionEngineProperties.class, name = "GovernanceActionEngineProperties"),
        @JsonSubTypes.Type(value = RepositoryGovernanceEngineProperties.class, name = "RepositoryGovernanceEngineProperties"),
        @JsonSubTypes.Type(value = SurveyActionEngineProperties.class, name = "SurveyActionEngineProperties"),
        @JsonSubTypes.Type(value = WatchdogActionEngineProperties.class, name = "WatchdogActionEngineProperties"),
})
public class GovernanceEngineProperties extends EngineProperties
{
    /**
     * Default constructor
     */
    public GovernanceEngineProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GOVERNANCE_ENGINE.typeName);
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public GovernanceEngineProperties(EngineProperties template)
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
        return "GovernanceEngineProperties{} " + super.toString();
    }
}
