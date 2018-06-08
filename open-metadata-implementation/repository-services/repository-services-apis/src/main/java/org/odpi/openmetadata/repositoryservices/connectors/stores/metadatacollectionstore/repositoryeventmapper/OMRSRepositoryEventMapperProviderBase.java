/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

/**
 * OMRSRepositoryEventMapperProviderBase provides a base class for the connector provider supporting
 * OMRSRepositoryEventMapper connectors.  It extends ConnectorProviderBase which does the creation of connector instances.
 *
 * The subclasses of OMRSRepositoryEventMapperProviderBase must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public abstract class OMRSRepositoryEventMapperProviderBase extends ConnectorProviderBase
{
    /**
     * Default Constructor
     */
    public OMRSRepositoryEventMapperProviderBase()
    {
        /*
         * Nothing to do
         */
    }
}
