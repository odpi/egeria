/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.metadataextractor.cassandra;

import org.odpi.openmetadata.adapters.connectors.datastore.cassandra.CassandraDataStoreConnector;
import org.odpi.openmetadata.dataplatformservices.api.DataPlatformMetadataExtractorProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;


/**
 * The type Apache Cassandra Database Metadata Extractor provider.
 */
public class CassandraMetadataExtractorProvider extends DataPlatformMetadataExtractorProvider {


    static final String  connectorTypeGUID = "0a8e2440-d07d-4058-9247-56e4167eeb13";
    static final String  connectorTypeName = "Cassandra Metadata Extractor Connector";
    static final String  connectorTypeDescription = "Connector listens metadata changes from Apache Cassandra Database";

    /**
     * Typical constructor
     */
    public CassandraMetadataExtractorProvider() {

        super();
        Class<?> connectorClass = CassandraDataStoreConnector.class;

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
