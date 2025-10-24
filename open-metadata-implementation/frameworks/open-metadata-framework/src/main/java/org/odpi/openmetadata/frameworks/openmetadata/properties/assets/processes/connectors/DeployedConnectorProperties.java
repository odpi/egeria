/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.DeployedSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DeployedConnectorProperties defines the properties of a connector that is in a runnable state.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = IntegrationConnectorProperties.class, name = "IntegrationConnectorProperties"),
})
public class DeployedConnectorProperties extends DeployedSoftwareComponentProperties
{
    /**
     * Default constructor
     */
    public DeployedConnectorProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DEPLOYED_CONNECTOR.typeName);;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public DeployedConnectorProperties(DeployedSoftwareComponentProperties template)
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
        return "DeployedConnectorProperties{} " + super.toString();
    }
}
