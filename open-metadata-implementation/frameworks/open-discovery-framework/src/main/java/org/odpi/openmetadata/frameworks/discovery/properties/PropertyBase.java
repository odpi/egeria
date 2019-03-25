/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This property header implements any common mechanisms that all property objects need.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Classification.class, name = "Classification"),
                @JsonSubTypes.Type(value = ElementHeader.class, name = "ElementHeader"),
                @JsonSubTypes.Type(value = ElementType.class, name = "ElementType"),
                @JsonSubTypes.Type(value = DiscoveryContext.class, name = "DiscoveryContext")
        })
public abstract class PropertyBase implements Serializable
{
    private static final long     serialVersionUID = 1L;

    /**
     * Typical Constructor
     */
    public PropertyBase()
    {
        /*
         * Nothing to do.  This constructor is included so variables are added in this class at a later date
         * without impacting the subclasses.
         */
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public PropertyBase(PropertyBase template)
    {
        /*
         * Nothing to do.  This constructor is included so variables are added in this class at a later date
         * without impacting the subclasses.
         */
    }
}