/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The ActorProperties describes the common properties of a Personal Profile, IT Profile and Team Profile.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ActorProfileProperties.class, name = "ActorProfileProperties"),
                @JsonSubTypes.Type(value = ActorRoleProperties.class, name = "ActorRoleProperties"),
                @JsonSubTypes.Type(value = UserIdentityProperties.class, name = "UserIdentityProperties"),
        })
public class ActorProperties extends ReferenceableProperties
{
    /**
     * Default Constructor
     */
    public ActorProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ACTOR.typeName);
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public ActorProperties(ReferenceableProperties template)
    {
        super (template);
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ActorProperties{" +
                "} " + super.toString();
    }
}
