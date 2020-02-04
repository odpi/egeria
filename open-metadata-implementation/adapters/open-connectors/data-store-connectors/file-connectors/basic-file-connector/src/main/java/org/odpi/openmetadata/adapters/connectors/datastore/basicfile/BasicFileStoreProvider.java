/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.basicfile;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;


/**
 * BasicFileStoreProvider is the OCF connector provider for the basic file store connector.
 */
public class BasicFileStoreProvider extends ConnectorProviderBase
{
    private static final String  connectorTypeGUID = "ba213761-f5f5-4cf5-a95f-6150aef09e0b";
    private static final String  connectorTypeName = "Basic File Store Connector";
    private static final String  connectorTypeDescription = "Connector supports reading of Files.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public BasicFileStoreProvider()
    {
        Class<BasicFileStoreConnector>  connectorClass = BasicFileStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}
