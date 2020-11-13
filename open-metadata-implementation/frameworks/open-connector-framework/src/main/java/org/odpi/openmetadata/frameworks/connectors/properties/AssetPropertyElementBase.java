/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import java.io.Serializable;
import java.util.UUID;


/**
 * This property header implements any common mechanisms that all property objects need.
 */
public abstract class AssetPropertyElementBase implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private              int      hashCode = UUID.randomUUID().hashCode();


    /**
     * Typical Constructor
     */
    public AssetPropertyElementBase()
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
    public AssetPropertyElementBase(AssetPropertyElementBase template)
    {
        /*
         * The hashCode value is replaced with the value from the template so the template object and this
         * new object will return equals set to true.
         */
        if (template != null)
        {
            hashCode = template.hashCode();
        }
    }


    /**
     * Provide a common implementation of hashCode for all OCF properties objects.  The UUID is unique and
     * is randomly assigned and so its hashCode is as good as anything to describe the hash code of the properties
     * object.  This method may be overridden by subclasses.
     */
    public int hashCode()
    {
        return hashCode;
    }


    /**
     * Provide a common implementation of equals for all OCF properties objects.  The UUID is unique and
     * is randomly assigned and so its hashCode is as good as anything to evaluate the equality of the properties
     * object.
     *
     * @param object object to test
     * @return boolean flag
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }

        AssetPropertyElementBase that = (AssetPropertyElementBase) object;

        return hashCode == that.hashCode;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "PropertyElementBase{" +
                "hashCode=" + hashCode +
                '}';
    }
}