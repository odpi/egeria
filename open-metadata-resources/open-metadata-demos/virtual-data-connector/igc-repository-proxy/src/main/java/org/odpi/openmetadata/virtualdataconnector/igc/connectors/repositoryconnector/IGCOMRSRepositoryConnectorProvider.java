/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

/**
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The IGCOMRSRepositoryConnectorProvider is the connector provider for the IGCOMRSRepositoryConnector.
 * It extends OMRSRepositoryConnectorProviderBase which in turn extends the OCF ConnectorProviderBase.
 * ConnectorProviderBase supports the creation of connector instances.
 * <p>
 * The IGCOMRSRepositoryConnectorProvider must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class IGCOMRSRepositoryConnectorProvider extends OMRSRepositoryConnectorProviderBase {
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public IGCOMRSRepositoryConnectorProvider() {
        Class connectorClass = IGCOMRSRepositoryConnector.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}