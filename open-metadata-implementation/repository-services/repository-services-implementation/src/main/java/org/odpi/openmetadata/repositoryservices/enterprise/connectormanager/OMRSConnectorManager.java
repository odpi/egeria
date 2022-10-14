/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.connectormanager;


/**
 * OMRSConnectorManager provides the methods for connector consumers to register with the connector manager.
 */
public interface OMRSConnectorManager
{
    /**
     * Register the supplied connector consumer with the connector manager.  During the registration
     * request, the connector manager will pass the connector to the local repository and
     * the connectors to all currently registered remote repositories.  Once successfully registered
     * the connector manager will call the connector consumer each time the repositories in the
     * metadata repository cluster changes.
     *
     * @param connectorConsumer OMRSConnectorConsumer interested in details of the connectors to
     *                           all repositories registered in the metadata repository cluster.
     * @return String identifier for the connectorConsumer used for the call to unregister().
     */
    String registerConnectorConsumer(OMRSConnectorConsumer    connectorConsumer);


    /**
     * Unregister a connector consumer from the connector manager so it is no longer informed of
     * changes to the metadata repository cluster.
     *
     * @param connectorConsumerId String identifier of the connector consumer returned on the
     *                             registerConnectorConsumer.
     */
    void unregisterConnectorConsumer(String   connectorConsumerId);
}
