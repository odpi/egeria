/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ITProfileRoleProperties covers a role that has been defined for an automated function.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionActorRoleProperties extends ActorRoleProperties
{

    /**
     * Default constructor
     */
    public SolutionActorRoleProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionActorRoleProperties(SolutionActorRoleProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SolutionActorRoleProperties{} " + super.toString();
    }
}
