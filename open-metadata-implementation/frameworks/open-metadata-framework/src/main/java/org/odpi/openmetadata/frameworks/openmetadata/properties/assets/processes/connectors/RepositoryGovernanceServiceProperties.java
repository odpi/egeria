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
 * RepositoryGovernanceServiceProperties defines the properties of a connector that is a RepositoryGovernanceService.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RepositoryGovernanceServiceProperties extends GovernanceServiceProperties
{
    /**
     * Default constructor
     */
    public RepositoryGovernanceServiceProperties()
    {
        super();
        super.typeName = OpenMetadataType.REPOSITORY_GOVERNANCE_SERVICE.typeName;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public RepositoryGovernanceServiceProperties(DeployedSoftwareComponentProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public RepositoryGovernanceServiceProperties(DeployedConnectorProperties template)
    {
        super(template);
    }



    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public RepositoryGovernanceServiceProperties(GovernanceServiceProperties template)
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
        return "RepositoryGovernanceServiceProperties{} " + super.toString();
    }
}
