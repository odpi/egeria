/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

/**
 * Describes the methods that an instance needs to implement to support calculating differences.
 */
public interface DifferenceCalculator {

    /**
     * Retrieve the differences between this instance and the one provided.
     *
     * @param other the other instance to compare against
     * @param <T> the type of instance
     * @return InstanceDifferences
     */
    <T extends InstanceAuditHeader> InstanceDifferences differences(T other);

}
