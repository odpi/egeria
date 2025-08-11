/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.DeployedSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceServiceProperties defines the properties of a connector that is in a runnable state.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GovernanceActionServiceProperties.class, name = "GovernanceActionServiceProperties"),
        @JsonSubTypes.Type(value = RepositoryGovernanceServiceProperties.class, name = "RepositoryGovernanceServiceProperties"),
        @JsonSubTypes.Type(value = SurveyActionServiceProperties.class, name = "SurveyActionServiceProperties"),
})
public class GovernanceServiceProperties extends DeployedConnectorProperties
{
    /**
     * Default constructor
     */
    public GovernanceServiceProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GOVERNANCE_SERVICE.typeName);;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public GovernanceServiceProperties(DeployedSoftwareComponentProperties template)
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
        return "GovernanceServiceProperties{} " + super.toString();
    }
}
