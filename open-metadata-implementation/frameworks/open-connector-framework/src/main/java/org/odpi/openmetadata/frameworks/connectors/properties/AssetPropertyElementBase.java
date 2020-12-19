/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * This property header implements any common mechanisms that all property objects need.
 */
public abstract class AssetPropertyElementBase implements Serializable
{
    private static final long     serialVersionUID = 1L;


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
         * Nothing to do.  This constructor is included so variables are added in this class at a later date
         * without impacting the subclasses.
         */
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }

        return true;
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return 1;
    }
}