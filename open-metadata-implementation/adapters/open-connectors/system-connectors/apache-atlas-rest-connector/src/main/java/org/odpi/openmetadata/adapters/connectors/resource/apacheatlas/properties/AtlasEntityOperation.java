/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

/**
 * AtlasEntityOperation describes the change to a collection of entities.
 */
public enum AtlasEntityOperation
{
    /**
     * The entity has been created.
     */
    CREATE,

    /**
     * The entity has been update.
     */
    UPDATE,

    /**
     * The entity has been partially updated.
     */
    PARTIAL_UPDATE,

    /**
     * The entity has been deleted.
     */
    DELETE,

    /**
     * The entity has been purged.
     */
    PURGE
}
