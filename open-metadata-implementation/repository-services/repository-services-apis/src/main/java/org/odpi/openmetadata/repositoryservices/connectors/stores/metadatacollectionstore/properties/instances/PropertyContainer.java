/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

/**
 * Describes the methods that any instance containing properties (eg. EntityDetail, Relationship) should provide.
 */
public interface PropertyContainer {

    /**
     * Return a copy of all of the properties for this instance.  Null means no properties exist.
     *
     * @return InstanceProperties
     */
    InstanceProperties getProperties();

    /**
     * Set up the properties for this instance.
     *
     * @param newProperties InstanceProperties object
     */
    void setProperties(InstanceProperties newProperties);

}
