/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors.datastoreconnectors;

import org.odpi.openmetadata.adapters.connectors.datastore.avrofile.AvroFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.cassandra.CassandraDataStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.archiveutilities.openconnectors.base.OpenConnectorArchiveBuilder;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;

import java.util.Date;

/**
 * DataStoreConnectorsArchiveBuilder provides the support to build an open metadata archive that
 * contains default connector types for each of the data connectors supported by ODPi Egeria.
 * The information for these connector types is extracted from the Connector Provider implementations.
 */
public class DataStoreConnectorsArchiveBuilder extends OpenConnectorArchiveBuilder
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "acdc5637-92a7-4926-b47b-a3d407546f89";
    private static final String                  archiveRootName    = "DataStoreConnectors";
    private static final String                  archiveName        = "Data Store Open Connector Types";
    private static final String                  archiveLicense     = "Apache 2.0";
    private static final String                  archiveDescription = "Connector type definitions for data store connectors that follow the Open " +
                                                                      "Connector Framework (OCF).";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "ODPi Egeria";
    private static final Date                    creationDate       = new Date(1570383395115L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";


    /**
     * Default constructor pushes all archive header values to the superclass
     */
    public DataStoreConnectorsArchiveBuilder()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              archiveType,
              archiveRootName,
              originatorName,
              archiveLicense,
              creationDate,
              versionNumber,
              versionName);
    }


    /**
     * Returns the open metadata type archive containing all of the elements extracted from the connector
     * providers of the featured open connectors.
     *
     * @return populated open metadata archive object
     */
    protected OpenMetadataArchive getOpenMetadataArchive()
    {
        ConnectorType              connectorType;

        CassandraDataStoreProvider cassandraDataStoreProvider = new CassandraDataStoreProvider();
        connectorType = cassandraDataStoreProvider.getConnectorType();

        super.addConnectorType(connectorType.getGUID(),
                               connectorType.getQualifiedName(),
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());



        AvroFileStoreProvider avroFileStoreProvider = new AvroFileStoreProvider();
        connectorType = avroFileStoreProvider.getConnectorType();

        super.addConnectorType(connectorType.getGUID(),
                               connectorType.getQualifiedName(),
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());

        BasicFileStoreProvider basicFileStoreProvider = new BasicFileStoreProvider();
        connectorType = basicFileStoreProvider.getConnectorType();

        super.addConnectorType(connectorType.getGUID(),
                               connectorType.getQualifiedName(),
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());

        CSVFileStoreProvider csvFileStoreProvider = new CSVFileStoreProvider();
        connectorType = csvFileStoreProvider.getConnectorType();

        super.addConnectorType(connectorType.getGUID(),
                               connectorType.getQualifiedName(),
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());

        DataFolderProvider dataFolderProvider = new DataFolderProvider();
        connectorType = dataFolderProvider.getConnectorType();

        super.addConnectorType(connectorType.getGUID(),
                               connectorType.getQualifiedName(),
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());

        return super.getOpenMetadataArchive();
    }
}
