/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.DeployedSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DeployedAPIProperties is a class for representing an API deployed to an API Gateway or supported by an application.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DeployedAPIProperties extends DeployedSoftwareComponentProperties
{
    /**
     * Default constructor
     */
    public DeployedAPIProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DEPLOYED_API.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DeployedAPIProperties(DeployedAPIProperties template)
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
        return "DeployedAPIProperties{} " + super.toString();
    }
}
