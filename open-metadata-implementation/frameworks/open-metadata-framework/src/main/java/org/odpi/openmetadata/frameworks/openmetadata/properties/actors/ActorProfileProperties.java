/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The ActorProfileProperties describes the common properties of a Personal Profile, IT Profile and Team Profile.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = PersonProperties.class, name = "PersonProperties"),
                @JsonSubTypes.Type(value = TeamProperties.class, name = "TeamProperties"),
                @JsonSubTypes.Type(value = ITProfileProperties.class, name = "ITProfileProperties"),
        })
public class ActorProfileProperties extends ActorProperties
{
    /**
     * Default Constructor
     */
    public ActorProfileProperties()
    {
        super();
        super.typeName = OpenMetadataType.ACTOR_PROFILE.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActorProfileProperties(ActorProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ActorProfileProperties{" +
                "} " + super.toString();
    }
}
