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
}