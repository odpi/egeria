/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.avrofile;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;


/**
 * AvroFileStoreProvider is the OCF connector provider for the avro file store connector.
 */
public class AvroFileStoreProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "edacbe33-feb1-4a74-84e8-da249455368d";
    static final String  connectorTypeName = "Avro File Store Connector";
    static final String  connectorTypeDescription = "Connector supports reading of Avro Files.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public AvroFileStoreProvider()
    {
        Class<?>    connectorClass = AvroFileStoreConnector.class;

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
