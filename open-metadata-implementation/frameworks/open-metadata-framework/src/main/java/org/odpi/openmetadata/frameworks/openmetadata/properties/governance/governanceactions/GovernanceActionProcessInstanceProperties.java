/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.TransientEmbeddedProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceControlProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A governance action describes the running instance of a governance action process.
 * It is linked to the first engine action that is executing the process steps with an ActionRequester relationship.
 * The subsequent engine actions are linked together using ControlFlow relationships.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionProcessInstanceProperties extends TransientEmbeddedProcessProperties
{
    /**
     * Default Constructor
     */
    public GovernanceActionProcessInstanceProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName);
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProcessInstanceProperties(GovernanceActionProcessInstanceProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceActionProperties{" +
                "} " + super.toString();
    }
}
