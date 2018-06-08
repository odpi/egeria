/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This property header implements any common mechanisms that all property objects need.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class PropertyBase implements Serializable
{
    private static final long     serialVersionUID = 1L;
    private static final int      hashCode = UUID.randomUUID().hashCode();


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
     * Copy/clone Constructor
     *
     * @param template - object being copied
     */
    public PropertyBase(PropertyBase template)
    {
        /*
         * Nothing to do.  This constructor is included so variables are added in this class at a later date
         * without impacting the subclasses.
         */
    }


    /**
     * Provide a common implementation of hashCode for all OCF properties objects.  The UUID is unique and
     * is randomly assigned and so its hashCode is as good as anything to describe the hash code of the properties
     * object.
     */
    public int hashCode()
    {
        return hashCode;
    }
}