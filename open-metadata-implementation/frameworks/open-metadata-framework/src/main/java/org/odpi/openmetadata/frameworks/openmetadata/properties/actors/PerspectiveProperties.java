/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The PerspectiveProperties describes the common properties of a perspective.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PerspectiveProperties extends ActorProperties
{
    /**
     * Default Constructor
     */
    public PerspectiveProperties()
    {
        super();
        super.typeName = OpenMetadataType.PERSPECTIVE.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PerspectiveProperties(PerspectiveProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PerspectiveProperties(ActorProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.PERSPECTIVE.typeName;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PerspectiveProperties{" +
                "} " + super.toString();
    }
}
